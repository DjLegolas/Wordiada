
package engine.jabx.schema;


//import examples.jaxb.schema.generated.Countries;

import engine.jabx.schema.generated.GameDescriptor;
import engine.jabx.schema.generated.Letters;
import engine.jabx.schema.generated.Letter;
import engine.jabx.schema.generated.Structure;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.InputStream;

public class SchemaBasedJAXBMain {

    private final static String JAXB_XML_GAME_PACKAGE_NAME = "engine.jabx.schema.generated";

    public static void main(String[] args) {
        InputStream inputStream = SchemaBasedJAXBMain.class.getResourceAsStream("/resources/master.xml");
        try {
            GameDescriptor gd = deserializeFromletter(inputStream);
            Structure struct = gd.getStructure();
            Letters letters = struct.getLetters();
          //  System.out.println("sign of first letter is: " + letters.getLetter().get(0).getSign());
            for(int i = 0; i < letters.getLetter().size(); i++){
                System.out.println("letter number" + i + ": " + letters.getLetter().get(i).getSign());
            }
            System.out.println("Board size:" + struct.getBoardSize() );

        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }
    private static GameDescriptor deserializeFromletter(InputStream in) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(JAXB_XML_GAME_PACKAGE_NAME);
        Unmarshaller u = jc.createUnmarshaller();
        return (GameDescriptor)u.unmarshal(in);
    }




}

