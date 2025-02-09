package com.keyfall.mypokedex

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query

//Make requests to PokeApi, although we'll only GET data from it
interface PokeApiService{
  @GET("pokemon")
  suspend fun getPokemonList(
    @Query("limit") limit: Int,
    @Query("offset") offset: Int
  ): PokemonApiPagination
}

//Retrofit instance to make API calls for pokemon list data
object RetroInstance {
  private const val BASE_URL = "https://pokeapi.co/api/v2/"
  private val json = Json {ignoreUnknownKeys = true}

  private val retrofit: Retrofit by lazy{
    Retrofit.Builder()
      .baseUrl(BASE_URL)
      .addConverterFactory(
        json.asConverterFactory("application/json".toMediaType())
      ).build()
  }

  val pokeApiService: PokeApiService by lazy {
    retrofit.create(PokeApiService::class.java)
  }
}