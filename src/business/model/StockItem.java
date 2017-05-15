package business.model;

import java.util.Objects;

/**
 * StockItem class, pairs a stock level with an item.
 *
 * @author David Jones [dsj1n15]
 *
 * @param <T> The type of item being handled
 */
public class StockItem<T> {
	private final T item;
	private final StockLevels stockLevels;

	/**
	 * Instantiate a stock item.
	 *
	 * @param item Item that stock item should represent
	 * @param stockLevels Stock levels attached to item
	 */
	public StockItem(T item, StockLevels stockLevels) {
		this.item = item;
		this.stockLevels = stockLevels;
	}

	/**
	 * @return Item represented by object
	 */
	public T getItem() {
		return item;
	}

	/**
	 * @return Stock levels held by object
	 */
	public StockLevels getStockLevels() {
		return stockLevels;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof StockItem<?>))
			return false;
		final StockItem<?> other = (StockItem<?>) obj;
		return (Objects.equals(this.item, other.item));
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		return prime + (item == null ? 0 : item.hashCode());
	}

}
