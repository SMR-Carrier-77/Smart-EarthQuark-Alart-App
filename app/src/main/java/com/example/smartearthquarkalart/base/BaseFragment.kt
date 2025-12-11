package com.example.smartearthquarkalart.base

import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

@Suppress("DEPRECATION")
abstract class BaseFragment<VB : ViewBinding>(private var bindingInflater: (inflater : LayoutInflater) -> VB) : Fragment() {

    private var _binding : VB? = null

    val binding : VB get() = _binding as VB

    lateinit var loading_progress_dialog : ProgressDialog


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = bindingInflater.invoke(inflater)

        loading_progress_dialog = ProgressDialog(requireContext())

        setListener()
        allObserver()

        return binding.root
    }

    abstract fun setListener()
    abstract fun allObserver()

}