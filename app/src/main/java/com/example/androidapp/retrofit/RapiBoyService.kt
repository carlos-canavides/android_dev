package com.example.androidapp.retrofit

import retrofit2.Response
import retrofit2.http.GET

interface RapiBoyService {
    @GET("v1/LastMile/Vehiculos")
    suspend fun getListItem(): Response<List<ItemListRapiBoy>>
}