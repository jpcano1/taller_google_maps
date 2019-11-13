package model.data_structures;

import java.util.Iterator;

/**
 * @author Robert Sedgewick
 * @param <K> Key of the Hash Table
 * @param <V> Value of the Hash Table
 * Taken from: https://algs4.cs.princeton.edu/34hash/LinearProbingHashST.java.html
 */
public class HashTable<K extends Comparable<K>, V> implements Iterable<K>
{

	public static final Integer MAX_SIZE = 127;

	private int m;

	private int n;

	private K[] keys;

	private V[] values;

	public HashTable()
	{
		this(MAX_SIZE);
	}

	@SuppressWarnings("unchecked")
	public HashTable(int pMax)
	{
		m = pMax;
		n = 0;

		keys = (K[]) new Comparable[pMax];
		values = (V[]) new Object[pMax];
	}

	public boolean contains(K pKey)
	{
		if(pKey == null) return false;
		return get(pKey) != null;
	}

	public int hash(K key)
	{
		return (key.hashCode() & 0x7fffffff)% keys.length;
	}

	public int getCapacity()
	{
		return m;
	}

	public int size()
	{
		return n;
	}

	public boolean isEmpty()
	{
		return n == 0;
	}

	public void put(K pKey, V pValue)
	{

		if(n >= m*(0.75)) rehash(m*2);

		if(pValue == null)
		{
			delete(pKey);
			return;
		}

		int i;

		for(i = hash(pKey); keys[i] != null; i = (i + 1) % m)
		{
			if(keys[i].equals(pKey))
			{
				values[i] = pValue;
				return;
			}
		}

		keys[i] = pKey;
		values[i] = pValue;
		n++;
	}

	public void rehash(int pMax)
	{
		HashTable<K, V> temporal = new HashTable<K, V>(pMax);
		for(int i = 0; i < m; i++)
		{
			if(keys[i] != null)
			{
				temporal.put(keys[i], values[i]);
			}
		}

		keys = temporal.getKeys();
		values = temporal.getValues();
		m = temporal.getCapacity();
	}

	public K[] getKeys()
	{
		return keys;
	}

	public V[] getValues()
	{
		return values;
	}

	public V get(K pKey)
	{
		if(pKey == null) return null;

		for(int i = hash(pKey); keys[i] != null; i = (i + 1) % m)
		{
			if(keys[i].equals(pKey))
			{
				return values[i];
			}
		}

		return null;
	}

	public V delete(K pKey)
	{
		if(pKey == null) return null;
		if(!contains(pKey)) return null;

		int i = hash(pKey);

		while(!pKey.equals(keys[i]))
		{
			i = (i + 1) % m;
		}

		V value = values[i];

		keys[i] = null;
		values[i] = null;

		i = (i + 1)%m;

		while(keys[i] != null)
		{
			K keyRehash = keys[i];
			V valueRehash = values[i];
			keys[i] = null;
			values[i] = null;
			n--;

			put(keyRehash, valueRehash);
			i = (i + 1)%m;
		}
		n--;

		if(n > 0 && n <= m/8) rehash(m/2);

		return value;
	}

	@Override
	public Iterator<K> iterator()
	{
		Stack<K> stack = new Stack<K>();
		for(int i = 0; i < m; i++)
		{
			if(keys[i] != null)
			{
				stack.push(keys[i]);
			}
		}

		return stack.iterator();
	}
}

