package com.school.service.utils

inline infix fun <K, reified V> MutableMap<K, V>.oneLess(key: K) where V : Number, V : Comparable<V> {
    val value = this[key] ?: return
    val remaining = when (V::class) {
        Int::class -> (value.toInt() - 1) as V
        Long::class -> (value.toLong() - 1L) as V
        Short::class -> (value.toShort() - 1).toShort() as V
        Byte::class -> (value.toByte() - 1).toByte() as V
        else -> throw IllegalArgumentException("oneLess :: Only integers like Int, Long, Short, Byte allowed, passed class: ${V::class}")
    }

    if (remaining > zeroOf()) {
        this[key] = remaining
    } else {
        this.remove(key)
    }
}

inline fun <reified V> zeroOf(): V where V : Number, V : Comparable<V> =
    when (V::class) {
        Int::class -> 0 as V
        Long::class -> 0L as V
        Short::class -> 0.toShort() as V
        Byte::class -> 0.toByte() as V
        else -> throw IllegalArgumentException("zeroOf :: Only integers like Int, Long, Short, Byte allowed, passed class:  ${V::class}")
    }

