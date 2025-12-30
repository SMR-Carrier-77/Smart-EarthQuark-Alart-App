package com.example.smartearthquarkalart.views.dashboard.home

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.BitmapDrawable
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.smartearthquarkalart.R
import com.example.smartearthquarkalart.base.BaseFragment
import com.example.smartearthquarkalart.data.models.Earthquake_Data_Class
import com.example.smartearthquarkalart.databinding.FragmentHomeBinding
import com.example.smartearthquarkalart.views.adapter.EarthquakeAdapter
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONArray
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Marker

@AndroidEntryPoint
class HomeFragment :
    BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    private val dataList = mutableListOf<Earthquake_Data_Class>()
    private val locations = mutableListOf<GeoPoint>()

    companion object {
        var lat = 23.8103
        var lon = 90.4125
    }

    override fun setListener() {

        binding.swipeRefresh.setOnRefreshListener {

            binding.animationView.visibility = View.VISIBLE
            binding.homeData.visibility = View.GONE

            loadData()
        }

        setupOSM()
        loadSavedLocation()
        loadData()
    }

    override fun allObserver() {}

    // -------------------- OSM setup --------------------
    private fun setupOSM() {
        Configuration.getInstance().load(
            requireContext(),
            requireContext().getSharedPreferences(
                "osm_pref",
                Context.MODE_PRIVATE
            )
        )
        Configuration.getInstance().userAgentValue =
            requireContext().packageName
    }

    // -------------------- Load saved lat/lon --------------------
    private fun loadSavedLocation() {
        val sharedPref =
            requireContext().getSharedPreferences("user_location", Context.MODE_PRIVATE)

        val savedLat = sharedPref.getString("latitude", null)
        val savedLon = sharedPref.getString("longitude", null)

        if (savedLat != null && savedLon != null) {
            lat = savedLat.toDouble()
            lon = savedLon.toDouble()
        }
    }

    // -------------------- API load --------------------
    private fun loadData() {

        val queue = Volley.newRequestQueue(requireContext())

        val request = JsonArrayRequest(
            Request.Method.GET,
            "https://arsarkar.xyz/apps/get_earthquake_data.php",
            null,
            { response: JSONArray ->

                binding.animationView.visibility = View.GONE
                binding.homeData.visibility = View.VISIBLE

                binding.swipeRefresh.isRefreshing = false

                dataList.clear()
                locations.clear()
                binding.mapView.overlays.clear()

                for (i in 0 until response.length()) {
                    val obj = response.getJSONObject(i)

                    val item = Earthquake_Data_Class(
                        obj.optInt("id"),
                        obj.optString("event_id"),
                        obj.optDouble("magnitude").toFloat(),
                        obj.optString("place"),
                        obj.optLong("event_time"),
                        obj.optDouble("latitude"),
                        obj.optDouble("longitude"),
                        obj.optString("depth"),
                        obj.optString("title"),
                        obj.optInt("tsunami"),
                        obj.optString("magType"),
                        obj.optInt("sig")
                    )

                    dataList.add(item)
                    locations.add(GeoPoint(item.latitude, item.longitude))
                }

                setupMap()
                setupRecycler()

            },
            {

                binding.swipeRefresh.isRefreshing = false

                Toast.makeText(
                    requireContext(),
                    "Error loading data",
                    Toast.LENGTH_SHORT
                ).show()
            }
        )

        queue.add(request)
    }

    private fun createBlueDotIcon(): BitmapDrawable {
        val size = 40
        val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        // outer circle
        val outerPaint = Paint().apply {
            color = Color.BLUE
            alpha = 70
            style = Paint.Style.STROKE
            strokeWidth = 5f
            isAntiAlias = true
        }
        canvas.drawCircle(size / 2f, size / 2f, size / 2.4f, outerPaint)

        // inner dot
        val innerPaint = Paint().apply {
            color = Color.BLUE
            style = Paint.Style.FILL
            isAntiAlias = true
        }
        canvas.drawCircle(size / 2f, size / 2f, size / 6f, innerPaint)

        return BitmapDrawable(resources, bitmap)
    }


    // -------------------- Create red dot icon --------------------
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

    // -------------------- Map render --------------------
    private fun setupMap() {
        val map = binding.mapView
        map.setMultiTouchControls(true)

        val controller = map.controller
        controller.setZoom(4.0)

        val userPoint = GeoPoint(lat, lon)
        controller.setCenter(userPoint)

        val redDotIcon = createRedDotIcon()
        val blueLocationIcon = createBlueDotIcon()

        // User location
        val userMarker = Marker(map)
        userMarker.position = userPoint
        userMarker.icon = blueLocationIcon
        userMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)
        userMarker.title = "Your Location"
        map.overlays.add(userMarker)

        // Earthquake locations
        for (point in locations) {
            val marker = Marker(map)
            marker.position = point
            marker.icon = redDotIcon
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)
            marker.title = "Earthquake Location"
            map.overlays.add(marker)
        }

        map.invalidate()
    }

    // -------------------- Recycler --------------------
    private fun setupRecycler() {

        dataList.sortByDescending { it.event_time }

        binding.recycleView.layoutManager = LinearLayoutManager(requireContext())
        binding.recycleView.adapter = EarthquakeAdapter(requireContext(), dataList)
    }

    // -------------------- Map lifecycle --------------------
    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }

    override fun onPause() {
        binding.mapView.onPause()
        super.onPause()
    }

    override fun onDestroy() {
        binding.mapView.onDetach()
        super.onDestroy()
    }

}