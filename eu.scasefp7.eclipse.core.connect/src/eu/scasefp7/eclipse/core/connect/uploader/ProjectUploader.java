package eu.scasefp7.eclipse.core.connect.uploader;

import java.io.File;
import java.io.IOException;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public class ProjectUploader {

	private static final String baseURL = "http://109.231.121.125:8080/s-case/assetregistry";

	public static void uploadProject(IProject project) {
		String projectName = project.getName();

		// Initialize connection to the assets registry
		Client client = connectToAssetsRegistry();

		// Create or update project
		createOrUpdateProject(client, projectName);

		// Create rqs files
		for (IFile rqsFile : ProjectFileLoader.getFilesOfProject(project, "rqs"))
			uploadFile(client, projectName, rqsFile);

		// Create sbd files
		for (IFile sbdFile : ProjectFileLoader.getFilesOfProject(project, "sbd"))
			uploadFile(client, projectName, sbdFile);

		// Create uml files
		for (IFile umlFile : ProjectFileLoader.getFilesOfProject(project, "uml"))
			uploadFile(client, projectName, umlFile);

		// Create xmi files
		for (IFile xmiFile : ProjectFileLoader.getFilesOfProject(project, "xmi"))
			uploadFile(client, projectName, xmiFile);

	}

	public static void uploadProject(String projectFolder) {
		String projectName = new File(projectFolder).getName();

		// Initialize connection to the assets registry
		Client client = connectToAssetsRegistry();

		// Create or update project
		createOrUpdateProject(client, projectName);

		// Create rqs files
		for (File rqsFile : ProjectFileLoader.getFilesOfProject(projectFolder, "rqs"))
			uploadFile(client, projectName, rqsFile);

		// Create sbd files
		for (File sbdFile : ProjectFileLoader.getFilesOfProject(projectFolder, "sbd"))
			uploadFile(client, projectName, sbdFile);

		// Create uml files
		for (File umlFile : ProjectFileLoader.getFilesOfProject(projectFolder, "uml"))
			uploadFile(client, projectName, umlFile);

		// Create xmi files
		for (File xmiFile : ProjectFileLoader.getFilesOfProject(projectFolder, "xmi"))
			uploadFile(client, projectName, xmiFile);

	}

	private static Client connectToAssetsRegistry() {
		Client client = ClientBuilder.newClient();
		Response response = client.target(baseURL + "/version").request().get();
		if (response.getStatus() != 200)
			throw new RuntimeException("Failed to get version");
		else
			return client;
	}

	private static void createOrUpdateProject(Client client, String projectName) {
		Response response;
		// Delete the project if it already exists
		response = client.target(baseURL + "/project/" + projectName).request().get();
		if (response.getStatus() == 200) {
			// Project already exists, so delete it
			// Note that when deleting a project, its artefacts are also deleted
			response = client.target(baseURL + "/project/" + projectName).request().delete();
		}

		// Create project
		// @formatter:off
		String json =  "{"
					 + "	\"name\": \"" + projectName + "\""
					 + "}";
		// @formatter:on
		response = client.target(baseURL + "/project").request().post(Entity.json(json));
	}

	private static void uploadFile(Client client, String projectName, File file) {
		uploadFile(client, projectName, file.getName(), StringFileHelpers.readFileToString(file));
	}

	private static void uploadFile(Client client, String projectName, IFile file) {
		uploadFile(client, projectName, file.getName(), StringFileHelpers.readFileToString(file));
	}

	/**
	 * Returns the diagram type of an XMI file.
	 * 
	 * @param fileContents the contents of the file of which the type is returned.
	 * @return the diagram type, either "UseCaseDiagram" or "ActivityDiagram" (or "Error" if there is a parsing error).
	 */
	private static String getDiagramType(String fileContents) {
		Document doc = null;
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			doc = docBuilder.parse(fileContents);
			Node root = doc.getDocumentElement();
			Node packagedElement = root.getFirstChild().getNextSibling();
			if (packagedElement.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) packagedElement;
				String type = eElement.getAttribute("xmi:type");
				if (type.equalsIgnoreCase("uml:Use Case"))
					return "UseCaseDiagram";
				else if (type.equalsIgnoreCase("uml:Activity"))
					return "ActivityDiagram";
			}
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
		return "Error";
	}

	private static void uploadFile(Client client, String projectName, String filename, String fileContents) {
		@SuppressWarnings("unused")
		Response response;
		String byteArray = StringFileHelpers.convertStringToBytes(fileContents);
		String extension = filename.lastIndexOf('.') > 0 ? filename.substring(filename.lastIndexOf('.') + 1) : "";
		String json = null;
		if (extension.equals("rqs")) {
			// @formatter:off
			json =  "{"
					+ "	\"name\": \"" + filename + "\","
					+ "	\"projectName\": \"" + projectName + "\","
					+ "	\"type\": \"TEXTUAL\","
					+ "	\"description\": \"" + filename + "\","
					+ "	\"payload\": ["
					+ "		{"
					+ "			\"type\": \"TEXTUAL\","
					+ "			\"name\": \"functional\","
					+ "			\"format\": \"TEXT_UTF8\","
					+ "			\"payload\": [" + byteArray + "]"
					+ "		}"
					+ "	]"
					+ "}";
			// @formatter:on
		} else if (extension.equals("sbd")) {
			// @formatter:off
			json =  "{"
					+ "	\"name\": \"" + filename + "\","
					+ "	\"projectName\": \"" + projectName + "\","
					+ "	\"type\": \"STORYBOARD\","
					+ "	\"description\": \"" + filename + "\","
					+ "	\"payload\": ["
					+ "		{"
					+ "			\"type\": \"TEXTUAL\","
					+ "			\"name\": \"storyboard\","
					+ "			\"format\": \"TEXT_UTF8\","
					+ "			\"payload\": [" + byteArray + "]"
					+ "		}"
					+ "	]"
					+ "}";
			// @formatter:on
		} else if (extension.equals("uml")) {
			// @formatter:off
			json =  "{"
					+ "	\"name\": \"" + filename + "\","
					+ "	\"projectName\": \"" + projectName + "\","
					+ "	\"type\": \"" + (getDiagramType(fileContents).equals("UseCaseDiagram") ? "USE_CASE":"ACTIVITY_DIAGRAM") + "\","
					+ "	\"description\": \"" + filename + "\","
					+ "	\"payload\": ["
					+ "		{"
					+ "			\"type\": \"TEXTUAL\","
					+ "			\"name\": \"uml_diagram\","
					+ "			\"format\": \"TEXT_UTF8\","
					+ "			\"payload\": [" + byteArray + "]"
					+ "		}"
					+ "	]"
					+ "}";
			// @formatter:on
		}
		if (json != null)
			response = client.target(baseURL + "/artefact").request().post(Entity.json(json));
		// System.out.println("Create artefact status: " + response.getStatus());
		// System.out.println(Entity.json(json).toString());
		// System.out.print(rqsFile.getName() + "\t" + byteArray.length() + "\t" + bytes.length + "\t");
		// System.out.println(response.getHeaders());
		// System.out.println(response.getMediaType());
		// System.out.println(response.getStatusInfo());
	}

	public static void main(String[] args) {
		uploadProject(args[0]);
	}
}
