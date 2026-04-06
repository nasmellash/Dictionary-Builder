package dictionary;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Implements a dictionary using a hash table with separate chaining.
 * The class sanitizes the words (by removing any character that is not a-z),
 * and stores an accompanying frequency counter.
 * The hash table size is determined using algorithm provided in instructions.
 */
public class DictionaryBuilder {
    private static final double LOAD_FACTOR = 0.6;
    private GenericLinkedList<String>[] hashTable;
    private int hashTableSize;

    /**
     * Constructs a DictionaryBuilder object given an estimated amount of numbers.
     * If estimated entries is less than or equal to zero, use a default value of 100 for the estimated entries.
     * Using the Gaussian prime algorithm, find a hash table size then declare the size of the hash table.
     * @param estimatedEntries the estimated words in the file
     */
    public DictionaryBuilder(int estimatedEntries) {
        // Assumes the estimated entries
        int estimatedTableSize;
        if(estimatedEntries <= 0)
            estimatedTableSize = (int) (100 / LOAD_FACTOR); // Random default value for CLI test
        else
            estimatedTableSize = (int) (estimatedEntries / LOAD_FACTOR);
        this.hashTableSize = findClosetGaussianPrime(estimatedTableSize);
        this.hashTable = new GenericLinkedList[hashTableSize];
    }

    /**
     * Constructs a DictionaryBuilder object using a file.
     * First, checks if filename is empty, if so, throws IllegalArgumentException.
     * Then creates a file, retrieves the size of the file, and creates an estimated amount of entries
     * based on justification from instructions.
     * Then generates a hash table using a Gaussian prime algorithm.
     * Then goes through the file and adds each word.
     * @param filename the name of the file being read from
     * @throws FileNotFoundException if file doesn't exist when attempt to be opened and read by Scanner
     * @throws IllegalArgumentException if filename is empty
     */
    public DictionaryBuilder(String filename) throws FileNotFoundException {
        if(filename.isEmpty()) {
            throw new IllegalArgumentException("Filename cannot be empty");
        }

        File  file = new File(filename); // Create the file
        long bytes = file.length(); // Get the size of the file
        int estimatedEntries = (int) (bytes / 100); // Get the amount of unique words
        this(estimatedEntries); // Use other constructor to reduce code for initialization

        Scanner scan = new Scanner(file);
        while(scan.hasNextLine()) {
            String line = scan.nextLine();
            String[] words = line.split(" ");
            for(int i = 0; i < words.length; i++) {
                words[i] = words[i].trim().toLowerCase().replaceAll("[^a-z]", "");
                addWord(words[i]);
            }
        }
    }

    /**
     * Adds word to the dictionary by sanitizing word parameter and
     * generating a hash index to place the word.
     * First checks if empty, if so, it stops the addition.
     * Then, checks if hash index slot is null, if so, creates a linked list
     * at the point, and adds the word.
     * If slot is not null, checks if word is present at the hash index, if so,
     * increments the count of the word by calling increment method.
     * Otherwise, it adds the word to the end of the bucket.
     * @param word the word being added
     */
    public void addWord(String word){
        word = sanitizeWord(word);
        if(word.isEmpty())
            return;

        int hashIndex = generateHashIndex(word);

        if(hashTable[hashIndex] == null) {
            hashTable[hashIndex] = new GenericLinkedList<>();
            hashTable[hashIndex].addLast(word);
        } else if (hashTable[hashIndex].contains(word)) {
            hashTable[hashIndex].increment(word);
        } else {
            hashTable[hashIndex].addLast(word);
        }
    }

    /**
     * Creates a sorted ArrayList of all the words in the dictionary by
     * iterating through each bucket in the hash table, and adding it to
     * the ArrayList.
     * Then utilizes Java's utility method, Collections.sort() to sort the ArrayList.
     * @return a sorted ArrayList of all the words in the dictionary
     */
    public List<String> getAllWords() {
        ArrayList<String> allWords = new ArrayList<>();
        for (GenericLinkedList<String> bucket : hashTable) {
            if (bucket != null) {
                ListIterator<String> iterator = bucket.listIterator();
                while (iterator.hasNext()) {
                   allWords.add(iterator.next());
                }
            }
        }
        Collections.sort(allWords);
        return allWords;
    }

    /**
     * Gets the frequency of the word parameter by generating the hash index
     * and checking if word appears in bucket.
     * If it does, it uses the getCount method to find the frequency
     * If the hash index leads to a null slot or doesn't exist in
     * the bubble, returns 0.
     * @param word the word whose frequency is being retrieved
     * @return the frequency of the word if present, or 0
     */
    public int getFrequency(String word) {
        word = sanitizeWord(word);
        int hashIndex = generateHashIndex(word);
        if(hashTable[hashIndex] == null)
            return 0;
        else if(hashTable[hashIndex].contains(word))
            return hashTable[hashIndex].getCount(word);
        else
            return 0;
    }

