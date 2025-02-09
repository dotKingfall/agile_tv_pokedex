package com.keyfall.mypokedex

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.keyfall.mypokedex.composables.FakeAppBar
import com.keyfall.mypokedex.composables.Pokelist

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
        Box( // remove focus from textfield if tapped outside
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
          Pokelist()
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