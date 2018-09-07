package com.just_me.news.net

import com.just_me.news.core.exception.Failure
import com.just_me.news.core.functional.Either
import com.just_me.news.core.platform.NetworkHandler
import retrofit2.Call

interface DataRepository {
    fun getData(country: String): Either<Failure, String>

    class Network constructor(private val networkHandler: NetworkHandler,
                              private val service: Service): DataRepository {

        override fun getData(country: String): Either<Failure, String> {
            return when (networkHandler.isConnected) {
                true -> request(service.getData(country), {it}, String())
                false, null -> Either.Left(Failure.NetworkConnection())
            }
        }

        private fun <T, R> request(call: Call<T>, transform: (T) -> R, default: T): Either<Failure, R> {
            return try {
                val response = call.execute()
                when (response.isSuccessful) {
                    true -> Either.Right(transform((response.body() ?: default)))
                    false -> Either.Left(Failure.ServerError())
                }
            } catch (exception: Throwable) {
                Either.Left(Failure.ServerError())
            }
        }
    }
}