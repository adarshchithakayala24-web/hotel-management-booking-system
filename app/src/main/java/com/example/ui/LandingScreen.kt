package com.example.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.WarmMutedText
import com.example.ui.theme.WarmWhiteText
import com.example.viewmodel.HMSViewModel
import com.example.viewmodel.UserRole

@Composable
fun LandingScreen(
    viewModel: HMSViewModel,
    onNavigateToGuest: () -> Unit,
    onNavigateToStaff: () -> Unit
) {
    var selectedRole by remember { mutableStateOf<UserRole?>(null) }
    var inputName by remember { mutableStateOf("") }
    var inputEmail by remember { mutableStateOf("") }
    var inputPassword by remember { mutableStateOf("") }
    var loginError by remember { mutableStateOf<String?>(null) }
    
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        visible = true
    }

    val gradientBrush = remember {
        Brush.verticalGradient(
            colors = listOf(
                Color(0xFF100F14), // Velvet Obsidian
                Color(0xFF1A1822)  // Deep Charcoal Slate
            )
        )
    }

    val goldRingBrush = remember {
        Brush.radialGradient(
            colors = listOf(
                Color(0x35D4AF37),
                Color.Transparent
            ),
            center = Offset(200f, 100f),
            radius = 600f
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradientBrush)
            .drawBehind {
                drawCircle(
                    brush = goldRingBrush,
                    radius = 800f,
                    center = Offset(size.width, 0f)
                )
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            
            // Elegant Monogram App Header
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(
                        Brush.linearGradient(
                            listOf(Color(0xFFD4AF37), Color(0xFFA67C1E))
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "A",
                    fontSize = 42.sp,
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF100F14)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Brand Title & Tagline
            Text(
                text = "A U R A S T A Y",
                fontSize = 32.sp,
                fontWeight = FontWeight.Light,
                letterSpacing = 8.sp,
                color = GoldPrimary,
                textAlign = TextAlign.Center
            )

            Text(
                text = "Bespoke Suites & Serene Sanctuary",
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                letterSpacing = 1.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 8.dp),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            AnimatedVisibility(
                visible = visible,
                enter = fadeIn() + slideInVertically(initialOffsetY = { 40 })
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    // Feature Highlight Panel - Using core-guaranteed icons
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0xFF22202E))
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        FeatureIconItem(icon = Icons.Default.Favorite, text = "Ritual Spa")
                        FeatureIconItem(icon = Icons.Default.Star, text = "Infinity Pool")
                        FeatureIconItem(icon = Icons.Default.Star, text = "Michelin Food")
                        FeatureIconItem(icon = Icons.Default.Person, text = "5-Star Butler")
                    }

                    Spacer(modifier = Modifier.height(28.dp))

                    if (selectedRole == null) {
                        // Portal Entrance Selection cards
                        Text(
                            text = "SELECT RECEPTION PORTAL",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 2.sp,
                            color = GoldPrimary,
                            modifier = Modifier.align(Alignment.Start).padding(bottom = 12.dp)
                        )

                        // Guest Portal Card
                        PortalSelectionCard(
                            title = "LUXURY GUEST ENTRANCE",
                            description = "Browse our bespoke catalog, view bookings, request in-suite dining, or query your Private AI Butler.",
                            badge = "Five-Star Experience",
                            onClick = {
                                selectedRole = UserRole.GUEST
                                inputEmail = "guest@aurastay.com"
                                inputName = "Alex Mercer"
                            }
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Staff Portal Card
                        PortalSelectionCard(
                            title = "STAFF OPERATIONS DESK",
                            description = "Manage check-ins, oversee housekeeping status, handle guest service tickets, and track live occupancy metrics.",
                            badge = "Internal Operations",
                            onClick = {
                                selectedRole = UserRole.STAFF
                                inputEmail = "staff@aurastay.com"
                                inputPassword = "staff"
                            }
                        )
                    } else {
                        // Dynamic Sign-In Form with gorgeous inputs
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF1B1A22)),
                            shape = RoundedCornerShape(20.dp),
                            border = CardDefaults.outlinedCardBorder().copy(width = 1.dp)
                        ) {
                            Column(modifier = Modifier.padding(24.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = if (selectedRole == UserRole.GUEST) "Register Guest Stay" else "Staff Credential Check",
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = GoldPrimary
                                    )
                                    Text(
                                        text = "Cancel",
                                        color = Color.Red,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Medium,
                                        modifier = Modifier.clickable {
                                            selectedRole = null
                                            loginError = null
                                        }
                                    )
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                if (selectedRole == UserRole.GUEST) {
                                    OutlinedTextField(
                                        value = inputName,
                                        onValueChange = { inputName = it },
                                        label = { Text("Guest Full Name") },
                                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = GoldPrimary) },
                                        modifier = Modifier.fillMaxWidth().testTag("guest_name_input"),
                                        shape = RoundedCornerShape(12.dp),
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedBorderColor = GoldPrimary,
                                            unfocusedBorderColor = Color(0xFF3B394E)
                                        )
                                    )

                                    Spacer(modifier = Modifier.height(12.dp))

                                    OutlinedTextField(
                                        value = inputEmail,
                                        onValueChange = { inputEmail = it },
                                        label = { Text("Guest Email") },
                                        leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = GoldPrimary) },
                                        modifier = Modifier.fillMaxWidth().testTag("guest_email_input"),
                                        shape = RoundedCornerShape(12.dp),
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedBorderColor = GoldPrimary,
                                            unfocusedBorderColor = Color(0xFF3B394E)
                                        )
                                    )
                                } else {
                                    OutlinedTextField(
                                        value = inputEmail,
                                        onValueChange = { inputEmail = it },
                                        label = { Text("Staff Email Identifier") },
                                        leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = GoldPrimary) },
                                        modifier = Modifier.fillMaxWidth().testTag("staff_email_input"),
                                        shape = RoundedCornerShape(12.dp),
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedBorderColor = GoldPrimary,
                                            unfocusedBorderColor = Color(0xFF3B394E)
                                        )
                                    )

                                    Spacer(modifier = Modifier.height(12.dp))

                                    OutlinedTextField(
                                        value = inputPassword,
                                        onValueChange = { inputPassword = it },
                                        label = { Text("Access Key") },
                                        leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = GoldPrimary) },
                                        modifier = Modifier.fillMaxWidth().testTag("staff_password_input"),
                                        shape = RoundedCornerShape(12.dp),
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedBorderColor = GoldPrimary,
                                            unfocusedBorderColor = Color(0xFF3B394E)
                                        )
                                    )
                                }

                                loginError?.let {
                                    Text(
                                        text = it,
                                        color = Color.Red,
                                        fontSize = 12.sp,
                                        modifier = Modifier.padding(top = 8.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.height(20.dp))

                                Button(
                                    onClick = {
                                        if (selectedRole == UserRole.GUEST) {
                                            if (inputName.isBlank() || inputEmail.isBlank()) {
                                                loginError = "Please populate all fields."
                                            } else {
                                                viewModel.login(inputEmail, inputName, UserRole.GUEST)
                                                onNavigateToGuest()
                                            }
                                        } else {
                                            if (inputEmail == "staff@aurastay.com" && inputPassword == "staff") {
                                                viewModel.login(inputEmail, "Supervisor Mode", UserRole.STAFF)
                                                onNavigateToStaff()
                                            } else {
                                                loginError = "Correct keys required."
                                            }
                                        }
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(54.dp)
                                        .testTag("submit_login_button"),
                                    shape = RoundedCornerShape(14.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Center
                                    ) {
                                        Text(
                                            text = if (selectedRole == UserRole.GUEST) "Register & Step Inside" else "Verify Credentials",
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF100F14),
                                            fontSize = 16.sp
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Icon(
                                            Icons.Default.ArrowForward,
                                            contentDescription = null,
                                            tint = Color(0xFF100F14),
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(28.dp))

                    // Helpful Hint panel regarding the mini project configuration
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0X1AFFFFFF)),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Icon(
                                Icons.Default.Info,
                                contentDescription = null,
                                tint = GoldPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "AuraStay is built with Kotlin and Jetpack Compose utilizing local offline Room persistence. Quick Staff keys: 'staff@aurastay.com' / 'staff'.",
                                fontSize = 11.sp,
                                color = WarmMutedText,
                                lineHeight = 16.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FeatureIconItem(icon: androidx.compose.ui.graphics.vector.ImageVector, text: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFF333045)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = GoldPrimary, modifier = Modifier.size(22.dp))
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(text, fontSize = 10.sp, color = WarmWhiteText, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun PortalSelectionCard(
    title: String,
    description: String,
    badge: String,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .testTag(title.lowercase().replace(" ", "_")),
        shape = RoundedCornerShape(16.dp),
        color = Color(0xFF1A1822),
        border = CardDefaults.outlinedCardBorder().copy(width = 1.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color(0x2BD4AF37))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(text = badge, fontSize = 9.sp, color = GoldPrimary, fontWeight = FontWeight.Bold)
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = title, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = GoldPrimary)
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = description, fontSize = 12.sp, color = WarmMutedText, lineHeight = 18.sp)
        }
    }
}
