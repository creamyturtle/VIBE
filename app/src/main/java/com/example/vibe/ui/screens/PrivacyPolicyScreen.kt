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
import com.example.vibe.ui.components.SectionTitle


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PrivacyPolicyScreen(
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

        Spacer(Modifier.height(16.dp))

        SectionTitle(title = stringResource(R.string.privacy_policy))

        ContentCard(
            title = stringResource(R.string._1_information_we_collect),
            content = stringResource(R.string.we_may_collect_the_following) +
                    "\n" +
                    stringResource(R.string.email_address) +
                    stringResource(R.string.name3) +
                    stringResource(R.string.instagram_username2) +
                    stringResource(R.string.age3) +
                    stringResource(R.string.gender3) +
                    " \n" +
                    stringResource(R.string.whatsapp_number) +
                    stringResource(R.string.facebook_username)
        )

        ContentCard(
            title = stringResource(R.string._2_how_we_use_your_information),
            content = stringResource(R.string.we_use_the_information_we_collect_for) +
                    "\n" +
                    stringResource(R.string.account_registration_and_) +
                    "\n" +
                    stringResource(R.string.communications_we_may_use_your_) +
                    "\n" +
                    stringResource(R.string.improving_our_service_we_may)
        )

        ContentCard(
            title = stringResource(R.string._3_data_security),
            content = stringResource(R.string.your_personal_data_is_stored) +
                    "\n" +
                    stringResource(R.string.the_vibe_app_may_ask) +
                    "\n" +
                    stringResource(R.string.the_vibe_app_may_ask_you_for)
        )

        ContentCard(
            title = stringResource(R.string._4_data_retention),
            content = stringResource(R.string.we_retain_your_personal_information)
        )

        ContentCard(
            title = stringResource(R.string._5_sharing_your_information),
            content = stringResource(R.string.we_do_not_sell_trade) +
                    "\n" +
                    "\n" +
                    stringResource(R.string.if_required_by_law) +
                    "\n" +
                    stringResource(R.string.to_enforce_our_terms_conditions) +
                    "\n" +
                    stringResource(R.string.to_protect_the_rights)
        )

        ContentCard(
            title = stringResource(R.string._6_third_party_services),
            content = stringResource(R.string.our_service_may_include)
        )

        ContentCard(
            title = stringResource(R.string._7_your_rights),
            content = stringResource(R.string.depending_on_your_jurisdiction)
        )

        ContentCard(
            title = stringResource(R.string._8_changes_to_this_privacy_policy),
            content = stringResource(R.string.we_may_update)
        )






        Spacer(Modifier.height(148.dp))

    }



}

