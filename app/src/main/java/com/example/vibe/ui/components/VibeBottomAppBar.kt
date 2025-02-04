package com.example.vibe.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.vibe.R


@Composable
fun VibeBottomAppBar(navController: NavController, offsetY: Dp) {

    BottomAppBar (
        modifier = Modifier
            .offset(y = offsetY)
            .height(148.dp),
        containerColor = Color(0xFF333F57), // Background color for the BottomAppBar
        contentColor = Color.White         // Default content (icon/text) color
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp, bottom = 4.dp)
                .offset(x = (-4).dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {

            FancyAnimatedButton(
                onClick = {

                    navController.navigate("home_screen/House") {
                        popUpTo("home_screen/all") { inclusive = true } // Reset navigation stack
                    }

                }
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(
                        painter = painterResource(R.drawable.house),
                        contentDescription = "House Parties",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                    )
                    Text(
                        text = "House Parties",
                        fontSize = 12.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }

            Spacer(Modifier.width(0.dp))

            FancyAnimatedButton(
                onClick = {
                    navController.navigate("home_screen/Finca") {
                        popUpTo("home_screen/all") { inclusive = true } // Reset navigation stack
                    }
                }
            ) {

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Image(
                        painter = painterResource(R.drawable.finca),
                        contentDescription = "Finca Parties",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                    )

                    Text(
                        text = "Finca Parties",
                        fontSize = 12.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )

                }

            }

            Spacer(Modifier.width(0.dp))

            FancyAnimatedButton(
                onClick = {

                    navController.navigate("home_screen/Pool") {
                        popUpTo("home_screen/all") { inclusive = true } // Reset navigation stack
                    }

                }
            ) {

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Image(
                        painter = painterResource(R.drawable.pool),
                        contentDescription = "Pool Parties",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                    )

                    Text(
                        text = "Pool Parties",
                        fontSize = 12.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )

                }

            }

            Spacer(Modifier.width(8.dp))

            FancyAnimatedButton(
                onClick = {

                    navController.navigate("home_screen/Activity") {
                        popUpTo("home_screen/all") { inclusive = true } // Reset navigation stack
                    }

                }
            ) {

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Image(
                        painter = painterResource(R.drawable.activities),
                        contentDescription = "Activities",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                    )

                    Text(
                        text = "Activities",
                        fontSize = 12.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )

                }
            }


        }


    }
}
