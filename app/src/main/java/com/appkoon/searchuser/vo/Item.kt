package com.appkoon.searchuser.vo

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
data class Item (
        @PrimaryKey
        val id: Int,
        val login: String,
        val node_id: String,
        val avatar_url: String,
        val gravatar_id: String,
        val url: String,
        val html_url: String,
        val followers_url: String,
        val subscriptions_url: String,
        val organizations_url: String,
        val repos_url: String,
        val received_events_url: String,
        val type: String,
        val score: String,
        var like: Boolean
)
