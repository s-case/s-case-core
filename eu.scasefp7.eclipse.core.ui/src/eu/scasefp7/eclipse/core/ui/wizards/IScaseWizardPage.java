package eu.scasefp7.eclipse.core.ui.wizards;

import org.eclipse.core.resources.IResource;
import org.eclipse.jface.wizard.IWizardPage;

public interface IScaseWizardPage extends IWizardPage {
	
	public boolean performFinish(IResource resource);
	
}
