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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.vibe.R


@Composable
fun MapBottomAppBar(
    navController: NavController,
    onFilterSelected: (String) -> Unit
) {
    BottomAppBar(
        containerColor = Color(0xFF333F57),
        contentColor = Color.White,
        modifier = Modifier
            .height(148.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp, bottom = 4.dp)
                .offset(x = (-4).dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {

            FancyAnimatedButton(
                onClick = { onFilterSelected("House") }
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(
                        painter = painterResource(R.drawable.house),
                        contentDescription = "House Parties",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.size(50.dp).clip(CircleShape)
                    )
                    Text(text = "House Parties", fontSize = 12.sp, modifier = Modifier.padding(top = 4.dp))
                }
            }
            Spacer(Modifier.width(0.dp))

            FancyAnimatedButton(
                onClick = { onFilterSelected("Finca") }
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(
                        painter = painterResource(R.drawable.finca),
                        contentDescription = "Finca Parties",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.size(50.dp).clip(CircleShape)
                    )
                    Text(text = "Finca Parties", fontSize = 12.sp, modifier = Modifier.padding(top = 4.dp))
                }
            }

            Spacer(Modifier.width(0.dp))

            FancyAnimatedButton(
                onClick = { onFilterSelected("Pool") }
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(
                        painter = painterResource(R.drawable.pool),
                        contentDescription = "Pool Parties",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.size(50.dp).clip(CircleShape)
                    )
                    Text(text = "Pool Parties", fontSize = 12.sp, modifier = Modifier.padding(top = 4.dp))
                }
            }

            Spacer(Modifier.width(8.dp))

            FancyAnimatedButton(
                onClick = { onFilterSelected("Activity") }
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(
                        painter = painterResource(R.drawable.activities),
                        contentDescription = "Activities",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.size(50.dp).clip(CircleShape)
                    )
                    Text(text = "Activities", fontSize = 12.sp, modifier = Modifier.padding(top = 4.dp))
                }
            }
        }
    }
}
