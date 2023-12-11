package com.example.cryptocurrencyapp.domain.usecases.getcoins

import android.net.http.HttpException
import com.example.cryptocurrencyapp.common.Resource
import com.example.cryptocurrencyapp.data.remote.dto.toCoin
import com.example.cryptocurrencyapp.domain.model.Coin
import com.example.cryptocurrencyapp.domain.repository.CoinRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class GetCoinsUsecase @Inject constructor(
    private val repository: CoinRepository
) {
    operator fun invoke() : Flow<Resource<List<Coin>>>  = flow {
        try {
            emit(Resource.Loading<List<Coin>>())
            val coins  = repository.getCoins()
            emit(Resource.Success<List<Coin>>(coins.map { it.toCoin() }))
        } catch (e : IOException){
            emit(Resource.Error<List<Coin>>("Couldn't Reach Server, Check your internet"))
        } catch (e: Exception){
            emit(Resource.Error<List<Coin>>(e.localizedMessage ?: "Unexpected Error Occurred"))
        }
    }
}