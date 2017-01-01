package model;

import com.complexible.pinto.annotations.RdfId;
import com.complexible.pinto.annotations.RdfProperty;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by aarunova on 12/11/16.
 */

@XmlRootElement (name = "Object")
public class Object {

    private String id;
    private String type;
    private String udef;
    private String description;
    private List<InfoItem> infoItem;
    private List<Object> object;

    public Object(String id, String type, String udef, String description, List<InfoItem> infoItem, List<Object> object){
        this.id = id;
        this.type = type;
        this.udef = udef;
        this.description = description;
        this.infoItem = infoItem;
        this.object = object;
    }

    public Object(){}

    @RdfId
    @RdfProperty("odf:type")
    @XmlAttribute
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @XmlElement (name = "InfoItem")
    public List<InfoItem> getInfoItem() {
        return infoItem;
    }

    public void setInfoItem(List<InfoItem> infoItem) {
        this.infoItem = infoItem;
    }

    @XmlElement (name = "Object")
    public List<Object> getObject() {
        return object;
    }

    public void setObject(List<Object> object) {
        this.object = object;
    }

    @RdfId
    @RdfProperty("odf:udef")
    @XmlAttribute
    public String getUdef() {
        return udef;
    }

    public void setUdef(String udef) {
        this.udef = udef;
    }

    @RdfId
    @RdfProperty("odf:description")
    @XmlElement
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @RdfId
    @RdfProperty("odf:id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
