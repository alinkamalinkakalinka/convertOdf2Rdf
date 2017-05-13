import org.junit.Test;

/**
 * Created by aarunova on 5/8/17.
 */
public class SerializerTest {
    @Test
    public void odf2rdf() throws Exception {

        Serializer serializer = new Serializer();

        Deserializer deserializer = new Deserializer();

        String xmlFile = "test/xml/metadata.xml";

        serializer.odf2rdf(xmlFile, "metadata_about_refrigerator_power.ttl");

        deserializer.deserialize("metadata_about_refrigerator_power.ttl", "metadata.xml");





    }

}