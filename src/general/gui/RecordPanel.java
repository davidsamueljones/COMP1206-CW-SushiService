package general.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import business.gui.RecordEditor;
import general.utility.Utilities;

/**
 * Abstract GUI class that handles the creation, editing and viewing of records. Records are defined
 * as multiple items of any single data type. Implementation dictates the record's table model and
 * how a single record can be created/edited.
 *
 * @author David Jones [dsj1n15]
 *
 * @param <T> Type being handled by record panel
 */
public abstract class RecordPanel<T> extends JPanel implements View, RecordEditor<T> {
	private static final long serialVersionUID = -9219503779318689377L;
	private static final int MIN_WIDTH_LHS = 300;

	// Content containers to be filled by class instance
	protected JPanel pnlRecord;
	protected JTable tblRecords;
	protected ListTableModel<T> model_tblRecords;

	// Structural containers
	private JSplitPane splitPane;
	private JPanel pnlLHS;
	private JPanel pnlRHS;
	private JScrollPane scrRecord;
	// LHS ToolBar
	protected ToolBar tlbRecord;
	protected ToolBarButton tbbNew;
	protected ToolBarButton tbbEdit;
	protected ToolBarButton tbbDelete;
	protected ToolBarButton tbbSave;
	protected ToolBarButton tbbCancel;
	// RHS ToolBar
	protected ToolBar tlbRecords;
	protected ToolBarButton tbbExpandTable;
	protected ToolBarButton tbbRefresh;

	// Current editing mode
	protected RecordEditor.EditingMode editingMode;
	private boolean newEnabled = true;
	private boolean editEnabled = true;
	private boolean deleteEnabled = true;

	// The record displayed on the LHS; this does not necessarily mean
	// the record that is currently selected in tblRecords.
	protected T loadedRecord;
	// Initialisation size of JScrollPane (scroll bar size)
	private Dimension scrollBarSize;

	/**
	 * Instantiate the panel with default type.
	 */
	public RecordPanel() {
		this("Record", "Records");
	}

	/**
	 * Instantiate the panel with given type.
	 *
	 * @param singleType Type of a single record
	 * @param pluralType Plural of single record
	 */
	public RecordPanel(String singleType, String pluralType) {
		// Initialise GUI
		initGUI(singleType, pluralType);
		// Initialise event listeners
		initEventListeners();
	}

