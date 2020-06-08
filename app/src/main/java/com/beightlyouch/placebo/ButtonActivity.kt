package com.beightlyouch.placebo

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import io.realm.Realm
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_button.*

class ButtonActivity : AppCompatActivity() {
    private lateinit var realm: Realm
    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_button)
        realm = Realm.getDefaultInstance()
        mediaPlayer = MediaPlayer.create(this, R.raw.click)

        val id = intent.getLongExtra("id", 0L)
        val pb :PlaceboButton? = realm.where<PlaceboButton>().equalTo("id", id).findFirst()

        titleView.text = pb?.title
        countView.text = pb?.count.toString() + "PUSH!"

        backBtn.setOnClickListener {
            val intent = Intent(this, EditActivity::class.java)
            intent.putExtra("id", id)
            startActivity(intent)
        }

        placeboBtn.setOnClickListener {
            val messageList: List<String> = listOf("Good!", "Great!", "Congratulations!", "Well Done!", "Awesome!", "Nice!", "WOW!", "Excellent!", "Incredible!")
            Toast.makeText(applicationContext, messageList.random(), Toast.LENGTH_SHORT).show()
            play(mediaPlayer)

            realm.executeTransaction {
                pb!!.count += 1 //????
                countView.text = pb?.count.toString() + "PUSH!"
            }
        }
    }

    fun play(mp: MediaPlayer) {
        if (mp.isPlaying) {
            mp.stop()
            mp.prepare()
        }
        mp.start()
    }
}
