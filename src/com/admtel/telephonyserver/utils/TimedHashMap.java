package com.admtel.telephonyserver.utils;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public class TimedHashMap<K,V> implements Map<K,V> {

	class TimedEntry<T,E> implements Entry<T,E> {
		T key;
		E value;

		
		TimerTask timeoutTask = new TimerTask() {
			public void run() {
				synchronized ( TimedHashMap.this ) {
					if ( containsKey(key) &&
							baseMap.get( key )==TimedEntry.this) {
						remove( key );
					}
				}
			}
		};

		public TimedEntry(T key, E value) {
			super();
			this.key = key;
			this.value = value;
			tableTimer.schedule(timeoutTask, timeout);
		}

		public T getKey() {
			return key;
		}

		@Override
		public E setValue(E value) {
			E oldValue = this.value;
			this.value = value; 
			return oldValue;
		}

		public E getValue() {
			return value;
		}
	}

	private int timeout;

	private Timer tableTimer = new Timer();
	private HashMap<K, TimedEntry<K,V>> baseMap = new  HashMap<K, TimedEntry<K,V>>();

	/** crée une table de timedEntry qui se supprime si elle n'ont pas été modifiée au bout de timout secondes
	 * @param timeout temps au bout duquels les entrées non modifiées sont supprimées
	 */
	public TimedHashMap(int timeout) {
		super();
		this.timeout = timeout;
	}

	@Override
	public void clear() {
		baseMap.clear();
	}

	@Override
	public Set<Entry<K, V>> entrySet() {
		return new HashSet<Entry<K,V>>(baseMap.values());
	}

	@Override
	public boolean containsKey(Object key) {
		return baseMap.containsKey(key);
	}

	/**attention, ne doit pas être utilisée car n'a pas de sens !	 */
	@Override
	public boolean containsValue(Object value) {
		throw new RuntimeException( "!!! NOT IMPLEMENTED !!!");
	}

	@SuppressWarnings("unchecked")
	@Override
	public V get( Object key) {
		if (  ! baseMap.containsKey( (K) key ) ) {
			return null;
		}
		return baseMap.get((K) key).getValue();
	}

	@Override
	public boolean isEmpty() {
		return baseMap.isEmpty();
	}

	@Override
	public Set<K> keySet() {
		return baseMap.keySet();
	}

	/** pour ajouter une entrée avec un timer, possibilité de commenter les ajouts	 */
	@Override
	public V put(K key, V value) {
		V oldVal = null;
		//Log.log("[TimeHashMap] adding "+key, Log.VV);
		if ( containsKey(key) ) {
			oldVal = get( key );
			//Log.log("[TimeHashMap] "+key+" already exists : overwriting", Log.VV);
		}
		baseMap.put(key, new TimedEntry<K,V>( key, value) );
		return oldVal;
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		for ( Entry<? extends K, ? extends V> e : m.entrySet() ) {
			put( e.getKey(), e.getValue() );
		}
	}

	@Override
	public synchronized V remove(Object key) {
		baseMap.remove(key);
		return null;
	}

	@Override
	public int size() {
		return baseMap.size();
	}

	@Override
	public Collection<V> values() {
		Set<V> valuesSet = new HashSet<V>();
		for ( Entry<K,V> e : entrySet() ) {
			valuesSet.add(e.getValue());
		}
		return valuesSet;
	}

}