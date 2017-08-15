package com.idealcn.define.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final MyView myView = (MyView) findViewById(R.id.myView);


       Button btnUpdate = (Button) findViewById(R.id.update);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                myView.setMyViewText("人民英雄永垂不朽!");
                startActivity(new Intent(MainActivity.this,MyViewGroupActivity.class));
            }
        });
    }
}
