package com.gideondev.githubuserrepo.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.gideondev.githubuserrepo.R
import com.gideondev.githubuserrepo.databinding.FragmentFavouriteBinding
import com.gideondev.githubuserrepo.model.User
import com.gideondev.githubuserrepo.ui.adapter.FavoriteUserAdapter
import com.gideondev.githubuserrepo.ui.viewmodel.GithubUserViewModel
import dagger.hilt.android.AndroidEntryPoint
import androidx.appcompat.app.AppCompatActivity




// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FavouriteFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class FavouriteFragment : Fragment() {
    private val viewModel: GithubUserViewModel by viewModels()
    private lateinit var userAdapter: FavoriteUserAdapter
    private lateinit var binding: FragmentFavouriteBinding
    private var userList: MutableList<User?> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavouriteBinding.inflate(
            inflater, container, false
        )
        (activity as AppCompatActivity?)!!.setSupportActionBar(binding.toolbarMain)
        binding.toolbarMain.title = getString(R.string.favorite)
        return binding.root
    }

    private fun setUpAdapter() {
        val layoutManager = LinearLayoutManager(activity)
        binding.recyclerviewFavorite.layoutManager = layoutManager
        userAdapter = FavoriteUserAdapter(userList, object : FavoriteUserAdapter.ClickListner {

            override fun onItemClick(model: User?, position: Int) {
                val intent = Intent(activity, UserDetailsActivity::class.java)
                intent.putExtra("model", model)
                intent.putExtra("fav", true)
                startActivity(intent)
            }

        })
        binding.recyclerviewFavorite.setHasFixedSize(true)
        binding.recyclerviewFavorite.adapter = userAdapter

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpAdapter()
        viewModel.favouriteUserList.observe(
            viewLifecycleOwner,
            Observer { response ->
                if (response != null) {
                    userAdapter.addAllItem(response.toMutableList())
                }

                print(response.toString())
            })
        viewModel.getAllFavoriteUsers()

        binding.tvRemoveAllUser.setOnClickListener {
            viewModel.removeAllUserFromFavorite()
            userAdapter.removeAllData()
        }

    }

    override fun onResume() {
        super.onResume()
        viewModel.getAllFavoriteUsers()
    }
}