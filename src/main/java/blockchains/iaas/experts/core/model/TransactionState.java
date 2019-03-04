package blockchains.iaas.experts.core.model;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="State")
public enum TransactionState {
    UNKNOWN,
    PENDING,
    CONFIRMED,
    NOT_FOUND,
    INVALID
}
