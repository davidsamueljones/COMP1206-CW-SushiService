package general.gui;

import javax.swing.JButton;

/**
 * Interface for handling a View. A view is defined as something that should have an initialisation
 * and refresh behaviour. The implementation should dictate exact behaviour but it is assumed that
 * initialisation will occur when a view is being made visible and a refresh occurs frequently.
 *
 * @author David Jones [dsj1n15]
 */
public interface View {

	/**
	 * Do initial view setup; this should be called every time the view is shown.
	 */
	public abstract void initialise();

	/**
	 * Refresh changes in the view.
	 */
	public abstract void refresh();

	/**
	 * Get the accept button of the view, default behaviour returns null.
	 * 
	 * @return The accept button
	 */
	public default JButton getAcceptButton() {
		return null;
	}

}
