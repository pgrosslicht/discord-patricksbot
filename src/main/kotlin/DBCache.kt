package com.grosslicht.patricksbot

import com.google.common.cache.Cache
import com.google.common.cache.CacheBuilder
import com.grosslicht.patricksbot.models.Channel
import com.grosslicht.patricksbot.models.Guild
import com.grosslicht.patricksbot.models.User


object DBCache {
    val userCache: Cache<String, User> = CacheBuilder.newBuilder()
            .maximumSize(System.getenv("USER_CACHE_SIZE")?.toLong() ?: 500)
            .build()

    val channelCache: Cache<String, Channel> = CacheBuilder.newBuilder()
            .maximumSize(System.getenv("CHANNEL_CACHE_SIZE")?.toLong() ?: 100)
            .build()

    val guildCache: Cache<String, Guild> = CacheBuilder.newBuilder()
            .maximumSize(System.getenv("GUILD_CACHE_SIZE")?.toLong() ?: 100)
            .build()
}
