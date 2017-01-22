/**
 * Created by aarunova on 12/11/16.
 */

import com.complexible.pinto.RDFMapper;
import model.Objects;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.openrdf.model.Model;
import org.openrdf.model.Statement;
import org.openrdf.model.vocabulary.RDF;
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
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {



    public static void main(String[] args) throws FileNotFoundException {

        try {

            File file = new File("/Users/aarunova/Desktop/thesis/odfTordf/object_with_sub_objects.xml");
            JAXBContext jaxbContext = JAXBContext.newInstance(Objects.class);

            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            Objects objects= (Objects) jaxbUnmarshaller.unmarshal(file);

            Model aGraph = RDFMapper.builder()
                    .build().writeValue(objects);


            ModelClass.writeModel(aGraph);


        } catch (JAXBException e) {
            e.printStackTrace();
        }

    }


}
