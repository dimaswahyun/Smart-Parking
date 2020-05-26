package com.example.dimas.komentar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by dimas on 07/09/2017.
 */

public class ClassA3 extends Activity {

    final Context context = this;
    private static final String LINK_TAMBAH_KOMENTAR = "http://suksessidang.com/smartparking/tambah_histori.php";

    private static final String TAG_SEMUA_KOMENTAR = "semua_komentar";
    private static final String NODENYA = "node";
    private static final String STATUSNYA = "status_booking";
    private static final String TAG_USERNAME = "username";
    private JSONArray semuaKomentar = null;

    // utk tambah komentar perlu deklarasi class JSON
    // coba di server jauh(remote), server bayaran:
    // private static final String LINK_TAMBAH_KOMENTAR =
    // http://www.namaWeb.com/folderNya/tambah_komentar.php;

    // berikut adalah identitas dari JSON element yang
    // merespond dari halaman PHP (tambah_komentar.php)
    private ArrayList<HashMap<String, String>> susunanKomentar;
    private static final String LINK_SEMUA_KOMENTAR = "http://suksessidang.com/smartparking/lihat_denah.php?";
    private static final String TAG_BERHASIL = "sukses";
    private static final String TAG_PESAN = "pesan";
    private EditText isiJudul, isiKomentar;
    private TextView pesan_booking;
    private Button kirimKomentar, booking;

    // utk progress bar_nya
    private ProgressDialog progressBarNya;

    ClassJSONParser jsonParserNya = new ClassJSONParser();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.booking);
        booking = (Button) findViewById(R.id.booking);
        pesan_booking = (TextView)findViewById(R.id.pesan_booking);

        booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TambahKomentar().execute();

            }
        });
    }
    public class TampilSemuaKomentar extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBarNya = new ProgressDialog(ClassA3.this);
            progressBarNya.setMessage("Tunggu yah, masih loading...");
            progressBarNya.setIndeterminate(false);
            progressBarNya.setCancelable(true);
            progressBarNya.show();
        }

        @Override
        protected Boolean doInBackground(Void... arg0) {

            jsonDataNyaDiUpdate();

            return null;
        }
//setelah tampil hasilnya matikan progress barnya

        @Override
        protected void onPostExecute(Boolean tampilkanHasilNya) {
            super.onPostExecute(tampilkanHasilNya);
//dengan metode dismiss();

            progressBarNya.dismiss();
//            updateListViewNya();

            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    jsonDataNyaDiUpdate();
                }
            }, 1000, 1000);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // pasang(loading) semua komentar melalui AsyncTask
        new ClassA3.TampilSemuaKomentar().execute();
    }
    public void jsonDataNyaDiUpdate() {
        //buatkan arraylist utk menampung semua JSON data dan
        //kita akan menggunakan beberapa 'key value pairs' ber
        //dasarkan nama JSON element yang kita tanam di
        //halaman php yg identitasnya telah di sebutkan
        //pada beberapa TAG di atas

        susunanKomentar = new ArrayList<HashMap<String, String>>();

        // hidupkan JSONParser-nya
        ClassJSONParser jsonParserNya = new ClassJSONParser();
        // masukan url untuk komentar-nya, sehingga
        // JSONObject memuntahkan isi komentar yang ada

        String query = "status_booking=1";
        SharedPreferences sharedPrefNya = PreferenceManager.getDefaultSharedPreferences(ClassA3.this);
        final String userNameNya = sharedPrefNya.getString("username", "anonnymous");
        JSONObject jsonObjectNya = jsonParserNya.ambilJsonDariUrl(LINK_SEMUA_KOMENTAR + query);


        // ketika parsing sesuatu lewat JSON , ada baiknya
        // di pasang 'try catch exceptions' utk menanggkap hal2 yang
        // tak terduga(error) :
        try {
            semuaKomentar = jsonObjectNya.getJSONArray(TAG_SEMUA_KOMENTAR);

            // terus looping ke semua komentar yang telah ada sesuai
            // dengan apa yang di dapatkan oleh JSONObject
            for (int i = 0; i < semuaKomentar.length(); i++) {
                JSONObject komentarNya = semuaKomentar.getJSONObject(i);

                final String username_nya = komentarNya.getString(TAG_USERNAME);
                // ambil isi dari masing2 tag

                String node = komentarNya.getString(NODENYA);
                String statusnya = komentarNya.getString(STATUSNYA);
                System.out.print("@@@@@@@@@@@@@@@@@@@@@@@@@@" + node);
                if (node.equals("A3")&&statusnya.equals("1")) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            HashMap<String, String> map = new HashMap<String, String>();

                            map.put(TAG_USERNAME, username_nya);
//                            map.put(TAG_JUDUL_KOMENTAR, judul_komentar);
//                                A1.setText(map.put(TAG_USERNAME, username_nya));
//                                A1.setBackgroundResource(R.drawable.booked);
                            booking.setEnabled(false);
                            booking.setVisibility(View.GONE);
                            pesan_booking.setVisibility(View.VISIBLE);


//                                A1.setEnabled(false);
                        }
                    });
                }else {
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            HashMap<String, String> map = new HashMap<String, String>();
//
//                            map.put(TAG_USERNAME, username_nya);
////                            map.put(TAG_JUDUL_KOMENTAR, judul_komentar);
//                            A1.setText(map.put(TAG_USERNAME, username_nya));
//                            A1.setBackgroundColor(Color.GREEN);
//                            A1.setEnabled(false);
//                        }
//                    });
                }

