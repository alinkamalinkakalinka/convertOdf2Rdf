/**
 * Created by aarunova on 12/11/16.
 */

import com.complexible.pinto.RDFMapper;
import modelXml.Objects;
import org.openrdf.model.Model;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileNotFoundException;
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

            Model aGraph = RDFMapper.builder()
                    .build().writeValue(objects);

            ModelModifier modelModifier = new ModelModifier();

            //InputStream odfStructure = getClass().getResourceAsStream("/resources/infoitem_values.xml");
            //modelModifier.odf2rdf();

            Deserializer deserializer = new Deserializer();
            deserializer.deserialize("test3.rdf");


            //FileInputStream stream = new FileInputStream(new File("test3.rdf").toURL().toString());

            //Collection<Objects> objectsCollection = modelModifier.convert();



        } catch (JAXBException e) {
            e.printStackTrace();
        }


    }


}
