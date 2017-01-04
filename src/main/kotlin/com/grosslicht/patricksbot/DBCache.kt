package com.grosslicht.patricksbot

import com.google.common.cache.Cache
import com.google.common.cache.CacheBuilder
import com.grosslicht.patricksbot.models.Channel
import com.grosslicht.patricksbot.models.Guild
import com.grosslicht.patricksbot.models.User


/**
 * Created by patrickgrosslicht on 04/01/17.
 */
object DBCache {
    val userCache: Cache<String, User> = CacheBuilder.newBuilder()
            .recordStats()
            .maximumSize(System.getenv("USER_CACHE_SIZE")?.toLong() ?: 500)
            .build()

    val channelCache: Cache<String, Channel> = CacheBuilder.newBuilder()
            .recordStats()
            .maximumSize(System.getenv("CHANNEL_CACHE_SIZE")?.toLong() ?: 100)
            .build()

    val guildCache: Cache<String, Guild> = CacheBuilder.newBuilder()
            .recordStats()
            .maximumSize(System.getenv("GUILD_CACHE_SIZE")?.toLong() ?: 100)
            .build()
}
