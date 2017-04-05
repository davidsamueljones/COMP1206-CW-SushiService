import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import java.awt.GridBagLayout;
import java.awt.GridLayout;
import javax.swing.JSplitPane;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.JLabel;
import javax.swing.JFormattedTextField;

public class ClientOrderFrame extends JFrame {
	private static final String COMPANY_NAME = "Absolutely Sushi";
	private static final String FORM_TITLE = "Client Application";
	private static final String GUI_TITLE = String.format("<html><b>%s</b> - %s</html>", COMPANY_NAME, FORM_TITLE);
	
	private static final String[] EXISTING_ORDERS_COLUMN_TITLES={"Date", "Status", "Price", "Dishes"};
	private static final String[] DISHES_COLUMN_TITLES = {"Name", "Stock", "Price"};
	private static final String[] ORDER_COLUMN_TITLES = {"Name", "Quantity", "Price"};
	private static final String[][] EMPTY_REPLACE_THIS = {};
	private JPanel contentPane;
	private JTable tblExistingOrders;
	private JTable tblDishes;
	private JTable tblOrder;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClientOrderFrame frame = new ClientOrderFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ClientOrderFrame() {
		init();
	}
	
	public void init() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(768, 480);
        setLocationRelativeTo(null);
        
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{0, 0, 0};
		gbl_contentPane.rowHeights = new int[]{0, 0, 0};
		gbl_contentPane.columnWeights = new double[]{0.5, 0.0, 0.5};
		gbl_contentPane.rowWeights = new double[]{0.0, 0.3, 0.35, 0.35};
		contentPane.setLayout(gbl_contentPane);
		
		// Add title
		JLabel lblGUITitle = new JLabel(GUI_TITLE);
		lblGUITitle.setFont(new Font("Trebuchet MS", Font.PLAIN, 21));
		GridBagConstraints gbc_lblGUITitle = new GridBagConstraints();
		gbc_lblGUITitle.gridwidth = 3;
		gbc_lblGUITitle.gridx = 0;
		gbc_lblGUITitle.gridy = 0;
		gbc_lblGUITitle.insets = new Insets(10, 10, 10, 10);
		contentPane.add(lblGUITitle, gbc_lblGUITitle);
		
