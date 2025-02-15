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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
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
                .background(color = Color.White, shape = CircleShape)
                .border(width = 1.dp, color = Color.LightGray, shape = CircleShape)
                .size(32.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = Color.Black,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(Modifier.height(16.dp))

        SectionTitle(title = "Privacy Policy")

        ContentCard(
            title = "1. Information We Collect",
            content = "We may collect the following personal information about you:\n" +
                    "\n" +
                    "Email Address\n" +
                    "Name\n" +
                    "Instagram Username\n" +
                    "Age\n" +
                    "Gender"
        )

        ContentCard(
            title = "2. How We Use Your Information",
            content = "We use the information we collect for the following purposes:\n" +
                    "\n" +
                    "Account Registration and Management: We use your email address, first name, and Instagram username to create and manage your account.\n" +
                    "\n" +
                    "Communications: We may use your email address to communicate important updates or notifications regarding your account and our services.\n" +
                    "\n" +
                    "Improving Our Service: We may use aggregated and anonymized information to improve our Service."
        )

        ContentCard(
            title = "3. Data Security",
            content = "We take reasonable precautions to protect your information. However, no method of transmission over the Internet or method of electronic storage is 100% secure, and we cannot guarantee absolute security."
        )

        ContentCard(
            title = "4. Data Retention",
            content = "We retain your personal information as long as necessary to fulfill the purposes outlined in this Privacy Policy, or as required by law."
        )

        ContentCard(
            title = "5. Sharing Your Information",
            content = "We do not sell, trade, or otherwise transfer your personal information to outside parties, except:\n" +
                    "\n" +
                    "\n" +
                    "If required by law or to comply with legal processes.\n" +
                    "\n" +
                    "To enforce our Terms & Conditions.\n" +
                    "\n" +
                    "To protect the rights, safety, and security of our users or the public."
        )

        ContentCard(
            title = "6. Third-Party Services",
            content = "Our Service may include links to third-party websites or services, such as social media platforms. We are not responsible for the privacy practices or content of these third parties."
        )

        ContentCard(
            title = "7. Your Rights",
            content = "Depending on your jurisdiction, you may have rights regarding your personal information, including the right to access, update, or delete it. Please contact us if you have any requests regarding your information."
        )

        ContentCard(
            title = "8. Changes to This Privacy Policy",
            content = "We may update our Privacy Policy from time to time. We will notify you of any significant changes by posting the new Privacy Policy on this page. Your continued use of our Service constitutes your acceptance of any changes."
        )






        Spacer(Modifier.height(148.dp))

    }



}

