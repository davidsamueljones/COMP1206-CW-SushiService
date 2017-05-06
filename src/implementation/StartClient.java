package implementation;

import java.awt.EventQueue;

import business.gui.BusinessApplication;
import business.model.BusinessLocation;
import client.gui.ClientApplication;
import client.model.ClientModel;

public class StartClient {
	
	/**
	 * Launch the client application targeting a specific business location
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					// Get test location
					BusinessLocation location = BusinessLocations.LOCATIONS.get("Southampton_1");
					// If location found start application
					if (location != null) {
						// Create application (exception in constructor will cause termination)
						ClientApplication application = new ClientApplication(location);
						application.setVisible(true);
					} else {
						throw new RuntimeException("Location identifier not found");
					}
				} catch (final Exception e) {
					System.err.println(e.getMessage());
				}
			}
		});
	}
	
}
