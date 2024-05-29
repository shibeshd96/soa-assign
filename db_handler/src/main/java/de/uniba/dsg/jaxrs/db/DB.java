package de.uniba.dsg.jaxrs.db;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import de.uniba.dsg.jaxrs.model.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;

public class DB {
	private static final Logger logger = Logger.getLogger("Beverage Services");

	private static final String DATA_FILE_PATH = "data.json";
	private final Gson gson = new Gson();
	private List<Bottle> bottles;
	private List<Crate> crates;
	private List<Order> orders;

	public DB() {
		loadDataFromFile();
	}

	// Method to load data from the data.json file
	private void loadDataFromFile() {
		try {
			Type bottleListType = new TypeToken<List<Bottle>>() {
			}.getType();
			Type crateListType = new TypeToken<List<Crate>>() {
			}.getType();
			Type orderListType = new TypeToken<List<Order>>() {
			}.getType();

			JsonFileHandler<List<Bottle>> bottleFileHandler = new JsonFileHandler<>(DATA_FILE_PATH);
			JsonFileHandler<List<Crate>> crateFileHandler = new JsonFileHandler<>(DATA_FILE_PATH);
			JsonFileHandler<List<Order>> orderFileHandler = new JsonFileHandler<>(DATA_FILE_PATH);

			bottles = bottleFileHandler.readData(bottleListType);
			crates = crateFileHandler.readData(crateListType);
			orders = orderFileHandler.readData(orderListType);

			if (bottles == null) {
				bottles = new ArrayList<>();
			}
			if (crates == null) {
				crates = new ArrayList<>();
			}
			if (orders == null) {
				orders = new ArrayList<>();
			}
		} catch (IOException e) {
			// Handle the exception (e.g., create new lists if the file is not found)
			bottles = new ArrayList<>();
			crates = new ArrayList<>();
			orders = new ArrayList<>();
			e.printStackTrace();
		}
	}

	private void saveDataToFile() {
		try {
			Type bottleListType = new TypeToken<List<Bottle>>() {
			}.getType();
			JsonFileHandler<List<Bottle>> bottleFileHandler = new JsonFileHandler<>(DATA_FILE_PATH);
			bottleFileHandler.writeData(bottles, bottleListType);

			Type crateListType = new TypeToken<List<Crate>>() {
			}.getType();
			JsonFileHandler<List<Crate>> crateFileHandler = new JsonFileHandler<>(DATA_FILE_PATH);
			crateFileHandler.writeData(crates, crateListType);

			Type orderListType = new TypeToken<List<Order>>() {
			}.getType();
			JsonFileHandler<List<Order>> orderFileHandler = new JsonFileHandler<>(DATA_FILE_PATH);
			orderFileHandler.writeData(orders, orderListType);
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

	public void add(final Bottle newBottle) {
		newBottle.setId(this.bottles.stream().map(Bottle::getId).max(Comparator.naturalOrder()).orElse(0) + 1);
		this.bottles.add(newBottle);
		saveDataToFile(); // Call saveDataToFile() after adding a bottle
	}

	public boolean deleteBottle(int bevid) {
		final Bottle b = this.getBeverageById(bevid);
		boolean removed = this.bottles.remove(b);
		if (removed) {
			saveDataToFile(); // Call saveDataToFile() after deleting a bottle
		}
		return removed;
	}

	public Bottle updateBottle(Bottle bottle) {
		int index = bottle.getId() - 1;
		this.bottles.set(index, bottle);
		saveDataToFile(); // Call saveDataToFile() after updating a bottle
		return bottle;
	}

	public List<Bottle> getFilteredBottles(int maxfilter, int minfilter) {
		List<Bottle> filteredBottles = new ArrayList<>();
		for (Bottle a : this.bottles) {
			if (a.getPrice() < maxfilter && a.getPrice() > minfilter) {
				filteredBottles.add(a);
			}
		}
		return filteredBottles;
	}

	// Crates

	public List<Crate> getCrates() {
		return this.crates;
	}

	public Crate getCrateById(int crateId) {
		return this.crates.stream().filter(b -> b.getId() == crateId).findFirst().orElse(null);
	}

	public void addCrate(Crate crate) {
		crate.setId(this.crates.stream().map(Crate::getId).max(Comparator.naturalOrder()).orElse(0) + 1);
		this.crates.add(crate);
		saveDataToFile(); // Call saveDataToFile() after adding a crate
	}

	public Crate updateCrate(Crate crate) {
		int index = crate.getId() - 1;
		if (index < 0 || index >= crates.size()) {
			// Crate with the specified ID does not exist
			return null;
		}
		this.crates.set(index, crate);
		saveDataToFile(); // Call saveDataToFile() after updating a crate
		return crate;
	}

	// Orders

	public List<Order> getOrders() {
		return this.orders;
	}

	public Order getOrderbyId(int ordid) {
		return this.orders.stream().filter(b -> b.getOrderId() == ordid).findFirst().orElse(null);
	}

	public Order addOrder(Order order) {
		order.setOrderId(this.orders.stream().map(Order::getOrderId).max(Comparator.naturalOrder()).orElse(0) + 1);
		this.orders.add(order);
		logger.info("Order id in DB is : " + order.getOrderId());
		saveDataToFile(); // Call saveDataToFile() after adding an order
		return order;
	}

	public Order updateOrder(Order order) {
		int index = order.getOrderId() - 1;
		this.orders.set(index, order);
		saveDataToFile(); // Call saveDataToFile() after updating an order
		return order;
	}

	public boolean deleteOrder(int ordid) {
		final Order o = this.getOrderbyId(ordid);
		boolean removed = this.orders.remove(o);
		if (removed) {
			saveDataToFile(); // Call saveDataToFile() after deleting an order
		}
		return removed;
	}

	public Order changeOrderStatus(Order order) {
		int index = order.getOrderId() - 1;
		this.orders.set(index, order);
		saveDataToFile(); // Call saveDataToFile() after changing the order status
		return order;
	}
}
