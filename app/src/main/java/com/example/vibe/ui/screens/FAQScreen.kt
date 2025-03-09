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
fun FAQScreen(
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

        SectionTitle(title = stringResource(R.string.frequently_asked_questions))

        ContentCard(
            title = stringResource(R.string.what_is_vibe_social),
            content = stringResource(R.string.vibe_is_an_awesome_new_place)
        )

        ContentCard(
            title = stringResource(R.string.how_does_it_work),
            content = stringResource(R.string.all_you_have_to_do) +
                    "\n" +
                    stringResource(R.string.instagram_is_a_great_way)
        )

        ContentCard(
            title = stringResource(R.string.can_i_create_an_event),
            content = stringResource(R.string.yes_anybody_with_a_vibe)
        )

        ContentCard(
            title = stringResource(R.string.can_anybody_use_vibe),
            content = stringResource(R.string.the_vibe_platform_is_open) +
                    "\n" +
                    stringResource(R.string.anyone_is_welcome_to_attend) +
                    "\n" +
                    stringResource(R.string.as_for_users)
        )

        ContentCard(
            title = stringResource(R.string.is_vibe_safe_to_use),
            content = stringResource(R.string.all_of_your_personal_data) +
                    "\n" +
                    stringResource(R.string.as_for_the_safety) +
                    "\n" +
                    stringResource(R.string.as_with_any_private_party)
        )


        Spacer(Modifier.height(32.dp))

        SectionTitle(stringResource(R.string.hosting_an_event))


        ContentCard(
            title = stringResource(R.string.publish_your_event),
            content = stringResource(R.string.if_you_are_interested_in_hosting)
        )

        ContentCard(
            title = stringResource(R.string.review_process),
            content = stringResource(R.string.once_you_submit_your_event) +
                    "\n" +
                    stringResource(R.string.all_events_are_subject_to)
        )

        ContentCard(
            title = stringResource(R.string.event_approval),
            content = stringResource(R.string.after_your_event_has_been_approved) +
                    "\n" +
                    stringResource(R.string.you_can_also_now_see)
        )

        ContentCard(
            title = stringResource(R.string.review_rsvps),
            content = stringResource(R.string.when_your_event_is_active)
        )

        ContentCard(
            title = stringResource(R.string.checking_in_guests),
            content = stringResource(R.string.on_the_day_of_your_event) +
                    "\n" +
                    stringResource(R.string.when_reviewing_guests)
        )


        Spacer(Modifier.height(32.dp))

        SectionTitle(stringResource(R.string.attending_events))


        ContentCard(
            title = stringResource(R.string.how_do_i_attend_a_party),
            content = stringResource(R.string.if_you_are_a_partygoer) +
                    "\n" +
                    stringResource(R.string.once_you_have_found_an_event)
        )

        ContentCard(
            title = stringResource(R.string.rsvp_process),
            content = stringResource(R.string.when_requesting_a_reservation) +
                    "\n" +
                    stringResource(R.string.in_the_rsvp_request) +
                    "\n" +
                    stringResource(R.string.once_approved_you_will_receive)
        )

        ContentCard(
            title = stringResource(R.string.day_of_the_event),
            content = stringResource(R.string.on_the_day_of_the_event_please) +
                    "\n" +
                    stringResource(R.string.please_do_not_bring_any_surprise_guests)
        )


        Spacer(Modifier.height(32.dp))

        SectionTitle(stringResource(R.string.user_dashboard))

        ContentCard(
            title = stringResource(R.string.what_is_the_dashboard),
            content = stringResource(R.string.the_vibe_user_dashboard)
        )

        ContentCard(
            title = stringResource(R.string.dashboard_access),
            content = stringResource(R.string.to_use_the_dashboard)
        )


        Spacer(Modifier.height(32.dp))

        SectionTitle(stringResource(R.string.account_issues))


        ContentCard(
            title = stringResource(R.string.trouble_registering),
            content = stringResource(R.string.the_registration_form) +
                    "\n" +
                    stringResource(R.string.if_all_of_your_information)
        )

        ContentCard(
            title = stringResource(R.string.trouble_logging_in),
            content = stringResource(R.string.if_you_have_forgotten_your_password) +
                    "\n" +
                    stringResource(R.string.if_all_of_your_details_are_correct)
        )

        ContentCard(
            title = stringResource(R.string.profile_changes),
            content = stringResource(R.string.currently_we_are_not_allowing)
        )

        ContentCard(
            title = stringResource(R.string.event_changes),
            content = stringResource(R.string.as_of_now_we_are_not_allowing_event_hosts) +
                    "\n" +
                    stringResource(R.string.if_you_are_a_host)
        )

        ContentCard(
            title = stringResource(R.string.other_issues),
            content = stringResource(R.string.if_you_have_any_other_issues)
        )






        Spacer(Modifier.height(148.dp))

    }



}
