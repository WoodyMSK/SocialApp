package ru.woodymsk.socialapp.data.media

import android.media.MediaMetadataRetriever
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import ru.woodymsk.socialapp.domain.common.model.AttachmentMetadata
import ru.woodymsk.socialapp.domain.formatVideoDuration
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

private const val EXTRACTION_TIMEOUT_MS = 5_000L

class AudioMetadataExtractor @Inject constructor() {

    suspend fun extract(audioUri: String): AttachmentMetadata? =
        withContext(Dispatchers.IO) {
            withTimeoutOrNull(EXTRACTION_TIMEOUT_MS.milliseconds) {
                if (!audioUri.startsWith("http")) return@withTimeoutOrNull null

                val retriever = MediaMetadataRetriever()
                try {
                    retriever.setDataSource(audioUri, HashMap())

                    val durationMs = retriever.extractMetadata(
                        MediaMetadataRetriever.METADATA_KEY_DURATION
                    )?.toLongOrNull() ?: 0

                    AttachmentMetadata(
                        title = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE),
                        artist = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST),
                        album = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM),
                        duration = formatVideoDuration(durationMs),
                        author = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_AUTHOR),
                        composer = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_COMPOSER),
                        genre = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_GENRE),
                    )
                } catch (e: Exception) {
                    null
                } finally {
                    retriever.release()
                }
            }
        }
}
