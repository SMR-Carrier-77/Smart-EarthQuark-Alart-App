package com.example.smartearthquarkalart.views.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.smartearthquarkalart.data.models.Earthquake_Data_Class
import com.example.smartearthquarkalart.databinding.ItemBinding
import com.example.smartearthquarkalart.views.details.DetailsActivity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class EarthquakeAdapter(
    var context: Context,
    private var dataList: MutableList<Earthquake_Data_Class>
) : RecyclerView.Adapter<EarthquakeAdapter.EarthquakeViewHolder>() {

    class EarthquakeViewHolder(var binding: ItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EarthquakeViewHolder {
        var binding = ItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return EarthquakeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EarthquakeViewHolder, position: Int) {

       holder.binding.apply {

           dataList[position].let{

               tvPlace.text = it.place
               tvDepth.text = "Depth: "+it.depth
               tvMagnitude.text = "%.2f".format(it.magnitude)
               tvLatitude.text = "Lat: %.2f".format(it.latitude)
               tvLongitude.text = "Lon: %.2f".format(it.longitude)

               val eventTimeMillis = it.event_time // যদি timestamp milliseconds এ হয়
               val currentTimeMillis = System.currentTimeMillis()

               val diffMillis = currentTimeMillis - eventTimeMillis

               val seconds = diffMillis / 1000
               val minutes = seconds / 60
               val hours = minutes / 60
               val days = hours / 24

               val timeAgo = when {
                   days > 0 -> "$days days ago"
                   hours > 0 -> "$hours hours ago"
                   minutes > 0 -> "$minutes minutes ago"
                   else -> "$seconds seconds ago"
               }

               tvTimeAgo.text = timeAgo  // Output: e.g. "2 hours ago"

               val eventTimeStr = if (it.event_time != 0L) {
                   val sdf = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault())
                   sdf.timeZone = TimeZone.getDefault()  // local timezone
                   sdf.format(Date(it.event_time))
               } else {
                   "Unknown"
               }

               tvDate.text =eventTimeStr.toString()

               var level = it.magType.lowercase()

               tvMagnitudeType.text = when(level) {
                   "md" -> "Type: Normal"
                   "ml" -> "Type: Micro"
                   "mb" -> "Type: Medium"
                   "ms" -> "Type: Big"
                   "mw" -> "Type: Dangerous"
                   else -> "Type: Unknown"
               }


               if (it.tsunami==0){
                   tvTsunamiStatus.text = "Tsunami: No"
               }else{
                   tvTsunamiStatus.text = "Tsunami: Yes"
               }

               root.setOnClickListener {

                   val item = dataList[position]
                   val intent = Intent(context, DetailsActivity::class.java)

                   intent.putExtra("place",  item.place)
                   intent.putExtra("depth", item.depth)
                   intent.putExtra("magnitude", item.magnitude)
                   intent.putExtra("latitude", item.latitude)
                   intent.putExtra("longitude", item.longitude)
                   intent.putExtra("time", item.event_time)
                   intent.putExtra("magType", item.magType)
                   intent.putExtra("tsunami", item.tsunami)
                   intent.putExtra("title", item.title)
                   intent.putExtra("sig", item.sig)

                   context.startActivity(intent)
               }



           }

       }
    }

    override fun getItemCount(): Int = dataList.size
}