		JPanel pnlExistingOrders = new JPanel();
		pnlExistingOrders.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), 
				"Existing Customer Orders", TitledBorder.LEFT, TitledBorder.TOP));
		GridBagConstraints gbc_pnlExistingOrders = new GridBagConstraints();
		gbc_pnlExistingOrders.fill = GridBagConstraints.BOTH;
		gbc_pnlExistingOrders.gridwidth = 3;
		gbc_pnlExistingOrders.gridx = 0;
		gbc_pnlExistingOrders.gridy = 1;
		contentPane.add(pnlExistingOrders, gbc_pnlExistingOrders);
		GridBagLayout gbl_pnlExistingOrders = new GridBagLayout();
		gbl_pnlExistingOrders.columnWeights = new double[]{1.0};
		gbl_pnlExistingOrders.rowWeights = new double[]{1.0};
		pnlExistingOrders.setLayout(gbl_pnlExistingOrders);
		
		tblExistingOrders = new JTable(EMPTY_REPLACE_THIS, EXISTING_ORDERS_COLUMN_TITLES);			
		tblExistingOrders.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tblExistingOrders.setGridColor(Color.LIGHT_GRAY);
		JScrollPane scrExistingOrders = new JScrollPane(tblExistingOrders);
		scrExistingOrders.setPreferredSize(new Dimension(0,0));
		scrExistingOrders.setBackground(pnlExistingOrders.getBackground());
		GridBagConstraints gbc_scrExistingOrders = new GridBagConstraints();
		gbc_scrExistingOrders.insets = new Insets(5, 5, 5, 5);
		gbc_scrExistingOrders.fill = GridBagConstraints.BOTH;
		pnlExistingOrders.add(scrExistingOrders, gbc_scrExistingOrders);
		
		JPanel pnlDishes = new JPanel();
		pnlDishes.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), 
				"Available Dishes", TitledBorder.LEFT, TitledBorder.TOP));
		GridBagConstraints gbc_pnlDishes = new GridBagConstraints();
		gbc_pnlDishes.gridheight = 2;
		gbc_pnlDishes.fill = GridBagConstraints.BOTH;
		gbc_pnlDishes.gridx = 0;
		gbc_pnlDishes.gridy = 2;
		contentPane.add(pnlDishes, gbc_pnlDishes);
		
		GridBagLayout gbl_pnlDishes = new GridBagLayout();
		gbl_pnlDishes.columnWidths = new int[]{0};
		gbl_pnlDishes.rowHeights = new int[]{0};
		gbl_pnlDishes.columnWeights = new double[]{1.0};
		gbl_pnlDishes.rowWeights = new double[]{1.0};
		pnlDishes.setLayout(gbl_pnlDishes);
		
		tblDishes = new JTable(EMPTY_REPLACE_THIS, DISHES_COLUMN_TITLES);			
		tblDishes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tblDishes.setGridColor(Color.LIGHT_GRAY);
		JScrollPane scrDishes = new JScrollPane(tblDishes);
		scrDishes.setPreferredSize(new Dimension(0,0));
		scrDishes.setBackground(pnlDishes.getBackground());
		GridBagConstraints gbc_scrDishes = new GridBagConstraints();
		gbc_scrDishes.insets = new Insets(5, 5, 5, 5);
		gbc_scrDishes.fill = GridBagConstraints.BOTH;
		pnlDishes.add(scrDishes, gbc_scrDishes);
		
		JButton btnAdd = new JButton(">");
		GridBagConstraints gbc_btnAdd = new GridBagConstraints();
		gbc_btnAdd.fill = GridBagConstraints.VERTICAL;
		gbc_btnAdd.insets = new Insets(5, 0, 2, 0);
		gbc_btnAdd.gridx = 1;
		gbc_btnAdd.gridy = 2;
		contentPane.add(btnAdd, gbc_btnAdd);
		
		JButton btnRemove = new JButton("<");
		GridBagConstraints gbc_btnRemove = new GridBagConstraints();
		gbc_btnRemove.fill = GridBagConstraints.VERTICAL;
		gbc_btnRemove.insets = new Insets(2, 0, 5, 0);
		gbc_btnRemove.gridx = 1;
		gbc_btnRemove.gridy = 3;
		contentPane.add(btnRemove, gbc_btnRemove);
		
		JPanel pnlOrder = new JPanel();
		pnlOrder.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), 
				"Order", TitledBorder.LEFT, TitledBorder.TOP));
		GridBagConstraints gbc_pnlOrder = new GridBagConstraints();
		gbc_pnlOrder.gridheight = 2;
		gbc_pnlOrder.fill = GridBagConstraints.BOTH;
		gbc_pnlOrder.gridx = 2;
		gbc_pnlOrder.gridy = 2;
		contentPane.add(pnlOrder, gbc_pnlOrder);
		GridBagLayout gbl_pnlOrder = new GridBagLayout();
		gbl_pnlOrder.columnWidths = new int[]{0, 0};
		gbl_pnlOrder.rowHeights = new int[]{0, 0, 0};
		gbl_pnlOrder.columnWeights = new double[]{0.0, 1.0};
		gbl_pnlOrder.rowWeights = new double[]{1.0, 0.0, 0.0};
		pnlOrder.setLayout(gbl_pnlOrder);
		
		tblOrder = new JTable(EMPTY_REPLACE_THIS, ORDER_COLUMN_TITLES);	
		tblOrder.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tblOrder.setGridColor(Color.LIGHT_GRAY);
		JScrollPane scrOrder = new JScrollPane(tblOrder);
		scrOrder.setPreferredSize(new Dimension(0,0));
		scrOrder.setBackground(pnlOrder.getBackground());
		GridBagConstraints gbc_scrOrder = new GridBagConstraints();
		gbc_scrOrder.gridwidth = 2;
		gbc_scrOrder.insets = new Insets(5, 5, 5, 5);
		gbc_scrOrder.fill = GridBagConstraints.BOTH;
		pnlOrder.add(scrOrder, gbc_scrOrder);
		
		JLabel lblTotalPriceDescription = new JLabel("Total Price:");
		GridBagConstraints gbc_lblTotalPriceDescription = new GridBagConstraints();
		gbc_lblTotalPriceDescription.insets = new Insets(5, 5, 5, 5);
		gbc_lblTotalPriceDescription.anchor = GridBagConstraints.EAST;
		gbc_lblTotalPriceDescription.gridx = 0;
		gbc_lblTotalPriceDescription.gridy = 1;
		pnlOrder.add(lblTotalPriceDescription, gbc_lblTotalPriceDescription);
		
		JLabel lblTotalPrice = new JLabel("<html><b>£10.50</b></html>");
		GridBagConstraints gbc_lblTotalPrice = new GridBagConstraints();
		gbc_lblTotalPrice.anchor = GridBagConstraints.EAST;
		gbc_lblTotalPrice.insets = new Insets(5, 5, 5, 5);
		gbc_lblTotalPrice.gridx = 1;
		gbc_lblTotalPrice.gridy = 1;
		pnlOrder.add(lblTotalPrice, gbc_lblTotalPrice);
		
		JButton btnSubmitOrder = new JButton("Submit Order");
		GridBagConstraints gbc_btnSubmitOrder = new GridBagConstraints();
		gbc_btnSubmitOrder.gridwidth = 2;
		gbc_btnSubmitOrder.insets = new Insets(5, 5, 5, 5);
		gbc_btnSubmitOrder.fill = GridBagConstraints.BOTH;
		gbc_btnSubmitOrder.gridx = 0;
		gbc_btnSubmitOrder.gridy = 2;
		pnlOrder.add(btnSubmitOrder, gbc_btnSubmitOrder);
	}
	
}
