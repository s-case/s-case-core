package eu.scasefp7.eclipse.core.connect.uploader;

import java.io.File;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
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

/**
 * Class used to upload a project to the assets registry.
 * 
 * @author themis
 */
public class ProjectUploader {

	/**
	 * Uploads an Eclipse project to the assets registry.
	 * 
	 * @param project the project to be uploaded.
	 * @param monitor the progress monitor used to track the progress.
	 */
	public static void uploadProject(IProject project, IProgressMonitor monitor) {
		String projectName = project.getName();

		// Find the files of the project
		ArrayList<IFile> files = ProjectFileLoader.getFilesOfProject(project, "rqs", "sbd", "uml", "owl", "xmi");
		monitor.beginTask("Uploading artefacts", files.size());

		// Initialize connection to the assets registry
		Client client = connectToAssetsRegistry();
		if (client != null) {
			// Create or update project
			createOrUpdateProject(client, projectName);

			// Create all files
			for (IFile file : files) {
				monitor.worked(1);
				uploadFile(client, projectName, file.getName(), StringFileHelpers.readFileToString(file));
			}
		}
		monitor.done();
	}

	/**
	 * Uploads a project to the assets registry.
	 * 
	 * @param projectFolder the folder of the project to be uploaded.
	 * @param monitor the command line progress monitor used to track the progress.
	 */
	public static void uploadProject(String projectFolder, CommandLineProgressMonitor monitor) {
		String projectName = new File(projectFolder).getName();

		// Find the files of the project
		ArrayList<File> files = ProjectFileLoader.getFilesOfProject(projectFolder, "rqs", "sbd", "uml", "owl", "xmi");
		monitor.beginTask("Uploading artefacts", files.size());

		// Initialize connection to the assets registry
		Client client = connectToAssetsRegistry();
		if (client != null) {
			// Create or update project
			createOrUpdateProject(client, projectName);

			// Create all files
			for (File file : files) {
				monitor.worked(1);
				uploadFile(client, projectName, file.getName(), StringFileHelpers.readFileToString(file));
			}
		}
		monitor.done();
	}

	/**
	 * Builds and returns a jersey client that accepts SSL certificates.
	 * For more info check: http://stackoverflow.com/a/33500913
	 * 
	 * @return a new jersey client instance.
	 */
	private static Client getClient() {
		Client client = null;
		try {
			SSLContext sc = SSLContext.getInstance("TLSv1");
			sc.init(null, new TrustManager[] { new X509TrustManager() {
				public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
				}

				public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
				}

				public X509Certificate[] getAcceptedIssuers() {
					return new X509Certificate[0];
				}
			} }, new java.security.SecureRandom());
			client = ClientBuilder.newBuilder().sslContext(sc).hostnameVerifier(null).build();
		} catch (NoSuchAlgorithmException | KeyManagementException e) {
			throw new RuntimeException("Failed to create client");
		}
		return client;
	}

	/**
	 * Tests the connection to the assets registry and returns a {@link Client} instance.
	 * 
	 * @return a {@link Client} instance upon connecting to the assets registry.
	 */
	private static Client connectToAssetsRegistry() {
		Client client = getClient();
		Response response = RESTHelpers.makeRestRequest("GET", client, "/version");
		if (response != null) {
			if (response.getStatus() != 200)
				throw new RuntimeException("Failed to get version");
			else
				return client;
		}
		return null;
	}

	/**
	 * Creates or updates a project in the assets registry. Update is implemented by first deleting the project and then
	 * re-adding it. Note that when deleting a project, its artefacts are also deleted.
	 * 
	 * @param client the client used to connect to the assets registry.
	 * @param projectName the name of the project to be created or updated.
	 */
	private static void createOrUpdateProject(Client client, String projectName) {
		Response response;
		// Delete the project if it already exists
		response = RESTHelpers.makeRestRequest("GET", client, "/project/" + projectName);
		if (response.getStatus() == 200) {
			// Project already exists, so delete it
			// Note that when deleting a project, its artefacts are also deleted
			response = RESTHelpers.makeRestRequest("DELETE", client, "/project/" + projectName);
		}

		// Create project
		// @formatter:off
		String json = "{" + "	\"name\": \"" + projectName + "\"" + "}";
		// @formatter:on
		response = RESTHelpers.makeRestRequest("POST", client, "/project", json);
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

	/**
	 * Uploads a file as an artefact to the assets registry.
	 * 
	 * @param client the client used to connect to the assets registry.
	 * @param projectName the name of the project for which the file is uploaded.
	 * @param filename the name of the file that is uploaded.
	 * @param fileContents the contents of the file that is uploaded.
	 */
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
			response = RESTHelpers.makeRestRequest("POST", client, "/artefact", json);
		// System.out.println("Create artefact status: " + response.getStatus());
		// System.out.println(Entity.json(json).toString());
		// System.out.print(rqsFile.getName() + "\t" + byteArray.length() + "\t" + bytes.length + "\t");
		// System.out.println(response.getHeaders());
		// System.out.println(response.getMediaType());
		// System.out.println(response.getStatusInfo());
	}

	/**
	 * Function used to test this uploader.
	 * 
	 * @param args the location of the main folder of the project to be uploaded.
	 */
	public static void main(String[] args) {
		uploadProject(args[0], new CommandLineProgressMonitor());
	}
}
