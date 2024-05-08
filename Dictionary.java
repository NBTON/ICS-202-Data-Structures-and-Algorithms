import java.io.*;
import java.util.Scanner;

// The Dictionary class manages a dictionary using an AVL tree data structure
public class Dictionary {
    private AVLTree<String> dictionaryTree; // AVL tree to store dictionary words

    // Constructor to initialize the dictionary with a single string
    public Dictionary(String s) {
        dictionaryTree = new AVLTree<>(); // Initialize an empty AVL tree
        addWord(s); // Add the provided word to the dictionary
    }

    // Constructor to initialize an empty dictionary
    public Dictionary() {
        dictionaryTree = new AVLTree<>(); // Initialize an empty AVL tree
    }

    // Constructor to initialize the dictionary from a text file
    public Dictionary(File f) {
        dictionaryTree = new AVLTree<>(); // Initialize an empty AVL tree
        try {
            BufferedReader reader = new BufferedReader(new FileReader(f));
            String line;
            // Read lines from the file and add words to the dictionary
            while ((line = reader.readLine()) != null) {
                addWord(line.trim()); // Add each word after trimming leading/trailing spaces
            }
            reader.close();
        } catch (IOException e) {
            // If file reading fails, display an error message and exit the program
            System.out.println("The file does not exist");
            System.exit(0);
        }
    }

    // Method to add a word to the dictionary
    public void addWord(String s) {
        try {
            // Check if the word already exists in the dictionary, throw an exception if found
            if (dictionaryTree.isInTree(s)) {
                throw new WordAlreadyExistsException("Word already exists in the dictionary.");
            }
            // Insert the word into the AVL tree and display success message
            dictionaryTree.insertAVL(s);
            System.out.println("Word added successfully");
        } catch (WordAlreadyExistsException e) {
            // If the word already exists, display an exception message
            System.out.println("Exception: " + e.getMessage());
        }
    }

    // Method to check if a word exists in the dictionary
    public boolean findWord(String s) {
        return dictionaryTree.isInTree(s); // Returns true if the word is found in the AVL tree
    }

    // Method to delete a word from the dictionary
    public void deleteWord(String s) {
        try {
            // Check if the word exists in the dictionary, throw an exception if not found
            if (!dictionaryTree.isInTree(s)) {
                throw new WordNotFoundException("Word not found in the dictionary.");
            }
            // Delete the word from the AVL tree and display success message
            dictionaryTree.deleteAVL(s);
            System.out.println("Word deleted successfully");
        } catch (WordNotFoundException e) {
            // If the word is not found, display an exception message
            System.out.println("Exception: " + e.getMessage());
        }
    }

    // Method to find similar words to a given word in the dictionary
    public String[] findSimilar(String s) {
        Queue<String> similarWords = new Queue<>(); // Queue to store similar words
        char[] wordArray = s.toCharArray(); // Convert the input word to a character array
        int similarCount = 0; // Counter for similar words found

        // Iterate through each character of the word
        for (int i = 0; i < s.length(); i++) {
            char originalChar = wordArray[i];
            // Replace the current character with each letter of the alphabet (except original character)
            for (char c = 'a'; c <= 'z'; c++) {
                if (c != originalChar) {
                    wordArray[i] = c;
                    String modifiedWord = new String(wordArray);
                    // Check if the modified word exists in the dictionary and add it to similarWords queue
                    if (findWord(modifiedWord) && !contains(similarWords, modifiedWord)) {
                        similarWords.enqueue(modifiedWord);
                        similarCount++;
                    }
                }
            }
            wordArray[i] = originalChar; // Restore the original character
        }

        // Prepare an array to store similar words and populate it from the queue
        String[] result = new String[similarCount];
        int index = 0;
        while (!similarWords.isEmpty()) {
            result[index++] = similarWords.dequeue();
        }
        return result; // Return an array of similar words
    }

