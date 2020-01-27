package com.example.baidumusic2.room

import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import com.example.baidumusic2.MyApp
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.room.migration.Migration



@Database(entities = arrayOf(MusicEntity::class,DownloadEntity::class), version =1)
abstract class AppDatabase:RoomDatabase(){

     abstract fun getMusicDao(): MusicDao

     abstract fun getDownloadDao():DownloadDao


     companion object{

        private var mAppDatabase: AppDatabase? = null


          fun getAppDatabase():AppDatabase{
              if(mAppDatabase==null){
                  mAppDatabase=databaseBuilder(MyApp.getContext(), AppDatabase::class.java, "music.db")
//                          .addMigrations( MIGRATION_1_2,
//                                          MIGRATION_2_3,
//                                          MIGRATION_3_4,
//                                          MIGRATION_4_5,
//                                          MIGRATION_5_6
//                          )
                          .build()
              }
             return mAppDatabase as AppDatabase
          }

         /**
          * 数据库版本 1->2  musicEnity表格新增了lrc列
          */
        private val MIGRATION_1_2: Migration = object : Migration(1, 2) {
             override fun migrate(database: SupportSQLiteDatabase) {
                 database.execSQL("ALTER TABLE Music ADD COLUMN lrc TEXT");
                // database.execSQL("ALTER TABLE Music " + " ADD COLUMN lrc STRING")
             }
         }

         /**
          * 数据库版本 2->3新增Download表
          */
         private   val MIGRATION_2_3: Migration = object : Migration(2, 3) {
             override fun migrate(database: SupportSQLiteDatabase) {
                 database.execSQL("CREATE TABLE Download (id TEXT NOT NULL,duration INTEGER NOT NULL," +
                         "fileExtension TEXT NOT NULL," +
                         "fileSize INTEGER NOT NULL," +
                         " fileLink TEXT NOT NULL," +
                         "PRIMARY KEY(id))");
             }
         }


         /**
          * 数据库版本 3>4 Download表新增字段
          */
         private   val MIGRATION_3_4: Migration = object : Migration(3, 4) {
             override fun migrate(database: SupportSQLiteDatabase) {
                 database.execSQL("ALTER TABLE Download ADD COLUMN name TEXT ");
                 database.execSQL("ALTER TABLE Download ADD COLUMN status INTEGER");
                 database.execSQL("ALTER TABLE Download ADD COLUMN progress INTEGER");
             }
         }

         /**
          * 数据库版本4>5 Download表新增字段
          */
         private   val MIGRATION_4_5: Migration = object : Migration(4, 5) {
             override fun migrate(database: SupportSQLiteDatabase) {
                 database.execSQL("ALTER TABLE Download ADD COLUMN startPoistion INTEGER");
             }
         }

         /**
          * 数据库版本5>6 Download表新增字段
          */
         private   val MIGRATION_5_6: Migration = object : Migration(5, 6) {
             override fun migrate(database: SupportSQLiteDatabase) {
                 database.execSQL("ALTER TABLE Download ADD COLUMN  author  TEXT");
             }
         }
    }





}