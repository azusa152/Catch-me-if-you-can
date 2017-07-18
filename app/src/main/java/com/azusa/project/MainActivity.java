package com.azusa.project;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;



import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends ActionBarActivity implements SensorEventListener {                 //要加implements SensorEventListene 才會生成 sensor changred

    private TextView textHeight,txt1,txt2,title;
    private SensorManager aSensorManager;
    private Sensor aSensor;
    private ProgressBar progressbar;
    private Button btnStart,btnAgain,btnRank,btnExit;
    Boolean flagStart=false,gameStart=false;                                                          //gamestart==遊戲開始        ,flagstart==時間開始
    private static final String FILENAME="data.txt";                                                 //  檔案存取處
    private float msec=0,vel=0;                                                                            //msec==計算微渺
    private float firstset[]=new float[3],passtime=0,pre=0,now=0,azusa=0,first=0,move=0,judge=0;              // firstset== xyz 初始值    , passtime== 拋上時間
    private int sec=0,time=0;                                                                        //time==時間        ,sec==秒數

    private double distance=0;
    private int rankcount=0;
    private float rank[]=new float[1024];
    static Random random = new Random();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textHeight=(TextView)findViewById(R.id.textHeight);                                          //set view
        txt1=(TextView)findViewById(R.id.textView1);
        txt2=(TextView)findViewById(R.id.textView2);
        title=(TextView)findViewById(R.id.title);
        progressbar=(ProgressBar)findViewById(R.id.progressBar);
        btnStart=(Button)findViewById(R.id.btnStart);
        btnAgain=(Button)findViewById(R.id.btnAgain);
        btnRank=(Button)findViewById(R.id.btnRank);
        btnExit=(Button)findViewById(R.id.btnExit);

        Timer timer=new Timer();                                                                    // timer
        timer.schedule(timertask, 0, 10);

        aSensorManager=(SensorManager) getSystemService(SENSOR_SERVICE);                       //sensor
        aSensor  = aSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        aSensorManager.registerListener(this, aSensor, aSensorManager.SENSOR_DELAY_GAME);   //3-30ms

        btnStart.setOnClickListener(listener);                                                        //button
        btnAgain.setOnClickListener(listener);
        btnRank.setOnClickListener(listener);
        btnExit.setOnClickListener(listener);

    }

