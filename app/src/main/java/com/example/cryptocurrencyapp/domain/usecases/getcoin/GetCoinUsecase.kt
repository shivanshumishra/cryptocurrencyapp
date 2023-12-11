package com.example.cryptocurrencyapp.domain.usecases.getcoin

import android.net.http.HttpException
import com.example.cryptocurrencyapp.common.Resource
import com.example.cryptocurrencyapp.data.remote.dto.toCoin
import com.example.cryptocurrencyapp.data.remote.dto.toCoinDetail
import com.example.cryptocurrencyapp.domain.model.Coin
import com.example.cryptocurrencyapp.domain.model.CoinDetail
import com.example.cryptocurrencyapp.domain.repository.CoinRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class GetCoinUsecase @Inject constructor(
    private val repository: CoinRepository
) {
    operator fun invoke(id:String) : Flow<Resource<CoinDetail>>  = flow {
        try {
            emit(Resource.Loading<CoinDetail>())
            val coin  = repository.getCoinById(id).toCoinDetail()
            emit(Resource.Success<CoinDetail>(coin))
        } catch (e : IOException){
            emit(Resource.Error<CoinDetail>("Couldn't Reach Server, Check your internet"))
        } catch (e: Exception){
            emit(Resource.Error<CoinDetail>(e.localizedMessage ?: "Unexpected Error Occurred"))
        }
    }
}