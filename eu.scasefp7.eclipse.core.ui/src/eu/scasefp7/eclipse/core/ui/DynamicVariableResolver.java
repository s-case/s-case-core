package eu.scasefp7.eclipse.core.ui;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.variables.IDynamicVariable;
import org.eclipse.core.variables.IDynamicVariableResolver;
import org.eclipse.equinox.security.storage.ISecurePreferences;
import org.eclipse.equinox.security.storage.SecurePreferencesFactory;
import org.eclipse.equinox.security.storage.StorageException;
import org.eclipse.jface.preference.IPreferenceStore;

public class DynamicVariableResolver implements IDynamicVariableResolver {

	@Override
	public String resolveValue(IDynamicVariable variable, String argument)
			throws CoreException {
		
		String value = null;
		
		if(argument == "scase_fco_server_uuid"){
			Activator plugin = Activator.getDefault();
			IPreferenceStore prefStore = plugin.getPreferenceStore();
			value = prefStore.getString(argument);
			
			return value;
		}
		
		ISecurePreferences prefStore = SecurePreferencesFactory.getDefault();

		try {
			value = prefStore.get(argument, "");
		} catch (StorageException e) {
			e.printStackTrace();
		}

		if (value != null && value.length() > 0)
			return value;
		
		return null;
	}

}
