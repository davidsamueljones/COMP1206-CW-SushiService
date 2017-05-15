package business.gui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import business.model.BusinessModel;
import business.model.Customer;
import business.model.CustomerLogin;
import business.model.Postcode;
import general.gui.ListTableModel;
import general.gui.RecordPanel;
import general.utility.SerializationUtils;
import general.utility.Utilities;

/**
 * An extension of AbstractRecordPanel that handles customers. Customers can only be deleted.
 *
 * @author David Jones [dsj1n15]
 */
public class CustomersPanel extends RecordPanel<Customer> {
	private static final long serialVersionUID = 5315161679613333363L;
	// Business model
	private final BusinessModel model;
	// Record objects
	private final JTextField txtUsername;
	private final JTextField txtFullName;
	private final JTextField txtAddress;
	private final JTextField txtPostcode;
	private final JPanel pnlValid;

	/**
	 * Create the panel.
	 *
	 * @param model Data model being served
	 */
	public CustomersPanel(BusinessModel model) {
		super("Customer", "Customers");
		// Store model
		this.model = model;
		
		// [Record Panel] - Set layout as grid bag
		final GridBagLayout gbl_pnlRecord = new GridBagLayout();
		gbl_pnlRecord.columnWidths = new int[] {0, 0};
		gbl_pnlRecord.rowHeights = new int[] {0, 0, 0, 0, 0, 0};
		gbl_pnlRecord.columnWeights = new double[] {0.0, 1.0};
		gbl_pnlRecord.rowWeights = new double[] {0.0, 0.0, 0.0, 0.0, 0.0, 1.0};
		pnlRecord.setLayout(gbl_pnlRecord);

		// [Record Panel] <- 'Username Field' Label
		final JLabel lblUsername = new JLabel("Username:");
		final GridBagConstraints gbc_lblUsername = new GridBagConstraints();
		gbc_lblUsername.anchor = GridBagConstraints.EAST;
		gbc_lblUsername.insets = new Insets(5, 5, 5, 5);
		gbc_lblUsername.gridx = 0;
		gbc_lblUsername.gridy = 0;
		pnlRecord.add(lblUsername, gbc_lblUsername);
		// [Record Panel] <- 'Username Field' TextField
		txtUsername = new JTextField();
		final GridBagConstraints gbc_txtUsername = new GridBagConstraints();
		gbc_txtUsername.insets = new Insets(5, 0, 5, 5);
		gbc_txtUsername.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtUsername.gridx = 1;
		gbc_txtUsername.gridy = 0;
		pnlRecord.add(txtUsername, gbc_txtUsername);
		txtUsername.setColumns(10);
		txtUsername.setEnabled(false);

		// [Record Panel] <- 'Full Name Field' Label
		final JLabel lblFullName = new JLabel("Full Name:");
		final GridBagConstraints gbc_lblFullName = new GridBagConstraints();
		gbc_lblFullName.anchor = GridBagConstraints.EAST;
		gbc_lblFullName.insets = new Insets(0, 5, 5, 5);
		gbc_lblFullName.gridx = 0;
		gbc_lblFullName.gridy = 1;
		pnlRecord.add(lblFullName, gbc_lblFullName);
		// [Record Panel] <- 'Full Name Field' TextBox
		txtFullName = new JTextField();
		final GridBagConstraints gbc_txtFullName = new GridBagConstraints();
		gbc_txtFullName.anchor = GridBagConstraints.NORTH;
		gbc_txtFullName.insets = new Insets(0, 0, 5, 5);
		gbc_txtFullName.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtFullName.gridx = 1;
		gbc_txtFullName.gridy = 1;
		pnlRecord.add(txtFullName, gbc_txtFullName);
		txtFullName.setColumns(10);
		txtFullName.setEnabled(false);

		// [Record Panel] <- 'Address Field' Label
		final JLabel lblAddress = new JLabel("Address:");
		final GridBagConstraints gbc_lblAddress = new GridBagConstraints();
		gbc_lblAddress.anchor = GridBagConstraints.EAST;
		gbc_lblAddress.insets = new Insets(0, 5, 5, 5);
		gbc_lblAddress.gridx = 0;
		gbc_lblAddress.gridy = 2;
		pnlRecord.add(lblAddress, gbc_lblAddress);
		// [Record Panel] <- 'Address Field' TextField
		txtAddress = new JTextField();
		final GridBagConstraints gbc_txtAddress = new GridBagConstraints();
		gbc_txtAddress.insets = new Insets(0, 0, 5, 5);
		gbc_txtAddress.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtAddress.gridx = 1;
		gbc_txtAddress.gridy = 2;
		pnlRecord.add(txtAddress, gbc_txtAddress);
		txtAddress.setEnabled(false);

		// [Record Panel] <- 'Postcode Field' Label
		final JLabel lblPostcode = new JLabel("Postcode:");
		final GridBagConstraints gbc_lblPostcode = new GridBagConstraints();
		gbc_lblPostcode.anchor = GridBagConstraints.EAST;
		gbc_lblPostcode.insets = new Insets(0, 5, 5, 5);
		gbc_lblPostcode.gridx = 0;
		gbc_lblPostcode.gridy = 3;
		pnlRecord.add(lblPostcode, gbc_lblPostcode);
		// [Record Panel] <- 'Postcode Field' TextField
		txtPostcode = new JTextField();
		final GridBagConstraints gbc_txtPostcode = new GridBagConstraints();
		gbc_txtPostcode.anchor = GridBagConstraints.NORTH;
		gbc_txtPostcode.insets = new Insets(0, 0, 5, 5);
		gbc_txtPostcode.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtPostcode.gridx = 1;
		gbc_txtPostcode.gridy = 3;
		pnlRecord.add(txtPostcode, gbc_txtPostcode);
		txtPostcode.setEnabled(false);

		// [Record Panel] <- 'Valid Field' Label
		final JLabel lblValid = new JLabel("Valid:");
		final GridBagConstraints gbc_lblValid = new GridBagConstraints();
		gbc_lblValid.anchor = GridBagConstraints.EAST;
		gbc_lblValid.insets = new Insets(0, 5, 5, 5);
		gbc_lblValid.gridx = 0;
		gbc_lblValid.gridy = 4;
		pnlRecord.add(lblValid, gbc_lblValid);
		// [Record Panel] <- 'Valid Field' Panel
		pnlValid = new JPanel();
		final GridBagConstraints gbc_pnlValid = new GridBagConstraints();
		gbc_pnlValid.fill = GridBagConstraints.BOTH;
		gbc_pnlValid.insets = new Insets(0, 5, 5, 10);
		gbc_pnlValid.ipady = 10;
		gbc_pnlValid.gridx = 1;
		gbc_pnlValid.gridy = 4;
		pnlRecord.add(pnlValid, gbc_pnlValid);
		pnlValid.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

		// Disable Creation/Editing
		setNewEnabled(false);
		setEditEnabled(false);

		// Load relevant table model
		model_tblRecords = new CustomerTableModel();
		tblRecords.setModel(model_tblRecords);
		tblRecords.getRowSorter().toggleSortOrder(1); // sort by name
		Utilities.setColumnColouredBoolean(tblRecords, 0, Utilities.COLOR_RED,
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
				Collection<Customer> customers;
				synchronized (model.customers) {
					// Convert collection of values to set so serialisable
					final Set<Customer> modelCustomers = new HashSet<>(model.customers.values());
					// Cast safe due to known functionality of deep clone
					customers = (Collection<Customer>) SerializationUtils.deepClone(modelCustomers);
				}
				// Refresh using local copy
				refreshTable(new ArrayList<>(customers));
			}
		});
	}

	@Override
	public void loadRecord(Customer record) {
		final CustomerLogin login = record.getLogin();
		txtUsername.setText(login.getUsername());
		txtFullName.setText(record.getName());
		txtPostcode.setText(record.getPostcode().getPostcode());
		txtAddress.setText(record.getAddress());
		if (login.isChecksumValid()) {
			pnlValid.setBackground(Utilities.COLOR_GREEN);
		} else {
			pnlValid.setBackground(Utilities.COLOR_RED);
		}
		pnlValid.setOpaque(true);
		pnlValid.repaint();
	}

	@Override
	public void clearRecord() {
		txtUsername.setText(null);
		txtFullName.setText(null);
		txtAddress.setText(null);
		txtPostcode.setText(null);
		pnlValid.setOpaque(false);
		pnlValid.repaint();
	}

	@Override
	public Customer createRecord() {
		throw new IllegalStateException("Unsupported operation");
	}

	@Override
	public void updateRecord(Customer record) {
		throw new IllegalStateException("Unsupported operation");
	}

	@Override
	public boolean saveNewRecord() {
		throw new IllegalStateException("Unsupported operation");
	}

	@Override
	public boolean saveEditedRecord(Customer record) {
		throw new IllegalStateException("Unsupported operation");
	}

	@Override
	public boolean deleteRecord(Customer record) {
		// Get confirmation from the user
		final int res = JOptionPane.showConfirmDialog(null,
				"Are you sure you want to delete the selected customer?", "Delete Record",
				JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
		if (res == JOptionPane.YES_OPTION) {
			// Remove customer from model
			synchronized (model.customers) {
				model.customers.remove(record.getLogin());
			}
			return true;
		}
		return false;
	}

	/**
	 * An extension of ListTableModel that displays customers.
	 *
	 * @author David Jones [dsj1n15]
	 */
	class CustomerTableModel extends ListTableModel<Customer> {
		private static final long serialVersionUID = 7294325804359010564L;
		private final String[] COLUMN_TITLES =
				{"Valid", "Username", "Full Name", "Address", "Postcode"};
		private final Class<?>[] COLUMN_CLASSES =
				{Boolean.class, String.class, String.class, String.class, Postcode.class};

		/**
		 * Instantiate table model with default column titles and classes.
		 */
		public CustomerTableModel() {
			setColumnNames(COLUMN_TITLES);
			setColumnClasses(COLUMN_CLASSES);
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			final Customer customer = getObjectAt(rowIndex);
			if (customer == null) {
				return null;
			}
			final CustomerLogin login = customer.getLogin();
			switch (columnIndex) {
				case 0:
					return login.isChecksumValid();
				case 1:
					return login.getUsername();
				case 2:
					return customer.getName();
				case 3:
					return customer.getAddress();
				case 4:
					return customer.getPostcode();
				default:
					return null;
			}
		}

	}
}
