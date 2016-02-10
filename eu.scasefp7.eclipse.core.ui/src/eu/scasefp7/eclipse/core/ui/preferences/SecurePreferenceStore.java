package eu.scasefp7.eclipse.core.ui.preferences;

import org.eclipse.core.runtime.preferences.IScopeContext;
import org.eclipse.equinox.security.storage.ISecurePreferences;
import org.eclipse.equinox.security.storage.SecurePreferencesFactory;
import org.eclipse.equinox.security.storage.StorageException;
import org.eclipse.ui.preferences.ScopedPreferenceStore;

/**
 * Implements a secure preference store using Eclipse preference APIs.
 * 
 * @author Leonora Gaspar
 * @author Marin Orlic
 */
public class SecurePreferenceStore extends ScopedPreferenceStore {

    private static final ISecurePreferences SECURE_PREFERENCES = SecurePreferencesFactory.getDefault();

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
	}

	@Override
	public void setValue(String name, boolean value) {
		try {
			SECURE_PREFERENCES.putBoolean(name, value, true);
		} catch (StorageException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void setValue(String name, double value) {
		try {
			SECURE_PREFERENCES.putDouble(name, value, true);
		} catch (StorageException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void setValue(String name, float value) {
		try {
			SECURE_PREFERENCES.putFloat(name, value, true);
		} catch (StorageException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void setValue(String name, int value) {
		try {
			SECURE_PREFERENCES.putInt(name, value, true);
		} catch (StorageException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void setValue(String name, long value) {
		try {
			SECURE_PREFERENCES.putLong(name, value, true);
		} catch (StorageException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void setValue(String name, String value) {
		try {
			SECURE_PREFERENCES.put(name, value, true);
		} catch (StorageException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean getBoolean(String name) {
		Boolean result = getDefaultBoolean(name);
		try {
			result = SECURE_PREFERENCES.getBoolean(name, result);
		} catch (StorageException e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public double getDouble(String name) {
		Double result = getDefaultDouble(name);
		try {
			result = SECURE_PREFERENCES.getDouble(name, result);
		} catch (StorageException e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public float getFloat(String name) {
		Float result = getDefaultFloat(name);
		try {
			result = SECURE_PREFERENCES.getFloat(name, result);
		} catch (StorageException e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public int getInt(String name) {
		int result = getDefaultInt(name);
		try {
			result = SECURE_PREFERENCES.getInt(name, result);
		} catch (StorageException e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public long getLong(String name) {
		Long result = getDefaultLong(name);
		try {
			result = SECURE_PREFERENCES.getLong(name, result);
		} catch (StorageException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	@Override
	public String getString(String name) {
		String result = getDefaultString(name);
		try {
			result = SECURE_PREFERENCES.get(name, result);
		} catch (StorageException e) {
			e.printStackTrace();
		}
		return result;
	}
}