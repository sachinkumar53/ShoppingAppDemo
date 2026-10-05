package com.sachin.shopping.demo.ui.search

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import com.sachin.shopping.demo.data.model.ProductListItem
import com.sachin.shopping.demo.data.repository.SearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import org.orbitmvi.orbit.OrbitContainerHost
import org.orbitmvi.orbit.viewmodel.orbitContainer
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repo: SearchRepository
) : ViewModel(), OrbitContainerHost<SearchUiState, SearchUiState, Nothing> {

    private val queryFlow = MutableStateFlow(TextFieldValue(""))

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    override val container = orbitContainer<SearchUiState, Nothing>(SearchUiState()) {
        intent {
            queryFlow
                .debounce(300.milliseconds)
                .map { it.text.trim() }
                .distinctUntilChanged()
                .flatMapLatest { q ->
                    if (q.isEmpty()) flowOf(emptyList()) else repo.search(q)
                }
                .collect { results ->
                    reduce { state.copy(results = results, isSearching = false) }
                }
        }
    }

    fun onQueryChange(query: TextFieldValue) = intent {
        reduce {
            state.copy(
                query = query,
                isSearching = query.text.isNotBlank()
            )
        }
        queryFlow.update { query }
    }

    fun clear() = onQueryChange(TextFieldValue(""))
}

data class SearchUiState(
    val query: TextFieldValue = TextFieldValue(""),
    val results: List<ProductListItem> = emptyList(),
    val isSearching: Boolean = false
) {
    val isBlank get() = query.text.isBlank()
    val showNoResults get() = !isBlank && !isSearching && results.isEmpty()
}
