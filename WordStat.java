import java.util.ArrayList;
import java.util.Collections;
import java.util.NoSuchElementException;


/**
 * A class that can calculate statistics for a set of words
 */
public class WordStat {
    /** Stores the normalized order of the words in the inputted String */
    private ArrayList<String> order;

    /** Stores the normalized words and their ranking based on frequency */
    private HashTable<Integer> rankTable;
    /** Stores normalized words and their frequencies in ascending order */
    private ArrayList<HashTable<Integer>.HashNode<Integer>> sortedTable;
    /** Stores the normalized words and their frequencies */
    private HashTable<Integer> table;


    /**
     * Creates an instance of WordStat
     * @param file the file containing the text to be examined
     */
    public WordStat(String file) {
        Tokenizer token;
        int rank, rankN, prevFreq;

        /** Normalize words and store order of words */
        token = new Tokenizer(file);
        order = token.wordList();

        /** Add all normalized words to a hashtable and store frequency */
        table = new HashTable<Integer>(100);
        /** Checks that there is at least 1 normalized word */
        if (order != null) {
            for (int i = 0; i < order.size(); i++) {
                table.put(order.get(i), 1);
            }
            /** Copy HashTable into ArrayList */
            sortedTable = table.getTable();

            /** Sort the arraylist by frequency */
            Collections.sort(sortedTable);
            rankTable = new HashTable<Integer>();
            /** Stores the number of words that have been traversed */
            rank = 0;
            /** Stores the rank that should be given to the current word */
            rankN = 0;
            /** Stores the frequency of the previous word to check if equal */
            prevFreq = -1;
            /** Traverses through sorted list of words and frequencies */
            for (int i = sortedTable.size() - 1; i >= 0; i--) {
                rank++;
                if(prevFreq != sortedTable.get(i).getValue()) {
                    rankN = rank;
                }
                /** Add word and its rank to rankTable */
                rankTable.put(sortedTable.get(i).getKey(), rankN);
                /** Update prevFreq before next iteration */
                prevFreq = sortedTable.get(i).getValue();
            }
        }
    }


    /**
     * Creates an instance of WordStat
     * @param text the set of Strings containing the text to be examined
     */
    public WordStat(String[] text) {
        Tokenizer token;
        int rank, rankN, prevFreq;

        /** Normalize words and store order of words */
        token = new Tokenizer(text);
        order = token.wordList();

        /** Add all normalized words to a hashtable and store frequency */
        table = new HashTable<Integer>(100);
        /** Checks that there is at least 1 normalized word */
        if (order != null) {
            for (int i = 0; i < order.size(); i++) {
                table.put(order.get(i), 1);
            }
            /** Copy HashTable into ArrayList */
            sortedTable = table.getTable();

            /** Sort the arraylist by frequency */
            Collections.sort(sortedTable);
            rankTable = new HashTable<Integer>();
            /** Stores the number of words that have been traversed */
            rank = 0;
            /** Stores the rank that should be given to the current word */
            rankN = 0;
            /** Stores the frequency of the previous word to check if equal */
            prevFreq = -1;
            int j = 0;
            /** Traverses through sorted list of words and frequencies */
            for (int i = sortedTable.size() - 1; i >= 0; i--, j++) {
                rank++;
                if(prevFreq != sortedTable.get(i).getValue()) {
                    rankN = rank;
                }
                /** Add word and its rank to rankTable */
                rankTable.put(sortedTable.get(i).getKey(), rankN);
                /** Update prevFreq before next iteration */
                prevFreq = sortedTable.get(i).getValue();

            }

        }
    }

    /**
     * Counts the number of times a word appears in the text
     * @param word the word who's frequency will be recorded
     * @return the frequency of the inputted word
     */
    public int wordCount(String word) {
        return table.get(word);
    }

    /**
     * Returns the rank of the word inputted based on its frequency relative to other words
     * @param word the word whose rank will be returned
     * @return the rank of the word
     */
    public int wordRank(String word) {
        /** checks if word is in the table */
        try {
            return rankTable.get(word);
            }
       catch(NoSuchElementException e) {
            return 0;
       }
    }

    /**
     * Returns k words with the highest frequencies
     * @param k the number of words to return
     * @return k words with the highest frequencies
     */
    public String[] mostCommonWords(int k) {
        /** checks if k is less than 0 */
        if(k >= 0) {
            String[] result = new String[k];
            int count = 0;
            /** Traverses from back for highest frequencies */
            for (int i = sortedTable.size() - 1; count < k; i--) {
                result[count] = sortedTable.get(i).getKey();
                count++;
            }
            return result;
        }
        else
            throw new IllegalArgumentException();
    }
    /**
     * Returns k words with the lowest frequencies
     * @param k the number of words to return
     * @return k words with the lowest frequencies
     */
    public String[] leastCommonWords(int k) {
        /** checks if k is less than 0 */
        if(k >= 0) {
            String[] result = new String[k];
            /** Traverses list from the front for lowest frequencies */
            for (int i = 0; i < k; i++) {
                result[i] = sortedTable.get(i).getKey();
            }
            return result;
        }
        else
            throw new IllegalArgumentException();
    }

    /**
     * Returns k most frequent words before or after specified word
     * @param k the number of words to return in array
     * @param baseWord the word from which words are returned before or after
     * @param precede whether to return most frequent words before or after
     * @return k most frequent words before or after base word
     * @throws IllegalArgumentException
     *         thrown when k is negative
     */
    public String[] mostCommonCollocations(int k, String baseWord, boolean precede) throws IllegalArgumentException{
        /** Stores all of the words before or after baseword */
        HashTable<Integer> commonWords = new HashTable<Integer>();
        HashTable<Integer>.HashNode<Integer> node, node1;
        /** List with words before or after baseword sorted by frequency */
        ArrayList<HashTable<Integer>.HashNode<Integer>> commonWordsSorted;
        /** stores freq of words before or after base word */
        int freq;
        /** flag that indicates if base word was found yet */
        boolean wordFound = false;
        /** stores words traversing through in list */
        String word1;
        /** stores number of words before or after base word traversed through */
        int count = 0;
        /** stores the final resulting arraylist with k most frequent words before or after base word */
        if(k >= 0 && order != null) {
            String[] result = new String[k];
            /** traverse through list to find baseword and continue if precede is false */
            for (int i = 0; i < order.size(); i++) {
                word1 = order.get(i);
                if (wordFound == false && word1.equals(baseWord)) {
                    wordFound = true;
                    /** uses saved words BEFORE the baseword if precede is true */
                    if (precede == true) {
                        break;
                    }
                    /** continue in loop to collect words AFTER baseword if precede is false */
                    continue;
                }
                /** check if word has already been added to array and update only frequency if so */
                if (precede == true || (precede == false && wordFound == true)) {
                    try {
                        freq = commonWords.get(word1);
                    } catch (NoSuchElementException e) {
                        freq = table.get(word1);
                        commonWords.put(word1, freq);
                    }
                }
            }
            if(wordFound == false) {
                throw new IllegalArgumentException();
            }
            /** Copy HashTable into ArrayList */
            commonWordsSorted = commonWords.getTable();
            Collections.sort(commonWordsSorted);

            /** traverse through list of words before or after and add k most common words to result */
            for (int i = commonWordsSorted.size() - 1; i >= 0; i--) {
                if (commonWordsSorted.get(i).getKey() != null) {
                    result[count] = commonWordsSorted.get(i).getKey();
                    count++;
                    if (count >= k)
                        break;
                }
            }
            return result;
        }
        else
            throw new IllegalArgumentException();
    }
}
