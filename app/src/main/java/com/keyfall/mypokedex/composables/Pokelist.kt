package com.keyfall.mypokedex.composables
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.keyfall.mypokedex.mainRed

val pokemonResponse: List<@Composable () -> Unit> = mutableListOf()
val pageLimit = 30
val currentPage = 1

@Composable
fun Pokelist(){
  Column {
    Column(){
      Pokecard(0, "aaaaaaaaaaaaaaaaaaaaaaaaaaaa")
      Pokecard(1, "bbbbbbbbbbbbbbbbbbbbbbbbbbbb")
      Pokecard(2, "cccccccccccccccccccccccccccc")
      Pokecard(3, "dddddddddddddddddddddddddddd")
      Pokecard(4, "eeeeeeeeeeeeeeeeeeeeeeeeeeee")
      Pokecard(5, "ffffffffffffffffffffffffffff")
      Pokecard(66, "gggggggggggggggggggggggggggg")
      Pokecard(777, "hhhhhhhhhhhhhhhhhhhhhhhhhhhh")
      Pokecard(8888, "iiiiiiiiiiiiiiiiiiiiiiiiiiii")
      Pokecard(99999, "jjjjjjjjjjjjjjjjjjjjjjjjjjjj")
      Pokecard(10, "kkkkkkkkkkkkkkkkkkkkkkkkkkkk")
      Pokecard(11, "llllllllllllllllllllllllllll")
      Pokecard(12, "mmmmmmmmmmmmmmmmmmmmmmmmmmmm")
    }

    //TODO NEXT AND PREVIOUS BUTTONS
  }
}

@Composable
fun Pokecard(
  id: Int,
  name: String,
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
            text = id.toString(),
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