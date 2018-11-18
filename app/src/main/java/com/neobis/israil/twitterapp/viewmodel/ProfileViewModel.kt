package com.neobis.israil.twitterapp.viewmodel

import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.databinding.ObservableField
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import com.neobis.israil.twitterapp.R
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target

class ProfileViewModel(context: Context, imageUrl : String) {

    var IMAGE_URL: String? = null
    var profileImage: ObservableField<Drawable>? = null
    private var bindableFieldTarget: BindableFieldTarget? = null

    init {
        this.IMAGE_URL = imageUrl
        profileImage = ObservableField<Drawable>()
        bindableFieldTarget = BindableFieldTarget(profileImage!!, context.getResources())
        Picasso.with(context)
                .load(IMAGE_URL)
                .placeholder(R.drawable.user)
                .into(bindableFieldTarget);
    }

    class BindableFieldTarget(private val observableField: ObservableField<Drawable>, private val resources: Resources) : Target {
        override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
            observableField.set(placeHolderDrawable)
        }

        override fun onBitmapFailed(errorDrawable: Drawable?) {
            observableField.set(errorDrawable)        }

        override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
            observableField.set(BitmapDrawable(resources, bitmap))
        }


    }
}