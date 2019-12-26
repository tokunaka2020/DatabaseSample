package com.websarva.wings.android.databasesample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    // 選択されたアイテムの主キーＩＤ格納
    private var _itemId = -1

    // 選択されたアイテム名格納
    private var _itemName = ""

    // データベースヘルパーオブジェクト
    private val _helper = DatabaseHelper(this@MainActivity)

    // アクティビティ生成時
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // アイテムListView(lvItem)取得
        val lvItem = findViewById<ListView>(R.id.lvItem)

        // アイテムListView(lvItem)にリスナー登録
        lvItem.onItemClickListener = ListItemClickListener()

    }

    // アクティビティ破棄時
    override fun onDestroy() {
        // ヘルパーオブジェクトの解放
        _helper.close()
        super.onDestroy()
    }


    // アイテムListViewがタップされた時のメンバクラス
    private inner class ListItemClickListener: AdapterView.OnItemClickListener{

        // アイテムリストタップ時
        override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {
            // タップされた行番号を主キーＩＤにセット
            _itemId = position
            // タップされた行のアイテム名を取得
            _itemName = parent.getItemAtPosition(position) as String

            // アイテム名のテキストビュー取得
            val tvItemName = findViewById<TextView>(R.id.tvItemName)
            // アイテム名の表示
            tvItemName.text = _itemName

            // 備考欄の取得
            val etNote = findViewById<EditText>(R.id.etNote)
            // 備考欄を有効に
            etNote.isEnabled = true

            // 備考欄の該当データを取得し、表示
            val db = _helper.writableDatabase
            val sql = "SELECT * FROM itemmemo WHERE _id = ${_itemId}"
            // ＳＱＬの実行
            val cursor = db.rawQuery(sql, null)
            // 取得データの一時格納用
            var note = ""
            while(cursor.moveToNext()){
                // カラムのインデックス値の取得
                val idxNote = cursor.getColumnIndex("note")
                // カラムのインデックス値をもとにデータを取得
                note = cursor.getString(idxNote)
            }

            // データの表示
            etNote.setText(note)

            // 保存ボタンの取得
            val btnSave = findViewById<Button>(R.id.btnSave)
            // 保存ボタンを有効に
            btnSave.isEnabled = true
        }
    }

    // 保存ボタンタップ時
    fun onSaveButtonClick(view: View){

        // 備考欄の取得
        val etNote = findViewById<EditText>(R.id.etNote)

        // 備考の値を取得
        val note = etNote.text.toString()

        // データベースヘルパーオブジェクトからデータベース接続オブジェクトを取得
        val db = _helper.writableDatabase

        // 選択されたアイテムのメモデータを一度削除してから、追加する。

        // 削除処理
        val sqlDelete = "DELETE FROM itemmemo WHERE _id = ?"
        // プリペアドステートメント（ＳＱＬを実行するオブジェクト）を取得
        var stmt = db.compileStatement(sqlDelete)
        // 変数のバインド（ＳＱＬの？の部分をあてはめる）
        stmt.bindLong(1,_itemId.toLong())
        // ＳＱＬの実行（削除）
        stmt.executeUpdateDelete()

        // 追加処理
        val sqlInsert = "INSERT INTO itemmemo ( _id, name, note ) VALUES ( ?, ?, ? )"
        // プリペアドステートメント（ＳＱＬを実行するオブジェクト）を取得
        stmt = db.compileStatement(sqlInsert)
        // 変数のバインド（ＳＱＬの？の部分をあてはめる）
        stmt.bindLong(1,_itemId.toLong())
        stmt.bindString(2, _itemName.toString())
        stmt.bindString(3, note)
        // ＳＱＬの実行（削除）
        stmt.executeInsert()

        // ここからはデータ保存後の処理

        // 備考欄初期化
        etNote.setText("")
        // 備考欄を無効に
        etNote.isEnabled = false

        // アイテム名表示欄の取得
        val tvItemName = findViewById<TextView>(R.id.tvItemName)
        // アイテム名を初期化
        tvItemName.text = getString(R.string.tv_name)

        // 保存ボタンの取得
        val btnSave = findViewById<Button>(R.id.btnSave)
        // 保存ボタンを無効に
        btnSave.isEnabled = false
    }
}
