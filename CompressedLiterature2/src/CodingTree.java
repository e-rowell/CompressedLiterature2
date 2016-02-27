
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Nicholas Hays and Ethan Rowell
 * Date: 2/9/2016
 * Assignment 3: Compressed Literature2
 * Presented For: Dr. Chris Marriott
 */

/**
 * A coding tree object provides methods to compress textual data
 * using Huffman's encoding algorithm. 
 * 
 * @author Nicholas Hays and Ethan Rowell
 */
public class CodingTree<T> {

	/**
	 * public​ hash table of words or separators used as keys to retrieve strings of 1s
	 * and 0s as values.
	 */
	MyHashTable<String, String> codes;
	MyHashTable<String, TreeNode> nodes; 
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
		codes = new MyHashTable<>(32768);
		nodes = new MyHashTable<>(32768);
		pq = new PriorityQueue<>();
		myHuffCode = new StringBuilder();
		myTextWords = new ArrayList<>();
		parseWords(fullText);
		buildHuffman();
	}

	/**
	 * parses the incoming message into a collection of unique words seperated by
	 * specific delimeters. Each word is added to a hashmap that stores the word's 
	 * string as its key, and a tree node object as its value.  
	 * @param theOrigMsg the original message to be encoded. 
	 */
	@SuppressWarnings("unchecked")
	private void parseWords(String theOrigMsg) {
		StringBuilder temp = new StringBuilder();
		Pattern pattern = Pattern.compile("[-a-zA-Z0-9']");
		Matcher matcher;

		
		for(char c : theOrigMsg.toCharArray()) {
			matcher = pattern.matcher(Character.toString(c));
			if(matcher.matches()) { 
				temp.append(c);
			} else {
				if (temp.toString().length() > 0) {
					myTextWords.add(temp.toString());
				}
				myTextWords.add(Character.toString(c));
				if (nodes.get(temp.toString()) != null) {
					nodes.get(temp.toString()).myFreq++;
				} else {
					nodes.put(temp.toString(), new TreeNode((T) temp.toString(), 1, null, null));
				}

				temp.setLength(0);

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
	 * encodes each word from the original input text to a string of '1' and '0's 
	 * to later be parsed into bytes for compressed storage. 
	 * 
	 * @return the string of '1' and '0's. 
	 */
	public String encodeText() {
		StringBuilder binString = new StringBuilder();

		for (String word : myTextWords) {
			binString.append(codes.get(word));
		}
		nodes.stats();
		return binString.toString();
	}


	/**
	 * recursively traverses the huffman tree concatenating a '0' if left traverse
	 * and '1' if right traverse to a temporary string. Once the leaf node is encountered, the 
	 * string is placed in a map of codes that links the leaf nodes data to the traversed string of 
	 * '1' and '0's. As the function climbs back up the tree the last '1' or '0' to be added to the 
	 * temporary string is removed to allocate a unique prefix for the next code. 
	 * 
	 * @param root the root of the huffman tree. 
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
			codes.put((String) root.myData, myHuffCode.toString());
		}
		if (myHuffCode.length() > 0) {
			myHuffCode.deleteCharAt(myHuffCode.length() - 1);
		}
	}


	/**
	 * builds a huffman tree by combining the frequency of the nodes
	 * with the least weights via polling the priority queue. Uses the root
	 * node as an argument to build codes from. 
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
	 * combines the frequency of the 2 nodes with the lowest weights
	 * into one node. 
	 * 
	 * @param node1 a node of the huffman tree.
	 * @param node2 a node of the huffman tree. 
	 * @return
	 */
	private TreeNode combineWeights(TreeNode node1,
			TreeNode node2) {
		int newFreq = node1.myFreq + node2.myFreq;
		TreeNode node = new TreeNode(null, newFreq, node1, node2);
		return node;
	}

	/**
	 * polls the priority queue to find the node with the smallest 
	 * frequency. 
	 * @return the minimum node. 
	 */
	private TreeNode getMin() {
		return pq.poll();
	}

	/**
	 * checks if the node is a leaf node.  
	 * 
	 * @param root the leaf node of the huffman tree. 
	 * @return the truth value of the node. 
	 */
	private boolean isLeaf(TreeNode leaf) {
		boolean theTruth = false;
		if (leaf.myLeft == null && leaf.myRight == null) {
			theTruth = true;
		}
		return theTruth;
	}


	/**
	 * this method will take the output of Huffman’s encoding and produce the original text.
	 * 
	 * @param binaryString the string of '1' and '0's. 
	 * @return the word associated with the huffman code. 
	 */
	public String decode(String binaryString) {
		StringBuilder decodedString = new StringBuilder();
		TreeNode node = root;

		for (Character character : binaryString.toCharArray()) {
			if (character == '0') {
				node = node.myLeft;
				if (isLeaf(node)) {
					decodedString.append(node.myData);
					node = root;
				}
			} else {
				node = node.myRight;
				if (isLeaf(node)) {
					decodedString.append(node.myData);
					node = root;
				}
			}
		}
		return decodedString.toString();
	}



	/**
	 * Tree Node represents a data container for holding nodes. Implements comparable 
	 * to determine ordering.
	 *  
	 * @author Nicholas Hays and Ethan Rowell
	 * @version Feb 22, 2016
	 * @param <T> type to compare to. 
	 */
	class TreeNode implements Comparable<T> {
		T myData;
		TreeNode myLeft;
		TreeNode myRight;
		int myFreq;
		/**
		 * constructor for creating nodes which contain data representation. 
		 * @param data the data to be inserted.
		 * @param freq the frequency of the word in the text film. 
		 * @param left the left child node.
		 * @param right the right child node. 
		 */
		public TreeNode(T data, int freq, TreeNode left, TreeNode right) {
			myFreq = freq;
			myData = data;
			myLeft = left;
			myRight = right;
		}
		/**
		 * gets the data object stored in this tree node. 
		 * 
		 * @return the data type store in this object. 
		 */
		public T getData() {
			return myData;
		}
		
		/**
		 * compares Tree Node frequencies to determine ordering of priority queue. 
		 */
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
