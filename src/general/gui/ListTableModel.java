package general.gui;

import java.util.List;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

/**
 * Abstract model class for displaying a list of objects. Each row is represented as a single
 * object. Implementations of this model dictate how these objects are displayed. Column classes can
 * be explicitly set to avoid requiring table structure remake if a new record is added to a blank
 * model.
 * 
 * @author David Jones [dsj1n15]
 *
 * @param <T> Type of list model is serving
 */
public abstract class ListTableModel<T> extends AbstractTableModel {
	private static final long serialVersionUID = -4547803764246108910L;
	private String[] columnNames = null;
	private Class<?>[] columnClasses = null;
	private List<T> list = null;

	/**
	 * Change the columns, automatically triggering table update.
	 * 
	 * @param columnNames The name for each column as an array
	 */
	public void setColumnNames(String[] columnNames) {
		this.columnNames = columnNames;
		fireTableStructureChanged();
	}

	/**
	 * @param columnClasses The class for each column as an array
	 */
	public void setColumnClasses(Class<?>[] columnClasses) {
		this.columnClasses = columnClasses;
	}

	/**
	 * @return The list being displayed by the model
	 */
	public List<T> getList() {
		return list;
	}

	/**
	 * Change the list being displayed, automatically displays table update.
	 * 
	 * @param list A list for model to display
	 */
	public void setList(List<T> list) {
		this.list = list;
		fireTableDataChanged();
	}

	/**
	 * Get the object being displayed at a specific row index.
	 * 
	 * @param rowIndex Row index to get object for (in model)
	 * @return Object at row index
	 */
	public T getObjectAt(int rowIndex) {
		if (rowIndex < 0 || list == null) {
			return null;
		} else {
			return list.get(rowIndex);
		}
	}

	/**
	 * Find the position of an object in the model.
	 * 
	 * @param object Object to find
	 * @return Index of object in model, -1 of object not found
	 */
	public int getObjectPos(Object object) {
		return (list == null) ? -1 : list.indexOf(object);
	}

	/**
	 * Return the selected object from the given table using this model.
	 * 
	 * @param table Table to get object selection from
	 * @return Object that is selected
	 */
	public T getSelectedTableObject(JTable table) {
		final int viewRow = table.getSelectedRow();
		if (viewRow >= 0) {
			final int modelRow = table.convertRowIndexToModel(viewRow);
			return getObjectAt(modelRow);
		}
		return null;
	}

	/**
	 * Select an object in the given table using this model.
	 * 
	 * @param table Table to select object in
	 * @param object Object to select
	 * @return Whether object was selected
	 */
	public boolean setSelectedTableObject(JTable table, Object object) {
		final int modelRow = getObjectPos(object);
		if (modelRow >= 0) {
			final int viewRow = table.convertRowIndexToView(modelRow);
			table.setRowSelectionInterval(viewRow, viewRow);
			return true;
		}
		return false;
	}

	@Override
	public int getRowCount() {
		return (list == null) ? 0 : list.size();
	}

	@Override
	public int getColumnCount() {
		return (columnNames == null) ? 0 : columnNames.length;
	}

	@Override
	public String getColumnName(int index) {
		return columnNames[index];
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		if (columnClasses == null) {
			return super.getColumnClass(columnIndex);
		}
		if (columnIndex < 0 || columnIndex > columnClasses.length) {
			return Object.class;
		}
		return columnClasses[columnIndex];
	}

}
