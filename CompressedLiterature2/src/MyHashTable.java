import java.util.ArrayList;
import java.util.List;

import javax.jws.WebParam.Mode;

/* Authors: Nicholas Hays and Ethan Rowell
 * Date: 2/9/2016
 * Assignment 3: Compressed Literature2
 * Presented For: Dr. Chris Marriott
 */
public class MyHashTable<K, V> {
	private static double LOAD_FACTOR = .75;
	List<Bucket> buckets;
	int myCapacity;
	int myBucketCount;
	K myKey;
	V myValue;

	/**
	 * creates a hash table with capacity number of buckets (for this assignment
	 * you will use capacity 2^15 = 32768). K is the type of the keys V is the
	 * type of the values
	 */
	public MyHashTable(int capacity) {
		myCapacity = capacity;
		buckets = new ArrayList<>(capacity);
	}
	

	/**
	 * update or add the newValue to the bucket hash(searchKey). if hash(key) is
	 * full use linear probing to find the next
	 * 
	 * @param searchKey
	 * @param value
	 */
	public void put(K searchKey, V value) {
		linearProbe(hashKey(searchKey), searchKey, value);	
	}

	private void linearProbe(int keyHash, K searchKey, V value) {
		if(keyHash == buckets.size()) keyHash = 0;
		if (buckets.get(keyHash) == null) {
			myBucketCount++;
			buckets.add(keyHash, new Bucket(searchKey, value));
			return;
		} else {
			if (searchKey == buckets.get(keyHash).getKey()) {
				buckets.get(keyHash).setValue(value);
				return;
			} else {
				buckets.get(keyHash).myFlag = true;
				linearProbe(++keyHash, searchKey, value);
			}
		}
	}

	/**
	 * return a value for the specified key from the bucket hash (searchkey). if
	 * hash(searchKey) doesn’t contain the value use linear probing to find the
	 * appropriate value.
	 * 
	 * @param searchKey..
	 * @return ..
	 */
	public V get(K searchKey) {
		int hash = hashKey(searchKey);
		while(true) {
			if(buckets.get(hash).myKey == searchKey) {
				return buckets.get(hash).myvalue;
			} else {
				if(!buckets.get(hash).myFlag) {
					return null;
				}
			}
			hash++;
		}
	}

	/**
	 * return true if there is a value stored for SearchKey
	 * 
	 * @param searchKey
	 * @return ...
	 */
	public boolean containsKey(K searchKey) {
		return get(searchKey) != null;
	}

	/**
	 * a method that converts the hash table contents to a String
	 */
	public void stats() {
		// to do
	}

	/**
	 * a ​private ​method that takes a key and returns an int in the range
	 * [0...capacity]
	 * 
	 * @param key
	 *            the key...
	 * @return integer representing...
	 */
	private int hashKey(K key) {
		return key.hashCode() % myCapacity;
	}

	private class Bucket {
		K myKey;
		V myvalue;
		boolean myFlag;

		public Bucket(K key, V value) {
			key = myKey;
			value = myValue;
		}

		public K getKey() {
			return myKey;
		}

		public void setKey(K key) {
			myKey = key;
		}

		public V getValue() {
			return myValue;
		}

		public void setValue(V value) {
			value = myValue;
		}
	}

}
