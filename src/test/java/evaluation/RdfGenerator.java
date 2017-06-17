package evaluation;

import org.apache.jena.datatypes.RDFDatatype;
import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.RDF;
import org.junit.Test;
import vocabs.NS;
import vocabs.ODFClass;
import vocabs.ODFProp;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Alina on 6/5/2017.
 */
public class RdfGenerator {

    @Test
    public void generateRdf() throws IOException {

        Model model = ModelFactory.createDefaultModel();
        HashMap<String, String> prefixes = new HashMap<>();
        prefixes.put("ca", NS.CA);
        prefixes.put("odf", NS.ODF);
        prefixes.put("dct", NS.DCT);
        prefixes.put("rdf", NS.RDF);
        prefixes.put("xsd", NS.XSD);

        model.setNsPrefixes(prefixes);

        model.add(ResourceFactory.createResource("http://eis-biotope.iais.fraunhofer.de/localhost/obj/29229920"),
                RDF.type,
                ResourceFactory.createResource(NS.ODF + ODFClass.OBJECTS));

        Resource objects = ResourceFactory.createResource("http://eis-biotope.iais.fraunhofer.de/localhost/obj/29229920");

        for (int i=1; i<2; i++) {

            Resource object = ResourceFactory.createResource("http://eis-biotope.iais.fraunhofer.de/localhost/obj/29229920/SmartFridge" + i);

            model.add(objects,
                    ResourceFactory.createProperty(NS.ODF + ODFProp.OBJECT)                                                 ,
                    object);

            model.add(object, RDF.type, ResourceFactory.createResource(NS.ODF + ODFClass.OBJECT));

            Resource id = ResourceFactory.createResource();

            model.add(object,
                    ResourceFactory.createProperty(NS.ODF + ODFProp.ID),
                    id);

            model.add(id, RDF.type, ResourceFactory.createResource(NS.ODF + ODFClass.QLMID));

            model.add(id,
                    ResourceFactory.createProperty(NS.ODF + ODFProp.IDVALUE),
                    "SmartFridge" + i);

            Resource infoItem = ResourceFactory.createResource("http://eis-biotope.iais.fraunhofer.de/localhost/infoitem/7147540/SmartFridge1/PowerConsumption" + i);

            model.add(object,
                    ResourceFactory.createProperty(NS.ODF + ODFProp.INFOITEM),
                    infoItem);

            model.add(infoItem, RDF.type, ResourceFactory.createResource(NS.ODF + ODFClass.INFOITEM));
            model.add(infoItem,
                    ResourceFactory.createProperty(NS.DCT + ODFProp.NAME),
                    "PowerConsumption" + i);

            Resource metaData = ResourceFactory.createResource();

            model.add(infoItem,
                    ResourceFactory.createProperty(NS.ODF + ODFProp.METADATA),
                    metaData);

            model.add(metaData, RDF.type, ResourceFactory.createResource(NS.ODF + ODFClass.METADATA));

            Resource infoItem2 = ResourceFactory.createResource("http://eis-biotope.iais.fraunhofer.de/localhost/infoitem/7147540/SmartFridge1/PowerConsumption1/20224162/accuracy" + i);
            model.add(metaData,
                    ResourceFactory.createProperty(NS.ODF + ODFProp.INFOITEM),
                    infoItem2);

            model.add(infoItem2, RDF.type, ResourceFactory.createResource(NS.ODF + ODFClass.INFOITEM));
            model.add(infoItem2,
                    ResourceFactory.createProperty(NS.DCT + ODFProp.NAME),
                    "accuracy" + i);

            Resource value = ResourceFactory.createResource();

            model.add(infoItem2,
                    ResourceFactory.createProperty(NS.ODF + ODFProp.VALUE),
                    value);

            model.add(value, RDF.type, ResourceFactory.createResource(NS.ODF + ODFClass.VALUE));
            model.add(value,
                    ResourceFactory.createProperty(NS.ODF + ODFProp.DATAVALUE),
                    ResourceFactory.createTypedLiteral("1", new XSDDatatype("double", Double.class)));
        }

        String outputFileName = "test.ttl";
        OutputStream fos = new FileOutputStream(outputFileName);

        model.write(fos, "TTL") ;
        fos.close();

    }
}
