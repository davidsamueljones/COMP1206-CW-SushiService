import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

public class ClientLoginPanel extends JPanel {
	private static final long serialVersionUID = 6429716243336673633L;
	private static final int DEFAULT_TXT_COLUMNS = 15;
	
	private ClientApplication application;
	
	private JTextField txtUsername;
	private JPasswordField txtPassword;
	private JButton btnLogin;
	
	/**
	 * Create the panel.
	 */
	public ClientLoginPanel(ClientApplication application) {
		this.application = application;
		init();
	}

	private void init() {
		// Set layout
		GridBagLayout layout = new GridBagLayout();
		layout.columnWidths = new int[]{0, 0};
		layout.rowHeights = new int[]{0, 0, 0};
		layout.columnWeights = new double[]{0.0, 1.0};
		layout.rowWeights = new double[]{0.0, 0.0, 0.0};
		setLayout(layout);
		
		JLabel lblUsername = new JLabel("Username:");
		GridBagConstraints gbc_lblUsername = new GridBagConstraints();
		gbc_lblUsername.insets = new Insets(0, 0, 5, 5);
		gbc_lblUsername.anchor = GridBagConstraints.EAST;
		gbc_lblUsername.gridx = 0;
		gbc_lblUsername.gridy = 0;
		add(lblUsername, gbc_lblUsername);
		
		txtUsername = new JTextField();
		txtUsername.setColumns(DEFAULT_TXT_COLUMNS);
		GridBagConstraints gbc_txtLoginUsername = new GridBagConstraints();
		gbc_txtLoginUsername.insets = new Insets(0, 0, 5, 5);
		gbc_txtLoginUsername.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtLoginUsername.gridx = 1;
		gbc_txtLoginUsername.gridy = 0;
		add(txtUsername, gbc_txtLoginUsername);
		
		JLabel lblPassword = new JLabel("Password:");
		GridBagConstraints gbc_lblPassword = new GridBagConstraints();
		gbc_lblPassword.insets = new Insets(0, 0, 5, 5);
		gbc_lblPassword.anchor = GridBagConstraints.EAST;
		gbc_lblPassword.gridx = 0;
		gbc_lblPassword.gridy = 1;
		add(lblPassword, gbc_lblPassword);
		
		txtPassword = new JPasswordField();
		txtPassword.setColumns(DEFAULT_TXT_COLUMNS);
		GridBagConstraints gbc_txtLoginPassword = new GridBagConstraints();
		gbc_txtLoginPassword.insets = new Insets(0, 0, 5, 5);
		gbc_txtLoginPassword.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtLoginPassword.gridx = 1;
		gbc_txtLoginPassword.gridy = 1;
		add(txtPassword, gbc_txtLoginPassword);
		
		btnLogin = new JButton("Login");
		GridBagConstraints gbc_btnLogin = new GridBagConstraints();
		gbc_btnLogin.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnLogin.gridwidth = 2;
		gbc_btnLogin.insets = new Insets(0, 0, 5, 5);
		gbc_btnLogin.gridx = 0;
		gbc_btnLogin.gridy = 2;
		add(btnLogin, gbc_btnLogin);
		
		btnLogin.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				CustomerLogin login = new CustomerLogin(txtUsername.getText(), 
							String.valueOf(txtPassword.getPassword()));
				Message message = new Message(Message.Command.SUBMIT_LOGIN, "", login);
				application.getComms().sendMessage(message);
				application.login();
			}	
		});
		
		

		
	}
}
