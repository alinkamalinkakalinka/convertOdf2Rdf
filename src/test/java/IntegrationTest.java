import org.castor.xmlctf.xmldiff.XMLDiff;
import org.junit.Test;
import utils.FileToStringConverter;

import static org.junit.Assert.assertTrue;

/**
 * Created by Alina on 5/21/2017.
 */
public class IntegrationTest {

    @Test
    public void testApplication () throws Exception {

        Main main = new Main();

        String inputFilepathXml = getClass().getClassLoader().getResource("integrationtest/evervent_omi.xml").getFile();

        String outputFilepathRdf = "target/rdf/evervent_omi.ttl";

        String outputFilepathXml = "target/xml/evervent_omi.xml";

        String[] args1 = new String[] { "odf2rdf", inputFilepathXml, outputFilepathRdf };

        main.main(args1);

        String[] args2 = new String[] { "rdf2odf", outputFilepathRdf, outputFilepathXml };

        main.main(args2);

        XMLDiff diff = new XMLDiff(inputFilepathXml, outputFilepathXml);
        int result = diff.compare();

        assertTrue(result == 0);
    }

}
