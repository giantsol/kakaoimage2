package com.ellen.kakaoimages.ui.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.ellen.kakaoimages.R
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.koin.android.scope.scope


class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
//        var job = runBlocking {
//            //scope(main){
//            delay(500L)
//            //}
//        }

        /**
         * runBlocking 왜 안될까? 찾아봐야할듯
         */
//        Handler().postDelayed(
//            Thread{
//                startActivity(Intent(this, MainActivity::class.java))
//                finish()
//            }, 900
//        )
//
//        val job2 = GlobalScope.launch {
//            delay(1000)
//            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
//            finish()
//        }

        //job.joinAndCancel() > join이랑 cancel 을 따로 쓰는게 맞아. - 자식들이 cancel되지 않음


        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}