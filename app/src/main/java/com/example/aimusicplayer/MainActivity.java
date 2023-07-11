package com.example.aimusicplayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity<MainActivityt> extends AppCompatActivity {
  private RelativeLayout parentRelativeLayout;
  private SpeechRecognizer speechRecognizer;
  private Intent speechRecognizerIntent;
  private String keeper="";

  private ImageView pausePlayBtn,nextBtn,previousBtn;
  private ImageView imageView;
  private TextView songNameTxt;
  private RelativeLayout lowerRelativeLayout;
  private Button voiceEnableBtn;
  private String mode ="ON";
  private MediaPlayer mediaPlayer;
  private int position;
  private ArrayList<File>mySong;
  private String mSongName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkVoiceCommandPermission();

//        mediaPlayer = new MediaPlayer(); // Initialize the mediaPlayer object
//
//        validateReceiveValue();

        pausePlayBtn =findViewById(R.id.pause_btn);
        nextBtn= findViewById(R.id.next_btn);
        previousBtn = findViewById(R.id.previous_btn);
        imageView=findViewById(R.id.logo);

        lowerRelativeLayout = findViewById(R.id.lower);
        voiceEnableBtn=findViewById(R.id.voice_enable_btn);
        imageView = findViewById(R.id.logo);
        songNameTxt=findViewById(R.id.songName);






        parentRelativeLayout =findViewById(R.id.parentRelativeLayout);
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(MainActivity.this);
        speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        mediaPlayer = new MediaPlayer(); // Initialize the mediaPlayer object

        validateReceiveValue();


        imageView.setBackgroundResource(R.drawable.logo);

        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {

            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float v) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int i) {

            }

            @Override
            public void onResults(Bundle bundle) {

                ArrayList<String>matchesFound = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);


                if(matchesFound != null){

                    if (mode.equals("ON"))
                    {
                        keeper=matchesFound.get(0);
                        Toast.makeText(MainActivity.this, "command song is "+keeper, Toast.LENGTH_SHORT).show();
                        if(keeper.equals("stop"))
                        {
                            playpauseSong();
                            Toast.makeText(MainActivity.this, "command song is "+keeper, Toast.LENGTH_SHORT).show();
                        } else if (keeper.equals("play"))
                        {
                            playpauseSong();
                            Toast.makeText(MainActivity.this, "Command song is  "+keeper , Toast.LENGTH_SHORT).show();

                        } else if (keeper.equals("play next"))
                        {
                            playNextSon();
                            Toast.makeText(MainActivity.this, "Command song is  "+keeper , Toast.LENGTH_SHORT).show();

                        }else if (keeper.equals("play next"))
                        {
                            playPreviousSong();
                            Toast.makeText(MainActivity.this, "Command song is  "+keeper , Toast.LENGTH_SHORT).show();

                        }


                    }
//

                    }
                }


            @Override
            public void onPartialResults(Bundle bundle) {

            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });

        parentRelativeLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        speechRecognizer.startListening(speechRecognizerIntent);
                        keeper ="";
                       break;
                    case MotionEvent.ACTION_UP:
                        speechRecognizer.stopListening();
                        break;
                }
                return false;
            }
        });

        voiceEnableBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mode.equals("ON"))
                {
                    mode ="OFF";
                    voiceEnableBtn.setText("Voice Enable Mode - OFF ");
                    lowerRelativeLayout.setVisibility(View.VISIBLE);
                }
                else{
                    mode ="ON";
                    voiceEnableBtn.setText("Voice Enable Mode - ON ");
                    lowerRelativeLayout.setVisibility(View.GONE);

                }
            }
        });

        pausePlayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playpauseSong();
            }
        });

        previousBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mediaPlayer.getCurrentPosition()>0)
                {
                    playPreviousSong();
                }
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mediaPlayer.getCurrentPosition()>0)
                {
                    playNextSon();
                }
            }
        });

    }





 private void  validateReceiveValue(){
     if (mediaPlayer != null) {
         mediaPlayer.stop();
         mediaPlayer.release();
         mediaPlayer = null; // Reset mediaPlayer to null after release
     }
        Intent intent =getIntent();
        Bundle bundle = intent.getExtras();

        mySong=(ArrayList) bundle.getStringArrayList("song");
        mSongName =mySong.get(position).getName();
        String songName=intent.getStringExtra("name");

        songNameTxt.setText(songName);
        songNameTxt.setSelected(true);

        position =bundle.getInt("position",0);
        Uri uri = Uri.parse(mySong.get(position).toString());

        mediaPlayer = MediaPlayer.create(MainActivity.this,uri);
        mediaPlayer.start();
 }




    public void checkVoiceCommandPermission()
    {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
           if(!(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.RECORD_AUDIO)== PackageManager.PERMISSION_GRANTED))
           {
               Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:"+ getPackageName()));
               startActivity(intent);
               finish();
           }
        }
    }

    private void  playpauseSong()
    {
      imageView.setBackgroundResource(R.drawable.four);
      if(mediaPlayer.isPlaying()){
          pausePlayBtn.setImageResource(R.drawable.play);
          mediaPlayer.pause();
      }
      else
      {
          pausePlayBtn.setImageResource(R.drawable.pause);
          mediaPlayer.start();

          imageView.setBackgroundResource(R.drawable.five);
      }
    }

    private void playNextSon(){
        mediaPlayer.pause();
        mediaPlayer.stop();
        mediaPlayer.release();

        position = ((position+1)%mySong.size());

        Uri uri= Uri.parse(mySong.get(position).toString());

        mediaPlayer = MediaPlayer.create(MainActivity.this,uri);

        mSongName = mySong.get(position).toString();
        songNameTxt.setText(mSongName);
        mediaPlayer.start();

        imageView.setBackgroundResource(R.drawable.three);

        if(mediaPlayer.isPlaying()){
            pausePlayBtn.setImageResource(R.drawable.play);
            mediaPlayer.pause();
        }
        else
        {
            pausePlayBtn.setImageResource(R.drawable.play);
            mediaPlayer.start();

            imageView.setBackgroundResource(R.drawable.five);
        }


    }


    private void playPreviousSong()
    {
        mediaPlayer.pause();
        mediaPlayer.stop();
        mediaPlayer.release();

        position = ((position-1)<0 ? (mySong.size()-1):(position-1));
        Uri uri = Uri.parse(mySong.get(position).toString());
        mediaPlayer = MediaPlayer.create(MainActivity.this,uri);

        mediaPlayer = MediaPlayer.create(MainActivity.this,uri);

        mSongName = mySong.get(position).toString();
        songNameTxt.setText(mSongName);
        mediaPlayer.start();

        imageView.setBackgroundResource(R.drawable.two );

        if(mediaPlayer.isPlaying()){
            pausePlayBtn.setImageResource(R.drawable.play);
            mediaPlayer.pause();
        }
        else
        {
            pausePlayBtn.setImageResource(R.drawable.play);
            mediaPlayer.start();

            imageView.setBackgroundResource(R.drawable.five);
        }


    }
}