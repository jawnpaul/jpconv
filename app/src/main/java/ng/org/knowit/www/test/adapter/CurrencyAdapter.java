package ng.org.knowit.www.test.adapter;

/**
 * Created by Toshiba1 on 01/11/2017.
 */

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import ng.org.knowit.www.test.R;
import ng.org.knowit.www.test.activity.CurrencyDetail;

//import static ng.org.knowit.www.test.R.id.cardView;

public class CurrencyAdapter extends RecyclerView.Adapter<CurrencyAdapter.CustomViewHolder> {


    private Context context;
    private List<Currency> currencyList;




    public CurrencyAdapter(Context context, List<Currency> currencyList) {
        this.context = context;
        this.currencyList = currencyList;
    }
    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View cv = LayoutInflater.from(parent.getContext()).inflate(R.layout.currency_cardview, parent, false);
        return new CustomViewHolder(cv);
    }

    @Override
    public void onBindViewHolder(final CustomViewHolder holder,  int position) {
        final Currency currency = currencyList.get(position);

        holder.title.setText(currency.getCurrencyName());
        holder.btcPrice.setText(currency.getBitcoinPrice());
        holder.ethprice.setText(currency.getEthereumPrice());
        Glide.with(context).load(currency.getCurrencyImageResource()).into(holder.currencyImage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {  // <--- here
            @Override

            public void onClick(View view) {
                sendIntentToDetailActivity(currency);
            }
        });

    }


    private void sendIntentToDetailActivity(Currency currency) {
        Intent currencyDetail = new Intent(context, CurrencyDetail.class);
        currencyDetail.putExtra("btcRate",currency.getBitcoinPrice());
        currencyDetail.putExtra("ethRate",currency.getEthereumPrice());
        currencyDetail.putExtra("currencyName",currency.getCurrencyName());
        currencyDetail.putExtra("currencyLogo",currency.getCurrencyImageResource());
        currencyDetail.putExtra("shortCode",currency.getCurrencyShortCode());
        context.startActivity(currencyDetail);
    }

    @Override
    public int getItemCount() {
        return currencyList.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        ImageView currencyImage;
        TextView title;
        TextView btcPrice;
        TextView ethprice;


        public CustomViewHolder(View itemView) {

            super(itemView);
            btcPrice = (TextView) itemView.findViewById(R.id.info_btc_price);
            ethprice = (TextView) itemView.findViewById(R.id.info_eth_price);
            currencyImage = (ImageView) itemView.findViewById(R.id.info_currency_img);
            title = (TextView) itemView.findViewById(R.id.info_currency_name);
        }
    }
}
