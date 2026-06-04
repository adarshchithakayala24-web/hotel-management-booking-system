package com.example.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StaffDashboard(
    viewModel: HMSViewModel,
    onNavigateBack: () -> Unit
) {
    val rooms by viewModel.rooms.collectAsState()
    val bookings by viewModel.allBookings.collectAsState()
    val services by viewModel.allServices.collectAsState()

    var activeTab by remember { mutableStateOf("Overview") }

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
                            Text("ST", fontWeight = FontWeight.Bold, color = Color.Black, fontSize = 11.sp)
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text("Ops Console", fontSize = 18.sp, fontWeight = FontWeight.Light, letterSpacing = 2.sp)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = GoldPrimary)
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padValues)
                .padding(horizontal = 20.dp)
        ) {
            
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF1B1A22))
                    .padding(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                val tabs = listOf("Overview", "Rooms", "Requests")
                tabs.forEach { tab ->
                    val isSelected = activeTab == tab
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isSelected) GoldPrimary else Color.Transparent)
                            .clickable { activeTab = tab }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = tab,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) Color.Black else WarmWhiteText
                        )
                    }
                }
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                when (activeTab) {
                    "Overview" -> {
                        item {
                            StaffStatisticsSummaryPanel(rooms, bookings, services)
                        }

                        item {
                            Text(
                                text = "WEEKLY REVENUE & DEMANDS",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 2.sp,
                                color = GoldPrimary
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFF1B1A22)),
                                border = CardDefaults.outlinedCardBorder().copy(width = 0.5.dp)
                            ) {
                                LiveCanvasAnalyticsGraph()
                            }
                        }

                        item {
                            Text(
                                text = "ACTIVE STAYS MANAGEMENT",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 2.sp,
                                color = GoldPrimary
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                        }

                        if (bookings.isEmpty()) {
                            item {
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1B1A22))
                                ) {
                                    Text(
                                        text = "No reservations logged yet. Go to Guest Portal to reserve suites.",
                                        modifier = Modifier.padding(20.dp),
                                        color = WarmMutedText,
                                        fontSize = 12.sp,
                                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                    )
                                }
                            }
                        } else {
                            items(bookings) { booking ->
                                StaffBookingControlItem(booking = booking,
                                    onCancel = {
                                        viewModel.cancelBooking(booking.id, booking.roomNumber)
                                    },
                                    onCheckout = {
                                        viewModel.checkOutBooking(booking.id, booking.roomNumber)
                                    }
                                )
                            }
                        }
                    }

                    "Rooms" -> {
                        item {
                            Text(
                                text = "DYNAMIC CHAMBER CONTROLS",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 2.sp,
                                color = GoldPrimary
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                        }

                        items(rooms) { room ->
                            StaffRoomManagementItem(room = room) { nextStatus ->
                                viewModel.setRoomStatus(room.roomNumber, nextStatus)
                            }
                        }
                    }

                    "Requests" -> {
                        item {
                            Text(
                                text = "GUEST SERVICES TICKETING",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 2.sp,
                                color = GoldPrimary
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                        }

                        val activeTickets = services.filter { it.status != "Completed" }
                        if (activeTickets.isEmpty()) {
                            item {
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1B1A22))
                                ) {
                                    Text(
                                        text = "Outstanding service requests resolved! Zero pending dining or clean alerts.",
                                        modifier = Modifier.padding(24.dp),
                                        color = WarmMutedText,
                                        fontSize = 12.sp,
                                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                    )
                                }
                            }
                        } else {
                            items(activeTickets) { service ->
                                StaffServiceTicketItem(service = service,
                                    onStart = { viewModel.updateServiceStatus(service.id, "In Progress") },
                                    onComplete = { viewModel.updateServiceStatus(service.id, "Completed") }
                                )
                            }
                        }
                    }
                }
                
                item {
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}

@Composable
fun StaffStatisticsSummaryPanel(
    rooms: List<RoomEntity>,
    bookings: List<BookingEntity>,
    services: List<ServiceEntity>
) {
    val totalRooms = rooms.size
    val occupiedCount = rooms.count { it.status == "Occupied" }
    val occupancyRate = if (totalRooms > 0) (occupiedCount * 100) / totalRooms else 0
    val pendingServiceCount = services.count { it.status == "Pending" || it.status == "In Progress" }
    val totalRevenue = bookings.filter { it.status != "Cancelled" }.sumOf { it.totalPrice }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Column(modifier = Modifier.weight(1f)) {
            StatCompactCell("Occupancy", "$occupancyRate%", Icons.Default.Person, ColorAvailable)
            Spacer(modifier = Modifier.height(10.dp))
            StatCompactCell("Active Orders", "$pendingServiceCount", Icons.Default.Settings, ColorCleaning)
        }
        
        Column(modifier = Modifier.weight(1.2f)) {
            StatCompactCell("Total Revenue", "$${totalRevenue.toInt()}", Icons.Default.Star, GoldPrimary)
            Spacer(modifier = Modifier.height(10.dp))
            StatCompactCell("Total System Units", "$totalRooms Rooms", Icons.Default.List, ColorMaintenance)
        }
    }
}

@Composable
fun StatCompactCell(title: String, valText: String, icon: androidx.compose.ui.graphics.vector.ImageVector, tint: Color) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1B1A22))
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(tint.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = tint, modifier = Modifier.size(16.dp))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(text = title, fontSize = 10.sp, color = WarmMutedText)
                Text(text = valText, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = WarmWhiteText)
            }
        }
    }
}

