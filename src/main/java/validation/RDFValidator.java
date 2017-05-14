package validation;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdfxml.xmlinput.ParseException;
import org.apache.jena.riot.RiotException;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import utils.Loggable;

/**
 * Created by aarunova on 5/14/17.
 */
public class RDFValidator implements SchemaValidator, Loggable {


    @Override
    public boolean validateAgainstSchema(String source) {

        try {
            InputStream inputStream = new FileInputStream(source);

            //create empty model
            Model model = ModelFactory.createDefaultModel();

            // parses in turtle format
            model.read(new InputStreamReader(inputStream), null, "TURTLE");

        } catch (IOException e) {
            logger().warn("IOException", e);
            return false;
        } catch (RiotException e) {
            logger().error("RiotException");
            return false;
        }

        return true;
    }

    @Override
    public FileType getFileType() {
        return FileType.RDF;
    }
}
