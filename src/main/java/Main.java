/**
 * Created by aarunova on 12/11/16.
 */

import com.complexible.pinto.RDFMapper;
//import modelRdf.ObjectsRdf;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import modelRdf.ObjectsRdf;
import modelXml.Objects;

import org.openrdf.model.Model;
import org.openrdf.model.impl.SimpleValueFactory;
import thewebsemantic.RDF2Bean;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.net.MalformedURLException;
import java.util.Collection;

public class Main {


    private final Helper helper = new Helper();

    public static void main(String[] args) throws FileNotFoundException, MalformedURLException {

        try {

            Helper helper = new Helper();

            String fileName = "infoitem_values.xml";

            File file = helper.getFile(fileName);

            JAXBContext jaxbContext = JAXBContext.newInstance(Objects.class);

            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            Objects objects= (Objects) jaxbUnmarshaller.unmarshal(file);

            Model aGraph = RDFMapper.builder()
                    .build().writeValue(objects);

            ModelModifier modelModifier = new ModelModifier();
            ModelWraper modelWraper = new ModelWraper();

            Model aGraph2 = modelModifier.modifyModel(aGraph);
            modelWraper.wrapModel(aGraph);


            //TODO
            //step: convert rdf to java beans by means of pinto library
            //here I get only Identifiable object but id and object are null.
            //Unsure regarding creating a new model for converting back to xml (modelRdf), maybe the same fits too (modelXml)

            ObjectsRdf objects1 = RDFMapper.create().readValue(aGraph,
                    ObjectsRdf.class,
                    SimpleValueFactory.getInstance().createIRI("ex:UniqueTargetID_1"));

            System.out.println(objects1);

            //InputStream odfStructure = getClass().getResourceAsStream("/resources/infoitem_values.xml");
            modelModifier.odf2rdf();


            //FileInputStream stream = new FileInputStream(new File("test3.rdf").toURL().toString());

            //Collection<Objects> objectsCollection = modelModifier.convert();



        } catch (JAXBException e) {
            e.printStackTrace();
        }


    }


}
