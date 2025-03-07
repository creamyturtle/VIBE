package com.example.vibe.ui.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.vibe.R
import com.example.vibe.model.Event
import com.example.vibe.model.getPartyTypeText


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EventDetailsCard(event: Event, onClose: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .background(MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(16.dp))
            //.shadow(8.dp)
            .padding(16.dp, 4.dp, 16.dp, 16.dp)

    ) {
        Column {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = event.partyname, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.secondaryContainer, fontSize = 18.sp)
                IconButton(onClick = onClose) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                }
            }


            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {


                AsyncImage(
                    model = event.fullImgSrc,
                    contentDescription = event.partyname,
                    modifier = Modifier
                        //.height(200.dp)
                        .size(120.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(R.drawable.loading_img),
                    error = painterResource(R.drawable.defaultimg)
                )

                Spacer(Modifier.width(12.dp))


                Column {
                    Text(
                        text = "📍 ${event.location}",
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        style = TextStyle(
                            textAlign = TextAlign.Start,
                            lineHeight = 20.sp,
                            textIndent = TextIndent(firstLine = 0.sp, restLine = 22.sp)
                        )
                    )

                    Spacer(Modifier.height(8.dp))

                    Text(
                        text = "🎉 ${getPartyTypeText(event.partytype)}",
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        style = TextStyle(
                            textAlign = TextAlign.Start,
                            lineHeight = 20.sp,
                            textIndent = TextIndent(firstLine = 0.sp, restLine = 22.sp)
                        )
                    )

                    Spacer(Modifier.height(8.dp))

                    Text(
                        text = "📅 ${event.formattedDate}",
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        style = TextStyle(
                            textAlign = TextAlign.Start,
                            lineHeight = 20.sp,
                            textIndent = TextIndent(firstLine = 0.sp, restLine = 22.sp)
                        )
                    )
                }


            }



        }
    }
}
