package general.gui;

import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.border.Border;

public class ToolBarButton extends JButton {
	/**
	 *
	 */
	private static final long serialVersionUID = -1763567343374687814L;
	final private ToolBar parent;
	private boolean clickable;

	public ToolBarButton(ToolBar parent, String text) {
		this.parent = parent;
		setText(text);
		setBackground(parent.getHighlightColour());
		setHighlight(false);
		setClickable(true);
		addMouseListener(new HighlightDetect());
		setContentAreaFilled(false);
	}

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

	public void setHighlight(boolean highlight) {
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

	public boolean isClickable() {
		return clickable && isEnabled();
	}

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
			if (isClickable()) {
				setHighlight(false);
				repaint();
			}
		}
	}
}
