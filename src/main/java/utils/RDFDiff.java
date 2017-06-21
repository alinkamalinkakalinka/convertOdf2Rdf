package utils;

import org.apache.jena.rdf.model.Statement;

import java.util.Collection;

/**
 * Created by aarunova on 5/14/17.
 */
public class RDFDiff {

    //check if basemodel contains the same number of statements as target model
    public static boolean diff (String baseModel, String targetModel){

        Collection<Statement> baseModelStatements = ModelHelper.getStatementsFromFile(baseModel, "TTL");
        Collection<Statement> targetModelStatements = ModelHelper.getStatementsFromFile(targetModel, "TTL");

        if (baseModelStatements.size() == targetModelStatements.size()) {
            return true;
        } else {
            return false;
        }

    }
}
