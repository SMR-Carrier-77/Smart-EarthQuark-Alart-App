package com.example.smartearthquarkalart.dependencyinjection

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import org.maplibre.android.MapLibre
import org.maplibre.android.WellKnownTileServer

@HiltAndroidApp
class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        // Initialize MapLibre globally
        MapLibre.getInstance(
            this,
            null, // apiKey (null if not needed)
            WellKnownTileServer.MapLibre
        )

    }

}