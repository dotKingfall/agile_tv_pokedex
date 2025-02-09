package com.keyfall.mypokedex.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.keyfall.mypokedex.R
import com.keyfall.mypokedex.mainRed
import com.keyfall.mypokedex.searchHintStyle
import com.keyfall.mypokedex.searchInputStyle

@Composable
fun FakeAppBar(){
  var searchInput by remember { mutableStateOf("") }
  var hideHintText by remember { mutableStateOf(true) }

  Box(
    modifier = Modifier
      .fillMaxWidth()
      .height(110.dp)
      .drawBehind { //Pokedex-like shape for the "appbar"
        drawPath(
          path = Path().apply {
            val width = size.width
            val height = size.height

            moveTo(0f, 0f)
            lineTo(0f, height)
            lineTo(width * 0.4f, height)
            lineTo(width * 0.5f, height * 0.55f)
            lineTo(width, height * 0.55f)
            lineTo(width, 0f)
            close()
          },
          color = mainRed,
          style = Fill,
        )
      }
  ){
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(end = 16.dp)
    ){
      Image(
        painter = painterResource(id = R.drawable.pokedex),
        contentDescription = "pokedex_logo",
        modifier = Modifier
          .padding(start = 30.dp, top = 35.dp)
          .fillMaxWidth(.35f)
          .aspectRatio(16f / 9f)
      )

      BasicTextField( //TODO SEARCH ALGORITHM
        value = searchInput,
        maxLines = 1,
        singleLine = true,
        textStyle = searchInputStyle,
        onValueChange = {
            userInput -> searchInput = userInput
        },
        modifier = Modifier
          .align(Alignment.BottomEnd)
          .fillMaxWidth(.47f)
          .height(45.dp)
          .padding(top = 10.dp, end = 15.dp)
          .onFocusChanged {
            hideHintText = if (searchInput.isEmpty()) !hideHintText else hideHintText
          }
          .drawBehind {
            if (hideHintText) {
              drawLine(
                color = mainRed,
                start = Offset(0f, size.height),
                end = Offset(size.width, size.height),
                strokeWidth = 3.dp.toPx()
              )
            }
          }
      )

      if(!hideHintText){
        Text(text = "Search...", style = searchHintStyle,
          modifier = Modifier
            .align(Alignment.BottomEnd)
            .padding(bottom = 8.dp, end = 55.dp)
        )
      }
    }
  }
}