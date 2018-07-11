package com.hoooopa.progressbar;

import android.print.PrinterId;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private Button bt;
    private  int i = 0 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bt = (Button) findViewById(R.id.bt);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        progressBar.setTotalNum(20);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressBar.setFinishedNum(i);
                i ++;
                if (i >20){
                    i=0;
                }
            }
        });
    }
}
