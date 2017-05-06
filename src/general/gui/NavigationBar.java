package general.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Class NavigationBar, this is a tool bar that handles a view and the buttons required to navigate it.
 * Note: Part of GUI extension that was done as opposed to changing look and feel UI managers,
 * or using external libraries - not all standard component coding practices may be acknowledged.
 * 
 * @author David Jones [dsj1n15]
 */
public class NavigationBar extends ToolBar {
	private static final long serialVersionUID = -2348476578810835917L;
	private final ViewHandler viewHandler;

	/**
	 * Instantiate as a toolbar with attached view handler.
	 * @param viewHandler View handler being controlled by navigation bar
	 * @param background Navigation bar colour
	 * @param foreground Text colour
	 * @param highlight Button colour on hover
	 */
	public NavigationBar(ViewHandler viewHandler, Color background, 
			Color foreground, Color highlight) {
		super(background, foreground, highlight);
		this.viewHandler = viewHandler;
	}

	/**
	 * Create a new navigation button with the given text assigned to it. 
	 * @param text Text to assign to button; this should be the same text as
	 * the view handler identifier.
	 * @return Created button
	 */
	public ToolBarButton addNavigationButton(String text) {
		// Create separator before new button if no components
		if (getComponentCount() == 0) {
			addSeparator();
		}
		// Create button
		final ToolBarButton button = addButton(text);
		button.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		// Handle click of navigation button
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!button.isClickable()) {
					return;
				}
				// Select button and view in attached view handler
				setSelected(button);
				viewHandler.setView(button.getText());
			}
		});
		// Create separator after button
		addSeparator();
		return button;
	}

	/**
	 * Select a button in the navigation bar by its text.
	 * @param text Text of button
	 */
	public void setSelected(String text) {
		for (final ToolBarButton button : buttons) {
			if (button.getText().equals(text)) {
				setSelected(button);
				break;
			}
		}
	}

	/**
	 * Select a button in the navigation bar.
	 * @param selButton Button to select
	 */
	private void setSelected(ToolBarButton selButton) {
		for (final ToolBarButton button : buttons) {
			button.setClickable(true);
		}
		selButton.setClickable(false);
	}

}
