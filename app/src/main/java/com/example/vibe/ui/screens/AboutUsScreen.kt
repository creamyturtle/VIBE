package com.example.vibe.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.vibe.R
import com.example.vibe.ui.components.ContentCard


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AboutUsScreen(
    onBack: () -> Unit
) {


        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {

            Spacer(Modifier.height(78.dp))

            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .padding(8.dp, 40.dp, 0.dp, 0.dp)
                    .background(color = MaterialTheme.colorScheme.surface, shape = CircleShape)
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outline,
                        shape = CircleShape
                    )
                    .size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.size(20.dp)
                )
            }

            ContentCard(
                title = stringResource(R.string.welcome_to_vibe_social),
                content = stringResource(R.string.vibe_is_a_unique_new_concept) +
                        "\n" +
                        stringResource(R.string.this_platform_was_designed)

            )

            ContentCard(
                title = stringResource(R.string.our_mission),
                content = stringResource(R.string.our_mission_is_simple_make_finding_parties_easier) +
                        stringResource(R.string.by_connecting_willing_hosts_with) +
                        "\n" +
                        stringResource(R.string.no_more_waiting)

            )

            ContentCard(
                title = stringResource(R.string.a_unique_new_experience),
                content = stringResource(R.string.the_vibe_platform_is_open_to) + stringResource(R.string.the_onus_is_on_party_hosts)

            )

            ContentCard(
                title = stringResource(R.string.secure_platform),
                content = stringResource(R.string.utilizing_instagram)

            )

            ContentCard(
                title = stringResource(R.string.completely_free),
                content = stringResource(R.string.there_are_no_charges)

            )

            ContentCard(
                title = stringResource(R.string.more_information),
                content = stringResource(R.string.for_more_information_please) + stringResource(R.string.also_check_out_our_terms)

            )






            Spacer(Modifier.height(148.dp))

        }



}


