package com.beightlyouch.placebo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
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

        val id = intent.getLongExtra("id", 0L)
        if(id > 0L) {
            val pb = realm.where<PlaceboButton>().equalTo("id", id).findFirst()
            titleTxt.setText(pb?.title)
        }

        saveBtn.setOnClickListener {
            var title: String = ""
            if(!titleTxt.text.isNullOrEmpty()) {
                title = titleTxt.text.toString()
            }

            when(id) {
                0L -> {
                    realm.executeTransaction {
                        val maxId = realm.where<PlaceboButton>().max("id")
                        val nextId = (maxId?.toLong() ?: 0L) + 1L
                        val pb = realm.createObject<PlaceboButton>(nextId)
                        pb.dateTime = Date()
                        pb.title = title
                        Log.d("TAG", title)
                    }
                }
                else -> {
                    realm.executeTransaction {
                        val pb = realm.where<PlaceboButton>().equalTo("id", id).findFirst()
                        pb?.title = title
                    }
                }
            }
            val intent = Intent(this, ButtonActivity::class.java)
            intent.putExtra("id", id)
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }
}
