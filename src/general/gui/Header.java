package general.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;

import javax.swing.JLabel;
import javax.swing.JPanel;

import general.utility.Utilities;

public class Header extends JPanel {
	/**
	 *
	 */
	private static final long serialVersionUID = -6888273333186980776L;
	private static final Color DEFAULT_BACKGROUND = new Color(204, 0, 0);
	private static final Color DEFAULT_FOREGROUND = new Color(255, 255, 255);
	private static final Color DEFAULT_HIGHLIGHT = new Color(224, 102, 102);

	private final String title;
	private final ViewHandler viewHandler;

	private UserAccountPanel pnlUserAccount;
	private JLabel lblStatusIndicator;
	private JLabel lblPage;
	private NavigationBar navigationBar;

	public Header(ViewHandler viewHandler, String title) {
		this.viewHandler = viewHandler;
		this.title = title;
		init();
	}

	private void init() {
		setBackground(Color.WHITE);
		final GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] {0, 0, 0};
		gridBagLayout.rowHeights = new int[] {0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[] {0.0, 1.0};
		gridBagLayout.rowWeights = new double[] {0.0, 0.5, 0.5, 0.0};
		setLayout(gridBagLayout);

		lblStatusIndicator = new JLabel();
		final GridBagConstraints gbc_lblStatusIndicator = new GridBagConstraints();
		gbc_lblStatusIndicator.insets = new Insets(5, 0, 0, 5);
		gbc_lblStatusIndicator.gridx = 2;
		gbc_lblStatusIndicator.gridy = 0;
		add(lblStatusIndicator, gbc_lblStatusIndicator);

		final JLabel lblLogo = new JLabel();
		Utilities.setLabelImage(lblLogo, new File("resources/imgLogo.png"));
		final GridBagConstraints gbc_lblLogo = new GridBagConstraints();
		gbc_lblLogo.gridheight = 2;
		gbc_lblLogo.insets = new Insets(10, 15, 10, 15);
		gbc_lblLogo.fill = GridBagConstraints.BOTH;
		gbc_lblLogo.gridx = 0;
		gbc_lblLogo.gridy = 1;
		add(lblLogo, gbc_lblLogo);

		final JLabel lblCompanyName =
				new JLabel("<html>SPEEDI<span style=\"color:#cc0000\">SUSHI</span></html>");
		lblCompanyName.setFont(new Font("Trebuchet MS", Font.BOLD, 52));
		final GridBagConstraints gbc_lblCompanyName = new GridBagConstraints();
		gbc_lblCompanyName.gridwidth = 2;
		gbc_lblCompanyName.anchor = GridBagConstraints.SOUTHWEST;
		gbc_lblCompanyName.fill = GridBagConstraints.BOTH;
		gbc_lblCompanyName.gridx = 1;
		gbc_lblCompanyName.gridy = 1;
		add(lblCompanyName, gbc_lblCompanyName);

		lblPage = new JLabel();
		lblPage.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		final GridBagConstraints gbc_lblPage = new GridBagConstraints();
		gbc_lblPage.anchor = GridBagConstraints.NORTHWEST;
		gbc_lblPage.fill = GridBagConstraints.BOTH;
		gbc_lblPage.insets = new Insets(0, 0, 5, 0);
		gbc_lblPage.gridx = 1;
		gbc_lblPage.gridy = 2;
		add(lblPage, gbc_lblPage);


		final JPanel pnlToolbarFill = new JPanel();
		pnlToolbarFill.setBackground(new Color(204, 0, 0));
		final GridBagConstraints gbc_pnlToolbarFill = new GridBagConstraints();
		gbc_pnlToolbarFill.fill = GridBagConstraints.BOTH;
		gbc_pnlToolbarFill.gridx = 0;
		gbc_pnlToolbarFill.gridy = 3;
		add(pnlToolbarFill, gbc_pnlToolbarFill);

		navigationBar = new NavigationBar(viewHandler, DEFAULT_BACKGROUND, DEFAULT_FOREGROUND,
				DEFAULT_HIGHLIGHT);
		final GridBagConstraints gbc_navigationBar = new GridBagConstraints();
		gbc_navigationBar.gridwidth = 2;
		gbc_navigationBar.insets = new Insets(0, 0, 0, 0);
		gbc_navigationBar.fill = GridBagConstraints.BOTH;
		gbc_navigationBar.gridx = 1;
		gbc_navigationBar.gridy = 3;
		add(navigationBar, gbc_navigationBar);

		setStatus(null, null);
	}

	public void setAccountPanel(UserAccountPanel userAccountPanel) {
		pnlUserAccount = userAccountPanel;
		pnlUserAccount.setBackground(Color.WHITE);
		final GridBagConstraints gbc_pnlUserAccount = new GridBagConstraints();
		gbc_pnlUserAccount.insets = new Insets(5, 0, 0, 0);
		gbc_pnlUserAccount.gridwidth = 2;
		gbc_pnlUserAccount.fill = GridBagConstraints.BOTH;
		gbc_pnlUserAccount.gridx = 0;
		gbc_pnlUserAccount.gridy = 0;
		add(pnlUserAccount, gbc_pnlUserAccount);
	}

	public void setStatus(Status status, String message) {
		if (status == null) {
			lblStatusIndicator.setVisible(false);
			return;
		}
		lblStatusIndicator.setToolTipText(message);
		lblStatusIndicator.setVisible(true);
		switch (status) {
			case RED:
				Utilities.setLabelImage(lblStatusIndicator, new File("resources/imgStatusRed.png"));
				break;
			case AMBER:
				Utilities.setLabelImage(lblStatusIndicator,
						new File("resources/imgStatusAmber.png"));
				break;
			case GREEN:
				Utilities.setLabelImage(lblStatusIndicator,
						new File("resources/imgStatusGreen.png"));
				break;
			default:
				break;
		}
	}

	public void setPage(String page) {
		final String text = String.format("<html><b>%s -</b> %s</html>", title, page);
		lblPage.setText(text);
	}

	public NavigationBar getNavigationBar() {
		return navigationBar;
	}

	public static enum Status {
		RED, AMBER, GREEN
	}
}
