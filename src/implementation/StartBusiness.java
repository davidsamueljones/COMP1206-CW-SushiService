package implementation;

import java.awt.EventQueue;

import business.gui.BusinessApplication;
import business.model.BusinessLocation;

public class StartBusiness {

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					try {
						final BusinessLocation location =
								BusinessLocations.LOCATIONS.get("Southampton_1");
						if (location != null) {
							final BusinessApplication application =
									new BusinessApplication(location);
							application.setVisible(true);
						} else {
							throw new RuntimeException("Location identifier not found");
						}
					} catch (final Exception e) {
						System.err.println(e.getMessage());
					}
				} catch (final Exception e) {
					System.err.println(e.getMessage());
				}
			}
		});
	}

}
