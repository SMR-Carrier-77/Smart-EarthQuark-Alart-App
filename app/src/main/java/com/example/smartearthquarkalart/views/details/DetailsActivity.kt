package com.example.smartearthquarkalart.views.details

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.smartearthquarkalart.databinding.ActivityDetailsBinding
import com.example.smartearthquarkalart.views.dashboard.home.HomeFragment.Companion.lat
import com.example.smartearthquarkalart.views.dashboard.home.HomeFragment.Companion.lon
import org.maplibre.android.maps.MapLibreMap
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Marker
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

        Handler(Looper.getMainLooper()).postDelayed({
            binding.loadingContainer.visibility = View.GONE
            binding.scrollView.visibility = View.VISIBLE
        }, 3000)

        // Get data from intent
        val place = intent.getStringExtra("place") ?: "Unknown"
        val depth = intent.getStringExtra("depth") ?: "0"
        val magnitude = intent.getFloatExtra("magnitude", 0f)
        val latitude = intent.getDoubleExtra("latitude", 0.0)
        val longitude = intent.getDoubleExtra("longitude", 0.0)
        val time = intent.getLongExtra("time", 0L)
        val magType = intent.getStringExtra("magType") ?: "-"
        val tsunami = intent.getIntExtra("tsunami", 0)

        setupMap(latitude , longitude , place)

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

    private fun createRedDotIcon(): BitmapDrawable {
        val size = 40
        val bitmap =
            Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        // outer circle
        val outerPaint = Paint().apply {
            color = Color.RED
            alpha = 70
            style = Paint.Style.STROKE
            strokeWidth = 5f
            isAntiAlias = true
        }
        canvas.drawCircle(size / 2f, size / 2f, size / 2.4f, outerPaint)

        // inner dot
        val innerPaint = Paint().apply {
            color = Color.RED
            style = Paint.Style.FILL
            isAntiAlias = true
        }
        canvas.drawCircle(size / 2f, size / 2f, size / 6f, innerPaint)

        return BitmapDrawable(resources, bitmap)
    }


    private fun setupMap(lati : Double , longi : Double , place : String) {
        val map = binding.mapView
        map.setMultiTouchControls(true)

        val controller = map.controller
        controller.setZoom(10.0)

        val userPoint = GeoPoint(lati, longi)
        controller.setCenter(userPoint)

        val redDotIcon = createRedDotIcon()
       // val blueLocationIcon = createRedDotIcon()

        // User location
        val userMarker = Marker(map)
        userMarker.position = userPoint
        userMarker.icon = redDotIcon
        userMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)
        userMarker.title = "$place"
        map.overlays.add(userMarker)

        map.invalidate()
    }

    // -------- MapView lifecycle --------
    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }

    override fun onPause() {
        binding.mapView.onPause()
        super.onPause()
    }


    override fun onDestroy() {
        binding.mapView.onDetach() // Correct method for OSMDroid
        super.onDestroy()
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

}