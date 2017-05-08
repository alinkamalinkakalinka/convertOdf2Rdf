import model.Object;
import model.Objects;
import utils.RegexHelper;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.RDFFormat;

import javax.xml.bind.JAXB;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

/**
 * Created by aarunova on 1/22/17.
 */
public class Serializer implements Loggable {

    private final static String BASE_URI = "http://eis-biotope.iais.fraunhofer.de/";

    private String hostname = "localhost";


    public Model odf2rdf(String inputFileName, String outputFileName) {

        Model modelJena = null;

        try {
            InputStream odfStructure = getClass().getResourceAsStream(inputFileName);

            Objects beans = JAXB.unmarshal(RegexHelper.getDateBetweenTags(odfStructure), Objects.class);

            String objectBaseIri = BASE_URI + hostname + "/obj/";
            String infoItemBaseIri = BASE_URI + hostname + "/infoitem/";

            modelJena = ModelFactory.createDefaultModel();

            modelJena.add(beans.serialize(objectBaseIri, infoItemBaseIri));

            RDFDataMgr.write(new FileOutputStream(outputFileName), modelJena, RDFFormat.TURTLE) ;


        } catch (IOException e) {
            logger().error("IOException", e);
        }

        return modelJena;
    }

}
