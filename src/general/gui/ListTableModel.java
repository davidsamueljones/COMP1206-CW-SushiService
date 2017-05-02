package general.gui;

import java.util.List;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

public abstract class ListTableModel<T> extends AbstractTableModel {
	/**
	 *
	 */
	private static final long serialVersionUID = -4547803764246108910L;
	private String[] columnNames = null;
	private Class<?>[] columnClasses = null;
	private List<T> list = null;

	public void setColumnNames(String[] columnNames) {
		this.columnNames = columnNames;
		fireTableStructureChanged();
	}

	public void setColumnClasses(Class<?>[] columnClasses) {
		this.columnClasses = columnClasses;
	}

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
		fireTableDataChanged();
	}

	public T getObjectAt(int rowIndex) {
		if (rowIndex < 0 || list == null) {
			return null;
		} else {
			return list.get(rowIndex);
		}
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

	public int getItemPos(Object item) {
		return (list == null) ? -1 : list.indexOf(item);
	}

	public Object getSelectedTableObject(JTable table) {
		return getSelectedTableObject(table, this);
	}

	public static Object getSelectedTableObject(JTable table, ListTableModel<?> model) {
		final int viewRow = table.getSelectedRow();
		if (viewRow >= 0) {
			final int modelRow = table.convertRowIndexToModel(viewRow);
			return model.getObjectAt(modelRow);
		}
		return null;
	}

	public boolean setSelectedTableObject(JTable table, Object object) {
		return setSelectedTableObject(table, this, object);
	}

	public static boolean setSelectedTableObject(JTable table, ListTableModel<?> model,
			Object object) {
		final int modelRow = model.getItemPos(object);
		if (modelRow >= 0) {
			final int viewRow = table.convertRowIndexToView(modelRow);
			table.setRowSelectionInterval(viewRow, viewRow);
			return true;
		}
		return false;
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
