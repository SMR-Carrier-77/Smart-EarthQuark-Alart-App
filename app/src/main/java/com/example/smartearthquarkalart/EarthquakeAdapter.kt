package com.example.smartearthquarkalart

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.smartearthquarkalart.data.models.Earthquake_Data_Class
import com.example.smartearthquarkalart.databinding.ItemBinding
import com.google.api.Context
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class EarthquakeAdapter(
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

               titleText.text = it.place
               subtitleText.text = "Depth : "+it.depth
               outlinedButton.text = ""+it.magnitude

               val eventTimeStr = if (it.event_time != 0L) {
                   val sdf = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault())
                   sdf.timeZone = TimeZone.getDefault()  // local timezone
                   sdf.format(Date(it.event_time))
               } else {
                   "Unknown"
               }

               timeAgoText.text =eventTimeStr.toString()

               var level = it.magType

               if (level=="md"){
                   damageText.text = "Type : No matter"
               }else if (level=="ml"){
                   damageText.text = "Type : Normal"
               }else if (level=="mb"){
                   damageText.text = "Type : Medium"
               }else if (level=="ms"){
                   damageText.text = "Type : Big"
               }else if (level=="mw"){
                   damageText.text = "Type : Dangerous"
               }


           }

       }
    }

    override fun getItemCount(): Int = dataList.size
}
