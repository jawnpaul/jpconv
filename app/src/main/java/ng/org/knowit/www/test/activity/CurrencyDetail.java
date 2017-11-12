package ng.org.knowit.www.test.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import ng.org.knowit.www.test.R;

public class CurrencyDetail extends AppCompatActivity {

    String currencyName, bitcoinRate, etherRate, shortCode;


    private EditText inputValue;

    // BTC conversion variables
    private EditText cardBtcInput;
    private TextView   btcConversionResult;

    // ETH conversion variables
    private EditText cardEthInput;
    private TextView ethTitle_tv, ethCurrencyShortCode1, ethCurrencyShortCode2, ethConversionResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency_conversion);
        //Display the details of the Currency


        TextView ethRate_tv = (TextView) findViewById(R.id.eth_text_view);
        TextView btcRate_iv = (TextView) findViewById(R.id.btc_text_view);
        ImageView currencyImage = (ImageView) findViewById(R.id.currency_image);


        //Read intent data passed from the MainActivity
        Intent incomingIntent = getIntent();

        if (incomingIntent.hasExtra("currencyName")) {
            currencyName = getIntent().getExtras().getString("currencyName");
            int image = getIntent().getExtras().getInt("currencyLogo");
            bitcoinRate = getIntent().getExtras().getString("btcRate");
            etherRate = getIntent().getExtras().getString("ethRate");
            shortCode = getIntent().getExtras().getString("shortCode");


            //set the exchange rates for BTH and ETH
            btcRate_iv.setText(bitcoinRate);
            ethRate_tv.setText(etherRate);


            // loading currencyExchange cover using Glide library
            Glide.with(this).load(image).into(currencyImage);


        }
        //intent data was not received
        else {
            Toast.makeText(this, "Error Occurred. No API Data", Toast.LENGTH_SHORT).show();
            finish();
        }


    }

    private void initBTCViews() {

        //finding and initializing the views

        btcConversionResult = (TextView) findViewById(R.id.btc_conversion_result);
        cardBtcInput = (EditText) findViewById(R.id.base_amount);
        Button btn_btc = (Button) findViewById(R.id.btn_convert);

        //Setting input fields to vice versa to support both way conversion
        btn_btc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                btcConversionResult.setText("");
                cardBtcInput.setText("");

            }
        });

        cardBtcInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String input = cardBtcInput.getText().toString();

                if (TextUtils.isEmpty(input)) {
                    btcConversionResult.setText("Enter a value");
                } else {
                    // parse the input from the string
                    double inputToBeConverted = Double.parseDouble(input);
                    // Do the conversion
                    btcConverter(bitcoinRate, inputToBeConverted);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    private void initETHViews() {
        //finding and initializing the views

        ethConversionResult = (TextView) findViewById(R.id.eth_result);
        cardEthInput = (EditText) findViewById(R.id.base_amount_eth);
        Button ethViceVersaBtn = (Button) findViewById(R.id.eth_btn);

        //Setting input fields to vice versa to support both way conversion
        ethViceVersaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if conversion mode is vice versa then return to normal mode


                //clear previous data
                ethConversionResult.setText("");
                cardEthInput.setText("");

            }
        });

        cardEthInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String input = cardEthInput.getText().toString();

                if (TextUtils.isEmpty(input)) {
                    ethConversionResult.setText("Enter a value");
                } else {
                    // parse the input from the string
                    double inputToBeConverted = Double.parseDouble(input);
                    // Do the conversion
                    ethConverter(etherRate, inputToBeConverted);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    private void btcConverter(String btcRate, double value) {
        //change the bitcoin_logo rate into a double
        double btcPrice = Double.parseDouble(btcRate);

        double answer = value * btcPrice;
        btcConversionResult.setText(String.valueOf(answer));

    }

    private void ethConverter(String ethRate, double value) {
        //change the etherum Rate into a double
        double etherPrice = Double.parseDouble(ethRate);

        double answer = value * etherPrice;
        ethConversionResult.setText(String.valueOf(answer));

    }

}




