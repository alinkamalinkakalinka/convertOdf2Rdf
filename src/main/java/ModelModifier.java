import org.apache.jena.rdf.model.RDFNode;
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
        HashMap<String , String> mapValues = new HashMap<>();
        ValueFactory factory = SimpleValueFactory.getInstance();
        Model model = new LinkedHashModel();
        Model model2 = new LinkedHashModel();

        //ByteArrayOutputStream stream = new ByteArrayOutputStream();
        FileOutputStream stream = new FileOutputStream("test.rdf");
        RDFWriter writer = Rio.createWriter(TURTLE, stream);

        try {

            for (Statement st : aGraph) {

                if (st.getPredicate().toString().equals("odf:id")
                        || st.getPredicate().toString().equals("odf:name")) {
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

                    //if (!subject.stringValue().contains("pinto") && !object.stringValue().contains("odf:Objects")) {
                    model.add(subject, st.getPredicate(), object);
                    ///}
                }

            }




            for (Statement st: model) {

                if (st.getSubject().toString().contains("pinto") && st.getPredicate().toString().equals("odf:value")) {
                    mapValues.put(st.getSubject().toString(), st.getObject().stringValue());
                }
            }

            for (Statement st: model) {

                //if (!mapValues.containsKey(st.getSubject().toString())) {

                if (mapValues.containsKey(st.getObject().toString())
                        && !st.getSubject().toString().contains("pinto")
                        && st.getPredicate().toString().equals("odf:value")) {

                    model2.add(st.getSubject(), st.getPredicate(), factory.createLiteral(mapValues.get(st.getObject().toString())));
                } else {
                    model2.add(st);
                }

                //}

            }

            //System.out.println(mapValues);


            writer.startRDF();

            for (Statement st: model2) {
                writer.handleStatement(st);
            }

            writer.endRDF();


            //System.out.println(map);

        }
        catch (RDFHandlerException e) {
            // oh no, do something!
        }
        return model2;
        //new ByteArrayInputStream(stream.toByteArray());
    }



}
