package com.example.smartearthquarkalart.views.dashboard.alart

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.smartearthquarkalart.R
import com.example.smartearthquarkalart.base.BaseFragment
import com.example.smartearthquarkalart.data.models.Earthquake_Data_Class
import com.example.smartearthquarkalart.databinding.FragmentAlartBinding
import com.example.smartearthquarkalart.views.adapter.EarthquakeAdapter
import org.json.JSONArray
import org.osmdroid.util.GeoPoint

class AlartFragment : BaseFragment<FragmentAlartBinding>(FragmentAlartBinding::inflate) {

    private val dataList = mutableListOf<Earthquake_Data_Class>()

    override fun setListener() {

        loadData()

    }

    override fun allObserver() {

    }

    private fun loadData() {
        val queue = Volley.newRequestQueue(requireContext())

        val request = JsonArrayRequest(
            Request.Method.GET,
            "https://arsarkar.xyz/apps/get_earthquake_query_data.php",
            null,
            { response: JSONArray ->

                binding.animationView.visibility = View.GONE
                binding.alartData.visibility = View.VISIBLE

                dataList.clear()

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
                    //locations.add(GeoPoint(item.latitude, item.longitude))
                }

                setupRecycler()

            },
            {
                Toast.makeText(
                    requireContext(),
                    "Error loading data",
                    Toast.LENGTH_SHORT
                ).show()
            }
        )

        queue.add(request)
    }

    // -------------------- Recycler --------------------
    private fun setupRecycler() {
        dataList.sortByDescending { it.event_time }
        binding.recycleView.layoutManager =
            LinearLayoutManager(requireContext())
        binding.recycleView.adapter =
            EarthquakeAdapter(requireContext(), dataList)
    }



}