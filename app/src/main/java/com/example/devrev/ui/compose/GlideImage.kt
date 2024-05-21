package com.example.devrev.ui.compose

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import jp.wasabeef.glide.transformations.BlurTransformation

@Composable
fun GlideImage(
    imageUrl: String,
    modifier: Modifier = Modifier,
    requestOptions: RequestOptions = RequestOptions(),//.override(Target.SIZE_ORIGINAL),
    errorPlaceholder: Int? = null, // Resource ID for error placeholder
) {
    AndroidView(
        factory = { context ->
            ImageView(context).apply {
                scaleType = ImageView.ScaleType.CENTER_CROP
            }
        },
        update = { imageView ->
            Glide.with(imageView.context)
                .load(imageUrl)
                .apply(requestOptions)
                .listener(object : RequestListener<Drawable> {
                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                       // onLoadingComplete?.invoke()
                        return false // Return false if the resource should be set on the target
                    }

                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        errorPlaceholder?.let {
                            imageView.setImageResource(it)
                        }
                        return true
                    }
                })
                .into(imageView)
        },
        modifier = modifier
    )
}

@Composable
fun BlurredImage(modifier: Modifier,imageUrl:String) {
    // Use the AndroidView composable to display the ImageView with Glide
    AndroidView(
        modifier = modifier,
        factory = { context ->
            ImageView(context).apply {
                scaleType = ImageView.ScaleType.CENTER_CROP
            }
        },
        update = { imageView ->
            Glide.with(imageView.context)
                .load(imageUrl)
                .apply(RequestOptions.bitmapTransform(BlurTransformation(25, 3)))
                .into(imageView)
        }
    )
}
