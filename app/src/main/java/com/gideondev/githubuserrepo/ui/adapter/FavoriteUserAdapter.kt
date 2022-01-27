package com.gideondev.githubuserrepo.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gideondev.githubuserrepo.databinding.ItemUserLayoutBinding
import com.gideondev.githubuserrepo.model.User

class FavoriteUserAdapter(
    private val items: MutableList<User?>,
    private val listner: ClickListner
) : RecyclerView.Adapter<FavoriteUserAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemUserLayoutBinding.inflate(inflater)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    fun addItem(user: User) {
        items.add(user)
        notifyItemInserted(items.size)
    }

    fun addAllItem(userList: MutableList<User?>) {
        if (items.isNotEmpty()) {
            items.clear()
        }
        items.addAll(userList)
        notifyDataSetChanged()
    }
    fun removeAllData() {
        if (items.isNotEmpty() && items.size > 0) {
            items.clear()
        }
        notifyDataSetChanged()
    }

    fun removeAt(position: Int) {
        items.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val model: User? = items[position]
        holder.itemView .setOnClickListener {
            listner.onItemClick(model, position)
        }
        holder.bind(model)
    }

    inner class ViewHolder(private val binding: ItemUserLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: User?) {
            binding.txtDisplayName.text = item?.login.toString()
            Glide.with(binding.imgUser .context)
                .load(item?.avatarUrl)
                .centerCrop()
                .into(binding.imgUser)
        }

    }

    interface ClickListner {
        fun onItemClick(model: User?, position: Int)
    }



}