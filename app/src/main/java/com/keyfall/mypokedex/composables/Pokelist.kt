package com.keyfall.mypokedex.composables
import android.util.Log
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.keyfall.mypokedex.Pokemon
import com.keyfall.mypokedex.PokemonResponse
import com.keyfall.mypokedex.R
import com.keyfall.mypokedex.RetroInstance
import com.keyfall.mypokedex.mainRed
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

val apiCallTextStyle = TextStyle(
  color = mainRed,
  fontSize = 24.sp
)

@Composable
fun Pokelist(){
  var pokemonList by remember { mutableStateOf(listOf<PokemonResponse>()) }
  var isLoadingPokemonList by remember { mutableStateOf(false) }

  val endlessLoop = rememberInfiniteTransition(label = "infinite transition")
  val pokeballLoop by endlessLoop.animateFloat(
    initialValue = 0f,
    targetValue = 360f,
    animationSpec = infiniteRepeatable(
      animation = tween(durationMillis = 2000, easing = LinearEasing)
    ), label = "rotation"
  )

  LaunchedEffect(key1 = Unit) {
    isLoadingPokemonList = true
    withContext(Dispatchers.IO){
      try{
        val pokeapiResponse = RetroInstance.pokeApiService.getPokemonList(20, 0)
        pokemonList = pokeapiResponse.results
      }
      catch(e: Exception){ Log.e("ErrorLoadingAPIData", e.message.toString()) }
      finally { isLoadingPokemonList = false }
    }
  }

  Column { //TODO MAKE COLUMN SCROLLABLE
    if(isLoadingPokemonList){
      Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
          .fillMaxSize()
      ){
        Column{
          Image(
            painter = painterResource(id = R.drawable.pokeball),
            contentDescription = "pokeball",
            modifier = Modifier
              .width(80.dp)
              .height(80.dp)
              .padding(bottom = 8.dp)
              .rotate(pokeballLoop)
          )
          Text(
            "Loading...",
            style = apiCallTextStyle,
            textAlign = TextAlign.Center
          )
        }
      }
    }
    else{
      LazyColumn(
        contentPadding = PaddingValues(16.dp),
        modifier = Modifier
          .fillMaxSize()
      ){
        items(pokemonList){ pokemon ->
          Pokecard(pokemon.name, pokemon.url)
        }
      }
    }

    //TODO NEXT AND PREVIOUS BUTTONS
  }
}

fun getId(url: String): String{
  return url.split("/")[6]
}

@Composable //TODO ONCLICK
fun Pokecard(
  name: String,
  url: String
){
  Card(
    content = {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
          .fillMaxSize()
          .drawBehind {
            drawPath(
              path = Path().apply {
                val width = size.width
                val height = size.height

                moveTo(0f, 0f)
                lineTo(0f, height)
                lineTo(width * .08f, height)
                lineTo(width * .08f, 0f)
                close()
              },
              color = mainRed,
              style = Fill
            )
          }
      ){
        Box(
          contentAlignment = Alignment.Center,
          modifier = Modifier
            .fillMaxHeight()
            .background(mainRed)
        ){
          Text(
            text = getId(url),
            textAlign = TextAlign.Center,
            style = TextStyle(
              fontSize = 35.sp,
              color = Color.White
            ),
            modifier = Modifier
              .padding(start = 15.dp, end = 10.dp)
              .background(mainRed)
          )
        }

        //NAME OF THE POKEMON
        Text(
          text = name.uppercase(),
          style = TextStyle(fontSize = 20.sp),
          maxLines = 2,
          overflow = TextOverflow.Ellipsis,
          modifier = Modifier
            .padding(start = 10.dp, end = 10.dp)
        )
      }
    },
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 10.dp)
      .padding(top = 15.dp)
      .height(55.dp)
      .shadow(
        elevation = 10.dp,
        shape = RoundedCornerShape(8.dp),
        ambientColor = Color.Gray,
        spotColor = Color.Gray,
      )

  )
}