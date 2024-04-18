package com.dv.comfortly.data.raw.observers

import kotlinx.coroutines.flow.Flow

interface BaseObserver<T> {
    fun observe(): Flow<T>
}
