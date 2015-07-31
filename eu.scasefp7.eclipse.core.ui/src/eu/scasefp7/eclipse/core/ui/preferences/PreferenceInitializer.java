package eu.scasefp7.eclipse.core.ui.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import eu.scasefp7.eclipse.core.ui.Activator;;

/**
 * Class used to initialize default preference values.
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#initializeDefaultPreferences()
	 */
	public void initializeDefaultPreferences() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		
		store.setDefault(PreferenceConstants.P_OUTPUT_PATH, "src"); //$NON-NLS-1$
		store.setDefault(PreferenceConstants.P_SERVICE_NAME, "SampleService"); //$NON-NLS-1$
		
		store.setDefault(PreferenceConstants.P_DATABASE_ADDRESS, "localhost"); //$NON-NLS-1$
	    store.setDefault(PreferenceConstants.P_DATABASE_PORT, 5432); //$NON-NLS-1$
	    store.setDefault(PreferenceConstants.P_DATABASE_USER, "postgres"); //$NON-NLS-1$
	    store.setDefault(PreferenceConstants.P_DATABASE_PASSWORD, "fp7s-case"); //$NON-NLS-1$
        
	    store.setDefault(PreferenceConstants.P_FACET_BASIC_AUTH, false); //$NON-NLS-1$
	    store.setDefault(PreferenceConstants.P_FACET_ABAC_AUTH, false); //$NON-NLS-1$
	    store.setDefault(PreferenceConstants.P_FACET_SEARCH, false); //$NON-NLS-1$
	    store.setDefault(PreferenceConstants.P_FACET_EXT_COMPOSITIONS, false); //$NON-NLS-1$
	}

}
