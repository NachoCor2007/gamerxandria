package com.austral.gamerxandria.tab

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.austral.gamerxandria.ui.theme.AppSize
import com.austral.gamerxandria.ui.theme.ErrorBackground
import com.austral.gamerxandria.ui.theme.ErrorText

@Composable
fun NotFound(message: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .background(ErrorBackground)
            .padding(AppSize.contentPadding),
        colors = CardDefaults.cardColors(
            containerColor = ErrorBackground
        )
    ) {
        Text(
            text = message,
            color = ErrorText,
            modifier = Modifier.padding(AppSize.contentPadding)
        )
    }
}
