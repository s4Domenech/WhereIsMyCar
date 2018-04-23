/*
 * Copyright (C) 2018 Sylvia Domenech.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.syl.whereismycar.datasource.mock;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationProvider;
import android.os.Bundle;
import android.util.Log;

import com.example.syl.whereismycar.ui.presenter.MainPresenter;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class LocationData implements LocationListener {
    MainPresenter mainPresenter;

    public void setMainPresenter(MainPresenter mainPresenter) {
        this.mainPresenter = mainPresenter;
    }

    @Override
    public void onLocationChanged(Location loc) {
        String Text = "Mi ubicacion actual es: " + "\n Lat = "
                + loc.getLatitude() + "\n Long = " + loc.getLongitude();
        System.out.println(Text);
        String calle = "";
        if (loc.getLatitude() != 0.0 && loc.getLongitude() != 0.0) {
            try {
                Geocoder geocoder = new Geocoder(mainPresenter.getContext(), Locale.getDefault());
                List<Address> list = geocoder.getFromLocation(
                        loc.getLatitude(), loc.getLongitude(), 1);
                if (!list.isEmpty()) {
                    Address DirCalle = list.get(0);
                    System.out.println(DirCalle.getAddressLine(0));
                    calle = DirCalle.getAddressLine(0);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        mainPresenter.setLocation(loc, calle);
    }

    @Override
    public void onProviderDisabled(String provider) {
        System.out.println("GPS Desactivado");
    }

    @Override
    public void onProviderEnabled(String provider) {
        System.out.println("GPS Activado");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        switch (status) {
            case LocationProvider.AVAILABLE:
                Log.d("debug", "LocationProvider.AVAILABLE");
                break;
            case LocationProvider.OUT_OF_SERVICE:
                Log.d("debug", "LocationProvider.OUT_OF_SERVICE");
                break;
            case LocationProvider.TEMPORARILY_UNAVAILABLE:
                Log.d("debug", "LocationProvider.TEMPORARILY_UNAVAILABLE");
                break;
            default:
                Log.d("debug", "Undefined error");
                break;
        }
    }
}