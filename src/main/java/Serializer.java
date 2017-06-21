import model.Objects;
import org.apache.commons.io.IOUtils;
import utils.ModelHelper;
import utils.RegexHelper;
import validation.FileType;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;

import javax.xml.bind.JAXB;

import java.io.*;

/**
 * Created by aarunova on 1/22/17.
 */
public class Serializer implements Loggable {

    private final static String BASE_URI = "http://eis-biotope.iais.fraunhofer.de/";

    private String hostname = "localhost";


    public Model serialize(String inputFileName, String outputFileName, String rdfFormat) {

        Model modelJena = null;

        try {

            File temp = File.createTempFile("temp-file", ".tmp");
            temp.deleteOnExit();
            FileOutputStream out = new FileOutputStream(temp);

            InputStream in = RegexHelper.getDateBetweenTags(inputFileName);

            IOUtils.copy(in, out);

            ModelHelper.checkIfFileIsValid(temp.getAbsolutePath(), "/odf.xsd", FileType.XML);

            Objects beans = JAXB.unmarshal(RegexHelper.getDateBetweenTags(inputFileName), Objects.class);

            String objectBaseIri = BASE_URI + hostname + "/obj/";
            String infoItemBaseIri = BASE_URI + hostname + "/infoitem/";

            modelJena = ModelFactory.createDefaultModel();

            modelJena.add(beans.serialize(objectBaseIri, infoItemBaseIri));
            OutputStream fos = new FileOutputStream(outputFileName);

            modelJena.write(fos, rdfFormat) ;
            fos.close();

        } catch (IOException e) {
            logger().error("IOException", e);
        }

        return modelJena;
    }

}
