package blockchains.iaas.experts.core.adaptation.utils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;


public class BitcoinUtils {
    private static final long SATOSHIS_PER_BITCCOIN = 100000000L;
    private static final int LARGEST_SCALE = 8;

    public static BigDecimal satoshiToBitcoin(BigDecimal satoshis){
        return satoshis.divide(new BigDecimal(SATOSHIS_PER_BITCCOIN), LARGEST_SCALE, RoundingMode.UNNECESSARY);
    }

    public static BigDecimal bitcoinsToSatoshi(BigDecimal bitcoins){
        return bitcoins.multiply(new BigDecimal(SATOSHIS_PER_BITCCOIN));
    }

}
