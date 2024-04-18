package com.dv.comfortly.domain.usecases

import kotlinx.coroutines.flow.Flow

interface BaseFlowUseCase {
    interface Output<Output> {
        operator fun invoke(): Flow<Output>
    }

    interface InputOutput<Input, Output> {
        operator fun invoke(input: Input): Flow<Output>
    }
}
