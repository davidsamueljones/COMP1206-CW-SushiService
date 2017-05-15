package implementation;

import java.awt.EventQueue;

import business.gui.BusinessApplication;
import business.model.BusinessLocation;

/**
 * Test class for starting a specific business location. This must be running for any clients to 
 * work as intended.
 * 
 * @author David Jones [dsj1n15]
 */
public class StartBusiness {

	/**
	 * Launch the business application for a single business location.
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
						BusinessApplication application = new BusinessApplication(location);
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
