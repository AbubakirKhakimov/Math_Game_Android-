package com.x.a_technologies.math_game

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.x.a_technologies.math_game.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        resultsOutput()
        installationChecked()

        binding.cardViewPlay.setOnClickListener(){
            startActivity(Intent(this,GameActivity::class.java))
            finish()
        }

        binding.lowButton.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                Datas.levelGame = 10
            }
        }

        binding.mediumButton.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                Datas.levelGame = 100
            }
        }

        binding.highButton.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                Datas.levelGame = 1000
            }
        }

        binding.lowTimeButton.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                Datas.timeGame = 30000
            }
        }

        binding.mediumTimeButton.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                Datas.timeGame = 60000
            }
        }

        binding.highTimeButton.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                Datas.timeGame = 120000
            }
        }
    }

    fun installationChecked(){
        when(Datas.levelGame){
            10 -> binding.lowButton.isChecked = true
            100 -> binding.mediumButton.isChecked = true
            1000 -> binding.highButton.isChecked = true
        }

        when(Datas.timeGame){
            30000L -> binding.lowTimeButton.isChecked = true
            60000L -> binding.mediumTimeButton.isChecked = true
            120000L -> binding.highTimeButton.isChecked = true
        }
    }

    fun resultsOutput(){
        read()
        binding.currentResult.text = Datas.currentResult.toString()
        binding.recordResult.text = Datas.recordResult.toString()
    }

    fun read(){
        val pref = getSharedPreferences("Results", MODE_PRIVATE)
        Datas.recordResult = pref.getInt("recordResult",0)
    }
}