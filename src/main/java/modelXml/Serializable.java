package modelXml;

import org.apache.jena.rdf.model.Model;

/**
 * Created by aarunova on 3/27/17.
 */
public interface Serializable {

    Model serialize (String baseURI);
}
