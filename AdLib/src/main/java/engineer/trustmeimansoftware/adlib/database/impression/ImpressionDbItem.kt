package engineer.trustmeimansoftware.adlib.database.impression

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Models an Database Entry in Impression Database
 */
@Entity
data class ImpressionDbItem(
    @PrimaryKey val impressionId: String,
    @ColumnInfo(name = "data") val data: String,
    @ColumnInfo(name = "isSent") val isSent: Boolean = false
) {
}