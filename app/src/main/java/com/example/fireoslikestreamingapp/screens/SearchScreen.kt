package com.example.fireoslikestreamingapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
fun SearchScreen(navController: NavController) {
    var searchQuery by remember { mutableStateOf("") }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    TextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("Buscar contenido...") },
                        singleLine = true,
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color(0xFF2D2D2D),
                            unfocusedContainerColor = Color(0xFF2D2D2D),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            cursorColor = Color.White,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1A1A1A)
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF141414))
                .padding(paddingValues)
        ) {
            if (searchQuery.isNotEmpty()) {
                items(10) { index ->
                    ListItem(
                        headlineContent = { 
                            Text(
                                "Resultado ${index + 1}",
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        },
                        supportingContent = { 
                            Text(
                                "Descripción del contenido",
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
                        modifier = Modifier
                            .clickable { /* TODO: Navegar al reproductor */ }
                            .background(Color(0xFF2D2D2D))
                    )
                }
            } else {
                item {
                    Text(
                        text = "Tendencias",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(16.dp)
                    )
                }
                items(5) { index ->
                    ListItem(
                        headlineContent = { 
                            Text(
                                "Tendencia ${index + 1}",
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        },
                        supportingContent = { 
                            Text(
                                "Contenido popular",
                                color = Color.White.copy(alpha = 0.7f)
                            )
                        },
                        leadingContent = {
                            Icon(
                                Icons.Default.TrendingUp,
                                contentDescription = null,
                                tint = Color(0xFFE50914)
                            )
                        },
                        modifier = Modifier.background(Color(0xFF2D2D2D))
                    )
                }
            }
        }
    }
} 