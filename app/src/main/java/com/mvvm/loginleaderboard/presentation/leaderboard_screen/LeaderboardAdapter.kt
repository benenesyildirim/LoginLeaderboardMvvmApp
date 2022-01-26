package com.mvvm.loginleaderboard.presentation.leaderboard_screen

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mvvm.loginleaderboard.data.remote.dto.User
import com.mvvm.loginleaderboard.databinding.LeaderboardRowDesignBinding

class LeaderboardAdapter(
    private val users: MutableList<User>, val listener: (User) -> Unit
) : RecyclerView.Adapter<LeaderboardAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = LeaderboardRowDesignBinding.inflate(inflater)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(users[position])

    override fun getItemCount(): Int = users.size

    inner class ViewHolder(private val binding: LeaderboardRowDesignBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            binding.apply {
                this.user = user
                executePendingBindings()
                root.setOnClickListener { listener(user) }
            }
        }
    }
}