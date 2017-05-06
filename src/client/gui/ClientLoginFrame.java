package client.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import client.model.ClientModel;

public class ClientLoginFrame extends JFrame {
	private static final long serialVersionUID = -7786280022594294907L;
	private static final String COMPANY_NAME = "SpeediSushi";
	private static final String FORM_TITLE = "Client Application";
	private static final String GUI_TITLE =
			String.format("<html><b>%s</b> - %s</html>", COMPANY_NAME, FORM_TITLE);

	private final ClientModel application;

	private JPanel contentPane;
	private JTabbedPane tabs;
	private JPanel pnlLogin;
	private JPanel pnlRegister;

	/**
	 * Create the frame.
	 */
	public ClientLoginFrame(ClientModel application) {
		this.application = application;
		init();
	}

	public void init() {
		// Define frame properties
		setTitle(FORM_TITLE);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Create bordered content frame
		contentPane = new JPanel();
		contentPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout());
		setContentPane(contentPane);

		// Add title
		JLabel lblGUITitle = new JLabel(GUI_TITLE);
		lblGUITitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblGUITitle.setFont(new Font("Trebuchet MS", Font.PLAIN, 21));
		lblGUITitle.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		contentPane.add(lblGUITitle, BorderLayout.NORTH);

		// Create a tab panel
		tabs = new JTabbedPane(SwingConstants.TOP);
		final Dimension dimTabsInit = tabs.getPreferredSize();
		contentPane.add(tabs, BorderLayout.CENTER);
		// Monitor tab size, resizing and positioning form as appropriate
		tabs.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				// Determine new preferred size
				Dimension dimCur = tabs.getSelectedComponent().getPreferredSize();
				Dimension dimNew = new Dimension(dimTabsInit.width + dimCur.width,
						dimTabsInit.height + dimCur.height);
				tabs.setPreferredSize(dimNew);
				// Resize JFrame and position
				Dimension dimFrameOld = getSize();
				Dimension dimFrameNew = getPreferredSize();
				Point newPos = new Point(getX() + (dimFrameOld.width - dimFrameNew.width) / 2,
						getY() + (dimFrameOld.height - dimFrameNew.height) / 2);
				setBounds(newPos.x, newPos.y, dimFrameNew.width, dimFrameNew.height);
			}
		});

		// Create login panel and add it to tabs
		pnlLogin = new ClientLoginPanel(application);
		pnlLogin.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		tabs.add("Login", pnlLogin);

		// Create registration panel add add it to tabs
		pnlRegister = new RegisterPanel(application);
		pnlRegister.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		tabs.add("Register", pnlRegister);

		// Set form location to centre
		pack();
		setLocationRelativeTo(null);
	}

}
