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
fun TermsAndConditionsScreen(
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
            title = stringResource(R.string.vibe_terms_and_conditions),
            content = stringResource(R.string.welcome_to_vibe_these_terms)
        )

        ContentCard(
            title = stringResource(R.string._1_user_accounts),
            content = stringResource(R.string.to_access_certain_features)
        )

        ContentCard(
            title = stringResource(R.string._2_user_conduct),
            content = stringResource(R.string.we_use_the_information_we_collect) +
                    "\n" +
                    stringResource(R.string.account_registration_and_management) +
                    "\n" +
                    stringResource(R.string.communications_we_may_use) +
                    "\n" +
                    stringResource(R.string.improving_our_service)
        )

        ContentCard(
            title = stringResource(R.string._3_user_content),
            content = stringResource(R.string.by_submitting_content)
        )

        ContentCard(
            title = stringResource(R.string._4_privacy),
            content = stringResource(R.string.by_using_the_service)
        )

        ContentCard(
            title = stringResource(R.string._5_limitation_of_liability),
            content = stringResource(R.string.to_the_fullest_extent_permitted)
        )

        ContentCard(
            title = stringResource(R.string._6_disclaimer),
            content = stringResource(R.string.our_service_is_provided)
        )

        ContentCard(
            title = stringResource(R.string._7_termination),
            content = stringResource(R.string.we_reserve_the_right)
        )

        ContentCard(
            title = stringResource(R.string._8_governing_law),
            content = stringResource(R.string.these_terms_are_governed_by)
        )

        ContentCard(
            title = stringResource(R.string._9_changes_to_these_terms),
            content = stringResource(R.string.we_may_update_these_terms)
        )








        Spacer(Modifier.height(148.dp))

    }



}

