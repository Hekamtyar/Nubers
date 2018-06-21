package com.example.hekmatyar.numbers;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.tapadoo.alerter.Alert;
import com.tapadoo.alerter.Alerter;
import com.tapadoo.alerter.OnHideAlertListener;

public class Welcome extends AppCompatActivity {

    public static Welcome welcome;
    private CustomVideoView videoview;
    private Button starts;
    private Button closes;
    private Button sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_welcome);
      SysApplication.getInstance().addActivity(this);
      starts = (Button) findViewById(R.id.btn_start);
      closes = (Button) findViewById(R.id.btn_close);
      sp = (Button) findViewById(R.id.btn_sp);
      initView();
       welcome = this;
       SysApplication.getInstance().query();
      starts.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Intent intent = new Intent(Welcome.this,MainActivity.class);
              intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
              startActivity(intent);

          }
      });


      closes.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              SysApplication.getInstance().exit();
          }
      });


      sp.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              toastMsg("游戏规则:将方块按照对应字符规则的顺序排列正确即为成功！");
          }
      });
    }

    @Override
    protected void onStart() {
        super.onStart();
        initView();
    }
    private boolean alertShow=true;
    private void toastMsg(String message){
        if(alertShow){
            alertShow=false;
            Alert mAlerter = Alerter.create(Welcome.this).
                    setText(message).
                    setDuration(2000).
                    setBackgroundColor(R.color.colorAccent).
                    show();
            mAlerter.setOnHideListener(new OnHideAlertListener() {
                @Override
                public void onHide() {
                    alertShow=true;
                }
            });
        }
    }
    private void initView() {

        videoview = (CustomVideoView) findViewById(R.id.videoview);
        videoview.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.saber));
        videoview.start();
        videoview.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                videoview.start();
            }
        });
    }

}

