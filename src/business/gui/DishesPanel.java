package business.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

import business.model.BusinessModel;
import business.model.Dish;
import business.model.Ingredient;
import business.model.StockItem;
import business.model.StockLevels;
import business.model.StockMap;
import general.gui.ListTableModel;
import general.gui.RecordPanel;
import general.model.Quantity;
import general.model.QuantityMap;
import general.utility.ErrorBuilder;
import general.utility.SerializationUtils;
import general.utility.Utilities;

/**
 * An extension of AbstractRecordPanel that handles dishes and their respective stock levels.
 *
 * @author David Jones [dsj1n15]
 */
public class DishesPanel extends RecordPanel<StockItem<Dish>> {
	private static final long serialVersionUID = -6211990855581822837L;
	// Business model
	private final BusinessModel model;
	// Record objects
	private JTextField txtName;
	private JTextArea txtDescription;
	private JSpinner nudPrice;
	private JTable tblIngredients;
	private IngredientListTableModel model_tblIngredients;
	private JPanel pnlIngredientControls;
	private JComboBox<Ingredient> cboIngredient;
	private JSpinner nudIngredientQuantity;
	private JButton btnSetIngredientQuantity;
	private StockPanel pnlStockLevels;

	/**
	 * Create the panel.
	 *
	 * @param model Data model being served
	 */
	public DishesPanel(BusinessModel model) {
		super("Dish", "Dishes");
		// Store model
		this.model = model;
		
		// [Record Panel] - Set layout as grid bag
		final GridBagLayout gbl_pnlRecord = new GridBagLayout();
		gbl_pnlRecord.columnWidths = new int[] {0, 0};
		gbl_pnlRecord.rowHeights = new int[] {0, 0, 0, 0, 0, 0, 0};
		gbl_pnlRecord.columnWeights = new double[] {0.0, 1.0};
		gbl_pnlRecord.rowWeights = new double[] {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		pnlRecord.setLayout(gbl_pnlRecord);

		// [Record Panel] <- 'Name Field' Label
		final JLabel lblName = new JLabel("Name:");
		final GridBagConstraints gbc_lblName = new GridBagConstraints();
		gbc_lblName.anchor = GridBagConstraints.EAST;
		gbc_lblName.insets = new Insets(5, 5, 5, 5);
		gbc_lblName.gridx = 0;
		gbc_lblName.gridy = 0;
		pnlRecord.add(lblName, gbc_lblName);
		// [Record Panel] <- 'Name Field' TextBox
		txtName = new JTextField();
		final GridBagConstraints gbc_txtName = new GridBagConstraints();
		gbc_txtName.insets = new Insets(5, 0, 5, 5);
		gbc_txtName.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtName.gridx = 1;
		gbc_txtName.gridy = 0;
		pnlRecord.add(txtName, gbc_txtName);

		// [Record Panel] <- 'Description Field' Label
		final JLabel lblDescription = new JLabel("Description:");
		final GridBagConstraints gbc_lblDescription = new GridBagConstraints();
		gbc_lblDescription.anchor = GridBagConstraints.EAST;
		gbc_lblDescription.insets = new Insets(0, 5, 5, 5);
		gbc_lblDescription.gridx = 0;
		gbc_lblDescription.gridy = 1;
		pnlRecord.add(lblDescription, gbc_lblDescription);
		// [Record Panel] <- 'Description Field' TextArea
		txtDescription = new JTextArea();
		txtDescription.setLineWrap(true);
		txtDescription.setTabSize(4);
		final JScrollPane scrDescription = new JScrollPane(txtDescription);
		scrDescription.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		// Make scrollable
		final GridBagConstraints gbc_scrDescription = new GridBagConstraints();
		gbc_scrDescription.insets = new Insets(0, 5, 5, 10);
		gbc_scrDescription.fill = GridBagConstraints.BOTH;
		gbc_scrDescription.gridx = 1;
		gbc_scrDescription.gridy = 1;
		pnlRecord.add(scrDescription, gbc_scrDescription);
		scrDescription.setPreferredSize(new Dimension(0, 100));

		// [Record Panel] <- 'Price Field' Label
		final JLabel lblPrice = new JLabel("Price (£):");
		final GridBagConstraints gbc_lblPrice = new GridBagConstraints();
		gbc_lblPrice.insets = new Insets(0, 5, 5, 5);
		gbc_lblPrice.anchor = GridBagConstraints.EAST;
		gbc_lblPrice.gridx = 0;
		gbc_lblPrice.gridy = 2;
		pnlRecord.add(lblPrice, gbc_lblPrice);
		// [Record Panel] <- 'Price Field' Spinner
		nudPrice = new JSpinner(new SpinnerNumberModel(0.0, 0.0, 1000, 0.01));
		// Enforce price format 0.00
		final JSpinner.NumberEditor editor = (JSpinner.NumberEditor) nudPrice.getEditor();
		final DecimalFormat format = editor.getFormat();
		format.setMinimumFractionDigits(2);
		format.setMaximumFractionDigits(2);
		final GridBagConstraints gbc_nudPrice = new GridBagConstraints();
		gbc_nudPrice.insets = new Insets(0, 0, 5, 5);
		gbc_nudPrice.fill = GridBagConstraints.HORIZONTAL;
		gbc_nudPrice.gridx = 1;
		gbc_nudPrice.gridy = 2;
		pnlRecord.add(nudPrice, gbc_nudPrice);

		// [Record Panel] <- 'Ingredients' Panel
		final JPanel pnlIngredients = new JPanel();
		final GridBagConstraints gbc_pnlIngredients = new GridBagConstraints();
		gbc_pnlIngredients.gridwidth = 2;
		gbc_pnlIngredients.insets = new Insets(0, 5, 5, 5);
		gbc_pnlIngredients.fill = GridBagConstraints.BOTH;
		gbc_pnlIngredients.gridx = 0;
		gbc_pnlIngredients.gridy = 3;
		pnlRecord.add(pnlIngredients, gbc_pnlIngredients);
		final GridBagLayout gbl_pnlIngredients = new GridBagLayout();
		gbl_pnlIngredients.columnWidths = new int[] {0};
		gbl_pnlIngredients.rowHeights = new int[] {0, 0, 0};
		gbl_pnlIngredients.columnWeights = new double[] {1.0};
		gbl_pnlIngredients.rowWeights = new double[] {0.0, 0.0, Double.MIN_VALUE};
		pnlIngredients.setLayout(gbl_pnlIngredients);
		pnlIngredients
				.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
						"Ingredients", TitledBorder.LEADING, TitledBorder.TOP));

