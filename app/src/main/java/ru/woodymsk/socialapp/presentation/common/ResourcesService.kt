package ru.woodymsk.socialapp.presentation.common

import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.text.SpannedString
import androidx.annotation.DrawableRes
import androidx.annotation.FontRes
import androidx.compose.ui.text.font.Typeface
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.text.HtmlCompat
import javax.inject.Inject

class ResourcesService @Inject constructor(private val context: Context) {
    private val resources: Resources = context.resources

    fun getString(resId: Int, vararg params: Any?): String =
        if (params.isNotEmpty()) {
            context.getString(resId, *params)
        } else {
            context.getString(resId)
        }

    fun getInt(resId: Int): Int = resources.getInteger(resId)

    fun getText(resId: Int, vararg params: Any?): CharSequence =
        if (params.isNotEmpty()) {
            val spanned = SpannedString.valueOf(context.getText(resId))
            val htmlText =
                HtmlCompat.toHtml(spanned, HtmlCompat.TO_HTML_PARAGRAPH_LINES_CONSECUTIVE)
            val formattedText = String.format(htmlText, *params)
            HtmlCompat.fromHtml(formattedText, HtmlCompat.FROM_HTML_MODE_LEGACY).trimEnd('\n')
        } else {
            context.getText(resId)
        }

    fun getTextWithoutFormat(resId: Int, vararg params: Any?): CharSequence =
        if (params.isNotEmpty()) {
            HtmlCompat.fromHtml(context.getString(resId, *params), HtmlCompat.FROM_HTML_MODE_LEGACY)
        } else {
            context.getText(resId)
        }

    fun getQuantityString(resId: Int, quantity: Int, vararg params: Any?): String =
        if (params.isNotEmpty()) {
            resources.getQuantityString(resId, quantity)
        } else {
            resources.getQuantityString(resId, quantity, *params)
        }

    fun getStringArray(resId: Int): Array<String> = resources.getStringArray(resId)

    fun getColor(resId: Int) = ContextCompat.getColor(context, resId)

    fun getDimension(dimenId: Int): Int = resources.getDimension(dimenId).toInt()

    fun getFont(@FontRes font: Int): Typeface? = ResourcesCompat.getFont(context, font) as Typeface?

    fun getLargeFontSize(): Boolean = resources.configuration.fontScale > 1

    fun getDrawable(@DrawableRes drawable: Int): Drawable? =
        ContextCompat.getDrawable(context, drawable)
}