package com.saji.captioneditor.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.saji.captioneditor.ui.theme.SajiColors

@Composable
fun sajiTextFieldColors(): TextFieldColors = OutlinedTextFieldDefaults.colors(
    focusedTextColor = SajiColors.TextPrimary,
    unfocusedTextColor = SajiColors.TextPrimary,
    focusedBorderColor = SajiColors.Violet,
    unfocusedBorderColor = SajiColors.BorderFaint,
    cursorColor = SajiColors.Pink,
    focusedContainerColor = SajiColors.Card,
    unfocusedContainerColor = SajiColors.Card,
    focusedPlaceholderColor = SajiColors.TextSecondary,
    unfocusedPlaceholderColor = SajiColors.TextSecondary,
    focusedLeadingIconColor = SajiColors.Violet,
    unfocusedLeadingIconColor = SajiColors.TextSecondary
)

@Composable
fun SearchField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
        singleLine = true,
        shape = RoundedCornerShape(50),
        placeholder = { Text(placeholder) },
        leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
        colors = sajiTextFieldColors()
    )
}
