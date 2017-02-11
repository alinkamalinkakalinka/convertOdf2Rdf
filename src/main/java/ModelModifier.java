import org.openrdf.model.*;
import org.openrdf.model.IRI;
import org.openrdf.model.impl.LinkedHashModel;
import org.openrdf.model.impl.SimpleValueFactory;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.RDFWriter;
import org.openrdf.rio.Rio;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.HashMap;

import static org.openrdf.rio.RDFFormat.TURTLE;

/**
 * Created by aarunova on 1/22/17.
 */
public class ModelModifier {



    public Model modifyModel(Model aGraph) throws FileNotFoundException {

        HashMap<Resource, Resource> map = new HashMap<>();
        ValueFactory factory = SimpleValueFactory.getInstance();
        Model model = new LinkedHashModel();

        model.setNamespace("ex", "http://example.org/");

        //ByteArrayOutputStream stream = new ByteArrayOutputStream();
        FileOutputStream stream = new FileOutputStream("test.rdf");
        RDFWriter writer = Rio.createWriter(TURTLE, stream);

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

                if (!st.getPredicate().toString().equals("odf:id")
                        && !st.getPredicate().toString().equals("odf:name")) {

                    if (map.containsKey(st.getSubject())) {
                        subject = factory.createIRI(map.get(st.getSubject()).toString());
                    }
                    if (map.containsKey(st.getObject())) {
                        object = factory.createIRI(map.get(st.getObject()).toString());
                    }

                    if (!subject.stringValue().contains("pinto")) {
                        model.add(subject, st.getPredicate(), object);
                    }
                }

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
        return model;
                //new ByteArrayInputStream(stream.toByteArray());
    }



}
