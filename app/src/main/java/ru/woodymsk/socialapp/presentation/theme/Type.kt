package ru.woodymsk.socialapp.presentation.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import ru.woodymsk.socialapp.R

val robotoFamily = FontFamily(
    Font(R.font.roboto_black, FontWeight.Black),
    Font(R.font.roboto_bold, FontWeight.Bold),
    Font(R.font.roboto_light, FontWeight.Light),
    Font(R.font.roboto_medium, FontWeight.Medium),
    Font(R.font.roboto_regular, FontWeight.W400),
    Font(R.font.roboto_thin, FontWeight.Thin),
)

private val SocialAppTypography = Typography()
@Composable
fun typography() = Typography(
    titleMedium = SocialAppTypography.titleMedium.copy(
        fontFamily = robotoFamily,
        fontWeight = FontWeight.W500,
        fontSize = 16.sp,
    ),
    bodyLarge = SocialAppTypography.bodyLarge.copy(
        fontFamily = robotoFamily,
        fontWeight = FontWeight.W400,
        fontSize = 16.sp,
    ),
    bodyMedium = SocialAppTypography.bodyMedium.copy(
        fontFamily = robotoFamily,
        fontWeight = FontWeight.W400,
        fontSize = 14.sp,
    ),
    labelLarge = SocialAppTypography.labelLarge.copy(
        fontSize = 14.sp,
        fontFamily = robotoFamily,
        fontWeight = FontWeight.W500,
        color = colorResource(id = R.color.purple_typography_label_large)
    ),
)