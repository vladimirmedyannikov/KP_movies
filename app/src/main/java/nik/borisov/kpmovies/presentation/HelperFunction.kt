package nik.borisov.kpmovies.presentation.adapters

import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import nik.borisov.kpmovies.R
import nik.borisov.kpmovies.domain.ReviewType
import java.text.SimpleDateFormat
import java.util.*


fun setupRating(textView: TextView, rating: Double) {
    val backgroundId = if (rating >= 7) {
        R.drawable.rating_green
    } else if (rating < 5) {
        R.drawable.rating_red
    } else {
        R.drawable.rating_gray
    }
    val background = ContextCompat.getDrawable(textView.context, backgroundId)
    textView.background = background
    val ratingText = "%.1f".format(rating)
    textView.text = ratingText
    textView.visibility = TextView.VISIBLE
}

fun setupReviewType(imageView: ImageView, type: ReviewType) {
    val backgroundId = when (type) {
        ReviewType.TYPE_POSITIVE -> {
            R.drawable.rating_green
        }
        ReviewType.TYPE_NEGATIVE -> {
            R.drawable.rating_red
        }
        ReviewType.TYPE_NEUTRAL -> {
            R.drawable.rating_gray
        }
    }
    val background = ContextCompat.getDrawable(imageView.context, backgroundId)
    imageView.background = background
}

fun setupReviewDate(textView: TextView, date: String) {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
    val outputFormat = SimpleDateFormat("dd.MM.yyyy", Locale.US)
    val input = inputFormat.parse(date)
    val output = outputFormat.format(input)
    textView.text = output
}