    // Helper method to check if a queue contains a specific word
    private boolean contains(Queue<String> queue, String word) {
        // Temporary queue to preserve the original queue's elements
        Queue<String> tempQueue = new Queue<>();
        boolean containsWord = false;

        // Check if the word is present in the queue
        while (!queue.isEmpty()) {
            String current = queue.dequeue();
            tempQueue.enqueue(current);
            if (current.equals(word)) {
                containsWord = true;
                break;
            }
        }

        // Restore the original elements back to the queue
        while (!tempQueue.isEmpty()) {
            queue.enqueue(tempQueue.dequeue());
        }

        return containsWord; // Return whether the word was found in the queue
    }

    // Method to save the dictionary to a file
    public void saveDictionaryToFile(String filename) {
        try {
            FileWriter writer = new FileWriter(filename); // Create a FileWriter for the specified file
            Queue<BSTNode<String>> queue = new Queue<>(); // Queue to traverse the AVL tree
            if (!dictionaryTree.isEmpty()) {
                // Enqueue the root node and traverse the AVL tree in level order
                queue.enqueue(dictionaryTree.root);
                while (!queue.isEmpty()) {
                    BSTNode<String> currentNode = queue.dequeue();
                    writer.write(currentNode.el + "\n"); // Write the word to the file
                    // Enqueue left and right children if present for further traversal
                    if (currentNode.left != null) {
                        queue.enqueue(currentNode.left);
                    }
                    if (currentNode.right != null) {
                        queue.enqueue(currentNode.right);
                    }
                }
                writer.close(); // Close the FileWriter after writing all words
                System.out.println("Dictionary saved successfully.");
                return;
            }
            System.out.println("Dictionary is empty. Nothing to save.");
        } catch (IOException e) {
            e.printStackTrace(); // Print the stack trace if an I/O exception occurs
        }
    }

    // Main method to interactively manage the dictionary
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Dictionary dictionary = null;

        System.out.print("Enter filename to create a dictionary: ");
        String input = scanner.nextLine();

        if (input.endsWith(".txt")) {
            File file = new File(input);
            dictionary = new Dictionary(file);
            System.out.println("Dictionary loaded successfully.");
        } else {
            System.out.println("Wrong input");
            System.exit(0);
        }

        // Interactive menu for dictionary operations
        while (true) {
            System.out.println("\nDictionary Operations:");
            System.out.println("1. Add word");
            System.out.println("2. Find word");
            System.out.println("3. Remove word");
            System.out.println("4. Find similar words");
            System.out.println("5. Save dictionary to file");
            System.out.println("6. Exit");
            System.out.print("Enter your choice (1-6): ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                // Perform operations based on user input
                case 1 -> {
                    System.out.print("Enter word to add: ");
                    String wordToAdd = scanner.nextLine();
                    dictionary.addWord(wordToAdd);
                }
                case 2 -> {
                    System.out.print("Enter word to find: ");
                    String wordToFind = scanner.nextLine();
                    boolean found = dictionary.findWord(wordToFind);
                    if (found) {
                        System.out.println("Word found in the dictionary.");
                    } else {
                        System.out.println("Word not found in the dictionary.");
                    }
                }
                case 3 -> {
                    System.out.print("Enter word to delete: ");
                    String wordToDelete = scanner.nextLine();
                    dictionary.deleteWord(wordToDelete);
                }
                case 4 -> {
                    System.out.print("Enter word to find similar words: ");
                    String wordToSearch = scanner.nextLine();
                    String[] similarWords = dictionary.findSimilar(wordToSearch);

                    System.out.print("Similar words: ");
                    for (String word : similarWords) {
                        System.out.print(word + ", ");
                    }
                    System.out.println();
                }
                case 5 -> {
                    System.out.print("Enter filename to save dictionary: ");
                    String filename = scanner.nextLine();
                    dictionary.saveDictionaryToFile(filename);
                }
                case 6 -> {
                    System.out.println("Exiting program.");
                    scanner.close();
                    System.exit(0);
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }
}

// Custom exception class for indicating that a word already exists in the dictionary
class WordAlreadyExistsException extends Exception {
    public WordAlreadyExistsException(String message) {
        super(message);
    }
}

// Custom exception class for indicating that a word was not found in the dictionary
class WordNotFoundException extends Exception {
    public WordNotFoundException(String message) {
        super(message);
    }
}
