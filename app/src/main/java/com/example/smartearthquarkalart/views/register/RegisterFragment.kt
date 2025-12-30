package com.example.smartearthquarkalart.views.register


import android.content.Intent
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.smartearthquarkalart.R
import com.example.smartearthquarkalart.base.BaseFragment
import com.example.smartearthquarkalart.core.DataState
import com.example.smartearthquarkalart.data.models.Register
import com.example.smartearthquarkalart.databinding.FragmentRegisterBinding
import com.example.smartearthquarkalart.isEmpty
import com.example.smartearthquarkalart.views.dashboard.DashboardActivity
import com.example.smartearthquarkalart.views.starter.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : BaseFragment<FragmentRegisterBinding>(FragmentRegisterBinding::inflate) {

    private val viewModel : RegisterViewModel by viewModels ()

    override fun setListener() {

        with(binding){

            buttonRegisterLogIn.setOnClickListener {

                findNavController().navigate(R.id.logInFragment)

            }

            buttonRegisterRegistation.setOnClickListener {

                nameRegister.isEmpty()
                emailRegister.isEmpty()
                passwordRegister.isEmpty()

                if(!nameRegister.isEmpty() && !emailRegister.isEmpty() && !passwordRegister.isEmpty()){

                    var user = Register(
                        userId = "",
                        name = nameRegister.text.toString().trim(),
                        email = emailRegister.text.toString().trim(),
                        password = passwordRegister.text.toString().trim()
                    )

                    viewModel.userRegistation(user)


                }

            }

        }
    }

    override fun allObserver() {

        viewModel.registation_responce.observe(viewLifecycleOwner){

            when(it){
                is DataState.Error -> {
                    loading_progress_dialog.dismiss()
                    Toast.makeText(context , it.message.toString(), Toast.LENGTH_SHORT).show()
                }
                is DataState.Loading -> {
                    loading_progress_dialog.show()
                }
                is DataState.Success -> {
                    loading_progress_dialog.dismiss()
                    Toast.makeText(requireContext() , "Registation Success!!", Toast.LENGTH_SHORT).show()
                    requireActivity().startActivity(Intent(requireContext() , DashboardActivity::class.java))
                    requireActivity().finish()

                }
            }

        }

    }

}