package com.ellen.kakaoimages.views.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.databinding.DataBindingUtil
import androidx.paging.PagedList
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ellen.kakaoimages.R
import com.ellen.kakaoimages.data.model.ImagesDocuments
import com.ellen.kakaoimages.databinding.ItemSearchImageBinding

class ImageListAdapter :
    PagedListAdapter<ImagesDocuments, ImageListAdapter.ImageViewHolder>(ProjectDiffCallback())
     {
         //Filterable

    lateinit var imageList: PagedList<ImagesDocuments>
    lateinit var filteredList: PagedList<ImagesDocuments>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val viewBinding: ItemSearchImageBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_search_image, parent, false
        )
        return ImageViewHolder(viewBinding)
    }

    //TODO: getItemcount없고viewtype만 있어야 뷰가 그려짐
    /**
     * The thing is if you'll apply filter {} to PagedList you will have List and in

    submitList((PagedList<Product>) results.values);
    it will crash because types are different.

    Behaviour what you want is possible but is different in case of "paged recycler"

    In simple RecyclerView you load and "store" data in it so you can filter it. If user change page or scroll down you just add more data and again you can filter it (but you also have to fetch and add in adapter data from server with applied filter in case if they not exist in adapter yet).

    In paged list, in my opinion, if you need to filter it, you should add filter in your DataSource and create new PagedList with this filtered data source (filter applied to api or database call in loadInitial and loadRange methods). So when it will scroll down (or load new page) you will load items from filtered pages.

    Hope it'll help
     */
    override fun getItemViewType(position: Int): Int = position

//    override fun getItemCount(): Int {
//        return filteredList.size
//    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        //TODO: filteredList[position]으로 가져오면 리스트에 아무것도 없어서 에러남.
        getItem(position)?.let {
            holder.onBind(it)
        }

        holder.itemView.setOnClickListener {
            onItemClickListener?.let {
//                it(filteredList[position]!!)
            }
        }
    }

//    fun setImages(items: LiveData<PagedList<ImagesDocuments>>) {
//        val position = filteredList.size
//        this.filteredList.addAll(items)
//        notifyItemRangeInserted(position, items.size)
//    }

    fun clear() {
//        this.filteredList.clear()
        notifyDataSetChanged()
    }

    inner class ImageViewHolder(private val viewBinding: ItemSearchImageBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {

        fun onBind(item: ImagesDocuments) {
//            val row = filteredList[position]
            viewBinding.item = item
        } //end clickListener
    }

    private var onItemClickListener: ((ImagesDocuments) -> Unit)? = null
    fun setOnItemClickListener(listener: (ImagesDocuments) -> Unit) {
        onItemClickListener = listener
    }

//    override fun getFilter(): Filter {
//        return collectionFilter
//    }
//
//    private val collectionFilter: Filter = object : Filter() {
//        //Automatic on background thread
//        override fun performFiltering(constraint: CharSequence): FilterResults {
//            if (constraint == null || constraint.isEmpty()) {
//                filteredList = imageList
//            } else {
//                var filteringList = ArrayList<ImagesDocuments>()
//                for (item in filteredList) {
//                    //TODO filter 대상 setting
//                    if (item.collection == constraint.toString()) {
//                        filteringList.add(item)
//                    }
//                }
//                filteredList = filteringList
//            }
//            val results = FilterResults()
//            results.values = filteredList
//            return results
//        }
//
//        //Automatic on UI thread
//        override fun publishResults(
//            constraint: CharSequence,
//            results: FilterResults
//        ) {
//            filteredList = results.values as PagedList<ImagesDocuments>
//            notifyDataSetChanged()
//        }
//    }

}

class ProjectDiffCallback : DiffUtil.ItemCallback<ImagesDocuments>() {
    override fun areItemsTheSame(oldItem: ImagesDocuments, newItem: ImagesDocuments): Boolean =
        oldItem.thumbnail_url == newItem.thumbnail_url

    override fun areContentsTheSame(oldItem: ImagesDocuments, newItem: ImagesDocuments): Boolean =
        oldItem == newItem
}