package engine;

import java.io.*;
//import java.io.FileNotFoundException;
import java.lang.*;
//import java.io.InputStream;
import java.util.*;
import java.lang.String;

import engine.exceptions.*;


import engine.jaxb.schema.generated.GameDescriptor;
import engine.jaxb.schema.generated.Letter;
import engine.jaxb.schema.generated.Letters;
import engine.jaxb.schema.generated.Structure;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;


public class GameDataFromXml {

    public class DataLetter{
       private Letter letter; // sign , score, freq
       private int amount = 0;

        DataLetter(Letter l){
           letter = l;
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

    List<DataLetter> letters = new ArrayList<>();

    private int totalAmountOfLetters = 0;
    private short boardSize;
    private int numOfCubeWigs;
    private int numOfTries;
    private int leftTargetDeckSize;  //בקופה
    private String dictFileName;
    private short totalTargetDeckSize; //כמות אריחים
    //private int totalWordsInDict;----> moved to dictionary class
    private final static String JAXB_XML_GAME_PACKAGE_NAME = "engine.jaxb.schema.generated";

    // test:
/*
    public static void main(String[] argv) {
        int freqratio = 0;
        GameDataFromXml g = new GameDataFromXml();
        g.initializingDataFromXml("ABC");
        System.out.println("board size: " + g.getBoardSize());
        System.out.println("num of cubs: " + g.getNumOfCubeWigs());
        for (int i : g.getRatiofrequencyLetter().values())
            System.out.println(i);

        for (int i = 0; i < g.getLetters().size(); i++) {
            freqratio = g.getRatiofrequencyLetter().get(g.getLetters().get(i).getSign().get(0));
            System.out.println("letter " + (i + 1) + ": " + g.getLetters().get(i).getSign().get(0) + " frequency: " + g.getFrequencyLetter().get(g.getLetters().get(i).getSign().get(0)) +
                    " Ratio frequency: " + freqratio);
        }

    }*/

    // get and set funcs:

    public short getTargetDeckSize() {
        return totalTargetDeckSize;
    }

   // public Map<String, Integer> getRatiofrequencyLetter() {
   //     return ratiofrequencyLetter;
   // }

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

   // public Map<String, Double> getFrequencyLetter() {
     //   return frequencyLetter;
    //}

    public String getDictFileName() {
        return dictFileName;
    }

    public int getTotalAmountOfLetters() {
        return totalAmountOfLetters;
    }

    public void initializingDataFromXml(String pathToXml) {
        GameDescriptor gd;
        InputStream inputStream = null;
        // TODO: delete note after tests
        /*
        try {
            inputStream = new FileInputStream(pathToXml);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }*/
        inputStream = GameDataFromXml.class.getResourceAsStream("/resources/master.xml");
        Structure struct;

        try {
            double totalFreq = 0;
            gd = deserializeFrom(inputStream);
            struct = gd.getStructure();

            // creates list of data letters

            for (int i = 0; i < struct.getLetters().getLetter().size(); i++) {
                this.letters.add(i,new DataLetter(struct.getLetters().getLetter().get(i)));
                totalFreq += this.letters.get(i).getLetter().getFrequency();
            }

            for(int i = 0; i < letters.size(); i++){
                double freq = letters.get(i).getLetter().getFrequency();
                letters.get(i).setAmount((int) ((Math.ceil(freq / 100 )) * totalFreq));
                totalAmountOfLetters += letters.get(i).amount;
            }

            //init board size
            boardSize = struct.getBoardSize();
            //init num of wings
            this.numOfCubeWigs = struct.getCubeFacets();
            //init num of tries
            this.numOfTries = struct.getRetriesNumber();
            //init dictionart file name
            dictFileName = struct.getDictionaryFileName();
            //init targer deck size
            this.totalTargetDeckSize = struct.getLetters().getTargetDeckSize();
            //   this.targetDeckSize = struct.getLetters().getTargetDeckSize();---->  הצפי

        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

   // NO NEED:

    // calc functions:
/*
    public void calcRatiofrequencyLetter(Map<String, Double> frequencyletter) {

        double totalfreq = 0;
        int ratiofreq;
        for (Double freq : frequencyletter.values()) {
            totalfreq += freq;
        }

        for (Map.Entry<String, Double> item : frequencyletter.entrySet()) {
            ratiofreq = (int) Math.ceil((item.getValue() / totalfreq) * 100);
            this.ratiofrequencyLetter.put(item.getKey(), ratiofreq);
        }
    }
*/

    //creats the xml details:

    private static GameDescriptor deserializeFrom(InputStream in) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(JAXB_XML_GAME_PACKAGE_NAME);
        Unmarshaller u = jc.createUnmarshaller();
        return (GameDescriptor) u.unmarshal(in);
    }

    // check Validation functions:

    public boolean isValidXml(String pathToXml) throws NotXmlFileException {

        String extension = pathToXml.substring(pathToXml.lastIndexOf(".") + 1, pathToXml.length());
        if ((extension != "xml") || (extension != ".xml")) {
            throw new NotXmlFileException("ITS NOT XML FILE!");
        } else
            return true;
    }


    // call this func after calling the one above

    public boolean isDictionaryInRightPos(String pathToDictFile, String pathToXml) throws WrongPathException {

        pathToXml = pathToXml.substring(0, pathToXml.length() - 4); // minus 4 for ".xml"
        while (!pathToXml.endsWith("\\")) {
            pathToXml = pathToXml.substring(0, pathToXml.length() - 1);
        }
        if (pathToDictFile == pathToXml + "dictionary\\" + this.getDictFileName() + ".txt")
            return true;
        else {
            throw new WrongPathException("THE PATH: " + pathToDictFile + " IS NOT THE VALID PATH TO DICTIONARY FILE!");
        }
    }

    public boolean isValidBoardSize(int size) throws InvalidInputException {
        if ((size >= 5) && (size <= 50))
            return true;
        else
            throw new InvalidInputException("BOARD SIZE" + size + " IS OUT OF RANGE!");
    }

    public boolean isAllLettersApperOne() throws InvalidInputException {
        boolean isMoreThanOnce = false;
        for (int i = 0; i < this.getLetters().size(); i++) {
            DataLetter l = this.getLetters().get(i);
            String c = this.getLetters().get(i).getLetter().getSign().get(0);
            this.getLetters().remove(i);
            isMoreThanOnce = this.getLetters().contains(c);
            this.getLetters().add(i, l);
            //appears more than once
            if (isMoreThanOnce)
                throw new InvalidInputException("THE SIGN: " + c + "APPEARS MORE THAN ONCE!");
        }
        return true;
    }

    public boolean isEnoughLettersForBoard() throws NotEnoughLettersException {
        if (this.letters.size() < this.boardSize * this.boardSize) {
            throw new NotEnoughLettersException(this.boardSize * this.boardSize, this.letters.size());
        }
        return  true;
    }
}






