import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.openrdf.model.Statement;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.HashMap;

/**
 * Created by aarunova on 2/11/17.
 */
public class ModelWraper {

    public Model wrapModel (org.openrdf.model.Model aGraph) throws FileNotFoundException {

        ModelModifier modelModifier = new ModelModifier();
        Model model = ModelFactory.createDefaultModel();

        HashMap<String, String> prefixes = new HashMap<>();
        prefixes.put("rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#");
        prefixes.put("xsd", "http://www.w3.org/2001/XMLSchema#");
        prefixes.put("ex", "http://example.org/");
        prefixes.put("dct", "http://purl.org/dc/terms/");

        model.setNsPrefixes(prefixes);


        FileOutputStream stream = new FileOutputStream("test2.rdf");

        org.openrdf.model.Model aGraphModel = modelModifier.modifyModel(aGraph);

        for (Statement st: aGraphModel){
            Resource subject = model.createResource(st.getSubject().toString());
            Property property = model.createProperty(st.getPredicate().toString());

            if (!st.getObject().stringValue().contains("odf") && !st.getObject().stringValue().contains("ex")){
                model.add(subject, property, st.getObject().stringValue());
            } else {
                Resource object = model.createResource(st.getObject().stringValue());
                model.add(subject, property, object);
            }

        }

        model.write(stream, "TTL");

        //ByteArrayInputStream inputStream = modelModifier.modifyModel(aGraph);

        //modelXml.read(inputStream, "TTL");





        return model;
    }
}
