package com.just_me.news.net

import com.google.gson.JsonObject
import com.just_me.news.RecyclerData
import com.just_me.news.core.exception.Failure
import com.just_me.news.core.functional.Either

class DataRecyclerUseCase(private val dataRepository: DataRepository): UseCase<ArrayList<RecyclerData>, UseCase.None>() {

    override suspend fun run(params: UseCase.None): Either<Failure, ArrayList<RecyclerData>> {
        return dataRepository.getRecyclerData()
    }
}