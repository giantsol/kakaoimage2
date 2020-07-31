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
import com.ellen.kakaoimages.util.Constants.Companion.FILTER

class ImageListAdapter : RecyclerView.Adapter<ImageListAdapter.ImageViewHolder>(), Filterable {

    inner class ImageViewHolder(private val viewBinding: ItemSearchImageBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {
        fun onBind(position: Int) {
            val row = filteredList[position]
            viewBinding.item = row
        }
    }

    private var unFilteredList: ArrayList<ImagesDocuments> = ArrayList()
    private var filteredList: ArrayList<ImagesDocuments> = unFilteredList

    private var addedList: ArrayList<ImagesDocuments> = ArrayList()
    private var filteredPosition = 0

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

        if (FILTER != "ALL") {
            filteredPosition = position
            addedList = items as ArrayList<ImagesDocuments>
            filter.filter(FILTER)
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

    override fun getFilter(): Filter {
        return if (addedList.isNotEmpty()) {
            loadMoreFilter
        } else{
            //when spinner item selected
            firstLoadFilter
        }
    }

    private val firstLoadFilter: Filter = object : Filter() {
        //Automatic on background thread
        override fun performFiltering(constraint: CharSequence): FilterResults {
            filteredList = if (constraint == "ALL" || constraint.isEmpty()) {
                unFilteredList
            } else {
                unFilteredList.filter { it.collection == constraint.toString() } as ArrayList
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
            val filteringList = addedList.filter { it.collection == constraint.toString() }
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
            notifyItemRangeInserted(filteredPosition, filteredAddedList.size)
        }
    }
}