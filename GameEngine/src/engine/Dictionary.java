package engine;

import engine.exceptions.DictionaryNotFoundException;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Dictionary {
    public static void main(String[] args) {
        Dictionary dic;
        try {
            dic = new Dictionary("C:\\Users\\Ido\\Downloads\\war and piece.txt");
        } catch (DictionaryNotFoundException e) {
            System.out.println("Dictionary not found...");
            return;
        }

        Map<String, Long> words = dic.getWords();
        long totalWords = dic.getNumberOfWords(), myCount = 0;
        for (String word: words.keySet()) {
            long val = words.get(word);
            myCount += val;
            System.out.println(word + ": " + val);
        }
        System.out.println("Total: " + totalWords + "\nMy count: " + myCount);
    }

    private long numberOfWords = 0;
    private Map<String, Long> words = new HashMap<>();

    Dictionary(String pathToDict) throws DictionaryNotFoundException {
        Scanner scanner = null;
        try {
            scanner = new Scanner(new File(pathToDict));
        } catch (FileNotFoundException e) {
            throw new DictionaryNotFoundException(e.getMessage());
        }

        String currentWord;
        String toRemove = " !?,.:;\\-_=+*\"'\\(\\)\\{\\}\\[\\]%$";
        while (scanner.hasNext()) {
            currentWord = scanner.next();
            currentWord = currentWord.replaceAll("[" + toRemove +"]", "").toUpperCase();
            if (currentWord.length() >= 2) {
                if (!words.containsKey(currentWord)){
                    words.put(currentWord, 0L);
                }
                words.put(currentWord, words.get(currentWord) + 1);
                numberOfWords++;
            }
        }
    }

    public boolean hasWord(String word) {
        return words.containsKey(word);
    }

    public long getNumberOfWords() {
        return numberOfWords;
    }

    public long getWordAmount(String word) {
        return words.get(word);
    }

    public Map<String, Long> getWords() {
        return words;
    }
}
