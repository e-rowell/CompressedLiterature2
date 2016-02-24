
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
public class CodingTree<T> {
	
	/**
	 * public​ hash table of words or separators used as keys to retrieve strings of 1s
	 * and 0s as values.
	 */
	//MyHashTable<String, String> codes; build when working
	Hashtable<String, String> codes;
	Hashtable<String, TreeNode> nodes; 
	PriorityQueue<TreeNode> pq;
	StringBuilder myHuffCode;
	ArrayList<String> myTextWords;
	TreeNode root;
	
	
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
		nodes = new Hashtable<>();
		pq = new PriorityQueue<>();
		myHuffCode = new StringBuilder();
		myTextWords = new ArrayList<>();
		parseWords(fullText);
		buildHuffman();
	}
	
	/**
	 * 
	 * @param theOrigMsg
	 */
	@SuppressWarnings("unchecked")
	private void parseWords(String theOrigMsg) {
		StringBuilder temp = new StringBuilder();
		Pattern pattern = Pattern.compile("[a-zA-Z0-9-']");
		Matcher matcher;
		
		for(char c : theOrigMsg.toCharArray()) {
			matcher = pattern.matcher(Character.toString(c));
			if(matcher.matches()) { 
				temp.append(c);
			} else {
				myTextWords.add(temp.toString());
				myTextWords.add("" + c);
				// System.out.println();
				// System.out.println(temp.toString());
				// System.out.println("Delimited by " + Integer.toHexString(c | 0x10000).substring(1));
				
				// gets the word
				if (nodes.get(temp.toString()) != null) {
					nodes.get(temp.toString()).myFreq++;
				} else {
					nodes.put(temp.toString(), new TreeNode((T) temp.toString(),
							1, null, null));
				}
				
				temp.setLength(0);
				
				// gets the char the delimited the word
				if (nodes.get(Character.toString(c)) != null) {
					nodes.get(Character.toString(c)).myFreq++;
				} else {
					nodes.put(Character.toString(c),
							new TreeNode((T) Character.toString(c),1, null, null));
				}
			}
		}
		
		for (String string : nodes.keySet()) {
			pq.add(nodes.get(string));
		}
	}
	
	/**
	 * 
	 * @param root
	 */
	private void buildCodes(TreeNode root) {
		if (root == null)
			return;
		if (root.myLeft != null) {
			myHuffCode.append(0);
		}
		buildCodes(root.myLeft);
		if (root.myRight != null) {
			myHuffCode.append(1);
		}
		buildCodes(root.myRight);
		if (isLeaf(root)) {
			System.out.println(myHuffCode.toString());
			codes.put((String) root.myData, myHuffCode.toString());
		}
		if (myHuffCode.length() > 0) {
			myHuffCode.deleteCharAt(myHuffCode.length() - 1);
		}
	}
	
	
	/**
	 * 
	 */
	private void buildHuffman() {
		while (pq.size() > 1) {
			TreeNode node1 = getMin();
			TreeNode node2 = getMin();
			pq.add(combineWeights(node1, node2));
		}
		
		root = pq.peek();
		buildCodes(root);
	}
	
	/**
	 * 
	 * @param node1
	 * @param node2
	 * @return
	 */
	private TreeNode combineWeights(TreeNode node1,
			TreeNode node2) {
		int newFreq = node1.myFreq + node2.myFreq;
		TreeNode node = new TreeNode(null, newFreq, node1, node2);
		return node;
	}
	
	/**
	 * 
	 * @return
	 */
	private TreeNode getMin() {
		return pq.poll();
	}

	/**
	 * 
	 * @param root
	 * @return
	 */
	private boolean isLeaf(TreeNode root) {
		boolean theTruth = false;
		if (root.myLeft == null && root.myRight == null) {
			theTruth = true;
		}
		return theTruth;
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
	
	
	
	/**
	 * 
	 * @author Nicholas Hays and Ethan Rowell
	 * @version Feb 22, 2016
	 * @param <T>
	 */
	class TreeNode implements Comparable<T> {
	// class TreeNode<T extends Comparable<T>> {
		T myData;
		TreeNode myLeft;
		TreeNode myRight;
		int myFreq;
		
		public TreeNode(T data, int freq, TreeNode left, TreeNode right) {
			myFreq = freq;
			myData = data;
			myLeft = left;
			myRight = right;
		}
		
		public T getData() {
			return myData;
		}
		
		@SuppressWarnings("unchecked")
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
