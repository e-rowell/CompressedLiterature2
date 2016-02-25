import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class CompLitMain {

	/**
	 * this method will carry out compression of a file using the CodingTree class.
	 * 
	 * make sure you test with more than one file. ○ Output to two files.  
	 * Again feel free to hardcode the names of these files into your program.  
	 * These are the codes text file and the compressed binary file. ○ Display statistics.  
	 * You must output the original size (in bits), the compressed size (in bits), 
	 * the compression ratio (as a percentage), the elapsed time for compression, 
	 * and the hash table statistics. ○ Component testing.  Include all methods used for 
	 * component testing and debugging.
		 
	 * @param args command line args
	 */
	public static void main(String[] args) {
		File inputFile = new File("WarAndPeace.txt");
		compressText(inputFile);
	}

	private static void compressText(File inputFile) {
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
		writeCodes(tree, str);
		encodeText(tree, str);
	}

	private static void writeCodes(CodingTree<String> tree, StringBuilder str) {
		StringBuilder codes = new StringBuilder();
		codes.append('{');
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter("codes.txt"));
			for(String code : tree.codes.keySet()){
				codes.append(code + "=" + tree.codes.get(code) + ", \n");
			}
			codes.append('}');
			bw.write(codes.toString());
			bw.close();
		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}
	
	private static String encodeText(CodingTree<String> tree, StringBuilder text) {
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
			BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream("compressed.txt"));
			for (int bin32Str : binCodes) {
				bos.write(bin32Str);
			}
			bos.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		return huffString.toString();
	}
}
