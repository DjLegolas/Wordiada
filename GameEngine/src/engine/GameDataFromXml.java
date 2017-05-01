package engine;

import java.io.*;
//import java.io.FileNotFoundException;
import java.lang.*;
//import java.io.InputStream;
import java.util.*;
import java.lang.String;

import engine.exceptions.*;


import engine.jaxb.schema.generated.*;
import engine.jaxb.schema.generated.Player;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;


public class GameDataFromXml {

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
        public void setAmount(int amount) {
            this.amount = amount;
        }
        public int getAmount() {
            return amount;
        }
        public void setLetter(Letter letter) {
            this.letter = letter;
        }
    }

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

    public void initializingDataFromXml(String pathToXml) throws WrongPathException, NotValidXmlFileException, DictionaryNotFoundException {
        GameDescriptor gd;
        InputStream inputStream = null;

        try {
            inputStream = new FileInputStream(pathToXml);
        } catch (FileNotFoundException e) {
            throw new WrongPathException();
        }
        //inputStream = GameDataFromXml.class.getResourceAsStream("/resources/master.xml");
        Structure struct;

        try {
            gd = deserializeFrom(inputStream);
        } catch (JAXBException e) {
            throw new NotValidXmlFileException();
        }
        double totalFreq = 0;
        struct = gd.getStructure();

        // creates list of data letters

        for (int i = 0; i < struct.getLetters().getLetter().size(); i++) {
            this.letters.add(i,new DataLetter(struct.getLetters().getLetter().get(i)));
            totalFreq += this.letters.get(i).getLetter().getFrequency();
        }

        for(int i = 0; i < letters.size(); i++){
            double freq = letters.get(i).getLetter().getFrequency();
            letters.get(i).setAmount((int) Math.ceil(Math.ceil(freq / totalFreq * 100 ) / 100 * struct.getLetters().getTargetDeckSize()));
            totalAmountOfLetters += letters.get(i).amount;
        }

        //init board size
        boardSize = struct.getBoardSize();
        //init num of wings
        this.numOfCubeWigs = struct.getCubeFacets();
        //init num of tries
        this.numOfTries = struct.getRetriesNumber();
        //init dictionary file name
        dictFileName = struct.getDictionaryFileName();
        //init dictFilePath variable

        pathToXml = pathToXml.substring(0, pathToXml.length() - 4); // minus 4 for ".xml"
        while (!pathToXml.endsWith("\\")) {
            pathToXml = pathToXml.substring(0, pathToXml.length() - 1);
        }
        dictFilePath = pathToXml + "dictionary\\" + dictFileName;

        //init target deck size
        this.totalTargetDeckSize = struct.getLetters().getTargetDeckSize();
        //   this.targetDeckSize = struct.getLetters().getTargetDeckSize();---->  הצפי
        //init board
        board = new Board(boardSize, letters, totalAmountOfLetters);
        //init players
        players = gd.getPlayers();
        //init dictionary
        dictionary = new Dictionary(dictFilePath);
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
        List<Player> players = this.players.getPlayer();
        if (players.size() != 2) {
            throw new NumberOfPlayersException(players.size(), engine.Player.MIN_PLAYERS, engine.Player.MAX_PLAYERS);
        }
        return players;
    }

    //creats the xml details:

    private static GameDescriptor deserializeFrom(InputStream in) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(JAXB_XML_GAME_PACKAGE_NAME);
        Unmarshaller u = jc.createUnmarshaller();
        return (GameDescriptor) u.unmarshal(in);
    }

    // check Validation functions:

    public boolean isValidXml(String pathToXml) throws NotXmlFileException {
        if (!pathToXml.toLowerCase().endsWith(".xml")) {
            throw new NotXmlFileException("ITS NOT XML FILE!");
        } else
            return true;
    }

    // call this func after calling the one above

    public boolean isDictionaryInRightPos() throws DictionaryNotFoundException {

        File f = new File(dictFilePath);
        if (f.exists() && f.isFile())
            return true;
        else {
            throw new DictionaryNotFoundException(dictFilePath);
        }
    }

    public boolean isValidBoardSize(short size) throws BoardSizeException {
        if ((size >= Board.MIN_SIZE) && (size <= Board.MAX_SIZE))
            return true;
        else
            throw new BoardSizeException(size, Board.MIN_SIZE, Board.MAX_SIZE);
    }

    public boolean isAllLettersAppearOnce() throws DuplicateLetterException {
        boolean isMoreThanOnce = false;
        for (int i = 0; i < this.getLetters().size(); i++) {
            DataLetter l = this.getLetters().get(i);
            String c = this.getLetters().get(i).getLetter().getSign().get(0);
            this.getLetters().remove(i);
            for(DataLetter toCompare : this.getLetters()){
               if(toCompare.getLetter().getSign().get(0) == c)
                   isMoreThanOnce = true;
            }
            this.getLetters().add(i, l);
            //appears more than once
            if (isMoreThanOnce)
                throw new DuplicateLetterException("THE SIGN: " + c + "APPEARS MORE THAN ONCE!");
        }
        return true;
    }

    public boolean isEnoughLettersForBoard() throws NotEnoughLettersException {
        if (this.letters.size() < this.boardSize * this.boardSize) {
            throw new NotEnoughLettersException(this.boardSize * this.boardSize, this.letters.size());
        }
        return  true;
    }

    public int calcScore(String word) {
        return 1;
    }
}






