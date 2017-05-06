package client.gui;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

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
import general.gui.ViewHandler;
import general.model.Message;
import general.utility.ErrorBuilder;

public class RegisterPanel extends JPanel implements View {
	private static final long serialVersionUID = 2438993133579447512L;
	private static final int DEFAULT_TXT_COLUMNS = 15;

	ClientApplication application;

	private JTextField txtUsername;
	private JPasswordField txtPassword;
	private JPasswordField txtConfirmPassword;
	private JTextField txtFullName;
	private JTextField txtAddress;
	private JComboBox<Postcode> cboPostcode;

	/**
	 * Create the panel.
	 */
	public RegisterPanel(ClientApplication application) {
		this.application = application;
		init();
	}

	private void init() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] {20, 0, 0, 20};
		gridBagLayout.rowHeights = new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[] {0.4, 0.0, 0.2, 0.4};
		gridBagLayout.rowWeights =
				new double[] {0.5, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.5};
		setLayout(gridBagLayout);

		JLabel lblInformation = new JLabel(
				"<html>Enter registration details for a new customer (all fields required):</html>");
		GridBagConstraints gbc_lblInformation = new GridBagConstraints();
		gbc_lblInformation.gridwidth = 2;
		gbc_lblInformation.anchor = GridBagConstraints.WEST;
		gbc_lblInformation.insets = new Insets(10, 5, 10, 5);
		gbc_lblInformation.gridx = 1;
		gbc_lblInformation.gridy = 1;
		add(lblInformation, gbc_lblInformation);
		
		JLabel lblUsername = new JLabel("Username:");
		GridBagConstraints gbc_lblUsername = new GridBagConstraints();
		gbc_lblUsername.anchor = GridBagConstraints.EAST;
		gbc_lblUsername.insets = new Insets(0, 0, 5, 5);
		gbc_lblUsername.gridx = 1;
		gbc_lblUsername.gridy = 2;
		add(lblUsername, gbc_lblUsername);

		txtUsername = new JTextField();
		txtUsername.setColumns(DEFAULT_TXT_COLUMNS);
		GridBagConstraints gbc_txtUsername = new GridBagConstraints();
		gbc_txtUsername.insets = new Insets(0, 0, 5, 0);
		gbc_txtUsername.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtUsername.gridx = 2;
		gbc_txtUsername.gridy = 2;
		add(txtUsername, gbc_txtUsername);

		JLabel lblPassword = new JLabel("Password:");
		GridBagConstraints gbc_lblPassword = new GridBagConstraints();
		gbc_lblPassword.anchor = GridBagConstraints.EAST;
		gbc_lblPassword.insets = new Insets(0, 0, 5, 5);
		gbc_lblPassword.gridx = 1;
		gbc_lblPassword.gridy = 3;
		add(lblPassword, gbc_lblPassword);

		txtPassword = new JPasswordField();
		txtPassword.setColumns(DEFAULT_TXT_COLUMNS);
		GridBagConstraints gbc_txtPassword = new GridBagConstraints();
		gbc_txtPassword.insets = new Insets(0, 0, 5, 0);
		gbc_txtPassword.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtPassword.gridx = 2;
		gbc_txtPassword.gridy = 3;
		add(txtPassword, gbc_txtPassword);

		JLabel lblConfirmPassword = new JLabel("Confirm Password:");
		GridBagConstraints gbc_lblConfirmPassword = new GridBagConstraints();
		gbc_lblConfirmPassword.insets = new Insets(0, 0, 5, 5);
		gbc_lblConfirmPassword.anchor = GridBagConstraints.EAST;
		gbc_lblConfirmPassword.gridx = 1;
		gbc_lblConfirmPassword.gridy = 4;
		add(lblConfirmPassword, gbc_lblConfirmPassword);

		txtConfirmPassword = new JPasswordField();
		txtConfirmPassword.setColumns(DEFAULT_TXT_COLUMNS);
		GridBagConstraints gbc_txtConfirmPassword = new GridBagConstraints();
		gbc_txtConfirmPassword.insets = new Insets(0, 0, 5, 0);
		gbc_txtConfirmPassword.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtConfirmPassword.gridx = 2;
		gbc_txtConfirmPassword.gridy = 4;
		add(txtConfirmPassword, gbc_txtConfirmPassword);

		JLabel lblFullName = new JLabel("Full Name:");
		GridBagConstraints gbc_lblFullName = new GridBagConstraints();
		gbc_lblFullName.anchor = GridBagConstraints.EAST;
		gbc_lblFullName.insets = new Insets(0, 0, 5, 5);
		gbc_lblFullName.gridx = 1;
		gbc_lblFullName.gridy = 5;
		add(lblFullName, gbc_lblFullName);

		txtFullName = new JTextField();
		txtFullName.setColumns(DEFAULT_TXT_COLUMNS);
		GridBagConstraints gbc_txtFullName = new GridBagConstraints();
		gbc_txtFullName.insets = new Insets(0, 0, 5, 0);
		gbc_txtFullName.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtFullName.gridx = 2;
		gbc_txtFullName.gridy = 5;
		add(txtFullName, gbc_txtFullName);

		JLabel lblAddress = new JLabel("Address:");
		GridBagConstraints gbc_lblAddress = new GridBagConstraints();
		gbc_lblAddress.insets = new Insets(0, 0, 5, 5);
		gbc_lblAddress.anchor = GridBagConstraints.EAST;
		gbc_lblAddress.gridx = 1;
		gbc_lblAddress.gridy = 6;
		add(lblAddress, gbc_lblAddress);

		txtAddress = new JTextField();
		txtAddress.setColumns(DEFAULT_TXT_COLUMNS);
		GridBagConstraints gbc_txtAddress = new GridBagConstraints();
		gbc_txtAddress.anchor = GridBagConstraints.NORTH;
		gbc_txtAddress.insets = new Insets(0, 0, 5, 0);
		gbc_txtAddress.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtAddress.gridx = 2;
		gbc_txtAddress.gridy = 6;
		add(txtAddress, gbc_txtAddress);

		JLabel lblPostcode = new JLabel("Postcode:");
		GridBagConstraints gbc_lblPostcode = new GridBagConstraints();
		gbc_lblPostcode.insets = new Insets(0, 0, 5, 5);
		gbc_lblPostcode.anchor = GridBagConstraints.EAST;
		gbc_lblPostcode.gridx = 1;
		gbc_lblPostcode.gridy = 7;
		add(lblPostcode, gbc_lblPostcode);

		cboPostcode = new JComboBox<>();
		GridBagConstraints gbc_cboPostcode = new GridBagConstraints();
		gbc_cboPostcode.insets = new Insets(0, 0, 5, 0);
		gbc_cboPostcode.fill = GridBagConstraints.HORIZONTAL;
		gbc_cboPostcode.gridx = 2;
		gbc_cboPostcode.gridy = 7;
		add(cboPostcode, gbc_cboPostcode);

		JButton btnRegister = new JButton("Register");
		GridBagConstraints gbc_btnRegister = new GridBagConstraints();
		gbc_btnRegister.insets = new Insets(0, 0, 10, 0);
		gbc_btnRegister.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnRegister.gridwidth = 2;
		gbc_btnRegister.gridx = 1;
		gbc_btnRegister.gridy = 8;
		add(btnRegister, gbc_btnRegister);

		btnRegister.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Create customer, validating fields locally
				String password = String.valueOf(txtPassword.getPassword());
				String confirmPassword = String.valueOf(txtConfirmPassword.getPassword());
				CustomerLogin login = new CustomerLogin(txtUsername.getText(), password);
				Customer customer = new Customer(txtFullName.getText(), txtAddress.getText(),
						(Postcode) cboPostcode.getSelectedItem(), login);
				// Validate the created customer
				ErrorBuilder eb = new ErrorBuilder();
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

	public void register(Customer customer) {
		ErrorBuilder eb = new ErrorBuilder();
		// Handle model behaviour (failure stops registration)
		if (application.model.register(customer)) {
			// Wait for message handler to handle response
			if (application.model.registerResponse.waitForNew(ClientApplication.REQUEST_TIMEOUT)) {
				eb.append(application.model.registerResponse.readComments());
				// Verify and handle response
				if (!eb.isError()) {
					JOptionPane.showMessageDialog(null, "Registration Successful - You may now login",
							"Registration Successful", JOptionPane.INFORMATION_MESSAGE);
					application.setView("Login");
				}	
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
	
	@Override
	public void initialise() {
		refresh();
		clear();
	}

	@Override
	public void refresh() {
		refreshPostcodes();
	}

	private void clear() {
		txtUsername.setText(null);
		txtPassword.setText(null);
		txtConfirmPassword.setText(null);
		txtFullName.setText(null);
		txtAddress.setText(null);
		cboPostcode.setSelectedItem(null);
		txtUsername.requestFocusInWindow();
	}
	
	private void refreshPostcodes() {
		// Don't attempt update if popup visible
		if (cboPostcode.isPopupVisible()) {
			return;
		}	
		Postcode[] postcodes = null;
		// Handle model behaviour (failure clears postcodes)
		if (application.model.refreshPostcodes()) {
			// Wait for message handler to handle response
			if (application.model.postcodes.waitForNew(ClientApplication.REQUEST_TIMEOUT)) {
				postcodes = application.model.postcodes.readObject();
			}
		}
		// Load postcodes
		loadPostcodeComboBox(postcodes);	
	}

	private void loadPostcodeComboBox(Postcode[] postcodes) {
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
						JOptionPane.showMessageDialog(null, "Currently selected postcode no longer available",
								"Registration Error", JOptionPane.ERROR_MESSAGE);
					}
				}
				
			}
		});
	}

}

// class TitledComboBoxRenderer extends JLabel implements
// ListCellRenderer<Object> {
// private static final long serialVersionUID = -2195032473950218046L;
// private String title;
//
// public TitledComboBoxRenderer(String title) {
// this.title = title;
// }
//
// @Override
// public Component getListCellRendererComponent(JList<?> list, Object value,int
// index,
// boolean isSelected, boolean hasFocus) {
// setText((value == null ? title : value.toString()));
// return this;
// }
// }
