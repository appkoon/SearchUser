package com.appkoon.searchuser.ui.like

import android.annotation.SuppressLint
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.*
import com.appkoon.searchuser.R
import com.appkoon.searchuser.databinding.FragmentLikeBinding
import com.appkoon.searchuser.di.Injectable
import com.appkoon.searchuser.vo.Item
import com.appkoon.searchuser.ui.MainActivity
import com.appkoon.searchuser.ui.search.SearchFragment
import com.appkoon.searchuser.ui.common.ItemAdapter
import kotlinx.android.synthetic.main.fragment_like.*
import javax.inject.Inject


class LikeFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var binding: FragmentLikeBinding
    private val itemAdapter = ItemAdapter()

    val viewModel: LikeViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(LikeViewModel::class.java)
    }

    companion object {
        fun newInstance(): LikeFragment = LikeFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        itemAdapter.onListItemClickListener = ::onItemClicked
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_like, container, false)
        binding.viewModel = viewModel
        binding.adapter = itemAdapter
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
        recyclerView.adapter = itemAdapter
    }

    private fun onItemClicked(item: Item, position: Int) {
        Log.d("good", "position = $position")
        viewModel.delete(item)
        itemAdapter.checkUnlike(position)
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