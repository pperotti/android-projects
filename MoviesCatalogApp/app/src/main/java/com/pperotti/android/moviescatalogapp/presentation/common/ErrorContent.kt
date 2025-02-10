package com.pperotti.android.moviescatalogapp.presentation.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pperotti.android.moviescatalogapp.R

@Composable
fun ErrorContent(message: String?, modifier: Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = message ?: stringResource(R.string.error_no_description),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium,
            fontSize = 14.sp,
            modifier = Modifier.fillMaxWidth()
        )
    }
}