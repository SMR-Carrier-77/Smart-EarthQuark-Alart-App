package com.example.smartearthquarkalart.views.dashboard

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.smartearthquarkalart.R
import com.example.smartearthquarkalart.databinding.ActivityDashboardBinding
import com.example.smartearthquarkalart.views.details.DetailsActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashboardActivity : AppCompatActivity() {

    private lateinit var binding : ActivityDashboardBinding
    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        askNotificationPermission()

        initFirebaseToken()

        navController = findNavController(R.id.fragmentDashboard)

        binding.sellerBottomNavigationView.setupWithNavController(navController)

        val appbarConfig = AppBarConfiguration(
            setOf(
                R.id.homeFragment,
                R.id.alartFragment,
                R.id.settingFragment
            )
        )

        setupActionBarWithNavController(navController , appbarConfig)

    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }


    fun initFirebaseToken(){

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("TAG", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result.toString()

            Log.d("FirebaseToken" , token)

            //binding.token.text = token

        })

    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (isGranted) {

        } else {

        }
    }

    private fun askNotificationPermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {

            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {

                AlertDialog.Builder(this)
                    .setTitle("Notification Permission")
                    .setMessage("You should give permission to get alert notificatio")
                    .setPositiveButton("Yes") {dialog, _->
                        requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                        dialog.dismiss()
                    }
                    .setNegativeButton("No") {dialog, _->
                        dialog.dismiss()
                    }
                    .create()
                    .show()

            } else {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
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