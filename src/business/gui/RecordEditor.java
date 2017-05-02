package business.gui;

public interface RecordEditor<T> {

	/**
	 * Set the editing mode, updating record editor GUI as appropriate.
	 *
	 * @param editingMode A new editing mode.
	 */
	public abstract void setEditingMode(EditingMode editingMode);

	/**
	 * Load record into record editors GUI.
	 *
	 * @param record
	 */
	public abstract void loadRecord(T record);

	/**
	 * Clear record fields of record editor.
	 */
	public abstract void clearRecord();

	/**
	 * Create a new record of type.
	 *
	 * @return New record
	 */
	public abstract T createRecord();

	/**
	 * Make updates to a record, updated fields are defined by type.
	 *
	 * @param record Record to apply updates to (in-place)
	 */
	public abstract void updateRecord(T record);

	public static enum EditingMode {
		NEW, EDIT, VIEW
	}

}
