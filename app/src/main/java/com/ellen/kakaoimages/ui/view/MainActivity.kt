package com.ellen.kakaoimages.ui.view

import com.ellen.kakaoimages.R
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ellen.kakaoimages.databinding.ActivityMainBinding
import com.ellen.kakaoimages.util.*
import com.ellen.kakaoimages.ui.viewmodel.ImageViewModel
import com.ellen.kakaoimages.ui.adapter.ImageListAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel

/**
 * 앱 실행시 보이는 개선점들!
 * 내가 3년차라 내 입장에서 볼 때 나랑 비슷한 경력이면 이정도는 기대한다 정도라고 생각하면돼!
 *
 * 1. 이미지 로딩된 상태에서 화면회전하면 다시 로딩하잖아? 이렇게되면 ViewModel을 쓴 의미가 없어.
 * ViewModel은 화면회전으로 Activity가 recreate돼도 살아남는 친구인데 어짜피 화면회전 할때마다
 * 재로딩할거면 ViewModel의 의미가 많이 퇴색되지.
 * 2. 화면회전하면 이전에 적용된 필터도 항상 ALL로 바뀌어버리는데 이것도 의도된건 아니지?
 * 3. 검색창에 띄어쓰기만 넣으면 no query (required param...) 이라는 알 수 없는 에러메시지가 나와.
 * 이런 경우에도 대처를 해둬야할듯. 검색이 아예 안되게 하든지 등.
 * 4. 네트워크 끊겼을때 에러 메시지도 좀 더 친절하게 바꿔주면좋을듯. 과제용 샘플 앱이긴하지만 완성도 있으면 좋으니까!
 * 5. 검색창에 "밥버걱"(걱 오타아니야!) 이라고 검색하면 똑같은 이미지만 주루룩뜨는데 페이징에 문제있는듯?
 * 그 상태에서 스크롤 계속 내리다보면 알 수 없는 에러 문구가 뜸. 의심되는점은 페이징 단위가 50인데
 * 결과값이 50개 미만일때 뭔가 같은 페이지만 계속 불러오게 되는듯. 페이징 쓸 때 흔히 마주치는 케이스 중 하나!
 * 6. 필터 적용한 상태에서 검색어 바뀌면 필터가 ALL로 초기화되어버리는데 의도된 동작이 맞나?
 * 7. 검색결과 스크롤 내리다가 네트워크 끊기면 그냥 에러 문구로 바뀌어버리는데 이전 결과값들 유지하는식으로 해야할듯.
 * 페이징만 더 안되도록 하는식으로. 그러다가 네트웤 다시 연결되면 페이진 계속 하고. 요것도 페이징 쓸 때 흔한 케이스 중 하나!
 * 8. 검색창에 "aka" 친 후 필터 바꿀때마다 이미지가 번쩍번쩍하면서 다른 이미지가 잠깐 보였다가 다시 나오는데 이것도 수정해야할듯.
 * 리사이클러뷰랑 ImageView 쓸때 흔히 마주치는 케이스인데 어떻게 대처하면될지 알아두면 좋아!
 */

class MainActivity : AppCompatActivity() {
    private val vm: ImageViewModel by viewModel()
    // 사소한거지만 변수명 앞에 m을 붙일건지 말지 통일하면 좋을듯
    private lateinit var mViewDataBinding: ActivityMainBinding
    private lateinit var imageListAdapter: ImageListAdapter
    private lateinit var scrollListener: EndlessRecyclerViewScrollListener
    private var job: Job? = null

    private var beforeSelected = 0    //for Check Spinner Changed


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mViewDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mViewDataBinding.viewModel = vm
        mViewDataBinding.lifecycleOwner = this

        /**
         * SPINNER
         */
        setupFilter()
        /**
         * RecyclerView
         */
        setUpRecyclerView()
        /**
         * EditText
         */
        setupEditText()

