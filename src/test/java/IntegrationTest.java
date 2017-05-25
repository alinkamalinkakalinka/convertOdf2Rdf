import org.apache.commons.io.IOUtils;
import org.castor.xmlctf.xmldiff.XMLDiff;
import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.diff.Diff;
import org.junit.Test;
import utils.FileToStringConverter;
import utils.RegexHelper;

import java.io.*;

import static org.junit.Assert.assertTrue;

/**
 * Created by Alina on 5/21/2017.
 */
public class IntegrationTest {

    //test InfoItem, Value, Id, Object, nested Object, optional attributes for id
    @Test
    public void testApplication1 () throws Exception {

        String inputFilepathXml = getClass().getClassLoader().getResource("integrationtest/evervent_omi.xml").getFile();
        String outputFilepathRdf = "target/rdf/evervent_omi.ttl";
        String outputFilepathXml = "target/xml/evervent_omi.xml";

        integrationTest(inputFilepathXml, outputFilepathRdf, outputFilepathXml);
    }

    //optional attributes for Object and InfoItem
    @Test
    public void testApplication2 () throws Exception {

        String inputFilepathXml = getClass().getClassLoader().getResource("integrationtest/object_with_sub_objects.xml").getFile();
        String outputFilepathRdf = "target/rdf/object_with_sub_objects.ttl";
        String outputFilepathXml = "target/xml/object_with_sub_objects.xml";

        integrationTest(inputFilepathXml, outputFilepathRdf, outputFilepathXml);
    }


    //Object, InfoItem, MetaData
    @Test
    public void testApplication3 () throws Exception {

        String inputFilepathXml = getClass().getClassLoader().getResource("integrationtest/metadata.xml").getFile();
        String outputFilepathRdf = "target/rdf/metadata.ttl";
        String outputFilepathXml = "target/xml/metadata.xml";

        integrationTest(inputFilepathXml, outputFilepathRdf, outputFilepathXml);
    }

    @Test
    public void testApplication4 () throws Exception {

        String inputFilepathXml = getClass().getClassLoader().getResource("integrationtest/smarthouse_omi.xml").getFile();
        String outputFilepathRdf = "target/rdf/smarthouse_omi.ttl";
        String outputFilepathXml = "target/xml/smarthouse_omi.xml";

        integrationTest(inputFilepathXml, outputFilepathRdf, outputFilepathXml);
    }

    //Object - Object - InfoItem - Value
    @Test
    public void testApplication5 () throws Exception {

        String inputFilepathXml = getClass().getClassLoader().getResource("integrationtest/object_object_infoitem_values.xml").getFile();
        String outputFilepathRdf = "target/rdf/object_object_infoitem_values.ttl";
        String outputFilepathXml = "target/xml/object_object_infoitem_values.xml";

        integrationTest(inputFilepathXml, outputFilepathRdf, outputFilepathXml);
    }

    private void integrationTest (String inputFilepathXml, String outputFilepathRdf, String outputFilepathXml) throws IOException {
        Main main = new Main();

        String[] args1 = new String[] { "odf2rdf", inputFilepathXml, outputFilepathRdf };

        main.main(args1);

        String[] args2 = new String[] { "rdf2odf", outputFilepathRdf, outputFilepathXml };

        main.main(args2);

        File temp = File.createTempFile("temp-file", ".tmp");
        temp.deleteOnExit();
        FileOutputStream out = new FileOutputStream(temp);

        InputStream in = RegexHelper.getDateBetweenTags(inputFilepathXml);
        IOUtils.copy(in, out);


        XMLDiff diff = new XMLDiff(temp.getAbsolutePath(), outputFilepathXml);
        int result = diff.compare();

        assertTrue(result == 0);
    }

}
