package general.utility;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

/**
 * A collection of generic utilities to be used throughout the application.
 * 
 * @author David Jones [dsj1n15]
 */
public class Utilities {
	// GUI Defaults
	public static final Color COLOR_GREEN = new Color(178, 255, 178);
	public static final Color COLOR_RED = new Color(255, 178, 178);
	public static final String PRICE_FORMAT = "%.2f";
	public static final DateTimeFormatter DATE_TIME_FORMAT =
			DateTimeFormatter.ofPattern("yyyy/MM/dd - HH:mm");

	/**
	 * Do not allow this class to be instantiated.
	 */
	private Utilities() {}

	/**
	 * Attempt to load an image from a given file.
	 * @param file File to read 
	 * @return Image if loaded, else null
	 */
	public static Image loadImage(File file) {
		try {
			return ImageIO.read(file);
		} catch(IOException e) {
			return null;
		}
	}

	/**
	 * Use an image to set the icon for a JLabel.
	 * 
	 * @param label Label for which to set icon
	 * @param file File to load image from
	 */
	public static void setLabelImage(JLabel label, File file) {
		final Image image = loadImage(file);
		if (image != null) {
			label.setIcon(new ImageIcon(image));
		}
	}

	/**
	 * Use an image to set the icon for a JButton.
	 * 
	 * @param button Button for which to set icon
	 * @param file File to load image from
	 */
	public static void setButtonImage(JButton button, File file) {
		final Image image = loadImage(file);
		if (image != null) {
			button.setIcon(new ImageIcon(image));
		}
	}

	/**
	 * Add two sets of dimensions.
	 * 
	 * @param d1 Dimension 1
	 * @param d2 Dimension 2
	 * @return New dimension equal to addition of two dimesnions
	 */
	public static Dimension addDimensions(Dimension d1, Dimension d2) {
		return new Dimension(d1.width + d2.width, d1.height + d2.height);
	}

	/**
	 * Set the look and feel of the application to be natural. This is ideal for
	 * Windows.
	 */
	public static void setNaturalGUI() {
		try {
			// Get natural GUI appearance
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			System.err.println("Error setting look and feel");
		}
	}

	/**
	 * Set a column renderer on a column of a table.
	 * 
	 * @param table Table to modify
	 * @param idx Column index to set renderer for
	 * @param renderer Renderer to set
	 */
	public static void setColumnRenderer(JTable table, int idx, TableCellRenderer renderer) {
		if (idx < 0 || idx >= table.getColumnCount()) {
			throw new IllegalArgumentException("No column at index");
		}
		final TableColumn column = table.getColumnModel().getColumn(idx);
		column.setCellRenderer(renderer);
	}
	
	/**
	 * Create and set a new column renderer for displaying a formatted date.
	 * 
	 * @param table Table to modify
	 * @param idx Column index to set renderer for
	 * @param format Date format to use
	 */
	public static void setColumnDateFormat(JTable table, int idx, DateTimeFormatter format) {
		setColumnRenderer(table, idx, new DefaultTableCellRenderer() {
			private static final long serialVersionUID = 1620238227013876455L;

			@Override
			public Component getTableCellRendererComponent(JTable table, Object value,
					boolean isSelected, boolean hasFocus, int row, int column) {
				if (value instanceof LocalDateTime) {
					final LocalDateTime date = (LocalDateTime) value;
					value = date.format(format);
				}
				return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row,
						column);
			}
		});
	}
	
	/**
	 * Create and set a new column renderer for displaying a formatted string.
	 * 
	 * @param table Table to modify
	 * @param idx Column index to set renderer for
	 * @param format String format to use
	 * @param alignment Where to position text in cell
	 */
	public static void setColumnStringFormat(JTable table, int idx, String format, int alignment) {
		setColumnRenderer(table, idx, new DefaultTableCellRenderer() {
			private static final long serialVersionUID = -4265703155878750155L;

			@Override
			public Component getTableCellRendererComponent(JTable table, Object value,
					boolean isSelected, boolean hasFocus, int row, int column) {
				setHorizontalAlignment(alignment);
				value = String.format(format, value);
				return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row,
						column);
			}
		});
	}

	/**
	 * Create and set a new column renderer for a colourer cell as opposed to a checkbox for
	 * a boolean.
	 * 
	 * @param table Table to modify
	 * @param idx Column index to set renderer for
	 * @param colFalse Colour for when boolean is false
	 * @param colTrue Colour for when boolean is true
	 */
	public static void setColumnColouredBoolean(JTable table, int idx, 
			Color colFalse, Color colTrue) {
		setColumnRenderer(table, idx, new DefaultTableCellRenderer() {
			private static final long serialVersionUID = 3758374732045770736L;

			@Override
			public Component getTableCellRendererComponent(JTable table, Object value,
					boolean isSelected, boolean hasFocus, int row, int column) {
				final Component cell = super.getTableCellRendererComponent(table, "", isSelected,
						hasFocus, row, column);
				if (value instanceof Boolean) {
					final boolean isTrue = (Boolean) value;
					if (isTrue) {
						cell.setBackground(colTrue);
					} else {
						cell.setBackground(colFalse);
					}
				}
				return cell;
			}
		});

	}

	/**
	 * Automatic scaler for columns in a JTable. Code sourced from:
	 * http://stackoverflow.com/questions/17627431/auto-resizing-the-jtable-column-widths
	 * 
	 * @param table Table to scale
	 */
	public static void scaleColumns(JTable table) {
		final TableColumnModel columnModel = table.getColumnModel();
		for (int column = 0; column < table.getColumnCount(); column++) {
			int width = 15; // Min width
			for (int row = 0; row < table.getRowCount(); row++) {
				final TableCellRenderer renderer = table.getCellRenderer(row, column);
				final Component comp = table.prepareRenderer(renderer, row, column);
				width = Math.max(comp.getPreferredSize().width + 1, width);
			}
			if (width > 300)
				width = 300;
			columnModel.getColumn(column).setPreferredWidth(width);
		}
	}

	/**
	 * Find the actual item in a collection. This can be used to get an entry where the 
	 * items equality criteria is equal but all fields may not be.
	 * 
	 * @param collection Collection to search
	 * @param item Item to search for
	 * @param clazz Class of item
	 * @return Item in collection
	 */
	public static <T> T getCollectionItem(Collection<T> collection, T item, Class<T> clazz) {
		for (final T collectionItem : collection) {
			if (collectionItem.equals(item)) {
				return collectionItem;
			}
		}
		return null;
	}

	/**
	 * Find the actual map entry for a key. This can be used to get an entry where the 
	 * keys equality criteria is equal but all fields may not be.
	 * 
	 * @param map Map to search
	 * @param key Key to search for
	 * @param keyClass Class of key for return
	 * @param valClass Class of value for return
	 * @return Key and value entry using given types
	 */
	public static <K, V> Entry<K, V> getMapEntry(Map<K, V> map, K key, Class<K> keyClass,
			Class<V> valClass) {
		for (final Entry<K, V> entry : map.entrySet()) {
			if (entry.getKey().equals(key)) {
				return entry;
			}
		}
		return null;
	}

	/**
	 * @return Hostname of current computer
	 */
	public static String getHostname() {
		try {
			return InetAddress.getLocalHost().getHostName();
		} catch(UnknownHostException e) {
			return "";
		}
	}

}
