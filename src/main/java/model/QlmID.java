package model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import java.util.Date;

/**
 * Created by aarunova on 12/11/16.
 */
public class QlmID {

    private String id;
    private String tagType;
    private Date startDate;
    private Date endDate;

    public QlmID() {}

    public QlmID(String id){
        this.id = id;
    }

    public QlmID(String id, String tagType, Date startDate, Date endDate) {
        this.id = id;
        this.tagType = tagType;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @XmlElement
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @XmlAttribute
    public String getTagType() {
        return tagType;
    }

    public void setTagType(String tagType) {
        this.tagType = tagType;
    }

    @XmlAttribute
    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    @XmlAttribute
    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
