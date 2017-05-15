package general.gui;

import java.awt.Cursor;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Class UserAccountHeader, displays an account login and provides a button to logout.
 *
 * @author David Jones [dsj1n15]
 */
public class UserAccountHeader extends JPanel {
	private static final long serialVersionUID = -5750140556562208944L;

	private final JLabel lblCustomerName;
	private final JButton btnLogout;

	/**
	 * Create the panel.
	 */
	public UserAccountHeader() {
		// Define layout
		final GridBagLayout gbl_pnlUserAccount = new GridBagLayout();
		gbl_pnlUserAccount.columnWidths = new int[] {0, 0, 0, 0, 0};
		gbl_pnlUserAccount.rowHeights = new int[] {0};
		gbl_pnlUserAccount.columnWeights = new double[] {1.0, 0.0, 0.0, 0.0};
		gbl_pnlUserAccount.rowWeights = new double[] {1.0};
		setLayout(gbl_pnlUserAccount);

		// [Content] <- 'Login Message' Label
		final JLabel lblLoginMessage = new JLabel("You are logged in as:");
		final GridBagConstraints gbc_lblLoginMessage = new GridBagConstraints();
		gbc_lblLoginMessage.insets = new Insets(0, 0, 0, 5);
		gbc_lblLoginMessage.gridx = 1;
		gbc_lblLoginMessage.gridy = 0;
		add(lblLoginMessage, gbc_lblLoginMessage);

		// [Content] <- 'Customer Name' Label
		lblCustomerName = new JLabel();
		lblCustomerName.setFont(new Font("Lucida Grande", Font.BOLD | Font.ITALIC, 13));
		final GridBagConstraints gbc_lblCustomerFullName = new GridBagConstraints();
		gbc_lblCustomerFullName.insets = new Insets(0, 0, 0, 5);
		gbc_lblCustomerFullName.gridx = 2;
		gbc_lblCustomerFullName.gridy = 0;
		add(lblCustomerName, gbc_lblCustomerFullName);

		// [Content] <- 'Logout' Button
		btnLogout = new JButton("Logout");
		btnLogout.setCursor(new Cursor(Cursor.HAND_CURSOR));
		final GridBagConstraints gbc_lbllogout = new GridBagConstraints();
		gbc_lbllogout.insets = new Insets(0, 0, 0, 5);
		gbc_lbllogout.gridx = 3;
		gbc_lbllogout.gridy = 0;
		add(btnLogout, gbc_lbllogout);

	}

	/**
	 * Sets the name displayed in the account header.
	 *
	 * @param name Name to display
	 */
	public void setCustomerName(String name) {
		lblCustomerName.setText(name);
	}

	/**
	 * @return A reference to the logout button
	 */
	public JButton getLogoutButton() {
		return btnLogout;
	}

}
