package com.keyfall.mypokedex
import kotlinx.serialization.Serializable

@Serializable
data class Pokemon(
  val id: Int,
  val name: String,
  val baseExperience: Int,
  val height: Double,
  val weight: Double,
  val types: List<String>,
  val moves: List<String>,
  val sprites: List<String>,
)

@Serializable
data class PokemonResponse(
  val name: String,
  val url: String,
)

@Serializable
data class PokemonApiPagination(
  val count: Int,
  val next: String?,
  val previous: String?,
  val results: List<PokemonResponse>
)