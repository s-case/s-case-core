package eu.scasefp7.eclipse.core.ui;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.variables.IDynamicVariable;
import org.eclipse.core.variables.IDynamicVariableResolver;
import org.eclipse.jface.preference.IPreferenceStore;

public class DynamicVariableResolver implements IDynamicVariableResolver {

	@Override
	public String resolveValue(IDynamicVariable variable, String argument)
			throws CoreException {

		Activator plugin = Activator.getDefault();
		IPreferenceStore prefStore = plugin.getPreferenceStore();
		String value = prefStore.getString(argument);

		if (value != null && value.length() > 0)
			return value;
		return null;
	}

}
