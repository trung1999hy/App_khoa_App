package com.example.LockGuardPro.ui.listapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.LockGuardPro.model.Lock
import com.thn.LockGuardPro.R
import com.thn.LockGuardPro.databinding.LayoutItemAppBinding


class AppListAdapter(val type: Int = 0, val itemOnClick: (Lock) -> Unit) :
    ListAdapter<Lock, AppListAdapter.ViewHolder>(object : DiffUtil.ItemCallback<Lock>() {
        override fun areItemsTheSame(oldItem: Lock, newItem: Lock): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Lock, newItem: Lock): Boolean {
            return oldItem == newItem
        }

    }) {
    private var list: ArrayList<Lock> = arrayListOf()

    inner class ViewHolder(private val layoutItemAppBinding: LayoutItemAppBinding) :
        RecyclerView.ViewHolder(layoutItemAppBinding.root) {
        fun bind(lock: Lock) {
            itemView.setOnClickListener {
                itemOnClick.invoke(lock)
            }
            if (type == 0) {
                layoutItemAppBinding.lock.setImageResource(R.drawable.unlocked)
            } else layoutItemAppBinding.lock.setImageResource(R.drawable.lock_icon)
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