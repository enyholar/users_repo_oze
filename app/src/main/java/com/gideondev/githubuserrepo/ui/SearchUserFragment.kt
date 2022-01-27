package com.gideondev.githubuserrepo.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gideondev.githubuserrepo.databinding.FragmentSearchUserBinding
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.gideondev.githubuserrepo.R
import com.gideondev.githubuserrepo.model.User
import com.gideondev.githubuserrepo.ui.adapter.UserAdapter
import com.gideondev.githubuserrepo.ui.viewmodel.GithubUserViewModel
import com.gideondev.githubuserrepo.utils.OnLoadMoreListener
import com.gideondev.githubuserrepo.utils.RecyclerViewLoadMoreScroll
import dagger.hilt.android.AndroidEntryPoint

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SearchUserFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class SearchUserFragment : Fragment() {
    private var isLoadMore: Boolean = false
    private lateinit var scrollListener: RecyclerViewLoadMoreScroll
    private val viewModel: GithubUserViewModel by viewModels()
    private lateinit var userAdapter: UserAdapter
    private lateinit var binding: FragmentSearchUserBinding
    private var userList: MutableList<User?> = ArrayList()
    var page = 1
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
         binding = FragmentSearchUserBinding.inflate(
            inflater, container, false
        )

        binding.toolbarMain.title = getString(R.string.users)

        return binding.root
    }

    private fun setUpAdapter() {
        val layoutManager = LinearLayoutManager(activity)
        binding.searchUserRecyclerview.layoutManager = layoutManager
        userAdapter = UserAdapter(userList, object  : UserAdapter.ClickListner{

            override fun onItemClick(model: User?, position: Int) {
                val intent = Intent(activity, UserDetailsActivity::class.java)
                intent.putExtra("model",model)
                startActivity(intent)
            }

        })
        binding.searchUserRecyclerview.setHasFixedSize(true)
        binding.searchUserRecyclerview.adapter = userAdapter

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpAdapter()
        setRVScrollListener()
        viewModel.searchResponseResult.observe(
            viewLifecycleOwner,
            Observer { response ->

                    if (response?.items != null && response.items.isNotEmpty()){
                        userAdapter.addAllItem(response.items.toMutableList())
                    }

                if(response.incompleteResults == false){
                    if (isLoadMore){
                        userAdapter.removeLoadingView()
                        scrollListener.setLoaded()
                    }
                    page += 1
                }
               print(response.totalCount)
        })

        fetchUserList(1)

    }

    private fun fetchUserList(page: Int){
        viewModel.searchForUser("lagos",page)
    }

    private  fun setRVScrollListener() {
       val layoutManager = LinearLayoutManager(activity)
        scrollListener = RecyclerViewLoadMoreScroll(layoutManager as LinearLayoutManager)
        scrollListener.setOnLoadMoreListener(object :
            OnLoadMoreListener {
            override fun onLoadMore() {
//                offset += 20
                isLoadMore = true
                userAdapter.addLoadingView()
                fetchUserList(page)
            }
        })
        binding.searchUserRecyclerview.addOnScrollListener(scrollListener)
    }
}