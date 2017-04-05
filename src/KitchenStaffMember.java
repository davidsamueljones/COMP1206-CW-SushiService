import java.util.Objects;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

/**
 * KitchenStaffMember class, holds data and appropriate methods for a member of kitchen staff.
 * Name of kitchen staff member defines class equality. 
 * 
 * Sushi Service - COMP1206 Coursework
 * @author David Jones [dsj1n15]
 */
public class KitchenStaffMember implements Runnable {
	final private String name;
	private StockManagement storage;
	private boolean working;

	public KitchenStaffMember(String name, StockManagement storage, boolean working) {
		this.name = name;
		this.storage = storage;
		this.working = working;
	}

	public boolean prepareDish() {
		// Get storage stock handlers
		StockHandler<Ingredient> ingredients = storage.getIngredients();
		StockHandler<Dish> dishes = storage.getDishes();

		Dish dish;
		synchronized (ingredients) {
			synchronized (dishes) {
				dish = storage.getRestockableDish();
				if (dish == null) {
					return false;
				}
				if (ingredients.removeStock(dish.getIngredients())) {
					statusUpdate(String.format("Preparing dish '%s'...", dish.getName()));
					dishes.addStock(dish, 1, false);
				}
				else {
					statusUpdate(String.format("Not enough stock to make dish '%s'!", dish.getName()));	
					return false;
				}
			}
		}
		// Wait (simulate dish preparation)
		randomWait();
		// Finalise dish by making it available
		dishes.makeStockAvailable(dish, 1);
		statusUpdate(String.format("Prepared dish '%s'!", dish.getName()));	
		return true;
	}

	private static void randomWait() {
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void statusUpdate(String msg) {
		System.out.println(String.format("[%s] : %s", name, msg));
	}

	@Override
	public void run() {
		while (working) {
			prepareDish();
			// Wait
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}		
	}

	@Override
	public String toString() {
		return String.format("Kitchen Staff Member [name=%s]", name);
	}	

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (!(obj instanceof KitchenStaffMember)) return false;
		KitchenStaffMember other = (KitchenStaffMember) obj;
		return (Objects.equals(this.name, other.name));
	}               

	@Override
	public int hashCode() {
		final int prime = 13;
		return prime + (name == null ? 0 : name.hashCode());
	}

}
