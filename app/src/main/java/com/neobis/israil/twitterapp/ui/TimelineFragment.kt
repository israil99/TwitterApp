package com.neobis.israil.twitterapp.ui

import android.app.AlertDialog
import android.content.DialogInterface
import android.databinding.DataBindingUtil

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.text.InputFilter
import android.text.InputType
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.EditText

import android.widget.Toast
import com.neobis.israil.twitterapp.R
import com.neobis.israil.twitterapp.databinding.FragmentTimelineBinding
import com.neobis.israil.twitterapp.viewmodel.TimelineFragmentViewModel
import com.twitter.sdk.android.core.Result
import com.twitter.sdk.android.core.models.Search
import com.twitter.sdk.android.core.models.Tweet
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_timeline.*


class TimelineFragment : Fragment(), TimelineFragmentViewModel.TimelineRefreshListener {



    private var text: String? = null

    private var binding: FragmentTimelineBinding? = null
    private var timelineFragmentVM: TimelineFragmentViewModel? = null

    private var timelineAdapter = TimelineAdapter()

    private var mutableList = mutableListOf<Tweet>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            text = arguments!!.getString(ARG_PARAM1)
        }
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout and attach the ViewModel for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_timeline, container, false)
        val view: View = binding!!.root
        timelineFragmentVM = TimelineFragmentViewModel(this)
        binding!!.vm = timelineFragmentVM

        binding!!.timelineRecview.apply {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(activity)
            // specify an adapter (see also next example)
            timelineFragmentVM!!.getTweets()
            adapter = timelineAdapter
        }
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        swipeRefresh.setOnRefreshListener {
            timelineFragmentVM!!.getTweets()

        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater!!.inflate(R.menu.menu_timeline, menu)

      }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item!!.itemId
        return if (id == R.id.action_tweet) {
            // show popup to add tweet
            showNewTweetDialog()
            true
        }
         else super.onOptionsItemSelected(item)
    }



    override fun onTimelineRefresh(tweets: List<Tweet>) {
        mutableList = tweets.toMutableList()
        timelineAdapter.updateItems(tweets)
    }
    override fun onRefreshAfterSearch(search: Result<Search>) {
        mutableList = search.data.tweets.toMutableList()
        val tweets:List<Tweet> = search.data.tweets
        timelineAdapter.updateItems(tweets)
    }

    companion object {
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private val ARG_PARAM1 = "param1"

        fun newInstance(param1: String): TimelineFragment {
            val fragment = TimelineFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            fragment.arguments = args
            return fragment
        }
    }



    private fun showNewTweetDialog() {
        val tweetText = EditText(activity)
        tweetText.id = R.id.text
        tweetText.setSingleLine()
        tweetText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_SENTENCES
        tweetText.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(140))
        tweetText.imeOptions = EditorInfo.IME_ACTION_DONE

        val builder = AlertDialog.Builder(activity)
        builder.setMessage(R.string.label_what_is_happening)
        builder.setPositiveButton(R.string.action_tweet) { dialog, which ->
            timelineFragmentVM!!.sendTweet(tweetText.text.toString()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            { x ->
                                if (timelineFragmentVM != null) {
                                    showMessage(getString(R.string.alert_tweet_successful))
                                    mutableList.add(0,x.data)
                                    onTimelineRefresh(mutableList)
                                }
                            },
                            { e ->
                                if (timelineFragmentVM != null) {
                                    showMessage(getString(R.string.alert_tweet_failed))
                                }
                            })
        }

        val alert = builder.create()
        alert.setView(tweetText, 64, 0, 64, 0)
        alert.show()

        tweetText.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                alert.getButton(DialogInterface.BUTTON_POSITIVE).callOnClick()
                true
            }
            false
        }
    }

    fun showMessage(message: String){
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
    }



    override fun showProgress() {
        swipeRefresh?.isRefreshing = true    }

    override fun hideProgress() {
        swipeRefresh?.isRefreshing = false
    }
}
