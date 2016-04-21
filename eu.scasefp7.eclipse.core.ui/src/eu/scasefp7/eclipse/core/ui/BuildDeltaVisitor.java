package eu.scasefp7.eclipse.core.ui;

import java.util.HashSet;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.QualifiedName;

/**
 * Visitor class used for checking whether a folder has been renamed or removed.
 * 
 * @author themis
 */
public class BuildDeltaVisitor implements IResourceDeltaVisitor {

	/**
	 * Receives an {@code IResourceDelta} as a parameter and returns its kind as a string. This function is only used
	 * for debugging purposes.
	 * 
	 * @param delta the {@code IResourceDelta} of a specific resource.
	 * @return the kind of delta.
	 */
	@SuppressWarnings("unused")
	private String deltaToString(IResourceDelta delta) {
		switch (delta.getKind()) {
		case IResourceDelta.REMOVED:
			return "REMOVED";
		case IResourceDelta.NO_CHANGE:
			return "NO_CHANGE";
		case IResourceDelta.ADDED:
			return "ADDED";
		case IResourceDelta.CHANGED:
			return "CHANGED";
		case IResourceDelta.ADDED_PHANTOM:
			return "ADDED_PHANTOM";
		case IResourceDelta.REMOVED_PHANTOM:
			return "REMOVED_PHANTOM";
		case IResourceDelta.ALL_WITH_PHANTOMS:
			return "ALL_WITH_PHANTOMS";
		case IResourceDelta.CONTENT:
			return "CONTENT";
		case IResourceDelta.MOVED_FROM:
			return "MOVED_FROM";
		case IResourceDelta.MOVED_TO:
			return "MOVED_TO";
		case IResourceDelta.COPIED_FROM:
			return "COPIED_FROM";
		case IResourceDelta.OPEN:
			return "OPEN";
		case IResourceDelta.TYPE:
			return "TYPE";
		case IResourceDelta.SYNC:
			return "SYNC";
		case IResourceDelta.MARKERS:
			return "MARKERS";
		case IResourceDelta.REPLACED:
			return "REPLACED";
		case IResourceDelta.DESCRIPTION:
			return "DESCRIPTION";
		case IResourceDelta.ENCODING:
			return "ENCODING";
		case IResourceDelta.LOCAL_CHANGED:
			return "LOCAL_CHANGED";
		case IResourceDelta.DERIVED_CHANGED:
			return "DERIVED_CHANGED";
		default:
			return "ERROR";
		}
	}

	@Override
	public boolean visit(IResourceDelta delta) throws CoreException {
		IResource resource = delta.getResource();
		if (resource instanceof IProject) {
			IProject project = (IProject) resource;
			if (project.isOpen() && project.hasNature("eu.scasefp7.eclipse.core.scaseNature")) {

				// Set some variables
				QualifiedName modelsFolder = new QualifiedName("", ScaseUiConstants.MODELS_FOLDER);
				QualifiedName outputFolder = new QualifiedName("", ScaseUiConstants.OUTPUT_FOLDER);
				QualifiedName requirementsFolder = new QualifiedName("", ScaseUiConstants.REQUIREMENTS_FOLDER);
				QualifiedName compositionsFolder = new QualifiedName("", ScaseUiConstants.COMPOSITIONS_FOLDER);
				String modelsPath = project.getPersistentProperty(modelsFolder);
				String outputPath = project.getPersistentProperty(outputFolder);
				String reqPath = project.getPersistentProperty(requirementsFolder);
				String comPath = project.getPersistentProperty(compositionsFolder);

				// Check all possible cases
				if (delta.getKind() == IResourceDelta.ADDED) {
					// A new project was added
					// Do nothing
				} else if (delta.getKind() == IResourceDelta.REMOVED) {
					// A project was removed
					// Do nothing
				} else if (delta.getKind() == IResourceDelta.CHANGED) {
					// A child of a project has been added, removed, or renamed
					IResourceDelta[] childrenDeltas = delta.getAffectedChildren();
					if (childrenDeltas != null && childrenDeltas.length > 0) {
						// Get the folder(s) that have been changed and split them according to the typoe of change
						HashSet<IResource> addedFolders = new HashSet<IResource>();
						HashSet<IResource> removedFolders = new HashSet<IResource>();
						HashSet<IResource> changedFolders = new HashSet<IResource>();
						for (IResourceDelta childDelta : childrenDeltas) {
							IResource childResource = childDelta.getResource();
							if (childResource instanceof IFolder) {
								if (childDelta.getKind() == IResourceDelta.ADDED)
									addedFolders.add(childResource);
								else if (childDelta.getKind() == IResourceDelta.REMOVED)
									removedFolders.add(childResource);
								else if (childDelta.getKind() == IResourceDelta.CHANGED)
									changedFolders.add(childResource);
							}
						}
						// If there are one removed folders and no added folders, then some folders have been deleted
						if (removedFolders.size() > 0 && addedFolders.isEmpty()) {
							for (IResource removedFolder : removedFolders) {
								String removedFolderPath = removedFolder.getProjectRelativePath().toString();
								if (removedFolderPath.equals(modelsPath))
									project.setPersistentProperty(modelsFolder, "");
								else if (removedFolderPath.equals(outputPath))
									project.setPersistentProperty(outputFolder, "");
								else if (removedFolderPath.equals(reqPath))
									project.setPersistentProperty(requirementsFolder, "");
								else if (removedFolderPath.equals(comPath))
									project.setPersistentProperty(compositionsFolder, "");
							}
						}
						// If there is exactly one removed folder and one added folder, then a folder has been renamed
						if (removedFolders.size() == 1 && addedFolders.size() == 1) {
							IResource addedFolder = addedFolders.iterator().next();
							IResource removedFolder = removedFolders.iterator().next();
							String removedFolderPath = removedFolder.getProjectRelativePath().toString();
							String addedFolderPath = addedFolder.getProjectRelativePath().toString();
							if (removedFolderPath.equals(modelsPath))
								project.setPersistentProperty(modelsFolder, addedFolderPath);
							else if (removedFolderPath.equals(outputPath))
								project.setPersistentProperty(outputFolder, addedFolderPath);
							else if (removedFolderPath.equals(reqPath))
								project.setPersistentProperty(requirementsFolder, addedFolderPath);
							else if (removedFolderPath.equals(comPath))
								project.setPersistentProperty(compositionsFolder, addedFolderPath);
						}

						// For changed folders it means that a resource inside a folder has been added/deleted/changed
						// So we do nothing

						// Return false to avoid visiting deeper deltas
						return false;
					}
				}
			}
		}
		return true;
	}

}
