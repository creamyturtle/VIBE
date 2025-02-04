package com.example.vibe.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties


@Composable
fun HamburgerMenuButton() {
    var menuExpanded by remember { mutableStateOf(false) } // Main menu state
    var submenuExpanded by remember { mutableStateOf(false) } // Submenu state

    Box {
        // Menu Button (Row with Text and Icon)
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .clickable { menuExpanded = true } // Open main menu
                .padding(8.dp, 8.dp, 16.dp, 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Menu",
                style = MaterialTheme.typography.bodyLarge,
                color = Color(0xFFFE1943),
                modifier = Modifier.padding(end = 4.dp)
            )

            Spacer(Modifier.size(8.dp))

            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = "Menu Button",
                modifier = Modifier.size(36.dp),
                tint = MaterialTheme.colorScheme.onBackground
            )
        }

        // Main Dropdown Menu
        DropdownMenu(
            expanded = menuExpanded,
            onDismissRequest = { menuExpanded = false },
            properties = PopupProperties(focusable = true)
        ) {
            DropdownMenuItem(
                text = { Text("Dashboard") },
                onClick = { menuExpanded = false } // Handle Option 1
            )
            DropdownMenuItem(
                text = { Text("Host an Event") },
                onClick = { menuExpanded = false } // Handle Option 2
            )

            // Submenu Toggle Item
            DropdownMenuItem(
                text = {
                    Row(verticalAlignment = Alignment.CenterVertically) {

                        Text("Information ")
                        Spacer(Modifier.weight(1f))
                        Icon(
                            imageVector = if (submenuExpanded) Icons.Default.Close else Icons.Default.Add,
                            contentDescription = "Expand Submenu",
                            Modifier.size(18.dp)
                        )



                    }
                },
                onClick = { submenuExpanded = !submenuExpanded } // Toggle submenu
            )

            // Submenu items (conditionally displayed)
            if (submenuExpanded) {
                DropdownMenuItem(
                    text = { Text("  About") },
                    onClick = { submenuExpanded = false; menuExpanded = false }
                )
                DropdownMenuItem(
                    text = { Text("  FAQ") },
                    onClick = { submenuExpanded = false; menuExpanded = false }
                )
                DropdownMenuItem(
                    text = { Text("  Terms & Conditions") },
                    onClick = { submenuExpanded = false; menuExpanded = false }
                )
                DropdownMenuItem(
                    text = { Text("  Privacy Policy") },
                    onClick = { submenuExpanded = false; menuExpanded = false }
                )
            }

            DropdownMenuItem(
                text = { Text("User Profile") },
                onClick = { menuExpanded = false } // Handle Option 4
            )
            DropdownMenuItem(
                text = { Text("Logout") },
                onClick = { menuExpanded = false } // Handle Option 5
            )
        }
    }
}

