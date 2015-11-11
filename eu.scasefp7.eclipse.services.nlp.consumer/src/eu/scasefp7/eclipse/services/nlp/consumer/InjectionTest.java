package eu.scasefp7.eclipse.services.nlp.consumer;

import javax.inject.Inject;

import eu.scasefp7.eclipse.services.nlp.AnnotationFormat;
import eu.scasefp7.eclipse.services.nlp.INLPServiceAsync;

/**
 * Test DI for DS :)
 * @author emaorli
 *
 */
public class InjectionTest {
  /**
   * DI-ed constructor.
   * @param service
   */
  @Inject
  public InjectionTest(INLPServiceAsync service) {
      System.out.println("############################");
      System.out.println(service);
      
      // Get completable future and when complete
      service.annotateSentenceAsync("Find systems which provide a search for computer products", "", AnnotationFormat.ANN).whenComplete(
          (result, exception) -> {
              // Check for exception and print out
              if (exception != null) {
                  System.out.println(exception.getMessage());
                  exception.printStackTrace();
              } else
                  // Success!
                  System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$Received response:  posts=" + result);
      });           
  }
    
}