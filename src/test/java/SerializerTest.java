import org.junit.Test;

import utils.RDFDiff;

import static org.junit.Assert.assertTrue;

/**
 * Created by aarunova on 5/8/17.
 */
public class SerializerTest {

    @Test
    public void testOdf2rdf() throws Exception {

        Serializer serializer = new Serializer();

        String inputXmlFile = "src/test/resources/test/input/xml/metadata.xml";
        String outputRdfFile = "target/rdf/metadata.ttl";
        String targetRdfFile = "src/test/resources/test/input/rdf/metadata.ttl";

        serializer.serialize(inputXmlFile, outputRdfFile);

        boolean ifEqual = RDFDiff.diff(targetRdfFile, outputRdfFile);
        assertTrue(ifEqual);
        }

}