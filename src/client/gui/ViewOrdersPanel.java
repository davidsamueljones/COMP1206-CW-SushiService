package client.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

import business.gui.RecordEditor;
import business.model.Dish;
import business.model.Order;
import client.model.ClientModel;
import general.gui.ListTableModel;
import general.gui.RecordPanel;
import general.model.Quantity;
import general.utility.Utilities;

/**
 * An extension of AbstractRecordPanel that handles viewing of orders for
 * a single customer.
 *
 * @author David Jones [dsj1n15]
 */
public class ViewOrdersPanel extends RecordPanel<Order> {
	private static final long serialVersionUID = -2306931811883597482L;
	// Client model
	private final ClientModel model;
	// Record objects
	private final JTextField txtDate;
	private final JTextField txtStatus;
	private final JTable tblDishes;
	private final DishesListTableModel model_tblDishes;
	private final JTextField txtTotalPrice;

	/**
	 * Create the panel.
	 * 
	 * @param model Data model being served
	 */
	public ViewOrdersPanel(ClientModel model) {
		super("Order", "Orders");
		// Store model
		this.model = model;
		
		// [Record Panel] - Set layout as grid bag
		final GridBagLayout gbl_pnlRecord = new GridBagLayout();
		gbl_pnlRecord.columnWidths = new int[] {0, 0};
		gbl_pnlRecord.rowHeights = new int[] {0, 0, 0, 0, 0};
		gbl_pnlRecord.columnWeights = new double[] {0.0, 1.0};
		gbl_pnlRecord.rowWeights = new double[] {0.0, 0.0, 0.0, 0.0, 1.0};
		pnlRecord.setLayout(gbl_pnlRecord);

		// [Record Panel] <- 'Date Field' Label
		final JLabel lblDate = new JLabel("Date:");
		final GridBagConstraints gbc_lblDate = new GridBagConstraints();
		gbc_lblDate.anchor = GridBagConstraints.EAST;
		gbc_lblDate.insets = new Insets(5, 5, 5, 5);
		gbc_lblDate.gridx = 0;
		gbc_lblDate.gridy = 0;
		pnlRecord.add(lblDate, gbc_lblDate);
		// [Record Panel] <- 'Date Field' TextBox
		txtDate = new JTextField();
		final GridBagConstraints gbc_txtDate = new GridBagConstraints();
		gbc_txtDate.anchor = GridBagConstraints.NORTH;
		gbc_txtDate.insets = new Insets(5, 0, 5, 5);
		gbc_txtDate.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtDate.gridx = 1;
		gbc_txtDate.gridy = 0;
		pnlRecord.add(txtDate, gbc_txtDate);
		txtDate.setEnabled(false);

		// [Record Panel] <- 'Status Field' Label
		final JLabel lblStatus = new JLabel("Status:");
		final GridBagConstraints gbc_lblStatus = new GridBagConstraints();
		gbc_lblStatus.anchor = GridBagConstraints.EAST;
		gbc_lblStatus.insets = new Insets(0, 5, 5, 5);
		gbc_lblStatus.gridx = 0;
		gbc_lblStatus.gridy = 1;
		pnlRecord.add(lblStatus, gbc_lblStatus);
		// [Record Panel] <- 'Status Field' TextBox
		txtStatus = new JTextField();
		final GridBagConstraints gbc_txtStatus = new GridBagConstraints();
		gbc_txtStatus.insets = new Insets(0, 0, 5, 5);
		gbc_txtStatus.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtStatus.gridx = 1;
		gbc_txtStatus.gridy = 1;
		pnlRecord.add(txtStatus, gbc_txtStatus);
		txtStatus.setColumns(10);
		txtStatus.setEnabled(false);

		// [Record Panel] <- 'Dishes' Panel
		final JPanel pnlDishes = new JPanel();
		final GridBagConstraints gbc_pnlDishes = new GridBagConstraints();
		gbc_pnlDishes.gridwidth = 2;
		gbc_pnlDishes.insets = new Insets(0, 5, 5, 5);
		gbc_pnlDishes.fill = GridBagConstraints.BOTH;
		gbc_pnlDishes.gridx = 0;
		gbc_pnlDishes.gridy = 2;
		pnlRecord.add(pnlDishes, gbc_pnlDishes);
		final GridBagLayout gbl_pnlDishes = new GridBagLayout();
		gbl_pnlDishes.columnWidths = new int[] {0, 0};
		gbl_pnlDishes.rowHeights = new int[] {0, 0, 0};
		gbl_pnlDishes.columnWeights = new double[] {0, 1.0};
		gbl_pnlDishes.rowWeights = new double[] {0.0, 0.0, Double.MIN_VALUE};
		pnlDishes.setLayout(gbl_pnlDishes);
		pnlDishes.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
				"Dishes", TitledBorder.LEADING, TitledBorder.TOP));
		
		// [Dishes Panel] <- 'Dishes' Table
		tblDishes = new JTable();
		tblDishes.setEnabled(false);
		tblDishes.setGridColor(Color.LIGHT_GRAY);
		tblDishes.setAutoCreateRowSorter(true);
		model_tblDishes = new DishesListTableModel();
		tblDishes.setModel(model_tblDishes);
		tblDishes.getRowSorter().toggleSortOrder(0);
		Utilities.setColumnStringFormat(tblDishes, 2, Utilities.PRICE_FORMAT, SwingConstants.RIGHT);
		// Make scrollable
		final JScrollPane scrDishes = new JScrollPane(tblDishes);
		scrDishes.setBackground(this.getBackground());
		final GridBagConstraints gbc_scrDishes = new GridBagConstraints();
		gbc_scrDishes.gridwidth = 2;
		gbc_scrDishes.ipady = 100;
		gbc_scrDishes.fill = GridBagConstraints.BOTH;
		gbc_scrDishes.insets = new Insets(5, 5, 5, 5);
		gbc_scrDishes.gridx = 0;
		gbc_scrDishes.gridy = 0;
		pnlDishes.add(scrDishes, gbc_scrDishes);
		scrDishes.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
		scrDishes.setPreferredSize(new Dimension(0, 0));

		// [Dishes Panel] <- 'Total Price' Label
		final JLabel lblTotalPrice = new JLabel("Total Price (£):");
		final GridBagConstraints gbc_lblTotalPrice = new GridBagConstraints();
		gbc_lblTotalPrice.anchor = GridBagConstraints.EAST;
		gbc_lblTotalPrice.insets = new Insets(0, 5, 5, 5);
		gbc_lblTotalPrice.gridx = 0;
		gbc_lblTotalPrice.gridy = 1;
		pnlDishes.add(lblTotalPrice, gbc_lblTotalPrice);
		// [Dishes Panel] <- 'Total Price' TextBox
		txtTotalPrice = new JTextField();
		final GridBagConstraints gbc_txtTotalPrice = new GridBagConstraints();
		gbc_txtTotalPrice.insets = new Insets(0, 0, 5, 5);
		gbc_txtTotalPrice.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtTotalPrice.gridx = 1;
		gbc_txtTotalPrice.gridy = 1;
		pnlDishes.add(txtTotalPrice, gbc_txtTotalPrice);
		txtTotalPrice.setColumns(10);
		txtTotalPrice.setEnabled(false);

		// Disable Creation/Editing
		setNewEnabled(false);
		setEditEnabled(false);
		setDeleteEnabled(false);
		
		// Load relevant table model
		model_tblRecords = new OrderTableModel();
		tblRecords.setModel(model_tblRecords);
		Utilities.setColumnDateFormat(tblRecords, 0, Utilities.DATE_TIME_FORMAT);
		Utilities.setColumnStringFormat(tblRecords, 2, "%.2f", SwingConstants.RIGHT);
		tblRecords.getRowSorter().toggleSortOrder(0); // sort by date
		tblRecords.getRowSorter().toggleSortOrder(0); // descending

		// Finalise
		setEditingMode(RecordEditor.EditingMode.VIEW);
	}

	@Override
	public void initialise() {
		refresh();
		// Ensure initialising is on EDT
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				Utilities.scaleColumns(tblRecords);
				tblRecords.requestFocusInWindow();
			}
		});
	}

	@Override
	public void refresh() {
		// Refresh orders using server
		Order[] orderArray = null;
		if (model.refreshOrders()) {
			// Wait for message handler to handle response
			if (model.orders.waitForNew(ClientApplication.REQUEST_TIMEOUT)) {
				// Get response
				orderArray = model.orders.readObject();
			}
		}
		if (orderArray == null) {
			orderArray = new Order[0];
		}
		final List<Order> orders = Arrays.asList(orderArray);
		// Ensure refresh is on EDT
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				refreshTable(orders);
			}
		});
	}
	
	@Override
	public void loadRecord(Order record) {
		final String dateTime = record.getDate().format(Utilities.DATE_TIME_FORMAT);
		txtDate.setText(dateTime);
		txtStatus.setText(String.valueOf(record.getStatus()));
		model_tblDishes.setList(record.getDishes().getList());
		txtTotalPrice.setText(String.format("%.2f", record.getTotalPrice()));
	}

	@Override
	public void clearRecord() {
		txtDate.setText(null);
		txtStatus.setText(null);
		model_tblDishes.setList(null);
		txtTotalPrice.setText(null);
	}

	@Override
	public Order createRecord() {
		throw new IllegalStateException("Unsupported operation");
	}

	@Override
	public void updateRecord(Order record) {
		throw new IllegalStateException("Unsupported operation");
	}

	@Override
	public boolean saveNewRecord() {
		throw new IllegalStateException("Unsupported operation");
	}

	@Override
	public boolean saveEditedRecord(Order record) {
		throw new IllegalStateException("Unsupported operation");
	}

	@Override
	public boolean deleteRecord(Order record) {
		throw new IllegalStateException("Unsupported operation");
	}
	
	/**
	 * An extension of ListTableModel that displays orders.
	 *
	 * @author David Jones [dsj1n15]
	 */
	class OrderTableModel extends ListTableModel<Order> {
		private static final long serialVersionUID = 7294325804359010564L;
		private final String[] COLUMN_TITLES = {"Date", "Status", "Total Price (£)"};
		private final Class<?>[] COLUMN_CLASSES = {LocalDateTime.class, Order.Status.class, Double.class};

		/**
		 * Instantiate table model with default column titles and classes.
		 */
		public OrderTableModel() {
			setColumnNames(COLUMN_TITLES);
			setColumnClasses(COLUMN_CLASSES);
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			final Order order = getObjectAt(rowIndex);
			if (order == null) {
				return null;
			}
			switch (columnIndex) {
				case 0:
					return order.getDate();
				case 1:
					return order.getStatus();
				case 2:
					return order.getTotalPrice();
				default:
					return null;
			}
		}

	}

	/**
	 * An extension of ListTableModel that displays dishes and a respective quantity.
	 *
	 * @author David Jones [dsj1n15]
	 */
	class DishesListTableModel extends ListTableModel<Quantity<Dish>> {
		private static final long serialVersionUID = 6848893576001534549L;
		private final String[] COLUMN_TITLES = {"Name", "Quantity", "Price (£)"};
		private final Class<?>[] COLUMN_CLASSES = {String.class, Double.class, Double.class};

		/**
		 * Instantiate table model with default column titles and classes.
		 */
		public DishesListTableModel() {
			setColumnNames(COLUMN_TITLES);
			setColumnClasses(COLUMN_CLASSES);
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			final Quantity<Dish> record = getObjectAt(rowIndex);
			if (record == null) {
				return null;
			}
			final Dish dish = record.getItem();
			switch (columnIndex) {
				case 0:
					return dish.getName();
				case 1:
					return record.getQuantity();
				case 2:
					return dish.getPrice();
				default:
					return null;
			}
		}
	}

}
