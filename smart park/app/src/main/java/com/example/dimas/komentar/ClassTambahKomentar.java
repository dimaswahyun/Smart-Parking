package com.example.dimas.komentar;

/**
 * Created by dimas on 07/09/2017.
 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ClassTambahKomentar extends Activity implements OnClickListener{

    private EditText isiJudul, isiKomentar;
    private Button  kirimKomentar;

    // utk progress bar_nya
    private ProgressDialog progressBarNya;

    // utk tambah komentar perlu deklarasi class JSON
    ClassJSONParser jsonParserNya = new ClassJSONParser();

    // lokasi script utk tambah komentar: bernama tambah_komentar.php

    // biasanya localhost :
    // tapi untuk testing, lebih baik pakai alamat IP
    // kalau tak tahu IP-nya di windows silahkan buka
    // command prompt lalu ketik ipconfig
    // lihat IPv4 Address
    // kalau di mac ketik ifconfig dan cari ip dengan en0 atau en1
    // private static final String LINK_TAMBAH_KOMENTAR =
    // "http://xxx.xxx.x.x.xxx/folderNya/tambah_komentar.php";

    // coba di Emulator:jangan lupa di ganti dengan
    //alamat IP komputer anda sendiri
    private static final String LINK_TAMBAH_KOMENTAR = "http://suksessidang.com/smartparking/tambah_komentar.php";

    // coba di server jauh(remote), server bayaran:
    // private static final String LINK_TAMBAH_KOMENTAR =
    // http://www.namaWeb.com/folderNya/tambah_komentar.php;

    // berikut adalah identitas dari JSON element yang
    // merespond dari halaman PHP (tambah_komentar.php)
    private static final String TAG_BERHASIL = "sukses";
    private static final String TAG_PESAN = "pesan";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.tambah_komentar);

        isiJudul = (EditText)findViewById(R.id.judulKomentar);
        isiKomentar = (EditText)findViewById(R.id.isiKomentar);

        kirimKomentar = (Button)findViewById(R.id.kirimKomentar);
        kirimKomentar.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        String judulKomentar = isiJudul.getText().toString();
        String komentar = isiKomentar.getText().toString();
        new TambahKomentar().execute(judulKomentar,komentar);
    }


    class TambahKomentar extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBarNya = new ProgressDialog(ClassTambahKomentar.this);
            progressBarNya.setMessage("Sedang kirim Komentarnya...");
            progressBarNya.setIndeterminate(false);
            progressBarNya.setCancelable(true);
            progressBarNya.show();
        }

        @Override
        protected String doInBackground(String... args) {
            // priksa jika proses tambah komentar berhasil
            // lihat TAG_BERHASIL
            int komentar_sukses;
            String judulKomentar = args[0];
            String komentar = args[1];
            //ini harus di ubah
            SharedPreferences sharedPrefNya = PreferenceManager.getDefaultSharedPreferences(ClassTambahKomentar.this);
            String userNameNya = sharedPrefNya.getString("username_nya", "anon");

            try {
                // menyusun parameter-nya
                List<NameValuePair> parameterNya = new ArrayList<NameValuePair>();
                parameterNya.add(new BasicNameValuePair("username_nya", userNameNya));
                parameterNya.add(new BasicNameValuePair("judul_komentar", judulKomentar));
                parameterNya.add(new BasicNameValuePair("isi_komentar", komentar));

                Log.d("me-request!", "dimulai");

                //kirim data dari user ke halaman php script
                JSONObject jsonObjectNya = jsonParserNya.membuatHttpRequest(
                        LINK_TAMBAH_KOMENTAR, "POST", parameterNya);

                // jawaban dari JSON
                Log.d("mencoba kirim komentar", jsonObjectNya.toString());

                // Bagian JSON jika komentarnya berhasil/tidak terkirim
                komentar_sukses = jsonObjectNya.getInt(TAG_BERHASIL);
                if (komentar_sukses == 1) {
                    Log.d("Komentar_nya telah di masukan!", jsonObjectNya.toString());
                    finish();
                    return jsonObjectNya.getString(TAG_PESAN);
                }else{
                    Log.d("Kirim komentar_nya Gagal!", jsonObjectNya.getString(TAG_PESAN));
                    return jsonObjectNya.getString(TAG_PESAN);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;

        }

        protected void onPostExecute(String url_link_nya) {
            // matikan progressBarnya dengan metode dismiss();
            progressBarNya.dismiss();
            if (url_link_nya != null){
                Toast.makeText(ClassTambahKomentar.this, url_link_nya, Toast.LENGTH_LONG).show();
            }

        }

    }


}