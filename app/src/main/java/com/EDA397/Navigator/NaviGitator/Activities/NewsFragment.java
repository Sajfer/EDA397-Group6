package com.EDA397.Navigator.NaviGitator.Activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.EDA397.Navigator.NaviGitator.R;

/**
 * Created by QuattroX on 2014-04-09.
 */
public class NewsFragment extends Fragment {

    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_news, container, false);

        return view;
    }
}