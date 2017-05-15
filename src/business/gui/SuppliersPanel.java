package business.gui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import business.model.BusinessModel;
import business.model.Ingredient;
import business.model.Supplier;
import general.gui.ListTableModel;
import general.gui.RecordPanel;
import general.utility.ErrorBuilder;
import general.utility.SerializationUtils;
import general.utility.Utilities;

/**
 * An extension of AbstractRecordPanel that handles suppliers.
 *
 * @author David Jones [dsj1n15]
 */
public class SuppliersPanel extends RecordPanel<Supplier> {
	private static final long serialVersionUID = -5294497133864971568L;
	// Business model
	private final BusinessModel model;
	// Record objects
	private final JTextField txtName;
	private final JSpinner nudDistance;
	private final JLabel lblRestocking;
	private final JPanel pnlRestocking;

	/**
	 * Create the panel.
	 *
	 * @param model Data model being served
	 */
	public SuppliersPanel(BusinessModel model) {
		super("Supplier", "Suppliers");
		// Store model
		this.model = model;
		
		// [Record Panel] - Set layout as grid bag
		final GridBagLayout gbl_pnlRecord = new GridBagLayout();
		gbl_pnlRecord.columnWidths = new int[] {0, 0};
		gbl_pnlRecord.rowHeights = new int[] {0, 0, 0, 0};
		gbl_pnlRecord.columnWeights = new double[] {0.0, 1.0};
		gbl_pnlRecord.rowWeights = new double[] {0.0, 0.0, 0.0, 1.0};
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

		// [Record Panel] <- 'Distance Field' Label
		final JLabel lblDistance = new JLabel("Distance (km):");
		final GridBagConstraints gbc_lblDistance = new GridBagConstraints();
		gbc_lblDistance.anchor = GridBagConstraints.EAST;
		gbc_lblDistance.insets = new Insets(0, 5, 5, 5);
		gbc_lblDistance.gridx = 0;
		gbc_lblDistance.gridy = 1;
		pnlRecord.add(lblDistance, gbc_lblDistance);
		// [Record Panel] <- 'Distance Field' Spinner
		nudDistance = new JSpinner(new SpinnerNumberModel(0.0, 0.0, 1000, 0.01));
		final GridBagConstraints gbc_nudDistance = new GridBagConstraints();
		gbc_nudDistance.fill = GridBagConstraints.HORIZONTAL;
		gbc_nudDistance.insets = new Insets(0, 0, 5, 5);
		gbc_nudDistance.gridx = 1;
		gbc_nudDistance.gridy = 1;
		pnlRecord.add(nudDistance, gbc_nudDistance);

		// [Record Panel] <- 'Restocking Field' Label
		lblRestocking = new JLabel("Restocking:");
		final GridBagConstraints gbc_lblRestocking = new GridBagConstraints();
		gbc_lblRestocking.anchor = GridBagConstraints.EAST;
		gbc_lblRestocking.insets = new Insets(0, 5, 5, 5);
		gbc_lblRestocking.gridx = 0;
		gbc_lblRestocking.gridy = 2;
		pnlRecord.add(lblRestocking, gbc_lblRestocking);
		// [Record Panel] <- 'Restocking Field' Panel
		pnlRestocking = new JPanel();
		final GridBagConstraints gbc_pnlRestocking = new GridBagConstraints();
		gbc_pnlRestocking.fill = GridBagConstraints.BOTH;
		gbc_pnlRestocking.insets = new Insets(0, 5, 5, 10);
		gbc_pnlRestocking.ipady = 10;
		gbc_pnlRestocking.gridx = 1;
		gbc_pnlRestocking.gridy = 2;
		pnlRecord.add(pnlRestocking, gbc_pnlRestocking);
		pnlRestocking.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

		// Load relevant table model
		model_tblRecords = new SupplierTableModel();
		tblRecords.setModel(model_tblRecords);
		tblRecords.getRowSorter().toggleSortOrder(0);
		Utilities.setColumnStringFormat(tblRecords, 1, "%.2f", SwingConstants.RIGHT);
		Utilities.setColumnColouredBoolean(tblRecords, 2, Utilities.COLOR_RED,
				Utilities.COLOR_GREEN);

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
				// safety. This is done as an alternative to locking suppliers
				// whilst refreshing due to the reduced time the lock is required.
				Collection<Supplier> suppliers;
				synchronized (model.suppliers) {
					// Cast safe due to known functionality of deep clone
					suppliers =
							(Collection<Supplier>) SerializationUtils.deepClone(model.suppliers);
				}
				// Refresh using local copy
				refreshTable(new ArrayList<>(suppliers));
			}
		});
	}

	@Override
	public void loadRecord(Supplier record) {
		txtName.setText(record.getName());
		nudDistance.setValue(record.getDistance());
		if (record.isBeingRestocked()) {
			pnlRestocking.setBackground(Utilities.COLOR_GREEN);
		} else {
			pnlRestocking.setBackground(Utilities.COLOR_RED);
		}
		pnlRestocking.setOpaque(true);
		pnlRestocking.repaint();
	}

	@Override
	public void clearRecord() {
		txtName.setText(null);
		nudDistance.setValue(0.0);
		pnlRestocking.setOpaque(false);
		pnlRestocking.repaint();
	}

	@Override
	public Supplier createRecord() {
		final Supplier supplier = new Supplier(txtName.getText());
		updateRecord(supplier);
		return supplier;
	}

	@Override
	public void updateRecord(Supplier record) {
		record.setDistance((double) nudDistance.getValue());
	}

	@Override
	public void setEditingMode(RecordEditor.EditingMode editingMode) {
		// Ensure parent layout set editing mode
		super.setEditingMode(editingMode);
		// Do child behaviour
		switch (editingMode) {
			case NEW:
				txtName.setEnabled(true);
				nudDistance.setEnabled(true);
				lblRestocking.setVisible(false);
				pnlRestocking.setVisible(false);
				txtName.requestFocusInWindow();
				clearRecord();
				break;
			case EDIT:
				txtName.setEnabled(false);
				nudDistance.setEnabled(true);
				lblRestocking.setVisible(false);
				pnlRestocking.setVisible(false);
				nudDistance.requestFocusInWindow();
				break;
			case VIEW:
				txtName.setEnabled(false);
				nudDistance.setEnabled(false);
				lblRestocking.setVisible(true);
				pnlRestocking.setVisible(true);
				break;
			default:
				break;
		}
	}

	@Override
	public boolean saveNewRecord() {
		final ErrorBuilder eb = new ErrorBuilder();
		// Get record without using existing record
		final Supplier supplier = createRecord();
		synchronized (model.suppliers) {
			if (model.suppliers.contains(supplier)) {
				eb.addError("Supplier already exists");
			}
			eb.append(supplier.validate());
			if (!eb.isError()) {
				// Store record in relevant data structure
				model.suppliers.add(supplier);
				return true;
			}
		}
		// Show error builder message
		JOptionPane.showMessageDialog(null, eb.listComments("Save Failed"), "Save Failed",
				JOptionPane.ERROR_MESSAGE);
		return false;
	}

	@Override
	public boolean saveEditedRecord(Supplier record) {
		ErrorBuilder eb = new ErrorBuilder();
		synchronized (model.suppliers) {
			// Validate record fields
			final Supplier validationRecord = createRecord();
			eb = validationRecord.validate();
			// Get existing entry to update
			final Supplier storedRecord =
					Utilities.getCollectionItem(model.suppliers, record, Supplier.class);
			if (storedRecord == null) {
				eb.addError("Record being edited does not exist in model");
			}
			if (!eb.isError()) {
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
	public boolean deleteRecord(Supplier record) {
		// Get confirmation from the user
		final int res = JOptionPane.showConfirmDialog(null,
				"Are you sure you want to delete the selected supplier?\r\n"
						+ "The supplier will be removed from any ingredients that use it.",
				"Delete Record", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
		if (res == JOptionPane.YES_OPTION) {
			// Remove supplier from model
			synchronized (model.suppliers) {
				model.suppliers.remove(record);
			}
			// Handle affect on ingredients
			synchronized (model.stock.dishes) {
				for (final Ingredient ingredient : model.stock.ingredients.keySet()) {
					// Remove supplier from ingredient
					if (ingredient.getSupplier().equals(record)) {
						ingredient.setSupplier(null);
					}
				}
			}
			return true;
		}
		return false;
	}

	/**
	 * An extension of ListTableModel that displays suppliers.
	 *
	 * @author David Jones [dsj1n15]
	 */
	class SupplierTableModel extends ListTableModel<Supplier> {
		private static final long serialVersionUID = 6848893576001534549L;
		private final String[] COLUMN_TITLES = {"Name", "Distance (km)", "Restocking"};
		private final Class<?>[] COLUMN_CLASSES = {String.class, Double.class, Boolean.class};

		/**
		 * Instantiate table model with default column titles and classes.
		 */
		public SupplierTableModel() {
			setColumnNames(COLUMN_TITLES);
			setColumnClasses(COLUMN_CLASSES);
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			final Supplier supplier = getObjectAt(rowIndex);
			if (supplier == null) {
				return null;
			}
			switch (columnIndex) {
				case 0:
					return supplier.getName();
				case 1:
					return supplier.getDistance();
				case 2:
					return supplier.isBeingRestocked();
				default:
					return null;
			}
		}

	}

}
