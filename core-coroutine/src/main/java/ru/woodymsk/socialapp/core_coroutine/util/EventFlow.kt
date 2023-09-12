package ru.woodymsk.socialapp.core_coroutine.util

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.first

/**
 * Позволяет эмитить сразу нескольким подписчикам все отправленные события.
 * Ждет, когда появится хотя бы один подписчик, а затем отправляем ему все ивенты.
 */
class EventFlow<T> : FlowCollector<T>, Flow<T> {
    private val sharedFlow = MutableSharedFlow<T>()

    override suspend fun emit(value: T) {
        sharedFlow.subscriptionCount.first { it > 0 } // ждет, пока у sharedFlow появятся подписчики
        sharedFlow.emit(value)
    }

    override suspend fun collect(collector: FlowCollector<T>) {
        sharedFlow.collect(collector)
    }
}