package com.just_me.news.net

import com.just_me.news.core.exception.Failure
import com.just_me.news.core.functional.Either

class CodeUseCase(private val dataRepository: DataRepository): UseCase<String, CodeUseCase.Params>() {

    override suspend fun run(params: Params): Either<Failure, String> {
        return dataRepository.getCode(params.country)
    }

    data class Params(val country: String)
}