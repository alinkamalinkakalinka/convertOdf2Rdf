package evaluation;

import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Statement;
import org.junit.Test;
import utils.ModelHelper;

import java.util.Collection;

/**
 * Created by Alina on 6/5/2017.
 */
public class SPARQLTest {

    @Test
    public void sparqlTest() {

        String fileName1 = "src/test/resources/test/input/rdf/metadata.ttl";
        String fileName2 = "src/test/resources/test/input/rdf/metadata2.ttl";

        Collection<Statement> statements1 = ModelHelper.getStatementsFromFile(fileName1, "TTL");
        Collection<Statement> statements2 = ModelHelper.getStatementsFromFile(fileName2, "TTL");

        Model model = ModelFactory.createDefaultModel();

        for (Statement statement1 : statements1) {
            model.add(statement1);
        }

        for (Statement statement2 : statements2) {
            model.add(statement2);
        }

        String queryString = "SELECT ?x \n" +
                "  {\n" +
                "     ?infoItemMain <http://purl.org/dc/terms/name> \"PowerConsumption\"." +
                "     ?infoItemMain <http://eis-biotope.iais.fraunhofer.de/odf#metadata> ?metaData." +
                "     ?metaData <http://eis-biotope.iais.fraunhofer.de/odf#infoItem> ?infoItem." +
                "     ?infoItem <http://purl.org/dc/terms/name> ?x .\n" +
                "  }";

        Query query = QueryFactory.create(queryString);
        QueryExecution qexec = QueryExecutionFactory.create(query, model);
        ResultSet results = qexec.execSelect();
        while (results.hasNext()) {
            QuerySolution soln = results.nextSolution();
            System.out.println(soln);
        }
    }
}
