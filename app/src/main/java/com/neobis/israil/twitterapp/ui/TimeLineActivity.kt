package com.neobis.israil.twitterapp.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import com.neobis.israil.twitterapp.R

class TimelineActivity : AppCompatActivity() {

        private val TAG: String = javaClass.simpleName

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_timeline)

            val toolbar = findViewById<Toolbar>(R.id.toolbar)

            setSupportActionBar(toolbar)

            if (savedInstanceState == null){
                supportFragmentManager.beginTransaction()
                        .replace(R.id.main_container, TimelineFragment.newInstance("Oi"))
                        .commit()
            }
        }

    }