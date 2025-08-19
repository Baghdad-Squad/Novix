package com.baghdad.viewmodel.utls

import app.cash.turbine.ReceiveTurbine
import kotlinx.coroutines.withTimeout

suspend fun <T> ReceiveTurbine<T>.awaitItemWhere(
    timeoutMillis: Long = 5_000,
    predicate: (T) -> Boolean
): T {
    return withTimeout(timeoutMillis) {
        var item: T
        do {
            item = awaitItem()
        } while (!predicate(item))
        item
    }
}