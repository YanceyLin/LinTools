package com.lxb.lintools.views.Indication;

import android.app.Activity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;

import com.lin.toollibs.views.indication.DotCircleIndicator;
import com.lxb.lintools.R;

public class IndicationTestActivity extends Activity {

    ViewPager viewPager;
    IndicationViewPageAdapter viewpageAdapterIndication;

    DotCircleIndicator dotCircleIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indication_test);

        viewPager = findViewById(R.id.view_page);
        dotCircleIndicator = findViewById(R.id.dotIndicator);
//        circleIndicator = findViewById(R.id.circle_indicator);

        viewpageAdapterIndication = new IndicationViewPageAdapter(this);
        viewPager.setAdapter(viewpageAdapterIndication);

        viewPager.setCurrentItem(0);

        //也可直接放入viewpager
        //dotCircleIndicator.attachToViewpager(viewPager);
        dotCircleIndicator.setCircleCount(viewpageAdapterIndication.getCount());
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
                dotCircleIndicator.onPageScrolled(i, v, i1);
            }

            @Override
            public void onPageSelected(int i) {

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }
}
