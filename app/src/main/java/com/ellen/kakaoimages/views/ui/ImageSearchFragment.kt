package com.ellen.kakaoimages.views.ui

import com.ellen.kakaoimages.R
import com.ellen.kakaoimages.viewmodel.ImageViewModel
import com.ellen.kakaoimages.views.adapter.ImageListAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.ellen.kakaoimages.databinding.FragmentSearchImageBinding
import kotlinx.android.synthetic.main.fragment_search_image.*
import org.koin.android.viewmodel.ext.android.sharedViewModel

class ImageSearchFragment : UserBaseFragment()
     {

    private val vm: ImageViewModel by sharedViewModel()
    private var imageListAdapter: ImageListAdapter = ImageListAdapter()
    private lateinit var mViewDataBinding: FragmentSearchImageBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mViewDataBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_search_image, container, false
        )
        val mRootView = mViewDataBinding.root
        mViewDataBinding.lifecycleOwner = this
        return mRootView
    }

    override fun onSearch() {
        initPage()
        vm.fetchImages()
    }

    override fun onClear() {
        initPage()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        /**
         * RecyclerView
         */
        setUpRecyclerView()
        /**
         * EditText
         */
        setupEditText(ed_search)

        mViewDataBinding.viewModel = vm
//        vm.userList.observe(viewLifecycleOwner, Observer {
//            if (it.isNotEmpty() && it != null) {
//                imageListAdapter.setImages(it)
//            }
//        })

        vm.projects.observe(viewLifecycleOwner, Observer {
            it?.let {
                imageListAdapter.submitList(it)
            }
        })
        /**
         * Progress Loading
         */
//        projectListViewModel.networkState?.observe(this, Observer {
//
//            it?.let {
//                when(it) {
//                    is NetworkState.Loading -> showProgressBar(true)
//                    is NetworkState.Success -> showProgressBar(false)
//                    is NetworkState.Error -> {
//                        showProgressBar(false)
//                        if(it.errorCode == NO_DATA)
//                            uiHelper.showSnackBar(main_rootView, resources.getString(R.string.error_no_data))
//                        else if(it.errorCode == NO_MORE_DATA)
//                            uiHelper.showSnackBar(main_rootView, resources.getString(R.string.error_no_more_data))
//                        else uiHelper.showSnackBar(main_rootView, resources.getString(R.string.error_message))
//                    }
//                }
//            }
//        })

    }

    private fun initPage() {
        vm.init()
        imageListAdapter.clear()
    }

    private fun setUpRecyclerView() {
        val linearLayoutManager = StaggeredGridLayoutManager(2,RecyclerView.VERTICAL)


            rv_search_user.layoutManager = linearLayoutManager
            rv_search_user.adapter = imageListAdapter

        imageListAdapter.setOnItemClickListener {
            vm.select(it)
//            vm.showDetailFragment()
//            imageListAdapter.filter.filter(it.collection)
        }
    }


}
