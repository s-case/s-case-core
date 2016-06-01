package eu.scasefp7.eclipse.core.connect.uploader;

import java.io.File;
import java.util.ArrayList;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;

public class ProjectUploader {

	private static final String baseURL = "http://109.231.121.125:8080/s-case/assetregistry";

	public static void uploadProject(IProject project) {
		String projectName = project.getName();

		// Initialize connection to the assets registry
		Client client = connectToAssetsRegistry();

		// Create or update project
		createOrUpdateProject(client, projectName);

		// Create rqs files
		ArrayList<IFile> rqsFiles = ProjectFileLoader.getFilesOfProject(project, "rqs");
		for (IFile rqsFile : rqsFiles) {
			uploadFile(client, projectName, rqsFile);
		}

		// Create sbd files
		ArrayList<IFile> sbdFiles = ProjectFileLoader.getFilesOfProject(project, "sbd");
		for (IFile sbdFile : sbdFiles) {
			uploadFile(client, projectName, sbdFile);
		}
	}

	public static void uploadProject(String projectFolder) {
		String projectName = new File(projectFolder).getName();

		// Initialize connection to the assets registry
		Client client = connectToAssetsRegistry();

		// Create or update project
		createOrUpdateProject(client, projectName);

		// Create rqs files
		ArrayList<File> rqsFiles = ProjectFileLoader.getFilesOfProject(projectFolder, "rqs");
		for (File rqsFile : rqsFiles) {
			uploadFile(client, projectName, rqsFile);
		}

		// Create sbd files
		ArrayList<File> sbdFiles = ProjectFileLoader.getFilesOfProject(projectFolder, "sbd");
		for (File sbdFile : sbdFiles) {
			uploadFile(client, projectName, sbdFile);
		}

	}

	private static Client connectToAssetsRegistry() {
		Client client = ClientBuilder.newClient();
		Response response = client.target(baseURL + "/version").request().get();
		if (response.getStatus() != 200)
			throw new RuntimeException("Failed to get version");
		else
			return client;
	}

	private static void createOrUpdateProject(Client client, String projectName){
		Response response;
		// Delete the project if it already exists
		// response = client.target(baseURL + "/project/" + projectName).request().get();
		// if (response.getStatus() == 200) {
		// Project already exists, so delete it
		response = client.target(baseURL + "/project/" + projectName).request().delete();
		// }

		// Create project
		String json =  "{"
					 + "	\"name\": \"" + projectName + "\""
					 + "}";
		response = client.target(baseURL + "/project").request().post(Entity.json(json));
	}

	private static void uploadFile(Client client, String projectName, File file) {
		uploadFile(client, projectName, file.getName(), StringFileHelpers.readFileToString(file));
	}

	private static void uploadFile(Client client, String projectName, IFile file) {
		uploadFile(client, projectName, file.getName(), StringFileHelpers.readFileToString(file));
	}

	private static void uploadFile(Client client, String projectName, String filename, String fileContents) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	public static void main(String[] args) {
		uploadProject(args[0]);
	}
}
