package eu.scasefp7.eclipse.core.ui.wizards;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;

public class DeployWizard extends Wizard {
	protected DeployWizardPageOne one;
	protected DeployWizardPageTwo two;
	protected DeployWizardPageThree three;
	
	private static final String PAGE_ONE_NAME = "First page";
	private static final String PAGE_TWO_NAME = "Deploy generated service";
	private static final String PAGE_THREE_NAME = "Third page";
	private static final String WIZARD_NAME = "Service deployment";
	
	
	public DeployWizard() {
	    super();
	    setNeedsProgressMonitor(true);
	  }
	
	@Override
	  public String getWindowTitle() {
	    return WIZARD_NAME;
	  }


	@Override
	  public void addPages() {
	    one = new DeployWizardPageOne(PAGE_ONE_NAME);
	    two = new DeployWizardPageTwo(PAGE_TWO_NAME);
	    three = new DeployWizardPageThree(PAGE_THREE_NAME);
//	    addPage(one);
	    addPage(two);
//	    addPage(three);
	  }

	
	@Override
	public boolean performFinish() {
		// TODO Auto-generated method stub
		return true;
	}
	@Override
	public IWizardPage getNextPage(IWizardPage page) {

		if(one.getNextPageName() == "two")
			return two;
		if(one.getNextPageName() == "three")
				return three;
		
		return super.getNextPage(page);
	}

}
