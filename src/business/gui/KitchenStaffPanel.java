package business.gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;

import business.model.BusinessModel;
import business.model.KitchenStaffMember;
import business.model.Worker;
import general.gui.ListTableModel;
import general.gui.RecordPanel;
import general.utility.ErrorBuilder;
import general.utility.SerializationUtils;
import general.utility.Utilities;

/**
 * An extension of AbstractRecordPanel that handles kitchen staff.
 *
 * @author David Jones [dsj1n15]
 */
public class KitchenStaffPanel extends RecordPanel<KitchenStaffMember> {
	private static final long serialVersionUID = 2779726580663814359L;
	// Business model
	private final BusinessModel model;
	// Record objects
	private final JTextField txtName;
	private final JLabel lblAction;
	private final JScrollPane scrAction;
	private final JTextArea txtAction;
	private final JButton btnWorking;

	/**
	 * Create the panel.
	 *
	 * @param model Data model being served
	 */
	public KitchenStaffPanel(BusinessModel model) {
		super("Staff Member", "Staff");
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
		// [Record Panel] <- 'Name Field' TextBox
		txtName = new JTextField();
		final GridBagConstraints gbc_txtName = new GridBagConstraints();
		gbc_txtName.insets = new Insets(5, 0, 5, 5);
		gbc_txtName.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtName.gridx = 1;
		gbc_txtName.gridy = 0;
		pnlRecord.add(txtName, gbc_txtName);
		txtName.setColumns(10);

		// [Record Panel] <- 'Current Action' Label
		lblAction = new JLabel("Action:");
		final GridBagConstraints gbc_lblAction = new GridBagConstraints();
		gbc_lblAction.anchor = GridBagConstraints.NORTHEAST;
		gbc_lblAction.insets = new Insets(0, 5, 5, 5);
		gbc_lblAction.gridx = 0;
		gbc_lblAction.gridy = 1;
		pnlRecord.add(lblAction, gbc_lblAction);
		// [Record Panel] <- 'Current Action' TextArea
		txtAction = new JTextArea();
		txtAction.setLineWrap(true);
		txtAction.setTabSize(4);
		txtAction.setEnabled(false);
		scrAction = new JScrollPane(txtAction);
		scrAction.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		// Make scrollable
		final GridBagConstraints gbc_scrAction = new GridBagConstraints();
		gbc_scrAction.insets = new Insets(0, 5, 5, 10);
		gbc_scrAction.fill = GridBagConstraints.BOTH;
		gbc_scrAction.gridx = 1;
		gbc_scrAction.gridy = 1;
		pnlRecord.add(scrAction, gbc_scrAction);
		scrAction.setPreferredSize(new Dimension(0, 50));

		// [Record Panel] <- 'Working ' Button
		btnWorking = new JButton("Starting Working");
		final GridBagConstraints gbc_btnWorking = new GridBagConstraints();
		gbc_btnWorking.gridwidth = 2;
		gbc_btnWorking.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnWorking.insets = new Insets(0, 5, 5, 5);
		gbc_btnWorking.gridx = 0;
		gbc_btnWorking.gridy = 2;
		pnlRecord.add(btnWorking, gbc_btnWorking);

		// Disable Creation/Editing
		setEditEnabled(false);

		// Load relevant table model
		model_tblRecords = new KitchenStaffMemberTableModel();
		tblRecords.setModel(model_tblRecords);
		tblRecords.getRowSorter().toggleSortOrder(0); // sort by name

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
	public boolean setWorking(KitchenStaffMember worker, boolean work) {
		final ErrorBuilder eb = new ErrorBuilder();
		synchronized (model.kitchenStaff) {
			// Get up to date kitchen staff member
			final KitchenStaffMember storedRecord = Utilities.getCollectionItem(model.kitchenStaff,
					worker, KitchenStaffMember.class);
			if (storedRecord == null) {
				eb.addError("Kitchen staff member does not exist in model");
			}
			if (!eb.isError()) {
				try {
					if (work) {
						storedRecord.startWorking();
					} else {
						storedRecord.stopWorking();
					}
					return true;
				} catch (final Exception e) {
					eb.addError(e.getMessage());
				}
			}
		}
		// Show error builder message
		JOptionPane.showMessageDialog(null, eb.listComments("Set Working Failed"),
				"Set Working Failed", JOptionPane.ERROR_MESSAGE);
		return false;
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
				Collection<KitchenStaffMember> kitchenStaff;
				synchronized (model.kitchenStaff) {
					// Cast safe due to known functionality of deep clone
					kitchenStaff = (Collection<KitchenStaffMember>) SerializationUtils
							.deepClone(model.kitchenStaff);
				}
				// Refresh using local copy
				refreshTable(new ArrayList<>(kitchenStaff));
			}
		});
	}

	@Override
	public void loadRecord(KitchenStaffMember record) {
		txtName.setText(record.getIdentifier());
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
		txtName.setText(null);
		txtAction.setText(null);
		btnWorking.setVisible(false);
	}

	@Override
	public KitchenStaffMember createRecord() {
		final KitchenStaffMember kitchenStaffMember =
				new KitchenStaffMember(txtName.getText(), model.stock);
		return kitchenStaffMember;
	}

	@Override
	public void updateRecord(KitchenStaffMember record) {
		throw new IllegalStateException("Unsupported operation");
	}

	@Override
	public boolean saveNewRecord() {
		final ErrorBuilder eb = new ErrorBuilder();
		// Get record without using existing record
		final KitchenStaffMember kitchenStaffMember = createRecord();
		synchronized (model.kitchenStaff) {
			if (model.kitchenStaff.contains(kitchenStaffMember)) {
				eb.addError("Kitchen staff member already exists");
			}
			eb.append(kitchenStaffMember.validate());
			if (!eb.isError()) {
				// Store record in relevant data structure
				model.kitchenStaff.add(kitchenStaffMember);
				return true;
			}
		}
		// Show error builder message
		JOptionPane.showMessageDialog(null, eb.listComments("Save Failed"), "Save Failed",
				JOptionPane.ERROR_MESSAGE);
		return false;
	}

	@Override
	public boolean saveEditedRecord(KitchenStaffMember record) {
		throw new IllegalStateException("Unsupported operation");
	}

	@Override
	public boolean deleteRecord(KitchenStaffMember record) {
		// Check worker is deleteable
		if (!record.isStartable()) {
			JOptionPane.showMessageDialog(null,
					"Kitchen staff member must not be working to be deleted.", "Delete Failed",
					JOptionPane.ERROR_MESSAGE);
			return false;
		}
		// Get confirmation from the user
		final int res = JOptionPane.showConfirmDialog(null,
				"Are you sure you want to delete the selected kitchen staff member?",
				"Delete Record", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
		if (res == JOptionPane.YES_OPTION) {
			// Remove kitchen staff member from model
			synchronized (model.kitchenStaff) {
				model.kitchenStaff.remove(record);
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
				txtName.setEnabled(true);
				lblAction.setVisible(false);
				scrAction.setVisible(false);
				txtName.requestFocusInWindow();
				clearRecord();
				break;
			case VIEW:
				txtName.setEnabled(false);
				lblAction.setVisible(true);
				scrAction.setVisible(true);
				break;
			default:
				break;
		}
	}

	/**
	 * An extension of ListTableModel that displays kitchen staff members.
	 *
	 * @author David Jones [dsj1n15]
	 */
	class KitchenStaffMemberTableModel extends ListTableModel<KitchenStaffMember> {
		private static final long serialVersionUID = -528707502227057343L;
		private final String[] COLUMN_TITLES = {"Name", "Current Action", "Status"};
		private final Class<?>[] COLUMN_CLASSES = {String.class, String.class, Worker.Status.class};

		/**
		 * Instantiate table model with default column titles and classes.
		 */
		public KitchenStaffMemberTableModel() {
			setColumnNames(COLUMN_TITLES);
			setColumnClasses(COLUMN_CLASSES);
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			final KitchenStaffMember kitchenStaffMember = getObjectAt(rowIndex);
			if (kitchenStaffMember == null) {
				return null;
			}
			switch (columnIndex) {
				case 0:
					return kitchenStaffMember.getIdentifier();
				case 1:
					return kitchenStaffMember.getAction();
				case 2:
					return kitchenStaffMember.getStatus();
				default:
					return null;
			}
		}
	}

}
