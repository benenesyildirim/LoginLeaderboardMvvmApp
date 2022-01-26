package com.mvvm.loginleaderboard.presentation.leaderboard_screen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.mvvm.loginleaderboard.R
import com.mvvm.loginleaderboard.common.Resource
import com.mvvm.loginleaderboard.data.remote.dto.User
import com.mvvm.loginleaderboard.databinding.FragmentLeaderboardBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint class LeaderboardFragment : Fragment() {
    private lateinit var binding: FragmentLeaderboardBinding
    private val viewModel: LeaderboardViewModel by viewModels()
    private var isLeaderboardListing = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {}
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentLeaderboardBinding.inflate(layoutInflater)

        viewModel.getLeaderboard()

        setClickListeners()

        observeList()

        return binding.root
    }

    private fun setClickListeners() {
        binding.changeListBtn.setOnClickListener {
            isLeaderboardListing = !isLeaderboardListing
            if (isLeaderboardListing) viewModel.getLeaderboard()
            else viewModel.getAll()
        }

        binding.changeUserBtn.setOnClickListener {
            viewModel.setAsAdmin()
            Toast.makeText(context, "Admin is here", Toast.LENGTH_SHORT).show()
        }
    }

    private fun observeList() {
        with(viewModel) {
            leaderboardLiveData.observe(viewLifecycleOwner, { state ->
                when (state) {
                    is Resource.Loading -> {
                        Toast.makeText(context, "Loading...", Toast.LENGTH_SHORT).show()
                    }
                    is Resource.Success -> {
                        binding.leaderboardRv.adapter = LeaderboardAdapter(state.data!!) {
                            showUserDetailDialog(it)
                        }
                    }
                    is Resource.Error -> {
                        Toast.makeText(context, state.message ?: "Something went wrong...", Toast.LENGTH_SHORT).show()
                    }
                }
            })
        }
    }

    private fun showUserDetailDialog(user: User) {
        val dialog = BottomSheetDialog(requireContext())
        val view = layoutInflater.inflate(R.layout.user_detail_bottom_sheet_design, null)

        val userNameTv = view.findViewById<TextView>(R.id.bottom_sheet_username_tv)
        val pointTv = view.findViewById<TextView>(R.id.bottom_sheet_point_tv)

        val uniqueIdTv = view.findViewById<TextView>(R.id.bottom_sheet_unique_id_tv)
        val uniqueIdHeaderTv = view.findViewById<TextView>(R.id.unique_id_header_tv)
        val tokenTv = view.findViewById<TextView>(R.id.bottom_sheet_token_tv)
        val tokenHeaderTv = view.findViewById<TextView>(R.id.token_header_tv)

        userNameTv.text = user.nickName
        pointTv.text = user.point.toString()
        if (!user.uniqueId.isNullOrEmpty()) {
            uniqueIdHeaderTv.visibility = VISIBLE
            uniqueIdTv.visibility = VISIBLE
            uniqueIdTv.text = user.uniqueId
        }
        if (!user.token.isNullOrEmpty()) {
            tokenHeaderTv.visibility = VISIBLE
            tokenTv.visibility = VISIBLE
            tokenTv.text = user.token
        }

        dialog.setCancelable(true)
        dialog.setContentView(view)
        dialog.show()
    }
}