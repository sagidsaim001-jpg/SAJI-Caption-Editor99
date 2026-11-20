package com.saji.captioneditor.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.compose.rememberNavController
import com.saji.captioneditor.ui.AppViewModel
import com.saji.captioneditor.ui.components.MainTab
import com.saji.captioneditor.ui.components.SajiBottomNavigation
import com.saji.captioneditor.ui.components.ToastHost
import com.saji.captioneditor.ui.screens.gallery.MediaGalleryScreen
import com.saji.captioneditor.ui.screens.gallery.NewProjectScreen
import com.saji.captioneditor.ui.screens.home.HomeScreen
import com.saji.captioneditor.ui.screens.editor.EditorScreen
import com.saji.captioneditor.ui.screens.features.FeatureScreen
import com.saji.captioneditor.ui.screens.projects.ProjectsScreen
import com.saji.captioneditor.ui.screens.tools.ToolsScreen
import com.saji.captioneditor.ui.theme.SajiColors

@Composable
fun SajiApp() {
    val nav = rememberNavController()
    val vm: AppViewModel = viewModel()
    val backStack by nav.currentBackStackEntryAsState()
    val route = backStack?.destination?.route
    val currentTab = MainTab.values().firstOrNull { it.route == route }

    fun navigateTab(tabRoute: String) {
        nav.navigate(tabRoute) {
            popUpTo(Routes.Home) { saveState = true }
            launchSingleTop = true
            restoreState = true
        }
    }

    val onNavigate: (String) -> Unit = { target ->
        if (MainTab.values().any { it.route == target }) navigateTab(target) else nav.navigate(target)
    }
    val onBack: () -> Unit = { nav.popBackStack() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SajiColors.Bg)
    ) {
        NavHost(
            navController = nav,
            startDestination = Routes.Home,
            modifier = Modifier.fillMaxSize(),
            enterTransition = { fadeIn(tween(260)) + slideInHorizontally(tween(260)) { it / 8 } },
            exitTransition = { fadeOut(tween(200)) },
            popEnterTransition = { fadeIn(tween(260)) },
            popExitTransition = { fadeOut(tween(200)) + slideOutHorizontally(tween(260)) { it / 8 } }
        ) {
            composable(Routes.Home) { HomeScreen(vm, onNavigate) }
            composable(Routes.Projects) { ProjectsScreen(vm, onNavigate) }
            composable(Routes.Tools) { ToolsScreen(onNavigate) }
            composable(Routes.Gallery) {
                MediaGalleryScreen(vm, onBack = onBack, onNext = { nav.navigate(Routes.NewProject) })
            }
            composable(Routes.NewProject) {
                NewProjectScreen(
                    vm,
                    onBack = onBack,
                    onCreated = { id ->
                        nav.navigate(Routes.editor(id)) { popUpTo(Routes.Home) }
                    }
                )
            }

            composable(
                route = Routes.Editor,
                arguments = listOf(navArgument("id") { type = NavType.IntType })
            ) { entry ->
                EditorScreen(
                    vm = vm,
                    projectId = entry.arguments?.getInt("id") ?: -1,
                    onBack = onBack,
                    onOpenTool = { key ->
                        val target = when (key) {
                            "music" -> Routes.Music
                            "audio_settings" -> Routes.AudioSettings
                            "auto_captions" -> Routes.AutoCaptions
                            else -> Routes.Export
                        }
                        nav.navigate(target)
                    }
                )
            }

            val featureRoutes = listOf(
                Routes.Premium to "SAJI PRO",
                Routes.Settings to "Settings",
                Routes.TextEditor to "Text Editor",
                Routes.TextStyles to "Text Styles",
                Routes.Fonts to "Fonts",
                Routes.TextAnimation to "Animation",
                Routes.TextShadow to "Shadow",
                Routes.MoreText to "More Text Tools",
                Routes.Timing to "Caption Timing",
                Routes.Split to "Split Clip",
                Routes.Templates to "Templates",
                Routes.AutoCaptions to "Auto Captions",
                Routes.LanguageSelect to "Language",
                Routes.CaptionStyle to "Caption Style",
                Routes.Effects to "Effects",
                Routes.Music to "Music",
                Routes.AudioSettings to "Audio Settings",
                Routes.AspectRatio to "Aspect Ratio",
                Routes.Export to "Export",
                Routes.ExportProgress to "Export Progress",
                Routes.SettingsAppearance to "Appearance",
                Routes.SettingsLanguage to "Language",
                Routes.SettingsDefaultProject to "Default Project",
                Routes.SettingsAspect to "Default Aspect Ratio",
                Routes.SettingsResolution to "Default Resolution",
                Routes.SettingsTimeline to "Timeline Settings",
                Routes.SettingsPlayback to "Playback Settings",
                Routes.SettingsStorage to "Storage",
                Routes.SettingsAbout to "About SAJI",
                Routes.SettingsPrivacy to "Privacy",
                Routes.SettingsHelp to "Help & Support",
                Routes.SettingsSubscription to "Subscription",
                Routes.SettingsBenefits to "PRO Benefits",
                Routes.SettingsRestore to "Restore Purchases"
            )
            featureRoutes.forEach { (routeName, screenTitle) ->
                composable(routeName) {
                    FeatureScreen(title = screenTitle, vm = vm, onBack = onBack)
                }
            }
            composable(
                route = Routes.TemplateDetail,
                arguments = listOf(navArgument("id") { type = NavType.IntType })
            ) {
                FeatureScreen(title = "Template Details", vm = vm, onBack = onBack)
            }
        }

        AnimatedVisibility(
            visible = currentTab != null,
            modifier = Modifier.align(Alignment.BottomCenter),
            enter = slideInVertically(tween(220)) { it } + fadeIn(),
            exit = slideOutVertically(tween(220)) { it } + fadeOut()
        ) {
            SajiBottomNavigation(selected = currentTab, onSelect = { navigateTab(it.route) })
        }

        ToastHost(
            message = vm.toast,
            onDone = { vm.toast = null },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = if (currentTab != null) 112.dp else 48.dp)
        )
    }
}
