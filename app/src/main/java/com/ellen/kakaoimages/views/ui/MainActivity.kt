package com.ellen.kakaoimages.views.ui

import com.ellen.kakaoimages.R
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.ellen.kakaoimages.databinding.ActivityMainBinding
import com.ellen.kakaoimages.util.addFragment
import com.ellen.kakaoimages.util.hideFragment
import com.ellen.kakaoimages.util.showFragment
import com.ellen.kakaoimages.viewmodel.ImageViewModel
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private val vm: ImageViewModel by viewModel()
    private lateinit var mViewDataBinding: ActivityMainBinding

    private val searchFragment by inject<ImageSearchFragment>()
    private val detailFragment by inject<ImageDetailFragment>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mViewDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mViewDataBinding.viewModel = vm
        mViewDataBinding.lifecycleOwner = this

        init()

//        tv_search.setOnClickListener {
//            hideFragment(detailFragment)
//            showFragment(bookFragment)
//            tv_search.isSelected = true
//            tv_liked.isSelected = false
//        }
//        tv_liked.setOnClickListener {
//            hideFragment(bookFragment)
//            showFragment(detailFragment)
////            vm.getLikedUsers()
//            tv_search.isSelected = false
//            tv_liked.isSelected = true
//        }
    }


    private fun init() {
        addFragment(searchFragment, R.id.fragment_container)
        addFragment(detailFragment, R.id.fragment_container)
        hideFragment(detailFragment)
        showFragment(searchFragment)
//        tv_search.isSelected = true
//        tv_liked.isSelected = false
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }

}
