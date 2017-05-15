package general.gui;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JPanel;

import general.utility.Utilities;


public class Header extends JPanel {
	private static final long serialVersionUID = -6888273333186980776L;
	// Default colouring
	private static final Color DEFAULT_BACKGROUND = new Color(204, 0, 0);
	private static final Color DEFAULT_FOREGROUND = new Color(255, 255, 255);
	private static final Color DEFAULT_HIGHLIGHT = new Color(224, 102, 102);
	
	private final String identifier;
	
	// Header objects
	private UserAccountHeader pnlUserAccount;
	private JLabel lblPage;	
	// Handled navigation bars
	private Map<String, NavigationBar> navigationBars = new HashMap<>();
	private JPanel pnlNavigationBar;
	private CardLayout cl_pnlNavigationBar;
	
	public Header(String identifier) {
		this.identifier = identifier;
		init();
	}

	private void init() {
		// Define layout
		final GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] {0, 0, 0};
		gridBagLayout.rowHeights = new int[] {0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[] {0.0, 1.0};
		gridBagLayout.rowWeights = new double[] {0.0, 0.5, 0.5, 0.0};
		setLayout(gridBagLayout);
		setBackground(Color.WHITE);
		
		// [Content] <- 'Logo' Image Label
		final JLabel lblLogo = new JLabel();
		Utilities.setLabelImage(lblLogo, new File("resources/imgLogo.png"));
		final GridBagConstraints gbc_lblLogo = new GridBagConstraints();
		gbc_lblLogo.gridheight = 2;
		gbc_lblLogo.insets = new Insets(10, 15, 10, 15);
		gbc_lblLogo.fill = GridBagConstraints.BOTH;
		gbc_lblLogo.gridx = 0;
		gbc_lblLogo.gridy = 1;
		add(lblLogo, gbc_lblLogo);

		// [Content] <- 'Company Name' Label
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

		// [Content] <- 'Page' Label
		lblPage = new JLabel();
		lblPage.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		final GridBagConstraints gbc_lblPage = new GridBagConstraints();
		gbc_lblPage.anchor = GridBagConstraints.NORTHWEST;
		gbc_lblPage.fill = GridBagConstraints.BOTH;
		gbc_lblPage.insets = new Insets(0, 0, 5, 0);
		gbc_lblPage.gridx = 1;
		gbc_lblPage.gridy = 2;
		add(lblPage, gbc_lblPage);

		// [Content] <- Toolbar fill extension (for buffer)
		final JPanel pnlToolbarFill = new JPanel();
		pnlToolbarFill.setBackground(DEFAULT_BACKGROUND);
		final GridBagConstraints gbc_pnlToolbarFill = new GridBagConstraints();
		gbc_pnlToolbarFill.fill = GridBagConstraints.BOTH;
		gbc_pnlToolbarFill.gridx = 0;
		gbc_pnlToolbarFill.gridy = 3;
		add(pnlToolbarFill, gbc_pnlToolbarFill);

		// [Content] <- 'Navigation Bar' Card Layout Panel
		pnlNavigationBar = new JPanel();
		cl_pnlNavigationBar = new CardLayout();
		pnlNavigationBar.setLayout(cl_pnlNavigationBar);
		final GridBagConstraints gbc_pnlNavigationBar = new GridBagConstraints();
		gbc_pnlNavigationBar.gridwidth = 2;
		gbc_pnlNavigationBar.insets = new Insets(0, 0, 0, 0);
		gbc_pnlNavigationBar.fill = GridBagConstraints.BOTH;
		gbc_pnlNavigationBar.gridx = 1;
		gbc_pnlNavigationBar.gridy = 3;
		add(pnlNavigationBar, gbc_pnlNavigationBar);
	}

	/**
	 * Attach an account header to the header, formatting it and displaying as appropriate. 
	 *  
	 * @param userAccountHeader Account header to attach.
	 */
	public void setAccountHeader(UserAccountHeader userAccountHeader) {
		pnlUserAccount = userAccountHeader;
		pnlUserAccount.setBackground(Color.WHITE);
		final GridBagConstraints gbc_pnlUserAccount = new GridBagConstraints();
		gbc_pnlUserAccount.insets = new Insets(5, 0, 0, 0);
		gbc_pnlUserAccount.gridwidth = 2;
		gbc_pnlUserAccount.fill = GridBagConstraints.BOTH;
		gbc_pnlUserAccount.gridx = 0;
		gbc_pnlUserAccount.gridy = 0;
		add(pnlUserAccount, gbc_pnlUserAccount);
	}

	/**
	 * Set the current page title and update navigation bars to identify selected page.
	 * 
	 * @param page Page to select
	 * @param viewHandler View handler page is on
	 */
	public void setPage(String page, ViewHandler viewHandler) {
		// Set page title
		final String text = String.format("<html><b>%s -</b> %s</html>", identifier, page);
		lblPage.setText(text);
		// Update navigation bars
		for (NavigationBar navigationBar : navigationBars.values()) {
			if (navigationBar.getViewHandler() == viewHandler) {
				navigationBar.setSelected(page);
			}
		}
	}

	/**
	 * Get the navigation bar with a given identifier.
	 * 
	 * @param identifier Navigation bar identifier
	 * @return Navigation bar that matches identifier
	 */
	public NavigationBar getNavigationBar(String identifier) {
		return navigationBars.get(identifier);
	}
	
	/**
	 * Show the navigation bar with the given identifier.
	 * 
	 * @param identifier Navigation bar identifier
	 */
	public void showNavigationBar(String identifier) {
		cl_pnlNavigationBar.show(pnlNavigationBar, identifier);
	}
	
	/**
	 * Create a new navigation bar for a given view handler and represented with the given string
	 * identifier bar. The newly created navigation bar can then be shown or retrieved with the
	 * given identifier.
	 * 
	 * @param identifier Identifier for navigation bar storage
	 * @param viewHandler View handler for navigation bar to affect
	 * @return New navigation bar
	 */
	public NavigationBar addNavigationBar(String identifier, ViewHandler viewHandler) {
		NavigationBar navigationBar = new NavigationBar(viewHandler, 
				DEFAULT_BACKGROUND, DEFAULT_FOREGROUND, DEFAULT_HIGHLIGHT);
		// Store navigation bar
		navigationBars.put(identifier, navigationBar);
		// Put navigation bar in card layout
		pnlNavigationBar.add(navigationBar);
		cl_pnlNavigationBar.addLayoutComponent(navigationBar, identifier);
		return navigationBar;
	}

}
