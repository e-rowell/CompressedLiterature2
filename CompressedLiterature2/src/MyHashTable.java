import java.awt.RenderingHints.Key;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

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
	// K myKey;
	// V myValue;
	List<Integer> statProbe;
	private Integer myMaxProbe;
	private Integer myAvgProbe;
	private int numOfLookups;

	/**
	 * creates a hash table with capacity number of buckets (for this assignment
	 * you will use capacity 2^15 = 32768). K is the type of the keys V is the
	 * type of the values
	 */
	public MyHashTable(int capacity) {
		myCapacity = capacity;
		buckets = new ArrayList<>(capacity);
		statProbe = new ArrayList<>(capacity);
		createBuckets();
	}
	

	private void createBuckets() {
		for(int i = 0; i < myCapacity; i++) {
			buckets.add(new Bucket(null, null));
			statProbe.add(new Integer(0));
		}
	}


	/**
	 * update or add the newValue to the bucket hash(searchKey). if hash(key) is
	 * full use linear probing to find the next
	 * 
	 * @param searchKey
	 * @param value
	 */
	public void put(K searchKey, V value) {
		numOfLookups++;
		int prober = 0;
		linearProbe(hashKey(searchKey), searchKey, value, prober);	
	}

	private void linearProbe(int keyHash, K searchKey, V value, int prober) {
		
		while (true) {
			System.out.println(myBucketCount);
			
			if(keyHash == buckets.size()) keyHash = 0;
			
			if (buckets.get(keyHash).myKey == null) {
				
				myBucketCount++;
				// System.out.println(myBucketCount);
				
				buckets.get(keyHash).setKey(searchKey);
				buckets.get(keyHash).setValue(value);
				
				statProbe.add(prober, statProbe.get(prober).intValue() + 1);
				return;
			} else {
				if (searchKey.equals(buckets.get(keyHash).getKey())) {
					// System.out.println("Same key");
					buckets.get(keyHash).setValue(value);
					statProbe.add(prober, statProbe.get(prober).intValue() + 1);
					return;
				} else {
					// System.out.println("Probing");
					buckets.get(keyHash).myFlag = true;
					keyHash++;
					prober++;
					//linearProbe(++keyHash, searchKey, value, ++prober);
				}
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
		numOfLookups++;
		int prober = 0;
		int hash = hashKey(searchKey);
		while(true) {
			if(buckets.get(hash).myKey == null) {
				statProbe.add(prober, statProbe.get(prober).intValue() + 1);
				return null;
			}
			if(buckets.get(hash).myKey == searchKey) {
				statProbe.add(prober, statProbe.get(prober).intValue() + 1);
				return buckets.get(hash).myValue;
			} else {
				if(!buckets.get(hash).myFlag) {
					statProbe.add(prober, statProbe.get(prober).intValue() + 1);
					return null;
				}
			}
			hash++;
			prober++;
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
		genStats();
		System.out.println("Hash Table Stats \n");
		System.out.println("================");
		System.out.println("Number of Entries: " + myBucketCount);
		System.out.println("Number of Buckets: " + myCapacity);
		System.out.println("Histogram of Probes: ");
		System.out.print("[ ");
		for(int i = 0; i < myMaxProbe; i++) {
			if(i > 0 && i % 32 == 0) System.out.print("\n");
			if(statProbe.get(i) != null) {
			System.out.print(statProbe.get(i) + ", ");
			} else {
				System.out.print(", 0");
			}
		}
		System.out.println("]");
		System.out.println("Fill Percentage: " + myBucketCount / myCapacity);
		System.out.println("Max Linear Probe: " + myMaxProbe);
		System.out.println("Average Linear Probe: " + myAvgProbe);
	}

	private void genStats() {
		
		for(int i = 0; i < statProbe.size(); i++) {
			if(statProbe.get(i) != null) {
				myAvgProbe += statProbe.get(i);
				myMaxProbe = statProbe.get(i);
			}
		}
		myAvgProbe /= numOfLookups; 
	}


	/**
	 * a ​private ​method that takes a key and returns an int in the range
	 * [0...capacity]
	 * 
	 * @param key the key...
	 * @return integer representing...
	 */
	private int hashKey(K key) {
		return (key.hashCode() % (myCapacity / 2)) + (myCapacity / 2);
		//System.out.println(key.hashCode() % myCapacity);
		// return key.hashCode() % myCapacity;
	}

	private class Bucket implements Entry<K, V> {
		K myKey;
		V myValue;
		boolean myFlag;

		public Bucket(K key, V value) {
			key = myKey;
			value = myValue;
		}

		public K getKey() {
			return myKey;
		}
		// for rehash
		public void setKey(K key) {
			myKey = key;
		}
		// for rehash
		public V getValue() {
			return myValue;
		}

		public V setValue(V value) {
			return value = myValue;
		}
	}

	@SuppressWarnings("unchecked")
	public List<K> keySet() {
		List<K> keySet = new ArrayList<>();
		for (Object key : buckets.toArray()) {
			if (key != null) {
				keySet.add((K) key);
			}
		}
		return keySet;
	}
}
