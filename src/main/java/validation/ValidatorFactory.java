package validation;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import utils.Loggable;

/**
 * Created by aarunova on 3/12/17.
 */
public class ValidatorFactory implements Loggable {

    private static final Log LOG = LogFactory.getLog(ValidatorFactory.class);

    public static SchemaValidator getValidator (String schemaFileName, FileType fileType) {

        try {

            switch (fileType) {
                case XML:
                    return new XSDSchemaValidator(schemaFileName);
                case RDF:
                    return new RDFValidator();

            }

        } catch (Exception e) {
            LOG.warn("Exception", e);
        }

        throw new IllegalArgumentException(String.format("File type %s not supported", fileType));

    }
}
