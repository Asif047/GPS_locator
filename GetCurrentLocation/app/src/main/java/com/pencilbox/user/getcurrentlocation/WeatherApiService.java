package com.pencilbox.user.getcurrentlocation;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * Created by User on 7/8/2017.
 */

public interface WeatherApiService {
    @GET()
    Call<WeatherForecastResponse>getForecastResponse(@Url String urlString);
}
