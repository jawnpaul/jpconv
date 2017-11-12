package ng.org.knowit.www.test.models;

/**
 * Created by Toshiba1 on 11/11/2017.
 */

public class CryptoCompare {

    private ETH ETH;
    private BTC BTC;
    public ETH getETH ()
    {
        return ETH;
    }
    public BTC getBTC () { return BTC; }

    public void setETH (ETH ETH)
    {
        this.ETH = ETH;
    }
    public void setBTC (BTC BTC)
    {
        this.BTC = BTC;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [ETH = "+ETH+", BTC = "+BTC+"]";
    }

}
