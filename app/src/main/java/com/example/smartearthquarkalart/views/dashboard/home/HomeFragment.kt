package com.example.smartearthquarkalart.views.dashboard.home

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.smartearthquarkalart.EarthquakeAdapter
import com.example.smartearthquarkalart.data.models.Earthquake_Data_Class
import com.example.smartearthquarkalart.base.BaseFragment
import com.example.smartearthquarkalart.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONArray
import org.maplibre.android.annotations.Icon
import org.maplibre.android.annotations.IconFactory
import org.maplibre.android.annotations.MarkerOptions
import org.maplibre.android.camera.CameraPosition
import org.maplibre.android.geometry.LatLng
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    var dataList: MutableList<Earthquake_Data_Class> = mutableListOf()
    val locations = mutableListOf<LatLng>()

    companion object {
        var lat = 23.8103
        var lon = 90.4125
    }

    override fun setListener() {
        loadSavedLocation()

        // JSON data load & map update
        loadData()
    }

    private fun loadSavedLocation() {
        val sharedPref = requireContext().getSharedPreferences("user_location", Context.MODE_PRIVATE)
        val savedLat = sharedPref.getString("latitude", null)
        val savedLon = sharedPref.getString("longitude", null)

        if (savedLat != null && savedLon != null) {
            lat = savedLat.toDouble()
            lon = savedLon.toDouble()
        }
    }

    override fun allObserver() {
        // observers
    }

    private fun loadData() {
        val queue = Volley.newRequestQueue(requireContext())

        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET,
            "https://arsarkar.xyz/apps/get_earthquake_data.php",
            null,
            { response: JSONArray ->

                dataList.clear()
                locations.clear()

                for (i in 0 until response.length()) {
                    val jsonObject = response.getJSONObject(i)

                    val id = jsonObject.optInt("id", 0)
                    val eventId = jsonObject.optString("event_id", "unknown")
                    val magnitude = jsonObject.optDouble("magnitude", 0.0).toFloat()
                    val place = jsonObject.optString("place", "Unknown")
                    val eventTime = jsonObject.optLong("event_time", 0L)
                    val latitude = jsonObject.optDouble("latitude", 0.0)
                    val longitude = jsonObject.optDouble("longitude", 0.0)
                    val depth = jsonObject.optString("depth", "0")
                    val title = jsonObject.optString("title", "No title")
                    val tsunami = jsonObject.optInt("tsunami", 0)
                    val magType = jsonObject.optString("magType", "unknown")
                    val sig = jsonObject.optInt("sig", 0)

                    dataList.add(
                        Earthquake_Data_Class(
                            id,
                            eventId,
                            magnitude,
                            place,
                            eventTime,
                            latitude,
                            longitude,
                            depth,
                            title,
                            tsunami,
                            magType,
                            sig
                        )
                    )

                    locations.add(LatLng(latitude, longitude))
                }

                binding.mapView.getMapAsync { map ->
                    map.setStyle("https://demotiles.maplibre.org/style.json") { style ->

                        // Red dot with outer circle
                        val iconFactory = IconFactory.getInstance(requireContext())

                        val size = 40
                        val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
                        val canvas = Canvas(bitmap)

                        val outerPaint = Paint()
                        outerPaint.color = Color.RED
                        outerPaint.alpha = 80  // transparency
                        outerPaint.style = Paint.Style.STROKE
                        outerPaint.strokeWidth = 6f
                        canvas.drawCircle(size/2f, size/2f, size/3f, outerPaint)

                        val innerPaint = Paint()
                        innerPaint.color = Color.RED
                        innerPaint.style = Paint.Style.FILL
                        canvas.drawCircle(size/2f, size/2f, size/6f, innerPaint)

                        val markerIcon: Icon = iconFactory.fromBitmap(bitmap)

                        for (loc in locations) {
                            val marker = MarkerOptions()
                                .position(loc)
                                .title("Magnitude location")
                                .icon(markerIcon)  // red dot attach
                            map.addMarker(marker)
                        }

                        if (locations.isNotEmpty()) {
                            map.cameraPosition = CameraPosition.Builder()
                                .target(locations[0])
                                .zoom(0.1)
                                .build()
                        }
                    }
                }

                binding.recycleView.layoutManager = LinearLayoutManager(requireContext())
                binding.recycleView.adapter = EarthquakeAdapter( dataList)


            },
            {
                Toast.makeText(requireContext(), "Error loading data", Toast.LENGTH_SHORT).show()
            }
        )

        queue.add(jsonArrayRequest)
    }

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

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
    }

    override fun onDestroy() {
        binding.mapView.onDestroy()
        super.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.mapView.onSaveInstanceState(outState)
    }
}