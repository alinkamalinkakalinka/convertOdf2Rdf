package model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by aarunova on 12/11/16.
 */

@XmlRootElement
public class MetaData {

    private List<InfoItem> infoItem;

    public MetaData(){}

    public MetaData(List<InfoItem> infoItem) {
        super();
        this.infoItem = infoItem;
    }

    @XmlElement
    public List<InfoItem> getInfoItem() {
        return infoItem;
    }

    public void setInfoItem(List<InfoItem> infoItem) {
        this.infoItem = infoItem;
    }
}
