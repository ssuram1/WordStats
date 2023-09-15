import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;

/**
 * A class representing a Tokenizer that normalizes inputted text
 * @author Shravani Suram
 */
public class Tokenizer {
    /** stores the list of normalized words */
    private ArrayList<String> list;

    /**
     * Creates a list of all the normalized words from the inputted String array
     * @param file the text file with the words to be normalized
     */
    public Tokenizer(String file) {
        list = new ArrayList<String>();
        boolean slash = false;
        FileReader filereader;
        BufferedReader reader;
        try {
            filereader = new FileReader(file);
            reader = new BufferedReader(filereader);
            /** stores each normalized word and adds to list */
            StringBuilder builder = new StringBuilder();
            while (reader.ready()) {
                /** stores character to be read */
                char c = (char) reader.read();
                /** checks if character is a alphanumeric letter or digit */
                if (Character.isLetter(c) == true || Character.isDigit(c) == true) {
                    /** if letter is uppercase, will make lowercase */
                    if (Character.isUpperCase(c) == true) {
                        c = Character.toLowerCase(c);
                    }
                    builder.append(c);
                }
                /** if character is a space, then add word to ArrayList */
                else if (c == ' ' || c == '\n' || c == '\t' || c == '\r') {
                    if (builder.toString().length() != 0) {
                        this.list.add(builder.toString());
                        builder = new StringBuilder();
                    }
                }
            }
            /** checks and adds last word of text to list */
            if (builder.toString().length() != 0) {
                this.list.add(builder.toString());
            }
        }

        catch(FileNotFoundException e){
                System.out.println("FileNotFoundException");
        }
        catch(IOException b){
                System.out.println("IOException");
        }
     }

    /**
     * Creates a list of all the normalized words from the inputted String array
     * @param text the list of words to be normalized
     */
    public Tokenizer(String[] text) {
        list = new ArrayList<String>();
        boolean slash = false;
        /** stores each word in text inputted */
        String input;
        StringBuilder builder;
        /** Loops through each word in text file and normalizes */
        for (int i = 0; i < text.length; i++) {
            /** stores each normalized word and adds to list */
            builder = new StringBuilder();
            if (text[i] != null)
                input = text[i];
            else
                input = "";
            /** Loops through each character of word inputted */
            for (int j = 0; j < input.length(); j++) {
                char c = input.charAt(j);
                /** checks if alphanumeric character or digit to normalize */
                if (Character.isLetter(c) == true || Character.isDigit(c) == true) {
                    if (Character.isUpperCase(c) == true) {
                        c = Character.toLowerCase(c);
                    }
                    builder.append(c);
                }
                /** if character is a space, then add word to ArrayList */
                else if (c == ' ' || c == '\n' || c == '\t' || c == '\r') {
                    if (builder.toString().length() != 0) {
                        this.list.add(builder.toString());
                        builder = new StringBuilder();
                    }
                }
            }
            /** checks last word and adds to list if normalized */
            if (builder.toString().length() != 0) {
                this.list.add(builder.toString());
            }
        }
    }

    /**
     * Returns a list of all of the normalized words
     * @return a String ArrayList of all normalized words
     */
        public ArrayList<String> wordList() {
            if (this.list.isEmpty() != true) {
                return this.list;
            }
            else
                return null;
        }
}

