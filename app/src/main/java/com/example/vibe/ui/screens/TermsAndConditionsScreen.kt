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

        ContentCard(
            title = "VIBE Terms and Conditions",
            content = "Welcome to VIBE! These Terms & Conditions (\"Terms\") govern your access to and use of our website (the \"Service\"). By using our Service, you agree to comply with these Terms. If you do not agree to these Terms, please do not use our Service."
        )

        ContentCard(
            title = "1. User Accounts",
            content = "To access certain features of our Service, you must create an account. You are responsible for maintaining the confidentiality of your account information and for all activities that occur under your account."
        )

        ContentCard(
            title = "2. User Conduct",
            content = "We use the information we collect for the following purposes:\n" +
                    "\n" +
                    "Account Registration and Management: We use your email address, first name, and Instagram username to create and manage your account.\n" +
                    "\n" +
                    "Communications: We may use your email address to communicate important updates or notifications regarding your account and our services.\n" +
                    "\n" +
                    "Improving Our Service: We may use aggregated and anonymized information to improve our Service."
        )

        ContentCard(
            title = "3. User Content",
            content = "By submitting content (e.g., photos, descriptions, reviews) on our platform, you grant us a non-exclusive, worldwide, royalty-free license to use, modify, and display your content on our Service. You represent and warrant that you own or have the rights to submit the content you post."
        )

        ContentCard(
            title = "4. Privacy",
            content = "By using the Service, you consent to the collection and use of information as described in our Privacy Policy."
        )

        ContentCard(
            title = "5. Limitation of Liability",
            content = "To the fullest extent permitted by law, VIBE and its affiliates will not be liable for any damages, including but not limited to indirect, incidental, or consequential damages, arising out of your use of the Service or any content therein."
        )

        ContentCard(
            title = "6. Disclaimer",
            content = "Our Service is provided \"as is\" without warranties of any kind, either express or implied. We do not warrant that the Service will be error-free or uninterrupted, nor do we guarantee the accuracy or reliability of any information on the Service."
        )

        ContentCard(
            title = "7. Termination",
            content = "We reserve the right to suspend or terminate your account and access to the Service at our sole discretion, without prior notice, for conduct that violates these Terms or is otherwise harmful to the Service or other users."
        )

        ContentCard(
            title = "8. Governing Law",
            content = "These Terms are governed by and construed in accordance with the laws of Colombia, without regard to its conflict of law principles."
        )

        ContentCard(
            title = "9. Changes to These Terms",
            content = "We may update these Terms from time to time. We will notify you of any significant changes by posting the new Terms on this page. Your continued use of the Service after any such changes constitutes your acceptance of the new Terms."
        )








        Spacer(Modifier.height(148.dp))

    }



}

