package com.sisco.musicapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sisco.musicapp.data.model.ItemArtist
import com.sisco.musicapp.databinding.ItemSearchBinding

class SearchAdapter: ListAdapter<ItemArtist, SearchAdapter.SearchViewHolder>(DiffCallback) {

    companion object {
        private val DiffCallback = object: DiffUtil.ItemCallback<ItemArtist>(){
            override fun areItemsTheSame(oldItem: ItemArtist, newItem: ItemArtist): Boolean {
                return oldItem.id == newItem.id
            }
            override fun areContentsTheSame(oldItem: ItemArtist, newItem: ItemArtist): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SearchViewHolder {
        val binding = ItemSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val model = getItem(position)
        holder.bind(model)
    }

    class SearchViewHolder(private val binding: ItemSearchBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(model: ItemArtist) {
            if(model.image?.size != 0) {
                val img = model.image?.get(2)?.url
                Glide.with(binding.root)
                    .load(img)
                    .into(binding.img)
            }
            binding.txvName.text = model.name
        }
    }
}