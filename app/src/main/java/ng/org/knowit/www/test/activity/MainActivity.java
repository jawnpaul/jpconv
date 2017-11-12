package ng.org.knowit.www.test.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ng.org.knowit.www.test.R;
import ng.org.knowit.www.test.adapter.Currency;
import ng.org.knowit.www.test.adapter.CurrencyAdapter;
import ng.org.knowit.www.test.models.BTC;
import ng.org.knowit.www.test.models.CryptoCompare;
import ng.org.knowit.www.test.models.ETH;
import ng.org.knowit.www.test.rest.ApiClient;
import ng.org.knowit.www.test.rest.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;




public class MainActivity extends AppCompatActivity {


    private RecyclerView recyclerView;
    private CurrencyAdapter mAdapter;
    private List<Currency> currencyList;

    public String[] currencyNames;
    public String[] countryNames;

    public FloatingActionButton fab;

    public Spinner mCurrencySpinner;
    /*position of spinner selected item*/
    public int spinnerPosition;

    public ETH ethereum;
    public BTC bitcoin;

    public double[] BTCrates;
    public double[] ETHrates;

    public View mEmptyStateView;
    public Button retryButton;

    int images[] = new int[]{
            R.drawable.currency_nigeria, R.drawable.currency_usa, R.drawable.currency_bangladesh,
            R.drawable.currency_brazil, R.drawable.currency_brunei, R.drawable.currency_canada, R.drawable.currency_australia,
            R.drawable.currency_russia, R.drawable.currency_england, R.drawable.currency_uae, R.drawable.currency_china,
            R.drawable.currency_oman, R.drawable.currency_afghanistan, R.drawable.currency_egypt, R.drawable.currency_hongkong,
            R.drawable.currency_jamaica, R.drawable.currency_mexico, R.drawable.currency_lebanon, R.drawable.currency_switzerland,
            R.drawable.currency_chile
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        currencyNames = getResources().getStringArray(R.array.currency_options);
        countryNames = getResources().getStringArray(R.array.country_options);
        currencyList = new ArrayList<>();

        recyclerView = (RecyclerView) findViewById(R.id.currency_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

         mAdapter = new CurrencyAdapter(this, currencyList);
        recyclerView.setAdapter(mAdapter);


         fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.add_new_currency, null);
                mBuilder.setTitle("Select a country:");
                //mBuilder.setIcon(R.mipmap.ic_launcher);

                mCurrencySpinner = (Spinner) mView.findViewById(R.id.fullname_text);

                // Create an ArrayAdapter using the string array and a default spinner layout
                ArrayAdapter<String> currencyArrayAdapter = new ArrayAdapter<>(MainActivity.this,
                        android.R.layout.simple_spinner_item, countryNames);

                // Layout of the spinner items
                currencyArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                // apply the mAdapter to the spinner
                mCurrencySpinner.setAdapter(currencyArrayAdapter);
                mCurrencySpinner.setOnItemSelectedListener(new onSpinnerItemClicked());
                mBuilder.setPositiveButton("Add Country", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        addCountryCard();
                        dialog.dismiss();
                    }
                });
                mBuilder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();
                    }
                });
                mBuilder.setView(mView);
                AlertDialog dialog = mBuilder.create();
                dialog.show();
                dialog.setCanceledOnTouchOutside(false);
            }
        });




        // Get a reference to the ConnectivityManager to check the state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        mEmptyStateView = findViewById(R.id.empty_view);
        retryButton = (Button) findViewById(R.id.empty_view_retry);

        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchLatestExchangeRates();
            }
        });

        // Hide the empty state views
        mEmptyStateView.setVisibility(View.GONE);

        //If there is a connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            //Fetch the exchange rates for 20 countries from CryptoCompare Api using Retrofit
            fetchLatestExchangeRates();
            // Hide the empty state views
            mEmptyStateView.setVisibility(View.GONE);
            // Show the FAB button
            fab.setVisibility(View.VISIBLE);
        } else {
            // Show the empty state views
            mEmptyStateView.setVisibility(View.VISIBLE);
            // Hide the FAB button
            fab.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private class onSpinnerItemClicked implements AdapterView.OnItemSelectedListener {
        // This method is supposed to call the on item selected listener on the spinner class
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            //this method gets called automatically when the user selects an item so we need to
            // retrieve what the user has clicked
            spinnerPosition = position;
            parent.getItemAtPosition(position);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            //Do nothing
        }
    }


    /*method to handle user card creation*/
    public void addCountryCard() {

        currencyList.add(new Currency(countryNames[spinnerPosition],
                images[spinnerPosition],
                 String.valueOf(BTCrates[spinnerPosition])
                ,String.valueOf(ETHrates[spinnerPosition])
                ,currencyNames[spinnerPosition]));

        mAdapter.notifyDataSetChanged();
        recyclerView.scrollToPosition(mAdapter.getItemCount() );

        Toast.makeText(getApplicationContext(), String.valueOf(countryNames[spinnerPosition]) +
                " has been added", Toast.LENGTH_LONG).show();

    }


    private void fetchLatestExchangeRates() {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading latest Exchange Rates...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.show();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiClient.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiInterface service = retrofit.create(ApiInterface.class);

        Call<CryptoCompare> call = service.getLatestExchangeRates();

        call.enqueue(new Callback<CryptoCompare>() {
            @SuppressWarnings("ConstantConditions")
            @Override
            public void onResponse(Call<CryptoCompare> call, Response<CryptoCompare> response) {
                progressDialog.dismiss();

                if (response.code()==200){
                    try{
                        fab.setVisibility(View.VISIBLE);
                        mEmptyStateView.setVisibility(View.GONE);

                        // Get ether and btc objects from the response body
                        ethereum = response.body().getETH();
                        bitcoin = response.body().getBTC();

                        // Extract Exchange rates
                        extractExchangeRates(ethereum,bitcoin);

                    } catch (NullPointerException e){
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<CryptoCompare> call, Throwable t) {
                progressDialog.dismiss();
                mEmptyStateView.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(), "Please check your internet connection and try again",
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void extractExchangeRates(ETH ethereum, BTC bitcoin) {
        // Extract BTC exchange rates into an array
        BTCrates = new double[]{
                bitcoin.getNGN(),bitcoin.getUSD(),bitcoin.getBDT(),bitcoin.getBRL(),bitcoin.getBND(), bitcoin.getCAD(),
                bitcoin.getAUD(),bitcoin.getEUR(),bitcoin.getGBP(),bitcoin.getAED(),bitcoin.getCNY(),bitcoin.getOMR(),
                bitcoin.getAFN(),bitcoin.getEGP(),bitcoin.getHKD(),bitcoin.getJMD(),bitcoin.getMXN(),bitcoin.getLBP(),
                bitcoin.getCHF(), bitcoin.getCLP()
        };

        // Extract ETH exchange rates into an array
        ETHrates = new double[]{
                ethereum.getNGN(),ethereum.getUSD(),ethereum.getBDT(),ethereum.getBRL(),ethereum.getBND(), ethereum.getCAD(),
                ethereum.getAUD(),ethereum.getEUR(),ethereum.getGBP(),ethereum.getAED(),ethereum.getCNY(),ethereum.getOMR(),
                ethereum.getAFN(),ethereum.getEGP(),ethereum.getHKD(),ethereum.getJMD(),ethereum.getMXN(),ethereum.getLBP(),
                ethereum.getCHF(), ethereum.getCLP()
        };

        // Initialize the list of countries with Nigera
        Currency NGNdata = new Currency("NIGERIA",images[0],
                String.valueOf(bitcoin.getNGN()),
                String.valueOf(ethereum.getNGN()),
                "NGN");

        if(currencyList.size() == 1) {
            currencyList.remove(0);
            currencyList.add(NGNdata);
        } else if(currencyList.size() == 0) {
            currencyList.add(NGNdata);
        }

        mAdapter.notifyDataSetChanged();
    }

}
