package de.uniba.dsg.jaxrs.resources;

import de.uniba.dsg.jaxrs.models.dto.*;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import de.uniba.dsg.jaxrs.Configuration;
import de.uniba.dsg.jaxrs.controllers.BeverageServices;
import de.uniba.dsg.jaxrs.model.Bottle;
import java.util.List;
import java.util.logging.Logger;

@Path("beverages")
public class BeverageResources{
	private static final Logger logger = Logger.getLogger("Beverage Services");
	@GET
	@Path("/all")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getBeverages(@Context final UriInfo info) {
		logger.info("Get all beverages");
		BeverageServices backend = new BeverageServices(Configuration.getDBHandlerUri());
		try{
			List<Bottle> bottles = backend.getBeverages();
			return Response.ok(bottles).build();
		}catch (ProcessingException e){
			return Response.status(400).entity("DB-Handler is not reachable").build();
		}
	}

	@GET
	@Path("/bottles/{bevid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getBeverage(@Context final UriInfo uriInfo, @PathParam("bevid") final int bevid) {
		logger.info("Get Beverage with Id: " + bevid);
		final Bottle m = BeverageServices.instance.getBeverageById(bevid);
		if (m == null) {
			logger.info("Beverage with Id: " + bevid + "not Found!");
			return Response.status(Response.Status.NOT_FOUND).build();
		}
		return Response.ok(new BottleDTO(m, uriInfo.getBaseUri())).build();
	}



}
