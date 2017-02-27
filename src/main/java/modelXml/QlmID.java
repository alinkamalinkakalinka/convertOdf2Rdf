package modelXml;

import com.complexible.pinto.annotations.RdfId;
import com.complexible.pinto.annotations.RdfProperty;
import com.complexible.pinto.annotations.RdfsClass;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;
import java.util.Date;

/**
 * Created by aarunova on 12/11/16.
 */

@RdfsClass("odf:QlmID")
@XmlRootElement(name = "id")
public class QlmID {

    private String id;
    private String tagType;
    private Date startDate;
    private Date endDate;

    public QlmID(String id, String tagType, Date startDate, Date endDate) {
        this.id = id;
        this.tagType = tagType;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public QlmID() {
    }


    @RdfId
    @RdfProperty("dct:id")
    @XmlValue
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @RdfId
    @RdfProperty("odf:tagType")
    @XmlAttribute (name = "tagType")
    public String getTagType() {
        return tagType;
    }

    public void setTagType(String tagType) {
        this.tagType = tagType;
    }

    @RdfId
    @RdfProperty("time:startDate")
    @XmlAttribute
    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    @RdfId
    @RdfProperty("time:endDate")
    @XmlAttribute
    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
