
package id.ac.binus.bloomapp.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import id.ac.binus.bloomapp.R
import id.ac.binus.bloomapp.ui.theme.Primary
import id.ac.binus.bloomapp.ui.theme.TextSecondary

data class BottomNavItem(
    val label: String,
    val icon: Int,
    val activeIcon: Int
)

private val navItems = listOf(
    BottomNavItem(
        label = "Today",
        icon = R.drawable.icon_cycle,
        activeIcon = R.drawable.icon_cycle_active
    ),
    BottomNavItem(
        label = "Calendar",
        icon = R.drawable.icon_calendar,
        activeIcon = R.drawable.icon_calendar_active
    ),
    BottomNavItem(
        label = "Learn",
        icon = R.drawable.icon_learn,
        activeIcon = R.drawable.icon_learn_active
    ),
    BottomNavItem(
        label = "Profile",
        icon = R.drawable.icon_profile,
        activeIcon = R.drawable.icon_profile_active
    )
)

@Composable
fun BloomBottomNavigationBar(
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit
) {
    NavigationBar(
        containerColor = Color.White,
        contentColor = Primary
    ) {
        navItems.forEachIndexed { index, item ->

            val isSelected = selectedIndex == index

            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    onItemSelected(index)
                },
                icon = {
                    Image(
                        painter = painterResource(
                            id = if (isSelected) item.activeIcon else item.icon
                        ),
                        contentDescription = item.label,
                        modifier = Modifier.size(30.dp),
                        colorFilter = ColorFilter.tint(
                            if (isSelected) Primary else TextSecondary
                        )
                    )
                },
                label = {
                    Text(
                        text = item.label,
                        style = if (isSelected) {
                            MaterialTheme.typography.labelLarge
                        } else {
                            MaterialTheme.typography.labelMedium
                        }
                    )
                },
                alwaysShowLabel = true,
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Primary,
                    unselectedIconColor = TextSecondary,
                    selectedTextColor = Primary,
                    unselectedTextColor = TextSecondary,
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}
