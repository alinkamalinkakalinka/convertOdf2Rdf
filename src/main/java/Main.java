/**
 * Created by aarunova on 12/11/16.
 */

import model.Objects;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

public class Main {


    private final Helper helper = new Helper();

    public static void main(String[] args) throws FileNotFoundException, MalformedURLException {

        try {

            Helper helper = new Helper();

            String fileName = "infoitem_values.xml";

            File file = helper.getFile(fileName);

            JAXBContext jaxbContext = JAXBContext.newInstance(Objects.class);

            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            Objects objects= (Objects) jaxbUnmarshaller.unmarshal(file);


            Serializer serializer = new Serializer();
            //serializer.odf2rdf("infoitem_values.xml", "testJena1.rdf");
            serializer.odf2rdf("smarthouse_omi.xml", "testJena2.rdf");

            //InputStream odfStructure = getClass().getResourceAsStream("/resources/infoitem_values.xml");
            //serializer.odf2rdf();

            Deserializer deserializer = new Deserializer();
            deserializer.deserialize("testJena.rdf", "test10.xml");


            //FileInputStream stream = new FileInputStream(new File("test3.rdf").toURL().toString());

            //Collection<Objects> objectsCollection = serializer.convert();



        } catch (JAXBException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
