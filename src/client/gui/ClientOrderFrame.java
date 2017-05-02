package client.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import javax.swing.table.AbstractTableModel;

import business.model.Customer;
import business.model.Dish;
import business.model.Order;
import client.model.ClientModel;
import general.gui.Header;
import general.model.Message;
import general.model.Quantity;
import general.model.QuantityMap;
import general.utility.Utilities;

public class ClientOrderFrame extends JFrame {
	/**
	 *
	 */
	private static final long serialVersionUID = -5591207426290683707L;
	private static final String COMPANY_NAME = "SpeediSushi";
	private static final String FORM_TITLE = "Client Application";
	private static final String GUI_TITLE =
			String.format("<html><b>%s</b> - %s</html>", COMPANY_NAME, FORM_TITLE);

	private static final String[] EXISTING_ORDERS_COLUMN_TITLES =
			{"Date", "Status", "Price", "Dishes"};
	private static final String[] DISHES_COLUMN_TITLES = {"Name", "Stock", "Price"};
	private static final String[] ORDER_COLUMN_TITLES = {"Name", "Quantity", "Price"};
	private static final String[][] EMPTY_REPLACE_THIS = {};

	private final ClientModel application;

	private JPanel contentPane;
	private JTable tblExistingOrders;
	private DishQuantityTableModel model_tblExistingOrders;

	private JTable tblDishes;
	private DishQuantityTableModel model_tblDishes;


	private JTable tblOrder;
	private DishQuantityTableModel model_tblOrder;

	QuantityMap<Dish> orderItems = new QuantityMap<>();


	/**
	 * Create the frame.
	 */
	public ClientOrderFrame(ClientModel application) {
		this.application = application;
		init();
		refreshDishes();
	}

	private void refreshDishes() {
		// Request dish update
		Message message = new Message(Message.Command.GET_DISH_STOCK);
		if (application.getComms().sendMessage(message)) {
			// Check for response from server
			if (application.getDishes().waitForNew(ClientModel.GET_DISHES_TIMEOUT)) {
				loadDishesTable();
			} else {
				System.out.println(
						"Response Timeout " + message.getCommand() + " " + message.toString());
			}
		}
	}

