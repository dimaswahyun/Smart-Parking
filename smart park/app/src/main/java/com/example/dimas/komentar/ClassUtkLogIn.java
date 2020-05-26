package com.example.dimas.komentar;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ClassUtkLogIn extends Activity implements OnClickListener {

    private EditText isiNamaUser, isiPassword;
    private Button tombolLogin, tombolRegistrasi;

    // utk progress bar_nya
    private ProgressDialog progressBarNya;

    // pasang class JSON parser
    ClassJSONParser classJsonParser = new ClassJSONParser();

    // lokasi script utk login: bernama login_nya.php

    // biasanya localhost :
    // tapi untuk testing, lebih baik pakai alamat IP
    // kalau tak tahu IP-nya di windows silahkan buka
    // command prompt lalu ketik ipconfig
    // lihat IPv4 Address
    // kalau di mac ketik ifconfig dan cari ip dengan en0 atau en1
    // private static final String LINK_UNTUK_LOGIN =
    // "http://xxx.xxx.x.x.xxx/folderNya/login_nya.php";

    // kalau cobe di emulator:
    private static final String LINK_UNTUK_LOGIN = "http://suksessidang.com/smartparking/login_nya.php";

    // coba di server jauh(remote), server bayaran:
    // private static final String LINK_UNTUK_LOGIN =
    // http://www.namaWeb.com/folderNya/login_nya.php;

    // identitas elemen yg perlu di
    // jawab oleh JSON dari php script:
    private static final String TAG_SUKSES = "berhasil";
    private static final String TAG_PESAN = "tampilkan_pesan";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.tampilan_login);

        // utk kotak input yg perlu di di isi pengguna
        isiNamaUser = (EditText) findViewById(R.id.username);
        isiPassword = (EditText) findViewById(R.id.password);

        // utk tombol
        tombolLogin = (Button) findViewById(R.id.login);
        tombolRegistrasi = (Button) findViewById(R.id.register);

        // agar tombol bisa di klik
        tombolLogin.setOnClickListener(this);
        tombolRegistrasi.setOnClickListener(this);

    }

    @Override
    public void onClick(View apaYgDiKlik) {

        switch (apaYgDiKlik.getId()) {
            case R.id.login:

                String namaUser = isiNamaUser.getText().toString();
                String password = isiPassword.getText().toString();
                new MencobaLogin().execute(namaUser, password);
                break;
            case R.id.register:
                Intent i = new Intent(this, ClassRegistrasi.class);
                startActivity(i);
                break;

            default:
                break;
        }
    }

    class MencobaLogin extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBarNya = new ProgressDialog(ClassUtkLogIn.this);
            progressBarNya.setMessage("Mencoba login...");
            progressBarNya.setIndeterminate(false);
            progressBarNya.setCancelable(true);
            progressBarNya.show();
        }

        @Override
        protected String doInBackground(String... args) {

            // jika sukses
            int jikaSukses;
            String namaUser = args[0];
            String password = args[1];

            try {
                // menyusun parameternya (disini parameternya
                // cuma 2 (username dan password))
                List namaDanPassword = new ArrayList<>();
                namaDanPassword.add(new BasicNameValuePair("username_nya", namaUser));
                namaDanPassword.add(new BasicNameValuePair("password_nya", password));

                Log.d("requestNya!", "dimulai");
                // utk melengkapi proses login, maka
                // perlu membuatHttpRequst ke server
                JSONObject jsonObjectNya = classJsonParser.membuatHttpRequest(LINK_UNTUK_LOGIN, "POST",
                        namaDanPassword);

                // priksa log jawaban dari JSON
                Log.d("Coba login", jsonObjectNya.toString());

                // apa kata JSON tentang TAG_SUKSES
                jikaSukses = jsonObjectNya.getInt(TAG_SUKSES);
                if (jikaSukses == 1) {
                    Log.d("Login_nya Sukses!", jsonObjectNya.toString());
                    // simpan data yg di masukan pengguna
                    SharedPreferences sharedPrefNya = PreferenceManager
                            .getDefaultSharedPreferences(ClassUtkLogIn.this);
                    Editor editorNya = sharedPrefNya.edit();
                    editorNya.putString("username_nya", namaUser);
                    editorNya.commit();

                    Intent intentNya = new Intent(ClassUtkLogIn.this, ClassMaps.class);
                    finish();
                    startActivity(intentNya);
                    return jsonObjectNya.getString(TAG_PESAN);
                } else {
                    Log.d("Login_nya Gagal!", jsonObjectNya.getString(TAG_PESAN));
                    return jsonObjectNya.getString(TAG_PESAN);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;

        }

        protected void onPostExecute(String urlFileNya) {
            // kalau sudah selesai di gunakan, matikanlah
            // progressbar_nya dengan metode dismiss();
            progressBarNya.dismiss();
            if (urlFileNya != null) {
                Toast.makeText(ClassUtkLogIn.this, urlFileNya, Toast.LENGTH_LONG).show();
            }

        }

    }
}
