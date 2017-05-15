package business.gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import business.model.BusinessModel;
import business.model.Drone;
import business.model.Worker;
import general.gui.ListTableModel;
import general.gui.RecordPanel;
import general.utility.ErrorBuilder;
import general.utility.SerializationUtils;
import general.utility.Utilities;

/**
 * An extension of AbstractRecordPanel that handles drones.
 *
 * @author David Jones [dsj1n15]
 */
public class DronesPanel extends RecordPanel<Drone> {
	private static final long serialVersionUID = 8542794426225148740L;
	// Business model
	private final BusinessModel model;
	// Record objects
	private final JTextField txtIdentifier;
	private final JSpinner nudSpeed;
	private final JLabel lblAction;
	private final JScrollPane scrAction;
	private final JTextArea txtAction;
	private final JButton btnWorking;

	/**
	 * Create the panel.
	 *
	 * @param model Data model being served
	 */
	public DronesPanel(BusinessModel model) {
		super("Drone", "Drones");
		// Store model
		this.model = model;

		// [Record Panel] - Set layout as grid bag
		final GridBagLayout gbl_pnlRecord = new GridBagLayout();
		gbl_pnlRecord.columnWidths = new int[] {0, 0};
		gbl_pnlRecord.rowHeights = new int[] {0, 0, 0, 0, 0, 0};
		gbl_pnlRecord.columnWeights = new double[] {0.0, 1.0};
		gbl_pnlRecord.rowWeights = new double[] {0.0, 0.0, 0.0, 0.0, 1.0};
		pnlRecord.setLayout(gbl_pnlRecord);

		// [Record Panel] <- 'Identifier Field' Label
		final JLabel lblIdentifier = new JLabel("Identifier:");
		final GridBagConstraints gbc_lblIdentifier = new GridBagConstraints();
		gbc_lblIdentifier.anchor = GridBagConstraints.EAST;
		gbc_lblIdentifier.insets = new Insets(5, 5, 5, 5);
		gbc_lblIdentifier.gridx = 0;
		gbc_lblIdentifier.gridy = 0;
		pnlRecord.add(lblIdentifier, gbc_lblIdentifier);
		// [Record Panel] <- 'Identifier Field' TextField
		txtIdentifier = new JTextField();
		final GridBagConstraints gbc_txtIdentifier = new GridBagConstraints();
		gbc_txtIdentifier.insets = new Insets(5, 0, 5, 5);
		gbc_txtIdentifier.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtIdentifier.gridx = 1;
		gbc_txtIdentifier.gridy = 0;
		pnlRecord.add(txtIdentifier, gbc_txtIdentifier);
		txtIdentifier.setEnabled(false);

		// [Record Panel] <- 'Speed Field' Label
		final JLabel lblSpeed = new JLabel("Speed (km/h):");
		final GridBagConstraints gbc_lblSpeed = new GridBagConstraints();
		gbc_lblSpeed.anchor = GridBagConstraints.EAST;
		gbc_lblSpeed.insets = new Insets(0, 5, 5, 5);
		gbc_lblSpeed.gridx = 0;
		gbc_lblSpeed.gridy = 1;
		pnlRecord.add(lblSpeed, gbc_lblSpeed);
		// [Record Panel] <- 'Speed Field' Spinner
		nudSpeed = new JSpinner(new SpinnerNumberModel(0.0, 0.0, 1000, 0.01));
		final GridBagConstraints gbc_nudSpeed = new GridBagConstraints();
		gbc_nudSpeed.insets = new Insets(0, 0, 5, 5);
		gbc_nudSpeed.fill = GridBagConstraints.HORIZONTAL;
		gbc_nudSpeed.gridx = 1;
		gbc_nudSpeed.gridy = 1;
		pnlRecord.add(nudSpeed, gbc_nudSpeed);

		// [Record Panel] <- 'Current Action' Label
		lblAction = new JLabel("Action:");
		final GridBagConstraints gbc_lblAction = new GridBagConstraints();
		gbc_lblAction.anchor = GridBagConstraints.NORTHEAST;
		gbc_lblAction.insets = new Insets(0, 5, 5, 5);
		gbc_lblAction.gridx = 0;
		gbc_lblAction.gridy = 2;
		pnlRecord.add(lblAction, gbc_lblAction);
		// [Record Panel] <- 'Current Action' TextArea
		txtAction = new JTextArea();
		txtAction.setLineWrap(true);
		txtAction.setTabSize(4);
		txtAction.setEnabled(false);
		// Make scrollable
		scrAction = new JScrollPane(txtAction);
		scrAction.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		final GridBagConstraints gbc_scrAction = new GridBagConstraints();
		gbc_scrAction.insets = new Insets(0, 5, 5, 10);
		gbc_scrAction.fill = GridBagConstraints.BOTH;
		gbc_scrAction.gridx = 1;
		gbc_scrAction.gridy = 2;
		pnlRecord.add(scrAction, gbc_scrAction);
		scrAction.setPreferredSize(new Dimension(0, 50));

		// [Record Panel] <- 'Working' Button
		btnWorking = new JButton();
		final GridBagConstraints gbc_btnWorking = new GridBagConstraints();
		gbc_btnWorking.gridwidth = 2;
		gbc_btnWorking.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnWorking.insets = new Insets(0, 5, 5, 5);
		gbc_btnWorking.gridx = 0;
		gbc_btnWorking.gridy = 3;
		pnlRecord.add(btnWorking, gbc_btnWorking);

		// Load relevant table model
		model_tblRecords = new DroneTableModel();
		tblRecords.setModel(model_tblRecords);
		tblRecords.getRowSorter().toggleSortOrder(0); // sort by identifier
		Utilities.setColumnStringFormat(tblRecords, 1, "%.2f", SwingConstants.RIGHT);

		// Finalise
		setEditingMode(RecordEditor.EditingMode.VIEW);

		// [Working Button] <- Toggle whether worker is working
		btnWorking.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (loadedRecord.isStartable()) {
					setWorking(loadedRecord, true);
				} else if (loadedRecord.isStoppable()) {
					setWorking(loadedRecord, false);
				}
				// Trigger update immediately
				refresh();
			}
		});
	}

	/**
	 * Stop a worker.
	 *
	 * @param worker Worker to stop
	 * @param work Whether worker should stop
	 * @return Whether worker was stoppable
	 */
	public boolean setWorking(Drone worker, boolean work) {
		final ErrorBuilder eb = new ErrorBuilder();
		synchronized (model.kitchenStaff) {
			// Get up to date kitchen staff member
			final Drone storedRecord =
					Utilities.getCollectionItem(model.drones, worker, Drone.class);
			if (storedRecord == null) {
				eb.addError("Drone does not exist in model");
			}
			if (!eb.isError()) {
				try {
					if (work) {
						storedRecord.startWorking();
					} else {
						storedRecord.stopWorking();
					}
					return true;
				} catch (Exception e) {
					eb.addError(e.getMessage());
				}
			}
		}
		// Show error builder message
		JOptionPane.showMessageDialog(null, eb.listComments("Set Working Failed"),
				"Set Working Failed", JOptionPane.ERROR_MESSAGE);
		return false;
	}

	/**
	 * Get a random and unique identifier for a drone.
	 *
	 * @return New identifier
	 */
	public String getNewIdentifier() {
		// Find all current identifiers
		Set<String> existing;
		synchronized (model.drones) {
			existing = new HashSet<>(model.drones.size());
			for (final Drone drone : model.drones) {
				existing.add(drone.getIdentifier());
			}
		}
		// Generate a random identifier that is not a current identiifer
		while (true) {
			final String newIdentifier = Drone.generateIdentifier(8);
			if (!existing.contains(newIdentifier)) {
				return newIdentifier;
			}
		}
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
				// safety. This is done as an alternative to locking kitchen staff
				// whilst refreshing due to the reduced time the lock is required.
				Collection<Drone> drones;
				synchronized (model.drones) {
					// Cast safe due to known functionality of deep clone
					drones = (Collection<Drone>) SerializationUtils.deepClone(model.drones);
				}
				// Refresh using local copy
				refreshTable(new ArrayList<>(drones));
			}
		});
	}

	@Override
	public void loadRecord(Drone record) {
		txtIdentifier.setText(record.getIdentifier());
		nudSpeed.setValue(record.getSpeed());
		txtAction.setText(record.getAction());
		btnWorking.setVisible(true);
		switch (record.getStatus()) {
			case STOPPED:
				btnWorking.setText("Start Working");
				btnWorking.setEnabled(true);
				break;
			case STOPPING:
				btnWorking.setText("Stopping...");
				btnWorking.setEnabled(false);
				break;
			case WORKING:
				btnWorking.setText("Stop Working");
				btnWorking.setEnabled(true);
				break;
			default:
				break;
		}
	}

	@Override
	public void clearRecord() {
		txtIdentifier.setText(null);
		nudSpeed.setValue(0.0);
		txtAction.setText(null);
		btnWorking.setVisible(false);
	}

	@Override
	public Drone createRecord() {
		final Drone drone = new Drone(txtIdentifier.getText(), model);
		updateRecord(drone);
		return drone;
	}

	@Override
	public void updateRecord(Drone record) {
		record.setSpeed((double) nudSpeed.getValue());
	}

	@Override
	public boolean saveNewRecord() {
		final ErrorBuilder eb = new ErrorBuilder();
		// Get record without using existing record
		final Drone drone = createRecord();
		synchronized (model.drones) {
			if (model.drones.contains(drone)) {
				eb.addError("Drone with given identifier already exists");
			}
			eb.append(drone.validate());
			if (!eb.isError()) {
				// Store record in relevant data structure
				model.drones.add(drone);
				return true;
			}
		}
		// Show error builder message
		JOptionPane.showMessageDialog(null, eb.listComments("Save Failed"), "Save Failed",
				JOptionPane.ERROR_MESSAGE);
		return false;
	}

	@Override
	public boolean saveEditedRecord(Drone record) {
		ErrorBuilder eb = new ErrorBuilder();
		synchronized (model.drones) {
			// Validate record fields
			final Drone validationRecord = createRecord();
			eb = validationRecord.validate();
			// Get existing entry to update
			final Drone storedRecord =
					Utilities.getCollectionItem(model.drones, record, Drone.class);
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
	public boolean deleteRecord(Drone record) {
		// Check worker is deleteable
		if (!record.isStartable()) {
			JOptionPane.showMessageDialog(null, "Drone must not be working to be deleted.",
					"Delete Failed", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		// Get confirmation from the user
		final int res = JOptionPane.showConfirmDialog(null,
				"Are you sure you want to delete the selected drone?", "Delete Record",
				JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
		if (res == JOptionPane.YES_OPTION) {
			// Remove drone from model
			synchronized (model.drones) {
				model.drones.remove(record);
			}
			return true;
		}
		return false;
	}

	@Override
	public void setEditingMode(RecordEditor.EditingMode editingMode) {
		// Ensure parent layout set editing mode
		super.setEditingMode(editingMode);
		// Do child behaviour
		switch (editingMode) {
			case NEW:
				nudSpeed.setEnabled(true);
				lblAction.setVisible(false);
				scrAction.setVisible(false);
				btnWorking.setVisible(false);
				nudSpeed.requestFocusInWindow();
				clearRecord();
				// Generate a new unique name
				txtIdentifier.setText(getNewIdentifier());
				break;
			case EDIT:
				nudSpeed.setEnabled(true);
				lblAction.setVisible(false);
				scrAction.setVisible(false);
				btnWorking.setVisible(false);
				nudSpeed.requestFocusInWindow();
				break;
			case VIEW:
				nudSpeed.setEnabled(false);
				lblAction.setVisible(true);
				scrAction.setVisible(true);
				break;
			default:
				break;
		}
	}

	/**
	 * An extension of ListTableModel that displays drones.
	 *
	 * @author David Jones [dsj1n15]
	 */
	class DroneTableModel extends ListTableModel<Drone> {
		private static final long serialVersionUID = -528707502227057343L;
		private final String[] COLUMN_TITLES =
				{"Identifier", "Speed (km/h)", "Current Action", "Status"};
		private final Class<?>[] COLUMN_CLASSES =
				{String.class, Double.class, String.class, Worker.Status.class};

		/**
		 * Instantiate table model with default column titles and classes.
		 */
		public DroneTableModel() {
			setColumnNames(COLUMN_TITLES);
			setColumnClasses(COLUMN_CLASSES);
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			final Drone drone = getObjectAt(rowIndex);
			if (drone == null) {
				return null;
			}
			switch (columnIndex) {
				case 0:
					return drone.getIdentifier();
				case 1:
					return drone.getSpeed();
				case 2:
					return drone.getAction();
				case 3:
					return drone.getStatus();
				default:
					return null;
			}
		}
	}

}
