package business.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import business.model.BusinessModel;
import business.model.Postcode;
import general.gui.ListTableModel;
import general.gui.RecordPanel;
import general.utility.ErrorBuilder;
import general.utility.SerializationUtils;
import general.utility.Utilities;

/**
 * An extension of AbstractRecordPanel that handles postcodes.
 *
 * @author David Jones [dsj1n15]
 */
public class PostcodesPanel extends RecordPanel<Postcode> {
	private static final long serialVersionUID = -2292781651332679948L;
	// Business model
	private final BusinessModel model;
	// Record objects
	private JTextField txtPostcode;
	private JSpinner nudDistance;

	/**
	 * Create the panel.
	 *
	 * @param model Data model being served
	 */
	public PostcodesPanel(BusinessModel model) {
		super("Postcode", "Postcodes");
		// Store model
		this.model = model;
		
		// [Record Panel] - Set layout as grid bag
		final GridBagLayout gbl_pnlRecord = new GridBagLayout();
		gbl_pnlRecord.columnWidths = new int[] {0, 0};
		gbl_pnlRecord.rowHeights = new int[] {0, 0, 0};
		gbl_pnlRecord.columnWeights = new double[] {0.0, 1.0};
		gbl_pnlRecord.rowWeights = new double[] {0.0, 0.0, 1.0};
		pnlRecord.setLayout(gbl_pnlRecord);

		// [Record Panel] <- 'Postcode Field' Label
		JLabel lblPostcode = new JLabel("Postcode:");
		GridBagConstraints gbc_lblPostcode = new GridBagConstraints();
		gbc_lblPostcode.anchor = GridBagConstraints.EAST;
		gbc_lblPostcode.insets = new Insets(5, 5, 5, 5);
		gbc_lblPostcode.gridx = 0;
		gbc_lblPostcode.gridy = 0;
		pnlRecord.add(lblPostcode, gbc_lblPostcode);
		// [Record Panel] <- 'Postcode Field' TextBox
		txtPostcode = new JTextField();
		GridBagConstraints gbc_txtPostcode = new GridBagConstraints();
		gbc_txtPostcode.insets = new Insets(5, 0, 5, 5);
		gbc_txtPostcode.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtPostcode.gridx = 1;
		gbc_txtPostcode.gridy = 0;
		pnlRecord.add(txtPostcode, gbc_txtPostcode);
		txtPostcode.setColumns(10);

		// [Record Panel] <- 'Distance Field' Label
		JLabel lblDistance = new JLabel("Distance (km):");
		GridBagConstraints gbc_lblDistance = new GridBagConstraints();
		gbc_lblDistance.insets = new Insets(0, 5, 5, 5);
		gbc_lblDistance.gridx = 0;
		gbc_lblDistance.gridy = 1;
		pnlRecord.add(lblDistance, gbc_lblDistance);
		// [Record Panel] <- 'Distance Field' Spinner
		nudDistance = new JSpinner(new SpinnerNumberModel(0.0, 0.0, 1000, 0.01));
		GridBagConstraints gbc_nudDistance = new GridBagConstraints();
		gbc_nudDistance.fill = GridBagConstraints.HORIZONTAL;
		gbc_nudDistance.insets = new Insets(0, 0, 5, 5);
		gbc_nudDistance.gridx = 1;
		gbc_nudDistance.gridy = 1;
		pnlRecord.add(nudDistance, gbc_nudDistance);

		// Load relevant table model
		model_tblRecords = new PostcodeTableModel();
		tblRecords.setModel(model_tblRecords);
		tblRecords.getRowSorter().toggleSortOrder(0);
		Utilities.setColumnStringFormat(tblRecords, 1, "%.2f", SwingConstants.RIGHT);

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
				// Create local copy so table not backed by collection ensuring thread
				// safety. This is done as an alternative to locking postcodes
				// whilst refreshing due to the reduced time the lock is required.
				Collection<Postcode> postcodes;
				synchronized (model.postcodes) {
					// Cast safe due to known functionality of deep clone
					postcodes =
							(Collection<Postcode>) SerializationUtils.deepClone(model.postcodes);
				}
				// Refresh using local copy
				refreshTable(new ArrayList<>(postcodes));
			}
		});
	}


	@Override
	public void loadRecord(Postcode record) {
		txtPostcode.setText(record.getPostcode());
		nudDistance.setValue(record.getDistance());
	}

	@Override
	public void clearRecord() {
		txtPostcode.setText(null);
		nudDistance.setValue(0.0);
	}

	@Override
	public Postcode createRecord() {
		final Postcode postcode = new Postcode(txtPostcode.getText().toUpperCase());
		updateRecord(postcode);
		return postcode;
	}

	@Override
	public void updateRecord(Postcode record) {
		record.setDistance((double) nudDistance.getValue());
	}

	@Override
	public void setEditingMode(RecordEditor.EditingMode editingMode) {
		// Ensure parent layout set editing mode
		super.setEditingMode(editingMode);
		// Do child behaviour
		switch (editingMode) {
			case NEW:
				txtPostcode.setEnabled(true);
				nudDistance.setEnabled(true);
				txtPostcode.requestFocusInWindow();
				clearRecord();
				break;
			case EDIT:
				txtPostcode.setEnabled(false);
				nudDistance.setEnabled(true);
				nudDistance.requestFocusInWindow();
				break;
			case VIEW:
				txtPostcode.setEnabled(false);
				nudDistance.setEnabled(false);
				break;
			default:
				break;
		}
	}

	@Override
	public boolean saveNewRecord() {
		final ErrorBuilder eb = new ErrorBuilder();
		// Get record without using existing record
		final Postcode postcode = createRecord();
		synchronized (model.postcodes) {
			if (model.postcodes.contains(postcode)) {
				eb.addError("Postcode already exists");
			}
			eb.append(postcode.validate());
			if (!eb.isError()) {
				// Store record in relevant data structure
				model.postcodes.add(postcode);
				return true;
			}
		}
		// Show error builder message
		JOptionPane.showMessageDialog(null, eb.listComments("Save Failed"), "Save Failed",
				JOptionPane.ERROR_MESSAGE);
		return false;
	}

	@Override
	public boolean saveEditedRecord(Postcode record) {
		ErrorBuilder eb = new ErrorBuilder();
		synchronized (model.suppliers) {
			// Validate record fields
			final Postcode validationRecord = createRecord();
			eb = validationRecord.validate();
			// Get existing entry to update
			final Postcode storedRecord =
					Utilities.getCollectionItem(model.postcodes, record, Postcode.class);
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
	public boolean deleteRecord(Postcode record) {
		// Get confirmation from the user
		final int res = JOptionPane.showConfirmDialog(null,
				"Are you sure you want to delete the selected postcode?\r\n"
						+ "The postcode will no longer be available for deliveries, \r\n"
						+ "customers with this postcode must change their postcode \r\n"
						+ "to be create any new orders.",
				"Delete Record", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
		if (res == JOptionPane.YES_OPTION) {
			// Remove postcode from model
			synchronized (model.postcodes) {
				model.postcodes.remove(record);
			}
			return true;
		}
		return false;
	}

	/**
	 * An extension of ListTableModel that displays postcodes.
	 *
	 * @author David Jones [dsj1n15]
	 */
	class PostcodeTableModel extends ListTableModel<Postcode> {
		private static final long serialVersionUID = -4968121277188554566L;
		private final String[] COLUMN_TITLES = {"Name", "Distance (km)"};
		private final Class<?>[] COLUMN_CLASSES = {String.class, Double.class};

		/**
		 * Instantiate table model with default column titles and classes.
		 */
		public PostcodeTableModel() {
			setColumnNames(COLUMN_TITLES);
			setColumnClasses(COLUMN_CLASSES);
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			final Postcode postcode = getObjectAt(rowIndex);
			if (postcode == null) {
				return null;
			}
			switch (columnIndex) {
				case 0:
					return postcode.getPostcode();
				case 1:
					return postcode.getDistance();
				default:
					return null;
			}
		}

	}

}
