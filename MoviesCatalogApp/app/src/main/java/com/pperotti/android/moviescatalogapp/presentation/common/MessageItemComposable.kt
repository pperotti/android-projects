package com.pperotti.android.moviescatalogapp.presentation.common

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MessageItemComposable(
    @StringRes textRes: Int,
    textValue: String = ""
) {
    val title = LocalContext.current.getString(textRes)
    val annotatedString = buildAnnotatedString {
        withStyle(
            style = MaterialTheme
                .typography
                .bodyMedium
                .toSpanStyle()
                .copy(fontWeight = FontWeight.Bold, fontSize = 18.sp)
        ) {
            append(title)
        }
        withStyle(
            style = MaterialTheme
                .typography
                .bodyMedium
                .toSpanStyle()
                .copy(fontWeight = FontWeight.Normal, fontSize = 18.sp)
        ) {
            append(textValue)
        }
    }
    Text(
        text = annotatedString,
        fontSize = 10.sp,
        modifier = Modifier
            .padding(PaddingValues(horizontal = 8.dp, vertical = 4.dp))
    )
}
