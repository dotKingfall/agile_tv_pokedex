package com.keyfall.mypokedex.composables
import android.util.Log
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.keyfall.mypokedex.PokemonApiPagination
import com.keyfall.mypokedex.PokemonResponse
import com.keyfall.mypokedex.R
import com.keyfall.mypokedex.RetroInstance
import com.keyfall.mypokedex.mainRed
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.text.contains

val apiCallTextStyle = TextStyle(
  color = mainRed,
  fontSize = 24.sp
)

const val pokemonLimit = 3000
const val limitPerPage = 20

@Composable
fun Pokelist(
  searchInput: String,
  selectedPokemon: String?,
  changeSelectedPokemonState: (String?) -> Unit
){
  val focusManager = LocalFocusManager.current

  var pokemonList by remember { mutableStateOf(listOf<PokemonResponse>()) }
  var paginatedPokemonList by remember { mutableStateOf(listOf<List<PokemonResponse>>()) }
  var isLoadingPokemonList by remember { mutableStateOf(false) }
  var isLoadingSuccessful by remember { mutableStateOf(true) }

  //PAGINATION
  var currentPage by remember { mutableIntStateOf(1) }
  var pokeApiPagination by remember { mutableStateOf<PokemonApiPagination?>(null) }
  val pokelistState = rememberLazyListState()

  LaunchedEffect(key1 = Unit) {
    isLoadingPokemonList = true
    withContext(Dispatchers.IO){
      try{
        val pokeapiResponse = RetroInstance.pokeApiService.getPokemonList(pokemonLimit, 0)
        pokeApiPagination = pokeapiResponse
        pokemonList = pokeapiResponse.results
        paginatedPokemonList = pokemonList.chunked(limitPerPage)
      }
      catch(e: Exception){
        Log.e("ErrorLoadingAPIData", e.message.toString())
        isLoadingSuccessful = false
      }
      finally { isLoadingPokemonList = false }
    }
  }

  Column(
    modifier = Modifier
      .pointerInput(Unit){
        detectDragGestures { _, _ ->
          focusManager.clearFocus()
        }
      }
  ) {
    if(isLoadingPokemonList){
      LoadingScreen()
    }
    else if(!isLoadingSuccessful){
      ErrorScreen()
    }
    else if (selectedPokemon != null) {
      ExpandedPokemon(selectedPokemon)
    }
    else{
      //POKELIST AND FILTERS
      val filteredList = remember(searchInput, paginatedPokemonList, currentPage) {
        if (searchInput.isEmpty()) {
          if (paginatedPokemonList.isNotEmpty()) {
            paginatedPokemonList[currentPage - 1]
          } else {
            emptyList()
          }
        } else {
          pokemonList.filter { it.name.contains(searchInput, ignoreCase = true) } // Filter the entire list
        }
      }

      Column{
        if(filteredList.isNotEmpty()){
          LazyColumn( //SHOW LIST OF POKEMON
            state = pokelistState,
            contentPadding = PaddingValues(25.dp),
            modifier = Modifier
              .fillMaxWidth()
              .fillMaxHeight(
                if (searchInput.isEmpty()) {
                  .92f
                } else {
                  1f
                }
              )
          ){
            items(filteredList){ pokemon ->
              Pokecard(
                pokemon.name,
                pokemon.url,
                pokeClick = { pokemonUrl ->
                  changeSelectedPokemonState(pokemonUrl)
                }
              )
            }
          }
        }
        else{
          NotFoundScreen()
        }

        Box( //PAGINATION BUTTONS
          modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
        ){
          if(searchInput.isEmpty()){
            PagesRow(
              pokemonList.size,
              changeCurrentPage = { newPage ->
                currentPage = newPage
              }
            )
          }
        }
      }
    }
  }
}

fun getId(url: String): String{
  return url.split("/")[6]
}

@Composable
fun Pokecard(
  name: String,
  url: String,
  pokeClick: (String) -> Unit
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
      .clickable {
        pokeClick(url)
      }

  )
}

@Composable
fun PagesRow(
  pokelistSize: Int,
  changeCurrentPage: (Int) -> Unit
){
  val limit = (pokelistSize / limitPerPage)
  val pages = List(limit){it + 1}

  LazyRow(
    horizontalArrangement = Arrangement.spacedBy(15.dp),
    modifier = Modifier
      .fillMaxWidth()
      .padding(top = 15.dp)
      .padding(horizontal = 50.dp)
  ){
    items(pages){page ->
      Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
          .fillMaxHeight()
          .drawBehind {
            drawRoundRect(
              color = mainRed,
              cornerRadius = CornerRadius(17.dp.toPx()),
              style = Fill
            )
          }
          .clip(RoundedCornerShape(17.dp))
          .clickable {
            changeCurrentPage(page)
          }
      ){
        Text(
          page.toString().padStart(2, '0'),
          style = apiCallTextStyle, color = Color.White,
          modifier = Modifier
            .padding(5.dp)
        )
      }
    }
  }
}

//STATES
@Composable
fun LoadingScreen(){
  val endlessLoop = rememberInfiniteTransition(label = "infinite transition")
  val pokeballLoop by endlessLoop.animateFloat(
    initialValue = 0f,
    targetValue = 360f,
    animationSpec = infiniteRepeatable(
      animation = tween(durationMillis = 2000, easing = LinearEasing)
    ), label = "rotation"
  )

  Box(
    contentAlignment = Alignment.Center,
    modifier = Modifier
      .fillMaxSize()
  ){
    Column(
      horizontalAlignment = Alignment.CenterHorizontally
    ){
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

@Composable
fun ErrorScreen(){
  Box(
    contentAlignment = Alignment.Center,
    modifier = Modifier
      .fillMaxSize()
  ){
    Column(
      horizontalAlignment = Alignment.CenterHorizontally
    ){
      Image(
        painter = painterResource(id = R.drawable.error),
        contentDescription = "error_image",
        modifier = Modifier
          .width(80.dp)
          .height(80.dp)
          .padding(bottom = 8.dp)
      )
      Text(
        "Who's this poke-Error?",
        style = apiCallTextStyle,
        textAlign = TextAlign.Center
      )
      Text(
        "(Something wrong has happened, please relaunch the app)",
        style = TextStyle(mainRed, fontSize = 15.sp),
        textAlign = TextAlign.Center
      )
    }
  }
}

@Composable
fun NotFoundScreen(){
  Box(
    contentAlignment = Alignment.Center,
    modifier = Modifier
      .fillMaxSize()
  ){
    Column(
      horizontalAlignment = Alignment.CenterHorizontally
    ){
      Image(
        painter = painterResource(id = R.drawable.error),
        contentDescription = "error_image",
        modifier = Modifier
          .width(80.dp)
          .height(80.dp)
          .padding(bottom = 8.dp)
      )
      Text(
        "Who's this Pokémon?",
        style = apiCallTextStyle,
        textAlign = TextAlign.Center
      )
      Text(
        "(No, seriously, we don't have that one)",
        style = TextStyle(mainRed, fontSize = 15.sp),
        textAlign = TextAlign.Center
      )
    }
  }
}