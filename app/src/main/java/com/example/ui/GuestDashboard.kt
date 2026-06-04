package com.example.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.BookingEntity
import com.example.data.RoomEntity
import com.example.data.ServiceEntity
import com.example.ui.theme.ColorAvailable
import com.example.ui.theme.ColorCleaning
import com.example.ui.theme.ColorMaintenance
import com.example.ui.theme.ColorOccupied
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.WarmMutedText
import com.example.ui.theme.WarmWhiteText
import com.example.viewmodel.HMSViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GuestDashboard(
    viewModel: HMSViewModel,
    onNavigateToChat: () -> Unit,
    onNavigateBack: () -> Unit
) {
    val rooms by viewModel.rooms.collectAsState()
    val bookings by viewModel.guestBookings.collectAsState()
    val services by viewModel.allServices.collectAsState()
    val session by viewModel.userSession.collectAsState()

    var selectedRoomForBooking by remember { mutableStateOf<RoomEntity?>(null) }
    var showBookingForm by remember { mutableStateOf(false) }
    var showServiceForm by remember { mutableStateOf(false) }

    var guestServiceType by remember { mutableStateOf("Dining") }
    var guestServiceDetails by remember { mutableStateOf("") }
    var serviceSubmittedAlert by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(GoldPrimary),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("AS", fontWeight = FontWeight.Bold, color = Color.Black, fontSize = 12.sp)
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text("AuraStay Lounge", fontSize = 18.sp, fontWeight = FontWeight.Light, letterSpacing = 2.sp)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Log out", tint = GoldPrimary)
                    }
                },
                actions = {
                    IconButton(onClick = onNavigateToChat, modifier = Modifier.testTag("ai_concierge_shortcut")) {
                        Icon(Icons.Default.Send, contentDescription = "Chat AI Concierge", tint = GoldPrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF100F14),
                    titleContentColor = GoldPrimary
                )
            )
        },
        containerColor = Color(0xFF100F14)
    ) { padValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padValues)
                .padding(horizontal = 20.dp)
        ) {
            // Welcome Header Card
            item {
                Spacer(modifier = Modifier.height(16.dp))
                GuestWelcomePanel(session.name, onNavigateToChat)
                Spacer(modifier = Modifier.height(24.dp))
            }

            // Book Area / Room List Header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "EXPLORE BESPOKE CHAMBERS",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp,
                        color = GoldPrimary
                    )
                    Text(
                        text = "${rooms.size} units available",
                        fontSize = 12.sp,
                        color = WarmMutedText
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            // LazyRow Rooms Explorer
            item {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(310.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(rooms) { room ->
                        RoomCardItem(room = room) {
                            if (room.status == "Available") {
                                selectedRoomForBooking = room
                                showBookingForm = true
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(28.dp))
            }

            // Active / Prior Stays timeline
            item {
                Text(
                    text = "YOUR LUXURY TIMELINE",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp,
                    color = GoldPrimary
                )
                Spacer(modifier = Modifier.height(12.dp))

                if (bookings.isEmpty()) {
                    EmptyHistoryCard()
                } else {
                    bookings.forEach { booking ->
                        GuestBookingItem(booking = booking) {
                            if (booking.status == "Confirmed" || booking.status == "Checked In") {
                                showServiceForm = true
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }

            // Guest Service Requests history
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "ROOM ORDERS & TICKETS",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp,
                        color = GoldPrimary
                    )
                    Text(
                        text = "Request assistance",
                        color = GoldPrimary,
                        fontSize = 12.sp,
                        modifier = Modifier.clickable { showServiceForm = true }
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))

                val filterServices = services.filter { svc ->
                    bookings.any { b -> b.roomNumber == svc.roomNumber && b.status != "Checked Out" }
                }

                if (filterServices.isEmpty()) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF1B1A22))
                    ) {
                        Text(
                            text = "No active room requests. Request dining, amenities, or spa options anytime using our form.",
                            modifier = Modifier.padding(16.dp),
                            fontSize = 12.sp,
                            color = WarmMutedText,
                            textAlign = TextAlign.Center
                        )
                    }
                } else {
                    filterServices.forEach { svc ->
                        GuestServiceItem(service = svc)
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }
                Spacer(modifier = Modifier.height(40.dp))
            }
        }

        // Animated Form Overlays
        if (showBookingForm && selectedRoomForBooking != null) {
            BookingFormOverlay(
                room = selectedRoomForBooking!!,
                guestName = session.name,
                guestEmail = session.email,
                onDismiss = { showBookingForm = false },
                onConfirm = { checkIn, checkOut, total ->
                    viewModel.bookRoom(
                        roomNumber = selectedRoomForBooking!!.roomNumber,
                        guestName = session.name,
                        guestEmail = session.email,
                        checkInDate = checkIn,
                        checkOutDate = checkOut,
                        totalPrice = total
                    ) {
                        showBookingForm = false
                    }
                }
            )
        }

        if (showServiceForm) {
            val liveBooking = bookings.firstOrNull { it.status == "Confirmed" || it.status == "Checked In" }
            ServiceFormOverlay(
                activeRoomNumber = liveBooking?.roomNumber ?: "101",
                onDismiss = { showServiceForm = false },
                onConfirm = { type, details ->
                    viewModel.requestService(
                        roomNumber = liveBooking?.roomNumber ?: "101",
                        type = type,
                        details = details
                    ) {
                        showServiceForm = false
                        serviceSubmittedAlert = true
                    }
                }
            )
        }
    }
}

