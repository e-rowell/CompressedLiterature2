import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.plaf.synth.SynthStyle;

/**
 * Compressed Literature Main.
 * 
 * @author Nicholas Hays & Ethan Rowell
 * @version Feb 27, 2016
 * Assignment 4: Compressed Literature2
 * Presented For: Dr. Chris Marriott
 */
public class CompLitMain {

	/**
	 * Main entry for program.
	 * 
	 * @param args command line args
	 */
	public static void main(String[] args) {
		File inputFile = new File("WarAndPeace.txt");
		compressText(inputFile);
	}

	/**
	 * Compresses the text and outputs the results to file.
	 * 
	 * @param inputFile The file to write the compressed text to.
	 */
	private static void compressText(File inputFile) {
		double startTime = System.currentTimeMillis();
		
		BufferedReader reader = null;
		StringBuilder str = new StringBuilder();
		
		try {
			reader = new BufferedReader(new FileReader(inputFile));
			
			while (reader.ready()) {
				str.append((char) reader.read());
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		CodingTree<String> tree = new CodingTree<>(str.toString());
		writeCodes(tree);
		
		File compressedFile = new File("compressed.txt");
		String binStr = encodeText(compressedFile, tree, str);
		
		double elapsedTime = (System.currentTimeMillis() - startTime) / 1000;
		
		System.out.println(String.format("Uncompressed file size: %d bytes", inputFile.length()));
		System.out.println(String.format("Compressed file size: %d bytes", compressedFile.length()));
		System.out.println(String.format("Compression ratio: %.2f%%",
				(double) compressedFile.length() / inputFile.length() * 100));
		System.out.println(String.format("Running Time: %.3f seconds \n", elapsedTime));
		
		decodeText(tree, binStr);
		
		testMethod(tree);
	}

	/**
	 * Writes the Huffman codes to file.
	 * 
	 * @param tree Coding tree that holds the Huffman codes for encoding.
	 * @param str The string of original characters
	 */
	private static void writeCodes(CodingTree<String> tree) {
		StringBuilder codes = new StringBuilder();
		codes.append('[');
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter("codes.txt"));
			for(String code : tree.codes.keySet()){
				codes.append(String.format("(%s, %s), ", code, tree.codes.get(code)));
			}
			codes.delete(codes.length() - 2, codes.length()); // removes last comma and space
			codes.append(']');
			
			bw.write(codes.toString());
			bw.close();
		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}
	
	/**
	 * Encodes the text to binary using Huffman codes in the tree.
	 * 
	 * @param compressedFile The file to write to.
	 * @param tree Coding tree that holds the Huffman codes for encoding.
	 * @param text The original text to encode.
	 * @return the binary string.
	 */
	private static String encodeText(File compressedFile, CodingTree<String> tree, StringBuilder text) {
		String huffString = tree.encodeText(text);	
		// testMethod(huffString);
		
		int numBytes = huffString.length() / 8;
		ArrayList<Integer> binCodes = new ArrayList<>();
		String intString;
		for (int i = 0; i < numBytes; i++) {
			intString = huffString.substring(i * 8, (i + 1) * 8);
			binCodes.add(Integer.parseUnsignedInt(intString, 2));
		}
		try {
			BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(compressedFile));
			for (int bin32Str : binCodes) {
				bos.write(bin32Str);
			}
			bos.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		return huffString.toString();
	}
	
	/**
	 * Decodes the text from the tree given the binary encoded text.
	 * 
	 * @param tree Coding tree that holds the Huffman codes for encoding.
	 * @param encodedText Encoded binary string.
	 */
	private static void decodeText(CodingTree<String> tree, String encodedText) {
		String originalText = tree.decode(encodedText);
		File decompressedFile = new File("decompressed.txt");
		try {
			BufferedOutputStream origBos = new BufferedOutputStream(new FileOutputStream(decompressedFile));
			char[] chars = originalText.toString().toCharArray();
			for (char c : chars) {
				origBos.write(c);
			}
			origBos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Test method to test MyHashTable.
	 * 
	 * @param tree Coding tree that holds the Huffman codes for encoding.
	 */
	private static void testMethod(CodingTree<String> tree) {
		MyHashTable<Integer, Character> testHashTable = new MyHashTable<>(100);
		Integer myInteger = new Integer(0);
		
		testHashTable.put(myInteger, 'c');
		
		// will find key in both instances
		if (testHashTable.containsKey(myInteger)) System.out.println("Contains: " + myInteger.toString());
		if (testHashTable.containsKey(0)) System.out.println("Contains: " + 0);
		
		// won't find key
		myInteger = Integer.valueOf(10);
		if (testHashTable.containsKey(myInteger)) System.out.println("Contains: " + myInteger.toString());
		
		// won't find key
		myInteger = new Integer(10);
		if (testHashTable.containsKey(myInteger)) System.out.println("Contains: " + myInteger.toString());
		
		testHashTable.put(myInteger, 'c');
		
		// will find all
		if (testHashTable.get(myInteger) != null) System.out.println("Found " + myInteger.toString());
		if (testHashTable.get(Integer.valueOf(10)) != null) System.out.println("Found " + myInteger.toString());
		if (testHashTable.get(10) != null) System.out.println("Found " + myInteger.toString());
		
		System.out.println("Print char: " + testHashTable.get(myInteger));
	}
}
