package dictionary;

import java.io.FileNotFoundException;
import java.util.Scanner;

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
            line = scan.nextLine();
            String[] command = line.trim().toLowerCase().split(" ");
            switch (command[0]) {
                case "add":
                    if(command.length < 2) {
                        System.out.println("Please enter a word to be added.");
                    } else {
                        dictionary.addWord(command[1]);
                    }
                    break;
                case "delete":
                    try {
                        dictionary.removeWord(command[1]);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case "search":
                    boolean found;
                    try {
                        found = dictionary.searchWord(command[1]);
                        if(!found) {
                            throw new DictionaryEntryNotFoundException(command[1] + " not found.");
                        }
                    } catch (DictionaryEntryNotFoundException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case "list":
                    if(dictionary.getAllWords().isEmpty())
                        System.out.println("No words found.");
                    else
                        System.out.print(dictionary.getAllWords());
                    break;
                case "stats":
                    System.out.println("Total words: " +  dictionary.getTotalWords());
                    System.out.println("Total unique words: " + dictionary.getUniqueWords());
                    System.out.println("Estimated load factor: " + dictionary.getLoadFactor());
                    break;
                case "exit":
                    System.out.print("Quitting...");
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
