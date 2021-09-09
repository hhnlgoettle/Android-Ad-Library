package engineer.trustmeimansoftware.adlib.database.impression

import androidx.room.*

/**
 * Dao for ImpressionDatabase
 */
@Dao
interface ImpressionDao {
    @Query("SELECT * FROM ImpressionDbItem WHERE isSent == 0")
    fun getUnsent(): List<ImpressionDbItem>

    @Query("SELECT * FROM ImpressionDbItem")
    fun getAll(): List<ImpressionDbItem>

    @Delete
    fun delete(impressionDbItem: ImpressionDbItem)

    @Query("DELETE FROM ImpressionDbItem WHERE impressionId == :impressionId")
    fun deleteById(impressionId: String)

    @Query("DELETE FROM ImpressionDbItem")
    fun deleteAll()

    @Query("SELECT * FROM ImpressionDbItem WHERE impressionId == :impressionId LIMIT 1")
    fun getByImpressionId(impressionId: String): ImpressionDbItem?



    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(impressionDbItem: ImpressionDbItem)


}