package com.just_me.news.net

import com.just_me.news.core.exception.Failure
import com.just_me.news.core.functional.Either

class DataUseCase(private val dataRepository: DataRepository): UseCase<String, DataUseCase.Params>() {

    override suspend fun run(params: Params): Either<Failure, String> {
        return dataRepository.getData(params.country)
    }

    data class Params(val country: String)
}