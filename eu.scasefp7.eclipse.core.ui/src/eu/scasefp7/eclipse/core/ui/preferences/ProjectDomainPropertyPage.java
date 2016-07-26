package eu.scasefp7.eclipse.core.ui.preferences;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.FilteredTree;
import org.eclipse.ui.dialogs.PatternFilter;
import org.eclipse.ui.dialogs.PropertyPage;

import eu.scasefp7.eclipse.core.builder.ProjectUtils;
import eu.scasefp7.eclipse.core.ui.Activator;
import eu.scasefp7.eclipse.core.ui.ScaseUiConstants;
import eu.scasefp7.eclipse.core.ui.preferences.internal.DomainEntry;
import eu.scasefp7.eclipse.core.ui.preferences.internal.IProjectDomains;

/**
 * Configures the semantic domain of the project.
 * 
 * @author Marin Orlic
 */
public class ProjectDomainPropertyPage extends PropertyPage {

	private static final String DOMAIN_PROPERTY = ScaseUiConstants.PROP_PROJECT_DOMAIN;
	private static final int DOMAIN_DEFAULT = -1;
	
	private Label domainLabel;

	private boolean locked;
	
	private Composite composite;
	private Composite cmpLabels;
	
	protected DomainFilteredTree filteredTree;
	protected TreeViewer treeViewer;
	
	protected class DomainFilteredTree extends FilteredTree {
		private ViewerFilter viewerFilter;

		DomainFilteredTree(Composite parent, int treeStyle, PatternFilter filter) {
			super(parent, treeStyle, filter, true);
		}

		protected void addFilter(ViewerFilter filter) {
			viewerFilter = filter;
			getViewer().addFilter(filter);

			if (filterText != null) {
				setFilterText("type filter text");
				textChanged();
			}
		}

		protected void updateToolbar(boolean visible) {
			super.updateToolbar((viewerFilter != null) || (visible));
		}

		protected void clearText() {
			setFilterText("");

			if ((!locked) && (viewerFilter != null)) {
				getViewer().removeFilter(viewerFilter);
				viewerFilter = null;
			}
			textChanged();
		}
	}
	
	/**
	 * Constructor for ProjectDomainPropertyPage.
	 * Sets the message and description.
	 */
	public ProjectDomainPropertyPage() {
		super();
		setMessage("Project domain");
		setDescription("Configures project semantical domain");
	}
	
	protected void setContentAndLabelProviders(TreeViewer treeViewer)
	{
		treeViewer.setLabelProvider(new DomainBoldLabelProvider(filteredTree));
		treeViewer.setContentProvider(new DomainContentProvider());
	}
	

	/**
	 * Read the configured properties and set the label
	 * @return domain code
	 */
	private int loadProperties() {
		// Populate domain label
		IProject project = ((IProject) getElement().getAdapter(IResource.class));
		return ProjectUtils.getProjectDomain(project);
	}

	protected void selectSavedItem()
	{
		DomainEntry[] domains = IProjectDomains.PROJECT_DOMAINS;
		treeViewer.setInput(domains);

		int domainId = loadProperties();
		
		DomainEntry domain = findDomainById(domains, domainId);
		if (domain != null) {
			treeViewer.setSelection(new StructuredSelection(domain), true);
		}
		
		// Set focus to tree or the filter control
		if (treeViewer.getTree().getItemCount() > 1) {
			Text filterText = filteredTree.getFilterControl();
			if (filterText != null) {
				filterText.setFocus();
			}
		} else {
			treeViewer.getControl().setFocus();
		}
	}
	
	
	private DomainEntry findDomainById(DomainEntry[] domains, int domainId) {
		for (DomainEntry de : domains) {
			if (de.getId() == domainId) {
				return de;
			}
			if(de.hasChildren()) {
				for (DomainEntry child : de.getChildren()) {
					if(child.getId() == domainId) {
						return child;
					}
				}
			}
		}
		return null;
	}
	
	
	protected void createDomainLabel(Composite parent, Object data) {
		cmpLabels = new Composite(parent, SWT.NONE);
		GridLayout gl_cmpLabels = new GridLayout(2, false);
		gl_cmpLabels.marginWidth = 0;
		cmpLabels.setLayout(gl_cmpLabels);
		cmpLabels.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		
		// Add label
		Label noteLabel = new Label(cmpLabels, SWT.NONE);
		noteLabel.setText("Project do&main:");
		
		domainLabel = new Label(cmpLabels, SWT.NONE);
		domainLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		domainLabel.setText("(select a domain)");
		domainLabel.setData(data);
	}

