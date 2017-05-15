package business.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Arrays;
import java.util.Map.Entry;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import business.model.BusinessModel;
import business.model.Dish;
import business.model.Ingredient;
import business.model.StockItem;
import business.model.StockLevels;
import business.model.StockMap;
import business.model.Supplier;
import general.gui.ListTableModel;
import general.gui.RecordPanel;
import general.utility.ErrorBuilder;
import general.utility.SerializationUtils;
import general.utility.Utilities;

/**
 * An extension of AbstractRecordPanel that handles ingredients and their respective stock levels.
 *
 * @author David Jones [dsj1n15]
 */
public class IngredientsPanel extends RecordPanel<StockItem<Ingredient>> {
	private static final long serialVersionUID = 5163613751286161072L;
	// Business model
	private final BusinessModel model;
	// Record objects
	private final JTextField txtName;
	private final JComboBox<Ingredient.Unit> cboUnit;
	private final JComboBox<Supplier> cboSupplier;
	private final StockPanel pnlStockLevels;

	/**
	 * Create the panel.
	 *
	 * @param model Data model being served
	 */
	public IngredientsPanel(BusinessModel model) {
		super("Ingredient", "Ingredients");
		// Store model
		this.model = model;

		// [Record Panel] - Set layout as grid bag
		final GridBagLayout gbl_pnlRecord = new GridBagLayout();
		gbl_pnlRecord.columnWidths = new int[] {0, 0};
		gbl_pnlRecord.rowHeights = new int[] {0, 0, 0, 0, 0, 0};
		gbl_pnlRecord.columnWeights = new double[] {0.0, 1.0};
		gbl_pnlRecord.rowWeights = new double[] {0.0, 0.0, 0.0, 0.0, 1.0};
		pnlRecord.setLayout(gbl_pnlRecord);

		// [Record Panel] <- 'Name Field' Label
		final JLabel lblName = new JLabel("Name:");
		final GridBagConstraints gbc_lblName = new GridBagConstraints();
		gbc_lblName.anchor = GridBagConstraints.EAST;
		gbc_lblName.insets = new Insets(5, 5, 5, 5);
		gbc_lblName.gridx = 0;
		gbc_lblName.gridy = 0;
		pnlRecord.add(lblName, gbc_lblName);
		// [Record Panel] <- 'Name Field' TextField
		txtName = new JTextField();
		final GridBagConstraints gbc_txtName = new GridBagConstraints();
		gbc_txtName.insets = new Insets(5, 0, 5, 5);
		gbc_txtName.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtName.gridx = 1;
		gbc_txtName.gridy = 0;
		pnlRecord.add(txtName, gbc_txtName);

		// [Record Panel] <- 'Unit Field' Label
		final JLabel lblUnit = new JLabel("Unit:");
		final GridBagConstraints gbc_lblUnit = new GridBagConstraints();
		gbc_lblUnit.anchor = GridBagConstraints.EAST;
		gbc_lblUnit.insets = new Insets(0, 5, 5, 5);
		gbc_lblUnit.gridx = 0;
		gbc_lblUnit.gridy = 1;
		pnlRecord.add(lblUnit, gbc_lblUnit);
		// [Record Panel] <- 'Name Field' ComboBox
		cboUnit = new JComboBox<>();
		final GridBagConstraints gbc_cboUnit = new GridBagConstraints();
		gbc_cboUnit.insets = new Insets(0, 0, 5, 5);
		gbc_cboUnit.fill = GridBagConstraints.HORIZONTAL;
		gbc_cboUnit.gridx = 1;
		gbc_cboUnit.gridy = 1;
		pnlRecord.add(cboUnit, gbc_cboUnit);

		// [Record Panel] <- 'Supplier Field' Label
		final JLabel lblSupplier = new JLabel("Supplier:");
		final GridBagConstraints gbc_lblSupplier = new GridBagConstraints();
		gbc_lblSupplier.anchor = GridBagConstraints.EAST;
		gbc_lblSupplier.insets = new Insets(0, 5, 5, 5);
		gbc_lblSupplier.gridx = 0;
		gbc_lblSupplier.gridy = 2;
		pnlRecord.add(lblSupplier, gbc_lblSupplier);
		// [Record Panel] <- 'Supplier Field' ComboBox
		cboSupplier = new JComboBox<>();
		final GridBagConstraints gbc_cboSupplier = new GridBagConstraints();
		gbc_cboSupplier.insets = new Insets(0, 0, 5, 5);
		gbc_cboSupplier.fill = GridBagConstraints.HORIZONTAL;
		gbc_cboSupplier.gridx = 1;
		gbc_cboSupplier.gridy = 2;
		pnlRecord.add(cboSupplier, gbc_cboSupplier);

		// [Record Panel] <- 'Stock Panel'
		pnlStockLevels = new StockPanel();
		final GridBagConstraints gbc_pnlStock = new GridBagConstraints();
		gbc_pnlStock.gridwidth = 2;
		gbc_pnlStock.insets = new Insets(5, 5, 5, 5);
		gbc_pnlStock.fill = GridBagConstraints.BOTH;
		gbc_pnlStock.gridx = 0;
		gbc_pnlStock.gridy = 3;
		pnlRecord.add(pnlStockLevels, gbc_pnlStock);

		// Load relevant table model
		model_tblRecords = new IngredientTableModel();
		tblRecords.setModel(model_tblRecords);
		tblRecords.getRowSorter().toggleSortOrder(0);
		Utilities.setColumnStringFormat(tblRecords, 3, "%.2f", SwingConstants.RIGHT);
		Utilities.setColumnStringFormat(tblRecords, 4, "%.2f", SwingConstants.RIGHT);
		Utilities.setColumnStringFormat(tblRecords, 5, "%.2f", SwingConstants.RIGHT);
		Utilities.setColumnStringFormat(tblRecords, 7, "%.2f", SwingConstants.RIGHT);
		Utilities.setColumnColouredBoolean(tblRecords, 8, Utilities.COLOR_RED,
				Utilities.COLOR_GREEN);

		// Finalise
		setEditingMode(RecordEditor.EditingMode.VIEW);
	}

