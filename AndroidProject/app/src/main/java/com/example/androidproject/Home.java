package com.example.androidproject;

import android.Manifest;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TabHost;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class Home extends AppCompatActivity {

    ListView listViewMP3;
    Button Play, Stop, Prev, Next, GoodNight;
    ProgressBar pbMP3;
    TextView tvMP3, tvTime;
    ArrayList<String> mp3List;
    String selectedMP3;
    String mp3Path = Environment.getExternalStorageDirectory().getPath() + "/";
    MediaPlayer mPlayer;
    Intent intent;
    private int pausePosition = 0;
    int page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Lemon Music");

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MODE_PRIVATE);

        // SDCard의 파일을 읽어서 리스트뷰에 출력
        mp3List = new ArrayList<String>(); // 가변적 문자열

        for (int i = 1; i <= 30; i++) {

            mp3List.add("music" + i + ".mp3");

        }

        listViewMP3 = (ListView) findViewById(R.id.listViewMP3);
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
        Next = (Button) findViewById(R.id.Next);
        Prev = (Button) findViewById(R.id.Prev);
        tvTime = (TextView) findViewById(R.id.tvTime);

        Play.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    if (mPlayer != null) {
                        mPlayer.stop();
                        mPlayer = null;
                    }
                        mPlayer = new MediaPlayer();
                        mPlayer.setDataSource(mp3Path + selectedMP3);
                        tvMP3.setText("실행중인 음악 :  " + selectedMP3);
                        mPlayer.prepare();
                        mPlayer.start();

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

        // 정지
        Stop.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    pausePosition = mPlayer.getCurrentPosition();
                    mPlayer.pause();
                        Log.d(">>>12","sd:" + pausePosition);

                } catch (Exception ie) {
                    ie.printStackTrace();
                }
                mPlayer.seekTo(0);
                pbMP3.setProgress(0); // 현재 위치를 지정
                tvMP3.setText("실행중인 음악 :  ");

                tvTime.setText("진행 시간 : ");
            }
        });

        Next.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                try {
                    mPlayer.stop();
                    mPlayer = null;

                    mPlayer = new MediaPlayer();


                    if (selectedMP3.length() == 10) {

                        selectedMP3 = selectedMP3.substring(0, 5) +
                                (Integer.parseInt(selectedMP3.substring(5, 6)) + 1) + selectedMP3.substring(6, 10);
                        Log.d(">>>", " ccccccc : " + selectedMP3);
                    } else if ((Integer.parseInt(selectedMP3.substring(5, 7)) > 30)) {
                        selectedMP3 = "music1.mp3";
                        Log.d(">>123123>>", selectedMP3.substring(5, 7));
                        Log.d(">>asdsad>>", selectedMP3);

                    } else if (selectedMP3.length() == 11) {
                        selectedMP3 = selectedMP3.substring(0, 5) +
                                (Integer.parseInt(selectedMP3.substring(5, 7)) + 1) + selectedMP3.substring(7, 11);

                    }

                    mPlayer.setDataSource(mp3Path + selectedMP3);

                    tvMP3.setText("실행중인 음악 :  " + selectedMP3);
                    mPlayer.prepare();
                    mPlayer.start();

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

        Prev.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                try {
                    mPlayer.stop();
                    mPlayer = null;

                    mPlayer = new MediaPlayer();

                    if (selectedMP3.length() == 10) {

                        selectedMP3 = selectedMP3.substring(0, 5) + (Integer.parseInt(selectedMP3.substring(5, 6)) - 1) + selectedMP3.substring(6, 10);

                    } else if ((Integer.parseInt(selectedMP3.substring(5, 7)) < 1)) {
                        selectedMP3 = "music30.mp3";
                        Log.d(">>123123>>", selectedMP3.substring(5, 7));
                        Log.d(">>asdsad>>", selectedMP3);

                    } else if (selectedMP3.length() == 11) {
                        selectedMP3 = selectedMP3.substring(0, 5) + (Integer.parseInt(selectedMP3.substring(5, 7)) - 1) + selectedMP3.substring(7, 11);

                    }

                    mPlayer.setDataSource(mp3Path + selectedMP3);

                    tvMP3.setText("실행중인 음악 :  " + selectedMP3);
                    mPlayer.prepare();
                    mPlayer.start();

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


    }

    public void onClick (View view){
        Intent intent = new Intent(this, Alram.class);
        startActivity(intent);
    }

}
