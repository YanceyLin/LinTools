package com.lxb.lintools;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.lxb.lintools.life.XLifeCycleTestActivity;
import com.lxb.lintools.timer.TimerTestActivity;
import com.lxb.lintools.views.Indication.IndicationTestActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.bt_circle_indication).setOnClickListener(this);
        findViewById(R.id.bt_life_cycle).setOnClickListener(this);
        findViewById(R.id.bt_time).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_circle_indication:
                startActivity(new Intent(MainActivity.this, IndicationTestActivity.class));
                break;
            case R.id.bt_life_cycle:
                startActivity(new Intent(MainActivity.this, XLifeCycleTestActivity.class));
                break;
            case R.id.bt_time:
                startActivity(new Intent(MainActivity.this, TimerTestActivity.class));
                break;
            default:
                break;
        }
    }
}
