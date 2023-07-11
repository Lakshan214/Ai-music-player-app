package com.example.aimusicplayer;

import androidx.appcompat.app.AppCompatActivity;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;

public class MainActivity2 extends AppCompatActivity {

    private ListView mSongsList;
    private String[] itemsAll;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        mSongsList = findViewById(R.id.songList);

        externalStoragePermission();
    }

    public void externalStoragePermission(){
        Dexter.withContext(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override public void onPermissionGranted(PermissionGrantedResponse response) {

                        displayAudioSongsName();
                    }
                    @Override public void onPermissionDenied(PermissionDeniedResponse response) {


                    }
                    @Override public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                      token.cancelPermissionRequest();
                    }
                }).check();
    }
    @SuppressLint("SuspiciousIndentation")
    public ArrayList<File>findSong(File file){

        ArrayList<File> arrayList = new  ArrayList<>();
        File[] allFiles = file.listFiles();

        for (File individualFile :allFiles)
        {
            if (individualFile.isDirectory() && !individualFile.isHidden()) {
                arrayList.addAll(findSong(individualFile));
            } else {
                if (individualFile.getName().endsWith(".mp3")) {
                    arrayList.add(individualFile);
                }
            }
        }

        return arrayList;

    }


        private void displayAudioSongsName()
        {
            final ArrayList<File> audioSongs =findSong(Environment.getExternalStorageDirectory());
            itemsAll = new String[audioSongs.size()];

            for(int songCounter=0; songCounter<audioSongs.size(); songCounter++)
            {
                itemsAll[songCounter] =audioSongs.get(songCounter).getName();

            }

            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(MainActivity2.this, android.R.layout.simple_list_item_1, itemsAll);
            mSongsList.setAdapter(arrayAdapter);

            mSongsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    String songName = mSongsList.getItemAtPosition(i).toString();
                    Intent intent = new Intent(MainActivity2.this,MainActivity.class);
                    intent.putExtra("song",audioSongs);
                    intent.putExtra("name",songName);
                    intent.putExtra("position",i);
                    startActivity(intent);

                }
            });
        }


}