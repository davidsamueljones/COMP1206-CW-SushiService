package general.gui;

import java.awt.Color;
import java.awt.Image;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class ToolBar extends JPanel {
	/**
	 *
	 */
	private static final long serialVersionUID = 4589560152804039907L;
	protected ArrayList<ToolBarButton> buttons;
	protected Color background;
	protected Color foreground;
	protected Color highlight;

	public ToolBar(Color background, Color foreground, Color highlight) {
		setBackground(background);
		this.background = background;
		this.foreground = foreground;
		this.highlight = highlight;
		buttons = new ArrayList<>();
		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
	}

	public ToolBarButton addButton(String text) {
		return addButton(text, null);
	}

	public ToolBarButton addButton(String text, Image img) {
		final ToolBarButton button = new ToolBarButton(this, text);
		if (img != null) {
			button.setIcon(new ImageIcon(img));
		}
		buttons.add(button);
		add(button);
		return button;
	}

	public JLabel addLabel(String text, int alignment) {
		final JLabel label = new JLabel(text, alignment);
		label.setBorder(BorderFactory.createEmptyBorder(0, 2, 0, 2));
		label.setForeground(foreground);
		add(label);
		return label;
	}

	public void addSeparator() {
		addLabel("|", SwingConstants.CENTER);
	}

	public Color getBackgroundColour() {
		return background;
	}

	public Color getForegroundColour() {
		return foreground;
	}

	public Color getHighlightColour() {
		return highlight;
	}

}
