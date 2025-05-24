package com.example.fireoslikestreamingapp.screens

import android.app.Activity
import android.content.Context
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.FrameLayout
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.WindowCompat
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.ui.input.pointer.pointerInput
import android.view.WindowManager
import android.provider.Settings
import android.content.ContentResolver
import android.view.Window
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerScreen(navController: NavController, videoUrl: String, title: String) {
    val context = LocalContext.current
    val view = LocalView.current
    var isPlaying by remember { mutableStateOf(true) }
    var showControls by remember { mutableStateOf(true) }
    var currentTime by remember { mutableStateOf(0L) }
    var totalTime by remember { mutableStateOf(0L) }
    var volume by remember { mutableStateOf(1f) }
    var isFullScreen by remember { mutableStateOf(false) }
    var isBuffering by remember { mutableStateOf(false) }
    var brightness by remember { mutableStateOf(0.5f) }
    var showGestureIndicator by remember { mutableStateOf(false) }
    var gestureType by remember { mutableStateOf("") }
    var gestureValue by remember { mutableStateOf(0f) }
    var showForwardRewind by remember { mutableStateOf(false) }
    var forwardRewindAmount by remember { mutableStateOf(0L) }
    val scope = rememberCoroutineScope()
    val controlsAlpha = remember { Animatable(0f) }
    val forwardRewindScale = remember { Animatable(1f) }

    // Inicializar ExoPlayer
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(videoUrl))
            prepare()
            playWhenReady = true
            volume = 1f
        }
    }

    // Sincronizar progreso
    LaunchedEffect(exoPlayer) {
        while (true) {
            if (exoPlayer.isPlaying) {
                currentTime = exoPlayer.currentPosition
                totalTime = exoPlayer.duration.takeIf { it > 0 } ?: totalTime
                isBuffering = exoPlayer.playbackState == ExoPlayer.STATE_BUFFERING
            }
            delay(200)
        }
    }

    // Liberar ExoPlayer al desmontar
    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }

    // Pantalla completa
    LaunchedEffect(isFullScreen) {
        val activity = context as? Activity
        activity?.let {
            WindowCompat.setDecorFitsSystemWindows(it.window, !isFullScreen)
        }
    }
    BackHandler(enabled = isFullScreen) {
        isFullScreen = false
    }

    // Función para ajustar el brillo
    fun adjustBrightness(value: Float) {
        val activity = context as? Activity
        activity?.let {
            val window = it.window
            val layoutParams = window.attributes
            layoutParams.screenBrightness = value.coerceIn(0.01f, 1f)
            window.attributes = layoutParams
        }
    }

    // Animación de los controles
    LaunchedEffect(showControls) {
        if (showControls) {
            controlsAlpha.animateTo(1f, animationSpec = tween(300))
        } else {
            controlsAlpha.animateTo(0f, animationSpec = tween(300))
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .pointerInput(Unit) {
                detectTapGestures(
                    onDoubleTap = { offset ->
                        val screenWidth = size.width
                        val isLeftSide = offset.x < screenWidth / 2
                        
                        scope.launch {
                            showForwardRewind = true
                            forwardRewindAmount = if (isLeftSide) -10000L else 10000L
                            forwardRewindScale.animateTo(1.2f, animationSpec = spring())
                            
                            val newPosition = (currentTime + forwardRewindAmount)
                                .coerceIn(0L, totalTime)
                            currentTime = newPosition
                            exoPlayer.seekTo(newPosition)
                            
                            delay(1000)
                            forwardRewindScale.animateTo(1f)
                            showForwardRewind = false
                        }
                    }
                )
            }
            .pointerInput(Unit) {
                detectVerticalDragGestures(
                    onVerticalDrag = { change, dragAmount ->
                        val screenWidth = size.width
                        val x = change.position.x
                        
                        if (x < screenWidth / 3) {
                            // Control de brillo (lado izquierdo)
                            gestureType = "Brillo"
                            brightness = (brightness + dragAmount / 1000f).coerceIn(0f, 1f)
                            adjustBrightness(brightness)
                            gestureValue = brightness
                        } else if (x > screenWidth * 2 / 3) {
                            // Control de volumen (lado derecho)
                            gestureType = "Volumen"
                            volume = (volume - dragAmount / 1000f).coerceIn(0f, 1f)
                            exoPlayer.volume = volume
                            gestureValue = volume
                        }
                        showGestureIndicator = true
                    }
                )
            }
            .pointerInput(Unit) {
                detectHorizontalDragGestures(
                    onHorizontalDrag = { change, dragAmount ->
                        gestureType = "Tiempo"
                        val newPosition = (currentTime + dragAmount * 1000).coerceIn(0f, totalTime.toFloat())
                        currentTime = newPosition.toLong()
                        exoPlayer.seekTo(currentTime)
                        gestureValue = newPosition / totalTime
                        showGestureIndicator = true
                    }
                )
            }
    ) {
        // Área del video con ExoPlayer
        AndroidView(
            factory = { ctx ->
                androidx.media3.ui.PlayerView(ctx).apply {
                    player = exoPlayer
                    useController = false
                    resizeMode = androidx.media3.ui.AspectRatioFrameLayout.RESIZE_MODE_FIT
                }
            },
            modifier = Modifier
                .fillMaxSize()
                .clickable { showControls = !showControls }
        )

        // Indicador de adelantar/retroceder
        AnimatedVisibility(
            visible = showForwardRewind,
            enter = fadeIn() + scaleIn(),
            exit = fadeOut() + scaleOut()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.scale(forwardRewindScale.value)
                ) {
                    Icon(
                        imageVector = if (forwardRewindAmount < 0) 
                            Icons.Default.Replay10 else Icons.Default.Forward10,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(48.dp)
                    )
                    Text(
                        text = formatTime(currentTime),
                        color = Color.White,
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }
        }

        // Indicador de gesto
        if (showGestureIndicator) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = gestureType,
                        color = Color.White,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = when (gestureType) {
                            "Tiempo" -> formatTime(currentTime)
                            else -> "${(gestureValue * 100).toInt()}%"
                        },
                        color = Color.White,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }

        // Controles del reproductor con animación
        AnimatedVisibility(
            visible = showControls,
            enter = fadeIn() + slideInVertically(),
            exit = fadeOut() + slideOutVertically()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f))
                    .alpha(controlsAlpha.value)
            ) {
                // Barra superior
                TopAppBar(
                    title = {
                        Text(title, color = Color.White)
                    },
                    navigationIcon = {
                        IconButton(onClick = { navController.navigateUp() }) {
                            Icon(
                                Icons.Default.ArrowBack,
                                contentDescription = "Volver",
                                tint = Color.White
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = { isFullScreen = !isFullScreen }) {
                            Icon(
                                if (isFullScreen) Icons.Default.FullscreenExit else Icons.Default.Fullscreen,
                                contentDescription = "Pantalla completa",
                                tint = Color.White
                            )
                        }
                        IconButton(onClick = { /* TODO: Más opciones */ }) {
                            Icon(
                                Icons.Default.MoreVert,
                                contentDescription = "Más opciones",
                                tint = Color.White
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent
                    )
                )

                Spacer(modifier = Modifier.weight(1f))

                // Indicador de buffering
                if (isBuffering) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }

                // Controles centrales
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = {
                        exoPlayer.seekTo((exoPlayer.currentPosition - 10000).coerceAtLeast(0))
                    }) {
                        Icon(
                            Icons.Default.Replay10,
                            contentDescription = "Retroceder 10s",
                            tint = Color.White
                        )
                    }

                    IconButton(
                        onClick = {
                            isPlaying = !isPlaying
                            exoPlayer.playWhenReady = isPlaying
                        },
                        modifier = Modifier.size(64.dp)
                    ) {
                        Icon(
                            if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = if (isPlaying) "Pausar" else "Reproducir",
                            tint = Color.White,
                            modifier = Modifier.size(48.dp)
                        )
                    }

                    IconButton(onClick = {
                        exoPlayer.seekTo((exoPlayer.currentPosition + 10000).coerceAtMost(exoPlayer.duration))
                    }) {
                        Icon(
                            Icons.Default.Forward10,
                            contentDescription = "Avanzar 10s",
                            tint = Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Barra de progreso
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Slider(
                        value = currentTime.toFloat(),
                        onValueChange = {
                            currentTime = it.toLong()
                            exoPlayer.seekTo(currentTime)
                        },
                        valueRange = 0f..(totalTime.takeIf { it > 0 }?.toFloat() ?: 1f),
                        colors = SliderDefaults.colors(
                            thumbColor = MaterialTheme.colorScheme.primary,
                            activeTrackColor = MaterialTheme.colorScheme.primary
                        )
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = formatTime(currentTime),
                            color = Color.White
                        )
                        Text(
                            text = formatTime(totalTime),
                            color = Color.White
                        )
                    }
                }

                // Control de volumen
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.VolumeDown, contentDescription = "Bajar volumen", tint = Color.White)
                    Slider(
                        value = volume,
                        onValueChange = {
                            volume = it
                            exoPlayer.volume = it
                        },
                        valueRange = 0f..1f,
                        modifier = Modifier.weight(1f),
                        colors = SliderDefaults.colors(
                            thumbColor = MaterialTheme.colorScheme.primary,
                            activeTrackColor = MaterialTheme.colorScheme.primary
                        )
                    )
                    Icon(Icons.Default.VolumeUp, contentDescription = "Subir volumen", tint = Color.White)
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        // Ocultar controles tras 3 segundos de inactividad
        LaunchedEffect(isPlaying, showControls) {
            if (isPlaying && showControls) {
                delay(3000)
                showControls = false
            }
        }
    }
}

private fun formatTime(timeMs: Long): String {
    val totalSeconds = timeMs / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return String.format("%02d:%02d", minutes, seconds)
} 