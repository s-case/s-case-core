package eu.scasefp7.eclipse.core.ui.preferences;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.IWizardPage;
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
import eu.scasefp7.eclipse.core.ui.preferences.internal.DomainEntry;
import eu.scasefp7.eclipse.core.ui.preferences.internal.IProjectDomains;

/**
 * @author Marin Orlic
 *
 */
public class ProjectDomainPropertyAndWizardPage extends PropertyPage implements IWizardPage {

	private static final int DOMAIN_DEFAULT = -1;
	
	private Label domainLabel;

	private boolean locked;
	
	private Composite composite;
	private Composite cmpLabels;
	
	protected DomainFilteredTree filteredTree;
	protected TreeViewer treeViewer;
	
	/**
     * This page's name.
     */
    private String name;

    /**
     * The wizard to which this page belongs; <code>null</code>
     * if this page has yet to be added to a wizard.
     */
    private IWizard wizard = null;

    /**
     * Indicates whether this page is complete.
     */
    private boolean isPageComplete = true;

    /**
     * The page that was shown right before this page became visible;
     * <code>null</code> if none.
     */
    private IWizardPage previousPage = null;

	
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
	public ProjectDomainPropertyAndWizardPage() {
		super();
		setMessage("Project domain");
		setDescription("Configures project semantical domain");
	}
	
    /**
     * Creates a new wizard page with the given name, and
     * with no title or image.
     *
     * @param pageName the name of the page
     */
    protected ProjectDomainPropertyAndWizardPage(String pageName) {
        this(pageName, null, (ImageDescriptor) null);
    }

    /**
     * Creates a new wizard page with the given name, title, and image.
     *
     * @param pageName the name of the page
     * @param title the title for this wizard page,
     *   or <code>null</code> if none
     * @param titleImage the image descriptor for the title of this wizard page,
     *   or <code>null</code> if none
     */
    protected ProjectDomainPropertyAndWizardPage(String pageName, String title,
            ImageDescriptor titleImage) {
        super();
        this.setTitle(title);
        this.setImageDescriptor(titleImage);
        Assert.isNotNull(pageName); // page name must not be null
        name = pageName;
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
	    if (getElement() == null) {
	        return DOMAIN_DEFAULT;
	    }
	    
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
	
	
	private void createDomainLabel(Composite parent, Object data) {
		cmpLabels = new Composite(parent, SWT.NONE);
		GridLayout glCmpLabels = new GridLayout(2, false);
		glCmpLabels.marginWidth = 0;
		cmpLabels.setLayout(glCmpLabels);
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
		GridLayout glComposite = new GridLayout();
		glComposite.marginWidth = 0;
		glComposite.marginHeight = 0;
		composite.setLayout(glComposite);
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
	
	protected DomainEntry getSingleSelection(ISelection selection)
	{
	  if (!selection.isEmpty()) {
	    IStructuredSelection structured = (IStructuredSelection)selection;
	    if ((structured.getFirstElement() instanceof DomainEntry)) {
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
            ProjectUtils.setProjectDomain((IProject) getElement(), de.getId());
        }
		return true;
	}

	/**
	 * @param selection
	 */
	private void updateDomainLabel(DomainEntry domain) {
		if(domain == null)
			return; 
		
		DomainEntry parent = domain.getParent();
		
		if(parent != null) {
			String text = parent.getName() + "/" + domain.getName();
			
			// Escape text for SWT
			domainLabel.setText(text.replaceAll("&", "&&")); //$NON-NLS-1$ //$NON-NLS-2$
			domainLabel.setData(domain);
		}
	}

    @Override
    public void createControl(Composite parent) {
        super.createControl(parent);
//        createContents(parent);
        setControl(parent);
    }
	
    @Override
    public boolean canFlipToNextPage() {
        return isPageComplete() && getNextPage() != null;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public IWizardPage getNextPage() {
        if (wizard == null) {
            return null;
        }
        return wizard.getNextPage(this);
    }

    @Override
    public IWizardPage getPreviousPage() {
        if (previousPage != null) {
            return previousPage;
        }

        if (wizard == null) {
            return null;
        }

        return wizard.getPreviousPage(this);
    }

    @Override
    public IWizard getWizard() {
        return wizard;
    }

    @Override
    public boolean isPageComplete() {
        return isPageComplete;
    }

    @Override
    public void setPreviousPage(IWizardPage page) {
        previousPage = page;
    }

    /**
     * Returns whether this page is the current one in the wizard's container.
     *
     * @return <code>true</code> if the page is active,
     *  and <code>false</code> otherwise
     */
    protected boolean isCurrentPage() {
        return (getContainer() != null 
                && getContainer() == wizard.getContainer()
                && this == wizard.getContainer().getCurrentPage());
    }

    
    /**
     * The <code>WizardPage</code> implementation of this method 
     * declared on <code>DialogPage</code> updates the container
     * if this is the current page.
     */
    @Override
    public void setMessage(String newMessage, int newType) {
        super.setMessage(newMessage, newType);
        if (isCurrentPage()) { // TODO
            getContainer().updateMessage();
        }
    }
    
    /**
     * The <code>WizardPage</code> implementation of this <code>IDialogPage</code>
     * method extends the <code>DialogPage</code> implementation to update
     * the wizard container title bar. Subclasses may extend.
     */
    @Override
    public void setDescription(String description) {
        super.setDescription(description);
        if (wizard != null) {
            wizard.getContainer().updateTitleBar();
        }
    }
    
    @Override
    public void setWizard(IWizard newWizard) {
        wizard = newWizard;
    }

}