package com.example.hekmatyar.numbers;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tapadoo.alerter.Alert;
import com.tapadoo.alerter.Alerter;
import com.tapadoo.alerter.OnHideAlertListener;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    public static MainActivity mainActivity;
    public static boolean isStart=false;
    private boolean isClick=true;
    private Button mBtnStart,mBtnChange;
    private TextView mTvTime,mTvHistory,mTvNum;
    private NumberHrdView mNumberHrdView;
    private CustomVideoView videoviews;
    private Button returns;


    private String[] number_01={"VIII","VI","III","I","V","VII","II","IV"};
    private String[] number_02={"F","A","D","C","H","B","G","E"};
    private String[] number_03={"5","2","6","7","4","1","8","3"};
    private String[] number_04={"IV","V","III","VII","VII","II","VIII","I"};
    private String[] number_05={"7","6","3","5","4","1","8","2"};
    private String[] number_06={"A","D","H","E","B","G","F","C"};

    private String[] number_01_color={"#8B7500","#FF83FA","#EE9A49","#FF3030","#F08080","#CD0000","#E0E0E0","#C1CDC1"};
    private String[] number_02_color={"#EE2C2C","#8B8B00","#A2CD5A","#EEAD0E","#7D9EC0","#48D1CC","#BDB76B","#B03060"};
    private String[] number_03_color={"#548B54","#87CEFA","#CD0000","#8B0000","#B3EE3A","#8B795E","#CDAD00","#48D1CC"};
    private String[] number_04_color={"#87CEFA","#473C8B","#FF00FF","#8B795E","#48D1CC","#8B0000","#FF3030","#C1CDC1"};
    private String[] number_05_color={"#CD3333","#CD6600","#C1CDCD","#87CEFA","#43CD80","#FF00FF","#C1CDC1","#DB7093"};
    private String[] number_06_color={"#8B0000","#BDB76B","#B22222","#FF00FF","#C1CDC1","#4169E1","#87CEFA","#8B5A00"};
    private RelativeLayout mLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainActivity = this;
        SysApplication.getInstance().addActivity(this);
        returns = (Button) findViewById(R.id.btn_return);
        mBtnStart=(Button)findViewById(R.id.btn_start);
        mTvTime=(TextView)findViewById(R.id.tv_time);
        mNumberHrdView=(NumberHrdView)findViewById(R.id.numberhrdview);
        mBtnChange=(Button)findViewById(R.id.btn_start_next);
        findViewById(R.id.btn_start_pre).setOnClickListener(this);
        mLayout=(RelativeLayout)findViewById(R.id.layout_success);
        mTvHistory=(TextView)findViewById(R.id.tv_history);
        mTvNum=(TextView)findViewById(R.id.tv_num);
        mBtnStart.setOnClickListener(this);
        mBtnChange.setOnClickListener(this);
        initView();
        createHistory();

        Welcome.welcome.finish();
        returns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,Welcome.class);
                startActivity(intent);
            }
        });

        mNumberHrdView.setOnSuccess(new NumberHrdView.OnSuccessListener() {
            @Override
            public void success(int type) {
                mLayout.setVisibility(View.VISIBLE);
                if(type==0){
                    type=1;
                }
                SharedPreUtils.put(MainActivity.this,"abnerType",type+"");
                String scale= SharedPreUtils.getString(MainActivity.this,"scale");
                if(TextUtils.isEmpty(scale)){
                    SharedPreUtils.put(MainActivity.this,"scale",""+lastTime);
                }else{
                    if(Integer.parseInt(scale)>lastTime){
                        SharedPreUtils.put(MainActivity.this,"scale",""+lastTime);
                    }
                }
                createHistory();
                createNumber();
                mHandler.sendEmptyMessageDelayed(2000,3000);
            }

            @Override
            public void toast() {
                toastMsg("请点击开始键后再滑动!");
            }
        });

    }

    private boolean alertShow=true;
    private void toastMsg(String message){
        if(alertShow){
            alertShow=false;
            Alert mAlerter = Alerter.create(MainActivity.this).
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
    private void createHistory(){
        try {
            String history=SharedPreUtils.getString(MainActivity.this,"scale");
            if(!TextUtils.isEmpty(history)){

            }
        }catch (Exception e){

        }
        String abnerType=SharedPreUtils.getString(MainActivity.this,"abnerType");
        if(!TextUtils.isEmpty(abnerType)){
            clickNum=Integer.parseInt(abnerType)+1;
            mTvNum.setText("第"+clickNum+"关");
            createNumber();
        }
    }

    private int clickNum=1;
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_start_pre://上一关
                clickNum--;
                if(clickNum==0){
                    clickNum=1;
                    toastMsg("已经是第1关了!");
                    return;
                }
                createNumber();
                break;

            case R.id.btn_start_next://下一关
                String abnerType=SharedPreUtils.getString(MainActivity.this,"abnerType");
                if(TextUtils.isEmpty(abnerType)){
                    toastMsg("这一关过了才能解锁下一关!");
                    return;
                }
                if(clickNum>Integer.parseInt(abnerType)){
                    toastMsg("只有通过了第"+clickNum+"关，才能解锁第"+(clickNum+1)+"关!");
                    return;
                }

                if(clickNum>=6){
                    toastMsg("已经是最后1关了!");
                    return;
                }
                clickNum++;
                createNumber();
                break;
            case R.id.btn_start://开始
                if(isClick){
                    mBtnStart.setBackground(getDrawable(R.drawable.a_start));
                    mHandler.sendEmptyMessage(1000);
                    MainActivity.isStart=true;
                    isClick=false;
                }else{


                    mBtnStart.setBackground();
                    mHandler.removeMessages(1000);
                    MainActivity.isStart=false;
                    isClick=true;
                }
                break;
        }

    }

    /**
     * 点重置后，初始化
     * */
    private void createNumber() {
        MainActivity.isStart=false;
        isClick=true;
        mBtnStart.setText("开始");
        mHandler.removeMessages(1000);
        mTvTime.setText("00:00");
        timeNum=0;
        mTvNum.setText("第"+clickNum+"关");
        if(clickNum==1){
            mNumberHrdView.setNumberData(clickNum,number_03,number_03_color);
        }else  if(clickNum==2){
            mNumberHrdView.setNumberData(clickNum,number_06,number_06_color);
        }else  if(clickNum==3){
            mNumberHrdView.setNumberData(clickNum,number_02,number_02_color);
        }else  if(clickNum==4){
            mNumberHrdView.setNumberData(clickNum,number_04,number_04_color);
        }else  if(clickNum==5){
            mNumberHrdView.setNumberData(clickNum,number_01,number_01_color);
        }else  if(clickNum==6){
            mNumberHrdView.setNumberData(clickNum,number_05,number_05_color);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!isClick){
            mHandler.removeMessages(1000);
            mHandler.sendEmptyMessage(1000);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mHandler.removeMessages(1000);
    }

    private int timeNum=0;
    private int lastTime;
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1000:
                    timeNum=timeNum+1;
                    String time = getTime();
                    lastTime=timeNum;
                    mTvTime.setText(time);
                    mHandler.sendEmptyMessageDelayed(1000,1000);
                    break;
                case 2000:
                    mLayout.setVisibility(View.GONE);
                    break;
            }

        }
    };

    private String getTime() {
        String time="当前用时：00:0"+timeNum;
        if(timeNum<=9){
            time="当前用时：00:0"+timeNum;
        }else if(timeNum>9&&timeNum<60){
            time="当前用时：00:"+timeNum;
        }else{
            int mm= timeNum/60;
            int ss= timeNum%60;
            String s=ss+"";
            s=s.length()>1?s:"0"+s;
            if(mm<10){
                time="当前用时：0"+mm+":"+s;
            }else{
                time="当前用时："+mm+":"+s;
            }
        }
        return time;
    }

    private String getTime(int num) {
        String time="";
        if(num<=9){
            time="00:0"+num;
        }else if(num>9&&num<60){
            time="00:"+num;
        }else{
            int mm= num/60;
            int ss= num%60;
            String s=ss+"";
            s=s.length()>1?s:"0"+s;
            if(mm<10){
                time="0"+mm+":"+s;
            }else{
                time=""+mm+":"+s;
            }
        }
        return time;
    }
    private void initView() {

        videoviews = (CustomVideoView) findViewById(R.id.videoviews);
        //设置播放加载路径
        videoviews.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.lingmeng));
        //播放
        videoviews.start();
        //循环播放
        videoviews.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                videoviews.start();
            }
        });
    }
}