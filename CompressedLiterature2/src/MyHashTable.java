import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

/* Authors: Nicholas Hays and Ethan Rowell
 * Date: 2/9/2016
 * Assignment 3: Compressed Literature2
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
	 * @param searchKey..
	 * @return ..
	 */
	public V get(K searchKey) {
		// numOfLookups++;
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
			myKey = key;
			myValue = value;
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
			return myValue = value;
		}
	}

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
