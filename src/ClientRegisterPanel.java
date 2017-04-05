import javax.swing.JPanel;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPasswordField;

public class ClientRegisterPanel extends JPanel {
	private static final long serialVersionUID = 2438993133579447512L;
	private static final int DEFAULT_TXT_COLUMNS = 15;

	ClientApplication application;

	private JTextField txtUsername;
	private JPasswordField txtPassword;
	private JPasswordField txtConfirmPassword;
	private JTextField txtFullName;
	private JTextField txtAddress;
	private JComboBox<String> cboPostcode;

	/**
	 * Create the panel.
	 */
	public ClientRegisterPanel(ClientApplication application) {
		this.application = application;
		init();
	}

	public void init() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
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

		cboPostcode = new JComboBox<String>();
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
				String confirmPassword = String.valueOf(txtPassword.getPassword());
				CustomerLogin login = new CustomerLogin(txtUsername.getText(), password);
				Customer customer = new Customer(txtFullName.getText(), txtAddress.getText(), 
						String.valueOf(cboPostcode.getSelectedItem()), login);
				// Validate the created customer
				Validator validator = new Validator();
				customer.validate(validator);
				if (CustomerLogin.isPasswordValid(password)) {	
					validator.addComment("- Passwords is not valid", true);
				}
				else if (!password.equals(confirmPassword)) {
					validator.addComment("- Passwords not the same", true);
				}
				// Attempt login if first stage of validation successfull
				if (validator.isValid()) {
					Message message = new Message(Message.Command.REGISTER_NEW_CUSTOMER, "", customer);
					application.getComms().sendMessage(message);
				}
				else {
					System.out.println(validator.getComment());
					application.setLoggedIn(null);
				}
				application.login();
			}	
		});

	}
}
