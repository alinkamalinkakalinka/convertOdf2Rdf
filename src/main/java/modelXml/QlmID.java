package modelXml;

import com.complexible.pinto.annotations.RdfId;
import com.complexible.pinto.annotations.RdfProperty;
import com.complexible.pinto.annotations.RdfsClass;
import org.eclipse.rdf4j.model.*;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import vocabs.NS;

import javax.xml.bind.DatatypeConverter;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by aarunova on 12/11/16.
 */

@RdfsClass("odf:QlmID")
@XmlRootElement(name = "id")
public class QlmID {

    private String id;
    private String tagType;
    private String startDate;
    private String endDate;

    public QlmID(String id, String tagType, String startDate, String endDate) {
        this.id = id;
        this.tagType = tagType;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public QlmID() {
    }


    public String getId() {
        return id;
    }

    @XmlValue
    public void setId(String id) {
        this.id = id;
    }


    public String getTagType() {
        return tagType;
    }

    @XmlAttribute (name = "tagType")
    public void setTagType(String tagType) {
        this.tagType = tagType;
    }


    public String getStartDate() {
        return startDate;
    }

    @XmlAttribute (name = "startDate")
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }


    public String getEndDate() {
        return endDate;
    }

    @XmlAttribute (name = "endDate")
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Model serialize(ValueFactory vf) {
        //Literal startDateValue = vf.createLiteral(DatatypeConverter.parseDateTime(startDate).getTime());
        //Literal endDateValue = vf.createLiteral(DatatypeConverter.parseDateTime(endDate).getTime());
        //Literal idValue = vf.createLiteral(id);

        HashMap<String, String> elementsAndAttributes = new HashMap<>();
        elementsAndAttributes.put("time:startDate", startDate);
        elementsAndAttributes.put("time:endDate", endDate);

        BNode subject = vf.createBNode();

        ModelBuilder builder = new ModelBuilder();
        builder.setNamespace("dct", NS.DCT)
                .setNamespace("time", NS.TIME)
                .setNamespace("odf", NS.ODF)
                .setNamespace("rdf", RDF.NAMESPACE);

        builder.subject(subject)
                .add("rdf:type", "odf:QlmID");

        if (id != null) {
            builder.add("odf:idValue", id);
        }

        for (Map.Entry<String, String> entry : elementsAndAttributes.entrySet()) {
            if (entry.getValue() != null) {
                Literal dateValue = vf.createLiteral(DatatypeConverter.parseDateTime(entry.getValue()).getTime());
                builder.add(entry.getKey(), dateValue);
            }
        }

        return builder.build();
    }
}
