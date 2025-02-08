package com.keyfall.mypokedex
import kotlinx.serialization.Serializable

@Serializable
data class PokemonClass(
  val id: Int,
  val name: String,
  val baseExperience: Int,
  val height: Double,
  val weight: Double,
  val types: List<String>,
  val moves: List<String>,
  val sprites: List<String>,
)