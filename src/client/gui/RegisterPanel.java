package client.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import business.model.Customer;
import business.model.CustomerLogin;
import business.model.Postcode;
import general.gui.View;
import general.utility.ErrorBuilder;

/**
 * A panel with components and implementation for registering a new user.
 *
 * @author David Jones [dsj1n15]
 */
public class RegisterPanel extends JPanel implements View {
	private static final long serialVersionUID = 2438993133579447512L;
	// Client model
	private final ClientApplication application;
	// Record objects
	private JButton btnRegister;
	private JTextField txtUsername;
	private JPasswordField txtPassword;
	private JPasswordField txtConfirmPassword;
	private JTextField txtFullName;
	private JTextField txtAddress;
	private JComboBox<Postcode> cboPostcode;

	/**
	 * Instantiate the panel using the client application.
	 */
	public RegisterPanel(ClientApplication application) {
		this.application = application;
		init();
	}

	/**
	 * Create the panel.
	 */
	private void init() {
		// Set layout
		final GridBagLayout layout = new GridBagLayout();
		layout.columnWidths = new int[] {20, 0, 20};
		layout.rowHeights = new int[] {0, 0, 0};
		layout.columnWeights = new double[] {0.4, 1.0, 0.4};
		layout.rowWeights = new double[] {0.5, 0.0, 0.5};
		setLayout(layout);
		setBackground(Color.WHITE);

		// Create content panel
		final JPanel pnlContent = new JPanel();
		pnlContent.setBorder(
				BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.BLACK),
						BorderFactory.createEmptyBorder(5, 5, 5, 5)));
		final GridBagConstraints gbc_pnlContent = new GridBagConstraints();
		gbc_pnlContent.insets = new Insets(0, 0, 5, 5);
		gbc_pnlContent.fill = GridBagConstraints.BOTH;
		gbc_pnlContent.gridx = 1;
		gbc_pnlContent.gridy = 1;
		add(pnlContent, gbc_pnlContent);
		final GridBagLayout gbl_pnlContent = new GridBagLayout();
		gbl_pnlContent.columnWidths = new int[] {0};
		gbl_pnlContent.rowHeights = new int[] {0, 0, 0, 0};
		gbl_pnlContent.columnWeights = new double[] {1.0};
		gbl_pnlContent.rowWeights = new double[] {0.0, 0.0, 0.0, 0.0};
		pnlContent.setLayout(gbl_pnlContent);

		// [Content Panel] <- 'Information' Label
		final JLabel lblInformation = new JLabel(
				"<html>Enter registration details for a new customer (all fields required):</html>");
		lblInformation.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		final GridBagConstraints gbc_lblInformation = new GridBagConstraints();
		gbc_lblInformation.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblInformation.insets = new Insets(10, 5, 10, 0);
		gbc_lblInformation.gridx = 0;
		gbc_lblInformation.gridy = 0;
		pnlContent.add(lblInformation, gbc_lblInformation);

		// [Content Panel] <- 'Login Detail' Panel
		final JPanel pnlLoginDetails = new JPanel();
		pnlLoginDetails.setBorder(BorderFactory
				.createTitledBorder(BorderFactory.createEtchedBorder(), "Login Details"));
		final GridBagConstraints gbc_pnlLoginDetails = new GridBagConstraints();
		gbc_pnlLoginDetails.fill = GridBagConstraints.HORIZONTAL;
		gbc_pnlLoginDetails.anchor = GridBagConstraints.NORTH;
		gbc_pnlLoginDetails.insets = new Insets(0, 0, 5, 0);
		gbc_pnlLoginDetails.gridx = 0;
		gbc_pnlLoginDetails.gridy = 1;
		pnlContent.add(pnlLoginDetails, gbc_pnlLoginDetails);
		final GridBagLayout gbl_pnlLoginDetails = new GridBagLayout();
		gbl_pnlLoginDetails.columnWidths = new int[] {0, 0};
		gbl_pnlLoginDetails.rowHeights = new int[] {0};
		gbl_pnlLoginDetails.columnWeights = new double[] {0, 1.0};
		gbl_pnlLoginDetails.rowWeights = new double[] {Double.MIN_VALUE};
		pnlLoginDetails.setLayout(gbl_pnlLoginDetails);

		// [Login Detail Panel] <- 'Username' Label
		final JLabel lblUsername = new JLabel("Username:");
		final GridBagConstraints gbc_lblUsername = new GridBagConstraints();
		gbc_lblUsername.anchor = GridBagConstraints.EAST;
		gbc_lblUsername.insets = new Insets(0, 0, 5, 5);
		gbc_lblUsername.gridx = 0;
		gbc_lblUsername.gridy = 0;
		pnlLoginDetails.add(lblUsername, gbc_lblUsername);
		// [Login Detail Panel] <- 'Username' TextBox
		txtUsername = new JTextField();
		final GridBagConstraints gbc_txtUsername = new GridBagConstraints();
		gbc_txtUsername.insets = new Insets(0, 0, 5, 5);
		gbc_txtUsername.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtUsername.gridx = 1;
		gbc_txtUsername.gridy = 0;
		pnlLoginDetails.add(txtUsername, gbc_txtUsername);

		// [Login Detail Panel] <- 'Password' Label
		final JLabel lblPassword = new JLabel("Password:");
		final GridBagConstraints gbc_lblPassword = new GridBagConstraints();
		gbc_lblPassword.anchor = GridBagConstraints.EAST;
		gbc_lblPassword.insets = new Insets(0, 0, 5, 5);
		gbc_lblPassword.gridx = 0;
		gbc_lblPassword.gridy = 1;
		pnlLoginDetails.add(lblPassword, gbc_lblPassword);
		// [Login Detail Panel] <- 'Password' TextBox
		txtPassword = new JPasswordField();
		final GridBagConstraints gbc_txtPassword = new GridBagConstraints();
		gbc_txtPassword.insets = new Insets(0, 0, 5, 5);
		gbc_txtPassword.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtPassword.gridx = 1;
		gbc_txtPassword.gridy = 1;
		pnlLoginDetails.add(txtPassword, gbc_txtPassword);

		// [Login Detail Panel] <- 'Confirm Password' Label
		final JLabel lblConfirmPassword = new JLabel("Confirm Password:");
		final GridBagConstraints gbc_lblConfirmPassword = new GridBagConstraints();
		gbc_lblConfirmPassword.insets = new Insets(0, 5, 5, 5);
		gbc_lblConfirmPassword.anchor = GridBagConstraints.EAST;
		gbc_lblConfirmPassword.gridx = 0;
		gbc_lblConfirmPassword.gridy = 2;
		pnlLoginDetails.add(lblConfirmPassword, gbc_lblConfirmPassword);
		// [Login Detail Panel] <- 'Confirm Password' TextBox
		txtConfirmPassword = new JPasswordField();
		final GridBagConstraints gbc_txtConfirmPassword = new GridBagConstraints();
		gbc_txtConfirmPassword.insets = new Insets(0, 0, 5, 5);
		gbc_txtConfirmPassword.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtConfirmPassword.gridx = 1;
		gbc_txtConfirmPassword.gridy = 2;
		pnlLoginDetails.add(txtConfirmPassword, gbc_txtConfirmPassword);

		// [Content Panel] <- 'Customer Detail' Panel
		final JPanel pnlCustomerDetails = new JPanel();
		pnlCustomerDetails.setBorder(BorderFactory
				.createTitledBorder(BorderFactory.createEtchedBorder(), "Customer Details"));
		final GridBagConstraints gbc_pnlCustomerDetails = new GridBagConstraints();
		gbc_pnlCustomerDetails.insets = new Insets(0, 0, 5, 0);
		gbc_pnlCustomerDetails.fill = GridBagConstraints.HORIZONTAL;
		gbc_pnlCustomerDetails.anchor = GridBagConstraints.NORTH;
		gbc_pnlCustomerDetails.gridx = 0;
		gbc_pnlCustomerDetails.gridy = 2;
		pnlContent.add(pnlCustomerDetails, gbc_pnlCustomerDetails);
		final GridBagLayout gbl_pnlCustomerDetails = new GridBagLayout();
		gbl_pnlCustomerDetails.columnWidths = new int[] {0, 0};
		gbl_pnlCustomerDetails.rowHeights = new int[] {0};
		gbl_pnlCustomerDetails.columnWeights = new double[] {0, 1.0};
		gbl_pnlCustomerDetails.rowWeights = new double[] {Double.MIN_VALUE};
		pnlCustomerDetails.setLayout(gbl_pnlCustomerDetails);

		// [Customer Detail Panel] <- 'Name' Label
		final JLabel lblFullName = new JLabel("Full Name:");
		final GridBagConstraints gbc_lblFullName = new GridBagConstraints();
		gbc_lblFullName.anchor = GridBagConstraints.EAST;
		gbc_lblFullName.insets = new Insets(0, 5, 5, 5);
		gbc_lblFullName.gridx = 0;
		gbc_lblFullName.gridy = 0;
		pnlCustomerDetails.add(lblFullName, gbc_lblFullName);
		// [Customer Detail Panel] <- 'Name' TextBox
		txtFullName = new JTextField();
		final GridBagConstraints gbc_txtFullName = new GridBagConstraints();
		gbc_txtFullName.insets = new Insets(0, 0, 5, 5);
		gbc_txtFullName.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtFullName.gridx = 1;
		gbc_txtFullName.gridy = 0;
		pnlCustomerDetails.add(txtFullName, gbc_txtFullName);

		// [Customer Detail Panel] <- 'Address' Label
		final JLabel lblAddress = new JLabel("Address:");
		final GridBagConstraints gbc_lblAddress = new GridBagConstraints();
		gbc_lblAddress.insets = new Insets(0, 5, 5, 5);
		gbc_lblAddress.anchor = GridBagConstraints.EAST;
		gbc_lblAddress.gridx = 0;
		gbc_lblAddress.gridy = 1;
		pnlCustomerDetails.add(lblAddress, gbc_lblAddress);
		// [Customer Detail Panel] <- 'Address' TextBox
		txtAddress = new JTextField();
		final GridBagConstraints gbc_txtAddress = new GridBagConstraints();
		gbc_txtAddress.anchor = GridBagConstraints.NORTH;
		gbc_txtAddress.insets = new Insets(0, 0, 5, 5);
		gbc_txtAddress.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtAddress.gridx = 1;
		gbc_txtAddress.gridy = 1;
		pnlCustomerDetails.add(txtAddress, gbc_txtAddress);

		// [Customer Detail Panel] <- 'Postcode' Label
		final JLabel lblPostcode = new JLabel("Postcode:");
		final GridBagConstraints gbc_lblPostcode = new GridBagConstraints();
		gbc_lblPostcode.insets = new Insets(0, 5, 5, 5);
		gbc_lblPostcode.anchor = GridBagConstraints.EAST;
		gbc_lblPostcode.gridx = 0;
		gbc_lblPostcode.gridy = 2;
		pnlCustomerDetails.add(lblPostcode, gbc_lblPostcode);
		// [Customer Detail Panel] <- 'Postcode' ComboBox
		cboPostcode = new JComboBox<>();
		final GridBagConstraints gbc_cboPostcode = new GridBagConstraints();
		gbc_cboPostcode.insets = new Insets(0, 0, 5, 5);
		gbc_cboPostcode.fill = GridBagConstraints.HORIZONTAL;
		gbc_cboPostcode.gridx = 1;
		gbc_cboPostcode.gridy = 2;
		pnlCustomerDetails.add(cboPostcode, gbc_cboPostcode);

		// [Content Panel] <- 'Submit Changes' Button
		btnRegister = new JButton("Submit Changes");
		final GridBagConstraints gbc_btnRegister = new GridBagConstraints();
		gbc_btnRegister.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnRegister.gridx = 0;
		gbc_btnRegister.gridy = 3;
		pnlContent.add(btnRegister, gbc_btnRegister);


		// [Register Customer] - Submit registration to server
		btnRegister.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Create customer, validating fields locally
				final String password = String.valueOf(txtPassword.getPassword());
				final String confirmPassword = String.valueOf(txtConfirmPassword.getPassword());
				final CustomerLogin login = new CustomerLogin(txtUsername.getText(), password);
				final Customer customer = new Customer(txtFullName.getText(), txtAddress.getText(),
						(Postcode) cboPostcode.getSelectedItem(), login);
				// Validate the created customer
				final ErrorBuilder eb = new ErrorBuilder();
				eb.append(customer.validate());
				// Do further validation pertaining to registration
				if (!CustomerLogin.isPasswordValid(password)) {
					eb.addError("Password is not valid");
				} else if (!password.equals(confirmPassword)) {
					eb.addError("Passwords not the same");
				}
				// Don't attempt network registration of local registration fails
				if (!eb.isError()) {
					register(customer);
				} else {
					JOptionPane.showMessageDialog(null, eb.listComments("Registration Failed"),
							"Registration Failed", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
	}

	@Override
	public void initialise() {
		refresh();
		clear();
	}

	@Override
	public void refresh() {
		refreshPostcodes();
	}

	@Override
	public JButton getAcceptButton() {
		return btnRegister;
	}

	/**
	 * Attempt to register a new customer.
	 *
	 * @param customer Customer object to register
	 */
	public void register(Customer customer) {
		final ErrorBuilder eb = new ErrorBuilder();
		// Handle model behaviour (failure stops registration)
		if (application.model.register(customer)) {
			// Wait for message handler to handle response
			if (application.model.registerResponse.waitForNew(ClientApplication.REQUEST_TIMEOUT)) {
				eb.append(application.model.registerResponse.readComments());
				// Verify and handle response
				if (!eb.isError()) {
					JOptionPane.showMessageDialog(null,
							"Registration Successful - You may now login",
							"Registration Successful", JOptionPane.INFORMATION_MESSAGE);
					application.setView("Login");
				}
			} else {
				eb.addError("Connection successful but server did not reply");
			}
		} else {
			eb.addError("No response from server");
		}
		// Alert user of any errors
		if (eb.isError()) {
			JOptionPane.showMessageDialog(null, eb.listComments("Registration Failed"),
					"Registration Failed", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Clear all input boxes.
	 */
	private void clear() {
		txtUsername.setText(null);
		txtPassword.setText(null);
		txtConfirmPassword.setText(null);
		txtFullName.setText(null);
		txtAddress.setText(null);
		cboPostcode.setSelectedItem(null);
		txtUsername.requestFocusInWindow();
	}

	/**
	 * Load allowed postcodes from server, updating combo box.
	 */
	private void refreshPostcodes() {
		// Don't attempt update if popup visible
		if (cboPostcode.isPopupVisible()) {
			return;
		}
		Postcode[] postcodes = new Postcode[0];
		// Handle model behaviour (failure clears postcodes)
		if (application.model.refreshPostcodes()) {
			// Wait for message handler to handle response
			if (application.model.postcodes.waitForNew(ClientApplication.REQUEST_TIMEOUT)) {
				postcodes = application.model.postcodes.readObject();
			}
		}
		// Load postcodes
		Arrays.sort(postcodes);
		final DefaultComboBoxModel<Postcode> model = new DefaultComboBoxModel<>(postcodes);
		// Update model on EDT
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				// Remember the currently selected value
				final Postcode curPostcode = (Postcode) cboPostcode.getSelectedItem();
				cboPostcode.setModel(model);
				// Reset selection
				cboPostcode.setSelectedItem(null);
				// Attempt reselection
				if (curPostcode != null) {
					if (model.getIndexOf(curPostcode) >= 0) {
						cboPostcode.setSelectedItem(curPostcode);
					} else {
						JOptionPane.showMessageDialog(null,
								"Currently selected postcode no longer available",
								"Registration Error", JOptionPane.ERROR_MESSAGE);
					}
				}

			}
		});
	}

}
