package com.lawrence.pokemon.viewModel

import com.lawrence.pokemon.model.Ability
import com.lawrence.pokemon.model.DetailsModel
import com.lawrence.pokemon.model.PokemonAbilityModel
import com.lawrence.pokemon.model.PokemonDetailsSpritesModel
import com.lawrence.pokemon.model.PokemonItem
import com.lawrence.pokemon.model.PokemonListModel
import com.lawrence.pokemon.networking.PokemonService
import com.lawrence.pokemon.repo.PokemonRepository
import com.lawrence.pokemon.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.mockito.Mock
import org.mockito.Mockito.anyString
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever

class MainViewModelTest {

    @Mock
    private lateinit var repository: PokemonRepository

    @Mock
    lateinit var service: PokemonService

    private lateinit var viewModel: MainViewModel

    @Mock
    private var testDispatcher = StandardTestDispatcher()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        viewModel = MainViewModel(repository)
    }

    @Test
    fun `getPokemon should return success if fetchPokemonList is successful with data`() {
        runBlocking {
            val offset = 0
            val limit = 100
            val pokemonList = PokemonListModel(
                count = 3,
                results = listOf(
                    PokemonItem(name = "bulbasaur", url = ""),
                    PokemonItem(name = "ivysaur", url = ""),
                    PokemonItem(name = "venusaur", url = "")
                )
            )

            val flow = flowOf(Result.Loading, Result.Success(pokemonList))
            val results = mutableListOf<Result<PokemonListModel>>()
            flow.collect { result ->
                results.add(result)
            }
            whenever(repository.fetchPokemonList(offset, limit)).thenReturn(flow)
            viewModel.getPokemon()

            val uiState = viewModel.uiState.value

            verify(repository).fetchPokemonList(offset, limit)
            assertTrue(uiState.isSuccess)
            assertEquals(2, results.size)
        }
    }


    @Test
    fun `getDetailsForAllPokemon should return success result if service is successful`() {
        runTest {
            val details = DetailsModel(
                sprite = PokemonDetailsSpritesModel(imageURL = ""),
                abilities = listOf(PokemonAbilityModel(Ability("attack")))
            )

            viewModel.pokemonList.addAll(
                listOf(
                    PokemonItem(name = "bulbasaur", url = ""),
                    PokemonItem(name = "ivysaur", url = ""),
                    PokemonItem(name = "venusaur", url = "")
                )
            )

            whenever(repository.fetchPokemonDetail(anyString())).thenReturn(flow {
                emit(Result.Loading)
                emit(Result.Success(details))
            })

            viewModel.getDetailsForAllPokemon()

            assertTrue(viewModel.uiState.value.isSuccess)
            assertEquals(3, viewModel.pokemonDetailsMap.size)
            assertEquals(details, viewModel.pokemonDetailsMap["bulbasaur"])
        }
    }

    @Test
    fun `onSearchQueryChanged updates search query`() {
        val query = "bulbasaur"
        viewModel.onSearchQueryChanged(query)
        assertEquals(query, viewModel.searchQuery.value)
    }

    @Test
    fun `getFilteredPokemonList filters by search query`() {
        val details = DetailsModel(
            sprite = PokemonDetailsSpritesModel(imageURL = ""),
            abilities = listOf(PokemonAbilityModel(Ability()))
        )

        viewModel.pokemonDetailsMap["bulbasaur"] = details
        viewModel.pokemonDetailsMap["ivysaur"] = details
        viewModel.pokemonDetailsMap["venusaur"] = details
        viewModel.pokemonList.addAll(
            listOf(
                PokemonItem(name = "bulbasaur", url = ""),
                PokemonItem(name = "ivysaur", url = ""),
                PokemonItem(name = "venusaur", url = "")
            )
        )

        viewModel.onSearchQueryChanged("bulb")
        val filteredList = viewModel.getFilteredPokemonList()

        assertEquals(1, filteredList.size)
        assertEquals("", filteredList[0].name)
    }
}