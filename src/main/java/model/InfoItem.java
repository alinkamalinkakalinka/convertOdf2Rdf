package model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by aarunova on 12/11/16.
 */

@XmlRootElement (name = "InfoItem")
public class InfoItem extends QlmID {

    private String name;
    private String udef;
    private List<Value> value;
    private String description;
    private MetaData metaData;

    public InfoItem(){}

    public InfoItem(String name, String name1, String udef, List<Value> value, String description, MetaData metaData){
        super(name);
        this.name = name1;
        this.udef = udef;
        this.value = value;
        this.description = description;
        this.metaData = metaData;
    }

    @XmlAttribute
    public String getUdef() {
        return udef;
    }

    public void setUdef(String udef) {
        this.udef = udef;
    }


    @XmlElement
    public List<Value> getValue() {
        return value;
    }

    public void setValue(List<Value> value) {
        this.value = value;
    }

    @XmlElement
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @XmlElement
    public MetaData getMetaData() {
        return metaData;
    }

    public void setMetaData(MetaData metaData) {
        this.metaData = metaData;
    }

    @XmlAttribute
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
