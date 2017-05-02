package client.gui;

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
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import business.model.Customer;
import business.model.CustomerLogin;
import business.model.Postcode;
import client.model.ClientModel;
import general.model.Message;
import general.utility.ErrorBuilder;

public class ClientRegisterPanel extends JPanel {
	private static final long serialVersionUID = 2438993133579447512L;
	private static final int DEFAULT_TXT_COLUMNS = 15;

	ClientModel application;

	private JTextField txtUsername;
	private JPasswordField txtPassword;
	private JPasswordField txtConfirmPassword;
	private JTextField txtFullName;
	private JTextField txtAddress;
	private JComboBox<Postcode> cboPostcode;

	/**
	 * Create the panel.
	 */
	public ClientRegisterPanel(ClientModel application) {
		this.application = application;
		init();
		refreshPostcodes();
	}

	private void refreshPostcodes() {
		// Request postcode update
		Message message = new Message(Message.Command.GET_POSTCODES);
		if (application.getComms().sendMessage(message)) {
			// Check for response from server
			if (application.getPostcodes().waitForNew(ClientModel.GET_POSTCODES_TIMEOUT)) {
				loadPostcodeComboBox();
			} else {
				System.out.println(
						"Response Timeout " + message.getCommand() + " " + message.toString());
			}
		}
	}