@Composable
fun GuestWelcomePanel(name: String, onNavigateToChat: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1B1A22)),
        shape = RoundedCornerShape(20.dp),
        border = CardDefaults.outlinedCardBorder().copy(width = 1.dp)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Text(
                text = "Welcome Back,",
                fontSize = 14.sp,
                color = WarmMutedText
            )
            Text(
                text = "Guest $name",
                fontSize = 24.sp,
                color = GoldPrimary,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Each room includes high-efficiency solar-controlled temperature systems and digital key authorization.",
                fontSize = 12.sp,
                color = WarmMutedText,
                lineHeight = 18.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            
            Button(
                onClick = onNavigateToChat,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0x1AD4AF37)),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, GoldPrimary)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Send, contentDescription = null, tint = GoldPrimary, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Acquire AI Concierge Advice", color = GoldPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun RoomCardItem(room: RoomEntity, onBook: () -> Unit) {
    Card(
        modifier = Modifier
            .width(230.dp)
            .fillMaxHeight(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1B1A22)),
        shape = RoundedCornerShape(16.dp),
        border = CardDefaults.outlinedCardBorder().copy(width = 0.5.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
            ) {
                AsyncImage(
                    model = room.imageUrl,
                    contentDescription = room.type,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                
                val statusColor = when (room.status) {
                    "Available" -> ColorAvailable
                    "Occupied" -> ColorOccupied
                    "Cleaning" -> ColorCleaning
                    else -> ColorMaintenance
                }
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(10.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(statusColor.copy(alpha = 0.85f))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = room.status, 
                        fontSize = 10.sp, 
                        color = Color.White, 
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Room ${room.roomNumber}",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = GoldPrimary
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Star, contentDescription = null, tint = GoldPrimary, modifier = Modifier.size(12.dp))
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(text = room.rating.toString(), fontSize = 12.sp, color = WarmWhiteText)
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = room.type,
                        fontSize = 13.sp,
                        color = WarmWhiteText,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = room.features,
                        fontSize = 11.sp,
                        color = WarmMutedText,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        lineHeight = 15.sp
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Column {
                        Text("Rate / Night", fontSize = 9.sp, color = WarmMutedText)
                        Text("$${room.price.toInt()}", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = GoldPrimary)
                    }

                    if (room.status == "Available") {
                        Button(
                            onClick = onBook,
                            colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .height(36.dp)
                                .testTag("book_room_${room.roomNumber}")
                        ) {
                            Text("Book", color = Color.Black, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    } else {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFF2C2A38))
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text("Reserved", color = WarmMutedText, fontSize = 11.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun GuestBookingItem(booking: BookingEntity, onRequestService: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1B1A22)),
        border = CardDefaults.outlinedCardBorder().copy(width = 0.5.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = "R O O M   ${booking.roomNumber}", fontSize = 11.sp, letterSpacing = 2.sp, fontWeight = FontWeight.Bold, color = GoldPrimary)
                    Text(text = "Stay duration total: $${booking.totalPrice.toInt()}", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = WarmWhiteText)
                }
                
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(
                            when (booking.status) {
                                "Confirmed" -> ColorAvailable.copy(alpha = 0.2f)
                                "Checked In" -> ColorMaintenance.copy(alpha = 0.2f)
                                else -> ColorOccupied.copy(alpha = 0.2f)
                            }
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = booking.status,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = when (booking.status) {
                            "Confirmed" -> ColorAvailable
                            "Checked In" -> ColorMaintenance
                            else -> ColorOccupied
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.DateRange, contentDescription = null, tint = GoldPrimary, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "${booking.checkInDate} to ${booking.checkOutDate}",
                    fontSize = 12.sp,
                    color = WarmMutedText
                )
            }

            if (booking.status == "Confirmed" || booking.status == "Checked In") {
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = onRequestService,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C2A38)),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(Icons.Default.Settings, contentDescription = null, tint = GoldPrimary, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Order Room Service / Dining", color = GoldPrimary, fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
fun GuestServiceItem(service: ServiceEntity) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1B1A22))
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF2E2B3E)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = when (service.type) {
                            "Dining" -> Icons.Default.Star
                            "Housekeeping" -> Icons.Default.Refresh
                            else -> Icons.Default.Settings
                        },
                        contentDescription = null,
                        tint = GoldPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(text = service.type, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = GoldPrimary)
                    Text(text = service.details, fontSize = 12.sp, color = WarmWhiteText, maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color(0xFF2A2835))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = service.status,
                    fontSize = 10.sp,
                    color = when (service.status) {
                        "Pending" -> ColorCleaning
                        "In Progress" -> ColorMaintenance
                        else -> ColorAvailable
                    },
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun EmptyHistoryCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1B1A22)),
        shape = RoundedCornerShape(14.dp),
        border = CardDefaults.outlinedCardBorder().copy(width = 0.5.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(Icons.Default.Home, contentDescription = null, tint = GoldPrimary, modifier = Modifier.size(40.dp))
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Welcome to your premier vacation!",
                fontWeight = FontWeight.Bold,
                color = WarmWhiteText,
                fontSize = 14.sp
            )
            Text(
                text = "Select any available luxury standard or duplex chamber card above to secure your booking instantly.",
                fontSize = 11.sp,
                color = WarmMutedText,
                textAlign = TextAlign.Center,
                lineHeight = 16.sp,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@Composable
fun BookingFormOverlay(
    room: RoomEntity,
    guestName: String,
    guestEmail: String,
    onDismiss: () -> Unit,
    onConfirm: (checkIn: String, checkOut: String, total: Double) -> Unit
) {
    var checkInDate by remember { mutableStateOf("06/10/2026") }
    var checkOutDate by remember { mutableStateOf("06/15/2026") }
    var nightsCount by remember { mutableStateOf(5) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.85f))
            .clickable { onDismiss() },
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .clickable(enabled = false) {},
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1822)),
            shape = RoundedCornerShape(20.dp),
            border = BorderStroke(1.dp, GoldPrimary)
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text(
                    text = "RESERVE SUITE ${room.roomNumber}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp,
                    color = GoldPrimary
                )
                Spacer(modifier = Modifier.height(16.dp))

                Text(text = room.type, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = WarmWhiteText)
                Text(text = "Features: ${room.features}", fontSize = 12.sp, color = WarmMutedText, modifier = Modifier.padding(top = 4.dp))
                
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = checkInDate,
                    onValueChange = { 
                        checkInDate = it
                        nightsCount = calculateNights(checkInDate, checkOutDate)
                    },
                    label = { Text("Check-In Date (MM/DD/YYYY)") },
                    leadingIcon = { Icon(Icons.Default.List, contentDescription = null, tint = GoldPrimary) },
                    modifier = Modifier.fillMaxWidth().testTag("checkin_input"),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = GoldPrimary,
                        unfocusedBorderColor = Color(0xFF3B394E)
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = checkOutDate,
                    onValueChange = { 
                        checkOutDate = it
                        nightsCount = calculateNights(checkInDate, checkOutDate)
                    },
                    label = { Text("Check-Out Date (MM/DD/YYYY)") },
                    leadingIcon = { Icon(Icons.Default.List, contentDescription = null, tint = GoldPrimary) },
                    modifier = Modifier.fillMaxWidth().testTag("checkout_input"),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = GoldPrimary,
                        unfocusedBorderColor = Color(0xFF3B394E)
                    )
                )

                Spacer(modifier = Modifier.height(20.dp))

                val totalCost = room.price * nightsCount
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF262431))
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Billing calculation", fontSize = 10.sp, color = WarmMutedText)
                        Text("$${room.price.toInt()} x $nightsCount nights", fontSize = 13.sp, color = WarmWhiteText)
                    }
                    Text("$${totalCost.toInt()}", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = GoldPrimary)
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E2B3E)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Back", color = WarmMutedText)
                    }
                    Button(
                        onClick = { onConfirm(checkInDate, checkOutDate, totalCost) },
                        colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1.5f).testTag("confirm_booking_button")
                    ) {
                        Text("Confirm Stay", color = Color.Black, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun ServiceFormOverlay(
    activeRoomNumber: String,
    onDismiss: () -> Unit,
    onConfirm: (type: String, details: String) -> Unit
) {
    var serviceType by remember { mutableStateOf("Dining") }
    var serviceDetails by remember { mutableStateOf("Prime Tenderloin Steak (Medium Rare) and fresh water") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.85f))
            .clickable { onDismiss() },
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .clickable(enabled = false) {},
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1822)),
            shape = RoundedCornerShape(20.dp),
            border = BorderStroke(1.dp, GoldPrimary)
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text(
                    text = "REQUEST ROOM ASSISTANCE",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp,
                    color = GoldPrimary
                )
                Spacer(modifier = Modifier.height(16.dp))

                Text("Service Category", fontSize = 13.sp, color = WarmMutedText)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val types = listOf("Dining", "Housekeeping", "Amenities")
                    types.forEach { type ->
                        val isSelected = serviceType == type
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) GoldPrimary else Color(0xFF262431))
                                .clickable { serviceType = type }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = type, 
                                fontSize = 11.sp, 
                                color = if (isSelected) Color.Black else WarmWhiteText,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = serviceDetails,
                    onValueChange = { serviceDetails = it },
                    label = { Text("Describe details of requests") },
                    modifier = Modifier.fillMaxWidth().height(100.dp).testTag("service_details_input"),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = GoldPrimary,
                        unfocusedBorderColor = Color(0xFF3B394E)
                    )
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E2B3E)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Cancel", color = WarmMutedText)
                    }
                    Button(
                        onClick = { onConfirm(serviceType, serviceDetails) },
                        colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1.5f).testTag("confirm_service_button")
                    ) {
                        Text("Order Service", color = Color.Black, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

fun calculateNights(checkIn: String, checkOut: String): Int {
    return try {
        val format = SimpleDateFormat("MM/dd/yyyy", Locale.US)
        val date1 = format.parse(checkIn)
        val date2 = format.parse(checkOut)
        val diff = date2.time - date1.time
        val nights = (diff / (1000 * 60 * 60 * 24)).toInt()
        if (nights > 0) nights else 1
    } catch (e: Exception) {
        5
    }
}
