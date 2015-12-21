package eu.scasefp7.eclipse.core.ui.handlers;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.m2e.core.MavenPlugin;
import org.eclipse.m2e.core.project.MavenProjectInfo;
import org.eclipse.m2e.core.project.ProjectImportConfiguration;
import org.eclipse.m2e.core.project.IProjectConfigurationManager;
import org.apache.maven.model.Model;


public class ImportMavenProjectHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) {	
//		Get pom file location from parameter
//		String directory = event.getParameter("eu.scasefp7.eclipse.core.ui.projectDirectory");
//		if (directory.equals(null) || directory.equals(" "))
//			directory = "C:\\Users\\sc2015\\Desktop\\SCase\\mde\\pom.xml";
		
		File pomFile = new File("C:\\Users\\sc2015\\Desktop\\SCase\\mde\\eu.scasefp7.eclipse.mde.ui\\pom.xml");
		Model model  = new Model();
		
		IProgressMonitor monitor = new NullProgressMonitor();
		ProjectImportConfiguration config    = new ProjectImportConfiguration();
		Collection<MavenProjectInfo> infos   = new ArrayList<MavenProjectInfo>();
		IProjectConfigurationManager manager = MavenPlugin.getProjectConfigurationManager();
		
		String version = "1.0.0";
		String groupId = "groupID";
		String artifactId  = "eu.scasefp7.eclipse.mde.ui";
		String projectName = "eu.scasefp7.eclipse.mde.ui";
		
		model.setGroupId(groupId);
		model.setArtifactId(artifactId);
		model.setVersion(version);
		model.setPomFile(pomFile);
		
		MavenProjectInfo info = new MavenProjectInfo(projectName, pomFile, model, null);
		infos.add(info);

		try {
			manager.importProjects(infos, config , monitor);
		} catch (CoreException e) {
			e.printStackTrace();
		}
		
	return null;
	}

}
