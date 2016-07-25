package eu.scasefp7.eclipse.core.ui.preferences;

import org.eclipse.core.runtime.preferences.IScopeContext;
import org.eclipse.equinox.security.storage.ISecurePreferences;
import org.eclipse.equinox.security.storage.SecurePreferencesFactory;
import org.eclipse.equinox.security.storage.StorageException;
import org.eclipse.ui.preferences.ScopedPreferenceStore;

import eu.scasefp7.eclipse.core.ui.Activator;

/**
 * Implements a secure preference store using Eclipse preference APIs.
 * 
 * @author Leonora Gaspar
 * @author Marin Orlic
 */
public class SecurePreferenceStore extends ScopedPreferenceStore {

    private static final ISecurePreferences SECURE_PREFERENCES = SecurePreferencesFactory.getDefault();
    
    protected ISecurePreferences preferences = null;


	/**
	 * Constructs the preference store focusing on the given context.
	 * 
	 * @param context
     *            the scope to store to
     * @param qualifier
     *            the qualifier used to look up the preference node
	 */
	public SecurePreferenceStore(IScopeContext context, String qualifier) {
		super(context, qualifier);
		
		// Open a qualified node
		this.preferences = SECURE_PREFERENCES.node(qualifier);
	}

	@Override
	public void setValue(String name, boolean value) {
		try {
			preferences.putBoolean(name, value, true);
		} catch (StorageException e) {
			Activator.log("Unable to store boolean preference.", e);
		}
	}

	@Override
	public void setValue(String name, double value) {
		try {
			preferences.putDouble(name, value, true);
		} catch (StorageException e) {
		    Activator.log("Unable to store double preference.", e);
		}
	}

	@Override
	public void setValue(String name, float value) {
		try {
			preferences.putFloat(name, value, true);
		} catch (StorageException e) {
		    Activator.log("Unable to store float preference.", e);
		}
	}

	@Override
	public void setValue(String name, int value) {
		try {
			preferences.putInt(name, value, true);
		} catch (StorageException e) {
		    Activator.log("Unable to store int preference.", e);
		}
	}

	@Override
	public void setValue(String name, long value) {
		try {
			preferences.putLong(name, value, true);
		} catch (StorageException e) {
		    Activator.log("Unable to store long preference.", e);
		}
	}

	@Override
	public void setValue(String name, String value) {
		try {
			preferences.put(name, value, true);
		} catch (StorageException e) {
		    Activator.log("Unable to store string preference.", e);
		}
	}

	@Override
	public boolean getBoolean(String name) {
		Boolean result = getDefaultBoolean(name);
		try {
			result = preferences.getBoolean(name, result);
		} catch (StorageException e) {
		    Activator.log("Unable to load boolean preference.", e);
		}
		return result;
	}

	@Override
	public double getDouble(String name) {
		Double result = getDefaultDouble(name);
		try {
			result = preferences.getDouble(name, result);
		} catch (StorageException e) {
		    Activator.log("Unable to load double preference.", e);
		}
		return result;
	}

	@Override
	public float getFloat(String name) {
		Float result = getDefaultFloat(name);
		try {
			result = preferences.getFloat(name, result);
		} catch (StorageException e) {
		    Activator.log("Unable to load float preference.", e);
		}
		return result;
	}

	@Override
	public int getInt(String name) {
		int result = getDefaultInt(name);
		try {
			result = preferences.getInt(name, result);
		} catch (StorageException e) {
		    Activator.log("Unable to load int preference.", e);
		}
		return result;
	}

	@Override
	public long getLong(String name) {
		Long result = getDefaultLong(name);
		try {
			result = preferences.getLong(name, result);
		} catch (StorageException e) {
		    Activator.log("Unable to load long preference.", e);
		}
		return result;
	}
	
	@Override
	public String getString(String name) {
		String result = getDefaultString(name);
		try {
			result = preferences.get(name, result);
		} catch (StorageException e) {
		    Activator.log("Unable to load string preference.", e);
		}
		return result;
	}
}