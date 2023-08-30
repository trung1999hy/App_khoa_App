package com.example.LockGuardPro.ui.applistlockprivate

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.LockGuardPro.model.Lock
import com.thn.LockGuardPro.databinding.LayoutItemPrivateLockBinding

class AppLockAdapter(val itemOnClick: (Lock) -> Unit) :
    ListAdapter<Lock, AppLockAdapter.ViewHolder>(object : DiffUtil.ItemCallback<Lock>() {
        override fun areItemsTheSame(oldItem: Lock, newItem: Lock): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Lock, newItem: Lock): Boolean {
            return oldItem == newItem
        }

    }) {

    inner class ViewHolder(val binding: LayoutItemPrivateLockBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(lock: Lock) {
            binding.root.setOnClickListener {
                itemOnClick.invoke(lock)
            }
            binding.lock.isChecked = !lock.pass.isNullOrEmpty()
            binding.lock.isClickable = false
            binding.appName.text = lock.appName
            Glide.with(binding.root).load(lock.drawable)
                .into(binding.logoApp)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            LayoutItemPrivateLockBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return currentList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}