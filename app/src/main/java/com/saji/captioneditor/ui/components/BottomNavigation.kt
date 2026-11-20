package com.saji.captioneditor.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.PlayCircleOutline
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.saji.captioneditor.ui.theme.SajiColors

enum class MainTab(val route: String, val label: String, val icon: ImageVector) {
    Home("home", "Home", Icons.Filled.Home),
    Projects("projects", "Projects", Icons.Outlined.PlayCircleOutline),
    Premium("premium", "Premium", Icons.Filled.WorkspacePremium),
    Tools("tools", "Tools", Icons.Outlined.GridView),
    Settings("settings", "Settings", Icons.Outlined.Settings)
}

/**
 * Bottom navigation matching the reference: Home, Projects, raised crown (Premium), Tools, Settings.
 * The active item gets an animated violet pill.
 */
@Composable
fun SajiBottomNavigation(
    selected: MainTab?,
    onSelect: (MainTab) -> Unit,
    modifier: Modifier = Modifier
) {
    val barShape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
    Box(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(SajiColors.Surface, barShape)
                .border(BorderStroke(1.5.dp, SajiColors.BorderGradient), barShape)
                .padding(horizontal = 6.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            MainTab.values().forEach { tab ->
                if (tab == MainTab.Premium) {
                    Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                        Box(
                            modifier = Modifier
                                .offset(y = (-18).dp)
                                .size(62.dp)
                                .clip(CircleShape)
                                .background(SajiColors.PrimaryGradient, CircleShape)
                                .border(BorderStroke(2.dp, SajiColors.Border), CircleShape)
                                .pressScale(onClick = { onSelect(tab) }),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(tab.icon, contentDescription = tab.label, tint = SajiColors.Gold, modifier = Modifier.size(30.dp))
                        }
                    }
                } else {
                    val active = selected == tab
                    val pill by animateColorAsState(
                        if (active) SajiColors.Purple.copy(alpha = 0.45f) else SajiColors.Surface.copy(alpha = 0f),
                        label = "navPill"
                    )
                    val tint by animateColorAsState(
                        if (active) SajiColors.TextPrimary else SajiColors.TextSecondary,
                        label = "navTint"
                    )
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(18.dp))
                            .background(pill)
                            .pressScale(pressedScale = 0.92f, onClick = { onSelect(tab) })
                            .padding(vertical = 8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            if (tab == MainTab.Home && !active) Icons.Outlined.Home else tab.icon,
                            contentDescription = tab.label,
                            tint = tint,
                            modifier = Modifier.size(24.dp)
                        )
                        Text(tab.label, style = MaterialTheme.typography.labelSmall, color = tint)
                    }
                }
            }
        }
    }
}
