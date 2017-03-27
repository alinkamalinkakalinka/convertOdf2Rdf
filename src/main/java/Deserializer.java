import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import modelXml.Object;
import modelXml.Objects;
import modelXml.QlmID;

import org.apache.jena.rdf.model.*;

import org.apache.jena.vocabulary.RDF;
import utils.Loggable;
import utils.RegexHelper;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

/**
 * Created by aarunova on 3/26/17.
 */

//convert rdf to java objects

public class Deserializer implements Loggable{

    public void deserialize (String rdfFile) {

        try {

            Objects objectsClass = new Objects();
            Object objectClass = new Object();
            Collection<Object> objects = new ArrayList<>();

            Collection<Statement> statements = getStatementsFromFile(rdfFile);

            if (statements != null) {

                Collection<Resource> rootObjects = getRootObjects(statements);

                for (Resource rootObject : rootObjects) {

                    Object object = objectClass.deserialize(rootObject, statements);
                    objects.add(object);
                }

                objectsClass.setObjects(objects);


                JAXBContext contextObj = JAXBContext.newInstance(objectsClass.getClass());
                Marshaller marshallerObj = contextObj.createMarshaller();
                marshallerObj.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

                marshallerObj.marshal(objectsClass, new FileOutputStream("test10.xml"));
            }

        } catch (JAXBException e) {
            logger().warn("JAXBException", e);
        } catch (FileNotFoundException e) {
            logger().warn("FileNotFoundException", e);
        }

    }

    public Collection<Resource> getRootObjects (Collection<Statement> statements) {

        Collection<String> stringRootObjects = new ArrayList<>();
        Collection<Resource> rootObjects = new ArrayList<>();

        for (Statement statement : statements) {

            if (statement.getPredicate().equals(RDF.type) &&
                    statement.getObject().toString().contains("Object")) {
                stringRootObjects.add(statement.getSubject().toString());
            }
        }

        for (Statement statement : statements) {

            if (statement.getPredicate().toString().contains("object") &&
                    stringRootObjects.contains(statement.getObject().toString())) {
                stringRootObjects.remove(statement.getObject().toString());
            }
        }

        for (String stringRootObject : stringRootObjects) {

            Resource rootObject = ResourceFactory.createResource(stringRootObject);
            rootObjects.add(rootObject);
        }

        return rootObjects;

    }


    private Collection<Statement> getStatementsFromFile (String rdfFile) {

        try {
            InputStream inputStream = new FileInputStream(rdfFile);

            //create empty model
            Model model = ModelFactory.createDefaultModel();

            // parses in turtle format
            model.read(new InputStreamReader(inputStream), null, "TURTLE");

            //generate list of statements
            StmtIterator iterator = model.listStatements();
            Collection<Statement> statements = new ArrayList<>();

            while (iterator.hasNext()) {
                Statement stmt = iterator.nextStatement();
                statements.add(stmt);
            }

            return statements;

        } catch (java.io.IOException e) {
            logger().error("FileNotFoundException", e);

            return null;
        }

    }



}
