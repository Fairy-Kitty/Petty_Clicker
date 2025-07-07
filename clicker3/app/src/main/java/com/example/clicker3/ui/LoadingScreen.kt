package com.example.clicker3.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.clicker3.R
import androidx.compose.ui.layout.ContentScale

@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Только кружок загрузки по центру, без фона
        CircularProgressIndicator(
            color = Color(0xFF4B2E19), // тёмно-коричневый
            modifier = Modifier
                .size(64.dp)
                .align(Alignment.Center)
        )
    }
} 