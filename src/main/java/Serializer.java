import model.Object;
import model.Objects;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.RDFFormat;

import javax.xml.bind.JAXB;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Collection;

/**
 * Created by aarunova on 1/22/17.
 */
public class Serializer {

    private final static String BASE_URI = "http://eis-biotope.iais.fraunhofer.de/";

    private String hostname = "localhost";


    public Model odf2rdf() throws FileNotFoundException {
        InputStream odfStructure = getClass().getResourceAsStream("infoitem_values.xml");

        Objects beans = JAXB.unmarshal(odfStructure, Objects.class);

        String baseIRI = BASE_URI + hostname;
        String objectBaseIri = BASE_URI + hostname + "/obj/";
        String infoItemBaseIri = BASE_URI + hostname + "/infoitem/";

        Model modelJena = ModelFactory.createDefaultModel();

        modelJena.add(beans.serialize(objectBaseIri, infoItemBaseIri));


        /*for (Object object : objects) {
            Model partialModelJena = object.serialize(objectBaseIri, infoItemBaseIri);
            modelJena.add(partialModelJena);
        }*/

        RDFDataMgr.write(new FileOutputStream("testJena1.rdf"), modelJena, RDFFormat.TURTLE) ;
        //beans.getObjects().forEach(objectBean -> model.addAll(objectBean.serialize(vf, objectBaseIri, infoItemBaseIri)));

        return modelJena;
    }

}
