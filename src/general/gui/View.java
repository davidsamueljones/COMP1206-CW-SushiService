package general.gui;

public interface View {

	/**
	 * Do initial view setup; this should be called every time the view is shown.
	 */
	public abstract void initialise();

	/**
	 * Refresh changes in the view.
	 */
	public abstract void refresh();

}
