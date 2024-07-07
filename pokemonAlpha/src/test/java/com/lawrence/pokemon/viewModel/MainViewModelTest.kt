package com.lawrence.pokemon.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
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
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.rules.TestRule
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Mock
import org.mockito.Mockito.anyString
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever

class MainViewModelTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var repository: PokemonRepository

    @Mock
    lateinit var service: PokemonService

    @Mock
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
    fun `getPokemon should update uiState to loading if loading results`() {

        runBlocking {

            val loadingResult = Result.Loading

            whenever(repository.fetchPokemonList(anyInt(), anyInt())).thenReturn(flow {
                emit(loadingResult)
            })

            viewModel.getPokemon()

            verify(repository).fetchPokemonList(anyInt(), anyInt())
            assert(viewModel.uiState.value.isLoading)
        }
    }

    @Test
    fun `getPokemon should update uiState to Error if service throws error`() {

        runBlocking {
            val errorResult = Result.Error("An error occurred")

            whenever(repository.fetchPokemonList(anyInt(), anyInt())).thenReturn(flow {
                emit(errorResult)
            })

            viewModel.getPokemon()


            verify(repository).fetchPokemonList(anyInt(), anyInt())
            assert(viewModel.uiState.value.isError)
        }
    }


    @Test
    fun `getDetailsForAllPokemon should return success result if service is successful`() {
        runBlocking {
            val details = DetailsModel(
                sprite = PokemonDetailsSpritesModel(imageURL = ""),
                abilities = listOf(PokemonAbilityModel(Ability("attack")))
            )

            viewModel.pokemonList.addAll(
                listOf(
                    PokemonItem(
                        name = "bulbasaur",
                        url = ""
                    ),
                    PokemonItem(
                        name = "ivysaur",
                        url = ""
                    ),
                    PokemonItem(
                        name = "venusaur",
                        url = ""
                    )
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
    fun `getDetailsForAllPokemon should update uiState to loading when fetchPokemonDetail is running`() {
        runBlocking {
            val pokemonList = listOf(PokemonItem("Pikachu", "url"))
            viewModel.pokemonList.addAll(pokemonList)

            val loadingResult = Result.Loading

            whenever(repository.fetchPokemonDetail("Pikachu")).thenReturn(flow { emit(loadingResult) })

            viewModel.getDetailsForAllPokemon()

            verify(repository).fetchPokemonDetail("Pikachu")
            assert(viewModel.uiState.value.isLoading)
        }
    }

    @Test
    fun `getDetailsForAllPokemon should update uiState to error when fetchPokemonDetail fails`() {
        runBlocking {
            val pokemonList = listOf(
                PokemonItem(
                    name = "Pikachu",
                    url = ""
                )
            )

            viewModel.pokemonList.addAll(pokemonList)
            val errorResult = Result.Error("An error occurred")

            whenever(repository.fetchPokemonDetail("Pikachu")).thenReturn(flow { emit(errorResult) })

            viewModel.getDetailsForAllPokemon()

            verify(repository).fetchPokemonDetail("Pikachu")
            assert(viewModel.uiState.value.isError)
        }
    }

    @Test
    fun `onSearchQueryChanged updates search query`() {
        val query = "bulbasaur"
        viewModel.onSearchQueryChanged(query)
        assertEquals(query, viewModel.searchQuery.value)
    }

    @Test
    fun `getFilteredPokemonList filters and returns match by search query`() {
        val details = DetailsModel(
            name = "bulbasaur",
            sprite = PokemonDetailsSpritesModel(imageURL = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/1.png"),
            abilities = listOf(PokemonAbilityModel(Ability(name = "punch")))
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
        assertEquals("bulbasaur", filteredList[0].name)
    }

    @Test
    fun `getFilteredPokemonList filters and returns empty list if search query does not match any item`() {
        val details = DetailsModel(
            name = "bulbasaur",
            sprite = PokemonDetailsSpritesModel(imageURL = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/1.png"),
            abilities = listOf(PokemonAbilityModel(Ability(name = "punch")))
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

        viewModel.onSearchQueryChanged("pikachu")
        val filteredList = viewModel.getFilteredPokemonList()

        assertTrue(filteredList.isEmpty())
    }
}