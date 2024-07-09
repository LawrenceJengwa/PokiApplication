package com.lawrence.pokemon.repo

import com.lawrence.pokemon.model.DetailsModel
import com.lawrence.pokemon.model.PokemonItem
import com.lawrence.pokemon.model.PokemonListModel
import com.lawrence.pokemon.networking.PokemonService
import com.lawrence.pokemon.utils.Result
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import retrofit2.HttpException
import java.io.IOException

@RunWith(MockitoJUnitRunner::class)
class PokemonRepoImplTest {
    @Mock
    lateinit var service: PokemonService

    @Mock
    lateinit var dispatcher: TestDispatcher

    private lateinit var repository: PokemonRepository

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(dispatcher)
        repository = PokemonRepoImpl(service)
    }

    @Test
    fun `fetchPokemonList should return success if service is successful with data`() {
        val name = "bulbasaur"
        val offset = 0
        val limit = 3
        runTest {
            val pokemonList =
                PokemonListModel(
                    count = 1,
                    results =
                        listOf(
                            PokemonItem(
                                name = name,
                                url = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/1.png",
                            ),
                        ),
                )
            whenever(service.getPokemonList(offset, limit)).thenReturn(pokemonList)

            val flow = repository.fetchPokemonList(offset, limit)
            val results = mutableListOf<Result<PokemonListModel>>()
            flow.collect { result ->
                results.add(result)
            }

            verify(service).getPokemonList(any(), any())
            assertEquals(2, results.size)
            assertEquals(Result.Loading, results[0])
            assertEquals(Result.Success(pokemonList), results[1])
        }
    }

    @Test
    fun `fetchPokemonList should throw HttpException when there a network error`() {
        runBlocking {
            val offset = 1
            val limit = 3
            val exception = Mockito.mock(HttpException::class.java)
            whenever(service.getPokemonList(offset, limit)).thenThrow(exception)

            val flow = repository.fetchPokemonList(offset, limit)
            val values = flow.toList()

            assertEquals(2, values.size)
            assertEquals(Result.Loading, values[0])
            assertEquals(Result.Error(exception.message().orEmpty()), values[1])
        }
    }

    @Test
    fun `fetchPokemonList should throw an IOException and set error message when service fails`() {
        runBlocking {
            val offset = 0
            val limit = 0
            val exception = IOException()
            val error = ""

            whenever(service.getPokemonList(offset, limit)).thenAnswer { throw exception }

            val flow = repository.fetchPokemonList(offset, limit)
            val values = flow.toList()

            assertEquals(2, values.size)
            assertEquals(Result.Loading, values[0])
            assertEquals(
                Result.Error(errorMessage = error),
                values[1],
            )
        }
    }

    @Test
    fun `fetchPokemonDetail should return success if service is successful with data`() {
        runTest {
            var name1 = "bulbasaur"
            val detailsModel =
                DetailsModel().apply {
                    weight = 69
                    height = 20
                    name = name1
                }
            whenever(service.getPokemonDetails(name1)).thenReturn(detailsModel)

            val flow = repository.fetchPokemonDetail(name1)
            val results = mutableListOf<Result<DetailsModel>>()
            flow.collect { result ->
                results.add(result)
            }

            verify(service).getPokemonDetails(name1)

            assertEquals(2, results.size)
            assertEquals(Result.Loading, results[0])
            assertEquals(Result.Success(detailsModel), results[1])
        }
    }

    @Test
    fun `fetchPokemonDetail should throw HttpException when there a network error`() {
        runBlocking {
            val name = "ivy"
            val exception = Mockito.mock(HttpException::class.java)
            whenever(service.getPokemonDetails(name = name)).thenThrow(exception)

            val flow = repository.fetchPokemonDetail(name = name)
            val values = flow.toList()

            assertEquals(2, values.size)
            assertEquals(Result.Loading, values[0])
            assertEquals(Result.Error(exception.message().orEmpty()), values[1])
        }
    }

    @Test
    fun `fetchPokemonDetail should throw an IOException and set error message when service fails`() {
        runBlocking {
            val name = ""
            val exception = IOException()
            val error = ""

            whenever(service.getPokemonDetails(name = name)).thenAnswer { throw exception }

            val flow = repository.fetchPokemonDetail(name = name)
            val values = flow.toList()

            assertEquals(2, values.size)
            assertEquals(Result.Loading, values[0])
            assertEquals(
                Result.Error(errorMessage = error),
                values[1],
            )
        }
    }
}
