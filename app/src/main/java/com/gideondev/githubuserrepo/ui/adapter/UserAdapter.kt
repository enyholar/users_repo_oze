package com.gideondev.githubuserrepo.ui.adapter

import android.content.Context
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Build
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gideondev.githubuserrepo.databinding.ItemProgressLoadingBinding
import com.gideondev.githubuserrepo.databinding.ItemUserLayoutBinding
import com.gideondev.githubuserrepo.model.User
import com.gideondev.githubuserrepo.utils.Constants.API.VIEW_TYPE_ITEM
import com.gideondev.githubuserrepo.utils.Constants.API.VIEW_TYPE_LOADING

class UserAdapter(
    private val items: MutableList<User?>,
    private val listner: ClickListner
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    lateinit var mcontext: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        mcontext = parent.context
        return if (viewType == VIEW_TYPE_ITEM) {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemUserLayoutBinding.inflate(inflater)
            ItemViewHolder(binding)
        } else {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemProgressLoadingBinding.inflate(inflater)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                binding.progressbar.indeterminateDrawable.colorFilter = BlendModeColorFilter(Color.WHITE, BlendMode.SRC_ATOP)
            } else {
                binding.progressbar.indeterminateDrawable.setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY)
            }
            LoadingViewHolder(binding)
        }
    }

    fun addLoadingView() {
        //add loading item
        Handler().post {
            items.add(null)
            notifyItemInserted(items.size - 1)
        }
    }

    fun removeLoadingView() {
        //Remove loading item
        if (items.size != 0) {
            items.removeAt(items.size - 1)
            notifyItemRemoved(items.size)
        }
    }

    class ItemViewHolder(val binding: ItemUserLayoutBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item: User?, position: Int) {
            with(binding) {
                binding.txtDisplayName.text = item?.login.toString()
                Glide.with(binding.imgUser .context)
                    .load(item?.avatarUrl)
                    .centerCrop()
                    .into(binding.imgUser)
            }
        }
    }



    class LoadingViewHolder(itemView: ItemProgressLoadingBinding) : RecyclerView.ViewHolder(itemView.root)

    override fun getItemCount(): Int = items!!.size

    fun addItem(model: User) {
        items.add(model)
        notifyItemInserted(items.size)
    }

    fun addAllItem(userList: MutableList<User?>) {
        // items.clear()
        if (items != null) {
            items.addAll(userList)
        }
        notifyDataSetChanged()
    }


    fun removeAt(position: Int) {
        if (items != null) {
            items.removeAt(position)
        }
        notifyItemRemoved(position)
    }

    interface ClickListner {
        fun onItemClick(model: User?, position: Int)
    }

    override fun getItemViewType(position: Int): Int {
        return if (items[position] == null) {
            VIEW_TYPE_LOADING
        } else {
            VIEW_TYPE_ITEM
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.itemViewType == VIEW_TYPE_ITEM) {
            if (holder is ItemViewHolder) {
                val model: User? = items!![position]

                holder.itemView .setOnClickListener(View.OnClickListener {
                    listner.onItemClick(model, position)
                })
                holder.bind(model,position)
            }
        }
    }





}