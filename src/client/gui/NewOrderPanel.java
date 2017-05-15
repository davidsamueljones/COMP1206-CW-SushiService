package client.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.File;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableModel;

import business.model.Customer;
import business.model.Dish;
import business.model.Order;
import general.gui.ListTableModel;
import general.gui.ToolBar;
import general.gui.ToolBarButton;
import general.gui.UserAccountHeader;
import general.gui.View;
import general.model.Message;
import general.model.Quantity;
import general.model.QuantityMap;
import general.utility.ErrorBuilder;
import general.utility.Utilities;
import java.awt.GridBagLayout;
import javax.swing.JButton;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JSpinner;

/**
 * A panel that handles the viewing of available dishes and creation of a new orders.
 *
 * @author David Jones [dsj1n15]
 */
public class NewOrderPanel extends JPanel implements View {
	private static final long serialVersionUID = -2945873057105630308L;
	// Column titles for different quantity lists
	private static final String[] DISHES_COLUMN_TITLES = {"Name", "Stock", "Price (£)"};
	private static final String[] ORDER_COLUMN_TITLES = {"Name", "Quantity", "Price (£)"};
	
	// Parent application
	private ClientApplication application;
	
	// Record objects
	private JTable tblDishes;
	private DishesListTableModel model_tblDishes;
	private JTable tblOrder;
	private DishesListTableModel model_tblOrder;
	private JSpinner nudTotalPrice;

	// Current order
	private QuantityMap<Dish> orderItems;

