package de.uniba.dsg.jaxrs.db;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import de.uniba.dsg.jaxrs.model.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class DB {
	private static final Logger logger = Logger.getLogger("Beverage Services");

	private static final String DATA_FILE_PATH = "data.json";
	private final Gson gson = new Gson();
	private List<Bottle> bottles;

	public DB() {
		loadDataFromFile();
	}

	// Method to load data from the data.json file
	private void loadDataFromFile() {
		try {
			Type bottleListType = new TypeToken<List<Bottle>>() {
			}.getType();

			JsonFileHandler<List<Bottle>> bottleFileHandler = new JsonFileHandler<>(DATA_FILE_PATH);

			bottles = bottleFileHandler.readData(bottleListType);

			if (bottles == null) {
				bottles = new ArrayList<>();
			}
		} catch (IOException e) {
			// Handle the exception (e.g., create new lists if the file is not found)
			bottles = new ArrayList<>();
			e.printStackTrace();
		}
	}

	private void saveDataToFile() {
		try {
			Type bottleListType = new TypeToken<List<Bottle>>() {
			}.getType();
			JsonFileHandler<List<Bottle>> bottleFileHandler = new JsonFileHandler<>(DATA_FILE_PATH);
			bottleFileHandler.writeData(bottles, bottleListType);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public List<Bottle> getBottles() {
		return this.bottles;
	}

	public Bottle getBeverageById(int bevid) {
		return this.bottles.stream().filter(b -> b.getId() == bevid).findFirst().orElse(null);
	}
}
