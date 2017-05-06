package client.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import business.model.CustomerLogin;
import client.model.ClientModel;
import general.gui.View;
import general.model.Message;
import general.utility.ErrorBuilder;

public class LoginPanel extends JPanel implements View {
	private static final long serialVersionUID = 6429716243336673633L;
	private static final int DEFAULT_TXT_COLUMNS = 15;

	private final ClientApplication application;

	private JTextField txtUsername;
	private JPasswordField txtPassword;
	private JButton btnLogin;

	/**
	 * Create the panel.
	 */
	public LoginPanel(ClientApplication application) {
		this.application = application;
		init();
	}

	private void init() {
		// Set layout
		GridBagLayout layout = new GridBagLayout();
		layout.columnWidths = new int[] {20, 0, 0, 20};
		layout.rowHeights = new int[] {0, 0, 0, 0, 0, 0};
		layout.columnWeights = new double[] {0.4, 0, 0.2, 0.4};
		layout.rowWeights = new double[] {0.5, 0.0, 0.0, 0.0, 0.0, 0.5};
		setLayout(layout);

		JLabel lblInformation = new JLabel("<html>Enter customer login details:</html>");
		GridBagConstraints gbc_lblInformation = new GridBagConstraints();
		gbc_lblInformation.gridwidth = 2;
		gbc_lblInformation.anchor = GridBagConstraints.WEST;
		gbc_lblInformation.insets = new Insets(10, 5, 10, 5);
		gbc_lblInformation.gridx = 1;
		gbc_lblInformation.gridy = 1;
		add(lblInformation, gbc_lblInformation);
		
		JLabel lblUsername = new JLabel("Username:");
		GridBagConstraints gbc_lblUsername = new GridBagConstraints();
		gbc_lblUsername.insets = new Insets(0, 0, 5, 5);
		gbc_lblUsername.anchor = GridBagConstraints.EAST;
		gbc_lblUsername.gridx = 1;
		gbc_lblUsername.gridy = 2;
		add(lblUsername, gbc_lblUsername);

		txtUsername = new JTextField();
		txtUsername.setColumns(DEFAULT_TXT_COLUMNS);
		GridBagConstraints gbc_txtLoginUsername = new GridBagConstraints();
		gbc_txtLoginUsername.insets = new Insets(0, 0, 5, 5);
		gbc_txtLoginUsername.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtLoginUsername.gridx = 2;
		gbc_txtLoginUsername.gridy = 2;
		add(txtUsername, gbc_txtLoginUsername);

		JLabel lblPassword = new JLabel("Password:");
		GridBagConstraints gbc_lblPassword = new GridBagConstraints();
		gbc_lblPassword.insets = new Insets(0, 0, 5, 5);
		gbc_lblPassword.anchor = GridBagConstraints.EAST;
		gbc_lblPassword.gridx = 1;
		gbc_lblPassword.gridy = 3;
		add(lblPassword, gbc_lblPassword);

		txtPassword = new JPasswordField();
		txtPassword.setColumns(DEFAULT_TXT_COLUMNS);
		GridBagConstraints gbc_txtLoginPassword = new GridBagConstraints();
		gbc_txtLoginPassword.insets = new Insets(0, 0, 5, 5);
		gbc_txtLoginPassword.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtLoginPassword.gridx = 2;
		gbc_txtLoginPassword.gridy = 3;
		add(txtPassword, gbc_txtLoginPassword);

		btnLogin = new JButton("Login");
		GridBagConstraints gbc_btnLogin = new GridBagConstraints();
		gbc_btnLogin.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnLogin.gridwidth = 2;
		gbc_btnLogin.insets = new Insets(0, 0, 5, 5);
		gbc_btnLogin.gridx = 1;
		gbc_btnLogin.gridy = 4;
		add(btnLogin, gbc_btnLogin);

		// !!! Login
		btnLogin.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				CustomerLogin login = new CustomerLogin(txtUsername.getText(),
						String.valueOf(txtPassword.getPassword()));
				application.login(login);
			}
		});

	}

	@Override
	public void initialise() {
		clear();
	}

	private void clear() {
		txtUsername.setText(null);
		txtPassword.setText(null);
		txtUsername.requestFocusInWindow();
	}

	@Override
	public void refresh() {
		// do nothing
	}
}
