package com.saji.captioneditor.ui.screens.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ClosedCaption
import androidx.compose.material.icons.filled.Crop
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material.icons.filled.Folder
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.saji.captioneditor.model.Project
import com.saji.captioneditor.navigation.Routes
import com.saji.captioneditor.ui.AppViewModel
import com.saji.captioneditor.ui.components.EmptyState
import com.saji.captioneditor.ui.components.GradientCard
import com.saji.captioneditor.ui.components.HeroIllustration
import com.saji.captioneditor.ui.components.ProjectCard
import com.saji.captioneditor.ui.components.ProjectMenuHost
import com.saji.captioneditor.ui.components.SajiBottomSheet
import com.saji.captioneditor.ui.components.SajiLogo
import com.saji.captioneditor.ui.components.SettingsItem
import com.saji.captioneditor.ui.components.ToolCard
import com.saji.captioneditor.ui.components.pressScale
import com.saji.captioneditor.ui.theme.SajiColors

private data class HomeTool(val icon: ImageVector, val label: String, val route: String)

private val homeTools = listOf(
    HomeTool(Icons.Filled.VideoLibrary, "Templates", Routes.Templates),
    HomeTool(Icons.Filled.ClosedCaption, "Auto Captions", Routes.AutoCaptions),
    HomeTool(Icons.Filled.TextFields, "Text Styles", Routes.TextStyles),
    HomeTool(Icons.Filled.AutoAwesome, "Effects", Routes.Effects),
    HomeTool(Icons.Filled.MusicNote, "Music", Routes.Music),
    HomeTool(Icons.Filled.Crop, "Aspect Ratio", Routes.AspectRatio)
)

@Composable
fun HomeScreen(vm: AppViewModel, onNavigate: (String) -> Unit) {
    var menuOpen by remember { mutableStateOf(false) }
    var menuProject by remember { mutableStateOf<Project?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SajiColors.ScreenGradient)
            .verticalScroll(rememberScrollState())
            .padding(bottom = 120.dp)
    ) {
        HomeHeader(onMenu = { menuOpen = true }, onPro = { onNavigate(Routes.Premium) })

        GradientCard(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
                .height(96.dp),
            onClick = { onNavigate(Routes.Gallery) }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 18.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF1B1560)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Filled.Add, contentDescription = null, tint = Color.White, modifier = Modifier.size(32.dp))
                }
                Spacer(Modifier.width(16.dp))
                Column(Modifier.weight(1f)) {
                    Text("Add Project", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    Text("Select Video or Photo", color = Color.White.copy(alpha = 0.85f), fontSize = 15.sp)
                }
                Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = Color.White, modifier = Modifier.size(30.dp))
            }
        }

        // My Projects
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 24.dp, bottom = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(SajiColors.Purple),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Filled.History, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
            }
            Spacer(Modifier.width(10.dp))
            Text("My Projects", style = MaterialTheme.typography.titleLarge, color = SajiColors.TextPrimary)
            Spacer(Modifier.weight(1f))
            Row(
                modifier = Modifier.pressScale(onClick = { onNavigate(Routes.Projects) }),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("See All", style = MaterialTheme.typography.bodyLarge, color = SajiColors.TextSecondary)
                Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = SajiColors.TextSecondary)
            }
        }

        val shown = vm.projects.take(3)
        if (shown.isEmpty()) {
            EmptyState(
                icon = Icons.Filled.VideoLibrary,
                title = "No projects yet",
                subtitle = "Tap Add Project to start your first caption video.",
                actionText = "Add Project",
                onAction = { onNavigate(Routes.Gallery) }
            )
        } else {
            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                shown.forEach { project ->
                    ProjectCard(
                        project = project,
                        onClick = { onNavigate(Routes.editor(project.id)) },
                        onMenuClick = { menuProject = project },
                        modifier = Modifier.weight(1f)
                    )
                }
                repeat(3 - shown.size) { Spacer(Modifier.weight(1f)) }
            }
        }

        // Premium Tools
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 24.dp, bottom = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Filled.Star, contentDescription = null, tint = SajiColors.Gold, modifier = Modifier.size(30.dp))
            Spacer(Modifier.width(10.dp))
            Text("Premium Tools", style = MaterialTheme.typography.titleLarge, color = SajiColors.TextPrimary)
        }
        Column(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            homeTools.chunked(3).forEach { rowTools ->
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    rowTools.forEach { tool ->
                        ToolCard(
                            icon = tool.icon,
                            label = tool.label,
                            onClick = { onNavigate(tool.route) },
                            modifier = Modifier
                                .weight(1f)
                                .height(88.dp)
                        )
                    }
                }
            }
        }
    }

    if (menuOpen) {
        SajiBottomSheet(onDismiss = { menuOpen = false }) {
            Text("Menu", style = MaterialTheme.typography.titleMedium, color = SajiColors.TextPrimary, modifier = Modifier.padding(bottom = 8.dp))
            SettingsItem(Icons.Filled.WorkspacePremium, "SAJI PRO", subtitle = "Unlock all premium tools", tint = SajiColors.Gold, onClick = { menuOpen = false; onNavigate(Routes.Premium) })
            SettingsItem(Icons.Filled.Folder, "My Projects", onClick = { menuOpen = false; onNavigate(Routes.Projects) })
            SettingsItem(Icons.Filled.Settings, "Settings", onClick = { menuOpen = false; onNavigate(Routes.Settings) })
            SettingsItem(Icons.Filled.Info, "Help & Support", onClick = { menuOpen = false; onNavigate(Routes.SettingsHelp) })
            SettingsItem(Icons.Filled.PrivacyTip, "Privacy Policy", onClick = { menuOpen = false; onNavigate(Routes.SettingsPrivacy) })
            Spacer(Modifier.height(12.dp))
        }
    }

    ProjectMenuHost(
        project = menuProject,
        onDismiss = { menuProject = null },
        vm = vm,
        onOpen = { onNavigate(Routes.editor(it.id)) }
    )
}

