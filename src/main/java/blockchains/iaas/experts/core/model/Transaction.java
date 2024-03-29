package blockchains.iaas.experts.core.model;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigInteger;


@XmlRootElement(name="Transaction")
@XmlAccessorType(XmlAccessType.NONE)
public class Transaction {

    @XmlElement(name="TransactionHash")
    private String transactionHash;

    @XmlElement
    private Block block;

    @XmlElement(name="From")
    private String from;

    @XmlElement(name="To")
    private String to;
    private BigInteger value;

    @XmlElement(name="TransactionState")
    private TransactionState state;

    public Transaction() {
    }

    public Transaction(String transactionHash,
                       Block block,
                       String from, String to, BigInteger value, TransactionState state) {
        this.transactionHash = transactionHash;
        this.block = block;
        this.from = from;
        this.to = to;
        this.value = value;
        this.state = state;
    }

    public String getTransactionHash() {
        return transactionHash;
    }

    public void setTransactionHash(String transactionHash) {
        this.transactionHash = transactionHash;
    }

    public Block getBlock() {
        return block;
    }

    public void setBlock(Block block) {
        this.block = block;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public BigInteger getValue() {
        return value;
    }

    public void setValue(BigInteger value) {
        this.value = value;
    }

    public TransactionState getState() {
        return state;
    }

    public void setState(TransactionState state) {
        this.state = state;
    }

    @XmlElement(name="Value")
    public String getValueAsString(){
        return value.toString();
    }

    public void setValueAsString(String value){
        this.value = new BigInteger(value);
    }
}
