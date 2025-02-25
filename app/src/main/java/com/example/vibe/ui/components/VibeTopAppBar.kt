package com.example.vibe.ui.components

import androidx.compose.animation.core.animateDpAsState
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
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.vibe.R
import org.intellij.lang.annotations.Language


@Composable
fun VibeTopAppBar(
    navController: NavController,
    isDrawerOpen: MutableState<Boolean>,
    listState: LazyListState,
    selectedLanguage: String
) {

    val density = LocalDensity.current.density
    val onePixel = 1f / density

    val isExpanded by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex == 0 && listState.firstVisibleItemScrollOffset < 150 // Adjust this threshold
        }
    }

    val height by animateDpAsState(if (isExpanded) 164.dp else 104.dp, label = "topbar_height")

    var selectedFilter by rememberSaveable { mutableStateOf("all") }

    //val selectedLanguage by languageViewModel.language.collectAsState()


    key(selectedLanguage) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(height)
                .background(MaterialTheme.colorScheme.background)
                .padding(
                    top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
                ) // ✅ Adds padding for status bar
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
                                if (selectedFilter != "all") {
                                    selectedFilter = "all"
                                    navController.navigate("home_screen/all") {
                                        popUpTo("home_screen") { inclusive = false }
                                    }
                                }
                            }
                        ) {
                            Text(
                                text = "VIBE",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 28.sp,
                                fontStyle = FontStyle.Italic,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(24.dp, 0.dp, 0.dp, 0.dp)
                            )
                        }


                        Text(
                            text = stringResource(R.string.your_official_party_connection),
                            fontStyle = FontStyle.Italic,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurface,
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


                if (isExpanded) {

                    MiniTopBar(
                        navController,
                        selectedFilter


                    ) { filterType ->
                        if (filterType != selectedFilter) {
                            selectedFilter = filterType

                            navController.navigate("home_screen/$filterType") {
                                popUpTo("home_screen") { inclusive = false }
                            }

                        }
                    }

                    Spacer(Modifier.weight(0.5f)) // ✅ Ensures separator is at the bottom

                }


                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(onePixel.dp)
                        .background(MaterialTheme.colorScheme.surfaceVariant) // ✅ Light grey separator now aligned
                )
            }

        }
    }


}


