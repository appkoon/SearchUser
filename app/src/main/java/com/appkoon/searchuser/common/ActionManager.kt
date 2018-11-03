package com.appkoon.searchuser.common

import android.os.Bundle
import java.util.*

class ActionManager private constructor(){
    companion object {
        val instance: ActionManager by lazy {
            ActionManager()
        }
    }

    private val actionStack: Stack<Action> = Stack()
    var onActionListener: ((Action) -> Unit)? = null

    fun fire(action: Action) {
        actionStack.push(action)
        onActionListener?.let {
            it(action)
        }
    }
}

data class Action(val type: ActionType, val data: Bundle? = null)

enum class ActionType {
    UNKNOWN,
    SEARCH_USER,
    LIKE_USER
}