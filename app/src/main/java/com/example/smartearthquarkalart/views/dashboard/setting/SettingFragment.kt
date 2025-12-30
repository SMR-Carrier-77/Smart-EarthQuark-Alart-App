package com.example.smartearthquarkalart.views.dashboard.setting

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.smartearthquarkalart.R
import com.example.smartearthquarkalart.base.BaseFragment
import com.example.smartearthquarkalart.data.models.LogIn
import com.example.smartearthquarkalart.databinding.FragmentSettingBinding
import com.example.smartearthquarkalart.views.dashboard.setting.other.AboutAppFragment
import com.example.smartearthquarkalart.views.dashboard.setting.other.DeleteAccountFragment
import com.example.smartearthquarkalart.views.dashboard.setting.other.DeveloperInfoFragment
import com.example.smartearthquarkalart.views.dashboard.setting.other.PrivacyPolicyFragment
import com.example.smartearthquarkalart.views.starter.MainActivity
import com.google.firebase.auth.FirebaseAuth

class SettingFragment : BaseFragment<FragmentSettingBinding>(FragmentSettingBinding::inflate) {

    private lateinit var auth: FirebaseAuth

    override fun setListener() {

        auth = FirebaseAuth.getInstance()

        var curretUser = auth.currentUser

        if (curretUser!=null){

            val email = curretUser.email
            val name = email?.substringBefore("@")
            binding.fullName.text = name
            binding.workerEmail.text = curretUser.email


        }else{
            //requireActivity().startActivity(Intent(requireContext() , LogIn::class.java))
            //requireActivity().finish()
        }

        binding.privacyPolicy.setOnClickListener {
            findNavController().navigate(R.id.privacyPolicyFragment)
        }

        binding.aboutApp.setOnClickListener {
            findNavController().navigate(R.id.aboutAppFragment)
        }

        binding.developerInfo.setOnClickListener {
            findNavController().navigate(R.id.developerInfoFragment)
        }

        binding.deleteAccount.setOnClickListener {
            findNavController().navigate(R.id.deleteAccountFragment)
        }

        binding.logOut.setOnClickListener {

            AlertDialog.Builder(requireContext())
                .setTitle("Warning")
                .setIcon(R.drawable.ic_warnings)
                .setMessage("Are you sure?")
                .setPositiveButton("Yes") { dialog, _ ->
                    dialog.dismiss()

                    // Firebase থেকে logout
                    auth.signOut()

                    // Login screen এ redirect with flags
                    val intent = Intent(requireContext(), MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    requireContext().startActivity(intent)
                    requireActivity().finish()

                }
                .setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()

        }

    }


    override fun allObserver() {
        // Your observer code
    }
}