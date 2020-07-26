package com.ellen.kakaoimages.views.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ellen.kakaoimages.R
import com.ellen.kakaoimages.data.model.ImagesDocuments
import com.ellen.kakaoimages.databinding.ItemSearchImageBinding
import com.ellen.kakaoimages.util.Constants.Companion.FILTER

class ImageListAdapter() : RecyclerView.Adapter<ImageListAdapter.ImageViewModel>(), Filterable {

    var unFilteredlist: ArrayList<ImagesDocuments> = ArrayList()
    var filteredList: ArrayList<ImagesDocuments> = unFilteredlist

    var addedList:ArrayList<ImagesDocuments> = ArrayList()
    private var filteredposition = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewModel {
        val viewBinding: ItemSearchImageBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_search_image, parent, false
        )
        return ImageViewModel(viewBinding)
    }


    override fun getItemCount(): Int {
        return filteredList.size
    }

    override fun onBindViewHolder(model: ImageViewModel, position: Int) {
        model.onBind(position)

    }

    fun setImages(items: List<ImagesDocuments>) {
        val position = filteredList.size
        this.unFilteredlist.addAll(items)

        if (FILTER != "ALL") {
            filteredposition = position;
            addedList = items as ArrayList<ImagesDocuments>
            filter.filter(FILTER)
        }else {
            filteredList = unFilteredlist
            notifyItemRangeInserted(position, items.size)
        }
    }

    fun clear() {
        this.filteredList.clear()
        notifyDataSetChanged()
    }

    inner class ImageViewModel(private val viewBinding: ItemSearchImageBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {

        fun onBind(position: Int) {
            val row = filteredList[position]
            viewBinding.item = row
        } //end clickListener
    }

    override fun getFilter(): Filter {
        if (addedList.isNotEmpty() && FILTER != "ALL") {
            return loadMoreFilter
        } else
            return collectionFilter
    }

    private val collectionFilter: Filter = object : Filter() {
        //Automatic on background thread
        override fun performFiltering(constraint: CharSequence): FilterResults {
            if (constraint == null || constraint.isEmpty()) {
                filteredList = unFilteredlist
            } else {
                var filteringList = ArrayList<ImagesDocuments>()
                for (item in unFilteredlist) {
                    //TODO filter 대상 setting
                    if (item.collection == constraint.toString()) {
                        filteringList.add(item)
                    }
                }
                filteredList = filteringList
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
            var filteringList = ArrayList<ImagesDocuments>()
            for (item in addedList) {
                //TODO filter 대상 setting
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
         var filteredAddedList = results.values as ArrayList<ImagesDocuments>
            filteredList.addAll(filteredAddedList)
            notifyItemRangeInserted(filteredposition, filteredAddedList.size)
        }
    }

}