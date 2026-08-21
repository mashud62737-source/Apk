package com.example.data

import android.content.Context
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

object MediaStorageHelper {

    /**
     * Saves a picked Uri (content:// or file://) directly into internal application storage.
     * Generates a persistent local file URI and extracts a thumbnail if it's a video.
     * Returns Pair(mediaUriString, thumbnailUriString).
     */
    fun saveMediaToInternalStorage(context: Context, sourceUri: Uri, isVideo: Boolean): Pair<String, String> {
        return try {
            val mediaDir = File(context.filesDir, if (isVideo) "videos" else "photos").apply { mkdirs() }
            val thumbDir = File(context.filesDir, "thumbnails").apply { mkdirs() }

            val timestamp = System.currentTimeMillis()
            val ext = if (isVideo) ".mp4" else ".jpg"
            val mediaFile = File(mediaDir, "toktok_media_${timestamp}$ext")

            // Copy input stream to internal file
            context.contentResolver.openInputStream(sourceUri)?.use { input ->
                FileOutputStream(mediaFile).use { output ->
                    input.copyTo(output)
                }
            }

            val mediaFileUri = Uri.fromFile(mediaFile).toString()
            var thumbFileUri = mediaFileUri

            if (isVideo) {
                // Extract video frame for thumbnail
                val thumbFile = File(thumbDir, "thumb_${timestamp}.jpg")
                try {
                    val retriever = MediaMetadataRetriever()
                    retriever.setDataSource(mediaFile.absolutePath)
                    val frame = retriever.getFrameAtTime(1000000) ?: retriever.frameAtTime
                    if (frame != null) {
                        FileOutputStream(thumbFile).use { out ->
                            frame.compress(Bitmap.CompressFormat.JPEG, 85, out)
                        }
                        thumbFileUri = Uri.fromFile(thumbFile).toString()
                    }
                    retriever.release()
                } catch (e: Exception) {
                    // Fallback to sample or media path
                }
            }

            Pair(mediaFileUri, thumbFileUri)
        } catch (e: Exception) {
            // If copying fails for any reason, return the raw URI
            Pair(sourceUri.toString(), sourceUri.toString())
        }
    }

    /**
     * Saves profile photo / NID document image into internal storage.
     */
    fun saveImageToInternalStorage(context: Context, sourceUri: Uri, prefix: String): String {
        return try {
            val imgDir = File(context.filesDir, "profiles_docs").apply { mkdirs() }
            val imgFile = File(imgDir, "${prefix}_${System.currentTimeMillis()}.jpg")

            context.contentResolver.openInputStream(sourceUri)?.use { input ->
                FileOutputStream(imgFile).use { output ->
                    input.copyTo(output)
                }
            }
            Uri.fromFile(imgFile).toString()
        } catch (e: Exception) {
            sourceUri.toString()
        }
    }
}
