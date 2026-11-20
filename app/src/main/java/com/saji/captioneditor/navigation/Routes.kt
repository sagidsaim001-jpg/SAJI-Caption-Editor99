package com.saji.captioneditor.navigation

/** Every route in the app. Screens are registered in SajiNavHost as they are built. */
object Routes {
    // Bottom navigation
    const val Home = "home"
    const val Projects = "projects"
    const val Premium = "premium"
    const val Tools = "tools"
    const val Settings = "settings"

    // Project flow
    const val Gallery = "gallery"
    const val NewProject = "new_project"
    const val Editor = "editor/{id}"
    fun editor(id: Int) = "editor/$id"

    // Editor tool screens
    const val TextEditor = "text_editor"
    const val TextStyles = "text_styles"
    const val Fonts = "fonts"
    const val TextAnimation = "text_animation"
    const val TextShadow = "text_shadow"
    const val MoreText = "more_text"
    const val Timing = "timing"
    const val Split = "split"

    // Feature screens
    const val Templates = "templates"
    const val TemplateDetail = "template/{id}"
    fun templateDetail(id: Int) = "template/$id"
    const val AutoCaptions = "auto_captions"
    const val LanguageSelect = "language_select"
    const val CaptionStyle = "caption_style"
    const val Effects = "effects"
    const val Music = "music"
    const val AudioSettings = "audio_settings"
    const val AspectRatio = "aspect_ratio"
    const val Export = "export"
    const val ExportProgress = "export_progress"

    // Settings sub-screens
    const val SettingsAppearance = "settings/appearance"
    const val SettingsLanguage = "settings/language"
    const val SettingsDefaultProject = "settings/default_project"
    const val SettingsAspect = "settings/default_aspect"
    const val SettingsResolution = "settings/default_resolution"
    const val SettingsTimeline = "settings/timeline"
    const val SettingsPlayback = "settings/playback"
    const val SettingsStorage = "settings/storage"
    const val SettingsAbout = "settings/about"
    const val SettingsPrivacy = "settings/privacy"
    const val SettingsHelp = "settings/help"
    const val SettingsSubscription = "settings/subscription"
    const val SettingsBenefits = "settings/benefits"
    const val SettingsRestore = "settings/restore"
}
