import org.openrdf.model.*;
import org.openrdf.model.IRI;
import org.openrdf.model.impl.LinkedHashModel;
import org.openrdf.model.impl.SimpleValueFactory;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.RDFWriter;
import org.openrdf.rio.Rio;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import static org.openrdf.rio.RDFFormat.TURTLE;

/**
 * Created by aarunova on 1/22/17.
 */
public class ModelClass {

    //org.apache.jena.rdf.model.Model model = ModelFactory.createDefaultModel();
    HashMap<org.openrdf.model.Resource, org.openrdf.model.Resource> map = new HashMap<>();
    ValueFactory factory = SimpleValueFactory.getInstance();
    Model model = new LinkedHashModel();

    public ArrayList<org.apache.jena.rdf.model.Statement> writeModel(Model aGraph) throws FileNotFoundException {
        FileOutputStream out = new FileOutputStream("test.rdf");
        //FileOutputStream out2 = new FileOutputStream("test2.rdf");
        RDFWriter writer = Rio.createWriter(TURTLE, out);

        try {

            for (Statement st : aGraph) {

                if (st.getPredicate().toString().equals("odf:id") || st.getPredicate().toString().equals("odf:name")) {
                    IRI object = factory.createIRI("ex:" + st.getObject().stringValue());
                    map.put(st.getSubject(), object);
                }
            }

            for (Statement st : aGraph) {

                Resource subject = st.getSubject();
                Value object = st.getObject();

                if (map.containsKey(st.getSubject())) {
                    subject = factory.createIRI(map.get(st.getSubject()).toString());
                }
                if (map.containsKey(st.getObject())) {
                    object = factory.createIRI(map.get(st.getObject()).toString());
                }

                model.add(subject, st.getPredicate(), object);

            }

            writer.startRDF();

            for (Statement st: model) {
                writer.handleStatement(st);
            }

            writer.endRDF();

            System.out.println(map);

        }
        catch (RDFHandlerException e) {
            // oh no, do something!
        }
        return null;
    }



}
