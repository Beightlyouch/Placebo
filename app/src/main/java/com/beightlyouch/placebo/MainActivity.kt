package com.beightlyouch.placebo

import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.realm.Realm
import io.realm.Sort
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.one_item.view.*
import java.nio.file.Files.delete
import java.util.*


class MainActivity : AppCompatActivity() {
    private lateinit var realm: Realm
    private lateinit var adapter: CustomRecyclerViewAdapter
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var itemDecoration: DividerItemDecoration
    private lateinit var itemTouchHelper: ItemTouchHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        realm = Realm.getDefaultInstance()

        fab.setOnClickListener { view ->
            val intent = Intent(this, EditActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        val realmResults = realm.where(PlaceboButton::class.java)
            .findAll()
            .sort("id", Sort.DESCENDING )

        //LayoutManager
        layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

        //Adapter
        adapter = CustomRecyclerViewAdapter(realmResults)
        recyclerView.adapter = adapter

        //Decoration
        itemDecoration = DividerItemDecoration(this,DividerItemDecoration.VERTICAL)
        recyclerView.addItemDecoration(itemDecoration)

        //スワイプ操作
        itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                viewHolder.let{
                    realm.executeTransaction{
                        //被ってたら？
                        val title = viewHolder.itemView.titleView.text.toString()
                        val pb: PlaceboButton? = realm.where<PlaceboButton>().
                                                            equalTo("title", title)
                                                            .findFirst()
                        pb?.deleteFromRealm()
                    }
                    Toast.makeText(applicationContext, "DELETED!", Toast.LENGTH_SHORT).show()
                    recyclerView.adapter?.notifyDataSetChanged()
                }
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
                val itemView = viewHolder.itemView
                val background = ColorDrawable()
                background.color = Color.RED
                if (dX < 0)
                    background.setBounds(
                        itemView.right + dX.toInt(),
                        itemView.top,
                        itemView.right,
                        itemView.bottom
                    )
                background.draw(c)
            }
        })
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }
}
