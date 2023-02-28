package com.example.savedatainfile;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class CurrencyAdapter extends ArrayAdapter<Currency> {
    public CurrencyAdapter(Context context, ArrayList<Currency> currencies) {
        super(context, 0, currencies);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Currency currency = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.currency_row, parent, false);
        }

        TextView currencyNameView = convertView.findViewById(R.id.currencyName);
        TextView currencyRateView = convertView.findViewById(R.id.currencyRate);
        TextView currencyUpdateDateView = convertView.findViewById(R.id.currencyUpdateDate);
        ImageView flag_place = convertView.findViewById(R.id.flag_img);


        currencyNameView.setText(currency.getName());
        currencyRateView.setText(String.valueOf(currency.getRate()));
        currencyUpdateDateView.setText(String.valueOf(currency.getDate()));

        String filename = "flag_".concat(currency.getName().toLowerCase()).concat(".png");
        try(InputStream inputStream = getContext().getAssets().open(filename)){
            Drawable drawable = Drawable.createFromStream(inputStream, null);
            flag_place.setImageDrawable(drawable);
            flag_place.setScaleType(ImageView.ScaleType.FIT_XY);
        }
        catch (IOException e){
            e.printStackTrace();
        }

        return convertView;
    }
}
