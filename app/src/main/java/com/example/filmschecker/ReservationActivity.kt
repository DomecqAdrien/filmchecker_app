package com.example.filmschecker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.example.filmschecker.adapter.CommentaireAdapter

class ReservationActivity : AppCompatActivity() {


    private lateinit var actorsRecyclerView: RecyclerView
    private lateinit var commentairesAdapter : CommentaireAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)
    }

    private fun initRecyclerView() {
        commentairesAdapter = CommentaireAdapter()
        actorsRecyclerView.apply {
            adapter = commentairesAdapter
        }
    }
}