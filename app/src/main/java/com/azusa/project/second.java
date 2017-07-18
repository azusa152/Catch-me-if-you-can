package com.azusa.project;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by azusa on 2015/1/6.
 */
public class second extends Activity {
    private TextView one,two,three,four,five,textView3,textView4,textView5,textView6,textView7,textView;
    private Button back;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.rank);

        textView =(TextView)findViewById(R.id.textView);
        textView3 =(TextView)findViewById(R.id.textView3);
        textView4 =(TextView)findViewById(R.id.textView4);
        textView5 =(TextView)findViewById(R.id.textView5);
        textView6 =(TextView)findViewById(R.id.textView6);
        textView7 =(TextView)findViewById(R.id.textView7);
         one =(TextView)findViewById(R.id.one);
         two =(TextView)findViewById(R.id.two);
         three =(TextView)findViewById(R.id.three);
         four =(TextView)findViewById(R.id.four);
         five =(TextView)findViewById(R.id.five);
         back=(Button)findViewById(R.id.back);

        Intent intent=this.getIntent();
        Bundle bundle=intent.getExtras();

        Float rk1 = bundle.getFloat("rk1");
        Float rk2 = bundle.getFloat("rk2");
        Float rk3 = bundle.getFloat("rk3");
        Float rk4 = bundle.getFloat("rk4");
        Float rk5 = bundle.getFloat("rk5");



        one.setText(String.valueOf(rk1));
        two.setText(String.valueOf(rk2));
        three.setText(String.valueOf(rk3));
        four.setText(String.valueOf(rk4));
        five.setText(String.valueOf(rk5));

        back.setOnClickListener(listener);


    }
    private Button.OnClickListener listener =new Button.OnClickListener(){

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            finish();
        }

    };
}
