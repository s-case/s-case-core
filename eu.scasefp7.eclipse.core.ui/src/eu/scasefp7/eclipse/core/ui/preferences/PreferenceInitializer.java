package eu.scasefp7.eclipse.core.ui.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import eu.scasefp7.eclipse.core.ui.Activator;
import eu.scasefp7.eclipse.core.ui.ScaseUiConstants;

/**
 * Class used to initialize default preference values.
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

	@Override
    public void initializeDefaultPreferences() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();

	    store.setDefault(PreferenceConstants.P_PROJECT_DOMAIN, ScaseUiConstants.PROP_PROJECT_DOMAIN_DEFAULT);
		store.setDefault(PreferenceConstants.P_USE_PROJECT_PREFS, false);
		store.setDefault(PreferenceConstants.P_NLP_ENDPOINT, "http://nlp.scasefp7.eu:8010/"); //$NON-NLS-1$
	}
}
