package com.example.dimas.komentar;

/**
 * Created by dimas on 07/09/2017.
 */

/*
 * ©Vik Sintus Projects
 *
 * Segala kelebihan dan kekurangan di luar tanggung jawab pembuat.
 * Di larang memakai kode ini untuk kepentingan komersial tanpa ijin.
 * Silahkan di pakai untuk kepentingan belajar.
 * vik.sintus@gmail.com
 *
 * Unless required by applicable law or agreed to in writing, this software
 * is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 *
 */
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
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

public class ClassRegistrasi extends Activity implements OnClickListener {
    private EditText kotakUser, kotakPassword;
    private Button  tombolRegistrasi;

    // utk progress bar
    private ProgressDialog progressBarNya;

    // panggil ClassJsonParser ke sisni
    ClassJSONParser classJSONParserNya = new ClassJSONParser();

    //lokasi registrasi.php

    //kalau localhost atau komputer rumah lebih baik pakai
    // alamat IP
    //kalau di windows, buka command prompt lalu ketik ipconfig
    //lihat IPv4 Address
    //atau kalau di aple mac terminal silahkan ketik ifconfig dan
    //lihat IPnya di bawah en0 or en1

    //sehingga kalau coba di Emulator akan terlihat mirip sbb:
    private static final String LINK_UTK_REGISTRASI = "http://suksessidang.com/smartparking/registrasi.php";

    //tapi kalau di server bayaran(remote) maka akan terlihat sbb:
    //private static final String LOGIN_URL = "http://www.namaWebMu.com/namafolderNya/registrasi.php";

    //id's
    private static final String TAG_BERHASIL = "sukses";
    private static final String TAG_PESAN = "pesan";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tampilan_registrasi);

        kotakUser = (EditText)findViewById(R.id.username);
        kotakPassword = (EditText)findViewById(R.id.password);


        tombolRegistrasi = (Button)findViewById(R.id.register);
        tombolRegistrasi.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        // klik tombol register untuk apa?

        String usernameNya = kotakUser.getText().toString();
        String passwordNya = kotakPassword.getText().toString();
        new MendaftarPenggunaBaru().execute(usernameNya, passwordNya);

    }

    private class MendaftarPenggunaBaru extends AsyncTask<String, String, String> {

        /**
         * saat setelah tekan tombol registrasi tunjukanlah progressBar kepada
         * *pengguna agar ia tahu aplikasi sedang apa
         * */
        boolean failure = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBarNya = new ProgressDialog(ClassRegistrasi.this);
            progressBarNya.setMessage("Menyimpan User...");
            progressBarNya.setIndeterminate(false);
            progressBarNya.setCancelable(true);
            progressBarNya.show();
        }

        @Override
        protected String doInBackground(String... args) {
            // priksa TAG_BERHASIL
            int berhasil;
            String usernameNya = args[0];
            String passwordNya = args[1];
            try {
                // Cocokan parameternya yah 'username ke username dan
                // password ke password
                List<NameValuePair> parameterNya = new ArrayList<NameValuePair>();
                parameterNya.add(new BasicNameValuePair("username_nya", usernameNya));
                parameterNya.add(new BasicNameValuePair("password_nya", passwordNya));

                Log.d("Request ke server!", "dimulai");

                // kirim data dari user ke script di server
                JSONObject jsonObjectNya = classJSONParserNya.membuatHttpRequest(
                        LINK_UTK_REGISTRASI, "POST", parameterNya);

                // json response-nya
                Log.d("Coba Login", jsonObjectNya.toString());

                // json berhasil
                berhasil = jsonObjectNya.getInt(TAG_BERHASIL);
                if (berhasil == 1) {
                    Log.d("Proses Registrasi berhasil!", jsonObjectNya.toString());
                    finish();
                    return jsonObjectNya.getString(TAG_PESAN);
                }else{
                    Log.d("Registrasi_nya Gagal!", jsonObjectNya.getString(TAG_PESAN));
                    return jsonObjectNya.getString(TAG_PESAN);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;

        }
        /**
         * kalau sudah selesai tugas background_nya matikan
         * progressbar_nya
         * **/
        protected void onPostExecute(String url_registrasi_nya) {
            // matikan progressBar-nya setelah selesai di gunakan
            progressBarNya.dismiss();
            if (url_registrasi_nya != null){
                Toast.makeText(ClassRegistrasi.this, url_registrasi_nya, Toast.LENGTH_LONG).show();
            }

        }

    }



}

