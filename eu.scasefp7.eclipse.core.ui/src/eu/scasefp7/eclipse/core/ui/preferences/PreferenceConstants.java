package eu.scasefp7.eclipse.core.ui.preferences;

/**
 * Constant definitions for plug-in preferences.
 */
public final class PreferenceConstants {

    /** Used to store constants, cannot be instantiated. */
    private PreferenceConstants() {};
    
    /** Use project prefs to override global ones. */
    public static final String P_USE_PROJECT_PREFS = "useProjectPreferences"; //$NON-NLS-1$
    
    /** Endpoint for the NLP service. */
    public static final String P_NLP_ENDPOINT = "nlpServiceURI"; //$NON-NLS-1$
   
    /** Endpoint for the UML extractor service. */
    public static final String P_UML_ENDPOINT = "umlServiceURI"; //$NON-NLS-1$
   
    /** Endpoint for the Ontology repository. */
    public static final String P_ONTOREPO_ENDPOINT = "ontoRepoServiceURI"; //$NON-NLS-1$

    /** Endpoint for the Web service composition server. */
    public static final String P_WSC_ENDPOINT = "wscServiceURI"; //$NON-NLS-1$

    /** Endpoint for the Control tower. */
    public static final String P_CONTROLTOWER_ENDPOINT = "controlTowerServiceURI"; //$NON-NLS-1$

    /** Project semantic domain. */
    public static final String P_PROJECT_DOMAIN = "projectDomain"; //$NON-NLS-1$
    
}
