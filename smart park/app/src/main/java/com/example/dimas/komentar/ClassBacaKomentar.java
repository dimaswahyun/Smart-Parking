package com.example.dimas.komentar;

/**
 * Created by dimas on 06/09/2017.
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
        import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
        import android.content.SharedPreferences;
        import android.os.AsyncTask;
import android.os.Bundle;
        import android.preference.PreferenceManager;
        import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ClassBacaKomentar extends ListActivity {

    // Progress bar_nya
    private ProgressDialog progressBarNya;

    // lokasi script utk baca_komentar.php

    // biasanya localhost :
    // tapi untuk testing, lebih baik pakai alamat IP
    // kalau tak tahu IP-nya di windows silahkan buka
    // command prompt lalu ketik ipconfig
    // lihat IPv4 Address
    // kalau di mac ketik ifconfig dan cari ip dengan en0 atau en1
    // private static final String LINK_SEMUA_KOMENTAR =
    // "http://xxx.xxx.x.x.xxx/folderNya/baca_komentar.php";

    // coba di Emulator:jangan lupa ganti dengan IP sendiri yah
    private static final String LINK_SEMUA_KOMENTAR = "http://suksessidang.com/smartparking/baca_komentar.php?username_nya=";

    // kalau di server benaran:
    // private static final String LINK_SEMUA_KOMENTAR =
    // "http://www.namaWeb.com/folderNya/baca_komentar.php";

    // berikut adalah identitas dari JSON element yang
    // merespond dari halaman PHP (baca_komentar.php)
    private static final String TAG_SUKSES = "koneksi_sukses";
    private static final String TAG_NODE = "node";
    private static final String TAG_WAKTU_MASUK = "waktu_masuk";
    private static final String TAG_WAKTU_KELUAR = "waktu_keluar";
    private static final String TAG_POST_ID = "id_histori";
    private static final String TAG_USERNAME = "username_nya";
    private static final String TAG_ISI_KOMENTAR = "isi_komentar";
    private static final String TAG_SEMUA_KOMENTAR = "semua_komentar";

    // sebuah array utk semua komentar
    private JSONArray semuaKomentar = null;
    // atur semua komentar dalam bentuk berbaris(daftar).
    private ArrayList<HashMap<String, String>> susunanKomentar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // tampilan yg di pakai disini adalah baca_komentar.xml dan
        // bukan single_post.xml yah
        setContentView(R.layout.baca_komentar);


    }

    @Override
    protected void onResume() {
        super.onResume();
        // pasang(loading) semua komentar melalui AsyncTask
        new TampilSemuaKomentar().execute();
    }
    //pada class BacaKomentar ini juga akan ada intent baru utk
    //membuka halaman(activity) baru yaitu activity utk classTambahKomentar
    //maka di buat metode 'tambahKomentar' berikut ini yg akan di
    //panggil dari xml dengan metode onclick... lihat di 'baca_komentar.xml'
    public void tambahKomentar(View v) {
        Intent intentNya = new Intent(ClassBacaKomentar.this, ClassTambahKomentar.class);
        startActivity(intentNya);
    }

    /**
     * ambil posting terbaru dari server dengan
     * meng-update JSON-nya.
     */
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
        SharedPreferences sharedPrefNya = PreferenceManager.getDefaultSharedPreferences(ClassBacaKomentar.this);
        String userNameNya = sharedPrefNya.getString("username_nya", "anon");
        JSONObject jsonObjectNya = jsonParserNya.ambilJsonDariUrl(LINK_SEMUA_KOMENTAR+userNameNya);

        // ketika parsing sesuatu lewat JSON , ada baiknya
        // di pasang 'try catch exceptions' utk menanggkap hal2 yang
        // tak terduga(error) :
        try {

            semuaKomentar = jsonObjectNya.getJSONArray(TAG_SEMUA_KOMENTAR);

            // terus looping ke semua komentar yang telah ada sesuai
            // dengan apa yang di dapatkan oleh JSONObject
            for (int i = 0; i < semuaKomentar.length(); i++) {
                JSONObject komentarNya = semuaKomentar.getJSONObject(i);

                // ambil isi dari masing2 tag
                String node = komentarNya.getString(TAG_NODE);
                String waktu_masuk = komentarNya.getString(TAG_WAKTU_MASUK);
                String waktu_keluar = komentarNya.getString(TAG_WAKTU_KELUAR);



                // buatkan HashMap baru utk mencocokan TAG dan parameter-nya
                HashMap<String, String> map = new HashMap<String, String>();

                map.put(TAG_NODE, node);
                map.put(TAG_WAKTU_MASUK, waktu_masuk);
                map.put(TAG_WAKTU_KELUAR, waktu_keluar);

                susunanKomentar.add(map);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * masukan data yg sudah di parsing
     * ke dalam listview.
     */
    private void updateListViewNya() {
        //utk listActivity kita perlu memasang List Adapter dengan
        //mendeklarasikan-nya
        //SimpleAdapter ini akan memakai HashMap arrayList yang
        //telah di update.
        //gunakan komentar_tunggal.xml sebagai template utk
        //setiap item di dalam list dan perhatikan identitas GUI_nya
        //agar jangan salah mengikuti susunan-nya
        ListAdapter adapterNya = new SimpleAdapter(this, susunanKomentar,
                R.layout.komentar_tunggal, new String[] { TAG_NODE, TAG_WAKTU_MASUK, TAG_WAKTU_KELUAR
        }, new int[] { R.id.judul, R.id.komentar, R.id.usernameNya
        });

        // kalau semuanya sudah benar maka
        //pasanglah ListAdapter-nya
        setListAdapter(adapterNya);
        //ketika klik pada satu komentar sebenarnya
        //bisa melakukan sesuatu, misalnya pergi ke activity
        //lain atau ke link tapi disini saya pilih utk
        //tidak melakukan apa2
        ListView lv = getListView();
        lv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int posisiNya, long idNya) {

            }
        });
    }

    public class TampilSemuaKomentar extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBarNya = new ProgressDialog(ClassBacaKomentar.this);
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

        @Override
        protected void onPostExecute(Boolean tampilkanHasilNya) {
            super.onPostExecute(tampilkanHasilNya);
//setelah tampil hasilnya matikan progress barnya
//dengan metode dismiss();
            progressBarNya.dismiss();
            updateListViewNya();
        }
    }
}