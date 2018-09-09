package com.just_me.news.net

import com.just_me.news.core.exception.Failure
import com.just_me.news.core.functional.Either
import com.just_me.news.utils.MCrypt

class CodeUseCase(private val dataRepository: DataRepository): UseCase<String, CodeUseCase.Params>() {

    override suspend fun run(params: Params): Either<Failure, String> {
        val code = dataRepository.getCode(params.country + params.optional)
        return when (code) {
            is Either.Left -> code
            is Either.Right -> {
                val mcrypt = MCrypt()
                val encrypted = String(mcrypt.decrypt(code.b))
                Either.Right(encrypted)
            }
        }
    }

    data class Params(val country: String, val optional: String)
}