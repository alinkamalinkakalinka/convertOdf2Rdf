package evaluation;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.vocabulary.RDF;
import org.junit.Test;
import vocabs.ODFClass;
import vocabs.ODFProp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alina on 6/5/2017.
 */
public class RdfGenerator {

    @Test
    public void generateRdf() {

        Model model = ModelFactory.createDefaultModel();

        model.add(ResourceFactory.createResource("http://eis-biotope.iais.fraunhofer.de/localhost/obj/29229920"),
                RDF.type,
                ODFClass.OBJECTS);

        for (int i=1; i<100000; i++) {

            //model.add(ResourceFactory.createResource("http://eis-biotope.iais.fraunhofer.de/localhost/obj/29229920"),
                    //ODFProp.OBJECT,
                    //ResourceFactory.createResource("http://eis-biotope.iais.fraunhofer.de/localhost/obj/29229920/SmartFridge" + i));


        }

    }
}
