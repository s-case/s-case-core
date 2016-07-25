package eu.scasefp7.eclipse.core.ui.preferences;

import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import eu.scasefp7.eclipse.core.ui.Activator;

/**
 * Configures the infrastructure services endpoints.
 */

public class ControlTowerAccessPreferencePage extends FieldEditorOverlayPage implements IWorkbenchPreferencePage {

	private static final String PAGE_ID = "eu.scasefp7.eclipse.core.ui.preferencePages.ControlTowerAccessPreferencePage";

    /**
     * Construct the page.
     */
    public ControlTowerAccessPreferencePage() {
		super(GRID);
        setPreferenceStore(Activator.getDefault().getSecurePreferenceStore());
    }
	
	/**
	 * Creates the field editors. Field editors are abstractions of
	 * the common GUI blocks needed to manipulate various types
	 * of preferences. Each field editor knows how to save and
	 * restore itself.
	 */
	public void createFieldEditors() {
	    addField(new StringFieldEditor(PreferenceConstants.P_CONTROLTOWER_ENDPOINT,
                "Control Tower &URI:", getFieldEditorParent()));
        addField(new StringFieldEditor(PreferenceConstants.P_CONTROLTOWER_TOKEN, "Control Tower &Token:",
				getFieldEditorParent()) {
			@Override
			protected void doFillIntoGrid(Composite parent, int numColumns) {
				super.doFillIntoGrid(parent, numColumns);
				getTextControl().setEchoChar('*');
			}			
		});
        addField(new StringFieldEditor(PreferenceConstants.P_CONTROLTOWER_SECRET, "Control Tower &Secret:",
				getFieldEditorParent()) {
			@Override
			protected void doFillIntoGrid(Composite parent, int numColumns) {
				super.doFillIntoGrid(parent, numColumns);
				getTextControl().setEchoChar('*');
			}
		});
	}

	
	/**
	 * {@inheritDoc}
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init(IWorkbench workbench) {
        setDescription("Configure Control Tower endpoint and authentication");
	}

	/**
	 * Returns the page ID for the overlay preference/properties to work.  
	 */
    @Override
    protected String getPageId() {
        return PAGE_ID;
    }

    @Override
    protected String getPreferenceQualifier() {
        return Activator.PLUGIN_ID;
    }
}