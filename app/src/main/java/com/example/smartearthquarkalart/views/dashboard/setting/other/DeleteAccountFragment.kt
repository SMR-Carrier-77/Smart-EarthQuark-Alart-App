package com.example.smartearthquarkalart.views.dashboard.setting.other

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.smartearthquarkalart.R
import com.example.smartearthquarkalart.base.BaseFragment
import com.example.smartearthquarkalart.databinding.FragmentDeleteAccountBinding
import com.example.smartearthquarkalart.views.starter.MainActivity
import com.google.firebase.auth.FirebaseAuth

class DeleteAccountFragment : BaseFragment<FragmentDeleteAccountBinding>(FragmentDeleteAccountBinding::inflate) {

    private lateinit var auth: FirebaseAuth

    override fun setListener() {

        auth = FirebaseAuth.getInstance()

        binding.btnDeleteAccount.setOnClickListener {

            auth.signOut()

            // Login screen এ redirect with flags
            val intent = Intent(requireContext(), MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            requireContext().startActivity(intent)
            requireActivity().finish()

        }

    }

    override fun allObserver() {
    }
}
