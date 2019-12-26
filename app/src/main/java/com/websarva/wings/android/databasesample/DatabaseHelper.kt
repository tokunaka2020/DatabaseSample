package com.websarva.wings.android.databasesample

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.lang.StringBuilder

// データベースヘルパークラス
class DatabaseHelper(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    // クラス内のPrivate定数宣言
    companion object{
        // データベース名
        private const val DATABASE_NAME = "itemmemo.db"
        // データベースバージョン情報
        private const val DATABASE_VERSION = 1
    }

    // 生成時（データベースが存在しない時に発生する）
    override fun onCreate(db: SQLiteDatabase) {
        // テーブル作成用ＳＱＬ文字列
        val sb = StringBuilder()
        sb.append("CREATE TABLE itemmemo (")
        sb.append("_id INTEGER PRIMARY KEY,")
        sb.append("name TEXT,")
        sb.append("note TEXT")
        sb.append(");")
        val sql = sb.toString()

        // ＳＱＬの実行
        db.execSQL(sql)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
    }

}