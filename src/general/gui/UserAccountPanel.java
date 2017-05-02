package general.gui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;

import javax.swing.JLabel;
import javax.swing.JPanel;

import general.utility.Utilities;

public class UserAccountPanel extends JPanel {

	/**
	 *
	 */
	private static final long serialVersionUID = -5750140556562208944L;

	/**
	 * Create the panel.
	 */
	public UserAccountPanel() {
		final GridBagLayout gbl_pnlUserAccount = new GridBagLayout();
		gbl_pnlUserAccount.columnWidths = new int[] {0, 0, 0, 0, 0};
		gbl_pnlUserAccount.rowHeights = new int[] {0};
		gbl_pnlUserAccount.columnWeights = new double[] {1.0, 0.0, 0.0, 0.0, 0.0};
		gbl_pnlUserAccount.rowWeights = new double[] {1.0};
		setLayout(gbl_pnlUserAccount);

		final JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		final GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.insets = new Insets(0, 0, 0, 5);
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 0;
		add(panel, gbc_panel);

		final JLabel lblLoginMessage = new JLabel("You are logged in as:");
		final GridBagConstraints gbc_lblLoginMessage = new GridBagConstraints();
		gbc_lblLoginMessage.insets = new Insets(0, 0, 0, 5);
		gbc_lblLoginMessage.gridx = 1;
		gbc_lblLoginMessage.gridy = 0;
		add(lblLoginMessage, gbc_lblLoginMessage);

		final JLabel lblUserFullName = new JLabel("David Jones");
		lblUserFullName.setFont(new Font("Lucida Grande", Font.BOLD | Font.ITALIC, 13));
		final GridBagConstraints gbc_lblUserFullName = new GridBagConstraints();
		gbc_lblUserFullName.insets = new Insets(0, 0, 0, 5);
		gbc_lblUserFullName.gridx = 2;
		gbc_lblUserFullName.gridy = 0;
		add(lblUserFullName, gbc_lblUserFullName);

		final JLabel lbllogout = new JLabel("(Logout)");
		lbllogout.setCursor(new Cursor(Cursor.HAND_CURSOR));
		final GridBagConstraints gbc_lbllogout = new GridBagConstraints();
		gbc_lbllogout.insets = new Insets(0, 0, 0, 5);
		gbc_lbllogout.gridx = 3;
		gbc_lbllogout.gridy = 0;
		add(lbllogout, gbc_lbllogout);

		final JLabel lblSettings = new JLabel();
		Utilities.setLabelImage(lblSettings, new File("resources/imgPreferences.png"));
		lblSettings.setCursor(new Cursor(Cursor.HAND_CURSOR));
		final GridBagConstraints gbc_lblSettings = new GridBagConstraints();
		gbc_lblSettings.insets = new Insets(0, 0, 0, 5);
		gbc_lblSettings.gridx = 4;
		gbc_lblSettings.gridy = 0;
		add(lblSettings, gbc_lblSettings);
	}

}
