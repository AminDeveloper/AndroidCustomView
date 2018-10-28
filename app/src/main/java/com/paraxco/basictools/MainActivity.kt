package com.paraxco.basictools

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.main_activity.*
import org.jetbrains.anko.doAsync


/**
 * Created by Amin on 18/11/2017.
 */

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        SmartLogger.initLogger(applicationContext)
        setContentView(R.layout.main_activity)


        progressButton.setOnClickListener {
            doAsync {
                Thread.sleep(2000)
//                uiThread {
                progressButton.showButton()
//                }
            }
        }
    }

}