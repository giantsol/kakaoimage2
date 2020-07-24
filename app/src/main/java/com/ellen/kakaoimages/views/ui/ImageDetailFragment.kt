package com.ellen.kakaoimages.views.ui

import com.ellen.kakaoimages.R
import com.ellen.kakaoimages.viewmodel.UsersViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.ellen.kakaoimages.databinding.FragmentDetailImageBinding
import kotlinx.android.synthetic.main.fragment_detail_image.*
import org.koin.android.viewmodel.ext.android.sharedViewModel

class ImageDetailFragment  :Fragment(){

    private val vm: UsersViewModel by sharedViewModel()
    private lateinit var mViewDataBinding: FragmentDetailImageBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mViewDataBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_detail_image, container, false
        )
        val mRootView = mViewDataBinding.root
        mViewDataBinding.lifecycleOwner = this
        return mRootView
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mViewDataBinding.viewModel = vm
        vm.likedList.observe(viewLifecycleOwner, Observer {
//            if (it.isNotEmpty() && it != null) {
//                userAdapter.clear()
//                userAdapter.setUsers(it)
//            }
        })

    }

}