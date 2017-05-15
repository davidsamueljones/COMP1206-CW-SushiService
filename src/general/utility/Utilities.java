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
import java.util.regex.Pattern;

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

public class Utilities {
	public static final Color COLOR_GREEN = new Color(178, 255, 178);
	public static final Color COLOR_RED = new Color(255, 178, 178);
	public static final String PRICE_FORMAT = "%.2f";
	public static final DateTimeFormatter DATE_TIME_FORMAT =
			DateTimeFormatter.ofPattern("yyyy/MM/dd - HH:mm");

	/**
	 * Do not allow this class to be instantiated.
	 */
	private Utilities() {}

	public static Image loadImage(File filename) {
		try {
			return ImageIO.read(filename);
		} catch (final IOException e) {
			return null;
		}
	}

	public static void setLabelImage(JLabel label, File filename) {
		final Image image = loadImage(filename);
		if (image != null) {
			label.setIcon(new ImageIcon(image));
		}
	}

	public static void setButtonImage(JButton label, File filename) {
		final Image image = loadImage(filename);
		if (image != null) {
			label.setIcon(new ImageIcon(image));
		}
	}

	public static Dimension addDimensions(Dimension d1, Dimension d2) {
		return new Dimension(d1.width + d2.width, d1.height + d2.height);
	}

	public static void setColumnDateFormat(JTable table, int idx, DateTimeFormatter format) {
		setColumnRenderer(table, idx, new DefaultTableCellRenderer() {
			/**
			 *
			 */
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

	public static void setNaturalGUI() {
		try {
			// Get natural GUI appearance
			final String lookAndFeel = UIManager.getSystemLookAndFeelClassName();
			UIManager.setLookAndFeel(lookAndFeel);
		} catch (final Exception e) {
			System.err.println("Error setting look and feel");
		}
	}

	public static void setColumnStringFormat(JTable table, int idx, String format, int alignment) {
		setColumnRenderer(table, idx, new DefaultTableCellRenderer() {
			/**
			 *
			 */
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

	public static void setColumnValidator(JTable table, int idx, String exp) {
		setColumnRenderer(table, idx, new DefaultTableCellRenderer() {
			/**
			 *
			 */
			private static final long serialVersionUID = -4163347442107591837L;

			@Override
			public Component getTableCellRendererComponent(JTable table, Object value,
					boolean isSelected, boolean hasFocus, int row, int column) {
				final Component cell = super.getTableCellRendererComponent(table, value, isSelected,
						hasFocus, row, column);

				final String strValue = String.valueOf(value);
				final Pattern p = Pattern.compile(exp);
				if (!p.matcher(strValue).matches()) {
					cell.setForeground(Color.RED);
				}
				return cell;
			}
		});
	}

	public static void setColumnColouredBoolean(JTable table, int idx, Color colFalse,
			Color colTrue) {
		setColumnRenderer(table, idx, new DefaultTableCellRenderer() {
			/**
			 *
			 */
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

	public static void setColumnRenderer(JTable table, int idx, TableCellRenderer renderer) {
		if (idx < 0 || idx >= table.getColumnCount()) {
			throw new IllegalArgumentException("No column at index");
		}
		final TableColumn column = table.getColumnModel().getColumn(idx);
		column.setCellRenderer(renderer);
	}

	// http://stackoverflow.com/questions/17627431/auto-resizing-the-jtable-column-widths
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

	public static <T> T getCollectionItem(Collection<T> collection, T item, Class<T> clazz) {
		for (final T collectionItem : collection) {
			if (collectionItem.equals(item)) {
				return collectionItem;
			}
		}
		return null;
	}

	public static <K, V> Entry<K, V> getMapEntry(Map<K, V> map, K key, Class<K> keyClass,
			Class<V> valClass) {
		for (final Entry<K, V> entry : map.entrySet()) {
			if (entry.getKey().equals(key)) {
				return entry;
			}
		}
		return null;
	}
	
	// This still doesn't work!
	public static String getHostname() {
		try {
			return InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			return "";
		}
	}

}
