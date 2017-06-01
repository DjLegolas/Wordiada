package engine;

import engine.exceptions.DictionaryNotFoundException;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

class Dictionary {
    private long numberOfWords = 0;
    private Map<String, Word> words = new HashMap<>();
    enum FreqSegment { COMMON, LESS_COMMON, RARE }

    private class Word {
        private String word;
        private long count = 0;
        private float frequency;
        private FreqSegment freqSegment;

        private Word(String word) {
            this.word = word;
            count = 1;
        }

        private long getCount() {
            return count;
        }

        private float getFrequency() {
            return frequency;
        }

        private void setFrequency(float value) {
            frequency = value;
        }

        private void advanceCount() {
            count++;
        }

        @Override
        public int hashCode() {
            int hash = 13;
            hash = 71 * hash + word.hashCode();
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (obj == this){
                return true;
            }
            if (!(obj instanceof Word)) {
                return false;
            }

            final Word other = (Word) obj;
            return !((this.word == null) ? (other.word != null) : !this.word.equals(other.word));
        }
    }


    Dictionary(String pathToDict) throws DictionaryNotFoundException {
        Scanner scanner;
        try {
            scanner = new Scanner(new File(pathToDict));
        } catch (FileNotFoundException e) {
            throw new DictionaryNotFoundException(pathToDict);
        }

        String currentWord;
        String toRemove = " !?,.:;-_=+*\"'\\(\\)\\{\\}\\[\\]%$";
        while (scanner.hasNext()) {
            currentWord = scanner.next();
            currentWord = currentWord.replaceAll("[" + toRemove +"]", "").toUpperCase();
            if (currentWord.length() >= 2) {
                if (!words.containsKey(currentWord)){
                    words.put(currentWord, new Word(currentWord));
                }
                else {
                    words.get(currentWord).advanceCount();
                }
                numberOfWords++;
            }
        }
        calcFrequency();
        setSegment();
    }


    boolean hasWord(String word) {
        return words.containsKey(word);
    }

    long getNumberOfWords() {
        return numberOfWords;
    }

    long getWordAmount(String word) {
        return words.get(word).getCount();
    }

    private void calcFrequency() {
        for (Word word: words.values()) {
            float freq = word.getCount() / numberOfWords * 100;
            word.setFrequency(freq);
        }
    }

    private void setSegment() {
        long firstSegmentSize = numberOfWords / 3;
        long secondSegmentSize = firstSegmentSize * 2;
        long totalWordsProccessed = 0;
        FreqSegment currentSegment = FreqSegment.COMMON;
        List<Word> wordList = new ArrayList<>();
        wordList.addAll(words.values());
        wordList.sort(Comparator.comparing(word -> (-word.count)));
        for (Word word: wordList) {
            if (totalWordsProccessed > secondSegmentSize) {
                currentSegment = FreqSegment.RARE;
            }
            else if (totalWordsProccessed > firstSegmentSize) {
                currentSegment = FreqSegment.LESS_COMMON;
            }
            word.freqSegment = currentSegment;
            totalWordsProccessed += word.count;
        }
    }

    int getSegmentScore(String strWord) {
        Word word = words.get(strWord);
        switch (word.freqSegment){
            case COMMON:
                return 1;
            case LESS_COMMON:
                return 2;
            case RARE:
                return 3;
            default:
                return 0;
        }
    }
}