	private void loadPostcodeComboBox() {
		System.out.println("Updating postcode combobox...");
		// Create model with current postcode array
		Postcode[] postcodes = application.getPostcodes().read();
		// !!! Sort here or at business
		Arrays.sort(postcodes);
		DefaultComboBoxModel<Postcode> model = new DefaultComboBoxModel<>(postcodes);
		// Update model on EDT
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				// Remember the currently selected value
				Postcode curPostcode = (Postcode) cboPostcode.getSelectedItem();
				if (curPostcode != null) {
					model.setSelectedItem(curPostcode);
				}
				cboPostcode.setModel(model);
			}
		});
	}

	private void init() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] {0, 0, 0};
		gridBagLayout.rowHeights = new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[] {0.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights =
				new double[] {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);

		JLabel lblUsername = new JLabel("Username:");
		GridBagConstraints gbc_lblUsername = new GridBagConstraints();
		gbc_lblUsername.anchor = GridBagConstraints.EAST;
		gbc_lblUsername.insets = new Insets(0, 0, 5, 5);
		gbc_lblUsername.gridx = 0;
		gbc_lblUsername.gridy = 0;
		add(lblUsername, gbc_lblUsername);

		txtUsername = new JTextField();
		txtUsername.setColumns(DEFAULT_TXT_COLUMNS);
		GridBagConstraints gbc_txtUsername = new GridBagConstraints();
		gbc_txtUsername.insets = new Insets(0, 0, 5, 0);
		gbc_txtUsername.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtUsername.gridx = 1;
		gbc_txtUsername.gridy = 0;
		add(txtUsername, gbc_txtUsername);

		JLabel lblPassword = new JLabel("Password:");
		GridBagConstraints gbc_lblPassword = new GridBagConstraints();
		gbc_lblPassword.anchor = GridBagConstraints.EAST;
		gbc_lblPassword.insets = new Insets(0, 0, 5, 5);
		gbc_lblPassword.gridx = 0;
		gbc_lblPassword.gridy = 1;
		add(lblPassword, gbc_lblPassword);

		txtPassword = new JPasswordField();
		txtPassword.setColumns(DEFAULT_TXT_COLUMNS);
		GridBagConstraints gbc_txtPassword = new GridBagConstraints();
		gbc_txtPassword.insets = new Insets(0, 0, 5, 0);
		gbc_txtPassword.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtPassword.gridx = 1;
		gbc_txtPassword.gridy = 1;
		add(txtPassword, gbc_txtPassword);

		JLabel lblConfirmPassword = new JLabel("Confirm Password:");
		GridBagConstraints gbc_lblConfirmPassword = new GridBagConstraints();
		gbc_lblConfirmPassword.insets = new Insets(0, 0, 5, 5);
		gbc_lblConfirmPassword.anchor = GridBagConstraints.EAST;
		gbc_lblConfirmPassword.gridx = 0;
		gbc_lblConfirmPassword.gridy = 2;
		add(lblConfirmPassword, gbc_lblConfirmPassword);

		txtConfirmPassword = new JPasswordField();
		txtConfirmPassword.setColumns(DEFAULT_TXT_COLUMNS);
		GridBagConstraints gbc_txtConfirmPassword = new GridBagConstraints();
		gbc_txtConfirmPassword.insets = new Insets(0, 0, 5, 0);
		gbc_txtConfirmPassword.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtConfirmPassword.gridx = 1;
		gbc_txtConfirmPassword.gridy = 2;
		add(txtConfirmPassword, gbc_txtConfirmPassword);

		JLabel lblFullName = new JLabel("Full Name:");
		GridBagConstraints gbc_lblFullName = new GridBagConstraints();
		gbc_lblFullName.anchor = GridBagConstraints.EAST;
		gbc_lblFullName.insets = new Insets(0, 0, 5, 5);
		gbc_lblFullName.gridx = 0;
		gbc_lblFullName.gridy = 3;
		add(lblFullName, gbc_lblFullName);

		txtFullName = new JTextField();
		txtFullName.setColumns(DEFAULT_TXT_COLUMNS);
		GridBagConstraints gbc_txtFullName = new GridBagConstraints();
		gbc_txtFullName.insets = new Insets(0, 0, 5, 0);
		gbc_txtFullName.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtFullName.gridx = 1;
		gbc_txtFullName.gridy = 3;
		add(txtFullName, gbc_txtFullName);

		JLabel lblAddress = new JLabel("Address:");
		GridBagConstraints gbc_lblAddress = new GridBagConstraints();
		gbc_lblAddress.insets = new Insets(0, 0, 5, 5);
		gbc_lblAddress.anchor = GridBagConstraints.EAST;
		gbc_lblAddress.gridx = 0;
		gbc_lblAddress.gridy = 4;
		add(lblAddress, gbc_lblAddress);

		txtAddress = new JTextField();
		txtAddress.setColumns(DEFAULT_TXT_COLUMNS);
		GridBagConstraints gbc_txtAddress = new GridBagConstraints();
		gbc_txtAddress.anchor = GridBagConstraints.NORTH;
		gbc_txtAddress.insets = new Insets(0, 0, 5, 0);
		gbc_txtAddress.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtAddress.gridx = 1;
		gbc_txtAddress.gridy = 4;
		add(txtAddress, gbc_txtAddress);

		JLabel lblPostcode = new JLabel("Postcode:");
		GridBagConstraints gbc_lblPostcode = new GridBagConstraints();
		gbc_lblPostcode.insets = new Insets(0, 0, 5, 5);
		gbc_lblPostcode.anchor = GridBagConstraints.EAST;
		gbc_lblPostcode.gridx = 0;
		gbc_lblPostcode.gridy = 5;
		add(lblPostcode, gbc_lblPostcode);

		cboPostcode = new JComboBox<>();
		// cboPostcode.setRenderer(new TitledComboBoxRenderer("PLEASE SELECT"));
		// cboPostcode.setSelectedIndex(-1);
		GridBagConstraints gbc_cboPostcode = new GridBagConstraints();
		gbc_cboPostcode.insets = new Insets(0, 0, 5, 0);
		gbc_cboPostcode.fill = GridBagConstraints.HORIZONTAL;
		gbc_cboPostcode.gridx = 1;
		gbc_cboPostcode.gridy = 5;
		add(cboPostcode, gbc_cboPostcode);

		JButton btnRegister = new JButton("Register");
		GridBagConstraints gbc_btnRegister = new GridBagConstraints();
		gbc_btnRegister.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnRegister.gridwidth = 2;
		gbc_btnRegister.gridx = 0;
		gbc_btnRegister.gridy = 6;
		add(btnRegister, gbc_btnRegister);

		btnRegister.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Create customer
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
				// Attempt login if first stage of validation successful
				if (!eb.isError()) {
					Message message =
							new Message(Message.Command.REGISTER_NEW_CUSTOMER, null, customer);
					if (!application.getComms().sendMessage(message)) {
						eb.addError("Could not connect to server");
					} else {
						// Attempt login
						eb.append(application.login());
					}
				}
				System.out.println(eb.listComments(""));
				refreshPostcodes();
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
