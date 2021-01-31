package com.ssho.orishchukfintechlab

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ssho.orishchukfintechlab.ui.GifsBrowserFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val isFragmentContainerEmpty = savedInstanceState == null
        if (isFragmentContainerEmpty) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, GifsBrowserFragment.newInstance())
                .commit()
        }
    }
}