package com.beightlyouch.placebo

import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Point
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
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
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        titleView.text = pb?.title
        countView.text = pb?.count.toString() + " PUSH!"

        editBtn.setOnClickListener {
            val intent = Intent(this, EditActivity::class.java)
            intent.putExtra("id", id)
            startActivity(intent)
        }

        backBtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        placeboBtn.setOnClickListener {
            val messageList: List<String> = listOf("Good!", "Great!", "Congratulations!", "Well Done!", "Awesome!", "Nice!", "WOW!", "Excellent!", "Incredible!")
            Toast.makeText(applicationContext, messageList.random(), Toast.LENGTH_SHORT).show()
            play(mediaPlayer)

            realm.executeTransaction {
                pb!!.count += 1 //????
                countView.text = pb?.count.toString() + " PUSH!"
            }
        }
    }

    override fun onStart() {
        super.onStart()

        //画面サイズ取得
        val display = windowManager.defaultDisplay
        val p = Point();
        display.getSize(p)
        val height = p.y;
        val width = p.x;

        val placeboBtnParams: ViewGroup.LayoutParams = placeboBtn.layoutParams
        val titleParams: ViewGroup.LayoutParams = titleView.layoutParams
        val countParams: ViewGroup.LayoutParams = countView.layoutParams

        val placeboBtnMargin = titleParams as ViewGroup.MarginLayoutParams
        val countMargin = countParams as ViewGroup.MarginLayoutParams
        val titleMargin = titleParams as ViewGroup.MarginLayoutParams

        //説明文のマージン
        placeboBtnMargin.setMargins(0, 0, 0, height / 10)
        countMargin.setMargins(0, 0, 0, 5)
        titleMargin.setMargins(0, height / 20, 0, 0)

        placeboBtn.setLayoutParams(placeboBtnParams)
        countView.setLayoutParams(countParams)
        titleView.setLayoutParams(titleParams)
    }

    fun play(mp: MediaPlayer) {
        if (mp.isPlaying) {
            mp.stop()
            mp.prepare()
        }
        mp.start()
    }
}
