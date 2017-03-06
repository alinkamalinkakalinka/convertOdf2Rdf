import com.hp.hpl.jena.rdf.model.ModelFactory;
import modelXml.Object;
import modelXml.Objects;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Namespace;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.sail.memory.model.MemValueFactory;
import thewebsemantic.RDF2Bean;

import javax.xml.bind.JAXB;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.Set;

/**
 * Created by aarunova on 1/22/17.
 */
public class ModelModifier {

    private final static String BASE_URI = "http://eis-biotope.iais.fraunhofer.de/";

    private String hostname = "localhost";


    public Model odf2rdf() throws FileNotFoundException {
        InputStream odfStructure = getClass().getResourceAsStream("simpleOdf.xml");

        Objects beans = JAXB.unmarshal(odfStructure, Objects.class);

        org.eclipse.rdf4j.model.ValueFactory vf = new MemValueFactory();
        String baseIRI = BASE_URI + hostname;
        String objectBaseIri = BASE_URI + hostname + "/obj/";
        String infoItemBaseIri = BASE_URI + hostname + "/infoitem/";

        org.eclipse.rdf4j.model.Model model = new ModelBuilder().build();

        Collection<Object> objects = beans.getObjects();

        for (Object object : objects) {
            Model partialModel = object.serialize(vf, objectBaseIri, infoItemBaseIri);
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
