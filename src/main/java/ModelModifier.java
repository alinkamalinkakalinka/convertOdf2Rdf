import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import modelXml.Object;
import modelXml.Objects;
import org.eclipse.rdf4j.model.*;
import org.eclipse.rdf4j.model.Namespace;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.sail.memory.model.MemValueFactory;
import org.openrdf.model.*;
import org.openrdf.model.IRI;
import org.openrdf.model.Model;
import org.openrdf.model.Resource;
import org.openrdf.model.Statement;
import org.openrdf.model.Value;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.impl.LinkedHashModel;
import org.openrdf.model.impl.SimpleValueFactory;
import org.openrdf.model.vocabulary.RDF;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.RDFWriter;
import org.openrdf.rio.Rio;
import thewebsemantic.RDF2Bean;
import vocabs.NS;

import javax.xml.bind.JAXB;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import static org.openrdf.rio.RDFFormat.TURTLE;

/**
 * Created by aarunova on 1/22/17.
 */
public class ModelModifier {

    private final static String BASE_URI = "http://eis-biotope.iais.fraunhofer.de/";

    private String hostname = "localhost";


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


    public org.eclipse.rdf4j.model.Model odf2rdf() throws FileNotFoundException {
        InputStream odfStructure = getClass().getResourceAsStream("simpleOdf.xml");

        Objects beans = JAXB.unmarshal(odfStructure, Objects.class);

        org.eclipse.rdf4j.model.ValueFactory vf = new MemValueFactory();
        String baseIRI = BASE_URI + hostname;
        String objectBaseIri = BASE_URI + hostname + "/obj/";
        String infoItemBaseIri = BASE_URI + hostname + "/infoitem/";

        org.eclipse.rdf4j.model.Model model = new ModelBuilder().build();

        Collection<Object> objects = beans.getObjects();

        for (Object object : objects) {
            org.eclipse.rdf4j.model.Model partialModel = object.serialize(vf, objectBaseIri, infoItemBaseIri);
            model.addAll(partialModel);
            Set<Namespace> nameSpaces = partialModel.getNamespaces();
            nameSpaces.forEach(nameSpace -> model.setNamespace(nameSpace));

        }

        //beans.getObjects().forEach(objectBean -> model.addAll(objectBean.serialize(vf, objectBaseIri, infoItemBaseIri)));
        dumpModel(model);

        return model;
    }

    private void dumpModel(org.eclipse.rdf4j.model.Model model) throws FileNotFoundException {
        //RDFWriter rdfWriter = new TurtleWriter(System.out);

        FileOutputStream stream = new FileOutputStream("test3.rdf");
        org.eclipse.rdf4j.rio.RDFWriter rdfWriter = org.eclipse.rdf4j.rio.Rio.createWriter(RDFFormat.TURTLE, stream);

        rdfWriter.startRDF();
        Set<Namespace> nameSpaces = model.getNamespaces();
        nameSpaces.forEach(nameSpace -> rdfWriter.handleNamespace(nameSpace.getPrefix(), nameSpace.getName()));
        model.forEach(statment -> rdfWriter.handleStatement(statment));
        rdfWriter.endRDF();
    }

    public Collection<Objects> convert () throws FileNotFoundException, MalformedURLException {
        URL url=getClass().getClassLoader().getResource("test3.rdf");
        String path=url.toString();

        FileOutputStream stream = new FileOutputStream("test3.rdf");
        //final OntModel m = ModelFactory.createOntologyModel();
        //m.write(stream);

        com.hp.hpl.jena.rdf.model.Model m = ModelFactory.createDefaultModel() ;
        m.read(new File("test3.rdf").toURL().toString()) ;

        final RDF2Bean reader = new RDF2Bean(m);
        final Collection<Objects> objectsCollection = reader.load(Objects.class);

        return objectsCollection;

    }


}