	private void loadDishesTable() {
		System.out.println("Updating dishes table...");
		QuantityMap<Dish> dishes = application.getDishes().read();
		model_tblDishes.setQuantities(dishes);
		// Update model on EDT
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				// Remember the currently selected value
				int curSel = tblDishes.getSelectedRow();
				Quantity<Dish> dish = model_tblDishes.getQuantityAt(curSel);
				model_tblDishes.fireTableDataChanged();
				int newSel = model_tblDishes.getItemPos(dish);
				if (newSel >= 0) {
					tblDishes.setRowSelectionInterval(newSel, newSel);
				}
				Utilities.scaleColumns(tblDishes);
			}
		});
	}

	private void loadOrderTable() {
		System.out.println("Updating order table...");
		model_tblOrder.setQuantities(orderItems);
		// Update model on EDT
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				// Remember the currently selected value
				int curSel = tblOrder.getSelectedRow();
				Quantity<Dish> dish = model_tblOrder.getQuantityAt(curSel);
				model_tblOrder.fireTableDataChanged();
				int newSel = model_tblOrder.getItemPos(dish);
				if (newSel >= 0) {
					tblOrder.setRowSelectionInterval(newSel, newSel);
				}
				Utilities.scaleColumns(tblOrder);
			}
		});
	}

	private void refreshExistingOrders() {
		// Request existing orders update
		Message message = new Message(Message.Command.GET_EXISTING_ORDERS);
		if (application.getComms().sendMessage(message)) {
			// Check for response from server
			if (application.getOrders().waitForNew(ClientModel.GET_ORDERS_TIMEOUT)) {
				// loadExistingOrdersTable();
			} else {
				System.out.println(
						"Response Timeout " + message.getCommand() + " " + message.toString());
			}
		}
	}

	// private void loadExistingOrdersTable() {
	// System.out.println("Updating dishes table...");
	// Order[] orders = application.getOrders().read();
	// model_tblExistingOrders.setQuantities(orders);
	// // Update model on EDT
	// SwingUtilities.invokeLater(new Runnable() {
	// @Override
	// public void run() {
	// // Remember the currently selected value
	// int curSel = tblDishes.getSelectedRow();
	// Quantity<Dish> dish = model_tblDishes.getQuantityAt(curSel);
	// model_tblDishes.fireTableDataChanged();
	// int newSel = model_tblDishes.getItemPos(dish);
	// if (newSel >= 0) {
	// tblDishes.setRowSelectionInterval(newSel, newSel);
	// }
	// scaleColumns(tblDishes);
	// }
	// });
	// }



	abstract class QuantityTableModel<T> extends AbstractTableModel {
		private static final long serialVersionUID = -2485103739386848564L;
		private final String[] columnNames;
		private List<Quantity<T>> quantities = null;

		public QuantityTableModel(String[] columnNames) {
			this.columnNames = columnNames;
		}

		public void setQuantities(QuantityMap<T> quantities) {
			this.quantities = quantities.getList();
			// Sort by natural ordering of item type
		}

		@Override
		public int getRowCount() {
			return (quantities == null) ? 0 : quantities.size();
		}

		@Override
		public int getColumnCount() {
			return (columnNames == null) ? 0 : columnNames.length;
		}

		@Override
		public String getColumnName(int index) {
			return columnNames[index];
		}

		public Quantity<T> getQuantityAt(int rowIndex) {
			if (rowIndex < 0 || quantities == null) {
				return null;
			} else {
				return quantities.get(rowIndex);
			}
		}

		public int getItemPos(Object item) {
			return (quantities == null) ? -1 : quantities.indexOf(item);
		}

	}

	class DishQuantityTableModel extends QuantityTableModel<Dish> {
		private static final long serialVersionUID = 5131778222293096337L;

		public DishQuantityTableModel(String[] columnNames) {
			super(columnNames);
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			Quantity<Dish> dish = getQuantityAt(rowIndex);
			if (dish == null) {
				return null;
			}
			switch (columnIndex) {
				case 0:
					return dish.getItem().getName();
				case 1:
					return dish.getQuantity();
				case 2:
					return String.format("£%.2f", dish.getItem().getPrice());
				default:
					return null;
			}
		}

	}

	public void init() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(768, 480);
		setLocationRelativeTo(null);

		contentPane = new JPanel();
		contentPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[] {0, 0, 0};
		gbl_contentPane.rowHeights = new int[] {0, 0, 0, 0};
		gbl_contentPane.columnWeights = new double[] {0.5, 0.0, 0.5};
		gbl_contentPane.rowWeights = new double[] {0.0, 0.0, 0.3, 0.35};
		contentPane.setLayout(gbl_contentPane);

		// Add title
		Header lblGUITitle = new Header(null, "Client Application");
		// lblGUITitle.setFont(new Font("Trebuchet MS", Font.PLAIN, 21));
		GridBagConstraints gbc_lblGUITitle = new GridBagConstraints();
		gbc_lblGUITitle.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblGUITitle.gridwidth = 3;
		gbc_lblGUITitle.gridx = 0;
		gbc_lblGUITitle.gridy = 0;
		// gbc_lblGUITitle.insets = new Insets(10, 10, 10, 10);
		contentPane.add(lblGUITitle, gbc_lblGUITitle);

		JPanel pnlExistingOrders = new JPanel();
		pnlExistingOrders
				.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
						"Existing Customer Orders", TitledBorder.LEFT, TitledBorder.TOP));
		GridBagConstraints gbc_pnlExistingOrders = new GridBagConstraints();
		gbc_pnlExistingOrders.insets = new Insets(0, 0, 5, 0);
		gbc_pnlExistingOrders.fill = GridBagConstraints.BOTH;
		gbc_pnlExistingOrders.gridwidth = 3;
		gbc_pnlExistingOrders.gridx = 0;
		gbc_pnlExistingOrders.gridy = 1;
		contentPane.add(pnlExistingOrders, gbc_pnlExistingOrders);
		GridBagLayout gbl_pnlExistingOrders = new GridBagLayout();
		gbl_pnlExistingOrders.columnWeights = new double[] {1.0};
		gbl_pnlExistingOrders.rowWeights = new double[] {1.0};
		pnlExistingOrders.setLayout(gbl_pnlExistingOrders);

		tblExistingOrders = new JTable(EMPTY_REPLACE_THIS, EXISTING_ORDERS_COLUMN_TITLES);
		tblExistingOrders.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tblExistingOrders.setGridColor(Color.LIGHT_GRAY);
		JScrollPane scrExistingOrders = new JScrollPane(tblExistingOrders);
		scrExistingOrders.setPreferredSize(new Dimension(0, 0));
		scrExistingOrders.setBackground(pnlExistingOrders.getBackground());
		GridBagConstraints gbc_scrExistingOrders = new GridBagConstraints();
		gbc_scrExistingOrders.insets = new Insets(5, 5, 5, 5);
		gbc_scrExistingOrders.fill = GridBagConstraints.BOTH;
		pnlExistingOrders.add(scrExistingOrders, gbc_scrExistingOrders);

		JPanel pnlDishes = new JPanel();
		pnlDishes.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
				"Available Dishes", TitledBorder.LEFT, TitledBorder.TOP));
		GridBagConstraints gbc_pnlDishes = new GridBagConstraints();
		gbc_pnlDishes.insets = new Insets(0, 0, 0, 5);
		gbc_pnlDishes.gridheight = 2;
		gbc_pnlDishes.fill = GridBagConstraints.BOTH;
		gbc_pnlDishes.gridx = 0;
		gbc_pnlDishes.gridy = 2;
		contentPane.add(pnlDishes, gbc_pnlDishes);

		GridBagLayout gbl_pnlDishes = new GridBagLayout();
		gbl_pnlDishes.columnWidths = new int[] {0};
		gbl_pnlDishes.rowHeights = new int[] {0};
		gbl_pnlDishes.columnWeights = new double[] {1.0};
		gbl_pnlDishes.rowWeights = new double[] {1.0};
		pnlDishes.setLayout(gbl_pnlDishes);

		model_tblDishes = new DishQuantityTableModel(DISHES_COLUMN_TITLES);
		tblDishes = new JTable(model_tblDishes);
		tblDishes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tblDishes.setGridColor(Color.LIGHT_GRAY);
		// tblDishes.setAutoCreateRowSorter(true);
		JScrollPane scrDishes = new JScrollPane(tblDishes);
		scrDishes.setPreferredSize(new Dimension(0, 0));
		scrDishes.setBackground(pnlDishes.getBackground());
		GridBagConstraints gbc_scrDishes = new GridBagConstraints();
		gbc_scrDishes.insets = new Insets(5, 5, 5, 5);
		gbc_scrDishes.fill = GridBagConstraints.BOTH;
		pnlDishes.add(scrDishes, gbc_scrDishes);

		JButton btnAdd = new JButton(">");
		btnAdd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Get currently selected
				int curSel = tblDishes.getSelectedRow();
				Quantity<Dish> dish = model_tblDishes.getQuantityAt(curSel);
				orderItems.increment(dish.getItem());
				loadOrderTable();
			}
		});
		GridBagConstraints gbc_btnAdd = new GridBagConstraints();
		gbc_btnAdd.fill = GridBagConstraints.VERTICAL;
		gbc_btnAdd.insets = new Insets(5, 0, 5, 5);
		gbc_btnAdd.gridx = 1;
		gbc_btnAdd.gridy = 2;
		contentPane.add(btnAdd, gbc_btnAdd);

		JButton btnRemove = new JButton("<");
		GridBagConstraints gbc_btnRemove = new GridBagConstraints();
		gbc_btnRemove.fill = GridBagConstraints.VERTICAL;
		gbc_btnRemove.insets = new Insets(2, 0, 0, 5);
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
		gbl_pnlOrder.columnWidths = new int[] {0, 0};
		gbl_pnlOrder.rowHeights = new int[] {0, 0, 0};
		gbl_pnlOrder.columnWeights = new double[] {0.0, 1.0};
		gbl_pnlOrder.rowWeights = new double[] {1.0, 0.0, 0.0};
		pnlOrder.setLayout(gbl_pnlOrder);

		model_tblOrder = new DishQuantityTableModel(ORDER_COLUMN_TITLES);
		tblOrder = new JTable(model_tblOrder);
		tblOrder.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tblOrder.setGridColor(Color.LIGHT_GRAY);
		JScrollPane scrOrder = new JScrollPane(tblOrder);
		scrOrder.setPreferredSize(new Dimension(0, 0));
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
		btnSubmitOrder.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Customer customer = application.getCustomer().read();
				Order newOrder = new Order(customer, orderItems);
				Message message = new Message(Message.Command.SUBMIT_ORDER, null, newOrder);
				application.getComms().sendMessage(message);
				if (application.getOrder().waitForNew()) {
					if (application.getOrder().read() == null) {
						System.out
								.println(application.getOrder().getErrorBuilder().listComments(""));
					} else {
						// refreshOrders();
					}
					orderItems = new QuantityMap<>();
					loadOrderTable();
					refreshDishes();

				} else {
					System.out.println("timeout");
				}
			}
		});
		GridBagConstraints gbc_btnSubmitOrder = new GridBagConstraints();
		gbc_btnSubmitOrder.gridwidth = 2;
		gbc_btnSubmitOrder.insets = new Insets(5, 5, 5, 5);
		gbc_btnSubmitOrder.fill = GridBagConstraints.BOTH;
		gbc_btnSubmitOrder.gridx = 0;
		gbc_btnSubmitOrder.gridy = 2;
		pnlOrder.add(btnSubmitOrder, gbc_btnSubmitOrder);
	}

}
