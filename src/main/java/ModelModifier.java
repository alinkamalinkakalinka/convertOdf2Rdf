import org.openrdf.model.*;
import org.openrdf.model.IRI;
import org.openrdf.model.impl.LinkedHashModel;
import org.openrdf.model.impl.SimpleValueFactory;
import org.openrdf.model.vocabulary.RDF;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.RDFWriter;
import org.openrdf.rio.Rio;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.HashMap;

import static org.openrdf.rio.RDFFormat.TURTLE;

/**
 * Created by aarunova on 1/22/17.
 */
public class ModelModifier {



    public Model modifyModel(Model aGraph) throws FileNotFoundException {

        HashMap<Resource, Resource> mapInfoItem = new HashMap<>();
        HashMap<String , String> mapValues = new HashMap<>();
        HashMap<Resource, String> mapId = new HashMap<>();
        ValueFactory factory = SimpleValueFactory.getInstance();
        Model model = new LinkedHashModel();
        Model model2 = new LinkedHashModel();

        FileOutputStream stream = new FileOutputStream("test.rdf");
        RDFWriter writer = Rio.createWriter(TURTLE, stream);

        try {

            for (Statement st : aGraph) {

                if (st.getPredicate().toString().equals("odf:name")) {
                    IRI object = factory.createIRI("ex:" + st.getObject().stringValue());
                    mapInfoItem.put(st.getSubject(), object);
                }

                if (st.getPredicate().toString().equals("dct:id")) {
                    mapId.put(st.getSubject(), st.getObject().stringValue());
                }

            }

            for (Statement st : aGraph) {

                Resource subject = st.getSubject();
                Value object = st.getObject();

                if (!st.getPredicate().toString().equals("odf:name")) {

                    if (mapInfoItem.containsKey(st.getSubject())) {
                        subject = factory.createIRI(mapInfoItem.get(st.getSubject()).toString());
                    }
                    if (mapInfoItem.containsKey(st.getObject())) {
                        object = factory.createIRI(mapInfoItem.get(st.getObject()).toString());
                    }

                    if (mapId.containsKey(st.getSubject())) {
                        subject = factory.createIRI("ex:" + mapId.get(st.getSubject()).toString() + "_id");
                    }
                    if (mapId.containsKey(st.getObject())) {
                        object = factory.createIRI("ex:" + mapId.get(st.getObject()) + "_id");
                    }

                    //if (!subject.stringValue().contains("pinto") && !object.stringValue().contains("odf:Objects")) {
                    model.add(subject, st.getPredicate(), object);
                    ///}
                }

            }

            for (Statement st: model) {

                if (st.getSubject().toString().contains("pinto") && st.getPredicate().equals(RDF.VALUE)) {
                    mapValues.put(st.getSubject().toString(), "ex:" + st.getObject().stringValue());
                }

                if (st.getPredicate().toString().equals("odf:id") && st.getObject().toString().contains("_id")) {
                    mapId.put(st.getSubject(), st.getObject().toString().replace("ex:", "").replace("_id", ""));
                }

            }

            System.out.println(mapValues);

            for (Statement st: model) {

                Resource subject = st.getSubject();
                Value object = st.getObject();

                if (!st.getPredicate().toString().equals("time:unixTime") && !st.getObject().toString().equals("0")) {


                    if (mapValues.containsKey(st.getSubject().toString())) {
                        subject = factory.createIRI(mapValues.get(st.getSubject().toString()));
                    }
                    if (mapValues.containsKey(st.getObject().toString())) {
                        object = factory.createIRI(mapValues.get(st.getObject().toString()));
                    }

                    if (mapId.containsKey(st.getSubject())) {
                        subject = factory.createIRI("ex:" + mapId.get(st.getSubject()).toString());
                    }
                    if (mapId.containsKey(st.getObject())) {
                        object = factory.createIRI("ex:" + mapId.get(st.getObject()).toString());
                    }

                    //if (!subject.stringValue().contains("pinto") && !object.stringValue().contains("odf:Objects")) {
                    model2.add(subject, st.getPredicate(), object);
                    ///}
                }

            }

            writer.startRDF();

            for (Statement st: model2) {
                writer.handleStatement(st);
            }

            writer.endRDF();


        }
        catch (RDFHandlerException e) {
            // oh no, do something!
        }
        return model2;
    }



}
