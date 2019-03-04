package blockchains.iaas.experts.core.restapi.model.response;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name="Variable")
@XmlAccessorType(XmlAccessType.FIELD)
public class CamundaVariable {
    @XmlElement(name="Value")
    private String value;
    @XmlElement(name="Type")
    private String type;

    public CamundaVariable(){

    }
    public CamundaVariable(String value, String type) {
        this.value = value;
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
