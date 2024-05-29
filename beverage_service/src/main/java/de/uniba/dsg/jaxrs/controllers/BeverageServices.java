package de.uniba.dsg.jaxrs.controllers;

import java.util.List;
import java.util.logging.Logger;
import java.util.ArrayList;
import java.util.stream.Collectors;

import de.uniba.dsg.jaxrs.Configuration;
import de.uniba.dsg.jaxrs.db.DB;
import de.uniba.dsg.jaxrs.model.Bottle;
import de.uniba.dsg.jaxrs.models.dto.BottleDTO;
import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

public class BeverageServices{
	private static final Logger logger = Logger.getLogger("Beverage Services");
	private final String uri;
	public static final BeverageServices instance = new BeverageServices(Configuration.getDBHandlerUri());


	public BeverageServices(String uri) {
		if ("".equals(uri)) {
			this.uri = "http://localhost:9999/v1";
		}
		else
			this.uri = uri;
		logger.info("Using rest backend implementation with "+ this.uri);
	}

	public List<Bottle> getBeverages() throws ProcessingException {
		Client client = ClientBuilder.newClient();
		Response response = client.target(this.uri)
				.path("/beverages/all")
				.request(MediaType.APPLICATION_JSON)
				.get();

		if (response.getStatus() == 200) {
			return response.readEntity(new GenericType<List<BottleDTO>>() {
					})
					.stream()
					.map(x -> x.unmarshall())
					.collect(Collectors.toList());
		} else {
			logger.info("Error in fetching all bottles from DB-Handler Status code " + response.getStatus());
		}

		List<Bottle> list = new ArrayList<>();
		return list;
	}

	public Bottle getBeverageById(final int bevid) throws ProcessingException {
		Client client = ClientBuilder.newClient();
		Response response = client.target(this.uri)
				.path("/beverages/bottles/" +bevid+ "")
				.request(MediaType.APPLICATION_JSON)
				.get();

		switch (response.getStatus()){
			case 200:
				return response.readEntity(new GenericType<Bottle>() { });
			case 404:
				return null;
			default:
				logger.info("Error in  bottle from DB-Handler Status code " + response.getStatus());
		}

		return null;
	}

}
