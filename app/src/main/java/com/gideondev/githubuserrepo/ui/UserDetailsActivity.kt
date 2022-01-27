package com.gideondev.githubuserrepo.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.gideondev.githubuserrepo.R
import com.gideondev.githubuserrepo.databinding.ActivityUserDetailsBinding
import com.gideondev.githubuserrepo.model.User
import com.gideondev.githubuserrepo.ui.viewmodel.GithubUserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserDetailsActivity : AppCompatActivity() {
    private lateinit var user: User
    var isFavorite = false;
    lateinit var binding: ActivityUserDetailsBinding
    private val viewModel: GithubUserViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_details)
        setSupportActionBar(binding.toolbarMain)

        supportActionBar?.apply {
            title = getString(R.string.details)
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
        getData()
        setValueToView()
        observeData()
    }

    private fun getData(){
        user = intent.getSerializableExtra("model") as User
        isFavorite = intent.getBooleanExtra("fav",false)
    }

    private fun setValueToView(){
        binding.txtUserName .text = user.login
        binding.txtScore .text = user.score.toString()

        Glide.with(binding.imgUser .context)
            .load(user.avatarUrl)
            .centerCrop()
            .into(binding.imgUser)
        binding.btnFavorite.setOnClickListener{
            if (isFavorite){
                user.id?.let { it1 -> viewModel.removeUserFromFavorite(it1) }
            }else{
                viewModel.saveUserToFavoriteList(user)
            }
        }
        if (isFavorite){
            binding.btnFavorite.text = getString(R.string.unfavorite)
        }else{
            binding.btnFavorite.text = getString(R.string.favorite)

        }
    }

    fun observeData(){
        viewModel.userAddedToFavoriteDb.observe(
            this,
            Observer { response ->
                binding.btnFavorite.text = getString(R.string.unfavorite)
                Toast.makeText(this,"User added to favorite list",Toast.LENGTH_LONG).show()
                print(response)
            })
        viewModel.userRemoveFromFavoriteDb.observe(
            this,
            Observer { response ->
                Toast.makeText(this,"User removed from favorite list",Toast.LENGTH_LONG).show()
                finish()
            })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

}