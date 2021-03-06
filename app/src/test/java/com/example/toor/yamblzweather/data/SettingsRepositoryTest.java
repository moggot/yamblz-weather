package com.example.toor.yamblzweather.data;

import android.content.SharedPreferences;

import com.example.toor.yamblzweather.common.AnswerWithSelf;
import com.example.toor.yamblzweather.common.SyncEntity;
import com.example.toor.yamblzweather.data.models.places.PlaceDetails;
import com.example.toor.yamblzweather.data.models.places.PlacesAutocompleteModel;
import com.example.toor.yamblzweather.data.models.settings.SettingsPreference;
import com.example.toor.yamblzweather.data.models.weather.common.Coord;
import com.example.toor.yamblzweather.data.network.PlacesService;
import com.example.toor.yamblzweather.data.network.api.ApiKeys;
import com.example.toor.yamblzweather.data.network.api.GooglePlacesAPI;
import com.example.toor.yamblzweather.data.repositories.settings.SettingsRepository;
import com.example.toor.yamblzweather.data.repositories.settings.SettingsRepositoryImpl;
import com.example.toor.yamblzweather.domain.utils.TemperatureMetric;
import com.example.toor.yamblzweather.presentation.mvp.models.settings.SettingsModel;


import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.TestObserver;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.TestSubscriber;
import retrofit2.Retrofit;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;

/**
 * Tests for SettingsRepository and PlaceServices
 * Created by igor on 7/30/17.
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest({ Retrofit.Builder.class, Retrofit.class, PlacesService.class})

public class SettingsRepositoryTest {
    private static final String TEST_CITY_NAME = "TestName";
    private static final int TEST_AUTOCOMPLETE_SIZE = 1;

    private SharedPreferencesMock preferencesMock;

    @Mock GooglePlacesAPI api;
    @Mock PlaceDetails placeDetails;
    @Mock PlacesAutocompleteModel placesAutocompleteModel;

    private SettingsRepository prepareRepository() {
        return new SettingsRepositoryImpl(new SettingsPreference(preferencesMock.getMockedPreferences()),
            new PlacesService(ApiKeys.GOOGLE_PLACES_API_KEY));
    }

    @Before
    public void prepare() throws Exception {
        preferencesMock = new SharedPreferencesMock();

        RxAndroidPlugins.setInitMainThreadSchedulerHandler(
                __ -> Schedulers.trampoline());

        PowerMockito.mockStatic(Retrofit.Builder.class);
        PowerMockito.mockStatic(Retrofit.class);
        Retrofit retrofit = PowerMockito.mock(Retrofit.class);
        when(retrofit.create(any())).thenReturn(api);
        Retrofit.Builder builder = mock(Retrofit.Builder.class, new AnswerWithSelf(Retrofit.Builder.class));
        whenNew(Retrofit.Builder.class).withNoArguments().thenReturn(builder);
        doReturn(retrofit).when(builder).build();

        when(placeDetails.getName()).thenReturn(TEST_CITY_NAME);
        when(placesAutocompleteModel.getSize()).thenReturn(TEST_AUTOCOMPLETE_SIZE);
        when(api.getPlaceDetails(anyString(), anyString(), anyString()))
                .thenReturn(Single.fromCallable(() -> placeDetails));
        when(api.getPlacesAutocomplete(anyString(), anyString(), anyString(), anyString()))
                .thenReturn(Single.fromCallable(() -> placesAutocompleteModel));
    }

    @Test
    public void getCityNameTest() {
        SettingsRepository repository = prepareRepository();
        assertThat(repository.getSelectedCityName(), equalTo(preferencesMock.getMockedModel().getCityName()));
    }

    @Test
    public void loadPlacesAutocompleteTest() throws InterruptedException {
        SettingsRepository repository = prepareRepository();
        TestObserver<PlacesAutocompleteModel> testObserver = new TestObserver<>();
        repository.loadPlacesAutocomplete("some stirng").subscribe(testObserver);
        testObserver.awaitTerminalEvent();
        testObserver.assertNoErrors();
        testObserver.assertValue(model -> model.getSize() == TEST_AUTOCOMPLETE_SIZE);

    }

    @Test
    public void loadPlacesDetails() {
        SettingsRepository repository = prepareRepository();
        TestObserver<PlaceDetails> testObserver = new TestObserver<>();
        repository.loadPlaceDetails(TEST_CITY_NAME).subscribe(testObserver);
        testObserver.awaitTerminalEvent();
        testObserver.assertNoErrors();
        testObserver.assertValue(place -> place.getName().equals(TEST_CITY_NAME));
    }

    private static class SharedPreferencesMock {
        static final int DEFAULT_ID = 0;
        static final String DEFAULT_NAME = "A city named City";
        static final Coord DEFAULT_COORDS = new Coord(10.0, 10.0);

        private SharedPreferences mockedPreferences;
        private SharedPreferences.Editor mockedEditor;
        private SettingsModel model;

        public SharedPreferencesMock() {
            mockedPreferences = mock(SharedPreferences.class);
            mockedEditor = mock(SharedPreferences.Editor.class);
            model = new SettingsModel.Builder(TemperatureMetric.CELSIUS, 10).cityId(DEFAULT_ID)
                    .cityName(DEFAULT_NAME).coords(DEFAULT_COORDS).build();
            when(mockedPreferences.getString(eq(SettingsPreference.CITY_NAME), anyString())).thenReturn(model.getCityName());
        }

        SharedPreferences getMockedPreferences() {
            return mockedPreferences;
        }

        SettingsModel getMockedModel() {
            return model;
        }
    }
}