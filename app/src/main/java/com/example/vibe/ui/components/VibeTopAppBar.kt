package com.example.vibe.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.vibe.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VibeTopAppBar(navController: NavController) {
    TopAppBar(
        title = {
            Row(
                modifier = Modifier
                    //.background(Color.Red)
                    .fillMaxWidth()
                    .padding(0.dp, 4.dp, 0.dp, 8.dp),
                //.offset(x = (-16).dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {


                Column(
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.Start
                    //modifier = Modifier
                    //.background(Color.Green)
                ) {

                    FancyAnimatedButton(
                        onClick = {
                            navController.navigate("home_screen/all")
                        }
                    ) {
                        Image(
                            painter = painterResource(R.drawable.logo_white),
                            contentDescription = "VIBE Logo",
                            modifier = Modifier
                                .padding(0.dp, 12.dp, 0.dp, 0.dp)
                                //.offset(x = 8.dp)
                                //.align(Alignment.Top)
                                .width(143.dp) // Use the image's exact width
                                .height(57.dp) // Use the image's exact height
                        )
                    }

                    Spacer(modifier = Modifier.size(4.dp))

                    Text(
                        text = "Your official party connection",
                        fontStyle = FontStyle.Italic,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(8.dp, 0.dp)
                    )



                }


                Spacer(
                    Modifier
                        .size(24.dp)
                )


                Column(
                    //modifier = Modifier
                    //.background(Color.Blue),
                    //.fillMaxWidth(),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.End
                ) {

                    Row() {


                        Text(
                            text = "\uD83C\uDDE8\uD83C\uDDF4",
                            Modifier.padding(0.dp, 12.dp, 0.dp, 0.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = "\uD83C\uDDFA\uD83C\uDDF8",
                            Modifier.padding(0.dp, 12.dp, 0.dp, 0.dp)
                        )

                        Spacer(Modifier.weight(1f))


                        HamburgerMenuButton()



                    }




                    Spacer(Modifier.size(6.dp))

                    Row(
                        modifier = Modifier
                            //.background(Color.Red)
                            .fillMaxWidth()
                            .padding(16.dp, 0.dp, 16.dp, 0.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {

                        FancyAnimatedButton(
                            onClick = {
                                navController.navigate("login")
                            }
                        ) {

                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                //modifier = Modifier
                                //.background(Color.Green)
                            ) {


                                Icon(
                                    imageVector = Icons.Filled.Person,
                                    contentDescription = "Log In",
                                    modifier = Modifier.size(24.dp)
                                )

                                Text(
                                    text = "Log In",
                                    fontSize = 12.sp
                                )

                            }
                        }

                        Spacer(Modifier.width(16.dp))

                        FancyAnimatedButton(
                            onClick = {
                                navController.navigate("home_screen/all")
                            }
                        ) {


                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                //modifier = Modifier
                                //.background(Color.Green)
                            ) {

                                Icon(
                                    imageVector = Icons.Filled.Groups,
                                    contentDescription = "Host an Event",
                                    modifier = Modifier.size(24.dp)
                                )

                                Text(
                                    text = "Host Event",
                                    fontSize = 12.sp
                                )

                            }
                        }

                        Spacer(Modifier.width(12.dp))

                        FancyAnimatedButton(
                            onClick = {
                                navController.navigate("home_screen/all")
                            }
                        ) {

                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                //modifier = Modifier
                                //.background(Color.Green)
                            ) {

                                Icon(
                                    imageVector = Icons.Filled.Event,
                                    contentDescription = "Calendar",
                                    modifier = Modifier.size(24.dp)
                                )

                                Text(
                                    text = "Calendar",
                                    fontSize = 12.sp
                                )

                            }
                        }


                    }

                }


            }
        },
        modifier = Modifier
            .height(168.dp),
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFF333F57),
            titleContentColor = Color.White
        )
    )
}

