package com.namutomatvey.financialaccount.adapter;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ProtocolException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;


public class GetCurrncyCBRAdapter extends AsyncTask<Void, Void, JSONObject> {

  @Override
  protected JSONObject doInBackground(Void... voids) {
    try {
      URL url = new URL("https://www.cbr-xml-daily.ru/daily_json.js");
      HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
      urlConnection.setRequestMethod("GET");
      urlConnection.setReadTimeout(10000);
      urlConnection.setConnectTimeout(15000);
      urlConnection.setDoInput(true);
      urlConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
      urlConnection.connect();

      BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "utf-8"));
      String s = null;
      StringBuilder sb = new StringBuilder();
      while ((s = reader.readLine()) != null) {
        sb.append(s);
      }
      reader.close();
      JSONObject newVersionCurrency = new JSONObject(sb.toString());
      return newVersionCurrency.getJSONObject("Valute");
    } catch (ProtocolException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (JSONException e) {
      e.printStackTrace();
    }
    return new JSONObject();
  }
}