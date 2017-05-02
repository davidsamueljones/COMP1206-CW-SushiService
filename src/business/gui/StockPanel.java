package business.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.text.DecimalFormat;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

import business.model.StockLevels;

public class StockPanel extends JPanel implements RecordEditor<StockLevels> {
	/**
	 *
	 */
	private static final long serialVersionUID = 4953583908325886546L;
	// Record objects
	private final JPanel pnlStockLevels;
	private final JTextField txtReserved;
	private final JTextField txtRestocking;
	private final JTextField txtTotalStock;
	private final JTextField txtAvailableStock;
	private final JSpinner nudRestockLevel;
	private final JLabel lblStockable;
	private final ButtonGroup bgStockable;
	private final JRadioButton radStockableYes;
	private final JRadioButton radStockableNo;

	/**
	 * Create the panel.
	 */
	public StockPanel() {
		// Set default panel border
		setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
				"Stock Levels", TitledBorder.LEADING, TitledBorder.TOP));

		// [Content Panel] - Set layout as grid bag
		final GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] {0, 0, 0};
		gridBagLayout.rowHeights = new int[] {0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[] {0.0, 0.0, 1.0};
		gridBagLayout.rowWeights = new double[] {0.0, 0.0, 0.0, 1.0};
		setLayout(gridBagLayout);

		// [Content Panel] <- 'Stock Levels' Panel
		pnlStockLevels = new JPanel();
		final GridBagConstraints gbc_pnlStockLevels = new GridBagConstraints();
		gbc_pnlStockLevels.gridwidth = 3;
		gbc_pnlStockLevels.insets = new Insets(5, 5, 5, 5);
		gbc_pnlStockLevels.fill = GridBagConstraints.BOTH;
		gbc_pnlStockLevels.gridx = 0;
		gbc_pnlStockLevels.gridy = 0;
		add(pnlStockLevels, gbc_pnlStockLevels);
		final GridBagLayout gbl_pnlStockLevels = new GridBagLayout();
		gbl_pnlStockLevels.columnWidths = new int[] {0, 0, 0, 0, 0};
		gbl_pnlStockLevels.rowHeights = new int[] {0, 0, 0};
		gbl_pnlStockLevels.columnWeights = new double[] {1.0, 1.0, 1.0, 1.0, Double.MIN_VALUE};
		gbl_pnlStockLevels.rowWeights = new double[] {0.0, 0.0, Double.MIN_VALUE};
		pnlStockLevels.setLayout(gbl_pnlStockLevels);

		// [Stock Levels Panel] <- 'Reserved' TextField
		txtReserved = new JTextField();
		final GridBagConstraints gbc_txtReserved = new GridBagConstraints();
		gbc_txtReserved.insets = new Insets(0, 0, 0, 5);
		gbc_txtReserved.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtReserved.gridx = 0;
		gbc_txtReserved.gridy = 0;
		pnlStockLevels.add(txtReserved, gbc_txtReserved);
		txtReserved.setColumns(4);
		txtReserved.setHorizontalAlignment(SwingConstants.CENTER);
		txtReserved.setEnabled(false);
		// [Stock Levels Panel] <- 'Reserved' Label
		final JLabel lblRes = new JLabel("RES");
		final GridBagConstraints gbc_lblRes = new GridBagConstraints();
		gbc_lblRes.insets = new Insets(0, 0, 0, 5);
		gbc_lblRes.gridx = 0;
		gbc_lblRes.gridy = 1;
		pnlStockLevels.add(lblRes, gbc_lblRes);

		// [Stock Levels Panel] <- 'Restocking' TextField
		txtRestocking = new JTextField();
		final GridBagConstraints gbc_txtRestocking = new GridBagConstraints();
		gbc_txtRestocking.insets = new Insets(0, 0, 0, 5);
		gbc_txtRestocking.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtRestocking.gridx = 1;
		gbc_txtRestocking.gridy = 0;
		pnlStockLevels.add(txtRestocking, gbc_txtRestocking);
		txtRestocking.setColumns(4);
		txtRestocking.setHorizontalAlignment(SwingConstants.CENTER);
		txtRestocking.setEnabled(false);
		// [Stock Levels Panel] <- 'Restocking' Label
		final JLabel lblRst = new JLabel("RST");
		final GridBagConstraints gbc_lblRst = new GridBagConstraints();
		gbc_lblRst.insets = new Insets(0, 0, 0, 5);
		gbc_lblRst.gridx = 1;
		gbc_lblRst.gridy = 1;
		pnlStockLevels.add(lblRst, gbc_lblRst);

		// [Stock Levels Panel] <- 'Total Stock' TextField
		txtTotalStock = new JTextField();
		final GridBagConstraints gbc_txtTotalStock = new GridBagConstraints();
		gbc_txtTotalStock.insets = new Insets(0, 0, 0, 5);
		gbc_txtTotalStock.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtTotalStock.gridx = 2;
		gbc_txtTotalStock.gridy = 0;
		pnlStockLevels.add(txtTotalStock, gbc_txtTotalStock);
		txtTotalStock.setColumns(4);
		txtTotalStock.setHorizontalAlignment(SwingConstants.CENTER);
		txtTotalStock.setEnabled(false);
		// [Stock Levels Panel] <- 'Total Stock' Label
		final JLabel lblTs = new JLabel("TS");
		final GridBagConstraints gbc_lblTs = new GridBagConstraints();
		gbc_lblTs.insets = new Insets(0, 0, 0, 5);
		gbc_lblTs.gridx = 2;
		gbc_lblTs.gridy = 1;
		pnlStockLevels.add(lblTs, gbc_lblTs);

		// [Stock Levels Panel] <- 'Available Stock' TextField
		txtAvailableStock = new JTextField();
		final GridBagConstraints gbc_txtAvailableStock = new GridBagConstraints();
		gbc_txtAvailableStock.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtAvailableStock.gridx = 3;
		gbc_txtAvailableStock.gridy = 0;
		pnlStockLevels.add(txtAvailableStock, gbc_txtAvailableStock);
		txtAvailableStock.setColumns(4);
		txtAvailableStock.setHorizontalAlignment(SwingConstants.CENTER);
		txtAvailableStock.setEnabled(false);
		// [Stock Levels Panel] <- 'Available Stock' Label
		final JLabel lblAs = new JLabel("AS");
		final GridBagConstraints gbc_lblAs = new GridBagConstraints();
		gbc_lblAs.gridx = 3;
		gbc_lblAs.gridy = 1;
		pnlStockLevels.add(lblAs, gbc_lblAs);

		// [Content Panel] <- 'Restock Level' Label
		final JLabel lblRestockLevel = new JLabel("Restock Level:");
		final GridBagConstraints gbc_lblRestockLevel = new GridBagConstraints();
		gbc_lblRestockLevel.anchor = GridBagConstraints.WEST;
		gbc_lblRestockLevel.insets = new Insets(5, 5, 5, 5);
		gbc_lblRestockLevel.gridx = 0;
		gbc_lblRestockLevel.gridy = 1;
		add(lblRestockLevel, gbc_lblRestockLevel);
		// [Content Panel] <- 'Restock Level' Spinner
		nudRestockLevel = new JSpinner(new SpinnerNumberModel(0, 0, 10000, 1));
		final GridBagConstraints gbc_nudRestockLevel = new GridBagConstraints();
		gbc_nudRestockLevel.gridwidth = 2;
		gbc_nudRestockLevel.fill = GridBagConstraints.HORIZONTAL;
		gbc_nudRestockLevel.insets = new Insets(5, 0, 5, 5);
		gbc_nudRestockLevel.gridx = 1;
		gbc_nudRestockLevel.gridy = 1;
		add(nudRestockLevel, gbc_nudRestockLevel);

		lblStockable = new JLabel("Stockable:");
		final GridBagConstraints gbc_lblStockable = new GridBagConstraints();
		gbc_lblStockable.anchor = GridBagConstraints.EAST;
		gbc_lblStockable.insets = new Insets(0, 0, 5, 5);
		gbc_lblStockable.gridx = 0;
		gbc_lblStockable.gridy = 2;
		add(lblStockable, gbc_lblStockable);

		radStockableYes = new JRadioButton("Yes");
		final GridBagConstraints gbc_radStockableYes = new GridBagConstraints();
		gbc_radStockableYes.insets = new Insets(0, 0, 5, 5);
		gbc_radStockableYes.gridx = 1;
		gbc_radStockableYes.gridy = 2;
		add(radStockableYes, gbc_radStockableYes);

		radStockableNo = new JRadioButton("No");
		final GridBagConstraints gbc_radStockableNo = new GridBagConstraints();
		gbc_radStockableNo.anchor = GridBagConstraints.WEST;
		gbc_radStockableNo.insets = new Insets(0, 0, 5, 0);
		gbc_radStockableNo.gridx = 2;
		gbc_radStockableNo.gridy = 2;
		add(radStockableNo, gbc_radStockableNo);

		bgStockable = new ButtonGroup();
		bgStockable.add(radStockableYes);
		bgStockable.add(radStockableNo);

	}

	/**
	 * Show/Hide panel with non-editable stock levels.
	 *
	 * @param show True if stock levels should be visible, else false
	 */
	private void showStockLevels(boolean show) {
		pnlStockLevels.setVisible(show);
	}

	@Override
	public void loadRecord(StockLevels record) {
		final DecimalFormat df = new DecimalFormat("0.##");
		txtReserved.setText(df.format(record.getReserved()));
		txtRestocking.setText(df.format(record.getRestocking()));
		txtTotalStock.setText(df.format(record.getStock()));
		txtAvailableStock.setText(df.format(record.getStockAvailable()));
		nudRestockLevel.setValue(record.getRestockLevel());
		if (record.isStockable()) {
			radStockableYes.setSelected(true);
		} else {
			radStockableNo.setSelected(true);
		}
	}

	@Override
	public void clearRecord() {
		txtReserved.setText(null);
		txtRestocking.setText(null);
		txtTotalStock.setText(null);
		txtAvailableStock.setText(null);
		nudRestockLevel.setValue(0);
		bgStockable.clearSelection();
	}

	@Override
	public StockLevels createRecord() {
		// Create new record structure using final fields
		final StockLevels record = new StockLevels();
		// Update non-final fields
		updateRecord(record);
		return record;
	}

	@Override
	public void updateRecord(StockLevels record) {
		record.setRestockLevel((Integer) nudRestockLevel.getValue());
		record.setStockable(radStockableYes.isSelected());
	}

	@Override
	public void setEditingMode(RecordEditor.EditingMode editingMode) {
		switch (editingMode) {
			case NEW:
			case EDIT:
				showStockLevels(false);
				nudRestockLevel.setEnabled(true);
				radStockableYes.setEnabled(true);
				radStockableNo.setEnabled(true);
				break;
			case VIEW:
				showStockLevels(true);
				nudRestockLevel.setEnabled(false);
				radStockableYes.setEnabled(false);
				radStockableNo.setEnabled(false);
				break;
			default:
				break;
		}
	}

}
