package com.example.filmschecker

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.filmschecker.adapter.FilmAdapter
import com.example.filmschecker.domain.Favori
import com.example.filmschecker.domain.Film
import com.example.filmschecker.service.ApiManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.stream.Collectors

class LikedFilmsActivity : AppCompatActivity() {

    private var films: MutableList<Film> = mutableListOf()
    private lateinit var filmsRecyclerView: RecyclerView
    private lateinit var filmsAdapter : FilmAdapter
    private lateinit var favorisTv: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_liked_films)
        filmsAdapter = FilmAdapter(this)
        filmsRecyclerView = findViewById(R.id.reservation_creneaux)
        favorisTv = findViewById(R.id.favoris_tv)

        filmsRecyclerView.apply {
            adapter = filmsAdapter
        }

        val database = FirebaseDatabase.getInstance().reference
        val userEmail = FirebaseAuth.getInstance().currentUser?.email.toString()
        var favoris: MutableList<Favori> = mutableListOf()

        database.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onCancelled(error: DatabaseError) {
                Log.i("error",error.message)
            }
            override fun onDataChange(snapshot: DataSnapshot) {

                for(s in snapshot.child("favoris").children){
                    favoris.add(s.getValue(Favori::class.java)!!)
                }
                favoris = favoris.stream().filter{it.userEmail == userEmail}.collect(Collectors.toList())
                if(favoris.size == 0){
                    favorisTv.visibility = View.VISIBLE
                }
                for(favori in favoris){
                    getFilm(favori.filmId)
                }
            }
        })
    }

    private fun getFilm(filmId: Int) {
        val filmsCall : Call<Film> = ApiManager.getInstance().filmService.getOneFilm(filmId)
        filmsCall.enqueue(object: Callback<Film> {
            override fun onResponse(call: Call<Film>, response: Response<Film>) {
                films.add(response.body()!!)
                filmsAdapter.films = films
            }
            override fun onFailure(call: Call<Film>, t: Throwable) {
                Log.i("error",t.message.toString())
            }
        })
    }
}