package com.keyfall.mypokedex.composables
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.keyfall.mypokedex.Pokemon
import com.keyfall.mypokedex.RetroInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun ExpandedPokemon(pokeUrl: String){
  var poke by remember { mutableStateOf<Pokemon?>(null) }
  var isLoadingPokemonInfo by remember { mutableStateOf(false) }
  var isLoadingSuccessful by remember { mutableStateOf(true) }

  LaunchedEffect(key1 = Unit) {
    isLoadingPokemonInfo = true
    withContext(Dispatchers.IO){
      try{
        val pokeapiResponse = RetroInstance.pokeApiService.getSpecificPokemon(pokeUrl)
        poke = pokeapiResponse
      }
      catch(e: Exception){
        Log.e("ErrorLoadingAPIData", e.message.toString())
        isLoadingSuccessful = false
      }
      finally { isLoadingPokemonInfo = false }
    }
  }

  Box(
    modifier = Modifier
      .fillMaxSize()
  ){
    if(isLoadingPokemonInfo){ LoadingScreen() }
    else if(!isLoadingSuccessful){ ErrorScreen() }
    else{
      Text(poke.toString())
    }
  }
}

//poke: Pokemon