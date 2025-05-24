package com.example.fireoslikestreamingapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.fireoslikestreamingapp.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryScreen(navController: NavController) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Continuar viendo", "Mi lista", "Descargas")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        "Mi Biblioteca",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1A1A1A)
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF141414))
                .padding(paddingValues)
        ) {
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color(0xFF1A1A1A),
                contentColor = Color.White
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = { 
                            Text(
                                title,
                                color = if (selectedTab == index) Color.White else Color.White.copy(alpha = 0.7f)
                            )
                        }
                    )
                }
            }

            when (selectedTab) {
                0 -> ContinueWatchingContent()
                1 -> MyListContent()
                2 -> DownloadsContent()
            }
        }
    }
}

@Composable
fun ContinueWatchingContent() {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(5) { index ->
            ListItem(
                headlineContent = { 
                    Text(
                        "Contenido ${index + 1}",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                },
                supportingContent = { 
                    Text(
                        "Progreso: ${(index + 1) * 20}%",
                        color = Color.White.copy(alpha = 0.7f)
                    )
                },
                leadingContent = {
                    Icon(
                        Icons.Default.PlayCircle,
                        contentDescription = null,
                        tint = Color(0xFFE50914)
                    )
                },
                modifier = Modifier.background(Color(0xFF2D2D2D))
            )
        }
    }
}

@Composable
fun MyListContent() {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(5) { index ->
            ListItem(
                headlineContent = { 
                    Text(
                        "Mi Lista ${index + 1}",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                },
                supportingContent = { 
                    Text(
                        "Añadido recientemente",
                        color = Color.White.copy(alpha = 0.7f)
                    )
                },
                leadingContent = {
                    Icon(
                        Icons.Default.Favorite,
                        contentDescription = null,
                        tint = Color(0xFFE50914)
                    )
                },
                modifier = Modifier.background(Color(0xFF2D2D2D))
            )
        }
    }
}

@Composable
fun DownloadsContent() {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(5) { index ->
            ListItem(
                headlineContent = { 
                    Text(
                        "Descarga ${index + 1}",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                },
                supportingContent = { 
                    Text(
                        "Tamaño: ${(index + 1) * 100}MB",
                        color = Color.White.copy(alpha = 0.7f)
                    )
                },
                leadingContent = {
                    Icon(
                        Icons.Default.Download,
                        contentDescription = null,
                        tint = Color(0xFFE50914)
                    )
                },
                modifier = Modifier.background(Color(0xFF2D2D2D))
            )
        }
    }
} 