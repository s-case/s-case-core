package eu.scasefp7.eclipse.core.ui.preferences;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.dialogs.PropertyPage;

import eu.scasefp7.eclipse.core.ui.preferences.internal.DomainEntry;
import eu.scasefp7.eclipse.core.ui.preferences.internal.IProjectDomains;

public class ProjectDomainPropertyPage extends PropertyPage {

	private static final String DOMAIN_PROPERTY = "eu.scasefp7.eclipse.core.projectDomain";
	private static final int DOMAIN_DEFAULT = -1;
	
	private Label domainLabel;

	/**
	 * Constructor for SamplePropertyPage.
	 */
	public ProjectDomainPropertyPage() {
		super();
		setMessage("");
	}

	private void addDomainItems(Tree tree, int selectedId) {
		
		for(DomainEntry d : IProjectDomains.PROJECT_DOMAINS) {
			TreeItem treeitem = new TreeItem(tree, SWT.NONE);
			treeitem.setData(d);
			treeitem.setText(d.getName());
			
			for(DomainEntry c : d.getChildren()) {
				TreeItem child = new TreeItem(treeitem, SWT.NONE);
				child.setData(c);
				child.setText(c.getName());
				
				if(selectedId == c.getId()) {
					//child.getParentItem().setExpanded(true);
					tree.setSelection(child);
					updateDomainLabel(child);
				}
			}
		}
	}

	/**
	 * Read the configured properties and set the label
	 * @return 
	 */
	private int loadProperties() {
		// Populate domain label
		try {
			String domain = ((IResource) getElement()).getPersistentProperty(new QualifiedName("", DOMAIN_PROPERTY));
			if(domain != null) {
				return Integer.parseInt(domain);
			}
		} catch (CoreException | NumberFormatException e) {
			return DOMAIN_DEFAULT;
		}
		
		return DOMAIN_DEFAULT;
	}

	private void addDomainLabel(Composite parent, Object data) {
		Composite cmpLabels = new Composite(parent, SWT.NONE);
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
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout gl_composite = new GridLayout();
		gl_composite.marginWidth = 0;
		gl_composite.marginHeight = 0;
		composite.setLayout(gl_composite);
		GridData data = new GridData(GridData.FILL);
		data.grabExcessHorizontalSpace = true;
		composite.setLayoutData(data);

		int domainId = loadProperties();
		
		// Add the domain label
		addDomainLabel(composite, (domainId != DOMAIN_DEFAULT) ? domainId : null);
		
		// Add tree
		final Tree tree = new Tree(composite, SWT.BORDER | SWT.FULL_SELECTION);
		tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		//cmpTree.setContent(tree);
		//cmpTree.setMinSize(tree.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		tree.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(tree.getSelectionCount() > 0) {
					TreeItem[] selection = tree.getSelection();
					TreeItem item = selection[0];
					updateDomainLabel(item);
				}
			}
		});
		
		// Add the list of domains
		addDomainItems(tree, domainId);

		return composite;
	}

	protected void performDefaults() {
		super.performDefaults();
		// Populate the owner text field with the default value
//		ownerText.setText(DEFAULT_OWNER);
	}
	
	public boolean performOk() {
		// Store the value in properties
		try {
			DomainEntry de = (DomainEntry) domainLabel.getData();
			if(de != null) {
				((IResource) getElement()).setPersistentProperty(new QualifiedName("", DOMAIN_PROPERTY), Integer.toString(de.getId()));
			}
		} catch (CoreException e) {
			return false;
		}
		return true;
	}

	/* (non-Javadoc)
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
	private void updateDomainLabel(TreeItem selection) {
		TreeItem parent = selection.getParentItem();
		
		if(parent != null) {
			String text = parent.getText() + "/" + selection.getText();
			
			// Escape text for SWT
			domainLabel.setText(text.replaceAll("&", "&&"));
			domainLabel.setData(selection.getData());
		}
	}

}