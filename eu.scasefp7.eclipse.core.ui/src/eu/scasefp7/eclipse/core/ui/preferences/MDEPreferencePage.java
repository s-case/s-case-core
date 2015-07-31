package eu.scasefp7.eclipse.core.ui.preferences;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.IWorkbench;

import eu.scasefp7.eclipse.core.ui.Activator;

/**
 * 
 */

public class MDEPreferencePage
	extends FieldEditorPreferencePage
	implements IWorkbenchPreferencePage {

	public MDEPreferencePage() {
		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setDescription("MDE Code generation preferences");
	}
	
	/**
	 * Creates the field editors. Field editors are abstractions of
	 * the common GUI blocks needed to manipulate various types
	 * of preferences. Each field editor knows how to save and
	 * restore itself.
	 */
	public void createFieldEditors() {
		addField(new StringFieldEditor(PreferenceConstants.P_SERVICE_NAME, "Web service &name:", getFieldEditorParent()));
		addField(new DirectoryFieldEditor(PreferenceConstants.P_OUTPUT_PATH, "&Output path:", getFieldEditorParent()));

		addField(new StringFieldEditor(PreferenceConstants.P_DATABASE_ADDRESS, "&Database server address:", getFieldEditorParent()));
        addField(new IntegerFieldEditor(PreferenceConstants.P_DATABASE_PORT, "Database server &port:", getFieldEditorParent(), 5));
        addField(new StringFieldEditor(PreferenceConstants.P_DATABASE_USER, "Database &user name:", getFieldEditorParent()));
        addField(new StringFieldEditor(PreferenceConstants.P_DATABASE_PASSWORD, "Database pass&word:", getFieldEditorParent()));
        
		addField(new BooleanFieldEditor(PreferenceConstants.P_FACET_BASIC_AUTH, "Add &Basic authentication", getFieldEditorParent()));
		addField(new BooleanFieldEditor(PreferenceConstants.P_FACET_ABAC_AUTH, "Add &ABAC authentication", getFieldEditorParent()));
		addField(new BooleanFieldEditor(PreferenceConstants.P_FACET_BASIC_AUTH, "Add database &searching", getFieldEditorParent()));
		addField(new BooleanFieldEditor(PreferenceConstants.P_FACET_BASIC_AUTH, "Add &External compositions", getFieldEditorParent()));
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init(IWorkbench workbench) {
	}
	
}