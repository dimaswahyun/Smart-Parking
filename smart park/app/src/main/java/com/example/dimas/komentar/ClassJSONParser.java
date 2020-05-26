package com.example.dimas.komentar;

/**
 * Created by dimas on 07/09/2017.
 */

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

public class ClassJSONParser {

    static InputStream inputStreamNya = null;
    static JSONObject JSONObjectNya = null;
    static String jsonStringNya = "";

    // berikut adalah constructor-nya
    public ClassJSONParser() {

    }

    //ambil json object melalui url script_nya maksudnya
    //object yang dari php yang ada di server
    public JSONObject ambilJsonDariUrl(final String urlNya) {

        // object dari Json di ambil melalui http request
        try {
            // kirim data bolak-balik pakai 'post' yah biar agak aman.
            DefaultHttpClient httpClientNya = new DefaultHttpClient();
            HttpPost httpPostNya = new HttpPost(urlNya);

            // begitu juga dengan jawaban dari server pakai metode POST
            HttpResponse httpResponseNya = httpClientNya.execute(httpPostNya);
            // mengambil data yang dapat di response oleh server.
            HttpEntity httpEntityNya = httpResponseNya.getEntity();
            // buka sebuah inputStream ke data yang ada.
            inputStreamNya = httpEntityNya.getContent();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            // buat sebuah  BufferedReader untuk di parse melalui inputStream.
            BufferedReader bufferedReaderNya = new BufferedReader(new InputStreamReader(
                    inputStreamNya, "iso-8859-1"), 8);
            // deklarasikan sebuah string utk membantu proses parsing-nya.
            StringBuilder stringBuilderNya = new StringBuilder();
            // deklarasi sebuah string utk menyimpan
            // data JSON object dalam bentuk string.
            String barisanStringNya = null;

            // susun string-nya sampai null(habis). baris demi baris
            while ((barisanStringNya = bufferedReaderNya.readLine()) != null) {
                stringBuilderNya.append(barisanStringNya + "\n");
            }

            // tutup input stream-nya.
            inputStreamNya.close();
            // ubalah 'string builder data' menjadi string aslinya.
            jsonStringNya = stringBuilderNya.toString();
        } catch (Exception e) {
            Log.e("BufferNya Salah", "Salah mengolah menjadi string aslinya " + e.toString());
        }

        // coba mengolah stringNya menjadi sebuah JSON object
        try {
            JSONObjectNya = new JSONObject(jsonStringNya);
        } catch (JSONException e) {
            Log.e("JSON ParserNya", "Salah Mengolah data " + e.toString());
        }

        return JSONObjectNya;

    }


    // fungsi ambilJsonDariUrl dengan
    // cara HTTP POST atau GET
    public JSONObject membuatHttpRequest(String urlNya, String caraNya,
                                         List<NameValuePair> parameterNya) {

        // buat jalur HTTP-nya
        try {

            // priksa metode yang di pakai POST atau GET
            if(caraNya == "POST"){
                DefaultHttpClient httpClientNya = new DefaultHttpClient();
                HttpPost httpPostNya = new HttpPost(urlNya);
                httpPostNya.setEntity(new UrlEncodedFormEntity(parameterNya));

                HttpResponse httpResponseNya = httpClientNya.execute(httpPostNya);
                HttpEntity httpEntityNya = httpResponseNya.getEntity();
                inputStreamNya = httpEntityNya.getContent();
                //kalau pakai cara 'GET' maka search string_nya akan
                //tampak di URL
            }else if(caraNya == "GET"){
                DefaultHttpClient defaultHttpClientNya = new DefaultHttpClient();
                String stringYgTampakDiUrl = URLEncodedUtils.format(parameterNya, "utf-8");
                urlNya += "?" + stringYgTampakDiUrl;
                HttpGet httpGetNya = new HttpGet(urlNya);

                HttpResponse httpResponseNya = defaultHttpClientNya.execute(httpGetNya);
                HttpEntity httpEntityNya = httpResponseNya.getEntity();
                inputStreamNya = httpEntityNya.getContent();
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            BufferedReader bufferedReaderNya = new BufferedReader(new InputStreamReader(
                    inputStreamNya, "iso-8859-1"), 8);
            StringBuilder stringBuilderNya = new StringBuilder();
            String barisanDataNya = null;
            while ((barisanDataNya = bufferedReaderNya.readLine()) != null) {
                stringBuilderNya.append(barisanDataNya + "\n");
            }
            inputStreamNya.close();
            jsonStringNya = stringBuilderNya.toString();
        } catch (Exception e) {
            Log.e("Buffer_nya Error", "Salah mengkonvert hasil" + e.toString());
        }

        // coba parsing string-nya menjadi JSON object
        try {
            JSONObjectNya = new JSONObject(jsonStringNya);
        } catch (JSONException e) {
            Log.e("JSON ParserNya", "Salah proses datanya " + e.toString());
        }

        return JSONObjectNya;

    }
}
