package com.example.LockGuardPro.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.LockGuardPro.model.Lock
import com.thn.LockGuardPro.R
import com.thn.LockGuardPro.databinding.LayoutItemAppBinding


class LockAppAdapter(val itemOnClick: (Lock) -> Unit) :
    ListAdapter<Lock, LockAppAdapter.ViewHolder>(object : DiffUtil.ItemCallback<Lock>() {
        override fun areItemsTheSame(oldItem: Lock, newItem: Lock): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Lock, newItem: Lock): Boolean {
            return oldItem == newItem
        }

    }) {

    inner class ViewHolder(
        val layoutItemAppBinding: LayoutItemAppBinding
    ) :
        RecyclerView.ViewHolder(layoutItemAppBinding.root) {
        fun bind(lock: Lock) {
            layoutItemAppBinding.root.setOnClickListener {
                itemOnClick.invoke(lock)
            }
            layoutItemAppBinding.lock.setImageResource(R.drawable.lock)
            layoutItemAppBinding.appName.text = lock.appName
            Glide.with(layoutItemAppBinding.root).load(lock.drawable)
                .into(layoutItemAppBinding.logoApp)
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            LayoutItemAppBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return currentList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun setData(list: List<Lock>) {
        submitList(list)
    }

}