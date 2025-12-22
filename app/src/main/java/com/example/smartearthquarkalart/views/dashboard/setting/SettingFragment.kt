package com.example.smartearthquarkalart.views.dashboard.setting

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Firebase Auth initialize করুন
        auth = FirebaseAuth.getInstance()
    }

    override fun setListener() {

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
            // Firebase থেকে logout
            auth.signOut()

            // Login screen এ redirect with flags
            val intent = Intent(requireContext(), MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            requireContext().startActivity(intent)
            requireActivity().finish()
        }

    }


    override fun allObserver() {
        // Your observer code
    }
}