import org.apache.jena.sparql.serializer.SerializerRegistry;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by aarunova on 5/8/17.
 */
public class SerializerTest {
    @Test
    public void odf2rdf() throws Exception {

        Serializer serializer = new Serializer();

        Deserializer deserializer = new Deserializer();

        String xmlFile = "xml/metadata.xml";

        serializer.odf2rdf(xmlFile, "metadata_about_refrigerator_power.ttl");

        deserializer.deserialize("metadata_about_refrigerator_power.ttl", "metadata.xml");





    }

}