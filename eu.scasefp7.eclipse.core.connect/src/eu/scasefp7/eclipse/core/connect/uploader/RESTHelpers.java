package eu.scasefp7.eclipse.core.connect.uploader;

import java.util.HashMap;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.IPreferencesService;
import org.eclipse.equinox.security.storage.ISecurePreferences;
import org.eclipse.equinox.security.storage.SecurePreferencesFactory;
import org.eclipse.equinox.security.storage.StorageException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import com.auth0.jwt.JWTSigner;

import eu.scasefp7.eclipse.core.connect.Activator;

/**
 * Class that contains helper functions for issuing REST requests.
 * 
 * @author themis
 */
public class RESTHelpers {

	/**
	 * Exception class used to denote that the provided credentials are wrong.
	 */
	@SuppressWarnings("serial")
	private static class WrongCredentialsException extends Exception {
		public WrongCredentialsException() {
			super();
		}
	}

	/**
	 * Makes a REST request to the assets registry and returns the response.
	 * 
	 * @param type the type of the request, one of {@code "POST"}, {@code "GET"}, and {@code "DELETE"}.
	 * @param client the client used to connect to the assets registry.
	 * @param requestURI the URI where the request is issued.
	 * @return the response of the request.
	 */
	public static Response makeRestRequest(String type, Client client, String requestURI) {
		return makeRestRequest(type, client, requestURI, null);
	}

	/**
	 * Makes a REST request with JSON body to the assets registry and returns the response.
	 * 
	 * @param type the type of the request, one of {@code "POST"}, {@code "GET"}, and {@code "DELETE"}.
	 * @param client the client used to connect to the assets registry.
	 * @param requestURI the URI where the request is issued.
	 * @param json the JSON request body to be sent.
	 * @return the response of the request.
	 */
	public static Response makeRestRequest(String type, Client client, String requestURI, String json) {
		try {
			return makeServerRequest(true, type, client, requestURI, json);
		} catch (WrongCredentialsException e) {
			showErrorMessage(Platform.getPreferencesService() != null);
			return null;
		} catch (StorageException e) {
			Activator.log("There is a problem with the secure storage", e);
			return null;
		}
	}

	/**
	 * Retrieves a parameter from the secure preferences store.
	 * 
	 * @param parameterName the name of the parameter.
	 * @param parameterDefaultValue the default value if the parameter is not found or if it does not have a value.
	 * @return the value of the parameter given.
	 * @throws StorageException if there is a problem with the way the parameter is stored.
	 */
	private static String getParameterFromSecureStore(String parameterName, String parameterDefaultValue)
			throws StorageException {
		ISecurePreferences securePreferences;
		try {
			securePreferences = SecurePreferencesFactory.getDefault();
		} catch (NullPointerException e) {
			securePreferences = null;
		}
		ISecurePreferences securePreferencesService = securePreferences != null ? securePreferences
				.node("eu.scasefp7.eclipse.core.ui") : null;
		return securePreferencesService != null ? securePreferencesService.get(parameterName, parameterDefaultValue)
				: parameterDefaultValue;
	}

	/**
	 * Retrieves a parameter from the preferences store.
	 * 
	 * @param parameterName the name of the parameter.
	 * @param parameterDefaultValue the default value if the parameter is not found or if it does not have a value.
	 * @return the value of the parameter given.
	 */
	private static String getParameterFromPreferenceStore(String parameterName, String parameterDefaultValue) {
		IPreferencesService preferencesService = Platform.getPreferencesService();
		return preferencesService != null ? preferencesService.getString("eu.scasefp7.eclipse.core.ui", parameterName,
				parameterDefaultValue, null) : parameterDefaultValue;
	}

	/**
	 * Revceives the S-CASE token and secret and creates the authorization header.
	 * 
	 * @param SCASEToken the S-CASE token.
	 * @param SCASESecret the S-CASE secret.
	 * @return an authorization header as a string.
	 * @throws WrongCredentialsException if the credentials are not correct.
	 */
	private static String getSignedCredentials(String SCASEToken, String SCASESecret) throws WrongCredentialsException {
		JWTSigner signer = new JWTSigner(SCASESecret);
		HashMap<String, Object> claims = new HashMap<String, Object>();
		claims.put("token", SCASEToken);
		if (claims.get("token") == "") {
			throw new WrongCredentialsException();
		}
		String signature = signer.sign(claims);
		return "CT-AUTH " + SCASEToken + ":" + signature;
	}

	/**
	 * Inner function that issues a REST request with JSON body returns the response.
	 * 
	 * @param useControlTower boolean denoting whether to use the control tower.
	 * @param type the type of the request, one of {@code "POST"}, {@code "GET"}, and {@code "DELETE"}.
	 * @param client the client used to connect to the assets registry.
	 * @param requestURI the URI where the request is issued.
	 * @param json the JSON request body to be sent.
	 * @return the response of the request.
	 * @throws WrongCredentialsException if the credentials are not correct.
	 * @throws StorageException if there is a problem with the way the credentials are stored.
	 */
	private static Response makeServerRequest(boolean useControlTower, String type, Client client, String requestURI,
			String json) throws WrongCredentialsException, StorageException {
		if (useControlTower) {
			String CTAddress = getParameterFromSecureStore("controlTowerServiceURI", "https://app.scasefp7.com/");
			String AssetsRegistryServerAddress = CTAddress + "api/proxy/assetregistry";
			String SCASEToken = getParameterFromSecureStore("controlTowerServiceToken", "");
			String SCASESecret = getParameterFromSecureStore("controlTowerServiceSecret", "");
			String credentials = getSignedCredentials(SCASEToken, SCASESecret);
			if (type.equals("POST"))
				return client.target(AssetsRegistryServerAddress + requestURI).request()
						.header("AUTHORIZATION", credentials).post(Entity.json(json));
			else if (type.equals("GET"))
				return client.target(AssetsRegistryServerAddress + requestURI).request()
						.header("AUTHORIZATION", credentials).get();
			else if (type.equals("DELETE"))
				return client.target(AssetsRegistryServerAddress + requestURI).request()
						.header("AUTHORIZATION", credentials).delete();
			else
				return null;
		} else {
			String AssetsRegistryServerAddress = getParameterFromPreferenceStore("assetRegistryServiceURI",
					"http://109.231.121.125:8080/s-case/assetregistry");
			if (type.equals("POST"))
				return client.target(AssetsRegistryServerAddress + requestURI).request().post(Entity.json(json));
			else if (type.equals("GET"))
				return client.target(AssetsRegistryServerAddress + requestURI).request().get();
			else if (type.equals("DELETE"))
				return client.target(AssetsRegistryServerAddress + requestURI).request().delete();
			else
				return null;
		}
	}

	/**
	 * Show an error message to the user.
	 * 
	 * @param isEclipse boolean to select to open window in Eclipse ({@code true}) or standalone ({@code false}).
	 */
	public static void showErrorMessage(boolean isEclipse) {
		if (isEclipse) {
			PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
				public void run() {
					Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
					MessageBox dialog = new MessageBox(shell, SWT.ICON_ERROR);
					dialog.setText("Authorization problem");
					dialog.setMessage("Please provide a valid S-CASE token and a valid S-CASE secret");
					dialog.open();
				}
			});
		} else {
			Shell shell = new Shell();
			MessageBox dialog = new MessageBox(shell, SWT.ICON_ERROR);
			dialog.setText("Authorization problem");
			dialog.setMessage("Please provide a valid S-CASE token and a valid S-CASE secret");
			dialog.open();
		}
	}

}
