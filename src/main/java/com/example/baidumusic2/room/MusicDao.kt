package com.example.baidumusic2.room

import androidx.room.*


@Dao
interface MusicDao {

    @Insert(onConflict =OnConflictStrategy.REPLACE)
    fun insertAll(vararg music:MusicEntity)


    @Delete
    fun deleteAll(vararg music:MusicEntity)

    @Query("DELETE  FROM Music  WHERE id == :id")
    fun deleteId(id: String)

    @Update
    fun updateAll(vararg music: MusicEntity)

    //查询全部
    @Query("SELECT * FROM Music")
    fun queryAll():List<MusicEntity>


    @Query("SELECT * FROM Music  WHERE id == :id")
    fun queryOne(id:String):MusicEntity?

    //查询未完成的
    @Query("SELECT * FROM Music  WHERE status !=3 and status!=-1")
    fun queryUnfinished():List<MusicEntity>

    //查询已完成的
    @Query("SELECT * FROM Music  WHERE status ==3")
    fun queryFinish():List<MusicEntity>

}


