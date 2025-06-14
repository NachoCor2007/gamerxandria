package com.austral.gamerxandria.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.window.Dialog
import com.austral.gamerxandria.R
import com.austral.gamerxandria.ui.theme.AppSize
import com.austral.gamerxandria.ui.theme.ButtonRed
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShelfCreatorPopUp(
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var text by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier.padding(AppSize.spacingLarge),
                verticalArrangement = Arrangement.spacedBy(AppSize.contentPadding)
            ) {
                Text(
                    text = stringResource(R.string.shelf_creation_add_new),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                OutlinedTextField(
                    singleLine = true,
                    value = text,
                    onValueChange = { text = it },
                    label = { Text(stringResource(R.string.shelf_creation_enter_name)) },
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(
                        onClick = onDismiss
                    ) {
                        Text(stringResource(R.string.shelf_creation_cancel))
                    }

                    Spacer(modifier = Modifier.width(AppSize.spacingSmall))

                    Button(
                        onClick = { onConfirm(text) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ButtonRed
                        )
                    ) {
                        Text(stringResource(R.string.shelf_creation_create))
                    }
                }
            }
        }
    }
}

/**
 * Preview del diálogo de creación de estanterías
 */
@Preview(showBackground = true)
@Composable
fun ShelfCreatorPopUpPreview() {
    MaterialTheme {
        ShelfCreatorPopUp(
            onDismiss = {},
            onConfirm = {}
        )
    }
}
