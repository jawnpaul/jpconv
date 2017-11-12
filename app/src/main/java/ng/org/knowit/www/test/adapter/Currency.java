package ng.org.knowit.www.test.adapter;

/**
 * Created by Toshiba1 on 01/11/2017.
 */

import java.io.Serializable;

public class Currency implements Serializable{
    private String currencyName;
    private int currencyImageResource;
    private String bitcoinPrice;
    private String ethereumPrice;
    private String currencyShortCode;

    //public static List<Currency> currencyArrayList = new ArrayList<>();





    public  Currency(String name, int currencyImageResource, String btcPrice, String ethPrice, String currencyShortCode) {
        this.currencyName = name;
        this.currencyImageResource = currencyImageResource;
        this.bitcoinPrice = btcPrice;
        this.ethereumPrice = ethPrice;
        this.currencyShortCode = currencyShortCode;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public int getCurrencyImageResource() {
        return currencyImageResource;
    }

    public String getBitcoinPrice() {
        return bitcoinPrice;
    }

    public String getEthereumPrice() {
        return ethereumPrice;
    }

    public String getCurrencyShortCode() {
        return currencyShortCode;
    }
}