		// [Ingredients Panel] <- 'Ingredients Field' Table
		tblIngredients = new JTable();
		tblIngredients.setEnabled(false);
		tblIngredients.setGridColor(Color.LIGHT_GRAY);
		tblIngredients.setAutoCreateRowSorter(true);
		model_tblIngredients = new IngredientListTableModel();
		tblIngredients.setModel(model_tblIngredients);
		tblIngredients.getRowSorter().toggleSortOrder(0);
		Utilities.setColumnStringFormat(tblIngredients, 2, "%.2f", SwingConstants.RIGHT);
		// Make scrollable
		final JScrollPane scrIngredients = new JScrollPane(tblIngredients);
		scrIngredients.setBackground(this.getBackground());
		final GridBagConstraints gbc_scrIngredients = new GridBagConstraints();
		gbc_scrIngredients.ipady = 100;
		gbc_scrIngredients.fill = GridBagConstraints.BOTH;
		gbc_scrIngredients.insets = new Insets(5, 5, 5, 5);
		gbc_scrIngredients.gridx = 0;
		gbc_scrIngredients.gridy = 0;
		pnlIngredients.add(scrIngredients, gbc_scrIngredients);
		scrIngredients.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
		scrIngredients.setPreferredSize(new Dimension(0, 0));

		// [Ingredients Panel] <- 'Ingredient Controls' Table
		pnlIngredientControls = new JPanel();
		final GridBagConstraints gbc_pnlIngredientControls = new GridBagConstraints();
		gbc_pnlIngredientControls.fill = GridBagConstraints.BOTH;
		gbc_pnlIngredientControls.insets = new Insets(0, 5, 5, 5);
		gbc_pnlIngredientControls.gridx = 0;
		gbc_pnlIngredientControls.gridy = 1;
		pnlIngredients.add(pnlIngredientControls, gbc_pnlIngredientControls);
		final GridBagLayout gbl_pnlIngredientControls = new GridBagLayout();
		gbl_pnlIngredientControls.columnWidths = new int[] {0, 0};
		gbl_pnlIngredientControls.rowHeights = new int[] {0, 0, 0, 0};
		gbl_pnlIngredientControls.columnWeights = new double[] {0.0, 1.0};
		gbl_pnlIngredientControls.rowWeights = new double[] {0.0, 0.0, 0.0, Double.MIN_VALUE};
		pnlIngredientControls.setLayout(gbl_pnlIngredientControls);