	/**
	 * Create the GUI.
	 */
	private void initGUI(String singleType, String pluralType) {
		// --- LHS (Record)
		// Create tool bar
		tlbRecord = new ToolBar(getBackground(), Color.BLACK, Color.WHITE);
		tlbRecord.addSeparator();
		final JLabel lblRecordTitle = tlbRecord.addLabel(singleType, SwingConstants.CENTER);
		lblRecordTitle.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		tlbRecord.addSeparator();
		// Add record editing buttons to tool bar
		tbbNew = tlbRecord.addButton("New", Utilities.loadImage(new File("resources/imgNew.png")));
		tbbEdit =
				tlbRecord.addButton("Edit", Utilities.loadImage(new File("resources/imgEdit.png")));
		tbbDelete = tlbRecord.addButton("Delete",
				Utilities.loadImage(new File("resources/imgDelete.png")));
		tbbSave =
				tlbRecord.addButton("Save", Utilities.loadImage(new File("resources/imgSave.png")));
		tbbCancel = tlbRecord.addButton("Cancel",
				Utilities.loadImage(new File("resources/imgCancel.png")));
		// Add bottom border to tool bar
		tlbRecord.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));

		// Create content panel and make it scrollable
		pnlRecord = new JPanel();
		scrRecord = new JScrollPane(pnlRecord, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrRecord.setBorder(null);
		scrRecord.setPreferredSize(new Dimension(0, 0));
		// Record initialisation size so future sizing can take it into account
		scrollBarSize = scrRecord.getMinimumSize();

		// Format into single panel
		pnlLHS = new JPanel(new BorderLayout());
		pnlLHS.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		pnlLHS.add(tlbRecord, BorderLayout.NORTH);
		pnlLHS.add(scrRecord, BorderLayout.CENTER);

		// --- RHS (Records)
		// Create toolbar
		tlbRecords = new ToolBar(getBackground(), Color.BLACK, Color.WHITE);
		tbbExpandTable = tlbRecords.addButton(""); // Position but do not initialise fully
		tlbRecords.addSeparator();
		final JLabel lblRecordsTitle = tlbRecords.addLabel(pluralType, SwingConstants.CENTER);
		lblRecordsTitle.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		tlbRecords.addSeparator();
		// Add record viewing buttons to tool bar
		tbbRefresh = tlbRecords.addButton("Refresh",
				Utilities.loadImage(new File("resources/imgRefresh.png")));
		// Add bottom border to tool bar
		tlbRecords.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));

		// Create table as content
		tblRecords = new JTable();
		tblRecords.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tblRecords.setGridColor(Color.LIGHT_GRAY);
		// Sort using default sorter
		tblRecords.setAutoCreateRowSorter(true);
		// Make scrollable
		final JScrollPane scrRecords = new JScrollPane(tblRecords);
		scrRecords.setBackground(this.getBackground());
		scrRecords.setBorder(
				BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5),
						BorderFactory.createLineBorder(Color.BLACK)));

		// Format into single panel
		pnlRHS = new JPanel(new BorderLayout());
		pnlRHS.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		pnlRHS.add(tlbRecords, BorderLayout.NORTH);
		pnlRHS.add(scrRecords, BorderLayout.CENTER);

		// --- Position LHS & RHS using SplitPane
		setLayout(new GridLayout(1, 1));
		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, pnlLHS, pnlRHS);
		splitPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		splitPane.setBackground(Color.WHITE);
		add(splitPane);

		// Initialise state of window
		expandRHS(false);
	}

	/**
	 * Attach event listeners with relevant functionality to GUI components.
	 */
	private void initEventListeners() {
		// [Table] - Listen for selection changes
		tblRecords.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				// Only trigger update once selection has stopped changing
				if (!e.getValueIsAdjusting()) {
					loadSelectedRecord();
				}
			}
		});

		// [New Record] - Listen for button presses
		tbbNew.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Update editing mode
				setEditingMode(EditingMode.NEW);
			}
		});

		// [Edit Record] - Listen for button presses
		tbbEdit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Update editing mode
				setEditingMode(EditingMode.EDIT);
			}
		});

		// [Delete Record] - Listen for button presses
		tbbDelete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (deleteRecord(loadedRecord)) {
					clearRecord();
					refresh();
				}
			}
		});

		// [Save Record] - Listen for button presses
		tbbSave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Use save method respective to editing mode
				boolean saved = false;
				switch (editingMode) {
					case NEW:
						saved = saveNewRecord();
						break;
					case EDIT:
						saved = saveEditedRecord(loadedRecord);
						break;
					default:
						// Not in a valid mode, do not change anything
						return;
				}
				if (saved) {
					setEditingMode(EditingMode.VIEW);
					refresh();
				}
			}
		});

		// [Cancel] - Listen for button presses
		tbbCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Reset current editing mode
				setEditingMode(EditingMode.VIEW);
			}
		});

		// [Expand Table] - Listen for button presses
		tbbExpandTable.addActionListener(new ActionListener() {
			// Keep track of whether panel is expanded currently
			boolean expanded = false;

			@Override
			public void actionPerformed(ActionEvent e) {
				// Toggle expanded and set expansion respectively
				expanded = !expanded;
				expandRHS(expanded);
			}
		});

		// [Refresh] - Listen for button presses
		tbbRefresh.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Manually refresh records
				refresh();
				Utilities.scaleColumns(tblRecords);
			}
		});
	}

	/**
	 * @param newEnabled Whether new record creation should be allowed
	 */
	protected void setNewEnabled(boolean newEnabled) {
		this.newEnabled = newEnabled;
	}

	/**
	 * @param editEnabled Whether editing of loaded record should be allowed
	 */
	protected void setEditEnabled(boolean editEnabled) {
		this.editEnabled = editEnabled;
	}

	/**
	 * @param deleteEnabled Whether loaded record should be deletable
	 */
	protected void setDeleteEnabled(boolean deleteEnabled) {
		this.deleteEnabled = deleteEnabled;
	}

	/**
	 * Determine and set the minimum size, which the LHS should be allowed to take.
	 */
	private void resetLHSMinimum() {
		int width = Math.max(pnlRecord.getPreferredSize().width, tlbRecord.getMinimumSize().width);
		width = Math.max(MIN_WIDTH_LHS, width);
		final Dimension dim = new Dimension(width, Integer.MIN_VALUE);
		scrRecord.setMinimumSize(Utilities.addDimensions(scrollBarSize, dim));
	}

	/**
	 * Hide/Show the LHS panel, expanding or contracting RHS respectively.
	 *
	 * @param expand Whether RHS should be expanded
	 */
	private void expandRHS(boolean expand) {
		if (expand) {
			pnlLHS.setVisible(false);
			Utilities.setButtonImage(tbbExpandTable, new File("resources/imgArrowRight.png"));
		} else {
			pnlLHS.setVisible(true);
			// Reset the divider location so that it acknowledges the minimum sizes
			splitPane.setDividerLocation(-1);
			Utilities.setButtonImage(tbbExpandTable, new File("resources/imgArrowLeft.png"));
		}
	}

	@Override
	public void setEditingMode(RecordEditor.EditingMode editingMode) {
		// Record editing mode
		this.editingMode = editingMode;
		// Handle default button behaviour
		switch (editingMode) {
			case NEW:
			case EDIT:
				tbbNew.setVisible(false);
				tbbEdit.setVisible(false);
				tbbDelete.setVisible(false);
				tbbSave.setVisible(true);
				tbbCancel.setVisible(true);
				break;
			case VIEW:
				tbbNew.setVisible(newEnabled);
				tbbEdit.setVisible(editEnabled);
				tbbDelete.setVisible(deleteEnabled);
				tbbSave.setVisible(false);
				tbbCancel.setVisible(false);
				// Load current selection
				loadSelectedRecord();
				break;
			default:
				break;
		}
		// Check if minimum size of LHS should be changed
		resetLHSMinimum();
	}

	/**
	 * Find the selected record in the table, store a reference and display the record. Do not do
	 * anything if currently editing.
	 */
	private void loadSelectedRecord() {
		// Only attempt load if in view mode, else ignore
		if (editingMode == RecordEditor.EditingMode.VIEW) {
			// Find the selected index relative to the model
			final int idxSelected = tblRecords.getSelectedRow();
			if (idxSelected >= 0) {
				// Selection valid - convert to actual position
				final int idxModel = tblRecords.getRowSorter().convertRowIndexToModel(idxSelected);
				// Find selected object and load it
				loadedRecord = model_tblRecords.getObjectAt(idxModel);
				loadRecord(loadedRecord);
				tbbEdit.setEnabled(true);
				tbbDelete.setEnabled(true);
			} else {
				// No selection
				clearRecord();
				loadedRecord = null;
				tbbEdit.setEnabled(false);
				tbbDelete.setEnabled(false);
			}
		}
	}

	/**
	 * Reload the current table using a given list. If an object exists in both lists, keep the
	 * selection.
	 *
	 * @param list List to load
	 */
	protected void refreshTable(List<T> list) {
		// Keep track of currently selected object
		final Object selected = model_tblRecords.getSelectedTableObject(tblRecords);
		// Update model
		model_tblRecords.setList(list);
		// If object still exists, select it
		model_tblRecords.setSelectedTableObject(tblRecords, selected);
	}

	/**
	 * Validate and save a new record using structures relevant to record type.
	 *
	 * @return True if record saved successfully, else false
	 */
	public abstract boolean saveNewRecord();

	/**
	 * Validate and save changes to a given record using structures relevant to record type.
	 *
	 * @return True if record saved successfully, else false
	 */
	public abstract boolean saveEditedRecord(T record);

	/**
	 * Delete record from structures relevant to record type.
	 *
	 * @return True if record deleted successfully, else false
	 */
	public abstract boolean deleteRecord(T record);

}
