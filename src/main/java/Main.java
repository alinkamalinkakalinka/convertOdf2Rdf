/**
 * Created by aarunova on 12/11/16.
 */

import com.complexible.pinto.RDFMapper;
import model.Objects;
import org.openrdf.model.Model;
import org.openrdf.model.Statement;
import org.openrdf.model.vocabulary.RDF;
import org.openrdf.model.vocabulary.XMLSchema;
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.RDFWriter;
import org.openrdf.rio.Rio;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {

        try {

            File file = new File("/Users/aarunova/Desktop/thesis/odfTordf/object_with_sub_objects.xml");
            JAXBContext jaxbContext = JAXBContext.newInstance(Objects.class);

            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            Objects objects= (Objects) jaxbUnmarshaller.unmarshal(file);

            //Model aGraph = RDFMapper.create().writeValue(objects);
            //aGraph.setNamespace(RDF.PREFIX, "gggg");
            Model aGraph = RDFMapper.builder()
                    .build().writeValue(objects);
            //aGraph.setNamespace(RDF.PREFIX, "http://www.w3.org/1999/02/22-rdf-syntax-ns#type");
            System.out.println(aGraph.getNamespaces());
            System.out.println(aGraph);

            FileOutputStream out = new FileOutputStream("test.rdf");
            RDFWriter writer = Rio.createWriter(RDFFormat.TURTLE, out);
            try {
                writer.startRDF();
                for (Statement st: aGraph) {
                    writer.handleStatement(st);
                }
                writer.endRDF();
            }
            catch (RDFHandlerException e) {
                // oh no, do something!
            }

            //System.out.println(objects.getObject().get(0).getObject().get(1).getId());

        } catch (JAXBException e) {
            e.printStackTrace();
        }

    }
}
