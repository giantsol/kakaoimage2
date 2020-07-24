package com.ellen.kakaoimages.views.adapter

import com.ellen.kakaoimages.R
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ellen.kakaoimages.databinding.ItemSearchImageBinding
import com.ellen.kakaoimages.data.model.ImagesDocuments

//TODO: clickListener null일때처리(detail activity)
class ImageListAdapter(val context: Context?) : RecyclerView.Adapter<ImageListAdapter.UserViewHolder>() {

    var userList: ArrayList<ImagesDocuments> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val viewBinding: ItemSearchImageBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_search_image, parent, false
        )
        return UserViewHolder(viewBinding)
    }


    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.onBind(position)

        holder.itemView.setOnClickListener {
//            clickListener?.onItemClick(position, userList[position])
            onItemClickListener?.let {
                it(userList[position])
            }
        }
    }

    fun setUsers(items: List<ImagesDocuments>) {
        val position = userList.size
        this.userList.addAll(items)
        notifyItemRangeInserted(position,items.size)
    }

    fun clear(){
        this.userList.clear()
        notifyDataSetChanged()
    }
    inner class UserViewHolder(private val viewBinding: ItemSearchImageBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {

        fun onBind(position: Int) {
            val row = userList[position]
            viewBinding.item = row
            } //end clickListener
        }
    private var onItemClickListener: ((ImagesDocuments) -> Unit)? = null
    fun setOnItemClickListener(listener: (ImagesDocuments) -> Unit) {
        onItemClickListener = listener
    }



}