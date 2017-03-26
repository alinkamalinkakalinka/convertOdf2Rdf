import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import modelXml.Object;
import modelXml.Objects;
import modelXml.QlmID;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;

import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
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

    public Object deserialize (String rdfFile, String baseOdfURI) {

        try {

            Objects objectsClass = new Objects();

            Collection<Statement> statements = getStatementsFromFile(rdfFile);

            Objects objects = objectsClass.deserialize(statements, baseOdfURI);

            //Collection<Object> objectCollection = getBaseObjects(statements, baseOdfURI);

            JAXBContext contextObj = JAXBContext.newInstance(objects.getClass());
            Marshaller marshallerObj = contextObj.createMarshaller();
            marshallerObj.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            marshallerObj.marshal(objects, new FileOutputStream("test10.xml"));

        } catch (JAXBException e) {
            logger().warn("JAXBException", e);
        } catch (FileNotFoundException e) {
            logger().warn("FileNotFoundException", e);
        }

        return null;

    }

    //look for main parents
    private Collection<Object> getBaseObjects(Collection<Statement> statements, String baseOdfURI) {

        Collection<Object> objectCollection = new ArrayList<>();

        Collection<String> objectIds = new ArrayList<>();

        if (statements != null) {

            //look for parents (objects)
            for (Statement statement : statements) {

                if (statement.getPredicate().equals(RDF.type)
                        && (baseOdfURI + "Object").equals(statement.getObject().toString())) {

                    String id = RegexHelper.getObjectName(statement.getSubject().toString());
                    objectIds.add(id);
                }
            }

            for (Statement statement : statements) {

                if (statement.getPredicate().toString().equals(baseOdfURI + "object")) {

                    String id = RegexHelper.getObjectName(statement.getObject().toString());
                    if (objectIds.contains(id)) {
                        objectIds.remove(id);
                    }
                }

            }

            // set id Properties method
            for (String id : objectIds) {

                Object object = new Object();
                QlmID qlmID = new QlmID();
                qlmID.setId(id);
                object.setId(qlmID);
                objectCollection.add(object);
            }


        }

        return objectCollection;
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
