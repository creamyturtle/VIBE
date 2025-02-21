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
import androidx.compose.ui.unit.dp
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
                .border(width = 1.dp, color = MaterialTheme.colorScheme.outline, shape = CircleShape)
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

        SectionTitle(title = "Frequently Asked Questions")

        ContentCard(
            title = "What is VIBE Social?",
            content = "VIBE is an awesome new place where you can find cool parties and events in your city. It was designed around the idea of connecting people on a more intimate level than your typical bar scene or nightclub. \n\nAll of the events you see on this page are private events, hosted by regular people. Businesses and commercial entities are not allowed to participate. Everything is completely FREE, and we require very little personal information to join VIBE."
        )

        ContentCard(
            title = "How does it work?",
            content = "All you have to do is create an account and you can start hosting or attending events. We use Instagram profiles as a way for hosts to review potential guests, and for guests to learn about their hosts. If you are going to participate, we suggest you set your Instagram profile to public, or quickly accept all follow requests you see on your account.\n" +
                    "\n" +
                    "Instagram is a great way to learn about the people that will be coming to your party, and it is not as intrusive as other methods like uploading ID's or taking selfies. We understand if this feels too personal to some people, but remember these are private events that could be taking place at their home so the host will need to learn at least something about you before allowing you in."
        )

        ContentCard(
            title = "Can I create an event?",
            content = "Yes, anybody with a VIBE account can create an event. You must have a legitimate Instagram and we suggest that you upload photos of what your event will look like. All events are age 18+ and nothing illegal is allowed. But beyond that, there are no real restrictions."
        )

        ContentCard(
            title = "Can anybody use VIBE?",
            content = "The VIBE platform is open to anybody aged 18 or older that has a legitimate Instagram account. We do not discriminate against any type of person or type of event. You could do literally anything from throwing a penthouse rager to hosting a bible study. As long as you think it will be a fun event, feel free to publish it.\n" +
                    "\n" +
                    "Anyone is welcome to attend the events, regardless of age or gender. As long as the host approves your RSVP, consider yourself welcome to participate just as anybody else. Some events are geared more towards certain demographics, so please be understanding if your RSVP was not approved. The host may have specific ideas about the type of party he wants to have, and we leave that discretion up to them.\n" +
                    "\n" +
                    "As for users, we do not condone any illegal activity. If you are planning on bringing drugs to an event or attempting to solicit prostitution, you will be quickly banned from the platform. This is a place to meet new people, not engage in illegal activities or earn a profit."
        )

        ContentCard(
            title = "Is VIBE safe to use?",
            content = "All of your personal data is encrypted on our servers, and we require very little info to get signed up. Your details are protected and nobody can see them unless you want them to.\n" +
                    "\n" +
                    "As for the safety of the actual events, we do our best to vet every event before allowing it to go public. If any host or guest develops a bad reputation they will be banned from VIBE. If you are hosting an event, please make sure to diligently review your guest's profile information before accepting them as a guest. If their profile looks fake, or they don't seem like a scrupulous person, do not accept them.\n" +
                    "\n" +
                    "As with any private party, there are risks to letting new people into your personal space. Please use our Check-In system to confirm all guests upon arrival, to ensure that they are who they say they are."
        )


        Spacer(Modifier.height(32.dp))

        SectionTitle("Hosting an Event")


        ContentCard(
            title = "Publish Your Event",
            content = "If you are interested in hosting a party or event, the process is simple. Simply click the Host an Event button at the top right. You will be asked to fill in some details about your event like a name, description, and amenities you are offering. \n\nYou can also upload photos or video to promote your event. Events without photos may still be published but you have a much higher chance of success if you can include photos with your listing. The more info your guests have the better."
        )

        ContentCard(
            title = "Review Process",
            content = "Once you submit your event listing, it will be reviewed by our site moderators for approval. This process usually only takes a couple hours, depending on what time you submitted your listing. We try to guarantee all approvals within 24 hours. Post your event early to ensure maximum visibility on the site.\n" +
                    "\n" +
                    "All events are subject to the VIBE Terms & Conditions and may be denied for any reason our moderators deem necessary. No commercial postings are allowed, by businesses or for-profit entities. You may not charge money for entrance to your event. And this goes without saying, but nothing illegal is allowed at your events. If we discover any of these rules being broken, your account will be suspended."
        )

        ContentCard(
            title = "Event Approval",
            content = "After your event has been approved, you will receive an email notifying you that the event is now Live on the website. You can visit your listing on the main page and check how everything appears. If there are any issues, please reach out to us and we will try to correct them.\n" +
                    "\n" +
                    "You can also now see your event active in your user Dashboard. There you can manage any RSVP requests, and accept or deny them."
        )

        ContentCard(
            title = "Review RSVPs",
            content = "When your event is active, you will start receiving RSVP requests in your Dashboard. For every reservation request, you will receive an email notifying you about it. \n\nYou can use the Instagram link to browse their profile or message them to decide if you would like them to attend your event. Please review and approve all requests in a timely manner so that you can fill up your party with guests."
        )

        ContentCard(
            title = "Checking In Guests",
            content = "On the day of your event, please use our Check-In feature in the Dashboard to Check-In your guests upon arrival. This is a way to ensure no unauthorized guests arrive at your event. Anybody without a reservation may be denied entry by the host.\n" +
                    "\n" +
                    "When reviewing guests who have arrived to your event, it may behoove you to compare their face to their instagram profile in person to guarantee they are who they claim they are. If all is well, simply click the \"Check In\" button and let them inside. Then, let the party begin! This process helps ensure a safe and fun environment for everyone."
        )


        Spacer(Modifier.height(32.dp))

        SectionTitle("Attending Events")


        ContentCard(
            title = "How do I attend a party?",
            content = "If you are a partygoer and want to explore events in your city, simply browse the main page where you can sort by different party types. You can also use our map feature to look around the city for an event close to where you live. The locations on the map are general locations, as the exact event location will only be revealed to guests whose RSVP has been accepted by the host.\n" +
                    "\n" +
                    "Once you have found an event that you want to attend, simply click the button to RSVP to the event. This will forward your details to the host and give them a chance to approve your reservation. Once approved, you will receive an email confirmation and be provided the actual location of the party in your Dashboard. Then, just show up and have a great time!"
        )

        ContentCard(
            title = "RSVP Process",
            content = "When requesting a reservation to a party, some limited information about yourself will be relayed to the event host. This includes your first name, age, gender, and your Instagram profile url. The host will review this information to decide whether to accept or deny your RSVP. We suggest that you set your Instagram profile to public, or that you pay close attention to any messages you receive on the platform. The host may be trying to contact you to decide whether to accept you or not.\n" +
                    "\n" +
                    "In the RSVP request page, you may offer to bring up to 4 other guests with you in your reservation. You may also offer to bring things to the party, such as food or alcohol or maybe a wireless speaker. Anything worth mentioning, feel free to include it in this field as it may improve your chances of being accepted to the event.\n" +
                    "\n" +
                    "Once approved, you will receive an email notification and you can visit your Dashboard to see the actual location of the event."
        )

        ContentCard(
            title = "Day of the Event",
            content = "On the day of the event, please visit your Dashboard to review the actual location of the event. Simply show up at the time specified and check-in with the host to be allowed in.\n" +
                    "\n" +
                    "Please do not bring any surprise guests with you, as they may result in your entire group being denied entry to the event. We leave event control completely up to the hosts, and they have sole discretion to deny you entry for any reason."
        )


        Spacer(Modifier.height(32.dp))

        SectionTitle("User Dashboard")

        ContentCard(
            title = "What is the Dashboard?",
            content = "The VIBE User Dashboard is an easy-to-use portal for managing all of your events, whether you are an eventgoer or a host. You can review all of the events you are attending or hosting there. \n\nYou can also review RSVPs submitted by your party guests, and approve or deny them. This portal is an easy way to manage your activities on VIBE, all in one place."
        )

        ContentCard(
            title = "Dashboard Access",
            content = "To use the Dashboard, you must be a registered user and be logged in to your account. This is a private area for managing your bookings, and thus is only available to VIBE users. Please make sure to login prior to visiting the Dashboard area."
        )


        Spacer(Modifier.height(32.dp))

        SectionTitle("Account Issues")


        ContentCard(
            title = "Trouble Registering?",
            content = "The registration form is pretty straightforward, as we only require a few bits of information. If you are having trouble creating an account, please check the form requirements. You must be over 18 and you must complete all of the fields, including Instagram username.\n" +
                    "\n" +
                    "If all of your information looks correct and you are still having trouble creating an account, please Contact Us directly so we can help."
        )

        ContentCard(
            title = "Trouble Logging In?",
            content = "If you have forgotten your password or want to change it, please use the Change Password feature located in the login form at the top left. Click login, then \"Lost Password\" and follow the process to reset your password.\n" +
                    "\n" +
                    "If all of your details are correct and you are still having trouble accessing your account, please Contact Us directly so we can help."
        )

        ContentCard(
            title = "Profile Changes",
            content = "Currently we are not allowing users to edit their profiles. This may be a feature we add in the future, but it could open the door to unscrupulous behavior. We don't want people changing their account details after registering. If you made an error when registering your account, please Contact Us and we will correct it."
        )

        ContentCard(
            title = "Event Changes",
            content = "As of now we are not allowing event hosts to change the details of their listings. We don't want people to have registered for an event, only to find out there is a different date, or some aspect of the event has changed. If you are an event host, please pay close attention when filling out the event form to ensure all of the details are correct. In specific cases, we may allow a host to change something about their event after contacting us directly, but this is up to the sole discretion of our moderation team.\n" +
                    "\n" +
                    "If you are a host and have something pertinent that you need to alter in your listing, please Contact Us."
        )

        ContentCard(
            title = "Other Issues",
            content = "If you have any other issues with the site or would like to send us some feedback, please Contact Us directly."
        )






        Spacer(Modifier.height(148.dp))

    }



}