	/**
	 * Load ingredient unit types into relevant combo box.
	 */
	private void loadUnits() {
		// Load units to combo box using new model
		cboUnit.setModel(new DefaultComboBoxModel<>(Ingredient.Unit.values()));
	}

	/**
	 * Load suppliers from model into relevant combo box.
	 */
	private void loadSuppliers() {
		// Get array of suppliers
		Supplier[] array;
		synchronized (model.suppliers) {
			array = model.suppliers.toArray(new Supplier[model.suppliers.size()]);
			Arrays.sort(array);
		}
		final ComboBoxModel<Supplier> model = new DefaultComboBoxModel<>(array);
		cboSupplier.setModel(model);
	}

	@Override
	public void initialise() {
		refresh();
		// Ensure initialising is on EDT
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				loadUnits();
				loadSuppliers();
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
				// safety. This is done as an alternative to locking ingredients
				// whilst refreshing due to the reduced time the lock is required.
				StockMap<Ingredient> ingredients;
				synchronized (model.stock.ingredients) {
					// Cast safe due to known functionality of deep clone
					ingredients = (StockMap<Ingredient>) SerializationUtils
							.deepClone(model.stock.ingredients);
				}
				// Refresh using local copy
				refreshTable(ingredients.getList());
			}
		});
	}

	@Override
	public void loadRecord(StockItem<Ingredient> record) {
		if (record != null) {
			// Load Ingredient
			final Ingredient ingredient = record.getItem();
			txtName.setText(ingredient.getName());
			cboUnit.setSelectedItem(ingredient.getUnit());
			cboSupplier.setSelectedItem(ingredient.getSupplier());
			// Load Stock Levels
			pnlStockLevels.loadRecord(record.getStockLevels());
		}
	}

	@Override
	public void clearRecord() {
		txtName.setText(null);
		cboUnit.setSelectedIndex(-1);
		cboSupplier.setSelectedIndex(-1);
		pnlStockLevels.clearRecord();
	}

	@Override
	public StockItem<Ingredient> createRecord() {
		// Create new record structure using final fields
		final Ingredient ingredient =
				new Ingredient(txtName.getText(), (Ingredient.Unit) cboUnit.getSelectedItem());
		final StockLevels levels = new StockLevels();
		final StockItem<Ingredient> record = new StockItem<>(ingredient, levels);
		// Update non-final fields
		updateRecord(record);
		return record;
	}

	@Override
	public void updateRecord(StockItem<Ingredient> record) {
		// Update ingredient fields
		final Ingredient ingredient = record.getItem();
		ingredient.setSupplier((Supplier) cboSupplier.getSelectedItem());
		// Update stock levels
		final StockLevels levels = record.getStockLevels();
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
				cboUnit.setEnabled(true);
				cboSupplier.setEnabled(true);
				txtName.requestFocusInWindow();
				clearRecord();
				break;
			case EDIT:
				txtName.setEnabled(false);
				cboUnit.setEnabled(false);
				cboSupplier.setEnabled(true);
				cboSupplier.requestFocusInWindow();
				break;
			case VIEW:
				txtName.setEnabled(false);
				cboUnit.setEnabled(false);
				cboSupplier.setEnabled(false);
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
		final StockItem<Ingredient> newRecord = createRecord();
		final Ingredient ingredient = newRecord.getItem();
		synchronized (model.stock.ingredients) {
			if (model.stock.ingredients.get(ingredient) != null) {
				eb.addError("Ingredient already being stocked");
			}
			eb.append(ingredient.validate());
			if (!eb.isError()) {
				// Store record in relevant data structure
				model.stock.ingredients.add(newRecord);
				return true;
			}
		}
		// Show error builder message
		JOptionPane.showMessageDialog(null, eb.listComments("Save Failed"), "Save Failed",
				JOptionPane.ERROR_MESSAGE);
		return false;
	}

	@Override
	public boolean saveEditedRecord(StockItem<Ingredient> record) {
		ErrorBuilder eb = new ErrorBuilder();
		synchronized (model.stock.dishes) {
			// Validate record fields
			final StockItem<Ingredient> validationRecord = createRecord();
			eb = validationRecord.getItem().validate();
			// Get existing entry to update
			final Entry<Ingredient, StockLevels> storedEntry = Utilities.getMapEntry(
					model.stock.ingredients, record.getItem(), Ingredient.class, StockLevels.class);
			if (storedEntry == null) {
				eb.addError("Record being edited does not exist in model");
			}
			if (!eb.isError()) {
				// Convert entry to record to update
				final StockItem<Ingredient> storedRecord =
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
	public boolean deleteRecord(StockItem<Ingredient> record) {
		// Get confirmation from the user
		final int res = JOptionPane.showConfirmDialog(null,
				"Are you sure you want to delete the selected ingredient?\r\n"
						+ "Any dishes using the ingredient will be made unstockable and "
						+ "the ingredient will be removed.",
				"Delete Record", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
		if (res == JOptionPane.YES_OPTION) {
			// Remove ingredient from stock
			synchronized (model.stock.ingredients) {
				model.stock.ingredients.remove(record.getItem());
			}
			// Handle affect on dishes
			synchronized (model.stock.dishes) {
				for (final Entry<Dish, StockLevels> entry : model.stock.dishes.entrySet()) {
					final Dish dish = entry.getKey();
					// Remove ingredient from recipe (if found)
					if (dish.getIngredients().remove(record.getItem()) != null) {
						// Make dish unstockable if
						entry.getValue().setStockable(false);
					}
				}
			}
			return true;
		}
		return false;
	}

	/**
	 * An extension of ListTableModel that displays ingredients and their respective stock levels.
	 *
	 * @author David Jones [dsj1n15]
	 */
	class IngredientTableModel extends ListTableModel<StockItem<Ingredient>> {
		private static final long serialVersionUID = -528707502227057343L;
		private final String[] COLUMN_TITLES =
				{"Name", "Unit", "Supplier", "RES", "RST", "TS", "RL", "SA", "STK"};
		private final Class<?>[] COLUMN_CLASSES =
				{String.class, Ingredient.Unit.class, String.class, Double.class, Double.class,
						Double.class, Double.class, Double.class, Boolean.class};

		/**
		 * Instantiate table model with default column titles and classes.
		 */
		public IngredientTableModel() {
			setColumnNames(COLUMN_TITLES);
			setColumnClasses(COLUMN_CLASSES);
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			final StockItem<Ingredient> record = getObjectAt(rowIndex);
			if (record == null) {
				return null;
			}
			final Ingredient ingredient = record.getItem();
			final StockLevels levels = record.getStockLevels();
			switch (columnIndex) {
				case 0:
					return ingredient.getName();
				case 1:
					return ingredient.getUnit();
				case 2:
					final Supplier supplier = ingredient.getSupplier();
					return (supplier == null) ? "-" : supplier.getName();
				case 3:
					return levels.getReserved();
				case 4:
					return levels.getRestocking();
				case 5:
					return levels.getStock();
				case 6:
					return levels.getRestockLevel();
				case 7:
					return levels.getStockAvailable();
				case 8:
					return levels.isStockable();
				default:
					return null;
			}
		}

	}

}
