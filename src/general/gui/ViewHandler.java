package general.gui;

import java.awt.Component;

/**
 * Interface for handling a view. A view is defined as a unique named component which should
 * implement the View interface. The current view can be tracked and changed using relevant
 * methods. In addition, utility methods are provided to invoke the View interface directly.
 * 
 * @author David Jones [dsj1n15]
 */
public interface ViewHandler {

	/**
	 * Add a view to the GUI, registering it with relevant navigation and tracking objects.
	 *
	 * @param component Component to add to view
	 * @param name Unique name for view that will also be used for navigation objects
	 * @param navigable Whether to add view to navigation objects
	 * @return True if view added successfully, else false
	 */
	public abstract boolean addView(Component component, String name, boolean navigable);

	/**
	 * @return The view to set as current
	 */
	public abstract void setView(String view);

	/**
	 * @return The current view
	 */
	public abstract View getView();

	/**
	 * @param view View to initialise
	 */
	public default void initialiseView(View view) {
		if (view != null) {
			view.initialise();
		}
	}

	/**
	 * @param view View to refresh
	 */
	public default void refreshView(View view) {
		if (view != null) {
			view.refresh();
		}
	}

}
