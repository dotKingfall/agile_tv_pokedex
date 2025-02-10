package com.keyfall.mypokedex.composables
import android.graphics.Bitmap
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Color
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

        HorizontalDivider(
          color = mainRed,
          modifier = Modifier
            .height(15.dp)
            .padding(horizontal = 15.dp, vertical = 6.dp)
        )

        LazyColumn {
          items(listOf(0)){ _ ->
            ExpandableSection(
              title = "Types",
              content = {
                poke?.types?.mapIndexed{ index, type ->
                  RowText("Type $index:", type.type?.name.toString())
                }
              })

            ExpandableSection(
              title = "Abilities",
              content = {
                poke?.abilities?.mapIndexed{ index, ability ->
                  RowText("Ability $index:", ability.ability?.name.toString())
                }
              })

            ExpandableSection(
              title = "Moves",
              content = {
                poke?.moves?.mapIndexed{ index, move ->
                  RowText("Move $index:", move.move?.name.toString())
                }
              })
          }
        }
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

@Composable
fun ExpandableSection(
  title: String,
  content: @Composable () -> Unit
){
  var isExpanded by remember { mutableStateOf(false) }

  AnimatedVisibility(visible = isExpanded) {
    Column(
      horizontalAlignment = Alignment.CenterHorizontally, //TODO CHECK IF RIGHT
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
    ) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        content = {
          Text(
            text = title,
            style = TextStyle(
              color = mainRed,
              fontSize = 24.sp,
              fontWeight = FontWeight.Bold
            ),
            modifier = Modifier
              .padding(8.dp)
          )
          Icon(
            imageVector = Icons.Default.KeyboardArrowUp,
            contentDescription = "Collapse View Icon",
            tint = mainRed
          )
        },
        modifier = Modifier
          .fillMaxWidth()
          .clickable { isExpanded = !isExpanded }
      )
      content()
    }
  }
  if(!isExpanded){
    Box(
      contentAlignment = Alignment.Center,
      modifier = Modifier
        .fillMaxWidth()
        .clickable { isExpanded = !isExpanded }
    ){
      Row(
        verticalAlignment = Alignment.CenterVertically,
        content = {
          Text(
            text = title,
            style = TextStyle(
              color = mainRed,
              fontSize = 24.sp,
              fontWeight = FontWeight.Bold
            ),
            modifier = Modifier
              .padding(8.dp)
          )
          Icon(
            imageVector = Icons.Default.KeyboardArrowDown,
            contentDescription = "Expand View Icon",
            tint = mainRed
          )
        }
      )
    }
  }
}