        mViewDataBinding.viewModel = vm
        vm.imageList.observe(this, Observer {
            if (it.isNotEmpty() && it != null) {
                imageListAdapter.setImages(it)
            }
        })

    }

    private fun initPage() {
        vm.init()
        imageListAdapter.clear()
        scrollListener.resetState()
        beforeSelected = 0
        job = null
    }

    private fun setUpRecyclerView() {
        val linearLayoutManager = GridLayoutManager(this, 2)
        // EndlessRecyclerViewScrollListener 요런거 찾아 쓰는건 괜찮지.
        // 근데 내가 위에 적은 개선점중에 paging 문제가 아마도 여기랑 관련된거일 가능성이 높은데
        // 그런 문제가 있을때 어찌됐든 해결할 수 있으려면 어케 동작하는지 이해해둘 필요는 있어!
        scrollListener = object : EndlessRecyclerViewScrollListener(linearLayoutManager) {
            var isKeyboardDismissedByScroll = false

            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                // 화면 회전 했을때 여기가 무조건 한번 불리고, 그래서 무조건 처음부터 fetch하기 때문에
                // 이미지를 첨부터 다시 불러오는걸까? 확인해봐야할듯
                vm.fetchImages(page)
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

        imageListAdapter = ImageListAdapter()
        rv_images.apply {
            layoutManager = linearLayoutManager
            addOnScrollListener(scrollListener)
            rv_images.adapter = imageListAdapter
        }

    }

    private fun setupEditText() {

        ed_search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(editable: Editable?) {
                // MVVM architecture 가져가기로했으니까 여기 로직도 보통 vm안으로 들어가.
                // vm.onSearchTextChagned(..) 식으로.
                // 그래서 MVVM 구조일때는 job을 Activity단에서 쓰는건 좀 어색하고 vm에서 쓰지.
                val clearIcon = if (editable?.isNotEmpty() == true) R.drawable.ic_clear else 0
                ed_search.setCompoundDrawablesWithIntrinsicBounds(0, 0, clearIcon, 0)
                job?.cancel()
                // 만약 여기서 쓸거면 MainScope() 얘도 관리해줘야하는거 아냐?
                // 샘플 코드보니까 onDestroy()에서 cancel해줘야할거처럼 보이는데
                job = MainScope().launch {
                    delay(500L)
                    editable?.let {
                        if (editable.toString().isNotEmpty()) {
                            //init
                            initPage()
                            vm.fetchImages(1)
                        }
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) =
                Unit

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit
        })

        ed_search.setOnTouchListener(View.OnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                v.performClick()
                if (event.rawX >= ((v as EditText).right - v.compoundPaddingRight)) {
                    v.setText("")
                    initPage()
                    return@OnTouchListener true
                }
            }
            return@OnTouchListener false
        })
    }

    private fun setupFilter() {
        vm.filter.observe(this, Observer {
            // filter값이 바뀔때마다 adapter를 바꿔주는건 좋은 방식은 아니야.
            // adapter는 초기에 고정해두고 adapter에 접근해서 값을 바꿔주는게 더 자연스러워!
            // recyclerview 쓸때도 adapter는 초기에 세팅해두고 adapter에 접근해서 값 바꿔주잖아? 그런식으로 되어야해!
            spinner.adapter =
                ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, it.toTypedArray())
            // 아래 코드 때문에 화면 회전할때마다 필터값이 초기화되는걸로 보여.
            // beforeSelected가 Activity에 있으니까 회전 할때마다 항상 0으로 초기화되니까.
            // 유지되어야하는 값은 ViewModel로 들어가야해!
            spinner.setSelection(beforeSelected)
        })
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) = Unit

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                // beforeSelected가 ViewModel로 들어간다는 가정 하에 이런 로직도 ViewModel로 들어가는게 더 자연스럽지.
                // 예컨대 vm.onSpinnerItemSelected()함수가 있고 그 안에서 이런 로직을 수행하도록.
                if (beforeSelected != position) {
                    vm.setCurrFilter(spinner.selectedItem.toString())
                }

                beforeSelected = position
            }

        }
    }

    override fun onBackPressed() {
        if (closeBackPressed(this)) super.onBackPressed()
    }

}
