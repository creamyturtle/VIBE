package com.example.vibe.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController



@Composable
fun VibeTopAppBar(navController: NavController, isDrawerOpen: MutableState<Boolean>) {

    val density = LocalDensity.current.density
    val onePixel = 1f / density



    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(104.dp)
            .background(Color.White)
            .padding(top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()) // ✅ Adds padding for status bar
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    //.background(Color.Green)
                    .padding(0.dp, 16.dp, 16.dp, 0.dp),
                verticalAlignment = Alignment.Top
            ) {
                Column(
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.Start
                ) {
                    FancyAnimatedButton(
                        onClick = {
                            navController.navigate("home_screen/all")
                        }
                    ) {

                        Text(
                            text = "VIBE",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 28.sp,
                            fontStyle = FontStyle.Italic,
                            color = Color(0xFFFE1943),
                            modifier = Modifier.padding(24.dp, 0.dp, 0.dp, 0.dp)
                        )

                    }

                    Text(
                        text = "Your official party connection",
                        fontStyle = FontStyle.Italic,
                        fontSize = 12.sp,
                        style = TextStyle(fontSize = 12.sp, lineHeight = 12.sp),
                        modifier = Modifier
                            //.background(Color.Green)
                            .padding(24.dp, 2.dp, 0.dp, 0.dp)
                    )
                }

                Spacer(Modifier.weight(1f))

                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                        //.background(Color.Blue),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.End
                ) {
                    IconButton(
                        onClick = { isDrawerOpen.value = true }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Menu Button",
                            tint = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier
                                .size(36.dp)
                        )
                    }
                }
            }

            Spacer(Modifier.weight(1f)) // ✅ Ensures separator is at the bottom

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(onePixel.dp)
                    .background(Color.LightGray) // ✅ Light grey separator now aligned
            )
        }



    }



}


