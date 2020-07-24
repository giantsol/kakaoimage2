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
import com.ellen.kakaoimages.util.EndlessRecyclerViewScrollListener
import com.ellen.kakaoimages.util.hideKeyboard
import kotlinx.android.synthetic.main.fragment_search_image.*
import org.koin.android.viewmodel.ext.android.sharedViewModel

class ImageSearchFragment : UserBaseFragment()
     {

    private val vm: ImageViewModel by sharedViewModel()
    private lateinit var imageListAdapter: ImageListAdapter
    private lateinit var mViewDataBinding: FragmentSearchImageBinding
    private lateinit var scrollListener: EndlessRecyclerViewScrollListener


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
        vm.userList.observe(viewLifecycleOwner, Observer {
            if (it.isNotEmpty() && it != null) {
                imageListAdapter.setUsers(it)
            }
        })

    }

    private fun initPage() {
        vm.init()
        imageListAdapter.clear()
        scrollListener.resetState()
    }

    private fun setUpRecyclerView() {
        val linearLayoutManager = StaggeredGridLayoutManager(2,RecyclerView.VERTICAL)
        scrollListener = object : EndlessRecyclerViewScrollListener(linearLayoutManager) {
            var isKeyboardDismissedByScroll = false

            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                vm.fetchImages()
            }

            //Hide Keyboard when scroll Dragging
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    if (!isKeyboardDismissedByScroll) {
                        hideKeyboard()
                        isKeyboardDismissedByScroll = !isKeyboardDismissedByScroll
                    }
                } else if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    isKeyboardDismissedByScroll = false
                }
            }
        }

        imageListAdapter = ImageListAdapter(context)
        rv_search_user.apply {
            layoutManager = linearLayoutManager
            addOnScrollListener(scrollListener)
            rv_search_user.adapter = imageListAdapter
        }

        imageListAdapter.setOnItemClickListener {
//            vm.select(it)
//            vm.showDetailFragment()
        }
    }


}