//
//                // buatkan HashMap baru utk mencocokan TAG dan parameter-nya
                HashMap<String, String> map = new HashMap<String, String>();
//
                map.put(NODENYA, node);
//                map.put(TAG_JUDUL_KOMENTAR, judul_komentar);
//                map.put(TAG_ISI_KOMENTAR, isi_komentar);
//
                susunanKomentar.add(map);


            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    class TambahKomentar extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBarNya = new ProgressDialog(ClassA3.this);
            progressBarNya.setMessage("Sedang kirim Komentarnya...");
            progressBarNya.setIndeterminate(false);
            progressBarNya.setCancelable(true);
            progressBarNya.show();
        }






        @SuppressLint("LongLogTag")
        @Override
        protected String doInBackground(String... args) {
            // priksa jika proses tambah komentar berhasil
            // lihat TAG_BERHASIL
            int komentar_sukses;
//            String judulKomentar = args[0];
//            String komentar = args[1];
            //ini harus di ubah
            SharedPreferences sharedPrefNya = PreferenceManager.getDefaultSharedPreferences(ClassA3.this);
            String userNameNya = sharedPrefNya.getString("username_nya", "anon");
            String node = "A3";

            try {
                // menyusun parameter-nya
                List<NameValuePair> parameterNya = new ArrayList<NameValuePair>();
                parameterNya.add(new BasicNameValuePair("username_nya", userNameNya));
                parameterNya.add(new BasicNameValuePair("node", node));
//                parameterNya.add(new BasicNameValuePair("isi_komentar", komentar));

                Log.d("me-request!", "dimulai");

                //kirim data dari user ke halaman php script
                JSONObject jsonObjectNya = jsonParserNya.membuatHttpRequest(
                        LINK_TAMBAH_KOMENTAR, "POST", parameterNya);

                // jawaban dari JSON
                Log.d("mencoba kirim komentar", jsonObjectNya.toString());

                // Bagian JSON jika komentarnya berhasil/tidak terkirim
                komentar_sukses = jsonObjectNya.getInt(TAG_BERHASIL);
                if (komentar_sukses == 1) {
//                    Log.d("Komentar_nya telah di masukan!", jsonObjectNya.toString());
                    finish();
                    return jsonObjectNya.getString(TAG_PESAN);
                } else {
                    Log.d("Kirim komentar_nya Gagal!", jsonObjectNya.getString(TAG_PESAN));
                    return jsonObjectNya.getString(TAG_PESAN);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;

        }

//        protected void onPostExecute(String url_link_nya) {
//            // matikan progressBarnya dengan metode dismiss();
//            progressBarNya.dismiss();
//            if (url_link_nya != null){
//                Toast.makeText(ClassA1.this, url_link_nya, Toast.LENGTH_LONG).show();
//            }
//
//        }
//        @Override

    }
}
