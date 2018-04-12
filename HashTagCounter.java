import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class HashTagCounter {

	public static void main(String[] args) {

		// Initial data structure declarations
		FibHeap<Integer> fheap = new FibHeap<Integer>();
		Hashtable<String, Node<Integer>> hashtag = new Hashtable<>();

		// To map the hashtag to its respective node in the heap
		Map<String, Integer> map = null;

		// String inputFile = "sampleInput_Million.txt";
		String outputFile = "output_file.txt";
		String inputFile = args[0];

		// To store each line of the file
		String line = null;

		try {
			// FileReader reads text files in the default encoding.
			FileReader fileReader = new FileReader(inputFile);
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			BufferedWriter bufferedWriter = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream(outputFile), "utf-8"));

			// Read lines one at a time until "STOP" is encountered in the input
			// file
			while ((line = bufferedReader.readLine()).equalsIgnoreCase(new String("STOP")) == false) {
				map = new HashMap<String, Integer>();
				// Split line to extract hashtag and its respective frequency
				String[] parts = line.split(" ");

				// Identify the hashtag
				if (parts[0].charAt(0) == '#') {

					// Discard '#' from the hashtag
					String hashTag = parts[0].substring(1);
					// Parse frequency to string
					Integer frequency = Integer.parseInt(parts[1]);

					// Update : Increase frequency of the hashtag if already
					// present in the hashtable
					if (hashtag.containsKey(hashTag)) {
						// Parse the hashtable to get the address of the
						// corresponding hashtag
						Node<Integer> key = hashtag.get(hashTag);
						// Increase the frequency of the hashtag and exit
						// from loop
						fheap.increaseKey(key, (key.getValue() + frequency));
					}
					// Add the new hashtag into the hashtable along with the
					// address to a node with its frequency in the Fibonacci
					// Heap
					else {
						Node<Integer> addr = fheap.enqueue(frequency);//
						hashtag.put(hashTag, addr);
					}
				}

				// Display hashtags with most frequency

				// Check if the line has only digits using regex
				else if (parts[0].matches("\\d+")) {
					Node<Integer> addr = null;
					// Parse the digit and store the value
					int num = Integer.parseInt(parts[0]);

					// Remove the max element 'n' number of times specified in
					// the input
					for (int i = 0; i < num - 1; i++) {
						addr = fheap.removeMax();

						// Get hashtag corresponding to the frequency and write
						// into the output file - comma separated
						for (String key : hashtag.keySet()) {
							if (hashtag.get(key) == addr) {
								bufferedWriter.write(key + ",");
								map.put(key, addr.getValue());
								break;
							}
						}
						// Store the hashtag along with its frequency to
						// reinsert

					}
					addr = fheap.removeMax();
					for (String key : hashtag.keySet()) {
						if (hashtag.get(key) == addr) {
							// Write newline for next query
							bufferedWriter.write(key + "\n");
							map.put(key, addr.getValue());
							hashtag.remove(key);
							break;
						}
					}
					addr = null;

					// Reinsert the hashtags removed, for next query
					for (String s : map.keySet()) {
						addr = fheap.enqueue(map.get(s));
						hashtag.put(s, addr);
					}

					// Clear the map for next query
					map = null;
				} else
					continue;
			}

			// Always close files.
			bufferedReader.close();
			bufferedWriter.close();

		} catch (FileNotFoundException ex) {
			System.out.println("Unable to open file '" + inputFile + "'");
		} catch (IOException ex) {
			System.out.println("Error reading file '" + inputFile + "'");
		}
	}

}
