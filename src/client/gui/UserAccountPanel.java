package client.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

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
import client.model.ClientModel;
import general.gui.View;
import general.utility.ErrorBuilder;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.awt.GridLayout;

/**
 * A panel with components for modifying an existing order.
 * 
 * @author David Jones [dsj1n15]
 */
public class UserAccountPanel extends JPanel implements View {
	private static final long serialVersionUID = 180627733294715899L;
	// Client model
	private ClientModel model;
	// Record objects
	private JButton btnSubmitChanges;
	private JTextField txtUsername;
	private JPasswordField txtPassword;
	private JPasswordField txtConfirmPassword;
	private JTextField txtFullName;
	private JTextField txtAddress;
	private JComboBox<Postcode> cboPostcode;

	/**
	 * Instantiate the panel using a client model.
	 */
	public UserAccountPanel(ClientModel model) {
		this.model = model;
		init();
	}
	
	/**
	 * Create the panel
	 */
	public void init() {
		// Set layout
		GridBagLayout layout = new GridBagLayout();
		layout.columnWidths = new int[] {20, 0, 20};
		layout.rowHeights = new int[] {0, 0, 0};
		layout.columnWeights = new double[] {0.4, 1.0, 0.4};
		layout.rowWeights = new double[] {0.5, 0.0, 0.5};
		setLayout(layout);
		setBackground(Color.WHITE);
		
		// Create content panel
		JPanel pnlContent = new JPanel();
		pnlContent.setBorder(
				BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.BLACK),
						BorderFactory.createEmptyBorder(5, 5, 5, 5)));
		GridBagConstraints gbc_pnlContent = new GridBagConstraints();
		gbc_pnlContent.insets = new Insets(0, 0, 5, 5);
		gbc_pnlContent.fill = GridBagConstraints.BOTH;
		gbc_pnlContent.gridx = 1;
		gbc_pnlContent.gridy = 1;
		add(pnlContent, gbc_pnlContent);
		GridBagLayout gbl_pnlContent = new GridBagLayout();
		gbl_pnlContent.columnWidths = new int[] {0};
		gbl_pnlContent.rowHeights = new int[] {0, 0, 0, 0};
		gbl_pnlContent.columnWeights = new double[] {1.0};
		gbl_pnlContent.rowWeights = new double[] {0.0, 0.0, 0.0, 0.0};
		pnlContent.setLayout(gbl_pnlContent);
		
		// [Content Panel] <- 'Information' Label
		JLabel lblInformation = new JLabel(
				"<html>Enter registration changes for the current account (password will be ignored if field is left blank):  </html>");
		lblInformation.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		GridBagConstraints gbc_lblInformation = new GridBagConstraints();
		gbc_lblInformation.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblInformation.insets = new Insets(10, 5, 10, 0);
		gbc_lblInformation.gridx = 0;
		gbc_lblInformation.gridy = 0;
		pnlContent.add(lblInformation, gbc_lblInformation);

		// [Content Panel] <- 'Login Detail' Panel
		JPanel pnlLoginDetails = new JPanel();
		pnlLoginDetails.setBorder(BorderFactory
				.createTitledBorder(BorderFactory.createEtchedBorder(), "Login Details"));
		GridBagConstraints gbc_pnlLoginDetails = new GridBagConstraints();
		gbc_pnlLoginDetails.fill = GridBagConstraints.HORIZONTAL;
		gbc_pnlLoginDetails.anchor = GridBagConstraints.NORTH;
		gbc_pnlLoginDetails.insets = new Insets(0, 0, 5, 0);
		gbc_pnlLoginDetails.gridx = 0;
		gbc_pnlLoginDetails.gridy = 1;
		pnlContent.add(pnlLoginDetails, gbc_pnlLoginDetails);
		GridBagLayout gbl_pnlLoginDetails = new GridBagLayout();
		gbl_pnlLoginDetails.columnWidths = new int[] {0, 0};
		gbl_pnlLoginDetails.rowHeights = new int[] {0};
		gbl_pnlLoginDetails.columnWeights = new double[] {0, 1.0};
		gbl_pnlLoginDetails.rowWeights = new double[] {Double.MIN_VALUE};
		pnlLoginDetails.setLayout(gbl_pnlLoginDetails);

		// [Login Detail Panel] <- 'Username' Label
		JLabel lblUsername = new JLabel("Username:");
		GridBagConstraints gbc_lblUsername = new GridBagConstraints();
		gbc_lblUsername.anchor = GridBagConstraints.EAST;
		gbc_lblUsername.insets = new Insets(0, 0, 5, 5);
		gbc_lblUsername.gridx = 0;
		gbc_lblUsername.gridy = 0;
		pnlLoginDetails.add(lblUsername, gbc_lblUsername);
		// [Login Detail Panel] <- 'Username' TextBox
		txtUsername = new JTextField();
		txtUsername.setEnabled(false);
		GridBagConstraints gbc_txtUsername = new GridBagConstraints();
		gbc_txtUsername.insets = new Insets(0, 0, 5, 5);
		gbc_txtUsername.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtUsername.gridx = 1;
		gbc_txtUsername.gridy = 0;
		pnlLoginDetails.add(txtUsername, gbc_txtUsername);

		// [Login Detail Panel] <- 'Password' Label
		JLabel lblPassword = new JLabel("Password:");
		GridBagConstraints gbc_lblPassword = new GridBagConstraints();
		gbc_lblPassword.anchor = GridBagConstraints.EAST;
		gbc_lblPassword.insets = new Insets(0, 0, 5, 5);
		gbc_lblPassword.gridx = 0;
		gbc_lblPassword.gridy = 1;
		pnlLoginDetails.add(lblPassword, gbc_lblPassword);
		// [Login Detail Panel] <- 'Password' TextBox
		txtPassword = new JPasswordField();
		GridBagConstraints gbc_txtPassword = new GridBagConstraints();
		gbc_txtPassword.insets = new Insets(0, 0, 5, 5);
		gbc_txtPassword.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtPassword.gridx = 1;
		gbc_txtPassword.gridy = 1;
		pnlLoginDetails.add(txtPassword, gbc_txtPassword);

		// [Login Detail Panel] <- 'Confirm Password' Label
		JLabel lblConfirmPassword = new JLabel("Confirm Password:");
		GridBagConstraints gbc_lblConfirmPassword = new GridBagConstraints();
		gbc_lblConfirmPassword.insets = new Insets(0, 5, 5, 5);
		gbc_lblConfirmPassword.anchor = GridBagConstraints.EAST;
		gbc_lblConfirmPassword.gridx = 0;
		gbc_lblConfirmPassword.gridy = 2;
		pnlLoginDetails.add(lblConfirmPassword, gbc_lblConfirmPassword);
		// [Login Detail Panel] <- 'Confirm Password' TextBox
		txtConfirmPassword = new JPasswordField();
		GridBagConstraints gbc_txtConfirmPassword = new GridBagConstraints();
		gbc_txtConfirmPassword.insets = new Insets(0, 0, 5, 5);
		gbc_txtConfirmPassword.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtConfirmPassword.gridx = 1;
		gbc_txtConfirmPassword.gridy = 2;
		pnlLoginDetails.add(txtConfirmPassword, gbc_txtConfirmPassword);

		// [Content Panel] <- 'Customer Detail' Panel
		JPanel pnlCustomerDetails = new JPanel();
		pnlCustomerDetails.setBorder(BorderFactory
				.createTitledBorder(BorderFactory.createEtchedBorder(), "Customer Details"));
		GridBagConstraints gbc_pnlCustomerDetails = new GridBagConstraints();
		gbc_pnlCustomerDetails.insets = new Insets(0, 0, 5, 0);
		gbc_pnlCustomerDetails.fill = GridBagConstraints.HORIZONTAL;
		gbc_pnlCustomerDetails.anchor = GridBagConstraints.NORTH;
		gbc_pnlCustomerDetails.gridx = 0;
		gbc_pnlCustomerDetails.gridy = 2;
		pnlContent.add(pnlCustomerDetails, gbc_pnlCustomerDetails);
		GridBagLayout gbl_pnlCustomerDetails = new GridBagLayout();
		gbl_pnlCustomerDetails.columnWidths = new int[] {0, 0};
		gbl_pnlCustomerDetails.rowHeights = new int[] {0};
		gbl_pnlCustomerDetails.columnWeights = new double[] {0, 1.0};
		gbl_pnlCustomerDetails.rowWeights = new double[] {Double.MIN_VALUE};
		pnlCustomerDetails.setLayout(gbl_pnlCustomerDetails);

		// [Customer Detail Panel] <- 'Name' Label
		JLabel lblFullName = new JLabel("Full Name:");
		GridBagConstraints gbc_lblFullName = new GridBagConstraints();
		gbc_lblFullName.anchor = GridBagConstraints.EAST;
		gbc_lblFullName.insets = new Insets(0, 5, 5, 5);
		gbc_lblFullName.gridx = 0;
		gbc_lblFullName.gridy = 0;
		pnlCustomerDetails.add(lblFullName, gbc_lblFullName);
		// [Customer Detail Panel] <- 'Name' TextBox
		txtFullName = new JTextField();
		GridBagConstraints gbc_txtFullName = new GridBagConstraints();
		gbc_txtFullName.insets = new Insets(0, 0, 5, 5);
		gbc_txtFullName.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtFullName.gridx = 1;
		gbc_txtFullName.gridy = 0;
		pnlCustomerDetails.add(txtFullName, gbc_txtFullName);

		// [Customer Detail Panel] <- 'Address' Label
		JLabel lblAddress = new JLabel("Address:");
		GridBagConstraints gbc_lblAddress = new GridBagConstraints();
		gbc_lblAddress.insets = new Insets(0, 5, 5, 5);
		gbc_lblAddress.anchor = GridBagConstraints.EAST;
		gbc_lblAddress.gridx = 0;
		gbc_lblAddress.gridy = 1;
		pnlCustomerDetails.add(lblAddress, gbc_lblAddress);
		// [Customer Detail Panel] <- 'Address' TextBox
		txtAddress = new JTextField();
		GridBagConstraints gbc_txtAddress = new GridBagConstraints();
		gbc_txtAddress.anchor = GridBagConstraints.NORTH;
		gbc_txtAddress.insets = new Insets(0, 0, 5, 5);
		gbc_txtAddress.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtAddress.gridx = 1;
		gbc_txtAddress.gridy = 1;
		pnlCustomerDetails.add(txtAddress, gbc_txtAddress);

		// [Customer Detail Panel] <- 'Postcode' Label
		JLabel lblPostcode = new JLabel("Postcode:");
		GridBagConstraints gbc_lblPostcode = new GridBagConstraints();
		gbc_lblPostcode.insets = new Insets(0, 5, 5, 5);
		gbc_lblPostcode.anchor = GridBagConstraints.EAST;
		gbc_lblPostcode.gridx = 0;
		gbc_lblPostcode.gridy = 2;
		pnlCustomerDetails.add(lblPostcode, gbc_lblPostcode);
		// [Customer Detail Panel] <- 'Postcode' ComboBox
		cboPostcode = new JComboBox<>();
		GridBagConstraints gbc_cboPostcode = new GridBagConstraints();
		gbc_cboPostcode.insets = new Insets(0, 0, 5, 5);
		gbc_cboPostcode.fill = GridBagConstraints.HORIZONTAL;
		gbc_cboPostcode.gridx = 1;
		gbc_cboPostcode.gridy = 2;
		pnlCustomerDetails.add(cboPostcode, gbc_cboPostcode);

		// [Content Panel] <- 'Submit Changes' Button
		btnSubmitChanges = new JButton("Submit Changes");
		GridBagConstraints gbc_btnSubmitChanges = new GridBagConstraints();
		gbc_btnSubmitChanges.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnSubmitChanges.gridx = 0;
		gbc_btnSubmitChanges.gridy = 3;
		pnlContent.add(btnSubmitChanges, gbc_btnSubmitChanges);

		// [Submit Changes] - Submit registration changes to server 
		btnSubmitChanges.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Validate the created customer
				ErrorBuilder eb = new ErrorBuilder();
				// Create customer, validating fields locally
				CustomerLogin login;
				String password = String.valueOf(txtPassword.getPassword());
				if (password.length() != 0) {
					String confirmPassword = String.valueOf(txtConfirmPassword.getPassword());
					login = new CustomerLogin(txtUsername.getText(), password);
					// Do validation pertaining to passwords
					if (!CustomerLogin.isPasswordValid(password)) {
						eb.addError("Password is not valid");
					} else if (!password.equals(confirmPassword)) {
						eb.addError("Passwords not the same");
					}
				} else {
					// Use existing login object
					login = model.loggedInCustomer.readObject().getLogin();
				}
				Customer customer = new Customer(txtFullName.getText(), txtAddress.getText(),
						(Postcode) cboPostcode.getSelectedItem(), login);
				eb.append(customer.validate());

				// Don't attempt network registration if local registration fails
				if (!eb.isError()) {
					modifyCustomer(customer);
				} else {
					JOptionPane.showMessageDialog(null, eb.listComments("Changes Failed"),
							"Changes Failed", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
	}

	@Override
	public void initialise() {
		refresh();
		// Invoke customer load later so occurs after postcode update
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				loadCustomer();
			}	
		});	
	}

	@Override
	public void refresh() {
		refreshPostcodes();
	}

	@Override
	public JButton getAcceptButton() {
		return btnSubmitChanges;
	}
	
	/**
	 * Fill in information for logged in customer.
	 */
	public void loadCustomer() {
		Customer customer = model.loggedInCustomer.readObject();
		txtUsername.setText(customer.getLogin().getUsername());
		txtPassword.setText(null);
		txtConfirmPassword.setText(null);
		txtFullName.setText(customer.getName());
		txtAddress.setText(customer.getAddress());
		cboPostcode.setSelectedItem(customer.getPostcode());
	}
	
	/**
	 * Use model to request customer modification.
	 * @param customer Customer to use for update
	 */
	public void modifyCustomer(Customer customer) {
		ErrorBuilder eb = new ErrorBuilder();
		// Handle model behaviour (failure stops changes)
		if (model.modifyCustomer(customer)) {
			// Wait for message handler to handle response
			if (model.loggedInCustomer.waitForNew(ClientApplication.REQUEST_TIMEOUT)) {
				eb.append(model.loggedInCustomer.readComments());
				// Verify and handle response
				if (!eb.isError()) {
					loadCustomer();
					JOptionPane.showMessageDialog(null,
							"Changes Successful", "Changes Successful", JOptionPane.INFORMATION_MESSAGE);
				}
			} else {
				eb.addError("Connection successful but server did not reply");
			}
		} else {
			eb.addError("No response from server");
		}
		// Alert user of any errors
		if (eb.isError()) {
			JOptionPane.showMessageDialog(null, eb.listComments("Changes Failed"),
					"Changes Failed", JOptionPane.ERROR_MESSAGE);
		}
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
		if (model.refreshPostcodes()) {
			// Wait for message handler to handle response
			if (model.postcodes.waitForNew(ClientApplication.REQUEST_TIMEOUT)) {
				postcodes = model.postcodes.readObject();
			}
		}
		// Load postcodes
		Arrays.sort(postcodes);
		DefaultComboBoxModel<Postcode> model = new DefaultComboBoxModel<>(postcodes);
		// Update model on EDT
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				// Remember the currently selected value
				Postcode curPostcode = (Postcode) cboPostcode.getSelectedItem();
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
