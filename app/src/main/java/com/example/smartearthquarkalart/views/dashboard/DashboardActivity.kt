package com.example.smartearthquarkalart.views.dashboard

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.smartearthquarkalart.R
import com.example.smartearthquarkalart.databinding.ActivityDashboardBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashboardActivity : AppCompatActivity() {

    private lateinit var binding : ActivityDashboardBinding
    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)


        navController = findNavController(R.id.fragmentDashboard)

        binding.sellerBottomNavigationView.setupWithNavController(navController)

        val appbarConfig = AppBarConfiguration(
            setOf(
                R.id.home_Dashboard,
                R.id.alart_Dashboard,
                R.id.setting_Dashboard
            )
        )

        setupActionBarWithNavController(navController , appbarConfig)

    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    /*
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_menu , menu)
        return super.onCreateOptionsMenu(menu)
    }

   override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.menu_report->{
                Toast.makeText(this , "Report" , Toast.LENGTH_SHORT).show()
            }
            R.id.menu_setting->{
                Toast.makeText(this , "Setting" , Toast.LENGTH_SHORT).show()
            }
            R.id.menu_logout->{

                auth.signOut()
                startActivity(Intent(this , MainActivity::class.java))
                finish()

            }
        }

        return super.onOptionsItemSelected(item)
    }



}
     */
}