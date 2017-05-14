import model.Object;
import model.Objects;
import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.RDF;
import utils.Loggable;
import utils.ModelHelper;
import validation.FileType;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.*;
import java.util.ArrayList;
import java.util.Collection;

import static utils.ModelHelper.getStatementsFromFile;

/**
 * Created by aarunova on 3/26/17.
 */

//convert rdf to java objects

public class Deserializer implements Loggable{

    public void deserialize (String rdfFile, String xmlFilePath) {

        try {

            ModelHelper.checkIfFileIsValid(rdfFile, "/odf.xsd", FileType.RDF);

            Objects objectsClass = new Objects();

            Collection<Statement> statements = getStatementsFromFile(rdfFile);

            if (statements != null) {

                objectsClass = objectsClass.deserialize(statements);

                JAXBContext contextObj = JAXBContext.newInstance(objectsClass.getClass());
                Marshaller marshallerObj = contextObj.createMarshaller();
                marshallerObj.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

                marshallerObj.marshal(objectsClass, new FileOutputStream(xmlFilePath));
            }

        } catch (JAXBException e) {
            logger().warn("JAXBException", e);
        } catch (FileNotFoundException e) {
            logger().warn("FileNotFoundException", e);
        }

    }



}
