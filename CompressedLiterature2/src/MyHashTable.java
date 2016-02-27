import java.util.ArrayList;
import java.util.List;

/**
 * Hash Table based implementation which provides most of the map operations. This implementation
 * provides constant time performance for basic operations (get and put) using linear probing to 
 * find proper bucket values. An instance of HashMap has two parameters that affect its performance:
 * initial capacity and load factor. The capacity is the number of buckets in the hash table, 
 * and the initial capacity is simply the capacity at the time the hash table is created.
 * 
 * @author Nicholas Hays and Ethan Rowell
 * @version 2/27/2016
 * Assignment 4: Compressed Literature2
 * Presented For: Dr. Chris Marriott
 */
public class MyHashTable<K, V> {
	List<Bucket> buckets;
	int myCapacity;
	int myBucketCount;
	List<Integer> statProbe;
	private int myMaxProbe;
	private double myAvgProbe;
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
	
	/**
	 * intializes a list of empty buckets. 
	 */
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

	/**
	 * Hashes the search key and performs linear probing until either a matching
	 * key entry is found or an empty bucket is located to replace or 
	 * insert the value respectively.  
	 * 
	 * @param keyHash the hashed key to look in the data structure.
	 * @param searchKey the key to match.
	 * @param value the value to replace or add to the hash table.
	 * @param prober keeps track of the number of probes. 
	 */
	private void linearProbe(int keyHash, K searchKey, V value, int prober) {
		Bucket tempBucket;
		while (true) {
			
			if(keyHash == buckets.size()) keyHash = 0;
			tempBucket = buckets.get(keyHash);
			if (tempBucket.myKey == null) {
				
				myBucketCount++;
				tempBucket.setKey(searchKey);
				tempBucket.setValue(value);
				statProbe.set(prober, statProbe.get(prober).intValue() + 1);
				break;
			} else {
				if (searchKey.equals(tempBucket.getKey())) {
					tempBucket.setValue(value);
					statProbe.set(prober, statProbe.get(prober).intValue() + 1);
					break;
				} else {
					tempBucket.myFlag = true;
					keyHash++;
					prober++;
				}
			}
		}
		if (prober > myMaxProbe) myMaxProbe = prober;
		return;
	}

	/**
	 * return a value for the specified key from the bucket hash (searchkey). if
	 * hash(searchKey) doesn’t contain the value use linear probing to find the
	 * appropriate value.
	 * 
	 * @param searchKey the key to search for in the data structure. 
	 * @return the value associated with the search key. 
	 */
	public V get(K searchKey) {
		int prober = 0;
		int hash = hashKey(searchKey);
		Bucket tempBucket;
		while(true) {
			if(hash == buckets.size()) hash = 0;
			tempBucket = buckets.get(hash);
			if(tempBucket.myKey == null) {
				return null;
			}
			if(tempBucket.myKey.equals(searchKey)) {
				return tempBucket.myValue;
			} else {
				if(!tempBucket.myFlag) {
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
	 * @param searchKey the key to search for in the data structure. 
	 * @return the truth value of the search key. 
	 */
	public boolean containsKey(K searchKey) {
		return get(searchKey) != null;
	}

	/**
	 * a method that converts the hash table contents to a String
	 */
	public void stats() {
		genStats();
		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append("Hash Table Stats\n");
		sBuilder.append("================\n");
		sBuilder.append(String.format("Number of Entries: %d \n", myBucketCount));
		sBuilder.append(String.format("Number of Buckets: %d \n", myCapacity));
		sBuilder.append("Histogram of Probes: \n");
		sBuilder.append("[");

		for (int i = 0; i <= myMaxProbe; i++) {
			if(i > 0 && i % 32 == 0) sBuilder.append("\n");
			if(statProbe.get(i) != null) {
			sBuilder.append(statProbe.get(i) + ", ");
			} else {
				sBuilder.append(", 0");
			}
		}
		
		sBuilder.delete(sBuilder.length() - 2, sBuilder.length());
		sBuilder.append("]\n");
		sBuilder.append(String.format("Fill Percentage:  %.6f%% \n", (myBucketCount / (double) myCapacity * 100 )));
		sBuilder.append(String.format("Max Linear Probe: %d \n", myMaxProbe));
		sBuilder.append(String.format("Average Linear Probe: %.6f", myAvgProbe));
		
		System.out.println(sBuilder.toString());
	}
	/**
	 * Monitors the runtime stats on our data structure (i.e average probe length). 
	 */
	private void genStats() {
		
		for(int i = 0; i < statProbe.size(); i++) {
			if(statProbe.get(i) != null) {
				myAvgProbe += statProbe.get(i) * i;
			}
		}
		myAvgProbe /= numOfLookups; 
	}


	/**
	 * a ​private ​method that takes a key and returns an int in the range
	 * [0...capacity]
	 * 
	 * @param key the key to search for in the data structure. 
	 * @return integer representing the key in the range between [0...capacity].
	 */
	private int hashKey(K key) {
		return (key.hashCode() % (myCapacity / 2)) + (myCapacity / 2);
	}
	/**
	 * Container that store key-value pairs for each entry in the hash table. 
	 * 
	 * @author Nicholas Hays & Ethan Rowell
	 */
	private class Bucket {
		K myKey;
		V myValue;
		boolean myFlag;

		public Bucket(K key, V value) {
			myKey = key;
			myValue = value;
		}
		/**
		 * Gets the key from this bucket. 
		 * 
		 * @return this buckets key. 
		 */
		public K getKey() {
			return myKey;
		}
		/**
		 * Sets this buckets key. 
		 * 
		 * @param key the 
		 */
		public void setKey(K key) {
			myKey = key;
		}
		/**
		 * for rehash purposes only. 
		 * never used locally. 
		 */
		public V getValue() {
			return myValue;
		}
		/**
		 * sets the value of the bucket's key. 
		 */
		public void setValue(V value) {
			myValue = value;
		}
	}
	
	/**
	 * List of hash keys in the data set.
	 * 
	 * @return a list of non null keys contained within the hash table. 
	 */
	@SuppressWarnings("unchecked")
	public List<K> keySet() {
		List<K> keySet = new ArrayList<>();
		for (Object key : buckets.toArray()) {
			if (((Bucket) key).myKey != null) {
				keySet.add(((Bucket) key).myKey);
			}
		}
		return keySet;
	}
}
