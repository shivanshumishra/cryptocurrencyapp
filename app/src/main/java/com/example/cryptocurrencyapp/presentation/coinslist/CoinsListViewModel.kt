package com.example.cryptocurrencyapp.presentation.coinslist

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptocurrencyapp.common.Resource
import com.example.cryptocurrencyapp.domain.model.Coin
import com.example.cryptocurrencyapp.domain.usecases.getcoins.GetCoinsUsecase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class CoinsListViewModel @Inject constructor(
    private val getCoinsUsecase: GetCoinsUsecase
) : ViewModel() {

    private val _state = mutableStateOf(CoinListState())
    val state : State<CoinListState> = _state

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    init {
        getCoins()
    }

    fun onSearchTextChange(text : String) {
        _searchText.value = text
        Log.i("Search", _searchText.value)
    }

    fun getCoins()  {
        getCoinsUsecase().onEach { result ->
            when(result){
                is Resource.Success -> {
                    var filteredList : MutableList<Coin> = mutableListOf()
                    if(searchText.value.isEmpty()){
                        result.data?.let { filteredList.addAll(it) }
                    } else {
                        result.data?.forEach { coin ->
                            if(coin.doesMatchSearchQuery(searchText.value)){
                                filteredList.add(coin)
                            }
                        }
                    }
                    _state.value = CoinListState(
                        coins = filteredList
                    )
                }
                is Resource.Error -> {
                    _state.value = CoinListState(error = result.message ?: "Unexpected error occurred")
                }
                is Resource.Loading -> {
                    _state.value = CoinListState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

}