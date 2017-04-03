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
	private final Object lock;

	/**
	 * Instantiates a new ConcurrentLinkedHashMap, using self as synchronisation lock.
	 */
	public ConcurrentLinkedHashMap() {
		this.lock = this;
	}

	/**
	 * Instantiates a new ConcurrentLinkedHashMap using a given object as a synchronisation lock.
	 * @param lock Object to use for locking
	 */
	public ConcurrentLinkedHashMap(Object lock) {
		this.lock = lock;		
	}

	/**
	 * @return The Oject being used for locking
	 */
	public Object getLock() {
		return lock;
	}
	
	@Override
	public boolean removeEldestEntry(Map.Entry<K,V> eldest) {
		synchronized (lock) {
			return super.removeEldestEntry(eldest);
		}
	}

	@Override
	public int size() {
		synchronized (lock) {
			return super.size();
		}
	}

	@Override
	public boolean isEmpty() {
		synchronized (lock) {
			return super.isEmpty();
		}
	}

	@Override
	public boolean containsValue(Object value) {
		synchronized (lock) {
			return super.containsValue(value);
		}
	}

	@Override
	public boolean containsKey(Object key) {
		synchronized (lock) {
			return super.containsKey(key);
		}
	}

	@Override
	public V get(Object key) {
		synchronized (lock) {
			return super.get(key);
		}
	}

	@Override
	public V put(K key, V value) {
		synchronized (lock) {
			return super.put(key, value);
		}
	}

	@Override
	public V replace(K key, V value) {
		synchronized (lock) {
			return super.put(key, value);
		}
	}

	@Override
	public V remove(Object key) {
		synchronized (lock) {
			return super.remove(key);
		}
	}

	@Override
	public void putAll(Map<? extends K,? extends V> m) {
		synchronized (lock) {
			super.putAll(m);
		}
	}

	@Override
	public void clear() {
		synchronized (lock) {
			super.clear();
		}
	}

	@Override
	public Set<K> keySet() {
		synchronized (lock) {
			return super.keySet();
		}
	}

	@Override
	public Collection<V> values() {
		synchronized (lock) {
			return super.values();
		}
	}

	@Override
	public Set<Map.Entry<K,V>> entrySet() {
		synchronized (lock) {
			return super.entrySet();
		}
	}

	@Override
	public boolean equals(Object o) {
		synchronized (lock) {
			return super.equals(o);
		}
	}

	@Override
	public int hashCode() {
		synchronized (lock) {
			return super.hashCode();
		}
	}

	@Override
	public String toString() {
		synchronized (lock) {
			return super.toString();
		}
	}

	@Override
	public V compute(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
		synchronized (lock) {
		return super.compute(key, remappingFunction);
		}
	}

	@Override
	public V computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction) {
		synchronized (lock) {
		return super.computeIfAbsent(key, mappingFunction);
		}
	}

	@Override
	public V computeIfPresent(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
		synchronized (lock) {
		return super.computeIfPresent(key, remappingFunction);
		}
	}

	@Override
	public void forEach(BiConsumer<? super K, ? super V> action) {
		synchronized (lock) {
		super.forEach(action);
		}
	}

	@Override
	public V getOrDefault(Object key, V defaultValue) {
		synchronized (lock) {
		return super.getOrDefault(key, defaultValue);
		}
	}

	@Override
	public V merge(K key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
		synchronized (lock) {
		return super.merge(key, value, remappingFunction);
		}
	}

	@Override
	public V putIfAbsent(K key, V value) {
		synchronized (lock) {
		return super.putIfAbsent(key, value);
		}
	}

	@Override
	public boolean remove(Object key, Object value) {
		synchronized (lock) {
		return super.remove(key, value);
		}
	}

	@Override
	public boolean replace(K key, V oldValue, V newValue) {
		synchronized (lock) {
		return super.replace(key, oldValue, newValue);
		}
	}

	@Override
	public void replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {
		synchronized (lock) {
		super.replaceAll(function);
		}
	}
	
}