//*************************button 關聯****************************************************************
    private Button.OnClickListener listener=new Button.OnClickListener()
    {

        @Override
        public void onClick(View v) {
            switch(v.getId())
            {
                case R.id.btnStart:
                    gameStart=true;
                    firstset[0]=0;
                    firstset[1]=0;
                    firstset[2]=0;
                    passtime=0;
                    progressbar.setVisibility(View.VISIBLE);
                    btnStart.setVisibility(View.INVISIBLE);
                    Toast.makeText(MainActivity.this,"warnning:be sure to catch your phone!! ",Toast.LENGTH_SHORT).show();
                    sec=0;
                    msec=0;
                    break;

                case R.id.btnAgain:
                    gameStart=true;
                    firstset[0]=0;
                    firstset[1]=0;
                    firstset[2]=0;
                    passtime=0;
                    progressbar.setVisibility(View.VISIBLE);
                    Toast.makeText(MainActivity.this,"warnning:be sure to catch your phone!! ",Toast.LENGTH_SHORT).show();
                    sec=0;
                    msec=0;
                    break;

                case R.id.btnRank:
                    rankcount=0;
                    try{
                        InputStream in = openFileInput(FILENAME);
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                        String s;
                        while((s = reader.readLine())!= null){
                            rankcount++;
                            rank[rankcount]=Float.parseFloat(s);
                        }
                        reader.close();
                    }catch(IOException e){
                        e.printStackTrace();
                    }
                    quicksort(rank,0,rankcount);
                    Bundle bundle=new Bundle();

                    if(rankcount>4)
                    {
                        bundle.putFloat("rk1", rank[rankcount]);
                        bundle.putFloat("rk2", rank[rankcount - 1]);
                        bundle.putFloat("rk3", rank[rankcount - 2]);
                        bundle.putFloat("rk4", rank[rankcount - 3]);
                        bundle.putFloat("rk5", rank[rankcount - 4]);
                    }
                    else if(rankcount==4)
                    {
                        bundle.putFloat("rk1", rank[rankcount]);
                        bundle.putFloat("rk2", rank[rankcount - 1]);
                        bundle.putFloat("rk3", rank[rankcount - 2]);
                        bundle.putFloat("rk4", rank[rankcount - 3]);
                        bundle.putFloat("rk5", 0);

                    }
                    else if(rankcount==3)
                    {
                        bundle.putFloat("rk1", rank[rankcount]);
                        bundle.putFloat("rk2", rank[rankcount - 1]);
                        bundle.putFloat("rk3", rank[rankcount - 2]);
                        bundle.putFloat("rk4", 0);
                        bundle.putFloat("rk5", 0);

                    }
                    else if(rankcount==2)
                    {
                        bundle.putFloat("rk1", rank[rankcount]);
                        bundle.putFloat("rk2", rank[rankcount - 1]);
                        bundle.putFloat("rk3", 0);
                        bundle.putFloat("rk4", 0);
                        bundle.putFloat("rk5", 0);

                    }
                    else if(rankcount==1)
                    {
                        bundle.putFloat("rk1", rank[rankcount]);
                        bundle.putFloat("rk2", 0);
                        bundle.putFloat("rk3", 0);
                        bundle.putFloat("rk4", 0);
                        bundle.putFloat("rk5", 0);

                    }
                    else if(rankcount==0)
                    {
                        bundle.putFloat("rk1", 0);
                        bundle.putFloat("rk2", 0);
                        bundle.putFloat("rk3", 0);
                        bundle.putFloat("rk4", 0);
                        bundle.putFloat("rk5", 0);

                    }

                    Intent intent=new Intent();
                    intent.setClass(MainActivity.this,second.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    break;

                case R.id.btnExit:
                    finish();
                    break;


            }
        }
    };



 //********************************排序*************************************************************
    private void quicksort(float grade[],int begin, int count)
    {
        int i,j;
        float pivot;

        if (begin >= count) { return; }
        pivot = grade[begin];
        i = begin+1;
        j = count;

        while (true)
        {
            while (i <= count)
            {
                if (grade[i] > pivot) break;
                i++;
            }

            while (j > begin)
            {
                if (grade[j] < pivot) break;
                j--;
            }

            if (i > j) { break; }
            float temp = grade[i];
            grade[i] = grade[j];
            grade[j] = temp;
        }
        float temp = grade[begin];
        grade[begin] = grade[j];
        grade[j] = temp;

        quicksort(grade,begin, j-1);
        quicksort(grade, j+1,count);
    }

//*********************************時間關聯**********************************************************

    private Handler handler=new Handler()
    {
        public void handleMessage(Message msg)
        {

            super.handleMessage(msg);
            switch (msg.what)
            {
                case 1:


                    sec=(time/100)%60;
                    msec=time%100;
            }

        }
    };
    private TimerTask timertask =new TimerTask()
    {

        @Override
        public void run() {
            if(flagStart)
            {
                time++;
                Message message=new Message();
                message.what=1;
                handler.sendMessage(message);
            }

        }

    };


 //********************************menu關聯*********************************************************

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            FileOutputStream fout=null;
            try {
                fout=openFileOutput(FILENAME, MODE_PRIVATE);
                fout.close();
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            Toast.makeText(MainActivity.this,"clear ranklist ",Toast.LENGTH_SHORT).show();
        }

        else if (id == R.id.action_about)
        {
            Toast.makeText(MainActivity.this,"made by azusa,bin,asdfz ",Toast.LENGTH_LONG).show();
        }



        return super.onOptionsItemSelected(item);
    }

