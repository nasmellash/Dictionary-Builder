package dictionary;

import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * A command-line interface class built to interact with the DictionaryBuilder class.
 * Accepts (if provided) a filename as initial command-line argument to load a dictionary.
 * Also supports additional features such as search, add, delete, list, stats, and exit (to
 * terminate program).
 */
public class DictionaryShell {
    public static void main (String[] args) {
        System.out.println("Welcome to the Dictionary Builder CLI.");
        System.out.println("Available commands: add<word>, delete<word>, search<word>," +
                "list, stats, exit.");
        String filename;
        DictionaryBuilder dictionary;

        if (args.length == 0) {
            dictionary = new DictionaryBuilder(0);
        } else {
            try {
                filename = args[0];
                dictionary = new DictionaryBuilder(filename);
            } catch (FileNotFoundException e) {
                dictionary = new DictionaryBuilder(0);
            }
        }

        Scanner scan = new Scanner(System.in);
        String line;
        boolean done = false;

        while (!done) {
            System.out.print("> ");

            if(!scan.hasNextLine())
                break;

            line = scan.nextLine();
            String[] command = line.trim().toLowerCase().split(" ");
            switch (command[0]) {
                case "add":
                    if(command.length < 2) {
                        System.out.println("Please enter a word to be added.");
                    } else {
                        dictionary.addWord(command[1]);
                    }
                    System.out.println("\"" + command[1] + "\" added.");
                    break;
                case "delete":
                    try {
                        dictionary.removeWord(command[1]);
                        System.out.println("\"" + command[1] + "\" deleted.");

                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case "search":
                    boolean found = dictionary.searchWord(command[1]);
                    if(!found) {
                        System.out.println(command[1] + " not found.");
                    } else {
                        System.out.println(dictionary.getFrequency(command[1]) + " instances(s) of " + "\"" + command[1] + "\" found.");
                    }
                    break;
                case "list":
                    if(dictionary.getAllWords().isEmpty())
                        System.out.println("No words found.");
                    else
                        System.out.println(dictionary.toString());
                    break;
                case "stats":
                    System.out.println("Total words: " +  dictionary.getTotalWords());
                    System.out.println("Total unique words: " + dictionary.getUniqueWords());
                    System.out.println("Estimated load factor: " + dictionary.getLoadFactor());
                    break;
                case "exit":
                    System.out.println("Quitting...");
                    scan.close();
                    done = true;
                    break;
                default:
                    System.out.println("Invalid command."
                                + " Please try again.");
            }
        }

    }
}
