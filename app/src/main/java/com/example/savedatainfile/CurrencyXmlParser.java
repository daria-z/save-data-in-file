package com.example.savedatainfile;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;

public class CurrencyXmlParser {
    static XmlPullParser xpp;

    public CurrencyXmlParser(XmlPullParser xpp) {
        CurrencyXmlParser.xpp = xpp;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static ArrayList<Currency> getCurrencyList() {
        ArrayList<Currency> currencyList = new ArrayList<>();
        Currency currentCurrency;
        LocalDate updateDate = null;

        try {

            while (xpp.getEventType() != XmlPullParser.END_DOCUMENT) {
                if (xpp.getEventType() == XmlPullParser.START_TAG
                        && xpp.getName().equalsIgnoreCase("Cube") && (xpp.getAttributeCount() == 1)) {
                    updateDate = LocalDate.parse(xpp.getAttributeValue(0));

                }
                if (xpp.getEventType() == XmlPullParser.START_TAG
                        && xpp.getName().equalsIgnoreCase("Cube") && (xpp.getAttributeCount() == 2)) {
                    currentCurrency = new Currency();
                    currencyList.add(currentCurrency);
                    currentCurrency.setName(xpp.getAttributeValue(0));
                    currentCurrency.setRate(Float.valueOf(xpp.getAttributeValue(1)));
                    currentCurrency.setDate(updateDate);
                    Log.v("DATE", String.valueOf(updateDate));
                }

                xpp.next();
            }

        }   catch (XmlPullParserException | IOException e) {
            throw new RuntimeException(e);
        }

        return currencyList;
    }
}
