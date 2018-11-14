package com.neobis.israil.twitterapp.viewmodel

import android.databinding.ObservableInt
import android.util.Log
import android.view.View
import com.twitter.sdk.android.core.Callback
import com.twitter.sdk.android.core.Result
import com.twitter.sdk.android.core.TwitterCore
import com.twitter.sdk.android.core.TwitterException
import com.twitter.sdk.android.core.models.Search
import com.twitter.sdk.android.core.models.Tweet
import io.reactivex.Observable

class TimelineFragmentViewModel(val timelineRefreshListener: TimelineRefreshListener) {

    private val TAG = javaClass.simpleName
    var isLoading: ObservableInt = ObservableInt(View.VISIBLE)
    var username = "Loading."

    fun getTweets() {
       timelineRefreshListener.showProgress()
        TwitterCore.getInstance().apiClient.statusesService.homeTimeline(20, null, null, false, true, false, false)
                .enqueue(object : Callback<List<Tweet>>() {
                    override fun failure(exception: TwitterException?) {
                        Log.d(TAG, "FAILURE")
                        Log.d(TAG, exception!!.message.toString())
                        timelineRefreshListener.hideProgress()
                    }
                    override fun success(result: Result<List<Tweet>>?) {
                        timelineRefreshListener.onTimelineRefresh(result!!.data)
                        isLoading.set(View.INVISIBLE)
                        timelineRefreshListener.hideProgress()
                    }
                })
    }



    fun sendTweet(tweetText: String): Observable<Result<Tweet>> {
        return Observable.create { subscriber ->
            val callback = object : Callback<Tweet>() {
                override fun success(result: Result<Tweet>) {
                    Log.i(TAG, "Tweet tweeted")
                    subscriber.onNext(result)
                }

                override fun failure(e: TwitterException) {
                    Log.e(TAG, e.message, e)
                    subscriber.onError(e)
                }
            }

            TwitterCore.getInstance().apiClient.statusesService.update(tweetText, null, null, null, null, null, null, null, null).enqueue(callback)
        }
    }

    fun searchTweets(query:String) {
        TwitterCore.getInstance().apiClient.searchService.tweets(query, null, null, null, null,null,null,null,null,null)
                .enqueue(object : Callback<Search>() {
                    override fun failure(exception: TwitterException?) {
                        Log.d(TAG, "FAILURE")
                        Log.d(TAG, exception!!.message.toString())
                    }
                    override fun success(result: Result<Search>) {
                        Log.d("Success",result.toString())
                        timelineRefreshListener.onRefreshAfterSearch(result)
                        isLoading.set(View.INVISIBLE)
                    }
                })
    }


    interface TimelineRefreshListener {
        fun onTimelineRefresh(tweets: List<Tweet>)
        fun onRefreshAfterSearch(search: Result<Search>)
        fun showProgress()
        fun hideProgress()
    }
}