	/**
	 * @see PreferencePage#createContents(Composite)
	 */
	protected Control createContents(Composite parent) {
		composite = new Composite(parent, SWT.NONE);
		GridLayout gl_composite = new GridLayout();
		gl_composite.marginWidth = 0;
		gl_composite.marginHeight = 0;
		composite.setLayout(gl_composite);
		GridData data = new GridData(GridData.FILL);
		data.grabExcessHorizontalSpace = true;
		composite.setLayoutData(data);

		int domainId = loadProperties();
		
		// Add the domain label
		createDomainLabel(composite, (domainId != DOMAIN_DEFAULT) ? domainId : null);
		
		// Add tree
		treeViewer = createTreeViewer(composite);
		
		// Load the saved property
		selectSavedItem();

		return composite;
	}

	protected TreeViewer createTreeViewer(Composite parent) {
		TreeViewer tree;

		filteredTree = new DomainFilteredTree(parent, SWT.FILL, new PatternFilter());
		filteredTree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
//		horizontalIndent = 7;
		filteredTree.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_LIST_BACKGROUND));

		tree = filteredTree.getViewer();

		setContentAndLabelProviders(tree);
		tree.setInput(IProjectDomains.PROJECT_DOMAINS);

		tree.addSelectionChangedListener(new ISelectionChangedListener() {

			public void selectionChanged(SelectionChangedEvent event) {
				handleTreeSelectionChanged(event);
			}

		});
		
		//super.addListeners(tree);
		return tree;
	}
	
	public Label getDomainLabel(){
		return domainLabel;
	}
	
	protected DomainEntry getSingleSelection(ISelection selection)
	{
	  if (!selection.isEmpty()) {
	    IStructuredSelection structured = (IStructuredSelection)selection;
	    if (structured.getFirstElement() instanceof DomainEntry) {
	      return (DomainEntry)structured.getFirstElement();
	    }
	  }
	  return null;
	}
	
	protected void handleTreeSelectionChanged(SelectionChangedEvent event) {
		DomainEntry de = getSingleSelection(event.getSelection());
		updateDomainLabel(de);
	}

	protected void performDefaults() {
		super.performDefaults();
		// Populate the owner text field with the default value
//		ownerText.setText(DEFAULT_OWNER);
	}
	
	public boolean performOk() {
		DomainEntry de = (DomainEntry) domainLabel.getData();
        if(de != null) {
        	IAdaptable element = getElement();
        	IProject project = null;
        	if(element instanceof IResource) {
        	    project = (IProject) getElement();
        	} else {
        	    Object resource = element.getAdapter(IResource.class);
        	    if (resource instanceof IResource) {
        	        project = (IProject) resource; 
        	    } else {
        	        Activator.log("Unable to adapt to project: " + element.toString(), null);
        	    }
        	}
        	
        	if (project != null) {
        	    ProjectUtils.setProjectDomain(project, de.getId());
        	}
        }
		return true;
	}

	/**
	 * {@inheritDoc}
	 * @see org.eclipse.jface.preference.PreferencePage#isValid()
	 */
	@Override
	public boolean isValid() {
		// TODO Auto-generated method stub
		return super.isValid();
	}

	/**
	 * @param selection
	 */
	private void updateDomainLabel(DomainEntry domain) {
		if(domain == null) {
			return; 
		}
		
		DomainEntry parent = domain.getParent();
		
		if(parent != null) {
			String text = parent.getName() + "/" + domain.getName();
			
			// Escape text for SWT
			domainLabel.setText(text.replaceAll("&", "&&")); //$NON-NLS-1$ //$NON-NLS-2$
			domainLabel.setData(domain);
		}
	}
}