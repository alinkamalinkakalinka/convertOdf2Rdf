package validation;

/**
 * Created by aarunova on 3/12/17.
 */
public interface SchemaValidator {

    boolean validateAgainstSchema(String source);

    FileType getFileType();
}
