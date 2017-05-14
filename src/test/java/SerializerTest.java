import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.RDFVisitor;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.castor.xmlctf.xmldiff.XMLDiff;
import org.custommonkey.xmlunit.XMLAssert;
import org.custommonkey.xmlunit.XMLTestCase;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import utils.RDFDiff;
import validation.RDFValidator;

/**
 * Created by aarunova on 5/8/17.
 */
public class SerializerTest extends XMLTestCase {

    @Test
    public void testOdf2rdf() throws Exception {

        Serializer serializer = new Serializer();

        Deserializer deserializer = new Deserializer();

        String xmlFile = "test/input/xml/metadata.xml";

        serializer.odf2rdf(xmlFile, "src/target/rdf/metadata.ttl");
        deserializer.deserialize(getClass().getClassLoader().getResource("test/input/rdf/metadata_about_refrigerator_power.ttl").getFile(), "src/target/xml/metadata.xml");

        XMLDiff diff = new XMLDiff("src/test/resources/test/input/xml/metadata.xml", "src/target/xml/metadata.xml");
        int result = diff.compare();
        System.out.println(result);

        boolean ifEqual = RDFDiff.diff("src/target/rdf/metadata.ttl", "src/target/rdf/metadata.ttl");
        System.out.println(ifEqual);

        }

}