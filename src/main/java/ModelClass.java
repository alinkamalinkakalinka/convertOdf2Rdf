import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.openrdf.model.Statement;
import org.openrdf.model.vocabulary.RDF;
import org.openrdf.query.algebra.Lang;
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.RDFWriter;
import org.openrdf.rio.Rio;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by aarunova on 1/22/17.
 */
public class ModelClass {

    private static final AtomicInteger objectCount = new AtomicInteger(0);
    private static final AtomicInteger infoItemCount = new AtomicInteger(0);
    private static final AtomicInteger objectsCount = new AtomicInteger(0);
    private static final AtomicInteger valueCount = new AtomicInteger(0);

    static org.apache.jena.rdf.model.Model model = ModelFactory.createDefaultModel();
    static HashMap<String, Resource> map = new HashMap<>();

    public static void writeModel(org.openrdf.model.Model aGraph) throws FileNotFoundException {
        FileOutputStream out = new FileOutputStream("test.rdf");
        RDFWriter writer = Rio.createWriter(RDFFormat.TURTLE, out);
        // create an empty Model


        try {
            writer.startRDF();
            for (Statement st: aGraph) {

                if (st.getObject().stringValue().equals("odf:Object") && st.getPredicate().equals(RDF.TYPE)) {
                    adjustStatement(st, "ex:Object", objectCount);
                }

                if (st.getObject().stringValue().equals("odf:InfoItem") && st.getPredicate().equals(RDF.TYPE)) {
                    adjustStatement(st, "ex:InfoItem", infoItemCount);

                }

                if (st.getObject().stringValue().equals("odf:Objects") && st.getPredicate().equals(RDF.TYPE)) {
                    adjustStatement(st, "ex:Objects", objectsCount);

                }

                if (st.getObject().stringValue().equals("odf:Value") && st.getPredicate().equals(RDF.TYPE)) {
                    adjustStatement(st, "ex:Value", valueCount);

                }


                writer.handleStatement(st);
            }
            writer.endRDF();

            for (Statement st: aGraph) {
                for (String key : map.keySet()) {

                    if (st.getObject().stringValue().equals(key)) {

                        Resource subject1 = model.createResource(st.getSubject().stringValue());
                        Property property1 = model.createProperty(st.getPredicate().toString());
                        model.add(subject1, property1, map.get(key).toString());
                    }

                    if (st.getSubject().stringValue().equals(key)) {

                        Resource subject1 = model.createResource(map.get(key).toString());
                        Property property1 = model.createProperty(st.getPredicate().toString());
                        model.add(subject1, property1, st.getObject().stringValue());
                    }
                }
            }

            model.write(System.out, "TTL");
        }
        catch (RDFHandlerException e) {
            // oh no, do something!
        }
    }

    private static void adjustStatement(Statement st, String string, AtomicInteger count) {
        // create the resource
        Resource subject = model.createResource(string + count.incrementAndGet());
        Property property = model.createProperty(st.getPredicate().toString());

        map.put(st.getSubject().toString(), subject);

        model.add(subject, property, st.getObject().stringValue());
    }
}
