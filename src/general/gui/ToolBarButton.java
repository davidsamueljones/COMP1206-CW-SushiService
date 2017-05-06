package general.gui;

import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.border.Border;

/**
 * Class ToolBarButton, this is a button that automatically formats itself to match that
 * of its parent ToolBar. It also features different aesthetics to standard buttons.
 * Note: Part of GUI extension that was done as opposed to changing look and feel UI managers,
 * or using external libraries - not all standard component coding practices may be acknowledged.
 * 
 * @author David Jones [dsj1n15]
 */
public class ToolBarButton extends JButton {
	private static final long serialVersionUID = -1763567343374687814L;
	final private ToolBar parent;
	private boolean clickable;

	/**
	 * Instantiate a tool bar button that is attached to a parent tool bar.
	 * @param parent Parent tool bar
	 * @param text Text displayed by button
	 */
	public ToolBarButton(ToolBar parent, String text) {
		this.parent = parent;
		setText(text);
		// Set look and feel
		setContentAreaFilled(false);
		setBackground(parent.getHighlightColour());
		setHighlight(false);
		// Set initial state
		setClickable(true);
		// Add highlight detector
		addMouseListener(new HighlightDetect());
	}

	/**
	 * @return Whether the button should be clickable
	 */
	public boolean isClickable() {
		return clickable && isEnabled();
	}
	
	/**
	 * Change the visual appearance of component to indicatate it is not clickable.
	 * @param clickable Whether component should be clickable
	 */
	public void setClickable(boolean clickable) {
		this.clickable = clickable;
		setHighlight(false);
		if (clickable) {
			setCursor(new Cursor(Cursor.HAND_CURSOR));
			setForeground(parent.getForegroundColour());
		} else {
			setCursor(null);
			setForeground(parent.getHighlightColour());
		}
	}

	/**
	 * Change the visual appearance of component to indicate it is being highlighted.
	 * @param highlight Whether the component should be highlighted
	 */
	private void setHighlight(boolean highlight) {
		setOpaque(highlight);
		Border border = BorderFactory.createEmptyBorder(2, 3, 2, 3);
		if (highlight) {
			border = BorderFactory.createCompoundBorder(
					BorderFactory.createLineBorder(parent.getForegroundColour(), 1), border);
			border = BorderFactory.createCompoundBorder(
					BorderFactory.createLineBorder(parent.getBackgroundColour(), 2), border);
		} else {
			border = BorderFactory.createCompoundBorder(
					BorderFactory.createLineBorder(parent.getBackgroundColour(), 3), border);
		}
		setBorder(border);

	}
	
	/**
	 * Mouse listener that handles button highlighting.
	 * @author David Jones [dsj1n15]
	 */
	class HighlightDetect extends MouseAdapter {

		@Override
		public void mouseEntered(MouseEvent e) {
			if (isClickable()) {
				setHighlight(true);
				repaint();
			}
		}

		@Override
		public void mouseExited(MouseEvent e) {
			setHighlight(false);
			repaint();
		}
		
	}
	
}
