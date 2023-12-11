package com.example.cryptocurrencyapp.presentation.coinslist

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptocurrencyapp.common.Resource
import com.example.cryptocurrencyapp.domain.usecases.getcoins.GetCoinsUsecase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class CoinsListViewModel @Inject constructor(
    private val getCoinsUsecase: GetCoinsUsecase
) : ViewModel() {

    private val _state = mutableStateOf(CoinListState())
    val state : State<CoinListState> = _state

    init {
        getCoins()
    }

    fun getCoins()  {
        getCoinsUsecase().onEach { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = CoinListState(
                        coins = result.data ?: emptyList()
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