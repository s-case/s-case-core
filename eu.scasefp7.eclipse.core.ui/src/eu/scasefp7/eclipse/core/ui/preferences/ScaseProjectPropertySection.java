package eu.scasefp7.eclipse.core.ui.preferences;

import java.util.ArrayList;
import java.util.Arrays;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.eclipse.ui.views.properties.ResourcePropertySource;
import org.eclipse.ui.views.properties.tabbed.AdvancedPropertySection;

import eu.scasefp7.eclipse.core.ui.ScaseUiConstants;
import eu.scasefp7.eclipse.core.ui.preferences.internal.DomainEntry;
import eu.scasefp7.eclipse.core.ui.preferences.internal.IProjectDomains;

public class ScaseProjectPropertySection extends AdvancedPropertySection {
	IProject project;
	
    @Override
    public void setInput(IWorkbenchPart part, ISelection selection) {
        if (selection instanceof StructuredSelection) {
            Object firstElement = ((StructuredSelection)selection).getFirstElement();
            
            if (firstElement instanceof IResource && ! (firstElement instanceof IWorkspaceRoot)) {
            	project = ((IResource)firstElement).getProject();
            	final IProject theProject = project;
                ISelection selection2 = new StructuredSelection(new ResourcePropertySource(theProject) {

                    @Override
                    public IPropertyDescriptor[] getPropertyDescriptors() {
                        ArrayList<IPropertyDescriptor>  arrayList = new ArrayList<IPropertyDescriptor>();
                        try {
							if(project.hasNature("eu.scasefp7.eclipse.core.scaseNature")) {
								PropertyDescriptor pd1 =  new PropertyDescriptor("projectdomain", "Project domain");
								PropertyDescriptor pd2 =  new PropertyDescriptor("compositionsfolder", "Compositions folder");
								PropertyDescriptor pd3 =  new PropertyDescriptor("modelsfolder", "Models folder");
								PropertyDescriptor pd4 =  new PropertyDescriptor("outputfolder", "Output folder");
								PropertyDescriptor pd5 =  new PropertyDescriptor("requirementsfolder", "Requirements folder");
								
								pd1.setCategory("Domain");
								pd2.setCategory("Folders");
								pd3.setCategory("Folders");
								pd4.setCategory("Folders");
								pd5.setCategory("Folders");
								IPropertyDescriptor[] array = {pd1,pd2,pd3,pd4,pd5};
								arrayList.addAll(Arrays.asList(array));
							}
						} catch (CoreException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
                        
                      return arrayList.toArray(new IPropertyDescriptor[0]);
                    }

                    @Override
                    public Object getPropertyValue(Object id) {
                    	if(project.isAccessible()) {
            				if (id.equals("projectdomain")) {
            				    	String projDomain = null;
            						try {
            							String domainId = project.getPersistentProperty(new QualifiedName("",ScaseUiConstants.PROP_PROJECT_DOMAIN));
            							if(domainId == null || domainId.equals("-1"))
            								projDomain = "domain unset";
            							else{
            								DomainEntry de = findDomainById(IProjectDomains.PROJECT_DOMAINS, Integer.parseInt(domainId));
            								projDomain = de.getParent().getName() + "/" + de.getName();
            							}
            							
            						} catch (CoreException e) {
            							// TODO Auto-generated catch block
            							e.printStackTrace();
            						}
            				      return projDomain;
            				    }
            			    if (id.equals("outputfolder")) {
            			    	String outFolder = null;
            					try {
            						outFolder = project.getPersistentProperty(new QualifiedName("",ScaseUiConstants.OUTPUT_FOLDER));
            					} catch (CoreException e) {
            						// TODO Auto-generated catch block
            						e.printStackTrace();
            					}
            			      return outFolder;
            			    }
            			    if (id.equals("compositionsfolder")) {
            			    	String comFolder = null;
            					try {
            						comFolder = project.getPersistentProperty(new QualifiedName("",ScaseUiConstants.COMPOSITIONS_FOLDER));
            					} catch (CoreException e) {
            						// TODO Auto-generated catch block
            						e.printStackTrace();
            					}
            			      return comFolder;
            			    }
            			    if (id.equals("modelsfolder")) {
            			    	String modFolder = null;
            					try {
            						modFolder = project.getPersistentProperty(new QualifiedName("",ScaseUiConstants.MODELS_FOLDER));
            					} catch (CoreException e) {
            						// TODO Auto-generated catch block
            						e.printStackTrace();
            					}
            			      return modFolder;
            			    }
            			    if (id.equals("requirementsfolder")) {
            			    	String rqsFolder = null;
            					try {
            						rqsFolder = project.getPersistentProperty(new QualifiedName("",ScaseUiConstants.REQUIREMENTS_FOLDER));
            					} catch (CoreException e) {
            						// TODO Auto-generated catch block
            						e.printStackTrace();
            					}
            			      return rqsFolder;
            			    }
                    	 }

						return super.getPropertyValue(id);
						
                    }

                });
                super.setInput(part, selection2); 
            } else {
                super.setInput(part, selection);
            }
        }
    }
    private DomainEntry findDomainById(DomainEntry[] domains, int domainId) {
        for (DomainEntry de : domains) {
            if (de.getId() == domainId) {
                return de;
            }
            if(de.hasChildren()) {
                for (DomainEntry child : de.getChildren()) {
                    if(child.getId() == domainId) {
                        return child;
                    }
                }
            }
        }
        return null;
    }
}