package com.example.bloom.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String = "",
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
    onTrailingIconClick: (() -> Unit)? = null,
    enabled: Boolean = true,
    isError: Boolean = false,
    errorMessage: String? = null,
    singleLine: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    visualTransformation: VisualTransformation =
        VisualTransformation.None,
    textStyle: TextStyle = TextStyle(
        fontSize = 16.sp,
        fontWeight = FontWeight.Medium,
        color = Color(0xFF252238)
    ),
    focusedColor: Color = MaterialTheme.colorScheme.primary,
    unfocusedColor: Color = Color(0xFFE5E5EF),
    backgroundColor: Color = Color.White,
    errorColor: Color = Color(0xFFE53935)
) {
    var isFocused by remember { mutableStateOf(false) }

    val targetBorderColor = when {
        isError -> errorColor
        isFocused -> focusedColor
        else -> unfocusedColor
    }

    val borderColor by animateColorAsState(
        targetValue = targetBorderColor,
        label = "textFieldBorder"
    )

    val shape = RoundedCornerShape(12.dp)

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        if (label != null) {
            Text(
                text = label,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF252238),
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .clip(shape)
                .background(
                    if (enabled) backgroundColor
                    else Color(0xFFF2F2F2)
                )
                .border(
                    width = 1.5.dp,
                    color = if (enabled) borderColor
                    else Color(0xFFD0D0D0),
                    shape = shape
                )
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            if (leadingIcon != null) {
                Icon(
                    imageVector = leadingIcon,
                    contentDescription = null,
                    tint = if (isError) errorColor
                    else Color(0xFF777787),
                    modifier = Modifier.size(22.dp)
                )

                Spacer(modifier = Modifier.size(10.dp))
            }

            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.CenterStart
            ) {
                if (value.isEmpty() && placeholder.isNotEmpty()) {
                    Text(
                        text = placeholder,
                        fontSize = 16.sp,
                        color = Color(0xFF9999A8)
                    )
                }

                BasicTextField(
                    value = value,
                    onValueChange = onValueChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .onFocusChanged {
                            isFocused = it.isFocused
                        },
                    enabled = enabled,
                    singleLine = singleLine,
                    textStyle = textStyle,
                    keyboardOptions = keyboardOptions,
                    visualTransformation = visualTransformation,
                    cursorBrush = SolidColor(focusedColor)
                )
            }

            if (trailingIcon != null) {
                Spacer(modifier = Modifier.size(4.dp))

                if (onTrailingIconClick != null) {
                    IconButton(
                        onClick = onTrailingIconClick,
                        enabled = enabled,
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            imageVector = trailingIcon,
                            contentDescription = "Aksi input",
                            tint = Color(0xFF777787),
                            modifier = Modifier.size(22.dp)
                        )
                    }
                } else {
                    Icon(
                        imageVector = trailingIcon,
                        contentDescription = null,
                        tint = Color(0xFF777787),
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
        }

        if (isError && errorMessage != null) {
            Text(
                text = errorMessage,
                color = errorColor,
                fontSize = 12.sp,
                modifier = Modifier.padding(
                    start = 4.dp,
                    top = 6.dp
                )
            )
        }
    }
}
