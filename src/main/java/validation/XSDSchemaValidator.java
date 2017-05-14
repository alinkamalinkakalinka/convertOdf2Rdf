package validation;

import org.xml.sax.SAXException;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;

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
            //validator.validate(new StreamSource(new ByteArrayInputStream(source.getBytes())));
            validator.validate(new StreamSource(checkForUtf8BOMAndDiscardIfAny(new FileInputStream(source))));
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

    private static InputStream checkForUtf8BOMAndDiscardIfAny(InputStream inputStream) throws IOException {
        PushbackInputStream pushbackInputStream = new PushbackInputStream(new BufferedInputStream(inputStream), 3);
        byte[] bom = new byte[3];
        if (pushbackInputStream.read(bom) != -1) {
            if (!(bom[0] == (byte) 0xEF && bom[1] == (byte) 0xBB && bom[2] == (byte) 0xBF)) {
                pushbackInputStream.unread(bom);
            }
        }
        return pushbackInputStream; }
}
