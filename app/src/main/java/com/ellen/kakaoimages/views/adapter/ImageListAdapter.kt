package com.ellen.kakaoimages.views.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.databinding.DataBindingUtil
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ellen.kakaoimages.R
import com.ellen.kakaoimages.data.model.ImagesDocuments
import com.ellen.kakaoimages.databinding.ItemSearchImageBinding

class ImageListAdapter() :
    PagedListAdapter<ImagesDocuments, ImageListAdapter.ImageViewModel>(diffCallback), Filterable {
    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<ImagesDocuments>() {
            override fun areItemsTheSame(
                oldItem: ImagesDocuments,
                newItem: ImagesDocuments
            ): Boolean = oldItem == newItem

            override fun areContentsTheSame(
                oldItem: ImagesDocuments,
                newItem: ImagesDocuments
            ): Boolean = oldItem == newItem
        }
    }

    var imageList: ArrayList<ImagesDocuments> = ArrayList()
    var filteredList: ArrayList<ImagesDocuments> = ArrayList()

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

        model.itemView.setOnClickListener {
            onItemClickListener?.let {
                it(filteredList[position])
            }
        }
    }

    fun setImages(items: List<ImagesDocuments>) {
        val position = filteredList.size
        this.filteredList.addAll(items)
        notifyItemRangeInserted(position, items.size)
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

    private var onItemClickListener: ((ImagesDocuments) -> Unit)? = null
    fun setOnItemClickListener(listener: (ImagesDocuments) -> Unit) {
        onItemClickListener = listener
    }

    override fun getFilter(): Filter {
        return collectionFilter
    }

    private val collectionFilter: Filter = object : Filter() {
        //Automatic on background thread
        override fun performFiltering(constraint: CharSequence): FilterResults {
            if (constraint == null || constraint.isEmpty()) {
                filteredList = imageList
            } else {
                var filteringList = ArrayList<ImagesDocuments>()
                for (item in filteredList) {
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

}