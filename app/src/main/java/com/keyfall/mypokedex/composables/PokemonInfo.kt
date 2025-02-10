package com.keyfall.mypokedex.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.keyfall.mypokedex.Pokemon
import com.keyfall.mypokedex.alternateYellow

@Composable
fun ExpandedPokemon(pokeUrl: String){
  Box(
    modifier = Modifier
      .fillMaxSize()
  ){
    Text(pokeUrl)
    //TODO ALL THIS
  }
}

//poke: Pokemon