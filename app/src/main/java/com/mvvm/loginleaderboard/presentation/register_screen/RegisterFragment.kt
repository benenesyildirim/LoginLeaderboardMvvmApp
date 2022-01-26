package com.mvvm.loginleaderboard.presentation.register_screen

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.mvvm.loginleaderboard.R
import com.mvvm.loginleaderboard.common.Resource
import com.mvvm.loginleaderboard.databinding.FragmentRegisterBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment() {
    private lateinit var binding: FragmentRegisterBinding
    private val viewModel: RegisterViewModel by viewModels()
    private lateinit var nickName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {}
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(layoutInflater)

        setCreateUserClickListener()
        setNickNameChangeListener()

        observeRegistration()

        return binding.root
    }

    private fun setCreateUserClickListener() {
        binding.createUserBtn.setOnClickListener {
            when {
                3 > nickName.length -> Toast.makeText(context, getString(R.string.cant_shorter_then_three), Toast.LENGTH_SHORT).show()
                15 < nickName.length -> Toast.makeText(context, getString(R.string.cant_bigger_then_fifteen), Toast.LENGTH_SHORT).show()
                else -> viewModel.registerUser(nickName)
            }
        }
    }

    private fun setNickNameChangeListener() {
        binding.registerNicknameEt.addTextChangedListener {
            nickName = it.toString()
        }
    }

    private fun observeRegistration() {
        val loadingAnimation = binding.loadingAnimationRegister
        with(viewModel) {
            registerLiveData.observe(viewLifecycleOwner, { state ->
                when (state) {
                    is Resource.Loading -> { //TODO Güzel bi animasyon yapıştır.
                        loadingAnimation.visibility = VISIBLE
                    }
                    is Resource.Success -> {
                        loadingAnimation.visibility = GONE

                        Navigation.findNavController(requireActivity() as Activity, R.id.nav_host_fragment).popBackStack()
                    }
                    is Resource.Error -> {
                        Toast.makeText(context, state.message ?: "Fail", Toast.LENGTH_SHORT).show()
                        loadingAnimation.visibility = GONE
                    }
                }
            })
        }
    }
}