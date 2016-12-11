package model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

/**
 * Created by aarunova on 12/11/16.
 */

@XmlRootElement
public class Value {

    private String type;
    private Date dateTime;
    private long unixTime;

    public Value(){}

    public Value(String type, Date dateTime, long unixTime){
        super();
        this.type = type;
        this.dateTime = dateTime;
        this.unixTime = unixTime;
    }

    @XmlAttribute
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @XmlAttribute
    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    @XmlAttribute
    public long getUnixTime() {
        return unixTime;
    }

    public void setUnixTime(long unixTime) {
        this.unixTime = unixTime;
    }
}