@Composable
fun LiveCanvasAnalyticsGraph() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Operational Yield Metrics", fontSize = 12.sp, color = GoldPrimary, fontWeight = FontWeight.Bold)
                Text("Prestige Suite Leads", fontSize = 10.sp, color = WarmMutedText)
            }
            
            Spacer(modifier = Modifier.height(12.dp))

            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                drawLine(
                    color = Color(0xFF2E2C3D),
                    start = Offset(0f, size.height * 0.25f),
                    end = Offset(size.width, size.height * 0.25f),
                    strokeWidth = 1f
                )
                drawLine(
                    color = Color(0xFF2E2C3D),
                    start = Offset(0f, size.height * 0.65f),
                    end = Offset(size.width, size.height * 0.65f),
                    strokeWidth = 1f
                )

                val barWidth = 45f
                val spacing = (size.width - (3 * barWidth)) / 4
                val categories = listOf(
                    Pair(size.height * 0.75f, ColorAvailable),
                    Pair(size.height * 0.45f, GoldPrimary),
                    Pair(size.height * 0.25f, ColorOccupied)
                )

                categories.forEachIndexed { idx, item ->
                    val x = spacing + idx * (barWidth + spacing)
                    drawRoundRect(
                        color = item.second.copy(alpha = 0.8f),
                        topLeft = Offset(x, item.first),
                        size = Size(barWidth, size.height - item.first),
                        cornerRadius = CornerRadius(8f, 8f)
                    )
                }

                val points = listOf(
                    Offset(0f, size.height * 0.8f),
                    Offset(size.width * 0.25f, size.height * 0.5f),
                    Offset(size.width * 0.5f, size.height * 0.3f),
                    Offset(size.width * 0.75f, size.height * 0.6f),
                    Offset(size.width, size.height * 0.15f)
                )

                val path = Path().apply {
                    moveTo(points[0].x, points[0].y)
                    points.forEach { point ->
                        lineTo(point.x, point.y)
                    }
                }

                drawPath(
                    path = path,
                    color = GoldPrimary,
                    style = Stroke(width = 4f)
                )

                drawCircle(
                    color = Color.White,
                    radius = 8f,
                    center = points[4]
                )
                drawCircle(
                    color = GoldPrimary,
                    radius = 14f,
                    center = points[4],
                    style = Stroke(width = 3f)
                )
            }
        }
    }
}

@Composable
fun StaffBookingControlItem(
    booking: BookingEntity,
    onCancel: () -> Unit,
    onCheckout: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1B1A22)),
        border = BorderStroke(0.5.dp, GoldPrimary)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = "R O O M   ${booking.roomNumber}", fontSize = 10.sp, color = GoldPrimary, fontWeight = FontWeight.Bold, letterSpacing = 2.sp)
                    Text(text = booking.guestName, fontSize = 14.sp, color = WarmWhiteText, fontWeight = FontWeight.Bold)
                    Text(text = booking.guestEmail, fontSize = 11.sp, color = WarmMutedText)
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

            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Days schedule: ${booking.checkInDate} — ${booking.checkOutDate}", fontSize = 12.sp, color = WarmWhiteText)
            
            if (booking.status == "Confirmed" || booking.status == "Checked In") {
                Spacer(modifier = Modifier.height(14.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = onCancel,
                        modifier = Modifier.weight(1f).height(38.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0x27FF0000)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Cancel stay", color = Color.Red, fontSize = 12.sp)
                    }
                    Button(
                        onClick = onCheckout,
                        modifier = Modifier.weight(1.2f).height(38.dp).testTag("checkout_button_${booking.roomNumber}"),
                        colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Checkout guest", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun StaffRoomManagementItem(room: RoomEntity, onStatusChange: (String) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1B1A22))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = "ROOM ${room.roomNumber}", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = GoldPrimary)
                    Text(text = room.type, fontSize = 12.sp, color = WarmWhiteText)
                }

                val dotColor = when (room.status) {
                    "Available" -> ColorAvailable
                    "Occupied" -> ColorOccupied
                    "Cleaning" -> ColorCleaning
                    else -> ColorMaintenance
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(dotColor))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = room.status, fontSize = 12.sp, color = WarmWhiteText, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            Text(text = "Reconfigure Status:", fontSize = 11.sp, color = WarmMutedText)

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                val statuses = listOf("Available", "Occupied", "Cleaning", "Maintenance")
                statuses.forEach { label ->
                    val isActive = room.status == label
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(6.dp))
                            .background(if (isActive) GoldPrimary else Color(0xFF24222E))
                            .clickable { onStatusChange(label) }
                            .padding(vertical = 6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = label, 
                            fontSize = 9.sp, 
                            color = if (isActive) Color.Black else WarmWhiteText,
                            fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun StaffServiceTicketItem(
    service: ServiceEntity,
    onStart: () -> Unit,
    onComplete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1B1A22))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Star, contentDescription = null, tint = GoldPrimary, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "${service.type} - Room ${service.roomNumber}", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = GoldPrimary)
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color(0xFF2C2A39))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(text = service.status, fontSize = 11.sp, color = ColorCleaning, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(6.dp))
            Text(text = service.details, fontSize = 12.sp, color = WarmWhiteText)

            Spacer(modifier = Modifier.height(14.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (service.status == "Pending") {
                    Button(
                        onClick = onStart,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2999F9)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Accept ticket", color = Color.White, fontSize = 11.sp)
                    }
                }
                
                Button(
                    onClick = onComplete,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = ColorAvailable),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Deem Completed", color = Color.White, fontSize = 11.sp)
                }
            }
        }
    }
}
