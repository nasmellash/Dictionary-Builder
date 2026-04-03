package dictionary;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.ListIterator;
import java.util.Scanner;

public class DictionaryBuilder {
    private static final double LOAD_FACTOR = 0.6;
    private GenericLinkedList<String>[] hashTable;


    public DictionaryBuilder(int estimatedEntries) {
        int estimatedTableSize = (int) (estimatedEntries / LOAD_FACTOR);
        int hashTableSize = findClosetGaussianPrime(estimatedTableSize);
        this.hashTable = new GenericLinkedList[hashTableSize];
    }
    public DictionaryBuilder(String filename) throws FileNotFoundException {
        if(filename.isEmpty()) {
            throw new IllegalArgumentException("Filename cannot be empty");
        }

        Scanner scan = new Scanner(new File(filename));
        while(scan.hasNextLine()) {
            String line = scan.nextLine();
            String[] words = line.split(" ");
            for(int i = 0; i < words.length; i++) {
                words[i] = words[i].trim().toLowerCase().replaceAll("[^a-z]", "");
                addWord(words[i]);
            }
        }
    }

    public void addWord(String word){
        int hashIndex = Math.abs(word.hashCode()) % hashTable.length;

        if(hashTable[hashIndex] == null) {
            hashTable[hashIndex] = new GenericLinkedList<>();
            hashTable[hashIndex].addLast(word);
        } else if (hashTable[hashIndex].contains(word)) {
            hashTable[hashIndex].increment(word);
        } else {
            hashTable[hashIndex].addLast(word);
        }
    }
    public List<String> getAllWords() { return null; }
    public int getFrequency(String word){return 0;}
    public void removeWord(String word){}










    public static int findClosetGaussianPrime (int num){
        if(num <= 2) return 2; // Smallest Gaussian Prime

        int mod = num % 4;
        int n = num + (3 - mod);

        while(true){
            if(isPrime(n))
                return n;
            n += 4; // Keeps the remainder at 4
        }

    }

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
