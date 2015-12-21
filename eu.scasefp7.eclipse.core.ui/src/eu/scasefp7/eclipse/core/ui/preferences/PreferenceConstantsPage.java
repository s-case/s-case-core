package eu.scasefp7.eclipse.core.ui.preferences;

import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import eu.scasefp7.eclipse.core.ui.handlers.SecureIPreferenceStore;

public class PreferenceConstantsPage extends FieldEditorOverlayPage implements
		IWorkbenchPreferencePage {

	private static final String PAGE_ID = "eu.scasefp7.eclipse.core.ui.preferencePages.PreferenceConstantsPage";

	public PreferenceConstantsPage() {
		super(GRID);
		setPreferenceStore(new SecureIPreferenceStore(ConfigurationScope.INSTANCE,"eu.scasefp7.eclipse.core.ui.preferences.secure"));
	}

	@Override
	public void createFieldEditors() {
		addField(new StringFieldEditor("scase_fco_cust_uuid", "Customer UUID:",
				getFieldEditorParent()));
		
		addField(new StringFieldEditor("scase_fco_cust_name", "Username:",
				getFieldEditorParent()));
		
		addField(new StringFieldEditor("scase_fco_cust_pwd", "Password",
				getFieldEditorParent()) {
			@Override
			protected void doFillIntoGrid(Composite parent, int numColumns) {
				super.doFillIntoGrid(parent, numColumns);
				Text text = getTextControl();
				String value = text.toString();
				getTextControl().setEchoChar('*');
				setStringValue(value);
			}
		});
		
		addField(new MultiLineTextFieldEditor("scase_fco_ssh_key", "SSH key:",
				getFieldEditorParent()));

	}

	@Override
	public void init(IWorkbench workbench) {
		setDescription("Preferences for arguments");
	}

	@Override
	protected String getPageId() {
		return PAGE_ID;
	}
}