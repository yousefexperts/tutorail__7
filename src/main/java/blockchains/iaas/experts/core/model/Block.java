package blockchains.iaas.experts.core.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigInteger;


@XmlRootElement(name="Block")
@XmlAccessorType(XmlAccessType.NONE)
public class Block {
    private BigInteger number;
    private String hash;

    public Block(){

    }

    public Block(BigInteger number, String hash) {
        this.number = number;
        this.hash = hash;
    }

    public BigInteger getNumber() {
        return number;
    }

    public void setNumber(BigInteger number) {
        this.number = number;
    }

    @XmlElement(name="BlockNumber")
    public long getNumberAsLong(){
        return this.number.longValue();
    }

    public void setNumberAsLong(long value){
        this.number = BigInteger.valueOf(value);
    }

    @XmlElement(name="BlockHash")
    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }
}
