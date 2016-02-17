/* Authors: Nicholas Hays and Ethan Rowell
 * Date: 2/9/2016
 * Assignment 3: Compressed Literature2
 * Presented For: Dr. Chris Marriott
 */
public class MyHashTable<K, V> {
	/**
	 * creates a hash table with capacity number of buckets (for this assignment you
	 * will use capacity  2^15 = 32768). K is the type of the keys 
	 *  V is the type of the values
	 */
	public MyHashTable<K, V>(int capacity) {
		// to do
	}
	/**
	 * update or add the newValue to the bucket hash(searchKey).
	 * if hash(key) is full use linear probing to find the next
	 * @param searchKey
	 * @param value
	 */
	public void put(K searchKey, V value) {
		// to do
	}
	/**
	 * return a value for the specified key from the bucket hash (searchkey).
	 * if hash(searchKey) doesn’t contain the value use linear probing to find
	 * the appropriate value.
	 * @param searchKey..
	 * @return ..
	 */
	public V get(K searchKey) {
		// to do
	}
	
	/**
	 * return true if there is a value stored for SearchKey
	 * @param searchKey ...
	 * @return ...
	 */
	public boolean containsKey(K searchKey) {
		// to do
	}
	
	/**
	 * a method that converts the hash table contents to a String
	 */
	public void stats() {
		// to do
	}
	/**
	 *  a ​private ​method that takes a key and returns an int in the range [0...capacity]
	 * @param key the key...
	 * @return integer representing...
	 */
	private int hashKey(K key) {
		// to do
	}
}
