package validation;


import utils.Loggable;

/**
 * Created by aarunova on 3/12/17.
 */
public class ValidatorFactory implements Loggable {


    public SchemaValidator getValidator (String schemaFileName, FileType fileType) {

        try {

            switch (fileType) {
                case XML:
                    return new XSDSchemaValidator(schemaFileName);
                case RDF:
                    return new RDFValidator();

            }

        } catch (Exception e) {
            logger().warn("Exception", e);
        }

        throw new IllegalArgumentException(String.format("File type %s not supported", fileType));

    }
}
