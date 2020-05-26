package com.example.dimas.komentar;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;


public class ClassDenahParkir2 extends Activity {
    private Button A5,A6,A7,A8;
    final Context context = this;
    private static final String LINK_SEMUA_KOMENTAR = "http://suksessidang.com/smartparking/lihat_denah.php?";
    private static final String TAG_SUKSES = "koneksi_sukses";
    private static final String TAG_JUDUL_KOMENTAR = "judul_komentar";
    private static final String TAG_SEMUA_KOMENTAR = "semua_komentar";
    private static final String NODENYA = "node";
    private static final String TAG_USERNAME = "username";
    private static final String TAG_ISI_KOMENTAR = "isi_komentar";

    // sebuah array utk semua komentar
    private JSONArray semuaKomentar = null;
    // atur semua komentar dalam bentuk berbaris(daftar).
    private ArrayList<HashMap<String, String>> susunanKomentar;
    // coba di server jauh(remote), server bayaran:
    // private static final String LINK_TAMBAH_KOMENTAR =
    // http://www.namaWeb.com/folderNya/tambah_komentar.php;

    // berikut adalah identitas dari JSON element yang
    // merespond dari halaman PHP (tambah_komentar.php)
    private static final String TAG_BERHASIL = "sukses";
    private static final String TAG_PESAN = "pesan";
    private EditText isiJudul, isiKomentar;
    private Button  kirimKomentar;

    // utk progress bar_nya
    private ProgressDialog progressBarNya;

    // utk tambah komentar perlu deklarasi class JSON
    ClassJSONParser jsonParserNya = new ClassJSONParser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.denah_parkir2);
        A5 = (Button)findViewById(R.id.A5);
        A6 = (Button)findViewById(R.id.A6);
        A7 = (Button)findViewById(R.id.A7);
        A8 = (Button)findViewById(R.id.A8);


//        A2.setOnClickListener(this);
//        A3.setOnClickListener(this);
//        A4.setOnClickListener(this);
//        A5.setOnClickListener(this);
//        A6.setOnClickListener(this);
//        A7.setOnClickListener(this);
//        A8.setOnClickListener(this);


        A5.setOnClickListener(new View.OnClickListener( ) {

            @Override
            public void onClick(View arg0) {


                startActivity(new Intent(ClassDenahParkir2.this, ClassA5.class));
            }
        });
        A6.setOnClickListener(new View.OnClickListener( ) {

            @Override
            public void onClick(View arg0) {


                startActivity(new Intent(ClassDenahParkir2.this, ClassA6.class));
            }
        });
        A7.setOnClickListener(new View.OnClickListener( ) {

            @Override
            public void onClick(View arg0) {


                startActivity(new Intent(ClassDenahParkir2.this, ClassA7.class));
            }
        });
        A8.setOnClickListener(new View.OnClickListener( ) {

            @Override
            public void onClick(View arg0) {


                startActivity(new Intent(ClassDenahParkir2.this, ClassA8.class));
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        // pasang(loading) semua komentar melalui AsyncTask
        new TampilSemuaKomentar().execute();
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
        SharedPreferences sharedPrefNya = PreferenceManager.getDefaultSharedPreferences(ClassDenahParkir2.this);
        final String userNameNya = sharedPrefNya.getString("username" , "anonnymous");
        JSONObject jsonObjectNya = jsonParserNya.ambilJsonDariUrl(LINK_SEMUA_KOMENTAR+query);



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
                System.out.print("@@@@@@@@@@@@@@@@@@@@@@@@@@" + node);
                if (node.equals("A5")) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            HashMap<String, String> map = new HashMap<String, String>();

                            map.put(TAG_USERNAME, username_nya);
//                            map.put(TAG_JUDUL_KOMENTAR, judul_komentar);
                            A5.setText(map.put(TAG_USERNAME, username_nya));
                            A5.setBackgroundResource(R.drawable.booked);
                            A5.setEnabled(false);
                        }
                    });
                } else if (node.equals("A6")) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            HashMap<String, String> map = new HashMap<String, String>();

                            map.put(TAG_USERNAME, username_nya);
//                            map.put(TAG_JUDUL_KOMENTAR, judul_komentar);
                            A6.setText(map.put(TAG_USERNAME, username_nya));
                            A6.setBackgroundResource(R.drawable.booked);
                            A6.setEnabled(false);
                        }
                    });
                } else if (node.equals("A7")) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            HashMap<String, String> map = new HashMap<String, String>();

                            map.put(TAG_USERNAME, username_nya);
//                            map.put(TAG_JUDUL_KOMENTAR, judul_komentar);
                            A7.setText(map.put(TAG_USERNAME, username_nya));
                            A7.setBackgroundResource(R.drawable.booked);
                            A7.setEnabled(false);
                        }
                    });
                } else if (node.equals("A8")) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            HashMap<String, String> map = new HashMap<String, String>();

                            map.put(TAG_USERNAME, username_nya);
//                            map.put(TAG_JUDUL_KOMENTAR, judul_komentar);
                            A8.setText(map.put(TAG_USERNAME, username_nya));
                            A8.setBackgroundResource(R.drawable.booked);
                            A8.setEnabled(false);
                        }
                    });
                } else {
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            HashMap<String, String> map = new HashMap<String, String>();
//
//                            map.put(TAG_USERNAME, username_nya);
////                            map.put(TAG_JUDUL_KOMENTAR, judul_komentar);
//                            A5.setText(map.put(TAG_USERNAME, username_nya));
//                            A5.setBackgroundColor(Color.GREEN);
//                            A5.setEnabled(false);
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


    public class TampilSemuaKomentar extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBarNya = new ProgressDialog(ClassDenahParkir2.this);
            progressBarNya.setMessage("Tunggu yah, masih loading ini teh...");
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
            timer.schedule(new TimerTask(){
                @Override
                public void run(){
                    jsonDataNyaDiUpdate();
                }
            }, 1000, 1000);
        }
    }


}
