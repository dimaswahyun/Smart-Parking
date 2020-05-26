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


public class ClassDenahParkir extends Activity {
    private Button A1,A2,A3,A4;
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
        setContentView(R.layout.denah_parkir);
        A1 = (Button)findViewById(R.id.A1);
        A2 = (Button)findViewById(R.id.A2);
        A3 = (Button)findViewById(R.id.A3);
        A4 = (Button)findViewById(R.id.A4);



//        A2.setOnClickListener(this);
//        A3.setOnClickListener(this);
//        A4.setOnClickListener(this);
//        A5.setOnClickListener(this);
//        A6.setOnClickListener(this);
//        A7.setOnClickListener(this);
//        A8.setOnClickListener(this);

        A1.setOnClickListener(new View.OnClickListener( ) {

            @Override
            public void onClick(View arg0) {


                startActivity(new Intent(ClassDenahParkir.this, ClassA1.class));
            }
        });
        A2.setOnClickListener(new View.OnClickListener( ) {

            @Override
            public void onClick(View arg0) {


                startActivity(new Intent(ClassDenahParkir.this, ClassA2.class));
            }
        });
        A3.setOnClickListener(new View.OnClickListener( ) {

            @Override
            public void onClick(View arg0) {


                startActivity(new Intent(ClassDenahParkir.this, ClassA3.class));
            }
        });
        A4.setOnClickListener(new View.OnClickListener( ) {

            @Override
            public void onClick(View arg0) {


                startActivity(new Intent(ClassDenahParkir.this, ClassA4.class));
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
        SharedPreferences sharedPrefNya = PreferenceManager.getDefaultSharedPreferences(ClassDenahParkir.this);
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
                if (node.equals("A1")) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            HashMap<String, String> map = new HashMap<String, String>();

                            map.put(TAG_USERNAME, username_nya);
//                            map.put(TAG_JUDUL_KOMENTAR, judul_komentar);
                            A1.setText(map.put(TAG_USERNAME, username_nya));
                            A1.setBackgroundResource(R.drawable.booked);
                            A1.setEnabled(false);
                        }
                    });
                } else if (node.equals("A2")) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            HashMap<String, String> map = new HashMap<String, String>();

                            map.put(TAG_USERNAME, username_nya);
//                            map.put(TAG_JUDUL_KOMENTAR, judul_komentar);
                            A2.setText(map.put(TAG_USERNAME, username_nya));
                            A2.setBackgroundResource(R.drawable.booked);
                            A2.setEnabled(false);
                        }
                    });
                } else if (node.equals("A3")) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            HashMap<String, String> map = new HashMap<String, String>();

                            map.put(TAG_USERNAME, username_nya);
//                            map.put(TAG_JUDUL_KOMENTAR, judul_komentar);
                            A3.setText(map.put(TAG_USERNAME, username_nya));
                            A3.setBackgroundResource(R.drawable.booked);
                            A3.setEnabled(false);
                        }
                    });
                } else if (node.equals("A4")) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            HashMap<String, String> map = new HashMap<String, String>();

                            map.put(TAG_USERNAME, username_nya);
//                            map.put(TAG_JUDUL_KOMENTAR, judul_komentar);
                            A4.setText(map.put(TAG_USERNAME, username_nya));
                            A4.setBackgroundResource(R.drawable.booked);
                            A4.setEnabled(false);
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


    public class TampilSemuaKomentar extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBarNya = new ProgressDialog(ClassDenahParkir.this);
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
            timer.schedule(new TimerTask(){
                @Override
                public void run(){
                    jsonDataNyaDiUpdate();
                }
            }, 1000, 1000);
        }
    }


}
