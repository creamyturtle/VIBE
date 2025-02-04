package com.example.vibe.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController


@Composable
fun ReservationsBottomBar(navController: NavController) {

    val density = LocalDensity.current.density
    val onePixel = 1f / density

    Column {
        // Separator bar (1px light grey)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(onePixel.dp)
                .background(Color.LightGray) // Light grey separator
        )

        BottomAppBar(
            modifier = Modifier
                //.offset(y = offsetY)
                .height(128.dp),
            containerColor = Color(0xFFFCFDF6),
            contentColor = Color.DarkGray
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp, bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {

                Column(Modifier.padding(start = 16.dp)) {


                    Text(
                        text = "Request entry to event",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    Text(text = "Bring up to 4 friends")


                }



                Spacer(Modifier.weight(1f))


                FancyAnimatedButton(
                    onClick = {
                        navController.navigate("home_screen/Finca") {
                            popUpTo("home_screen/all") {
                                inclusive = true
                            } // Reset navigation stack
                        }
                    }
                ) {


                    Button(
                        onClick = {
                            navController.navigate("home_screen/Finca") {
                                popUpTo("home_screen/all") {
                                    inclusive = true
                                } // Reset navigation stack
                            }
                        },
                        enabled = true,
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFFE1943),
                            contentColor = Color.White,
                            disabledContainerColor = Color(0xFFBDBDBD),
                            disabledContentColor = Color.White
                        ),
                        modifier = Modifier
                            .width(168.dp)
                            .height(50.dp)
                            .padding(end = 16.dp)
                    ) {

                        Text(
                            text = "Reserve",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )

                    }


                }


            }


        }
    }
}
