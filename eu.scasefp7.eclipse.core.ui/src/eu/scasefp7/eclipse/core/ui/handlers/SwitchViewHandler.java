package eu.scasefp7.eclipse.core.ui.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import eu.scasefp7.eclipse.core.ui.ScaseUiConstants;



public class SwitchViewHandler extends AbstractHandler{

	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {


		try {
			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(ScaseUiConstants.SC_VIEW_ID);
		} catch (PartInitException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		return null;
	}
}
