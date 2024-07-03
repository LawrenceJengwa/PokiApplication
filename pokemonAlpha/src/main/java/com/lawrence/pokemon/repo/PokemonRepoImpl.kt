package com.lawrence.pokemon.repo

import com.lawrence.pokemon.model.DetailsModel
import com.lawrence.pokemon.model.PokemonListModel
import com.lawrence.pokemon.networking.PokemonService
import com.lawrence.pokemon.utils.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

class PokemonRepoImpl @Inject constructor(
    private val service: PokemonService,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,

    ) : PokemonRepository {
    override suspend fun fetchPokemonList(
        offset: Int,
        limit: Int
    ): Flow<Result<PokemonListModel>> = flow {
        emit(Result.Loading)
        try {
            val result = service.getPokemonList(offset = offset, limit =limit)
            emit(Result.Success(result))
        } catch (exception: HttpException) {
            emit(Result.Error(exception.message.orEmpty()))
        } catch (exception: IOException) {
            emit(Result.Error("Please check your network connection and try again!"))
        }
    }.flowOn(dispatcher)

    override suspend fun fetchPokemonDetail(name: String) : Flow<Result<DetailsModel>>
    = flow {
        emit(Result.Loading)
        try {
            val result = service.getPokemonDetails(name =  name)
            emit(Result.Success(result))
        } catch (exception: HttpException) {
            emit(Result.Error(exception.message.orEmpty()))
        } catch (exception: IOException) {
            emit(Result.Error("Please check your network connection and try again!"))
        }
    }.flowOn(dispatcher)
}