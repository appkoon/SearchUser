package com.appkoon.searchuser.ui.detail

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingComponent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.*
import com.appkoon.searchuser.R
import com.appkoon.searchuser.binding.FragmentDataBindingComponent
import com.appkoon.searchuser.databinding.FragmentLikeBinding
import com.appkoon.searchuser.di.Injectable
import com.appkoon.searchuser.model.vo.Item
import com.appkoon.searchuser.ui.MainActivity
import com.appkoon.searchuser.ui.search.SearchFragment
import com.appkoon.searchuser.ui.search.UserAdapter
import com.appkoon.searchuser.viewmodel.LikeViewModel
import kotlinx.android.synthetic.main.fragment_like.*
import javax.inject.Inject


class LikeFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var binding: FragmentLikeBinding
    private lateinit var viewModel: LikeViewModel
    private val userAdapter = UserAdapter()


    companion object {
        fun newInstance(): LikeFragment = LikeFragment()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userAdapter.onListItemClickListener = ::onItemClicked
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(LikeViewModel::class.java)
        viewModel.itemLiveData.observe(this, Observer(::setList) )
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_like, container, false)
        return binding.root
    }


    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        (activity as MainActivity).supportActionBar?.let{
            it.setDisplayShowTitleEnabled(true)
            it.setDisplayHomeAsUpEnabled(true)
            it.title = getString(R.string.like_title)
        }
        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = userAdapter
    }


    private fun setList(data: List<Item>?) {
        if( userAdapter.itemCount == 0){
            userAdapter.setData(data!!.toList())
        }
    }


    private fun onItemClicked(item: Item, position: Int) {
        Log.d("good", "position = $position")
        viewModel.delete(item)
        userAdapter.checkUnlike(position)
        SearchFragment.instance?.itemSubject?.onNext(item)
    }


    override fun onPrepareOptionsMenu(menu: Menu) {
        val item= menu.findItem(R.id.menu_like)
        item.isVisible = false
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
                    android.R.id.home -> {
                        activity?.onBackPressed()
                        true
                    }
                    else -> super.onOptionsItemSelected(item)
        }
    }

}