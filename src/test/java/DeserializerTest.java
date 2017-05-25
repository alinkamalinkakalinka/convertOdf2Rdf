import org.castor.xmlctf.xmldiff.XMLDiff;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Alina on 5/20/2017.
 */
public class DeserializerTest {

    @Test
    public void testRdf2odf() throws Exception {

        Deserializer deserializer = new Deserializer();

        String inputRdfFile = "src/test/resources/test/input/rdf/metadata.ttl";
        String outputXmlFile = "target/rdf/metadata.ttl";
        String targetXmlFile = "src/test/resources/test/input/xml/metadata.xml";

        deserializer.deserialize(inputRdfFile, outputXmlFile);

        XMLDiff diff = new XMLDiff(targetXmlFile, outputXmlFile);
        int result = diff.compare();

        assertTrue(result == 0);

    }

}