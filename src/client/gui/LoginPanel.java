package client.gui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import business.model.CustomerLogin;
import general.gui.View;
import java.awt.Font;

/**
 * A panel with components and implementation for logging in using an existing user.
 * 
 * @author David Jones [dsj1n15]
 */
public class LoginPanel extends JPanel implements View {
	private static final long serialVersionUID = 6429716243336673633L;
	// Client application
	private final ClientApplication application;
	// Login objects
	private JTextField txtUsername;
	private JPasswordField txtPassword;
	private JButton btnLogin;

	/**
	 * Instantiate a new login panel for a client application.
	 */
	public LoginPanel(ClientApplication application) {
		this.application = application;
		init();
	}

	/**
	 * Create the panel
	 */
	private void init() {
		// Set layout
		GridBagLayout layout = new GridBagLayout();
		layout.columnWidths = new int[] {20, 0, 20};
		layout.rowHeights = new int[] {0, 0, 0};
		layout.columnWeights = new double[] {0.4, 0.2, 0.4};
		layout.rowWeights = new double[] {0.5, 0.0, 0.5};
		setLayout(layout);
		setBackground(Color.WHITE);
		
		// [Content Panel]
		JPanel pnlContent = new JPanel();
		pnlContent.setBorder(BorderFactory.createCompoundBorder(				
				BorderFactory.createLineBorder(Color.BLACK), 
				BorderFactory.createEmptyBorder(5, 5, 5, 5)));
		GridBagLayout gbl_pnlContent = new GridBagLayout();
		gbl_pnlContent.columnWidths = new int[] {0, 0};
		gbl_pnlContent.rowHeights = new int[] {0, 0, 0, 0};
		gbl_pnlContent.columnWeights = new double[] {0, 1.0};
		gbl_pnlContent.rowWeights = new double[] {0.0, 0.0, 0.0, 0.0};
		pnlContent.setLayout(gbl_pnlContent);
		GridBagConstraints gbc_pnlContent = new GridBagConstraints();
		gbc_pnlContent.fill = GridBagConstraints.BOTH;
		gbc_pnlContent.gridx = 1;
		gbc_pnlContent.gridy = 1;
		add(pnlContent, gbc_pnlContent);
		
		// [Content Panel] <- 'Information' Label
		JLabel lblInformation = new JLabel("<html>Enter customer login details:</html>");
		lblInformation.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		GridBagConstraints gbc_lblInformation = new GridBagConstraints();
		gbc_lblInformation.gridwidth = 2;
		gbc_lblInformation.anchor = GridBagConstraints.WEST;
		gbc_lblInformation.insets = new Insets(10, 5, 10, 5);
		gbc_lblInformation.gridx = 0;
		gbc_lblInformation.gridy = 0;
		pnlContent.add(lblInformation, gbc_lblInformation);

		// [Content Panel] <- 'Username' Label
		JLabel lblUsername = new JLabel("Username:");
		GridBagConstraints gbc_lblUsername = new GridBagConstraints();
		gbc_lblUsername.insets = new Insets(0, 5, 5, 5);
		gbc_lblUsername.anchor = GridBagConstraints.EAST;
		gbc_lblUsername.gridx = 0;
		gbc_lblUsername.gridy = 1;
		pnlContent.add(lblUsername, gbc_lblUsername);
		// [Content Panel] <- 'Username' TextBox
		txtUsername = new JTextField();
		GridBagConstraints gbc_txtLoginUsername = new GridBagConstraints();
		gbc_txtLoginUsername.insets = new Insets(0, 0, 5, 5);
		gbc_txtLoginUsername.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtLoginUsername.gridx = 1;
		gbc_txtLoginUsername.gridy = 1;
		pnlContent.add(txtUsername, gbc_txtLoginUsername);
		
		// [Content Panel] <- 'Password' Label
		JLabel lblPassword = new JLabel("Password:");
		GridBagConstraints gbc_lblPassword = new GridBagConstraints();
		gbc_lblPassword.insets = new Insets(0, 5, 5, 5);
		gbc_lblPassword.anchor = GridBagConstraints.EAST;
		gbc_lblPassword.gridx = 0;
		gbc_lblPassword.gridy = 2;
		pnlContent.add(lblPassword, gbc_lblPassword);
		// [Content Panel] <- 'Password' TextBox
		txtPassword = new JPasswordField();
		GridBagConstraints gbc_txtLoginPassword = new GridBagConstraints();
		gbc_txtLoginPassword.insets = new Insets(0, 0, 5, 5);
		gbc_txtLoginPassword.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtLoginPassword.gridx = 1;
		gbc_txtLoginPassword.gridy = 2;
		pnlContent.add(txtPassword, gbc_txtLoginPassword);

		// [Content Panel] <- 'Login' Button
		btnLogin = new JButton("Login");
		GridBagConstraints gbc_btnLogin = new GridBagConstraints();
		gbc_btnLogin.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnLogin.gridwidth = 2;
		gbc_btnLogin.insets = new Insets(0, 5, 5, 5);
		gbc_btnLogin.gridx = 0;
		gbc_btnLogin.gridy = 3;
		pnlContent.add(btnLogin, gbc_btnLogin);

		// [Login Button] - Login to the application
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

	@Override
	public void refresh() {
		// do nothing
	}
	
	@Override
	public JButton getAcceptButton() {
		return btnLogin;
	}
	
	/**
	 * Clear the login components.
	 */
	private void clear() {
		txtUsername.setText(null);
		txtPassword.setText(null);
		txtUsername.requestFocus();
	}
	
}
