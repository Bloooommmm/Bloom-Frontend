
package id.ac.binus.bloomapp.ui.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import id.ac.binus.bloomapp.ui.theme.Primary

@Composable
fun CustomButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = Primary,
    shadowColor: Color = Color(0xFFDC4862),
    contentColor: Color = Color.White,
    enabled: Boolean = true,
    leadingIcon: (@Composable () -> Unit)? = null
) {
    val interactionSource = remember {
        MutableInteractionSource()
    }

    var isPressed by remember { mutableStateOf(false) }

    LaunchedEffect(interactionSource) {
        interactionSource.interactions.collect { interaction ->
            when (interaction) {
                is PressInteraction.Press -> isPressed = true
                is PressInteraction.Release,
                is PressInteraction.Cancel -> isPressed = false
            }
        }
    }

    val offset by animateDpAsState(
        targetValue = if (isPressed) 0.dp else (-4).dp,
        animationSpec = spring(
            dampingRatio = 0.8f,
            stiffness = 600f
        ),
        label = "buttonOffset"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(CircleShape)
            .background(
                if (enabled) shadowColor else Color(0xFFBDBDBD)
            )
    ) {
        Button(
            onClick = onClick,
            enabled = enabled,
            interactionSource = interactionSource,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .align(Alignment.TopCenter)
                .offset(y = offset),
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(
                containerColor = containerColor,
                contentColor = contentColor,
                disabledContainerColor = Color(0xFFBDBDBD),
                disabledContentColor = Color.White
            ),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 0.dp,
                pressedElevation = 0.dp
            )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                if (leadingIcon != null) {
                    leadingIcon()
                    Spacer(modifier = Modifier.width(12.dp))
                }

                Text(
                    text = text,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}
