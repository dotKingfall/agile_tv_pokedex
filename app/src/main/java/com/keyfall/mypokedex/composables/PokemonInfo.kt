package com.keyfall.mypokedex.composables
import android.graphics.Bitmap
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.keyfall.mypokedex.Pokemon
import com.keyfall.mypokedex.R
import com.keyfall.mypokedex.RetroInstance
import com.keyfall.mypokedex.mainRed
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
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

  //PRE-LOAD POKEMON IMAGE
  val context = LocalContext.current
  val imageLoader = remember { ImageLoader(context) }
  val secondImage = poke?.sprites?.backDefault
  var secondImageBitmap by remember { mutableStateOf<Bitmap?>(null) }

  LaunchedEffect(key1 = secondImage) {
    val request = ImageRequest.Builder(context)
      .data(secondImage)
      .build()

    val result = imageLoader.execute(request)
    if (result is SuccessResult) {
      secondImageBitmap = result.drawable.toBitmap()
    }
  }

  Box(
    modifier = Modifier
      .fillMaxSize()
  ){
    if(isLoadingPokemonInfo){ LoadingScreen() }
    else if(!isLoadingSuccessful){ ErrorScreen() }
    else{
      var currentImageIndex by remember { mutableIntStateOf(0) }

      Column(
        modifier = Modifier
          .fillMaxSize()
      ){
        LaunchedEffect(key1 = true) { //CHANGE IMAGE EVERY 2 SECONDS
          while (true) {
            delay(1500)
            currentImageIndex = if(currentImageIndex == 1) 2 else 1
          }
        }

        Box(
          contentAlignment = Alignment.Center,
          modifier = Modifier
            .fillMaxWidth()
        ){
          if(secondImageBitmap != null && currentImageIndex == 2){
            Image(
              bitmap = secondImageBitmap!!.asImageBitmap(),
              contentDescription = "Pokemon Image Back",
              contentScale = ContentScale.Crop,
              modifier = Modifier.size(200.dp)
            )
          }
          else{
            AsyncImage( //POKEMON FRONT AND BACK IMAGES
              model = poke?.sprites?.frontDefault,
              contentDescription = "Pokemon Image Front",
              contentScale = ContentScale.Crop,
              placeholder = painterResource(id = R.drawable.pokeball),
              modifier = Modifier.size(200.dp)
            )
          }
        }
        //NAME AND ID
        RowText("ID: ", "${poke?.id}")
        RowText("Name: ", "${poke?.name}")

        //WEIGHT AND HEIGHT
        val heightToMeters = poke?.height?.toFloat()?.div(10)
        val weightToMeters = poke?.weight?.toFloat()?.div(10)
        RowText("Height: ", "${heightToMeters}m")
        RowText("Weight: ", "$weightToMeters Kg")

        Divider(
          color = mainRed,
          modifier = Modifier
            .height(15.dp)
            .padding(horizontal = 15.dp, vertical = 6.dp)
        )

      }
    }
  }
}

@Composable
fun RowText(field: String, value: String){
  val fieldStyle = TextStyle(
    color = mainRed,
    fontWeight = FontWeight.Bold,
    fontSize = 20.sp
  )
  val valueStyle = TextStyle(
    fontSize = 18.sp
  )

  Row(
    horizontalArrangement = Arrangement.SpaceBetween,
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 15.dp, vertical = 5.dp)
  ){
    Text(field, style = fieldStyle)
    Text(value, style = valueStyle)
  }
}