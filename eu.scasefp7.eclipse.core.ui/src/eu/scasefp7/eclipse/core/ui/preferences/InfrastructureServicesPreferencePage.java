package eu.scasefp7.eclipse.core.ui.preferences;

import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import eu.scasefp7.eclipse.core.ui.Activator;

/**
 * Configures the infrastructure services endpoints.
 */

public class InfrastructureServicesPreferencePage extends FieldEditorOverlayPage implements IWorkbenchPreferencePage {

	private static final String PAGE_ID = "eu.scasefp7.eclipse.core.ui.preferencePages.InfrastructureServicesPreferencePage";

    /**
     * Construct the page.
     */
    public InfrastructureServicesPreferencePage() {
		super(GRID);
        setPreferenceStore(Activator.getDefault().getPreferenceStore());
    }
	
	/**
	 * Creates the field editors. Field editors are abstractions of
	 * the common GUI blocks needed to manipulate various types
	 * of preferences. Each field editor knows how to save and
	 * restore itself.
	 */
	public void createFieldEditors() {
		addField(new StringFieldEditor(PreferenceConstants.P_NLP_ENDPOINT,
		        "NLP service &URI:", getFieldEditorParent()));
        addField(new StringFieldEditor(PreferenceConstants.P_UML_ENDPOINT,
                "UML extractor service &URI:", getFieldEditorParent()));
        addField(new StringFieldEditor(PreferenceConstants.P_ONTOREPO_ENDPOINT,
                "Ontology repository &URI:", getFieldEditorParent()));
        addField(new StringFieldEditor(PreferenceConstants.P_WSC_ENDPOINT,
                "Service composition server &URI:", getFieldEditorParent()));
	}

	
	/**
	 * {@inheritDoc}
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init(IWorkbench workbench) {
        setDescription("Infrastructure services preferences");
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