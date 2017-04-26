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

    Map<String, Double> frequencyLetter = new HashMap<String, Double>();
    Map<String, Integer> ratiofrequencyLetter = new HashMap<String, Integer>();

    Map<String, Double> frequencyWord = new HashMap<String, Double>();
    List<Letter> letters = new ArrayList<Letter>();


    private int boardSize;
    private int numOfCubeWigs;
    private int numOfTries;
    private int totalTiles;
    private int leftBoxTiles;
    private String dictFileName;
    private short targetDeckSize; //כמות אריחים

    private final static String JAXB_XML_GAME_PACKAGE_NAME = "engine.jaxb.schema.generated";

    // test:

    public static void main(String[] argv){
        int freqratio = 0;
        GameDataFromXml g = new GameDataFromXml();
        g.initializingDataFromXml("ABC");
        System.out.println("board size: "+ g.getBoardSize());
        System.out.println("num of cubs: " + g.getNumOfCubeWigs() );
        for(int i: g.getRatiofrequencyLetter().values())
            System.out.println(i);

        for (int i=0; i<g.getLetters().size(); i++){
            freqratio = g.getRatiofrequencyLetter().get(g.getLetters().get(i).getSign().get(0));
            System.out.println("letter " + (i+1) + ": " + g.getLetters().get(i).getSign().get(0) + " frequency: " + g.getFrequencyLetter().get(g.getLetters().get(i).getSign().get(0)) +
            " Ratio frequency: " + freqratio);
        }

    }

    // get and set funcs:

    public short getTargetDeckSize() { return targetDeckSize; }
    public Map<String, Integer> getRatiofrequencyLetter() {
        return ratiofrequencyLetter;
    }
    public int getBoardSize() {
        return boardSize;
    }
    public int getNumOfCubeWigs() {
        return numOfCubeWigs;
    }
    public int getNumOfTries() {
        return numOfTries;
    }
    public List<Letter> getLetters() {
        return letters;
    }
    public Map<String, Double> getFrequencyLetter() {
        return frequencyLetter;
    }
    public String getDictFileName() { return dictFileName; }


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
            gd = deserializeFrom(inputStream);
            struct = gd.getStructure();
            Letters letters = struct.getLetters();

            // creates list of letters + frequency

            for (int i = 0; i < letters.getLetter().size(); i++) {
               this.letters.add(i, letters.getLetter().get(i));
                frequencyLetter.put( this.letters.get(i).getSign().get(0), this.letters.get(i).getFrequency());
            }
            //init board size
            boardSize = struct.getBoardSize();
            //init num of wings
            this.numOfCubeWigs = struct.getCubeFacets();
            //init num of tries
            this.numOfTries = struct.getRetriesNumber();
            //init dictionart file name
            dictFileName = struct.getDictionaryFileName();
            calcRatiofrequencyLetter(frequencyLetter);
            //init targer deck size
            this.targetDeckSize = struct.getLetters().getTargetDeckSize();

        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }


    public void calcRatiofrequencyLetter(Map<String, Double> frequencyletter){

        double totalfreq = 0;
        int ratiofreq;
        for(Double freq : frequencyletter.values()){
            totalfreq += freq;
        }

        for(Map.Entry<String, Double> item : frequencyletter.entrySet()){
            ratiofreq = (int)Math.ceil((item.getValue()/totalfreq)*100);
            this.ratiofrequencyLetter.put(item.getKey(), ratiofreq);
        }
    }
    private static GameDescriptor deserializeFrom(InputStream in) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(JAXB_XML_GAME_PACKAGE_NAME);
        Unmarshaller u = jc.createUnmarshaller();
        return (GameDescriptor) u.unmarshal(in);
    }

    // check Validation functions:

    public boolean isValidXml(String pathToXml) throws  NotXmlFileException {

            String extension = pathToXml.substring(pathToXml.lastIndexOf(".") + 1, pathToXml.length());
            if ((extension != "xml") || (extension != ".xml")){
                throw new NotXmlFileException("ITS NOT XML FILE!");
            }
            else
            return true;
    }


    // call this func after calling the one above

    public boolean isDictionaryInRightPos(String pathToDictFile, String pathToXml) throws WrongPathException{

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

    public boolean isValidBoardSize(int size) throws  InvalidInputException{
        if((size >= 5) && (size <= 50))
            return true;
        else
            throw new InvalidInputException("BOARD SIZE" + size + " IS OUT OF RANGE!");
    }

    public boolean isAllLettersApperOne() throws InvalidInputException{
        boolean isMoreThanOnce = false;
        for(int i = 0; i < this.getLetters().size(); i++){
            Letter l = this.getLetters().get(i);
            String c = this.getLetters().get(i).getSign().get(0);
            this.getLetters().remove(i);
            isMoreThanOnce = this.getLetters().contains(c);
            this.getLetters().add(i,l);
            //appears more than once
            if(isMoreThanOnce)
                throw new InvalidInputException("THE SIGN: " + c + "APPEARS MORE THAN ONCE!");
        }
        return true;
    }

}





        /*
        GameDescriptor gd;
        Structure struct;
        InputStream inputStream = GameDataFromXml.class.getResourceAsStream("/resources/master.xml");
        try {
             gd = deserializeFrom(inputStream);
             struct = gd.getStructure();
             Letters letters = struct.getLetters();

             // creats list of letters
             for (int i = 0; i < letters.getLetter().size(); i++) {
             //System.out.println("letter number" + i + ": " + letters.getLetter().get(i).getSign());
             letters.getLetter().add(i, letters.getLetter().get(i));
            }
        } catch (JAXBException e) {
            e.printStackTrace();
        }

        //init board size
        try {
            gd = deserializeFrom(inputStream);

        } catch (JAXBException e) {
            e.printStackTrace();
        }

        struct = gd.getStructure();

        boardSize = struct.getBoardSize();
    }


    private static GameDescriptor deserializeFrom(InputStream in) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(JAXB_XML_GAME_PACKAGE_NAME);
        Unmarshaller u = jc.createUnmarshaller();
        return (GameDescriptor)u.unmarshal(in);
    }*/


























    /*




    public int getLeftBoxTiles(){
        return this.leftBoxTiles;
    }

    public boolean isValid()
    {
        Structure struct;

        return  true;

    }


    : all illiegele issues + xml loading + txt file in dictionary library
                     */
