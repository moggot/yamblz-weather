package com.example.toor.yamblzweather.presentation.di.modules;

import com.example.toor.yamblzweather.domain.interactors.WeatherInteractor;
import com.example.toor.yamblzweather.presentation.di.scopes.ScreenScope;
import com.example.toor.yamblzweather.presentation.presenter.WeatherFragmentPresenter;
import com.example.toor.yamblzweather.presentation.presenter.WeatherFragmentPresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module
public class WeatherScreenModule {

    @ScreenScope
    @Provides
    public WeatherFragmentPresenter provideWeatherFragmentPresenter(WeatherInteractor interactor) {
        return new WeatherFragmentPresenterImpl(interactor);
    }
}