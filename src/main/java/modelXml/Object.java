package modelXml;

import com.complexible.pinto.annotations.RdfId;
import com.complexible.pinto.annotations.RdfProperty;
import com.complexible.pinto.annotations.RdfsClass;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by aarunova on 12/11/16.
 */

@RdfsClass("odf:Object")
@XmlRootElement (name = "Object")
public class Object {

    private String id;
    private String type;
    private String udef;
    private String description;
    private List<InfoItem> infoItem;
    private List<Object> objectList;

    public Object(String id, String type, String udef, String description, List<InfoItem> infoItem, List<Object> objectList){
        this.id = id;
        this.type = type;
        this.udef = udef;
        this.description = description;
        this.infoItem = infoItem;
        this.objectList = objectList;
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

    @RdfId
    @RdfProperty("odf:infoItem")
    @XmlElement (name = "InfoItem")
    public List<InfoItem> getInfoItem() {
        return infoItem;
    }

    public void setInfoItem(List<InfoItem> infoItem) {
        this.infoItem = infoItem;
    }

    @RdfId
    @RdfProperty("odf:object")
    @XmlElement (name = "Object")
    public List<Object> getObjectList() {
        return objectList;
    }

    public void setObjectList(List<Object> objectList) {
        this.objectList = objectList;
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