//**********************************sensor關聯********************************************************
    @Override
    public void onSensorChanged(SensorEvent event) {
        float d0=event.values[0],d1=event.values[1],d2=event.values[2];
        BigDecimal bd0= new BigDecimal(d0);
        BigDecimal bd1= new BigDecimal(d1);
        BigDecimal bd2= new BigDecimal(d2);
        now=event.values[0]+event.values[1]+event.values[2];
        azusa=now-pre;
        move=now-first;
        passtime=sec+(msec/100);


        if(azusa<0)
            azusa=azusa*-1;
        if(move<0)
            move=move*-1;
        if(now<0)
            judge=now*-1;
        else
            judge=now;

        if(gameStart==true&&firstset[0]==0&&firstset[1]==0&&firstset[2]==0&&flagStart==false)               //遊戲開始，記錄初始值
        {
            bd0=bd0.setScale(2, BigDecimal.ROUND_HALF_UP);                                     // 小數後面二位, 四捨五入
            bd1=bd1.setScale(2, BigDecimal.ROUND_HALF_UP);
            bd2=bd2.setScale(2, BigDecimal.ROUND_HALF_UP);
            firstset[0]=bd0.floatValue();
            firstset[1]=bd1.floatValue();
            firstset[2]=bd2.floatValue();
            first=firstset[0]+firstset[1]+firstset[2];
        }

        else if( move>1 && gameStart==true&&flagStart==false)
        {
            flagStart=true;                                                                           //開始計時
            vel=move;

        }


        else if(gameStart==true&& judge<=0.5&&azusa<=0.5&& flagStart==true)                                                                                                                                                                                //當達到最高點時，停止並計算高度
        {
           NumberFormat nf = NumberFormat.getInstance();                                               //印出小數點後2位
           nf.setMaximumFractionDigits( 2 );

           passtime=sec+(msec/100);
           distance=((vel*passtime)+(0.5*10*(passtime*passtime)))/10;
           textHeight.setText(String.valueOf(nf.format(distance)));

            FileOutputStream fout=null;
            BufferedOutputStream buffout=null;
            try {
                fout=openFileOutput(FILENAME,MODE_APPEND);
                buffout=new BufferedOutputStream(fout);
                try {
                    buffout.write(textHeight.getText().toString().getBytes());
                    buffout.write("\n".getBytes());
                    buffout.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            flagStart=false;                                                                           //初始值
            gameStart=false;
            move=0;
            first=0;
            distance=0;
            passtime=0;
            sec=0;
            msec=0;
            pre=0;
            now=0;
            azusa=0;
            time=0;
            btnAgain.setVisibility(View.VISIBLE);
            txt1.setVisibility(View.VISIBLE);
            txt2.setVisibility(View.VISIBLE);
            textHeight.setVisibility(View.VISIBLE);
            progressbar.setVisibility(View.INVISIBLE);
            title.setVisibility(View.INVISIBLE);
            return;

        }
        else if(gameStart==true&&move<=1&& flagStart==true&&passtime>5)                                                                                                                                                                                //當達到最高點時，停止並計算高度
        {

            textHeight.setText("ERROR");


            flagStart=false;                                                                           //初始值
            gameStart=false;
            move=0;
            first=0;
            distance=0;
            passtime=0;
            sec=0;
            msec=0;
            pre=0;
            now=0;
            azusa=0;
            sec=0;
            time=0;
            btnAgain.setVisibility(View.VISIBLE);
            txt1.setVisibility(View.INVISIBLE);
            txt2.setVisibility(View.VISIBLE);
            textHeight.setVisibility(View.VISIBLE);
            progressbar.setVisibility(View.INVISIBLE);
            title.setVisibility(View.INVISIBLE);
            return;

        }


        pre=now;


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }



}