@Composable
private fun HomeHeader(onMenu: () -> Unit, onPro: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(350.dp)
    ) {
        HeroIllustration(
            Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(215.dp)
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Row(verticalAlignment = Alignment.Top) {
                Box(
                    modifier = Modifier
                        .padding(top = 12.dp)
                        .pressScale(onClick = onMenu)
                        .size(40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Filled.Menu, contentDescription = "Menu", tint = Color.White, modifier = Modifier.size(30.dp))
                }
                Spacer(Modifier.width(6.dp))
                SajiLogo()
                Spacer(Modifier.width(10.dp))
                Column(Modifier.weight(1f)) {
                    Text("SAJI", color = SajiColors.Pink, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 3.sp)
                    Text("Caption\nEditor", color = Color.White, fontSize = 30.sp, lineHeight = 32.sp, fontWeight = FontWeight.Bold)
                }
                ProBadge(onClick = onPro)
            }
            Spacer(Modifier.height(10.dp))
            Text(
                text = buildAnnotatedString {
                    append("Add Stylish Captions\nMake Your Videos Special ")
                    withStyle(SpanStyle(color = SajiColors.Danger)) { append("♥") }
                },
                color = SajiColors.TextSecondary.copy(alpha = 0.95f),
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun ProBadge(onClick: () -> Unit) {
    val shape = RoundedCornerShape(14.dp)
    Column(
        modifier = Modifier
            .pressScale(onClick = onClick)
            .size(width = 62.dp, height = 70.dp)
            .clip(shape)
            .background(Brush.verticalGradient(listOf(Color(0xFF4A3410), Color(0xFF2A1D08))), shape)
            .border(BorderStroke(1.5.dp, SajiColors.Gold.copy(alpha = 0.7f)), shape),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(Icons.Filled.WorkspacePremium, contentDescription = null, tint = SajiColors.Gold, modifier = Modifier.size(30.dp))
        Text("PRO", color = SajiColors.Gold, fontSize = 16.sp, fontWeight = FontWeight.ExtraBold)
    }
}
