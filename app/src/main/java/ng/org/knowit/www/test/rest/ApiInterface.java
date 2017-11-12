package ng.org.knowit.www.test.rest;

import ng.org.knowit.www.test.models.CryptoCompare;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Toshiba1 on 11/11/2017.
 */

public interface ApiInterface {

    @GET("pricemulti?fsyms=ETH,BTC&tsyms=NGN,USD,BDT,BRL,BND,CAD,AUD,EUR,GBP,AED,CNY,OMR,AFN,EGP,HKD,JMD,MXN,LBP,CHF,CLP")
    Call<CryptoCompare> getLatestExchangeRates();
}
