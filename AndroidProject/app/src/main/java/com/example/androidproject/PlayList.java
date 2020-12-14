package com.example.androidproject;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TabHost;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class PlayList extends AppCompatActivity {

    ListView listViewMP3;
    Button Play, Stop;
    ProgressBar pbMP3;
    TextView tvMP3, tvTime;
    ArrayList<String> mp3List;
    String selectedMP3;
    String mp3Path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/";
    MediaPlayer mPlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_list);
        setTitle("Lemon Music");





        ActivityCompat.requestPermissions(this,
                new String[] {android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, MODE_PRIVATE);

        // SDCard의 파일을 읽어서 리스트뷰에 출력
        mp3List = new ArrayList<String>(); // 가변적 문자열

        for (int i = 1; i <= 3; i++ ) {

            mp3List.add("song" + i + ".mp3");

        }


        ListView listViewMP3 = (ListView) findViewById(R.id.listViewMP3);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_single_choice, mp3List);
        listViewMP3.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listViewMP3.setAdapter(adapter);
        listViewMP3.setItemChecked(0, true);

        listViewMP3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                selectedMP3 = mp3List.get(arg2);
            }
        });
        selectedMP3 = mp3List.get(0);

        Play = (Button) findViewById(R.id.Play);
        Stop = (Button) findViewById(R.id.Stop);
        tvMP3 = (TextView) findViewById(R.id.tvMP3);
        pbMP3 = (ProgressBar) findViewById(R.id.pbMP3);

        tvTime = (TextView) findViewById(R.id.tvTime);

        Play.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    mPlayer = new MediaPlayer();
                    mPlayer.setDataSource(mp3Path + selectedMP3);
                    mPlayer.prepare();
                    mPlayer.start();
                    Play.setClickable(false);
                    Stop.setClickable(true);
                    tvMP3.setText("실행중인 음악 :  " + selectedMP3);

                    new Thread() {
                        SimpleDateFormat timeFormat = new SimpleDateFormat(
                                "mm:ss");

                        public void run() {
                            if (mPlayer == null)
                                return;
                            pbMP3.setMax(mPlayer.getDuration());
                            while (mPlayer.isPlaying()) {
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        pbMP3.setProgress(mPlayer
                                                .getCurrentPosition());
                                        tvTime.setText("진행 시간 : "
                                                + timeFormat.format(mPlayer
                                                .getCurrentPosition()));
                                    }
                                });
                                SystemClock.sleep(200);
                            }
                        }
                    }.start();

                } catch (IOException e) {
                }
            }
        });

        Stop.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mPlayer.stop();
                mPlayer.reset();
                Play.setClickable(true);
                Stop.setClickable(false);
                tvMP3.setText("실행중인 음악 :  ");

                pbMP3.setProgress(0); // 현재 위치를 지정
                tvTime.setText("진행 시간 : ");
            }
        });
        Stop.setClickable(false);












        TabHost tabHost = findViewById(R.id.tabhost);

        tabHost.setup();

        // 탭하나씩 정의
        TabHost.TabSpec tabSpec1 = tabHost.newTabSpec("HOME").setIndicator("홈");
        tabSpec1.setContent(R.id.tab1);
        tabHost.addTab(tabSpec1);

        TabHost.TabSpec tabSpec2 = tabHost.newTabSpec("CATEGORY").setIndicator("추천");
        tabSpec2.setContent(R.id.tab2);
        tabHost.addTab(tabSpec2);

        TabHost.TabSpec tabSpec3 = tabHost.newTabSpec("SEARCH").setIndicator("검색");
        tabSpec3.setContent(R.id.tab3);
        tabHost.addTab(tabSpec3);

        TabHost.TabSpec tabSpec4 = tabHost.newTabSpec("PLAYLIST").setIndicator("내음악");
        tabSpec4.setContent(R.id.tab4);
        tabHost.addTab(tabSpec4);

        TabHost.TabSpec tabSpec5 = tabHost.newTabSpec("PROFILE").setIndicator("MY");
        tabSpec5.setContent(R.id.tab5);
        tabHost.addTab(tabSpec5);

        tabHost.setCurrentTab(0);

        for (int i = 0; i<tabHost.getTabWidget().getChildCount(); i++) {
            TextView tv = (TextView) tabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
            tv.setTextColor(getResources().getColor(R.color.White));
        }

    }
}