		// [Ingredients Controls Panel] <- 'Ingredient' ComboBox
		cboIngredient = new JComboBox<>();
		final GridBagConstraints gbc_cboNewIngredient = new GridBagConstraints();
		gbc_cboNewIngredient.gridwidth = 2;
		gbc_cboNewIngredient.insets = new Insets(0, 0, 5, 0);
		gbc_cboNewIngredient.fill = GridBagConstraints.HORIZONTAL;
		gbc_cboNewIngredient.gridx = 0;
		gbc_cboNewIngredient.gridy = 0;
		pnlIngredientControls.add(cboIngredient, gbc_cboNewIngredient);

		// [Ingredients Controls Panel] <- 'Quantity' Label
		final JLabel lblQuantity = new JLabel("Quantity:");
		final GridBagConstraints gbc_lblQuantity = new GridBagConstraints();
		gbc_lblQuantity.insets = new Insets(0, 0, 5, 5);
		gbc_lblQuantity.gridx = 0;
		gbc_lblQuantity.gridy = 1;
		pnlIngredientControls.add(lblQuantity, gbc_lblQuantity);

		// [Ingredients Controls Panel] <- 'Quantity' Spinner
		nudIngredientQuantity = new JSpinner(new SpinnerNumberModel(0.0, 0.0, 1000, 0.01));
		final GridBagConstraints gbc_nudIngredientQuantity = new GridBagConstraints();
		gbc_nudIngredientQuantity.insets = new Insets(0, 0, 5, 0);
		gbc_nudIngredientQuantity.fill = GridBagConstraints.HORIZONTAL;
		gbc_nudIngredientQuantity.gridx = 1;
		gbc_nudIngredientQuantity.gridy = 1;
		pnlIngredientControls.add(nudIngredientQuantity, gbc_nudIngredientQuantity);

		// [Ingredients Controls Panel] <- 'Set Ingredient Quantity' Button
		btnSetIngredientQuantity = new JButton("Set Ingredient Quantity");
		final GridBagConstraints gbc_btnRemoveIngredient = new GridBagConstraints();
		gbc_btnRemoveIngredient.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnRemoveIngredient.insets = new Insets(0, 0, 5, 0);
		gbc_btnRemoveIngredient.gridwidth = 2;
		gbc_btnRemoveIngredient.gridx = 0;
		gbc_btnRemoveIngredient.gridy = 2;
		pnlIngredientControls.add(btnSetIngredientQuantity, gbc_btnRemoveIngredient);

