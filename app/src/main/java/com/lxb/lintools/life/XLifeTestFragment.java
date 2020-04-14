package com.lxb.lintools.life;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lin.toollibs.views.life.XLifeManager;
import com.lin.toollibs.views.life.listener.XLifecycleListener;
import com.lxb.lintools.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class XLifeTestFragment extends Fragment {

    private TextView textView;

    private XLifecycleListener lifecycleListener;
    private String text;

    public XLifeTestFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_xlife_test, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        textView = view.findViewById(R.id.text_view);
        config();
    }

    private void config() {
        textView.setText(text);
        if (lifecycleListener != null) {
            XLifeManager.with(getContext())
                    .setLifeCycleListener(lifecycleListener)
                    .build();
        }
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setLifecycleListener(XLifecycleListener lifecycleListener) {
        this.lifecycleListener = lifecycleListener;
    }
}
