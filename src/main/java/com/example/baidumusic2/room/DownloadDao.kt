package com.example.baidumusic2.room

import androidx.room.*


@Dao
interface DownloadDao {

    @Insert(onConflict =OnConflictStrategy.REPLACE)
    fun insertAll(vararg downloadEntity: DownloadEntity)


    @Delete
    fun deleteAll(vararg downloadEntity: DownloadEntity)

    @Update
    fun updateAll(vararg downloadEntity: DownloadEntity)

    //查询全部
    @Query("SELECT * FROM Download")
    fun queryAll():List<DownloadEntity>

    //根据id查义
//    @Query("SELECT * FROM Download  WHERE id == :id")
//    fun queryId(id:String):List<DownloadEntity>


    @Query("SELECT * FROM Download  WHERE downloadId == :id")
    fun queryId(id:String):DownloadEntity?


    //查询未完成的不包括初始化的init状态
    @Query("SELECT * FROM Download  WHERE status !=3&status!=-1")
    fun queryUnfinished():List<DownloadEntity>

    //查询已完成的
    @Query("SELECT * FROM Download  WHERE status == 3")
    fun queryFinish():List<DownloadEntity>

}