    /**
     * Estimates the load factor of the hash table by dividing the amount
     * of unique words by the hash table size.
     * @return the estimated load factor as a double
     */
    public double getLoadFactor() {
        return (double) getUniqueWords() / hashTableSize;
    }

    /**
     * Gets the total number of words by traversing the sorted ArrayList
     * of all the words and adding up the frequency of each element.
     * @return the total number of words
     */
    public int getTotalWords() {
        int totalWords = 0;

        for(int i = 0; i < getAllWords().size(); i++) {
            totalWords += getFrequency(getAllWords().get(i));
        }

        return totalWords;
    }

    /**
     * Gets the amount of unique words by taking the size of the sorted words
     * by calling size method on getAllWords method.
     * @return the amount of unique words
     */
    public int getUniqueWords() { return getAllWords().size();}

    /**
     *
     * @param word the word being removed from the hash table
     */
    public void removeWord(String word){
        word = sanitizeWord(word);
        if(word.isEmpty())
            return;
        int hashIndex = generateHashIndex(word);

        if(hashTable[hashIndex] == null)
            throw new DictionaryEntryNotFoundException(word + " not found.");
        else if(hashTable[hashIndex].contains(word)) {
            hashTable[hashIndex].remove(word);
        } else {
            throw new DictionaryEntryNotFoundException(word + " not found.");
        }
    }

    /**
     * Checks if a word exists in the hash table by taking the parameter, and generating a hashIndex.
     * Returns true if the word exists at the hash index.
     * Returns false if the hash index element is null or
     * if bucket at hash index does not contain the word.
     * @param word the word being searched for
     * @return a boolean signaling if the word exists in hash table or not
     */
    public boolean searchWord(String word){
        word = sanitizeWord(word);
        int hashIndex = generateHashIndex(word);

        if(hashTable[hashIndex] == null)
            return false;
        else return hashTable[hashIndex].contains(word);
    }

    /**
     * Generates the hash index of a given word by using Java's hashCode() method
     * and using the modulus operator.
     * Also ensures hashCode value is positive by using Java's absolute value method.
     * @param word the word used to create a hash index
     * @return the hash index based off of the parameter
     */
    public int generateHashIndex(String word) {
        return Math.abs(word.hashCode()) % hashTable.length;
    }
    /**
     * Sanitizes word parameter by trimming whitespace, making characters
     * lowercase, and removing any special characters (anything that is not a-z).
     */
    public String sanitizeWord(String word) {
        return word.toLowerCase().replaceAll("[^a-z]", "");
    }
    /**
     * Uses a StringBuilder and traverses through an Arraylist to create
     * a String of every word in the hash table separated by a newline.
     * @return a String of all words found in the hash table (in alphabetical order)
     */
    @Override
    public String toString() {
        StringBuilder output = new StringBuilder();
        for(int i = 0; i < getAllWords().size(); i++) {
            output.append(getAllWords().get(i)).append("\n");
        }
        return output.toString();
    }

    /**
     * Takes an integer value and finds the closet Gaussian Prime number,
     * 4k + 3, needed for hash table sizing.
     * First guarantees the integer mod 4 = 3. Then runs a loop,
     * checking for primality.
     * @param num an integer representing the estimated entries of a hash table
     * @return a Gaussian prime number closet to the parameter (greater than or equal to)
     */
    public static int findClosetGaussianPrime (int num){
        if(num <= 2) return 2; // Smallest Gaussian Prime Number

        int mod = num % 4; // Gets the remainder of the number mod 4
        int n = num + (3 - mod); // 3 - mod value guarantees the number mod 4 equals 3

        // Runs a while-loop to check if the number is prime
        while(true){
            if(isPrime(n))
                return n; // If prime, return the value
            n += 4; // If not prime, increment by 4 (Keeps the remainder at 3).
        }

    }

    /**
     * Utilizes the provided isPrime method from the instructions
     * to check if an integer is prime (using 6k+1 optimization).
     * Returns true if integer is prime.
     * Returns false if integer is composite (not prime).
     * @param n an integer value being checked for primality.
     * @return a boolean signaling if prime or composite
     */
    public static boolean isPrime (int n) {
        // Corner case
        if (n <= 1)
            return false;
        // For n=2 o r n=3 it will check
        if (n == 2 || n == 3)
            return true;
        // For multiple of 2 or 3 this will check
        if (n % 2 == 0 || n % 3 == 0 )
            return false;
        // Check for remaining possible factors
        for (int i = 5 ; i <= Math.sqrt(n); i = i + 6) {
            if (n % i == 0 || n % (i + 2) == 0)
                return false;
        }
            return true;
    }

}