		// [Record Panel] <- 'Stock Panel'
		pnlStockLevels = new StockPanel();
		final GridBagConstraints gbc_pnlStockLevels = new GridBagConstraints();
		gbc_pnlStockLevels.fill = GridBagConstraints.BOTH;
		gbc_pnlStockLevels.insets = new Insets(0, 5, 5, 5);
		gbc_pnlStockLevels.gridwidth = 2;
		gbc_pnlStockLevels.gridx = 0;
		gbc_pnlStockLevels.gridy = 4;
		pnlRecord.add(pnlStockLevels, gbc_pnlStockLevels);

		// Load relevant table model
		model_tblRecords = new DishesTableModel();
		tblRecords.setModel(model_tblRecords);
		tblRecords.getRowSorter().toggleSortOrder(0);
		Utilities.setColumnStringFormat(tblRecords, 1, Utilities.PRICE_FORMAT,
				SwingConstants.RIGHT);
		Utilities.setColumnColouredBoolean(tblRecords, 7, Utilities.COLOR_RED,
				Utilities.COLOR_GREEN);

		// Finalise
		setEditingMode(RecordEditor.EditingMode.VIEW);

		// [Ingredient ComboBox] - Display current quantity on selection
		cboIngredient.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				final Ingredient selected = (Ingredient) cboIngredient.getSelectedItem();
				if (selected == null) {
					nudIngredientQuantity.setValue(0.0);
				} else {
					final List<Quantity<Ingredient>> recipe = model_tblIngredients.getList();
					for (final Quantity<Ingredient> current : recipe) {
						if (selected.equals(current.getItem())) {
							nudIngredientQuantity.setValue(current.getQuantity());
							break;
						}
					}
				}
			}
		});

		// [Set Ingredient Quantity] - Set ingredient quantity in recipe
		btnSetIngredientQuantity.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Verify that an ingredient is selected
				final Ingredient selected = (Ingredient) cboIngredient.getSelectedItem();
				if (selected == null) {
					JOptionPane.showMessageDialog(null, "No ingredient selected", "Ingredient Add",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				final List<Quantity<Ingredient>> recipe = model_tblIngredients.getList();
				final Double quantity = (Double) nudIngredientQuantity.getValue();

				final Iterator<Quantity<Ingredient>> itr = recipe.iterator();
				// Check if ingredient exists
				while (itr.hasNext()) {
					final Quantity<Ingredient> current = itr.next();
					if (selected.equals(current.getItem())) {
						// Remove existing item
						if (quantity == 0) {
							itr.remove();
							// Replace quantity in item
						} else {
							current.setQuantity(quantity);
						}
						model_tblIngredients.fireTableDataChanged();
						return;
					}
				}
				// As ingredient does not exist, add if quantity not 0
				if (quantity > 0) {
					recipe.add(new Quantity<>(selected, quantity));
					model_tblIngredients.fireTableDataChanged();
				}
			}
		});

	}

	/**
	 * Load available ingredients into combo box This thread should be invoked on the EDT
	 */
	private void loadIngredients() {
		final Ingredient[] array;
		synchronized (model.stock.ingredients) {
			final Set<Ingredient> set = model.stock.ingredients.keySet();
			array = set.toArray(new Ingredient[set.size()]);
		}
		Arrays.sort(array);
		final ComboBoxModel<Ingredient> model = new DefaultComboBoxModel<>(array);
		cboIngredient.setModel(model);
		cboIngredient.setSelectedIndex(-1);
	}

	@Override
	public void initialise() {
		refresh();
		// Ensure initialising is on EDT
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				setEditingMode(RecordEditor.EditingMode.VIEW);
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
				// safety. This is done as an alternative to locking dishes
				// whilst refreshing due to the reduced time the lock is required.
				StockMap<Dish> dishes;
				synchronized (model.stock.dishes) {
					// Cast safe due to known functionality of deep clone
					dishes = (StockMap<Dish>) SerializationUtils.deepClone(model.stock.dishes);
				}
				// Refresh using local copy
				refreshTable(dishes.getList());
			}
		});
	}

	@Override
	public void loadRecord(StockItem<Dish> record) {
		if (record != null) {
			// Load Dish
			final Dish dish = record.getItem();
			txtName.setText(dish.getName());
			txtDescription.setText(dish.getDescription());
			nudPrice.setValue(dish.getPrice());
			model_tblIngredients.setList(dish.getIngredients().getList());
			// Load Stock Levels
			pnlStockLevels.loadRecord(record.getStockLevels());
		}
	}

	@Override
	public void clearRecord() {
		txtName.setText(null);
		txtDescription.setText(null);
		nudPrice.setValue(0);
		model_tblIngredients.setList(new ArrayList<Quantity<Ingredient>>());
		cboIngredient.setSelectedIndex(-1);
		pnlStockLevels.clearRecord();
	}

	@Override
	public StockItem<Dish> createRecord() {
		// Create new record structure using final fields
		final Dish dish = new Dish(txtName.getText());
		final StockLevels levels = new StockLevels();
		final StockItem<Dish> record = new StockItem<>(dish, levels);
		// Update non-final fields
		updateRecord(record);
		return record;
	}

	@Override
	public void updateRecord(StockItem<Dish> record) {
		final Dish dish = record.getItem();
		final StockLevels levels = record.getStockLevels();
		// Update dish fields
		dish.setDescription(txtDescription.getText());
		dish.setPrice((double) nudPrice.getValue());
		// Update ingredients from table quantity map
		final QuantityMap<Ingredient> ingredients = new QuantityMap<>();
		for (final Quantity<Ingredient> ingredient : model_tblIngredients.getList()) {
			ingredients.put(ingredient);
		}
		dish.setIngredients(ingredients);
		// Update stock levels
		pnlStockLevels.updateRecord(levels);
	}

	@Override
	public void setEditingMode(RecordEditor.EditingMode editingMode) {
		// Ensure parent layout set editing mode
		super.setEditingMode(editingMode);
		// Do child behaviour
		switch (editingMode) {
			case NEW:
				txtName.setEnabled(true);
				txtDescription.setEnabled(true);
				nudPrice.setEnabled(true);
				pnlIngredientControls.setVisible(true);
				txtName.requestFocusInWindow();
				clearRecord();
				loadIngredients();
				break;
			case EDIT:
				txtName.setEnabled(false);
				txtDescription.setEnabled(true);
				nudPrice.setEnabled(true);
				pnlIngredientControls.setVisible(true);
				loadIngredients();
				txtDescription.requestFocusInWindow();
				break;
			case VIEW:
				txtName.setEnabled(false);
				txtDescription.setEnabled(false);
				nudPrice.setEnabled(false);
				pnlIngredientControls.setVisible(false);
				break;
			default:
				break;
		}
		pnlStockLevels.setEditingMode(editingMode);
	}

	@Override
	public boolean saveNewRecord() {
		final ErrorBuilder eb = new ErrorBuilder();
		// Get record without using existing record
		final StockItem<Dish> newRecord = createRecord();
		final Dish dish = newRecord.getItem();
		synchronized (model.stock.dishes) {
			if (model.stock.dishes.get(dish) != null) {
				eb.addError("Dish already being stocked");
			}
			eb.append(dish.validate());
			if (!eb.isError()) {
				// Store record in relevant data structure
				model.stock.dishes.add(dish, newRecord.getStockLevels());
				return true;
			}
		}
		// Show error builder message
		JOptionPane.showMessageDialog(null, eb.listComments("Save Failed"), "Save Failed",
				JOptionPane.ERROR_MESSAGE);
		return false;
	}

	@Override
	public boolean saveEditedRecord(StockItem<Dish> record) {
		ErrorBuilder eb = new ErrorBuilder();
		synchronized (model.stock.dishes) {
			// Validate record fields
			final StockItem<Dish> validationRecord = createRecord();
			eb = validationRecord.getItem().validate();
			// Get existing entry to update
			final Entry<Dish, StockLevels> storedEntry = Utilities.getMapEntry(model.stock.dishes,
					record.getItem(), Dish.class, StockLevels.class);
			if (storedEntry == null) {
				eb.addError("Record being edited does not exist in model");
			}
			if (!eb.isError()) {
				// Convert entry to record to update
				final StockItem<Dish> storedRecord =
						new StockItem<>(storedEntry.getKey(), storedEntry.getValue());
				// Do updates
				updateRecord(storedRecord);
				return true;
			}
		}
		// Show error builder message
		JOptionPane.showMessageDialog(null, eb.listComments("Save Failed"), "Save Failed",
				JOptionPane.ERROR_MESSAGE);
		return false;
	}

	@Override
	public boolean deleteRecord(StockItem<Dish> record) {
		// Get confirmation from the user
		final int res = JOptionPane.showConfirmDialog(null,
				"Are you sure you want to delete the selected dish?", "Delete Record",
				JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
		if (res == JOptionPane.YES_OPTION) {
			// Remove dish from stock
			synchronized (model.stock.dishes) {
				model.stock.dishes.remove(record.getItem());
			}
			return true;
		}
		return false;
	}

	/**
	 * An extension of ListTableModel that displays dishes and their respective stock levels.
	 *
	 * @author David Jones [dsj1n15]
	 */
	class DishesTableModel extends ListTableModel<StockItem<Dish>> {
		private static final long serialVersionUID = -528707502227057343L;
		private final String[] COLUMN_TITLES =
				{"Name", "Price (£)", "RES", "RST", "TS", "RL", "SA", "STK"};
		private final Class<?>[] COLUMN_CLASSES = {String.class, Double.class, Integer.class,
				Integer.class, Integer.class, Integer.class, Integer.class, Boolean.class};

		/**
		 * Instantiate table model with default column titles and classes.
		 */
		public DishesTableModel() {
			setColumnNames(COLUMN_TITLES);
			setColumnClasses(COLUMN_CLASSES);
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			final StockItem<Dish> record = getObjectAt(rowIndex);
			if (record == null) {
				return null;
			}
			final Dish dish = record.getItem();
			final StockLevels levels = record.getStockLevels();
			switch (columnIndex) {
				case 0:
					return dish.getName();
				case 1:
					return dish.getPrice();
				case 2:
					return (int) levels.getReserved();
				case 3:
					return (int) levels.getRestocking();
				case 4:
					return (int) levels.getStock();
				case 5:
					return levels.getRestockLevel();
				case 6:
					return (int) levels.getStockAvailable();
				case 7:
					return levels.isStockable();
				default:
					return null;
			}
		}
	}

	/**
	 * An extension of ListTableModel that displays ingredients and a respective quantity.
	 *
	 * @author David Jones [dsj1n15]
	 */
	class IngredientListTableModel extends ListTableModel<Quantity<Ingredient>> {
		private static final long serialVersionUID = 6848893576001534549L;
		private final String[] COLUMN_TITLES = {"Name", "Unit", "Quantity"};
		private final Class<?>[] COLUMN_CLASSES = {String.class, String.class, Double.class};

		/**
		 * Instantiate table model with default column titles and classes.
		 */
		public IngredientListTableModel() {
			setColumnNames(COLUMN_TITLES);
			setColumnClasses(COLUMN_CLASSES);
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			final Quantity<Ingredient> record = getObjectAt(rowIndex);
			if (record == null) {
				return null;
			}
			final Ingredient ingredient = record.getItem();
			switch (columnIndex) {
				case 0:
					return ingredient.getName();
				case 1:
					return ingredient.getUnit();
				case 2:
					return record.getQuantity();
				default:
					return null;
			}
		}

	}

}
