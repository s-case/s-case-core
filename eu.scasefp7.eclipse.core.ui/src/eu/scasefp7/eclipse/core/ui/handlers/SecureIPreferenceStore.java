package eu.scasefp7.eclipse.core.ui.handlers;

import org.eclipse.core.runtime.preferences.IScopeContext;
import org.eclipse.equinox.security.storage.ISecurePreferences;
import org.eclipse.equinox.security.storage.SecurePreferencesFactory;
import org.eclipse.equinox.security.storage.StorageException;
import org.eclipse.ui.preferences.ScopedPreferenceStore;

public class SecureIPreferenceStore extends ScopedPreferenceStore {

	private static final ISecurePreferences SECURE_PREFERENCES = SecurePreferencesFactory
			.getDefault();

	public SecureIPreferenceStore(IScopeContext context, String qualifier) {
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
		Boolean result = false;
		try {
			result = SECURE_PREFERENCES.getBoolean(name, result);
		} catch (StorageException e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public double getDouble(String name) {
		Double result = 0.0;
		try {
			result = SECURE_PREFERENCES.getDouble(name, result);
		} catch (StorageException e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public float getFloat(String name) {
		Float result = 0.0f;
		try {
			result = SECURE_PREFERENCES.getFloat(name, result);
		} catch (StorageException e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public int getInt(String name) {
		int result = 0;
		try {
			result = SECURE_PREFERENCES.getInt(name, result);
		} catch (StorageException e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public long getLong(String name) {
		Long result = 0L;
		try {
			result = SECURE_PREFERENCES.getLong(name, result);
		} catch (StorageException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	@Override
	public String getString(String name) {
		String result = "";
		try {
			result = SECURE_PREFERENCES.get(name, "");
		} catch (StorageException e) {
			e.printStackTrace();
		}
		return result;
	}
}