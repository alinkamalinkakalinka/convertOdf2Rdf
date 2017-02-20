package modelXml;

import com.complexible.pinto.annotations.RdfId;
import com.complexible.pinto.annotations.RdfProperty;
import com.complexible.pinto.annotations.RdfsClass;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;
import java.util.Date;
import java.util.List;

/**
 * Created by aarunova on 12/11/16.
 */

@RdfsClass("odf:Value")
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
    @RdfProperty("odf:type")
    @XmlAttribute
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @RdfId
    @RdfProperty("dct:dateTime")
    @XmlAttribute
    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    @RdfId
    @RdfProperty("time:unixTime")
    @XmlAttribute
    public long getUnixTime() {
        return unixTime;
    }

    public void setUnixTime(long unixTime) {
        this.unixTime = unixTime;
    }

    @RdfId
    @RdfProperty("rdf:value")
    @XmlValue
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
