import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Class ConcurrentLinkedHashMap that inherits LinkedHashMap to provide basic thread-safe functionality.
 * 
 * All inherited methods which are not thread safe are wrapped so that they are thread safe 
 * whilst utilising a central locking object. Note that implementation of LinkedHashClass has not been
 * studied thoroughly so some methods may lock even when not required for thread safe behaviour.
 * 
 * Use of iterators or other method returns may not be thread safe and could affect the HashMap leading
 * to unpredictable behaviour.
 * 
 * @param <K> The type of keys maintained by this map
 * @param <V> The type of mapped values
 * 
 * Sushi Service - COMP1206 Coursework
 * @author David Jones [dsj1n15]
 */
public class ConcurrentLinkedHashMap<K, V> extends LinkedHashMap<K, V> {
	private static final long serialVersionUID = -8070400284083449990L;

	@Override
	public synchronized boolean removeEldestEntry(Map.Entry<K,V> eldest) {
		return super.removeEldestEntry(eldest);
	}

	@Override
	public synchronized int size() {
		return super.size();
	}

	@Override
	public synchronized boolean isEmpty() {
		return super.isEmpty();
	}

	@Override
	public synchronized boolean containsValue(Object value) {
		return super.containsValue(value);
	}

	@Override
	public synchronized boolean containsKey(Object key) {
		return super.containsKey(key);
	}

	@Override
	public synchronized V get(Object key) {
		return super.get(key);
	}

	@Override
	public synchronized V put(K key, V value) {
		return super.put(key, value);
	}

	@Override
	public synchronized V replace(K key, V value) {
		return super.put(key, value);
	}

	@Override
	public synchronized V remove(Object key) {
		return super.remove(key);
	}

	@Override
	public synchronized void putAll(Map<? extends K,? extends V> m) {
		super.putAll(m);
	}

	@Override
	public synchronized void clear() {
		super.clear();
	}

	@Override
	public synchronized Set<K> keySet() {
		return super.keySet();
	}

	@Override
	public synchronized Collection<V> values() {
		return super.values();
	}

	@Override
	public synchronized Set<Map.Entry<K,V>> entrySet() {
		return super.entrySet();
	}

	@Override
	public synchronized boolean equals(Object o) {
		return super.equals(o);
	}

	@Override
	public synchronized int hashCode() {
		return super.hashCode();
	}

	@Override
	public synchronized String toString() {
		return super.toString();
	}

	@Override
	public synchronized V compute(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
		return super.compute(key, remappingFunction);
	}

	@Override
	public synchronized V computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction) {
		return super.computeIfAbsent(key, mappingFunction);
	}

	@Override
	public synchronized V computeIfPresent(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
		return super.computeIfPresent(key, remappingFunction);
	}

	@Override
	public synchronized void forEach(BiConsumer<? super K, ? super V> action) {
		super.forEach(action);
	}

	@Override
	public synchronized V getOrDefault(Object key, V defaultValue) {
		return super.getOrDefault(key, defaultValue);
	}

	@Override
	public synchronized V merge(K key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
		return super.merge(key, value, remappingFunction);
	}

	@Override
	public synchronized V putIfAbsent(K key, V value) {
		return super.putIfAbsent(key, value);
	}

	@Override
	public synchronized boolean remove(Object key, Object value) {
		return super.remove(key, value);
	}

	@Override
	public synchronized boolean replace(K key, V oldValue, V newValue) {
		return super.replace(key, oldValue, newValue);
	}

	@Override
	public synchronized void replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {
		super.replaceAll(function);
	}

}
