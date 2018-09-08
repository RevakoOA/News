package com.just_me.news.net

import com.just_me.news.core.exception.Failure
import com.just_me.news.core.functional.Either
import com.just_me.news.utils.MCrypt

class CodeUseCase(private val dataRepository: DataRepository): UseCase<String, CodeUseCase.Params>() {

    override suspend fun run(params: Params): Either<Failure, String> {
        val code = dataRepository.getCode(params.country)
        when (code) {
            is Either.Left -> return code
            is Either.Right -> {
                val mcrypt = MCrypt()
                val encrypted = String(mcrypt.decrypt(code.b))
                return Either.Right(encrypted)
            }
        }
    }

    data class Params(val country: String)
}