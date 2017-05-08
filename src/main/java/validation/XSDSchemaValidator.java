package validation;

import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import utils.Loggable;

/**
 * Created by aarunova on 3/12/17.
 */
public class XSDSchemaValidator implements SchemaValidator, Loggable {

    private Schema schema;

    public XSDSchemaValidator(String xsdFileName) throws Exception{

        InputStream is = XSDSchemaValidator.class.getResourceAsStream(xsdFileName);
        SchemaFactory factory =
                SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        this.schema = factory.newSchema(new StreamSource(is));
        is.close();
        logger().info("Successfully read schema");
    }

    @Override
    public boolean validateAgainstSchema(String source) {
        try {
            Validator validator = this.schema.newValidator();
            validator.validate(new StreamSource(new ByteArrayInputStream(source.getBytes())));
            logger().info("Successfully validated");

            return true;

        } catch (IOException e){
            logger().warn("IOException", e);
            return false;
        } catch(SAXException e){
            logger().warn("SAXException", e);
            return false;
        }
    }

    @Override
    public FileType getFileType() {
        return FileType.XML;
    }
}
