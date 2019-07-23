package com.lxb.lintools.views;

import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.lin.toollibs.views.indication.DotCircleIndicator;
import com.lxb.lintools.R;

public class IndicationTestActivity extends Activity {

    ViewPager viewPager;
    ViewpageAdapter viewpageAdapter;

    DotCircleIndicator dotCircleIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indication_test);

        viewPager = findViewById(R.id.viewpage);
        dotCircleIndicator = findViewById(R.id.dotIndicator);
//        circleIndicator = findViewById(R.id.circle_indicator);

        viewpageAdapter = new ViewpageAdapter(this);
        viewPager.setAdapter(viewpageAdapter);

        viewPager.setCurrentItem(0);

        dotCircleIndicator.attachToViewpager(viewPager);
    }
}
