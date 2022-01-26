package com.mvvm.loginleaderboard.presentation.home_screen

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.github.ybq.android.spinkit.SpinKitView
import com.mvvm.loginleaderboard.R
import com.mvvm.loginleaderboard.common.Constants.NICKNAME
import com.mvvm.loginleaderboard.common.Constants.POINT
import com.mvvm.loginleaderboard.common.Resource
import com.mvvm.loginleaderboard.data.remote.dto.User
import com.mvvm.loginleaderboard.databinding.FragmentHomeScreenBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeScreenFragment : Fragment() {
    private lateinit var binding: FragmentHomeScreenBinding
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var loadingAnimation: SpinKitView

    private var user: User? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeScreenBinding.inflate(layoutInflater)
        loadingAnimation = binding.loadingAnimationHome

        if (viewModel.getUserFirestore() != null) {
            viewModel.getUserFirestore()!!.addOnSuccessListener {
                user = User(nickName = it.data?.get(NICKNAME).toString(), point = Integer.parseInt(it.data?.get(POINT).toString()))

                if (user == null) {
                    userCantFindNavigation()
                } else {
                    binding.user = user
                    loadingAnimation.visibility = GONE
                }
            }
        } else {
            userCantFindNavigation()
        }

        setClickListeners()

        updateUser()

        return binding.root
    }

    private fun userCantFindNavigation() {
        Toast.makeText(context, getString(R.string.not_logged_user_forwarding), Toast.LENGTH_SHORT).show()
        Navigation.findNavController(requireActivity() as Activity, R.id.nav_host_fragment).navigate(R.id.action_homeScreenFragment_to_registerFragment)
        loadingAnimation.visibility = GONE
    }

    private fun setClickListeners() {
        binding.increaseBtn.setOnClickListener {
            increasePoint()
        }

        binding.decreaseBtn.setOnClickListener {
            decreasePoint()
        }

        binding.goToLeaderboardBtn.setOnClickListener {
            Navigation.findNavController(requireActivity() as Activity, R.id.nav_host_fragment)
                .navigate(R.id.action_homeScreenFragment_to_leaderboardFragment)
        }

        binding.goToRegisterBtn.setOnClickListener {
            Navigation.findNavController(requireActivity() as Activity, R.id.nav_host_fragment)
                .navigate(R.id.action_homeScreenFragment_to_registerFragment)
        }
    }

    private fun decreasePoint() {
        var point = Integer.valueOf(binding.pointTv.text.toString())
        point -= 1
        viewModel.updatePoint(point)
        binding.pointTv.text = point.toString()
    }

    private fun increasePoint() {
        var point = Integer.valueOf(binding.pointTv.text.toString())
        point += 1
        viewModel.updatePoint(point)
        binding.pointTv.text = point.toString()
    }

    private fun updateUser() {
        with(viewModel) {
            updateUserLiveData.observe(viewLifecycleOwner, { state ->
                when (state) {
                    is Resource.Loading -> {
                        loadingAnimation.visibility = VISIBLE
                    }
                    is Resource.Success -> {
                        loadingAnimation.visibility = GONE
                    }
                    is Resource.Error -> {
                        Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
                        loadingAnimation.visibility = GONE
                    }
                }
            })
        }
    }
}