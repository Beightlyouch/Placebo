package com.beightlyouch.placebo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_edit.*
import io.realm.kotlin.createObject
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_edit.*
import java.util.*


class EditActivity : AppCompatActivity() {
    private lateinit var realm: Realm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        realm = Realm.getDefaultInstance()

        saveBtn.setOnClickListener {
            var title: String = ""
            if(!titleTxt.text.isNullOrEmpty()) {
                title = titleTxt.text.toString()
            }
            realm.executeTransaction {
                val maxId = realm.where<PlaceboButton>().max("id")
                val nextId = (maxId?.toLong()?: 0L) + 1L
                val pb = realm.createObject<PlaceboButton>(nextId)
                pb.dateTime = Date()
                pb.title = title
                Log.d("TAG", title)
            }
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }
}
