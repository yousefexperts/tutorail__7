package blockchains.iaas.experts.core.restapi.model.request;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigInteger;



@XmlRootElement(name = "ReceiveTransactionsRequest")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class ReceiveTransactionsRequest {

    private String blockchainId;

    private String subscriptionId;

    private long waitFor;

    private String epUrl;

    private String from;

    @XmlElement(name="EndPointUrl")
    public String getEpUrl() {
        return epUrl;
    }

    public void setEpUrl(String epUrl) {
        this.epUrl = epUrl;
    }

    @XmlElement(name="From")
    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    @XmlElement(name="BlockchainId")
    public String getBlockchainId() {
        return blockchainId;
    }

    public void setBlockchainId(String blockchainId) {
        this.blockchainId = blockchainId;
    }

    @XmlElement(name="SubscriptionId")
    public String getSubscriptionId() {
        return subscriptionId;
    }


    public void setSubscriptionId(String subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    @XmlElement(name="WaitFor")
    public long getWaitFor() {
        return waitFor;
    }


    public void setWaitFor(long waitFor) {
        this.waitFor = waitFor;
    }


}
