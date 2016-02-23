import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

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
		
		CodingTree tree = new CodingTree(str.toString());
		
	}
}
