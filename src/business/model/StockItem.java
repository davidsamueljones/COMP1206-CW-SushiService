package business.model;

import java.util.Objects;

public class StockItem<T> {
	private T item;
	private StockLevels stockLevels;

	public StockItem(T item, StockLevels stockLevels) {
		this.setIngredient(item);
		this.setStockLevels(stockLevels);
	}

	public T getItem() {
		return item;
	}

	public void setIngredient(T item) {
		this.item = item;
	}

	public StockLevels getStockLevels() {
		return stockLevels;
	}

	public void setStockLevels(StockLevels stockLevels) {
		this.stockLevels = stockLevels;
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
