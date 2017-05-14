package utils;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;

/**
 * Created by aarunova on 5/14/17.
 */
public class RDFDiff {

    //check if basemodel contains the same number of statements as target model
    public static boolean diff (String baseModel, String targetModel){

        Collection<Statement> baseModelStatements = ModelHelper.getStatementsFromFile(baseModel);
        Collection<Statement> targetModelStatements = ModelHelper.getStatementsFromFile(targetModel);

        if (baseModelStatements.size() == targetModelStatements.size()) {
            return true;
        } else {
            return false;
        }

    }
}
