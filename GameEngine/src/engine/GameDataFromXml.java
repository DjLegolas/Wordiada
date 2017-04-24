package engine;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Struct;
import java.util.*;
import java.lang.Character;
import java.lang.String;
import  java.math.RoundingMode;
import  java.math.BigDecimal;



import engine.jabx.schema.generated.GameDescriptor;
import engine.jabx.schema.generated.Letter;
import engine.jabx.schema.generated.Letters;
import engine.jabx.schema.generated.Structure;

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
    String dictFileName;

    private final static String JAXB_XML_GAME_PACKAGE_NAME = "engine.jabx.schema.generated";

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
            dictFileName = struct.getDictionaryFileName();
            calcRatiofrequencyLetter(frequencyLetter);

        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    public String getDictFileName() {
        return dictFileName;
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
