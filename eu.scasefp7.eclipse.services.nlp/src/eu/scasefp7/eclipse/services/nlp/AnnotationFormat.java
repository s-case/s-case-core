package eu.scasefp7.eclipse.services.nlp;
    
/**
 * Annotation output formats supported by the NLP service
 * @author Marin Orlic
 */
public enum AnnotationFormat {
    
    /** ANN format */
    ANN("ann"),
    /** TTL format */
    TTL("ttl");

    private String textValue;
    
    AnnotationFormat(String text) {
        this.textValue = text;
    }

    public String toString() {
        return this.textValue;
    }
    
    /**
     * Creates the annotation from format string (ann/ttl).
     * 
     * @param text format string
     * @return annotation
     */
    public static AnnotationFormat fromString(String text) {
        if (text != null) {
            for (AnnotationFormat a : AnnotationFormat.values()) {
                if (text.equalsIgnoreCase(a.textValue)) {
                    return a;
                }
            }
        }
        return null;
    }
} 

