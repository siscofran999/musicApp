package com.sisco.musicapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sisco.musicapp.data.model.ItemTrack
import com.sisco.musicapp.databinding.ItemMusicBinding

class MainAdapter(private val onClick: (String, String) -> Unit) :
    ListAdapter<ItemTrack, MainAdapter.MainViewHolder>(
        DiffCallback
    ) {

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<ItemTrack>() {
            override fun areItemsTheSame(oldItem: ItemTrack, newItem: ItemTrack): Boolean {
                return oldItem.added_at == newItem.added_at
            }

            override fun areContentsTheSame(oldItem: ItemTrack, newItem: ItemTrack): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val binding = ItemMusicBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MainViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val model = getItem(position)
        holder.bind(model)

        holder.itemView.setOnClickListener {
            onClick(model.track.preview_url ?: "", model.track.name)
        }
    }

    class MainViewHolder(private val binding: ItemMusicBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(model: ItemTrack) {
            if(model.track.album.images.isNotEmpty()) {
                val img = model.track.album.images[1].url
                Glide.with(binding.root)
                    .load(img)
                    .into(binding.img)
            }
            binding.txvAlbums.text = model.track.album.name
            binding.txvName.text = model.track.name
            val data = model.track.artists
            binding.txvArtist.text = data[0].name
        }
    }
}