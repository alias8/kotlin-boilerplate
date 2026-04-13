package org.example.utils

import org.springframework.data.redis.core.RedisTemplate
import java.util.concurrent.TimeUnit

const val REDIS_TTL_SECONDS = 60L
const val REDIS_MAX_SIZE = 60L


fun getRedisSomethingKey(id: String): String {
    return "something:$id"
}

fun getRedisSomethingValue(redisTemplate: RedisTemplate<String, String>, followedId: String): List<String>? {
    return redisTemplate.opsForList().range(getRedisSomethingKey(followedId), 0, -1)
}

fun setRedisSomethingKey(redisTemplate: RedisTemplate<String, String>, value: String) {
    val cacheKey = getRedisSomethingKey(value)
    redisTemplate.opsForList().leftPush(cacheKey, value)
    redisTemplate.expire(cacheKey, REDIS_TTL_SECONDS, TimeUnit.SECONDS)
    redisTemplate.opsForList().trim(cacheKey, 0, REDIS_MAX_SIZE - 1)
}