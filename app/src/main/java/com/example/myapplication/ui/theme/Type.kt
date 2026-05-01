package com.example.myapplication.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.myapplication.R

// Fraunces is a variable font with axes:
//   wght 100..900   (weight)
//   opsz 9..144     (optical size — design tuned for the rendered point size)
//   SOFT 0..100     (corner softness; 0 = hard, default)
//   WONK 0..1       (alt glyph set; 0 = standard, default)
// The same TTF can render every weight, but Compose still needs one Font(...) per
// weight/style combination so the FontMatcher knows which entry to pick at lookup.
@OptIn(androidx.compose.ui.text.ExperimentalTextApi::class)
private fun fraunces(weight: FontWeight, italic: Boolean) = Font(
    resId = if (italic) R.font.fraunces_italic else R.font.fraunces,
    weight = weight,
    style = if (italic) FontStyle.Italic else FontStyle.Normal,
    variationSettings = FontVariation.Settings(
        FontVariation.weight(weight.weight),
        FontVariation.Setting("opsz", 14f),   // tuned for 12-20sp UI text
    ),
)

val Fraunces = FontFamily(
    fraunces(FontWeight.Light, italic = false),
    fraunces(FontWeight.Normal, italic = false),
    fraunces(FontWeight.Medium, italic = false),
    fraunces(FontWeight.SemiBold, italic = false),
    fraunces(FontWeight.Bold, italic = false),
    fraunces(FontWeight.ExtraBold, italic = false),
    fraunces(FontWeight.Light, italic = true),
    fraunces(FontWeight.Normal, italic = true),
    fraunces(FontWeight.Medium, italic = true),
    fraunces(FontWeight.SemiBold, italic = true),
    fraunces(FontWeight.Bold, italic = true),
)

val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp,
    ),
)
