package engine;

import java.io.*;
import java.lang.*;
import java.util.*;
import java.lang.String;

import engine.exceptions.*;


import engine.jaxb.schema.generated.*;
import engine.jaxb.schema.generated.Player;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;


class GameDataFromXml {

    public class DataLetter{
       private Letter letter; // sign , score, freq
       private int amount = 0;

        DataLetter(Letter l){
           letter = l;
           amount = 0;
        }
        public Letter getLetter() {
            return letter;
        }
        void setAmount(int amount) {
            this.amount = amount;
        }
        int getAmount() {
            return amount;
        }
        public void setLetter(Letter letter) {
            this.letter = letter;
        }
    }

    private GameDescriptor gameDescriptor;
    private List<DataLetter> letters = new ArrayList<>();
    private int totalAmountOfLetters = 0;
    private short boardSize;
    private int numOfCubeWigs;
    private int numOfTries;
    private String dictFileName;
    private String dictFilePath;
    private short totalTargetDeckSize; //כמות אריחים
    private final static String JAXB_XML_GAME_PACKAGE_NAME = "engine.jaxb.schema.generated";
    private Board board;
    private Players players;
    private Dictionary dictionary;
    private enum WinAccordingTo {WORD_COUNT, WORD_SCORE}
    private WinAccordingTo winAccordingTo;

    // get and set funcs:

    public short getTargetDeckSize() {
        return totalTargetDeckSize;
    }
    public short getBoardSize() {
        return boardSize;
    }
    public int getNumOfCubeWigs() {
        return numOfCubeWigs;
    }
    public int getNumOfTries() {
        return numOfTries;
    }
    public List<DataLetter> getLetters() {
        return letters;
    }
    public String getDictFileName() {
        return dictFileName;
    }
    public int getTotalAmountOfLetters() {
        return totalAmountOfLetters;
    }

    public void initializeDataFromXml(String pathToXml)
            throws WrongPathException, NotValidXmlFileException, DictionaryNotFoundException, WinTypeException,
            NotXmlFileException, BoardSizeException, DuplicateLetterException, NotEnoughLettersException {

        loadXml(pathToXml);
        Structure struct;
        struct = gameDescriptor.getStructure();
        buildDataLetters(struct);

        //init board size
        boardSize = struct.getBoardSize();
        //init num of wings
        numOfCubeWigs = struct.getCubeFacets();
        //init num of tries
        numOfTries = struct.getRetriesNumber();
        //init dictionary file name
        dictFileName = struct.getDictionaryFileName();

        initDictionary(pathToXml);
        initBoard();

        //init players
        players = gameDescriptor.getPlayers();

        initWinType();
    }

    //creates the xml details:
    private static GameDescriptor deserializeFrom(InputStream in) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(JAXB_XML_GAME_PACKAGE_NAME);
        Unmarshaller u = jc.createUnmarshaller();
        return (GameDescriptor) u.unmarshal(in);
    }

    // load the xml to gameDescriptor
    private void loadXml(String pathToXml) throws NotXmlFileException, WrongPathException, NotValidXmlFileException {
        if (!pathToXml.toLowerCase().endsWith(".xml")) {
            throw new NotXmlFileException();
        }
        InputStream inputStream;

        try {
            inputStream = new FileInputStream(pathToXml);
        } catch (FileNotFoundException e) {
            throw new WrongPathException();
        }

        try {
            gameDescriptor = deserializeFrom(inputStream);
        } catch (JAXBException e) {
            throw new NotValidXmlFileException();
        }
    }

    // builds the letters variable and calculates the each letter's frequency
    private void buildDataLetters(Structure struct) throws DuplicateLetterException {
        // creates list of data letters
        double totalFreq = 0;
        for (int i = 0; i < struct.getLetters().getLetter().size(); i++) {
            this.letters.add(i, new DataLetter(struct.getLetters().getLetter().get(i)));
            totalFreq += letters.get(i).getLetter().getFrequency();
        }

        for (DataLetter letter : letters) {
            double freq = letter.getLetter().getFrequency();
            letter.setAmount((int) Math.ceil(Math.ceil(freq / totalFreq * 100) / 100 * struct.getLetters().getTargetDeckSize()));
            totalAmountOfLetters += letter.amount;
        }
        verifyLettersAppearOnce();
        //init target deck size
        totalTargetDeckSize = struct.getLetters().getTargetDeckSize();
        //   this.targetDeckSize = struct.getLetters().getTargetDeckSize();---->  הצפי
    }

    private void verifyLettersAppearOnce() throws DuplicateLetterException {
        for (int i = 0; i < letters.size(); i++) {
            DataLetter l = letters.get(i);
            String c = letters.get(i).getLetter().getSign().get(0);
            letters.remove(i);
            for(DataLetter toCompare : letters){
                if(toCompare.getLetter().getSign().get(0).equals(c)) {
                    throw new DuplicateLetterException(c);
                }
            }
            letters.add(i, l);
        }
    }

    // check if dictionary exists and pars it
    private void initDictionary(String pathToXml) throws DictionaryNotFoundException {
        pathToXml = pathToXml.substring(0, pathToXml.length() - 4); // minus 4 for ".xml"
        while (!pathToXml.endsWith("\\")) {
            pathToXml = pathToXml.substring(0, pathToXml.length() - 1);
        }
        dictFilePath = pathToXml + "dictionary\\" + dictFileName;
        dictionary = new Dictionary(dictFilePath);
    }

    // initialize board after size check
    private void initBoard() throws BoardSizeException, NotEnoughLettersException {
        if ((boardSize < Board.MIN_SIZE) && (boardSize > Board.MAX_SIZE)) {
            throw new BoardSizeException(boardSize, Board.MIN_SIZE, Board.MAX_SIZE);
        }
        if (totalAmountOfLetters < boardSize * boardSize) {
            throw new NotEnoughLettersException(boardSize * boardSize, totalAmountOfLetters);
        }
        //init board
        board = new Board(boardSize, letters, totalAmountOfLetters);
    }

    // gets win type
    private void initWinType() throws WinTypeException {
        // init game type
        String winnerAccordingTo = gameDescriptor.getGameType().getWinnerAccordingTo();
        switch (winnerAccordingTo) {
            case ("WordCount"):
                this.winAccordingTo = WinAccordingTo.WORD_COUNT;
                break;
            case ("WordScore"):
                this.winAccordingTo = WinAccordingTo.WORD_SCORE;
                break;
            default:
                throw new WinTypeException(winnerAccordingTo);
        }
    }

    public Board getBoard() {
        return board;
    }

    public Dictionary getDictionary() {
        return dictionary;
    }

    public void updateBoard(List<int[]> points) throws OutOfBoardBoundariesException {
        board.update(points);
    }

    public List<engine.jaxb.schema.generated.Player> getPlayers() throws NumberOfPlayersException{
        List<Player> players;
        if (this.players == null) {
            return new ArrayList<>();
        }
        players = this.players.getPlayer();
        if (players.size() > 2) {
            //TODO: fix when supporting more than 2
            throw new NumberOfPlayersException(players.size(), engine.Player.MIN_PLAYERS, engine.Player.MIN_PLAYERS);
        }
        return players;
    }

    public float calcScore(String word) {
        if (winAccordingTo == WinAccordingTo.WORD_COUNT) {
            return 1;
        }
        else if (winAccordingTo == WinAccordingTo.WORD_SCORE) {
            return 1;
        }
        else {
            return 0;
        }
    }

    public int getKupaAmount() {
        return board.getKupaAmount();
    }

    public List<DataLetter> getKupa() {
        return board.getKupa();
    }
}