	public NewOrderPanel(ClientApplication application) {
		this.application = application;

		// [LHS] - Dishes
		// [LHS] <- 'Toolbar'
		ToolBar tlbDishes = new ToolBar(getBackground(), Color.BLACK, Color.WHITE);
		tlbDishes.addSeparator();
		final JLabel lblDishesTitle = tlbDishes.addLabel("Dishes", SwingConstants.CENTER);
		lblDishesTitle.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		tlbDishes.addSeparator();
		// Add dish viewing buttons to tool bar
		ToolBarButton tbbRefresh = tlbDishes.addButton("Refresh",
				Utilities.loadImage(new File("resources/imgRefresh.png")));
		// Add bottom border to tool bar
		tlbDishes.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));

		// [LHS] <- 'Dishes' Panel
		JPanel pnlDishes = new JPanel();
		GridBagLayout gbl_pnlDishes = new GridBagLayout();
		gbl_pnlDishes.columnWidths = new int[]{0, 0};
		gbl_pnlDishes.rowHeights = new int[]{0, 0, 0};
		gbl_pnlDishes.columnWeights = new double[]{0.5, 0.5};
		gbl_pnlDishes.rowWeights = new double[]{1.0, 0.0, 0.0};
		pnlDishes.setLayout(gbl_pnlDishes);

		// [Dishes Panel] <- 'Dishes' Table
		model_tblDishes = new DishesListTableModel();
		model_tblDishes.setColumnNames(DISHES_COLUMN_TITLES);
		tblDishes = new JTable(model_tblDishes);
		tblDishes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tblDishes.setGridColor(Color.LIGHT_GRAY);
		tblDishes.setAutoCreateRowSorter(true);
		tblDishes.getRowSorter().toggleSortOrder(0);
		Utilities.setColumnStringFormat(tblDishes, 2, Utilities.PRICE_FORMAT,
				SwingConstants.RIGHT);
		// Make scrollable
		JScrollPane scrDishes = new JScrollPane(tblDishes);
		scrDishes.setPreferredSize(new Dimension(0, 0));
		scrDishes.setBackground(pnlDishes.getBackground());
		GridBagConstraints gbc_scrDishes = new GridBagConstraints();
		gbc_scrDishes.gridx = 0;
		gbc_scrDishes.gridy = 0;
		gbc_scrDishes.gridwidth = 2;
		gbc_scrDishes.insets = new Insets(5, 5, 5, 0);
		gbc_scrDishes.fill = GridBagConstraints.BOTH;
		pnlDishes.add(scrDishes, gbc_scrDishes);
		scrDishes.setBorder(null);
		scrDishes.setPreferredSize(new Dimension(0, 0));

		// [Dishes Panel] <- 'Minus One Quantity' Button
		JButton btnMinusOne = new JButton("-1 from Order");
		btnMinusOne.setEnabled(false);
		GridBagConstraints gbc_btnMinusOne = new GridBagConstraints();
		gbc_btnMinusOne.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnMinusOne.insets = new Insets(0, 5, 5, 5);
		gbc_btnMinusOne.gridx = 0;
		gbc_btnMinusOne.gridy = 1;
		pnlDishes.add(btnMinusOne, gbc_btnMinusOne);

		// [Dishes Panel] <- 'Plus One Quantity' Button
		JButton btnPlusOne = new JButton("+1 to Order");
		btnPlusOne.setEnabled(false);
		GridBagConstraints gbc_btnPlusOne = new GridBagConstraints();
		gbc_btnPlusOne.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnPlusOne.insets = new Insets(0, 2, 5, 5);
		gbc_btnPlusOne.gridx = 1;
		gbc_btnPlusOne.gridy = 1;
		pnlDishes.add(btnPlusOne, gbc_btnPlusOne);

		// [Dishes Panel] <- 'Remove All' Button
		JButton btnRemoveAll = new JButton("Remove From Order");
		btnRemoveAll.setEnabled(false);
		GridBagConstraints gbc_btnRemoveAll = new GridBagConstraints();
		gbc_btnRemoveAll.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnRemoveAll.gridwidth = 2;
		gbc_btnRemoveAll.insets = new Insets(0, 5, 5, 5);
		gbc_btnRemoveAll.gridx = 0;
		gbc_btnRemoveAll.gridy = 2;
		pnlDishes.add(btnRemoveAll, gbc_btnRemoveAll);
		
		// Format LHS into single panel
		JPanel pnlLHS = new JPanel(new BorderLayout());
		pnlLHS.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		pnlLHS.add(tlbDishes, BorderLayout.NORTH);
		pnlLHS.add(pnlDishes, BorderLayout.CENTER);
		
		// [RHS] - Order
		// [RHS] <- 'Toolbar'
		ToolBar tlbOrder = new ToolBar(getBackground(), Color.BLACK, Color.WHITE);
		tlbOrder.addSeparator();
		final JLabel lblOrderTitle = tlbOrder.addLabel("Order", SwingConstants.CENTER);
		lblOrderTitle.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		tlbOrder.addSeparator();
		ToolBarButton tbbSubmitOrder = tlbOrder.addButton("Submit Order",
				Utilities.loadImage(new File("resources/imgSubmit.png")));
		// Add bottom border to tool bar
		tlbOrder.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));

		// [RHS] <- 'Order' Panel
		JPanel pnlOrder = new JPanel();
		GridBagLayout gbl_pnlOrder = new GridBagLayout();
		gbl_pnlOrder.columnWidths = new int[] {0, 0};
		gbl_pnlOrder.rowHeights = new int[] {0, 0};
		gbl_pnlOrder.columnWeights = new double[] {0.0, 1.0};
		gbl_pnlOrder.rowWeights = new double[] {1.0, 0.0};
		pnlOrder.setLayout(gbl_pnlOrder);

		// [Order Panel] <- 'Dishes' Table
		model_tblOrder = new DishesListTableModel();
		model_tblOrder.setColumnNames(ORDER_COLUMN_TITLES);
		tblOrder = new JTable(model_tblOrder);
		tblOrder.setEnabled(false);
		tblOrder.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tblOrder.setGridColor(Color.LIGHT_GRAY);
		tblOrder.setAutoCreateRowSorter(true);
		tblOrder.getRowSorter().toggleSortOrder(0);
		Utilities.setColumnStringFormat(tblOrder, 2, Utilities.PRICE_FORMAT,
				SwingConstants.RIGHT);
		// Make scrollable
		JScrollPane scrOrder = new JScrollPane(tblOrder);
		scrOrder.setPreferredSize(new Dimension(0, 0));
		scrOrder.setBackground(pnlOrder.getBackground());
		GridBagConstraints gbc_scrOrder = new GridBagConstraints();
		gbc_scrOrder.gridx = 0;
		gbc_scrOrder.gridy = 0;
		gbc_scrOrder.gridwidth = 2;
		gbc_scrOrder.insets = new Insets(5, 5, 5, 5);
		gbc_scrOrder.fill = GridBagConstraints.BOTH;
		pnlOrder.add(scrOrder, gbc_scrOrder);

		// [Order Panel] <- 'Total Price' Label
		JLabel lblTotalPrice = new JLabel("Total Price (£):");
		GridBagConstraints gbc_lblTotalPrice = new GridBagConstraints();
		gbc_lblTotalPrice.insets = new Insets(0, 5, 5, 5);
		gbc_lblTotalPrice.anchor = GridBagConstraints.EAST;
		gbc_lblTotalPrice.gridx = 0;
		gbc_lblTotalPrice.gridy = 1;
		pnlOrder.add(lblTotalPrice, gbc_lblTotalPrice);

		// [Order Panel] <- 'Total Price' Spinner
		nudTotalPrice = new JSpinner(new SpinnerNumberModel(0.0, 0.0, 1000, 0.01));
		// Enforce price format 0.00
		final JSpinner.NumberEditor editor = (JSpinner.NumberEditor) nudTotalPrice.getEditor();
		final DecimalFormat format = editor.getFormat();
		format.setMinimumFractionDigits(2);
		format.setMaximumFractionDigits(2);
		nudTotalPrice.setEnabled(false);
		GridBagConstraints gbc_nudTotalPrice = new GridBagConstraints();
		gbc_nudTotalPrice.fill = GridBagConstraints.HORIZONTAL;
		gbc_nudTotalPrice.insets = new Insets(0, 0, 5, 5);
		gbc_nudTotalPrice.gridx = 1;
		gbc_nudTotalPrice.gridy = 1;
		pnlOrder.add(nudTotalPrice, gbc_nudTotalPrice);

		// Format RHS into single panel
		JPanel pnlRHS = new JPanel(new BorderLayout());
		pnlRHS.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		pnlRHS.add(tlbOrder, BorderLayout.NORTH);
		pnlRHS.add(pnlOrder, BorderLayout.CENTER);

		// Position LHS & RHS using SplitPane
		setLayout(new GridLayout(1, 1));
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, pnlLHS, pnlRHS);
		splitPane.setResizeWeight(0.75);
		splitPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		splitPane.setBackground(Color.WHITE);
		add(splitPane);
		
		// [Refresh] - Force refresh
		tbbRefresh.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Manually refresh records
				refresh();
				Utilities.scaleColumns(tblDishes);
			}
		});
		
		// [LHS Dishes Table] - Enable and disable buttons for order manipulation
		tblDishes.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				// Only trigger update once selection has stopped changing
				if (!e.getValueIsAdjusting()) {
					Quantity<Dish> selection = model_tblDishes.getSelectedTableObject(tblDishes); 
					if (selection == null) {
						btnMinusOne.setEnabled(false);
						btnPlusOne.setEnabled(false);
						btnRemoveAll.setEnabled(false);
					} else {
						btnMinusOne.setEnabled(true);
						btnPlusOne.setEnabled(true);
						btnRemoveAll.setEnabled(true);
					}
				}
			}
		});

		// [Minus One Button] - Minus one from current quantity in order
		btnMinusOne.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Dish dish = model_tblDishes.getSelectedTableObject(tblDishes).getItem(); 
				Double quantity = orderItems.get(dish);
				if (quantity == null || quantity <= 0) {
					return;
				} else if (quantity == 1) {
					orderItems.remove(dish);
				} else {
					orderItems.put(dish, quantity - 1);
				}
				refreshOrder();
			}
		});
		
		// [Plus One Button] - Add one to current quantity in order
		btnPlusOne.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
 				Dish dish = model_tblDishes.getSelectedTableObject(tblDishes).getItem(); 
				Double quantity = orderItems.get(dish);
				orderItems.put(dish, quantity == null ? 1 : quantity + 1);
				refreshOrder();
			}
		});
		
		// [Remove All Button] - Remove dish from order
		btnRemoveAll.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
 				Dish dish = model_tblDishes.getSelectedTableObject(tblDishes).getItem(); 
				orderItems.remove(dish);
				refreshOrder();
			}
		});

		// [Submit Order Button] - Submit order to server
		tbbSubmitOrder.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int res = JOptionPane.showConfirmDialog(null,
						"Are you sure you want to send the current order?", "Send Order", 
						JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
				if (res != JOptionPane.YES_OPTION) {
					return;
				}
				
				ErrorBuilder eb = new ErrorBuilder();
				// Handle model behaviour
				if (application.model.sendOrder(orderItems)) {
					// Wait for message handler to handle response
					if (application.model.orderResponse.waitForNew(
							ClientApplication.REQUEST_TIMEOUT)) {
						eb.append(application.model.orderResponse.readComments());
						// Verify and handle response
						if (!eb.isError()) {
							System.out.println("ORDERED");
							orderItems = new QuantityMap<>();
							refreshOrder();
							refreshDishes();
							res = JOptionPane.showConfirmDialog(null,
									"Order successful! Do you want to view your existing orders?", 
									"Order Successful", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
							if (res == JOptionPane.YES_OPTION) {
								application.setView("View Orders");
							}		
						}
					}
				} else {
					eb.addError("No response from server");
				}
				// Alert user of any errors
				if (eb.isError()) {
					JOptionPane.showMessageDialog(null, eb.listComments("Order Failed"), "Order Failed",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
	}

	@Override
	public void initialise() {
		orderItems = new QuantityMap<>();
		refreshOrder();
		refresh();
	}

	@Override
	public void refresh() {
		refreshDishes();
	}
	
	/** 
	 * Refresh available dishes using the server
	 */
	private void refreshDishes() {
		// Refresh dishes using server
		QuantityMap<Dish> dishes;
		if (application.model.refreshDishes()) {
			// Wait for message handler to handle response
			application.model.dishes.waitForNew(ClientApplication.REQUEST_TIMEOUT);
		}
		dishes = application.model.dishes.readObject();

		// Ensure refresh is on EDT
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				final Object selected = model_tblDishes.getSelectedTableObject(tblDishes);
				model_tblDishes.setList(dishes == null ? null : dishes.getList());
				model_tblDishes.setSelectedTableObject(tblDishes, selected);
			}
		});
		// Verify that current order is still valid
		verifyCurrentOrder(dishes.keySet());
	}
	
	/**
	 * Refresh order table using the current order dishes.
	 */
	private void refreshOrder() {
		model_tblOrder.setList(orderItems.getList());
		// Update model on EDT
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				// Remember the currently selected value
				Object selection = model_tblOrder.getSelectedTableObject(tblOrder);
				model_tblOrder.fireTableDataChanged();
				model_tblOrder.setSelectedTableObject(tblOrder, selection);
				Utilities.scaleColumns(tblOrder);
			}
		});
		nudTotalPrice.setValue(Order.getTotalPrice(orderItems));
	}

	/**
	 * Verify that current order being created does not have any out of date information
	 * based on a collection of available dishes.
	 * 
	 * @param dishes Set of dishes that are available for use
	 */
	private void verifyCurrentOrder(Collection<Dish> dishes) {
		ErrorBuilder eb = new ErrorBuilder();
		// Iterate over order dishes for verification
		Iterator<Dish> itrOrder = orderItems.keySet().iterator();
		while (itrOrder.hasNext()) {
			Dish orderDish = itrOrder.next();
			// Check if dish is still in available dishes
			if (dishes.contains(orderDish)) {
				Dish dish = Utilities.getCollectionItem(dishes, orderDish, Dish.class) ;
				// Check if dish has changed
				if (orderDish.getPrice() != dish.getPrice()) {
					eb.addError(String.format("'%s' price change", dish.getName()));
					orderDish.setPrice(dish.getPrice());
				}
			} else {
				eb.addError(String.format("'%s' no longer exists - removed from basket", orderDish.getName()));
				itrOrder.remove();
			}
		}
		// Alert of any problems with current order
		if (eb.isError()) {
			refreshOrder();
			JOptionPane.showMessageDialog(null, eb.listComments("Dish Change Alert"), "Dish Change Alert",
					JOptionPane.INFORMATION_MESSAGE);
		}
	}
	
	/**
	 * An extension of ListTableModel that displays dishes and a respective quantity.
	 *
	 * @author David Jones [dsj1n15]
	 */
	class DishesListTableModel extends ListTableModel<Quantity<Dish>> {
		private static final long serialVersionUID = 6848893576001534549L;
		private final Class<?>[] COLUMN_CLASSES = {String.class, Double.class, Double.class};

		/**
		 * Instantiate table model with default column titles and classes.
		 */
		public DishesListTableModel() {
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
