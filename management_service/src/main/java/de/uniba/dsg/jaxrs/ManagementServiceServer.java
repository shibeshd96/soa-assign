package de.uniba.dsg.jaxrs;

import jakarta.ws.rs.core.UriBuilder;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Properties;
import java.util.Scanner;

import com.sun.net.httpserver.HttpServer;
import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

public class ManagementServiceServer {
	private static final Properties properties = Configuration.loadProperties();

	public static void main(String[] args) throws IOException {
		String serverUri = properties.getProperty("serverUri");

		URI baseUri = UriBuilder.fromUri(serverUri).build();
		ResourceConfig config = ResourceConfig.forApplicationClass(ExamplesApi.class);
		HttpServer server = JdkHttpServerFactory.createHttpServer(baseUri, config);
		System.out.println("Server started at: " + serverUri); // Display the URL
		System.out.println("Server ready to serve your JAX-RS requests...");
		System.out.println("Press any key to exit...");
		// Call the method to consume the db_handler API
		consumeDbHandlerApi();
		System.in.read();
		System.out.println("Stopping server");
		server.stop(1);
	}

	public static void consumeDbHandlerApi() throws IOException {
		String dbHandlerBaseUrl = "http://db:9999/v1";
		URL url = new URL(dbHandlerBaseUrl);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setRequestMethod("POST");

		int responseCode = conn.getResponseCode(); // Declare the responseCode variable here

		if (responseCode == HttpURLConnection.HTTP_OK) {
			// Read the response from the input stream
			Scanner scanner = new Scanner(conn.getInputStream());
			StringBuilder response = new StringBuilder();
			while (scanner.hasNextLine()) {
				response.append(scanner.nextLine());
			}
			scanner.close();

			// Now you have the JSON response containing the list of beverages in the 'response' variable
			// You can parse the JSON and handle the data as needed
			System.out.println("Response from db_handler API: " + response.toString());
		} else {
			// Handle error cases here
			System.out.println("Failed to get response from db_handler API. Response code: " + responseCode);
		}

		conn.disconnect();
	}
}