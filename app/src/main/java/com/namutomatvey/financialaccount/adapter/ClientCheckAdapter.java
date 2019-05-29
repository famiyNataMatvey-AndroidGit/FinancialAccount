package com.namutomatvey.financialaccount.adapter;

import android.os.AsyncTask;
import android.util.Base64;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class ClientCheckAdapter extends AsyncTask<String, Void, JSONObject> {

    public static final String PURPOSE_REGISTRATION = "1";
    public static final String PURPOSE_LOGIN = "2";
    public static final String PURPOSE_PASSWORD_RECOVERY = "3";
    public static final String PURPOSE_GET_CHECK = "4";

    final private String REGISTRATION_URL = "https://proverkacheka.nalog.ru:9999/v1/mobile/users/signup";
    final private String LOGIN_URL = "https://proverkacheka.nalog.ru:9999/v1/mobile/users/login";
    final private String PASSWORD_RECOVERY_URL = "https://proverkacheka.nalog.ru:9999/v1/mobile/users/restore";
    final private String IS_CHECK_URL = "https://proverkacheka.nalog.ru:9999/v1/ofds/*/inns/*/fss/";
    final private String GET_CHECK_URL = "https://proverkacheka.nalog.ru:9999/v1/inns/*/kkts/*/fss/";

    private String email;
    private String name;
    private String phone;
    private String password;

    private String fn = null;
    private String fd = null;
    private String fpd = null;
    private String n = null;
    private String date = null;
    private String sum = null;

    public ClientCheckAdapter(String email, String name, String phone) {
        this.email = email;
        this.name = name;
        this.phone = phone;
    }

    public ClientCheckAdapter(String phone, String password) {
        this.phone = phone;
        this.password = password;
    }

    public ClientCheckAdapter(String phone) {
        this.phone = phone;
    }

    public void setQrData(String qr_data){
        String[] split_qr = qr_data.split("&");
        this.date = split_qr[0].split("=")[1];
        this.sum = split_qr[1].split("=")[1];
        this.fn = split_qr[2].split("=")[1];
        this.fd = split_qr[3].split("=")[1];
        this.fpd = split_qr[4].split("=")[1];
        this.n = split_qr[5].split("=")[1];
    }

    @Override
    protected JSONObject doInBackground(String... params) {
        JSONObject response = null;

        String purposeRequest = params[0];

        switch (purposeRequest){
            case PURPOSE_REGISTRATION:
                response = this.registration();
                break;
            case PURPOSE_LOGIN:
                response = this.login();
                break;
            case PURPOSE_PASSWORD_RECOVERY:
                response = this.passwordRecovery();
                break;
            case PURPOSE_GET_CHECK:
                response = this.getCheck(this.fn, this.fd, this.fpd, this.date, this.sum);
                break;
        }

        return response;
    }

    private JSONObject registration() {
//        BufferedReader reader = null;
        OutputStream outputStream = null;
        JSONObject response_json = new JSONObject();
        try {
            String data = new JSONObject()
                    .put("email", this.email)
                    .put("name", this.name)
                    .put("phone", this.phone)
                    .toString();

            URL url = new URL(this.REGISTRATION_URL);
            HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            urlConnection.connect();


            outputStream = urlConnection.getOutputStream();
            OutputStreamWriter  writer = new OutputStreamWriter(outputStream);
            writer.write(data);
            writer.flush();
            writer.close();
            outputStream.flush();
            outputStream.close();


            int responseCode = urlConnection.getResponseCode();
            response_json.put("code", responseCode);
            response_json.put("data", data);
            if (responseCode != HttpsURLConnection.HTTP_NO_CONTENT) {
                if (responseCode == HttpsURLConnection.HTTP_CONFLICT) {
                    response_json.put("massage", "Пользователь с данными" + data + " уже существует в базе ФНС");
                } else response_json.put("massage", "Ошибка взаимодействия с сервером ФНС");
            }
        } catch (JSONException e){
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response_json;
    }

    private JSONObject login(){
        JSONObject response_json = new JSONObject();
        try {
            URL url = new URL(this.LOGIN_URL);
            HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Device-OS", "Adnroid 6.0");
            urlConnection.setRequestProperty("Device-Id", "");

            String authString = this.phone + ":" + this.password;
            String basicAuth = "Basic "  + Base64.encodeToString(authString.getBytes(), Base64.DEFAULT);
            urlConnection.setRequestProperty ("Authorization", basicAuth);
            int responseCode = urlConnection.getResponseCode();
            response_json.put("code", responseCode);
            if(responseCode != HttpsURLConnection.HTTP_OK){
                if(responseCode == HttpsURLConnection.HTTP_FORBIDDEN) {
                    response_json.put("massage", "Некоректный номер телефона или пароль!");
                } else response_json.put("massage", "Ошибка взаимодействия с сервером ФНС");
            }
        } catch (JSONException e){
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response_json;
    }

    private JSONObject passwordRecovery() {
//        BufferedReader reader = null;
        OutputStream outputStream = null;
        JSONObject response_json = null;
        try {
            String data = new JSONObject()
                    .put("phone", this.phone)
                    .toString();

            URL url = new URL(this.PASSWORD_RECOVERY_URL);
            HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(20000);
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            urlConnection.connect();

            response_json = new JSONObject();

            outputStream = urlConnection.getOutputStream();
            OutputStreamWriter  writer = new OutputStreamWriter(outputStream);
            writer.write(data);
            writer.flush();
            writer.close();
            outputStream.flush();
            outputStream.close();

            int responseCode = urlConnection.getResponseCode();
            response_json.put("code", responseCode);

            if (responseCode != HttpsURLConnection.HTTP_NO_CONTENT) {
                if (responseCode == HttpsURLConnection.HTTP_NOT_FOUND) {
                    response_json.put("massage","Пользователя с логином" + this.phone + " не существует");
                }
                else {
                    response_json.put("massage","Статус запроса отличается от обычного");
                }
            }
        } catch (JSONException e){
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response_json;
    }

    private JSONObject isCheck(String FN, String FD, String FPD, String date, String sum){
        JSONObject response_json = new JSONObject();
        try {
            String baseUrl = this.IS_CHECK_URL + FN + "/operations/1/tickets/" + FD;
            String baseUrlParameters = baseUrl + "?fiscalSign=" + FPD + "&date=" + date + "&sum=" + sum;
            URL url = new URL(baseUrlParameters);

            HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Device-OS", "Adnroid 6.0");
            urlConnection.setRequestProperty("Device-Id", "");

            String authString = this.phone + ":" + this.password;
            String basicAuth = "Basic "  + Base64.encodeToString(authString.getBytes(), Base64.DEFAULT);
            urlConnection.setRequestProperty ("Authorization", basicAuth);
            int responseCode = urlConnection.getResponseCode();
            response_json.put("code", responseCode);
            if(responseCode != HttpsURLConnection.HTTP_NO_CONTENT){
                if(responseCode == HttpsURLConnection.HTTP_NOT_ACCEPTABLE) {
                    response_json.put("massage", "Чек не найден.");
                } else if(responseCode == HttpsURLConnection.HTTP_BAD_REQUEST) {
                    response_json.put("massage", "Не указана сумма или дата");
                } else response_json.put("massage", "Ошибка взаимодействия с сервером ФНС");
            }
        } catch (JSONException e){
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response_json;
    }

    private JSONObject getCheck(String FN, String FD, String FPD, String date, String sum){
        isCheck(FN, FD, FPD, date, sum);
        JSONObject response_json = new JSONObject();
        try {
            String baseUrl = this.GET_CHECK_URL + FN + "/tickets/" + FD;
            String baseUrlParameters = baseUrl + "?fiscalSign=" + FPD + "&sendToEmail=no";
            URL url = new URL(baseUrlParameters);

            HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Device-OS", "Adnroid 6.0");
            urlConnection.setRequestProperty("Device-Id", "");

            String authString = this.phone + ":" + this.password;
            String basicAuth = "Basic "  + Base64.encodeToString(authString.getBytes(), Base64.DEFAULT);
            urlConnection.setRequestProperty ("Authorization", basicAuth);
            int responseCode = urlConnection.getResponseCode();
            response_json.put("code", responseCode);
            if (responseCode != HttpsURLConnection.HTTP_OK) {
                if (responseCode == HttpsURLConnection.HTTP_FORBIDDEN) {
                    response_json.put("massage","Указаны некоректные данные пользователя.");
                } else if (responseCode == HttpsURLConnection.HTTP_NOT_ACCEPTABLE) {
                    response_json.put("massage", "Чек не найден.");
                } else {
                    response_json.put("massage", "Неизвестный код пришел от ФНС");
                }
            }
            else {
                BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "utf-8"));
                String s = null;
                StringBuilder sb = new StringBuilder();
                while ((s = reader.readLine()) != null) {
                    sb.append(s);
                }
                reader.close();
                response_json.put("massage", sb.toString());
            }
        } catch (JSONException e){
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response_json;
    }
}
