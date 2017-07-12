package com.example.toor.yamblzweather.presentation.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.toor.yamblzweather.R;

public class WeatherFragment extends Fragment {

    public static WeatherFragment newInstance() {
        return new WeatherFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        getActivity().setTitle(R.string.title_weather);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_weather, container, false);
    }

}
