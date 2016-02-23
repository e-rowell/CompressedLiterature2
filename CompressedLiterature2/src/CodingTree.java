
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

/* Authors: Nicholas Hays and Ethan Rowell
 * Date: 2/9/2016
 * Assignment 3: Compressed Literature2
 * Presented For: Dr. Chris Marriott
 */

/**
 * 
 * @author Nicholas Hays and Ethan Rowell
 *
 */
public class CodingTree {
	
	/**
	 * public​ hash table of words or separators used as keys to retrieve strings of 1s
	 * and 0s as values.
	 */
	//MyHashTable<String, String> codes; build when working
	Hashtable<String, String> codes;
	PriorityQueue<TreeNode> pq;
	StringBuilder myHuffCode;
	char[] myDelimeter = "[a-zA-Z0-9]";
	/**
	 *  a ​public​ data member that is the message encoded using the Huffman codes.
	 */
	String bits;
	
	/**
	 * Constructor that takes the text of an English message to be compressed.
	 * The constructor is responsible for calling all methods that carry out the 
	 * Huffman coding algorithm and ensuring that the following property has the
	 * correct value. 
	 * 
	 * @param fullText the text to be encoded
	 */
	public CodingTree(String fullText) {
		//codes = new MyHashTable<>();
		codes = new Hashtable<>(2^15);
		pq = new PriorityQueue<>();
		myHuffCode = new StringBuilder();
		parseWords(fullText);
		
	}
	private void parseWords(String theOrigMsg) {
		String temp;
		for(char c: theOrigMsg.toCharArray()) {
			if(isDelimeter(c))
		}
		
	}
	private boolean isDelimeter(char c) {
		if()
		return false;
	}
	/**
	 * this method will take the output of Huffman’s encoding and produce the original text.
	 * @param bits
	 * @param codes
	 * @return
	 */
	String decode(String bits, Map<String, String> codes) {
		// (Optional) to do 
		return bits;
	}
	
	
	
	
	class TreeNode<T> implements Comparable<T> {
		String myData;
		TreeNode<T> myLeft;
		TreeNode<T> myRight;
		int myFreq;
		
		public TreeNode(String data, int freq, TreeNode left, TreeNode right) {
			myFreq = freq;
			myData = data;
			myLeft = left;
			myRight = right;
		}
		
		
		@SuppressWarnings("rawtypes")
		public int compareTo(T o) {
			TreeNode test = (TreeNode) o;
			if( this.myFreq > test.myFreq) return 1;
			if(myFreq == test.myFreq) {
				return 0;
			}
			return -1;
		}
		
	}
	
	
	
	
	
	
	
}
