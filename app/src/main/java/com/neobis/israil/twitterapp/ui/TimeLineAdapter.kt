package com.neobis.israil.twitterapp.ui

import android.content.Context
import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.neobis.israil.twitterapp.MainApplication
import com.neobis.israil.twitterapp.R
import com.neobis.israil.twitterapp.viewmodel.TimelineItemViewModel
import com.neobis.israil.twitterapp.databinding.TimelineItemBinding
import com.neobis.israil.twitterapp.viewmodel.ProfileViewModel
import com.twitter.sdk.android.core.models.Tweet



class TimelineAdapter(var items: List<Tweet>) : RecyclerView.Adapter<TimelineAdapter.TimelineViewHolder>() {

    val TAG = javaClass.simpleName

    constructor() : this(emptyList())

    // Provide a reference to the views for each data item
    class TimelineViewHolder(var view: TimelineItemBinding) : RecyclerView.ViewHolder(view.root)

    override fun onBindViewHolder(holder: TimelineViewHolder, position: Int) {
        holder!!.view.vm!!.tweet.set(items[position].text)
        holder.view.vm!!.user.set(items[position].user.screenName)
        val profileViewModel = ProfileViewModel(MainApplication.applicationContext(), items[position].user.profileImageUrl)
        holder.view.vm!!.imageUrl.set(profileViewModel.profileImage!!.get())
        holder.view.vm!!.userScreenName.set(String.format("@%s", items[position].user.screenName))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimelineAdapter.TimelineViewHolder {
        val inflater = parent!!.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val binding: TimelineItemBinding = DataBindingUtil.inflate(inflater, R.layout.timeline_item, parent, false)
        binding.vm = TimelineItemViewModel()

        return TimelineViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun updateItems(tweets: List<Tweet>){
        items = emptyList()
        items = tweets
        notifyDataSetChanged()
    }

}