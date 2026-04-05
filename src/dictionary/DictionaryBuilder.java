package dictionary;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class DictionaryBuilder {
    private static final double LOAD_FACTOR = 0.6;
    private GenericLinkedList<String>[] hashTable;
    private int hashTableSize;


    public DictionaryBuilder(int estimatedEntries) {
        int estimatedTableSize = (int) (estimatedEntries / LOAD_FACTOR);
        this.hashTableSize = findClosetGaussianPrime(estimatedTableSize);
        this.hashTable = new GenericLinkedList[hashTableSize];
    }

    public DictionaryBuilder(String filename) throws FileNotFoundException {
        if(filename.isEmpty()) {
            throw new IllegalArgumentException("Filename cannot be empty");
        }

        File  file = new File(filename);
        long bytes = file.length();
        int estimatedEntries = (int) (bytes / 100);
        this.hashTableSize = findClosetGaussianPrime(estimatedEntries);
        this.hashTable = new GenericLinkedList[hashTableSize];

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

    public void addWord(String word){
        word = word.trim().toLowerCase().replaceAll("[^a-z]", "");
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

    public int getFrequency(String word) {
        int hashIndex = generateHashIndex(word);
        if(hashTable[hashIndex] == null)
            return 0;
        else if(hashTable[hashIndex].contains(word))
            return hashTable[hashIndex].getCount(word);
        else
            return 0;
    }

    public double getLoadFactor() {
        return (double) getUniqueWords() / hashTableSize;
    }

    public int getTotalWords() {
        int totalWords = 0;

        for(int i = 0; i < getAllWords().size(); i++) {
            totalWords += getFrequency(getAllWords().get(i));
        }

        return totalWords;
    }

    public int getUniqueWords() { return getAllWords().size();}

    public void removeWord(String word){
        word = word.trim().toLowerCase().replaceAll("[^a-z]", "");
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

    public boolean searchWord(String word){
        int hashIndex = generateHashIndex(word);
        if(hashTable[hashIndex] == null)
            return false;
        else return hashTable[hashIndex].contains(word);
    }

    public int generateHashIndex(String word) {
        return Math.abs(word.hashCode()) % hashTable.length;
    }

    @Override
    public String toString() {
        StringBuilder output = new StringBuilder();
        for(int i = 0; i < getAllWords().size(); i++) {
            output.append("\n").append(getAllWords().get(i));
        }
        return String.valueOf(output);
    }

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
