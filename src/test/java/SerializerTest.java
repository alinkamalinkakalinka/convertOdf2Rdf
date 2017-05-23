import org.castor.xmlctf.xmldiff.XMLDiff;
import org.custommonkey.xmlunit.XMLTestCase;
import org.junit.Test;

import utils.RDFDiff;

/**
 * Created by aarunova on 5/8/17.
 */
public class SerializerTest extends XMLTestCase {

    @Test
    public void testOdf2rdf() throws Exception {

        Serializer serializer = new Serializer();

        Deserializer deserializer = new Deserializer();

        String xmlFile = "test/input/xml/metadata.xml";

        serializer.serialize("src/test/resources/test/input/xml/metadata.xml", "target/rdf/metadata.ttl");
        deserializer.deserialize(getClass().getClassLoader().getResource("test/input/rdf/metadata_about_refrigerator_power.ttl").getFile(), "target/xml/metadata.xml");

        XMLDiff diff = new XMLDiff("src/test/resources/test/input/xml/metadata.xml", "target/xml/metadata.xml");
        int result = diff.compare();
        System.out.println(result);

        boolean ifEqual = RDFDiff.diff("target/rdf/metadata.ttl", "target/rdf/metadata.ttl");
        System.out.println(ifEqual);
        }

}