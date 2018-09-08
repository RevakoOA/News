package com.just_me.news.net

import com.google.gson.JsonObject
import com.just_me.news.RecyclerData
import com.just_me.news.core.exception.Failure
import com.just_me.news.core.functional.Either
import com.just_me.news.core.platform.NetworkHandler
import retrofit2.Call

interface DataRepository {
    fun getCode(country: String): Either<Failure, String>
    fun getRecyclerData(): Either<Failure, List<RecyclerData>>

    class Network constructor(private val networkHandler: NetworkHandler,
                              private val serviceApi: ServiceApi): DataRepository {

        override fun getCode(country: String): Either<Failure, String> {
            return when (networkHandler.isConnected) {
                true -> request(serviceApi.getData(country), {it}, String())
                false, null -> Either.Left(Failure.NetworkConnection())
            }
        }

        override fun getRecyclerData(): Either<Failure, List<RecyclerData>> {
            return when (networkHandler.isConnected) {
                true -> request(serviceApi.getRecyclerData(), {it}, emptyList())
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