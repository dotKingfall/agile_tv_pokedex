package com.keyfall.mypokedex

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MyPokedex()
    }
  }
}

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

      BasicTextField(
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

@Composable
fun MyPokedex(){
  val focusManager = LocalFocusManager.current

  MaterialTheme {
    Scaffold(
      modifier = Modifier
    ){ innerPadding ->
      Box( //BOX FOR BORDER
        modifier = Modifier
          .fillMaxSize()
          .border(
            BorderStroke(15.dp, mainRed),
            RoundedCornerShape(
              bottomStart = 50.dp,
              bottomEnd = 50.dp,
            ),
          )
      )

      Box{
        FakeAppBar()
        Box(
          modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(top = 60.dp, start = 15.dp, end = 15.dp)
            .pointerInput(Unit) {
              detectTapGestures(
                onTap = {
                  focusManager.clearFocus()
                },
                onPress = {
                  tryAwaitRelease()
                  focusManager.clearFocus()
                },
              )
            }
        ){
          /*TODO CALL ANOTHER FUNCTION TO GET THE LIST OF POKEMON*/
        }
      }
    }
  }
}

val searchInputStyle = TextStyle(
  fontSize = 24.sp
)

val searchHintStyle = TextStyle(
  fontSize = 24.sp,
  color = secondaryGrey
)