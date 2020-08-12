package com.ellen.kakaoimages.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ellen.kakaoimages.R
import com.ellen.kakaoimages.data.model.ImagesDocuments
import com.ellen.kakaoimages.databinding.ItemSearchImageBinding

class ImageListAdapter: RecyclerView.Adapter<ImageListAdapter.ImageViewHolder>(), Filterable {

    var unFilteredList: ArrayList<ImagesDocuments> = ArrayList()
    var filteredList: ArrayList<ImagesDocuments> = unFilteredList

    var addedList: ArrayList<ImagesDocuments> = ArrayList()
    private var filteredposition = 0
    private var currFilter = "ALL"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val viewBinding: ItemSearchImageBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_search_image, parent, false
        )
        return ImageViewHolder(viewBinding)
    }


    override fun getItemCount(): Int {
        return filteredList.size
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.onBind(position)

    }

    fun setImages(items: List<ImagesDocuments>) {
        val position = filteredList.size
        this.unFilteredList.addAll(items)

        if (currFilter != "ALL") {
            filteredposition = position;
            addedList = items as ArrayList<ImagesDocuments>
            filter.filter(currFilter)
        } else {
            filteredList = unFilteredList
            notifyItemRangeInserted(position, items.size)
        }
    }

    fun clear() {
        this.filteredList.clear()
        this.unFilteredList.clear()
        notifyDataSetChanged()
    }

    fun setCurrFilter(currFilter: String) {
        this.currFilter = currFilter
        filter.filter(currFilter)
    }

    inner class ImageViewHolder(private val viewBinding: ItemSearchImageBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {

        fun onBind(position: Int) {
            // 필터 바꿀때마다 이전 이미지가 잠깐 나오면서 번쩍하는건 Glide와 관련있을거야
            // 아마 viewBinding.item = row 바로 아랫줄에 viewBinding.executePendingBindings()를 해주거나
            // 또는 adapter의 onViewRecycled()에서 ImageView의 src를 clear해주면 개선될거야.
            // 다른 코드들과 어떻게 연관되느냐에 따라 다르긴 하겠지만 보통은 그런식으로 해!
            val row = filteredList[position]
            viewBinding.item = row
        }
    }

    override fun getFilter(): Filter {
        return if (addedList.isNotEmpty() && currFilter != "ALL") {
            loadMoreFilter
        } else
            collectionFilter
    }

    private val collectionFilter: Filter = object : Filter() {
        //Automatic on background thread
        override fun performFiltering(constraint: CharSequence): FilterResults {
            filteredList = if (constraint == "ALL" || constraint.isEmpty()) {
                unFilteredList
            } else {
                val filteringList = ArrayList<ImagesDocuments>()
                for (item in unFilteredList) {
                    if (item.collection == constraint.toString()) {
                        filteringList.add(item)
                    }
                }
                filteringList
            }
            val results = FilterResults()
            results.values = filteredList
            return results
        }

        //Automatic on UI thread
        override fun publishResults(
            constraint: CharSequence,
            results: FilterResults
        ) {
            filteredList = results.values as ArrayList<ImagesDocuments>
            notifyDataSetChanged()
        }
    }

    private val loadMoreFilter: Filter = object : Filter() {
        //Automatic on background thread
        override fun performFiltering(constraint: CharSequence): FilterResults {
            val filteringList = ArrayList<ImagesDocuments>()
            for (item in addedList) {
                if (item.collection == constraint.toString()) {
                    filteringList.add(item)
                }
            }
            addedList = ArrayList() //when filter was changed it have to reset
            val results = FilterResults()
            results.values = filteringList
            return results
        }

        //Automatic on UI thread
        override fun publishResults(
            constraint: CharSequence,
            results: FilterResults
        ) {
            val filteredAddedList = results.values as ArrayList<ImagesDocuments>
            filteredList.addAll(filteredAddedList)
            notifyItemRangeInserted(filteredposition, filteredAddedList.size)
        }
    }
}