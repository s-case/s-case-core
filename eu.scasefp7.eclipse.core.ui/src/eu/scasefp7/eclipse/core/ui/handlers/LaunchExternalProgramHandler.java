package eu.scasefp7.eclipse.core.ui.handlers;


import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.core.ILaunchesListener2;
import org.eclipse.swt.widgets.Display;



public class LaunchExternalProgramHandler extends AbstractHandler implements ILaunchesListener2 {

	private Set<ILaunch> allLaunches = new HashSet<ILaunch>();
	ILaunchManager manager;

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		try {
			manager = DebugPlugin.getDefault().getLaunchManager();
			manager.addLaunchListener(this);
			
			ILaunchConfigurationType type = manager
			  .getLaunchConfigurationType("org.eclipse.ui.externaltools.ProgramLaunchConfigurationType");
			
			ILaunchConfiguration[] configurations = manager
			  .getLaunchConfigurations(type);
			
			for (int i = 0; i < configurations.length; i++) {
				 ILaunchConfiguration configuration = configurations[i];
				 if (configuration.getName().equals("Gen doc")) {
					  configuration.delete();
					  break;
				 }
			}
			
			ILaunchConfigurationWorkingCopy workingCopy = type.newInstance( null, "Gen doc");
			String location   = event.getParameter("eu.scasefp7.eclipse.core.ui.location");
			String arguments  = event.getParameter("eu.scasefp7.eclipse.core.ui.arguments");
			String workingDir = event.getParameter("eu.scasefp7.eclipse.core.ui.workingDirectory");
			
			
			workingCopy.setAttribute("org.eclipse.ui.externaltools.ATTR_LOCATION", location);
			workingCopy.setAttribute("org.eclipse.ui.externaltools.ATTR_TOOL_ARGUMENTS", arguments);	
			workingCopy.setAttribute("org.eclipse.ui.externaltools.ATTR_WORKING_DIRECTORY", workingDir);
			
			ILaunch launch = workingCopy.launch(ILaunchManager.RUN_MODE, new NullProgressMonitor());

			allLaunches.add(launch);
			
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public void launchesRemoved(ILaunch[] launches) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void launchesAdded(ILaunch[] launches) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void launchesTerminated(ILaunch[] launches) {
		for (ILaunch launch : launches) {
			   allLaunches.remove(launch);
			   Display.getDefault().asyncExec(new Runnable() {
			    @Override
			    public void run() { }
			   });

			 }
	}

	@Override
	public void launchesChanged(ILaunch[] launches) {
		// TODO Auto-generated method stub
		
	}
	

}
