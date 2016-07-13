package eu.scasefp7.eclipse.core.connect.uploader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public class ProjectUploader {

	private static final String baseURL = "http://109.231.121.125:8080/s-case/assetregistry";

	public static void uploadProject(IProject project, IProgressMonitor monitor) {
		String projectName = project.getName();

		// Find the files of the project
		ArrayList<IFile> files = ProjectFileLoader.getFilesOfProject(project, "rqs", "sbd", "uml", "owl", "xmi");
		monitor.beginTask("Uploading artefacts", files.size());

		// Initialize connection to the assets registry
		Client client = connectToAssetsRegistry();

		// Create or update project
		createOrUpdateProject(client, projectName);

		// Create all files
		for (IFile file : files) {
			monitor.worked(1);
			uploadFile(client, projectName, file);
		}
		monitor.done();
	}

	public static void uploadProject(String projectFolder) {
		String projectName = new File(projectFolder).getName();

		// Find the files of the project
		ArrayList<File> files = ProjectFileLoader.getFilesOfProject(projectFolder, "rqs", "sbd", "uml", "owl", "xmi");

		// Initialize connection to the assets registry
		Client client = connectToAssetsRegistry();

		// Create or update project
		createOrUpdateProject(client, projectName);

		// Create all files
		for (File file : files)
			uploadFile(client, projectName, file);
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
	 * Returns the diagram type of a UML file.
	 * 
	 * @param fileContents the contents of the file of which the type is returned.
	 * @return the diagram type, either "USE_CASE" or "ACTIVITY_DIAGRAM" (or "Error" if there is a parsing error).
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
					return "USE_CASE";
				else if (type.equalsIgnoreCase("uml:Activity"))
					return "ACTIVITY_DIAGRAM";
			}
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
		return "Error";
	}

	/**
	 * Returns the ontology type of an OWL file.
	 * 
	 * @param filename the name of the file of which the type is returned.
	 * @return the ontology type, "STATIC_ONTOLOGY", "DYNAMIC_ONTOLOGY" or "AGGREGATED_ONTOLOGY" (or "Other").
	 */
	private static String getOntologyType(String filename) {
		if (filename.startsWith("Static"))
			return "STATIC_ONTOLOGY";
		else if (filename.startsWith("Dynamic"))
			return "DYNAMIC_ONTOLOGY";
		if (filename.startsWith("Linked"))
			return "AGGREGATED_ONTOLOGY";
		else
			return "Other";
	}

	/**
	 * Returns the model type of an XMI file.
	 * 
	 * @param filename the name of the file of which the type is returned.
	 * @return the diagram type, "CIM", "PIM" or "PSM" (or "Other").
	 */
	private static String getModelType(String filename) {
		if (filename.endsWith("CIM.xmi"))
			return "CIM";
		else if (filename.endsWith("PIM.xmi"))
			return "PIM";
		if (filename.endsWith("PSM.xmi"))
			return "PSM";
		else
			return "Other";
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
					+ "	\"description\": \"" + fileContents + "\","
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
					+ "	\"description\": \"" + fileContents + "\","
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
		} else if (extension.equals("uml") && !getDiagramType(fileContents).equals("Error")) {
			// @formatter:off
			json =  "{"
					+ "	\"name\": \"" + filename + "\","
					+ "	\"projectName\": \"" + projectName + "\","
					+ "	\"type\": \"" + getDiagramType(fileContents) + "\","
					+ "	\"description\": \"" + fileContents + "\","
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
		} else if (extension.equals("owl") && !getOntologyType(filename).equals("Other")) {
			// @formatter:off
			json =  "{"
					+ "	\"name\": \"" + filename + "\","
					+ "	\"projectName\": \"" + projectName + "\","
					+ "	\"type\": \"" + getOntologyType(filename)  + "\","
					+ "	\"description\": \"" + fileContents + "\","
					+ "	\"payload\": ["
					+ "		{"
					+ "			\"type\": \"TEXTUAL\","
					+ "			\"name\": \"owl_specification\","
					+ "			\"format\": \"TEXT_UTF8\","
					+ "			\"payload\": [" + byteArray + "]"
					+ "		}"
					+ "	]"
					+ "}";
			// @formatter:on
		} else if (extension.equals("xmi") && !getModelType(filename).equals("Other")) {
			// @formatter:off
			json =  "{"
					+ "	\"name\": \"" + filename + "\","
					+ "	\"projectName\": \"" + projectName + "\","
					+ "	\"type\": \"" + getModelType(filename) + "\","
					+ "	\"description\": \"" + fileContents + "\","
					+ "	\"payload\": ["
					+ "		{"
					+ "			\"type\": \"TEXTUAL\","
					+ "			\"name\": \"xmi_model\","
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
