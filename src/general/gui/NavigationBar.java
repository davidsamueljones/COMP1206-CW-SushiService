package general.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NavigationBar extends ToolBar {
	/**
	 *
	 */
	private static final long serialVersionUID = -2348476578810835917L;
	private final ViewHandler viewHandler;

	public NavigationBar(ViewHandler viewHandler, Color background, Color foreground,
			Color highlight) {
		super(background, foreground, highlight);
		this.viewHandler = viewHandler;
	}

	public ToolBarButton addNavigationButton(String text) {
		if (getComponentCount() == 0) {
			addSeparator();
		}
		final ToolBarButton button = addButton(text);
		button.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setSelected(button);
				final String selectedText = button.getText();
				viewHandler.setView(selectedText);
			}
		});
		addSeparator();
		return button;
	}

	public void setSelected(String text) {
		for (final ToolBarButton button : buttons) {
			if (button.getText().equals(text)) {
				setSelected(button);
				break;
			}
		}
	}

	private void setSelected(ToolBarButton selButton) {
		for (final ToolBarButton button : buttons) {
			button.setClickable(true);
		}
		selButton.setClickable(false);
	}

}
