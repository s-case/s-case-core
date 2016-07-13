package eu.scasefp7.eclipse.core.connect.uploader;

import java.io.File;
import java.util.ArrayList;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.QualifiedName;

import eu.scasefp7.eclipse.core.connect.Activator;

public class ProjectFileLoader {

	private static IContainer getProjectFolder(IProject project, String folderId) {
		String folderLocation = null;
		try {
			folderLocation = project.getPersistentProperty(new QualifiedName("", folderId));
		} catch (CoreException e) {
			Activator.log("Error retrieving project property (folder location)", e);
		}
		IContainer container = project;
		if (folderLocation != null) {
			if (project.findMember(new Path(folderLocation)).exists())
				container = (IContainer) project.findMember(new Path(folderLocation));
		}
		return container;
	}

	/**
	 * Finds the files of a container recursively.
	 * 
	 * @param container a workspace container (e.g. project or folder).
	 * @param files a list of files that is populated.
	 * @param extension the extension of the files that are to be retrieved.
	 */
	private static void processContainer(IContainer container, ArrayList<IFile> files, String extension) {
		try {
			IResource[] members = container.members();
			for (IResource member : members) {
				if (member instanceof IContainer) {
					processContainer((IContainer) member, files, extension);
				} else if (member instanceof IFile) {
					if (extension.equals("") || ((IFile) member).getName().endsWith("." + extension))
						files.add((IFile) member);
				}
			}
		} catch (CoreException e) {
			Activator.log("Error finding the files of a project", e);
		}
	}

	/**
	 * Returns the files of the given project.
	 * 
	 * @param project the project of which the files are returned.
	 * @param extensions the extensions of the returned files.
	 * @return a list of the files of the project with the given extension.
	 */
	protected static ArrayList<IFile> getFilesOfProject(IProject project, String... extensions) {
		ArrayList<IFile> allfiles = new ArrayList<IFile>();
		for (String extension : extensions) {
			ArrayList<IFile> files = new ArrayList<IFile>();
			IContainer container = null;
			if (extension.equals("rqs") || extension.equals("sbd") || extension.equals("uml"))
				container = getProjectFolder(project, "eu.scasefp7.eclipse.core.ui.rqsFolder");
			else if (extension.equals("scd") || extension.equals("sc") || extension.equals("cservice"))
				container = getProjectFolder(project, "eu.scasefp7.eclipse.core.ui.compFolder");
			else if (extension.equals("owl") || extension.equals("yaml"))
				container = getProjectFolder(project, "eu.scasefp7.eclipse.core.ui.modelsFolder");
			else if (extension.equals("xmi"))
				container = getProjectFolder(project, "eu.scasefp7.eclipse.core.ui.outputFolder");
			processContainer(container, files, extension);
			allfiles.addAll(files);
		}
		return allfiles;
	}

	private static void processContainer(File container, ArrayList<File> files, String extension) {
		for (File member : container.listFiles()) {
			if (member.isDirectory()) {
				processContainer(member, files, extension);
			} else {
				if (extension.equals("") || member.getName().endsWith("." + extension))
					files.add(member);
			}
		}
	}

	public static ArrayList<File> getFilesOfProject(String projectFolder, String... extensions) {
		ArrayList<File> allfiles = new ArrayList<File>();
		for (String extension : extensions) {
			ArrayList<File> files = new ArrayList<File>();
			processContainer(new File(projectFolder), files, extension);
			allfiles.addAll(files);
		}
		return allfiles;
	}

}
