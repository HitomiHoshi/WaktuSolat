package com.hitomi.waktusolat.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.hitomi.waktusolat.R;
import com.hitomi.waktusolat.callback.JakimCallback;
import com.hitomi.waktusolat.data.Message;
import com.hitomi.waktusolat.data.WaktuSolatListData;
import com.hitomi.waktusolat.data.ZonData;
import com.hitomi.waktusolat.service.JakimService;
import com.hitomi.waktusolat.service.SharedPreferencesService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private JakimService jakimService;
    private SharedPreferencesService sharedPreferencesService;

    TextView imsak;
    TextView subuh;
    TextView syuruk;
    TextView zohor;
    TextView asar;
    TextView maghrib;
    TextView isyak;
    Spinner zon;
    SwipeRefreshLayout refresh;
    ArrayAdapter<ZonData> zonAdapter;
    ArrayList<ZonData> zonDataList;

    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat receiveFormat = new SimpleDateFormat("HH:mm:ss");
    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat viewFormat = new SimpleDateFormat("hh:mm aa");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imsak = findViewById(R.id.waktu_imsak);
        subuh = findViewById(R.id.waktu_subuh);
        syuruk = findViewById(R.id.waktu_syuruk);
        zohor = findViewById(R.id.waktu_zohor);
        asar = findViewById(R.id.waktu_asar);
        maghrib = findViewById(R.id.waktu_maghrib);
        isyak = findViewById(R.id.waktu_isyak);
        zon = findViewById(R.id.zon_spinner);
        refresh = findViewById(R.id.refresh_solat);

        jakimService = new JakimService(MainActivity.this);
        sharedPreferencesService = new SharedPreferencesService(MainActivity.this);

        refresh.setOnRefreshListener(this);

        zonDataList = new ArrayList<>();
        zonDataList.add(new ZonData("Pulau Aur dan Pulau Pemanggil", "JHR01"));
        zonDataList.add(new ZonData("Johor Bahru, Kota Tinggi, Mersing, Kulai", "JHR02"));
        zonDataList.add(new ZonData("Batu Pahat, Muar, Segamat, Gemas Johor, Tangkak", "JHR02"));

        zonDataList.add(new ZonData("Kota Setar, Kubang Pasu, Pokok Sena (Daerah Kecil)", "KDH01"));
        zonDataList.add(new ZonData("Kuala Muda Yan, Pendang", "KDH02"));
        zonDataList.add(new ZonData("Padang Terap, Sik", "KDH03"));
        zonDataList.add(new ZonData("Baling", "KDH04"));
        zonDataList.add(new ZonData("Bandar Baharu, Kulim", "KDH05"));
        zonDataList.add(new ZonData("Langkawi", "KDH06"));
        zonDataList.add(new ZonData("Puncak Gunung Jerai", "KDH07"));

        zonDataList.add(new ZonData("Bachok, Kota Bharu, Machang, Pasir Mas, Pasir Puteh, Tanah Merah, Tumpat, Kuala Krai, Mukim Chiku", "KTN01"));
        zonDataList.add(new ZonData("Gua Musang (Daerah Galas Dan Bertam), Jeli, Jajahan, Kecil Lojing", "KTN02"));

        zonDataList.add(new ZonData("SELURUH NEGERI MELAKA", "MLK01"));

        zonDataList.add(new ZonData("Tampin, Jempol", "NGS01"));
        zonDataList.add(new ZonData("Jelebu, Kuala Pilah, Rembau", "NGS04"));
        zonDataList.add(new ZonData("Port Dickson, Seremban", "NGS03"));

        zonDataList.add(new ZonData("Pulau Tioman", "PHG01"));
        zonDataList.add(new ZonData("Kuantan, Pekan, Rompin, Muadzam Shah", "PHG02"));
        zonDataList.add(new ZonData("Jerantut, Temerloh, Maran, Bera, Chenor, Jengka", "PHG03"));
        zonDataList.add(new ZonData("Bentong, Lipis, Raub", "PHG04"));
        zonDataList.add(new ZonData("Genting Sempah, Janda Baik, Bukit Tinggi", "PHG05"));
        zonDataList.add(new ZonData("Cameron Highlands, Genting Higlands, Bukit Fraser", "PHG06"));

        zonDataList.add(new ZonData("Kangar, Padang Besar, Arau", "PLS01"));

        zonDataList.add(new ZonData("Seluruh Negeri Pulau Pinang", "PNG01"));

        zonDataList.add(new ZonData("Tapah, Slim River, Tanjung Malim", "PRK01"));
        zonDataList.add(new ZonData("Kuala Kangsar, Sg. Siput, Ipoh, Batu Gajah, Kampar", "PRK02"));
        zonDataList.add(new ZonData("Lengong, Pengkalan Hulu, Grik", "PRK03"));
        zonDataList.add(new ZonData("Temengor, Belum", "PRK04"));
        zonDataList.add(new ZonData("Kg Gajah, Teluk Intan, Bagan Datuk, Seri Inskandar, Beruas, Parit, Lumut, Sitiawan, Pulau Pangkor", "PRK05"));
        zonDataList.add(new ZonData("Selama, Tailing, Bagan Serai, Parit Buntar", "PRK06"));
        zonDataList.add(new ZonData("Bukit Larut", "PRK07"));

        zonDataList.add(new ZonData("Bahagian Sandakan (Timur), Bukit Garam, Semawang, Temanggong, Tambisan, Bandar Sandakan, Sukau", "SBH01"));
        zonDataList.add(new ZonData("Beluran, Telupid,Pinangah,", "SBH02"));
        zonDataList.add(new ZonData("Lahad Datu, Silabukan, Kunak, Sahabat, Semporna, Tungku, Bahagian Tawau (Timur)", "SBH03"));
        zonDataList.add(new ZonData("Bandar Tawau, Balong, Merotai, Kalabakan, Bahagian Kudat", "SBH04"));
        zonDataList.add(new ZonData("Kudat, Kota Marudu, Pitas, Pulau Banggi, Bahagian Kudat", "SBH05"));
        zonDataList.add(new ZonData("Gunung Kinabalu", "SBH06"));
        zonDataList.add(new ZonData("Kota Kinabalu, Ranau, Kota Belud, Tuaran, Penampang, Papar, Putatan, Bahagian Pantai Barat", "SBH07"));
        zonDataList.add(new ZonData("Pensiangan, Keningau, Tambunan, Nabawan, Bahagian Pendalaman (Atas)", "SBH08"));
        zonDataList.add(new ZonData("Beaufort, Kuala Penyu, Sipitang, Tenom, Long Pasia, Membakut, Weston, Bahagian Pendalaman (Bawah)", "SBH09"));

        zonDataList.add(new ZonData("Gombak, Petaling, Sepang, Hulu Langat, Hulu Selangor, S.Alam", "SGR01"));
        zonDataList.add(new ZonData("Kuala Selangor, Sabak Bernam", "SGR02"));
        zonDataList.add(new ZonData("Klang, Kuala Langat", "SGR01"));

        zonDataList.add(new ZonData("Limbang, Lawas, Sundar, Trusan", "SWK01"));
        zonDataList.add(new ZonData("Miri, Niah, Bekenu, Sibuti, Marudi", "SWK02"));
        zonDataList.add(new ZonData("Pandan, Belaga, Suai, Tatau, Sebauh, Bintulu", "SWK03"));
        zonDataList.add(new ZonData("Sibu, Mukah, Dalat, Song, Igan, Oya, Balingian, Kanowit, Kapit", "SWK04"));
        zonDataList.add(new ZonData("Sarikei, Matu, Julau, Rajang, Daro, Bintangor, Belawai", "SWK05"));
        zonDataList.add(new ZonData("Lubok Antu, Sri Aman, Roban, Debak, Kabong, Lingga, Engkelili, Betong, Spaoh, Pusa, Saratok", "SWK06"));
        zonDataList.add(new ZonData("Serian, Simunjan, Samarahan, Sebuyau, Meludam", "SWK07"));
        zonDataList.add(new ZonData("Kuching, Bau, Lundu, Sematan", "SWK08"));
        zonDataList.add(new ZonData("Zon Khas (Kampung Patarikan)", "SWK09"));

        zonDataList.add(new ZonData("Kuala Terengganu, Marang, Kuala Nerus", "TRG01"));
        zonDataList.add(new ZonData("Besut, Setiu", "TRG02"));
        zonDataList.add(new ZonData("Hulu Terengganu", "TRG03"));
        zonDataList.add(new ZonData("Dungun, Kemaman", "TRG04"));

        zonDataList.add(new ZonData("Kuala Lumpur, Putrajaya", "WLY01"));
        zonDataList.add(new ZonData("Labuan", "WLY02"));

        zonAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_item, zonDataList);
        zonAdapter.setDropDownViewResource(android.R.layout.simple_expandable_list_item_1);
        zon.setAdapter(zonAdapter);

        zon.setSelection(sharedPreferencesService.getZon());
        zon.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ZonData data = (ZonData) adapterView.getSelectedItem();

                sharedPreferencesService.setZon(i);
                jakimService.getWaktuSolatByZon(data.id, new JakimCallback() {
                    @Override
                    public void onSuccess(WaktuSolatListData result) throws ParseException {
                        imsak.setText(viewFormat.format(Objects.requireNonNull(receiveFormat.parse(result.waktuSolatList.get(0).description))));
                        subuh.setText(viewFormat.format(Objects.requireNonNull(receiveFormat.parse(result.waktuSolatList.get(1).description))));
                        syuruk.setText(viewFormat.format(Objects.requireNonNull(receiveFormat.parse(result.waktuSolatList.get(2).description))));
                        zohor.setText(viewFormat.format(Objects.requireNonNull(receiveFormat.parse(result.waktuSolatList.get(3).description))));
                        asar.setText(viewFormat.format(Objects.requireNonNull(receiveFormat.parse(result.waktuSolatList.get(4).description))));
                        maghrib.setText(viewFormat.format(Objects.requireNonNull(receiveFormat.parse(result.waktuSolatList.get(5).description))));
                        isyak.setText(viewFormat.format(Objects.requireNonNull(receiveFormat.parse(result.waktuSolatList.get(6).description))));

                    }

                    @Override
                    public void onError(Message result) {

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public void onRefresh() {
        zon.setSelection(sharedPreferencesService.getZon());
        refresh.setRefreshing(false);
    }
}