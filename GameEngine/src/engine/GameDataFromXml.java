package engine;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Struct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.lang.Character;
import java.lang.String;


import engine.jabx.schema.generated.GameDescriptor;
import engine.jabx.schema.generated.Letter;
import engine.jabx.schema.generated.Letters;
import engine.jabx.schema.generated.Structure;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;


public class GameDataFromXml {

    Map<String, Double> frequencyLetter = new HashMap<String, Double>();
    Map<String, Double> frequencyWord = new HashMap<String, Double>();
    List<Letter> letters;
    private int boardSize;
    private int numOfCubeWigs;
    private int numOfTries;
    private int totalTiles;
    private int leftBoxTiles;

    private final static String JAXB_XML_GAME_PACKAGE_NAME = "engine.jabx.schema.generated";


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
        } catch (JAXBException e) {
            e.printStackTrace();
        }

        //init board size

        try {
            gd = deserializeFrom(inputStream);
            struct = gd.getStructure();
            boardSize = struct.getBoardSize();

        } catch (JAXBException e) {
            e.printStackTrace();
        }

        // init num of cube wigs
        try {
            gd = deserializeFrom(inputStream);
            struct = gd.getStructure();
            this.numOfCubeWigs = struct.getCubeFacets();

        } catch (JAXBException e) {
            e.printStackTrace();
        }




        // init num of tries
        try {
            gd = deserializeFrom(inputStream);
            struct = gd.getStructure();
            this.numOfTries = struct.getRetriesNumber();

        } catch (JAXBException e) {
            e.printStackTrace();
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
}