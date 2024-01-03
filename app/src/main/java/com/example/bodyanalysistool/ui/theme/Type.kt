package com.example.bodyanalysistool.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.EmojiSupportMatch
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.example.bodyanalysistool.R

val provider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)


private val merriweather = GoogleFont("Merriweather")

val merriweatherFontFamily = FontFamily(
    Font(googleFont = merriweather, fontProvider = provider)
)

val kanitFontFamily = FontFamily(
    Font(R.font.kanit_black, FontWeight.Black),
    Font(R.font.kanit_bold, FontWeight.Bold),
    Font(R.font.kanit_extrabold, FontWeight.ExtraBold),
    Font(R.font.kanit_light, FontWeight.Light),
    Font(R.font.kanit_semibold, FontWeight.SemiBold),
    Font(R.font.kanit_regular, FontWeight.Normal),
    Font(R.font.kanit_thin, FontWeight.Thin),
    Font(R.font.kanit_extralight, FontWeight.ExtraLight)
)

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = kanitFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = kanitFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    titleLarge = TextStyle(
        fontFamily = kanitFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 25.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.2.sp,
        platformStyle = PlatformTextStyle(
            emojiSupportMatch = EmojiSupportMatch.None
        ),
        lineBreak = LineBreak.Heading,
        textAlign = TextAlign.Center,
        color = Brown,
        background = Color.White
    )
)









