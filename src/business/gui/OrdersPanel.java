package business.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

import business.model.BusinessModel;
import business.model.Dish;
import business.model.Order;
import general.gui.ListTableModel;
import general.gui.ToolBarButton;
import general.model.Quantity;
import general.utility.ErrorBuilder;
import general.utility.SerializationUtils;
import general.utility.Utilities;

public class OrdersPanel extends AbstractRecordPanel<Order> {
	/**
	 *
	 */
	private static final long serialVersionUID = 8046768538848728633L;

	private final ToolBarButton tbbDeleteCompleted;

	private final JTextField txtDate;
	private final JTextField txtStatus;
	private final JTextField txtCustomer;

	private final JTable tblDishes;
	private final DishesListTableModel model_tblDishes;
	private final JTextField txtTotalPrice;
	private final JButton btnCancelOrder;


	/**
	 * Create the panel.
	 */
	public OrdersPanel(BusinessModel model) {
		super(model, "Order", "Orders");

		final GridBagLayout gbl_pnlRecord = new GridBagLayout();
		gbl_pnlRecord.columnWidths = new int[] {0, 0};
		gbl_pnlRecord.rowHeights = new int[] {0, 0, 0, 0, 0, 0};
		gbl_pnlRecord.columnWeights = new double[] {0.0, 1.0};
		gbl_pnlRecord.rowWeights = new double[] {0.0, 0.0, 0.0, 0.0, 0.0, 1.0};
		pnlRecord.setLayout(gbl_pnlRecord);

		final JLabel lblDate = new JLabel("Date:");
		final GridBagConstraints gbc_lblDate = new GridBagConstraints();
		gbc_lblDate.anchor = GridBagConstraints.EAST;
		gbc_lblDate.insets = new Insets(5, 5, 5, 5);
		gbc_lblDate.gridx = 0;
		gbc_lblDate.gridy = 0;
		pnlRecord.add(lblDate, gbc_lblDate);

		txtDate = new JTextField();
		final GridBagConstraints gbc_txtDate = new GridBagConstraints();
		gbc_txtDate.anchor = GridBagConstraints.NORTH;
		gbc_txtDate.insets = new Insets(5, 0, 5, 5);
		gbc_txtDate.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtDate.gridx = 1;
		gbc_txtDate.gridy = 0;
		pnlRecord.add(txtDate, gbc_txtDate);
		txtDate.setColumns(10);
		txtDate.setEnabled(false);

		final JLabel lblStatus = new JLabel("Status:");
		final GridBagConstraints gbc_lblStatus = new GridBagConstraints();
		gbc_lblStatus.anchor = GridBagConstraints.EAST;
		gbc_lblStatus.insets = new Insets(0, 5, 5, 5);
		gbc_lblStatus.gridx = 0;
		gbc_lblStatus.gridy = 1;
		pnlRecord.add(lblStatus, gbc_lblStatus);

		txtStatus = new JTextField();
		final GridBagConstraints gbc_txtStatus = new GridBagConstraints();
		gbc_txtStatus.insets = new Insets(0, 0, 5, 5);
		gbc_txtStatus.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtStatus.gridx = 1;
		gbc_txtStatus.gridy = 1;
		pnlRecord.add(txtStatus, gbc_txtStatus);
		txtStatus.setColumns(10);
		txtStatus.setEnabled(false);

		final JLabel lblCustomer = new JLabel("Customer:");
		final GridBagConstraints gbc_lblCustomer = new GridBagConstraints();
		gbc_lblCustomer.anchor = GridBagConstraints.EAST;
		gbc_lblCustomer.insets = new Insets(0, 5, 5, 5);
		gbc_lblCustomer.gridx = 0;
		gbc_lblCustomer.gridy = 2;
		pnlRecord.add(lblCustomer, gbc_lblCustomer);

		txtCustomer = new JTextField();
		final GridBagConstraints gbc_txtCustomer = new GridBagConstraints();
		gbc_txtCustomer.insets = new Insets(0, 0, 5, 5);
		gbc_txtCustomer.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtCustomer.gridx = 1;
		gbc_txtCustomer.gridy = 2;
		pnlRecord.add(txtCustomer, gbc_txtCustomer);
		txtCustomer.setColumns(10);
		txtCustomer.setEnabled(false);

		final JPanel pnlDishes = new JPanel();
		final GridBagConstraints gbc_pnlDishes = new GridBagConstraints();
		gbc_pnlDishes.gridwidth = 2;
		gbc_pnlDishes.insets = new Insets(0, 5, 5, 5);
		gbc_pnlDishes.fill = GridBagConstraints.BOTH;
		gbc_pnlDishes.gridx = 0;
		gbc_pnlDishes.gridy = 3;
		pnlRecord.add(pnlDishes, gbc_pnlDishes);
		final GridBagLayout gbl_pnlDishes = new GridBagLayout();
		gbl_pnlDishes.columnWidths = new int[] {0, 0};
		gbl_pnlDishes.rowHeights = new int[] {0, 0, 0};
		gbl_pnlDishes.columnWeights = new double[] {0, 1.0};
		gbl_pnlDishes.rowWeights = new double[] {0.0, 0.0, Double.MIN_VALUE};
		pnlDishes.setLayout(gbl_pnlDishes);
		pnlDishes.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
				"Dishes", TitledBorder.LEADING, TitledBorder.TOP));
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

		final JLabel lblTotalPrice = new JLabel("Total Price (£):");
		final GridBagConstraints gbc_lblTotalPrice = new GridBagConstraints();
		gbc_lblTotalPrice.anchor = GridBagConstraints.EAST;
		gbc_lblTotalPrice.insets = new Insets(0, 5, 5, 5);
		gbc_lblTotalPrice.gridx = 0;
		gbc_lblTotalPrice.gridy = 1;
		pnlDishes.add(lblTotalPrice, gbc_lblTotalPrice);

		txtTotalPrice = new JTextField();
		final GridBagConstraints gbc_txtTotalPrice = new GridBagConstraints();
		gbc_txtTotalPrice.insets = new Insets(0, 0, 5, 5);
		gbc_txtTotalPrice.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtTotalPrice.gridx = 1;
		gbc_txtTotalPrice.gridy = 1;
		pnlDishes.add(txtTotalPrice, gbc_txtTotalPrice);
		txtTotalPrice.setColumns(10);
		txtTotalPrice.setEnabled(false);

		btnCancelOrder = new JButton("Cancel Order");
		final GridBagConstraints gbc_btnCancelOrder = new GridBagConstraints();
		gbc_btnCancelOrder.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnCancelOrder.gridwidth = 2;
		gbc_btnCancelOrder.insets = new Insets(0, 5, 5, 5);
		gbc_btnCancelOrder.gridx = 0;
		gbc_btnCancelOrder.gridy = 4;
		pnlRecord.add(btnCancelOrder, gbc_btnCancelOrder);

		// Disable Creation/Editing
		setNewEnabled(false);
		setEditEnabled(false);

		// Add delete completed to toolbar
		tbbDeleteCompleted = tlbRecords.addButton("Delete Completed",
				Utilities.loadImage(new File("resources/imgDeleteMultiple.png")));


		// Load relevant table model
		model_tblRecords = new OrderTableModel();
		tblRecords.setModel(model_tblRecords);
		Utilities.setColumnDateFormat(tblRecords, 0, Utilities.DATE_TIME_FORMAT);
		Utilities.setColumnStringFormat(tblRecords, 3, "%.2f", SwingConstants.RIGHT);
		tblRecords.getRowSorter().toggleSortOrder(0); // sort by date
		tblRecords.getRowSorter().toggleSortOrder(0); // descending

		// Finalise
		setEditingMode(RecordEditor.EditingMode.VIEW);

		tbbDeleteCompleted.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Get confirmation from the user
				final int res = JOptionPane.showConfirmDialog(null,
						"Are you sure you want to delete all completed orders?", "Delete Records",
						JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
				if (res == JOptionPane.YES_OPTION) {
					// Remove all completed orders
					synchronized (model.orders) {
						final Iterator<Order> itr = model.orders.iterator();
						while (itr.hasNext()) {
							final Order order = itr.next();
							if (order.isComplete()) {
								itr.remove();
							}
						}
					}
					refresh();
				}
			}
		});


		btnCancelOrder.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Get confirmation from the user
				final int res = JOptionPane.showConfirmDialog(null,
						"Are you sure you want to cancel the selected order?", "Cancel Order",
						JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
				if (res == JOptionPane.YES_OPTION) {
					cancelOrder();
				}
			}
		});
	}

	public boolean cancelOrder() {
		final ErrorBuilder eb = new ErrorBuilder();
		synchronized (model.orders) {
			// Get up to date order
			final Order storedRecord =
					Utilities.getCollectionItem(model.orders, loadedRecord, Order.class);
			// Check if order exists and can be cancelled
			if (storedRecord == null) {
				eb.addError("Order being cancelled does not exist");
			} else {
				if (storedRecord.isCancellable()) {
					// Update order status
					storedRecord.setStatus(Order.Status.CANCELLED);
					// Return reserved stock to actual stock
					synchronized (model.stock.dishes) {
						model.stock.dishes.unreserveStock(storedRecord.getDishes());
						model.stock.dishes.addStock(storedRecord.getDishes());
					}
					return true;
				} else {
					eb.addError("Order is not at a stage it can be cancelled");
				}
			}
		}
		// Show error builder message
		JOptionPane.showMessageDialog(null, eb.listComments("Cancel Failed"), "Cancel Failed",
				JOptionPane.ERROR_MESSAGE);
		return false;
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
		// Ensure refresh is on EDT
		SwingUtilities.invokeLater(new Runnable() {
			@SuppressWarnings("unchecked")
			@Override
			public void run() {
				// Create local copy so table not backed by map ensuring thread
				// safety. This is done as an alternative to locking customers
				// whilst refreshing due to the reduced time the lock is required.
				Collection<Order> orders;
				synchronized (model.customers) {
					// Cast safe due to known functionality of deep clone
					orders = (Collection<Order>) SerializationUtils.deepClone(model.orders);
				}
				// Refresh using local copy
				refreshTable(new ArrayList<>(orders));
			}
		});
	}

	@Override
	public void loadRecord(Order record) {
		final String dateTime = record.getDate().format(Utilities.DATE_TIME_FORMAT);
		txtDate.setText(dateTime);
		txtStatus.setText(String.valueOf(record.getStatus()));
		txtCustomer.setText(record.getCustomer().getLogin().getUsername());
		model_tblDishes.setList(record.getDishes().getList());
		txtTotalPrice.setText(String.format("%.2f", record.getTotalPrice()));
		if (record.isCancellable()) {
			btnCancelOrder.setVisible(true);
		} else {
			btnCancelOrder.setVisible(false);
		}
	}

	@Override
	public void clearRecord() {
		txtDate.setText(null);
		txtStatus.setText(null);
		txtCustomer.setText(null);
		model_tblDishes.setList(null);
		txtTotalPrice.setText(null);
		btnCancelOrder.setVisible(false);
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
		// Check order is deleteable
		if (!record.isComplete()) {
			JOptionPane.showMessageDialog(null,
					"Order must be delivered or cancelled to be deleted.", "Delete Failed",
					JOptionPane.ERROR_MESSAGE);
			return false;
		}
		// Get confirmation from the user
		final int res = JOptionPane.showConfirmDialog(null,
				"Are you sure you want to delete the selected order?", "Delete Record",
				JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
		if (res == JOptionPane.YES_OPTION) {
			// Remove order from model
			synchronized (model.orders) {
				model.orders.remove(record);
			}
			return true;
		}
		return false;
	}

	class OrderTableModel extends ListTableModel<Order> {
		private static final long serialVersionUID = 7294325804359010564L;
		private final String[] COLUMN_TITLES = {"Date", "Customer", "Status", "Total Price (£)"};
		private final Class<?>[] COLUMN_CLASSES =
				{LocalDateTime.class, String.class, Order.Status.class, Double.class};

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
					return order.getCustomer().getLogin().getUsername();
				case 2:
					return order.getStatus();
				case 3:
					return order.getTotalPrice();
				default:
					return null;
			}
		}

	}

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
