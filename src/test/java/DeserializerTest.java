import org.apache.commons.lang3.time.StopWatch;
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

        //String inputRdfFile = "src/test/resources/test/input/rdf/metadata.ttl";
        String inputRdfFile = "target/rdf/metadata.ttl";
        String outputXmlFile = "target/xml/metadata.xml";
        String targetXmlFile = "src/test/resources/test/input/xml/metadata.xml";

        StopWatch timer = new StopWatch();
        timer.start();

        deserializer.deserialize(inputRdfFile, outputXmlFile, "TTL");

        timer.stop();
        System.out.println(timer.getTime());

        XMLDiff diff = new XMLDiff(targetXmlFile, outputXmlFile);
        int result = diff.compare();

        assertTrue(result == 0);

    }

}