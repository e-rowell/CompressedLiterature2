import java.util.Map;

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
	MyHashTable<String, String> codes;
	
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
		codes = new MyHashTable<>();
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
	
}
