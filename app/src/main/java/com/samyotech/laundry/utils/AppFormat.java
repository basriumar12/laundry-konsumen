package com.samyotech.laundry.utils;

import android.util.Log;

import java.text.NumberFormat;
import java.util.Locale;

public class AppFormat {

    public static String doubleToRupiahFormat (Double number){
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        return formatRupiah.format(number);
    }

    public static String addDelimiter(String nominal) {
        Log.e("AppFormat", "addDelimiter: " + nominal );
        String s = null;
        try {
            // The comma in the format specifier does the trick
            s = String.format("%,d", Long.parseLong(nominal));
            Log.e("AppFormat", "addDelimiter: " + s );
            return s;
        } catch (Exception e) {
            Log.e("AppFormat", "addDelimiter: " + e.getMessage() );
        }
        // Set s back to the view after temporarily removing the text change listener
        return nominal;
    }

}
