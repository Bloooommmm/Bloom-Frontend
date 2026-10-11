package com.example.bloom.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

enum class IconPosition {
    Start,
    End
}

@Composable
fun CustomButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,
    shadowColor: Color = MaterialTheme.colorScheme.secondary,
    enabled: Boolean = true,
    icon: ImageVector? = null,
    iconPosition: IconPosition = IconPosition.Start
) {
    var pressed by remember { mutableStateOf(false) }

    // Animasi gerakan tombol ketika ditekan
    val buttonOffset by animateDpAsState(
        targetValue = if (pressed && enabled) 4.dp else 0.dp,
        animationSpec = spring(
            dampingRatio = 0.7f,
            stiffness = 500f
        ),
        label = "buttonOffset"
    )

    // Menentukan warna tombol
    val targetColor = when {
        !enabled -> Color(0xFFBDBDBD)
        pressed -> shadowColor
        else -> color
    }

    val actualColor by animateColorAsState(
        targetValue = targetColor,
        animationSpec = spring(
            dampingRatio = 0.7f,
            stiffness = 500f
        ),
        label = "buttonColor"
    )

    // Warna bayangan tombol
    val actualShadow = if (enabled) {
        shadowColor
    } else {
        Color(0xFF999999)
    }

    // Lapisan luar untuk bayangan
    Box(
        modifier = modifier
            .height(56.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(actualShadow)
    ) {
        // Lapisan dalam untuk tombol utama
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .offset(y = buttonOffset)
                .clip(RoundedCornerShape(16.dp))
                .background(actualColor)
                .pointerInput(enabled) {
                    if (!enabled) {
                        return@pointerInput
                    }

                    detectTapGestures(
                        onPress = {
                            pressed = true

                            try {
                                awaitRelease()
                                onClick()
                            } finally {
                                pressed = false
                            }
                        }
                    )
                },
            contentAlignment = Alignment.Center
        ) {
            // Baris untuk ikon dan teks
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                if (icon != null && iconPosition == IconPosition.Start) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )

                    Spacer(modifier = Modifier.size(8.dp))
                }

                Text(
                    text = text,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                if (icon != null && iconPosition == IconPosition.End) {
                    Spacer(modifier = Modifier.size(8.dp))

                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}
