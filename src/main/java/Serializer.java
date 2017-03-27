import modelXml.Object;
import modelXml.Objects;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.RDFDataMgr;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Namespace;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.sail.memory.model.MemValueFactory;

import javax.xml.bind.JAXB;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Collection;
import java.util.Set;

/**
 * Created by aarunova on 1/22/17.
 */
public class Serializer {

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
        org.apache.jena.rdf.model.Model modelJena = ModelFactory.createDefaultModel();

        Collection<Object> objects = beans.getObjects();

        for (Object object : objects) {
            Model partialModel = object.serialize(vf, objectBaseIri, infoItemBaseIri);
            org.apache.jena.rdf.model.Model partialModelJena = object.serialize(objectBaseIri, infoItemBaseIri);
            modelJena.add(partialModelJena);
            model.addAll(partialModel);
            Set<Namespace> nameSpaces = partialModel.getNamespaces();
            nameSpaces.forEach(nameSpace -> model.setNamespace(nameSpace));

        }

        RDFDataMgr.write(new FileOutputStream("testJena1.rdf"), modelJena, org.apache.jena.riot.RDFFormat.TURTLE) ;
        //beans.getObjects().forEach(objectBean -> model.addAll(objectBean.serialize(vf, objectBaseIri, infoItemBaseIri)));
        dumpModel(model);

        return model;
    }

    private void dumpModel(org.eclipse.rdf4j.model.Model model) throws FileNotFoundException {
        //RDFWriter rdfWriter = new TurtleWriter(System.out);

        FileOutputStream stream = new FileOutputStream("test4.rdf");
        org.eclipse.rdf4j.rio.RDFWriter rdfWriter = org.eclipse.rdf4j.rio.Rio.createWriter(RDFFormat.TURTLE, stream);

        rdfWriter.startRDF();
        Set<Namespace> nameSpaces = model.getNamespaces();
        nameSpaces.forEach(nameSpace -> rdfWriter.handleNamespace(nameSpace.getPrefix(), nameSpace.getName()));
        model.forEach(statment -> rdfWriter.handleStatement(statment));
        rdfWriter.endRDF();
    }


}
