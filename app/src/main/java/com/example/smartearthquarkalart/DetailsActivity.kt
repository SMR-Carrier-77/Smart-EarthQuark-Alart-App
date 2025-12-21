package com.example.smartearthquarkalart

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.smartearthquarkalart.databinding.ActivityDetailsBinding
import org.maplibre.android.annotations.Icon
import org.maplibre.android.annotations.IconFactory
import org.maplibre.android.annotations.MarkerOptions
import org.maplibre.android.camera.CameraUpdateFactory
import org.maplibre.android.geometry.LatLng
import org.maplibre.android.maps.MapLibreMap
import org.maplibre.android.maps.OnMapReadyCallback
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsBinding
    private lateinit var map: MapLibreMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get data from intent
        val place = intent.getStringExtra("place") ?: "Unknown"
        val depth = intent.getStringExtra("depth") ?: "0"
        val magnitude = intent.getFloatExtra("magnitude", 0f)
        val latitude = intent.getDoubleExtra("latitude", 0.0)
        val longitude = intent.getDoubleExtra("longitude", 0.0)
        val time = intent.getLongExtra("time", 0L)
        val magType = intent.getStringExtra("magType") ?: "-"
        val tsunami = intent.getIntExtra("tsunami", 0)

        // Bind data to views
        binding.tvMagnitude.text = magnitude.toString()
        binding.tvLocation.text = place
        binding.tvDepth.text = "Depth: $depth km"
        binding.tvLatitude.text = "Latitude: $latitude"
        binding.tvLongitude.text = "Longitude: $longitude"



        if (magType=="md"){
            binding.magType.text = "Micro"
        }else if (magType=="ml"){
            binding.magType.text = "Normal"
        }else if (magType=="mb"){
            binding.magType.text = "Medium"
        }else if (magType=="ms"){
            binding.magType.text = "Big"
        }else if (magType=="mw"){
            binding.magType.text = "Dangerous"
        }

        // Time formatting
        val formattedTime = try {
            SimpleDateFormat(
                "dd MMM yyyy, hh:mm a",
                Locale.getDefault()
            ).format(Date(time))
        } catch (e: Exception) {
            time.toString()
        }
        binding.tvTime.text = formattedTime

        // Magnitude color based on severity
        binding.tvMagnitude.setTextColor(
            when {
                magnitude >= 5f -> Color.RED
                magnitude >= 3f -> Color.parseColor("#FFA500")
                else -> Color.GREEN
            }
        )
    }

    // MapView lifecycle methods
    override fun onStart() {
        super.onStart()
        binding.mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }

    override fun onPause() {
        binding.mapView.onPause()
        super.onPause()
    }

    override fun onStop() {
        binding.mapView.onStop()
        super.onStop()
    }

    override fun onDestroy() {
        binding.mapView.onDestroy()
        super.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.mapView.onSaveInstanceState(outState)
    }
}