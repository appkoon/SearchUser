package com.appkoon.searchuser.ui

import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.appkoon.searchuser.R
import com.appkoon.searchuser.common.Action
import com.appkoon.searchuser.common.ActionManager
import com.appkoon.searchuser.common.ActionType
import com.appkoon.searchuser.ui.like.LikeFragment
import com.appkoon.searchuser.ui.search.SearchFragment
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity(), HasSupportFragmentInjector {

    @Inject
    lateinit var actionManager: ActionManager

    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        toolbar.setTitleTextColor(Color.WHITE)
        supportActionBar?.apply {
            setDisplayShowTitleEnabled(true)
            setDisplayHomeAsUpEnabled(false)
        }
        actionManager.onActionListener = ::fireAction

        if (supportFragmentManager.findFragmentById(R.id.container) == null) {
            actionManager.fire(Action(ActionType.SEARCH_USER))
        }
    }

    private fun fireAction(action: Action) {
        when (action.type) {
            ActionType.UNKNOWN -> Log.w(javaClass.simpleName, "Unknown Action Fired!")
            ActionType.SEARCH_USER -> transition(SearchFragment.newInstance(), replace = false)
            ActionType.LIKE_USER -> transition(LikeFragment.newInstance(), replace = true)
        }
    }

    private fun transition(fragment: Fragment, keepCurrent: Boolean = true, replace: Boolean = true) {
        if (!keepCurrent && supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        }
        val transaction = supportFragmentManager.beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)

        when (replace) {
            true -> transaction.replace(R.id.container, fragment).addToBackStack(fragment.javaClass.simpleName)
            false -> transaction.add(R.id.container, fragment)
        }
        transaction.commit()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.menu_like -> {
                fireAction(Action(ActionType.LIKE_USER))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> = fragmentInjector

}
