package modelXml;

import com.complexible.pinto.annotations.RdfId;
import com.complexible.pinto.annotations.RdfProperty;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;
import java.util.Date;

/**
 * Created by aarunova on 12/11/16.
 */

//@RdfsClass("odf:value")
@XmlRootElement (name = "value")
public class Value {

    private String type;
    private Date dateTime;
    private long unixTime;
    private String value;

    public Value(){}

    public Value(String type, Date dateTime, long unixTime, String value){
        this.type = type;
        this.dateTime = dateTime;
        this.unixTime = unixTime;
        this.value = value;
    }

    @RdfId
    @XmlAttribute
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @RdfId
    @RdfProperty("odf:dateTime")
    @XmlAttribute
    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    @RdfId
    @RdfProperty("odf:unixTime")
    @XmlAttribute
    public long getUnixTime() {
        return unixTime;
    }

    public void setUnixTime(long unixTime) {
        this.unixTime = unixTime;
    }

    @RdfId
    @RdfProperty("odf:value")
    @XmlValue
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
