package com.lxb.lintools.timer;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.lxb.lintools.R;

import java.lang.ref.WeakReference;

public class TimerTestActivity extends AppCompatActivity implements View.OnClickListener {

    TextView textView;

    MyHandler handler;
    Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer_tset);

        initView();
        initTool();
    }

    private void initTool() {
        handler = new MyHandler(this);
        runnable = new Runnable() {
            int count = 0;

            @Override
            public void run() {
                Message msg = new Message();
                msg.obj = "这是消息：" + (count++) + "===当前线程是：" + Thread.currentThread().getName();
                Log.d("Thread", "当前是否是主线程：" + (Thread.currentThread() == Looper.getMainLooper().getThread()));
                handler.sendMessage(msg);
            }
        };
    }

    private void initView() {
        textView = findViewById(R.id.text_view);
        findViewById(R.id.btn_start).setOnClickListener(this);
        findViewById(R.id.btn_pause).setOnClickListener(this);
        findViewById(R.id.btn_stop).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_start) {
            start(0);
        } else if (v.getId() == R.id.btn_pause) {
            pause();
        } else if (v.getId() == R.id.btn_stop) {
            stop();
        }
    }

    private void stop() {
        handler.removeCallbacks(runnable);
    }

    private void pause() {
        handler.removeCallbacks(runnable);
    }

    private void start(int second) {
        handler.postDelayed(runnable, second * 1000);
    }

    public void showMessage(String text) {
        textView.setText(text);
    }

    private static class MyHandler extends Handler {
        WeakReference<TimerTestActivity> activityWeakReference;
        String text;

        public MyHandler(TimerTestActivity activity) {
            activityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            if (msg != null) {
                text = msg.obj.toString();
                activityWeakReference.get().showMessage(msg.obj.toString());
                activityWeakReference.get().start(1);
            }
        }
    }
}
