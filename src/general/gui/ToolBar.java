package general.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * Class ToolBarButton, this is a tool bar that holds many components horizontally. Note: Part of
 * GUI extension that was done as opposed to changing look and feel UI managers, or using external
 * libraries - not all standard component coding practices may be acknowledged. The reason for
 * creating this component is that the swing tool bar was not customisable enough to get the desired
 * design.
 *
 * @author David Jones [dsj1n15]
 */
public class ToolBar extends JPanel {
	private static final long serialVersionUID = 4589560152804039907L;
	// Buttons
	protected final ArrayList<ToolBarButton> buttons = new ArrayList<>();
	// Theme
	private final Color background;
	private final Color foreground;
	private final Color highlight;

	/**
	 * Instantiate a tool bar with a given theme.
	 *
	 * @param background Tool bar colour
	 * @param foreground Text colour
	 * @param highlight Button colour on hover
	 */
	public ToolBar(Color background, Color foreground, Color highlight) {
		// Record theme
		this.background = background;
		this.foreground = foreground;
		this.highlight = highlight;
		// Set background of component
		setBackground(background);
		// Organise layout
		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		// Set minimum height
		add(Box.createRigidArea(new Dimension(0, 25)));
	}

	/**
	 * @return Tool bar colour
	 */
	public Color getBackgroundColour() {
		return background;
	}

	/**
	 * @return Text colour
	 */
	public Color getForegroundColour() {
		return foreground;
	}

	/**
	 * @return Button colour on hover
	 */
	public Color getHighlightColour() {
		return highlight;
	}

	/**
	 * Add a new button using given information.
	 *
	 * @param text Text to assign to button
	 * @return Created button
	 */
	public ToolBarButton addButton(String text) {
		return addButton(text, null);
	}

	/**
	 * Add a new button using given information.
	 *
	 * @param text Text to assign to button
	 * @param img Image to assign to button as icon
	 * @return Created button
	 */
	public ToolBarButton addButton(String text, Image img) {
		// Create button
		final ToolBarButton button = new ToolBarButton(this, text);
		if (img != null) {
			button.setIcon(new ImageIcon(img));
		}
		// Keep track of button
		buttons.add(button);
		add(button);
		return button;
	}

	/**
	 * Add non-clickable text to toolbar.
	 *
	 * @param text Text to assign to label
	 * @param alignment Alignment of text
	 * @return Created label
	 */
	public JLabel addLabel(String text, int alignment) {
		// Create label
		final JLabel label = new JLabel(text, alignment);
		label.setBorder(BorderFactory.createEmptyBorder(0, 2, 0, 2));
		label.setForeground(foreground);
		add(label);
		return label;
	}

	/**
	 * Create a label with text '|' acting as a separator
	 */
	public void addSeparator() {
		addLabel("|", SwingConstants.CENTER);
	}

}
