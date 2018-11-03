package com.appkoon.searchuser.ui.search

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import com.jakewharton.rxbinding2.widget.RxTextView
import com.appkoon.searchuser.R
import com.appkoon.searchuser.api.Status
import com.appkoon.searchuser.databinding.FragmentSearchBinding
import com.appkoon.searchuser.di.Injectable
import com.appkoon.searchuser.ui.ActionManager
import com.appkoon.searchuser.ui.MainActivity
import com.appkoon.searchuser.viewmodel.SearchViewModel
import com.appkoon.searchuser.model.vo.Item
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_search.*
import rx.subjects.PublishSubject
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SearchFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    val viewModel: SearchViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(SearchViewModel::class.java)
    }

    private lateinit var title: String
    private var userAdapter = UserAdapter()
    lateinit var binding: FragmentSearchBinding
    val itemSubject: PublishSubject<Item> = PublishSubject.create<Item>()


    companion object {
        var instance: SearchFragment? = null
        fun newInstance(): SearchFragment = SearchFragment()
    }


    init {
        itemSubject.subscribe{ userAdapter.checkUnlike(it) }
    }


    override fun onResume() {
        super.onResume()
        (activity as MainActivity).supportActionBar?.let{
            it.setDisplayShowTitleEnabled(true)
            it.setDisplayHomeAsUpEnabled(false)
            it.title = title
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        instance = this
        title = getString(R.string.search_title)
        userAdapter.onListItemClickListener = ::onItemClicked
        viewModel.responseLiveData.observe(this, Observer<List<Item>>(::onChanged))
        viewModel.messageLiveData.observe(this, Observer<String>(::onChanged))
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d("good", "onCreateView")
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false)
        binding.viewModel = viewModel
        return binding.root
    }


    @SuppressLint("CheckResult")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        RxTextView.afterTextChangeEvents(edit_query)
                .subscribeOn(Schedulers.newThread())
                .debounce(1, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
                .map { it.editable().toString() }
                .subscribe { it ->
                    if (it.isNotEmpty()) {
                       dismissKeyboard()
                       userAdapter.clear()
                       viewModel.search(it, true)
                       (activity as MainActivity).supportActionBar?.let{ bar ->
                           title = String.format(getString(R.string.search_result), it)
                           bar.title = title
                           edit_query.setText("")
                       }
                   }
                }

        binding.callback = object : RetryCallback {
            override fun retry() {
                viewModel.retry()
            }
        }
    }

    private fun initRecyclerView() {
        with(recyclerView) {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = userAdapter
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val totalItemCount = layoutManager.itemCount
                    val lastVisibleItemPosition = layoutManager.findLastCompletelyVisibleItemPosition()
                    if(totalItemCount > 0 && lastVisibleItemPosition != RecyclerView.NO_POSITION && (totalItemCount -1 <= lastVisibleItemPosition)){
                        binding.loadingMore = true
                        viewModel.searchNextPage()
                    }
                }
            })
        }
    }


    private fun onChanged(data: Any?) {
        binding.loadingMore = false
        when (data) {
            is String -> Snackbar.make(recyclerView, data, Snackbar.LENGTH_SHORT).show()
            is List<*> -> userAdapter.setData(data as List<Item>)
        }
    }


    private fun onItemClicked(item: Item, position: Int) {
        viewModel.insert(item)
        userAdapter.checkLike(position)
    }


    private fun dismissKeyboard() {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(edit_query.windowToken, 0)
    }


    override fun onDestroy() {
        super.onDestroy()
        itemSubject.onCompleted()
    }

}