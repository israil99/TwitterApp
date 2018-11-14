package com.neobis.israil.twitterapp.viewmodel

import android.databinding.ObservableField
import android.graphics.drawable.Drawable



class TimelineItemViewModel {

    private val TAG = javaClass.simpleName

    var tweet = ObservableField<String>()
    var user  = ObservableField<String>()
    var imageUrl= ObservableField<Drawable>()
    var userScreenName = ObservableField<String>()
}