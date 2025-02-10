package com.keyfall.mypokedex
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Pokemon(
  val id: Int,
  val name: String,
  @SerialName("base_experience") val baseExperience: Int,
  val height: Double,
  val weight: Double,
  val abilities: List<AbilityResponse>,
  val moves: List<MoveResponse>,
  val types: List<TypeResponse>,
  val sprites: Sprites,
)

@Serializable
data class Sprites(
  @SerialName("back_default") val backDefault: String? = null,
  @SerialName("front_default") val frontDefault: String? = null,
)

@Serializable
data class Ability(
  val name: String? = null,
  val url: String? = null,
)

@Serializable
data class AbilityResponse(
  val ability: Ability? = null,
  @SerialName("is_hidden") val isHidden: Boolean? = null,
  val slot: Int? = null,
)

@Serializable
data class Move(
  val name: String? = null,
  val url: String? = null,
)

@Serializable
data class MoveResponse(
  val move: Move? = null,
)

@Serializable
data class Type(
  val name: String? = null,
  val url: String? = null,
)

@Serializable
data class TypeResponse(
  val slot: Int? = null,
  val type: Type? = null,
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