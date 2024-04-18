package com.dv.comfortly.domain.usecases

interface BaseUseCase {
    interface Input<Input> : BaseUseCase {
        suspend operator fun invoke(input: Input)
    }

    interface Output<Output> {
        suspend operator fun invoke(): Output
    }

    interface InputOutput<Input, Output> {
        suspend operator fun invoke(input: Input): Output
    }
}
