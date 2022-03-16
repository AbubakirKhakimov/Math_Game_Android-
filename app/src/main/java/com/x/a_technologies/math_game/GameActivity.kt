package com.x.a_technologies.math_game

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.x.a_technologies.math_game.databinding.ActivityGameBinding
import kotlin.random.Random.Default.nextInt
import android.animation.ValueAnimator

import android.animation.ArgbEvaluator
import android.content.Intent
import android.graphics.Color
import android.os.CountDownTimer
import android.os.Vibrator


class GameActivity : AppCompatActivity() {

    lateinit var binding: ActivityGameBinding
    private var answer = 0
    val characters = arrayOf('+','-','x','/')
    var correctAnswerCount = 0
    lateinit var vibrator:Vibrator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator
        Datas.timerWork = true

        timer()
        gameCircle()

        binding.answerOne.setOnClickListener(){
            answersButtonListener(binding.answerOneText)
        }

        binding.answerTwo.setOnClickListener(){
            answersButtonListener(binding.answerTwoText)
        }

        binding.answerThree.setOnClickListener(){
            answersButtonListener(binding.answerThreeText)
        }

        binding.answerFour.setOnClickListener(){
            answersButtonListener(binding.answerFourText)
        }

        binding.stopButton.setOnClickListener(){
            if (Datas.recordResult < correctAnswerCount){
                val pref = getSharedPreferences("Results", MODE_PRIVATE)
                val edit = pref.edit()
                edit.putInt("recordResult",correctAnswerCount)
                edit.apply()
            }

            Datas.timerWork = false
            Datas.currentResult = correctAnswerCount
            startActivity(Intent(this,MainActivity()::class.java))
            finish()
        }
    }

    fun timer(){
        object :CountDownTimer(Datas.timeGame+1000,1000){
            override fun onTick(millisUntilFinished: Long) {
                if (!Datas.timerWork){
                    cancel()
                }
                binding.time.text = (millisUntilFinished/1000).toString()
            }

            override fun onFinish() {
                if (Datas.recordResult < correctAnswerCount){
                    val pref = getSharedPreferences("Results", MODE_PRIVATE)
                    val edit = pref.edit()
                    edit.putInt("recordResult",correctAnswerCount)
                    edit.apply()
                }

                Datas.currentResult = correctAnswerCount
                startActivity(Intent(this@GameActivity,MainActivity()::class.java))
                finish()
            }

        }.start()
    }

    fun answersButtonListener(answerTextView:TextView){
        if(answerTextView.text == answer.toString()){
            correctAnswerCount++
            binding.correctCount.text = correctAnswerCount.toString()

            animateColor("#FFFFFFFF","#A4FFA8")
        }else{
            vibrator.vibrate(50)
            animateColor("#FFFFFFFF","#FFBCBC")
        }
        gameCircle()
    }

    fun gameCircle(){
        val firstNumber = nextInt(Datas.levelGame)
        val lastNumber = nextInt(Datas.levelGame)
        val randomCharacter = characters[nextInt(4)]

        if (randomCharacter=='/' && lastNumber==0 || randomCharacter=='/' && firstNumber % lastNumber!=0){
            gameCircle()
        }else {
            binding.example.text = "$firstNumber $randomCharacter $lastNumber"
            calculationAnswer(firstNumber, lastNumber, randomCharacter)
            installationAnswers()
        }
    }

    fun animateColor(from:String, to:String){
        val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(),
            Color.parseColor(from), Color.parseColor(to))
        colorAnimation.duration = 400 // milliseconds

        colorAnimation.addUpdateListener { animator ->
            binding.scrollView.setBackgroundColor(animator.animatedValue as Int) }
        colorAnimation.start()

        val colorAnimation2 = ValueAnimator.ofObject(ArgbEvaluator(),
            Color.parseColor(to), Color.parseColor(from))
        colorAnimation2.duration = 400 // milliseconds

        colorAnimation2.addUpdateListener { animator ->
            binding.scrollView.setBackgroundColor(animator.animatedValue as Int) }
        colorAnimation2.start()
    }

    fun calculationAnswer(firstNumber:Int, lastNumber:Int, randomCharacter:Char){
        answer = when(randomCharacter){
            '+' -> firstNumber + lastNumber
            '-' -> firstNumber - lastNumber
            'x' -> firstNumber * lastNumber
            '/' -> firstNumber / lastNumber
            else -> 0
        }
    }

    fun installationAnswers(){
        val indexList = ArrayList<Int>()
        val valueList = ArrayList<Int>()

        for (i in 0 until 4){
            if (i==0){
                indexList.add(nextInt(4))
                valueList.add(answer)
            }else{
                var work=true
                while (work) {
                    val randomIndex = nextInt(4)
                    for (j in i-1 downTo 0) {
                        if (randomIndex == indexList[j]) {
                            break
                        }else if (j==0){
                            work = false
                            indexList.add(randomIndex)
                        }
                    }
                }

                work=true
                var size = answer.toString().length
                if (answer < 0){
                    size--
                }

                var numberMaxSize = "1"
                repeat(size){
                    numberMaxSize += "0"
                }

                while (work) {
                    var randomValue = nextInt(numberMaxSize.toInt())

                    if (answer < 0){
                        randomValue = ("-$randomValue").toInt()
                    }

                    for (j in i-1 downTo 0) {
                        if (randomValue == valueList[j]) {
                            break
                        }else if (j==0){
                            work = false
                            valueList.add(randomValue)
                        }
                    }
                }
            }
        }

        for (i in indexList.indices) {
            when (indexList[i]) {
                0 -> {
                    textSizeChanged(valueList[i], binding.answerOneText)
                    binding.answerOneText.text = valueList[i].toString()
                }
                1 -> {
                    textSizeChanged(valueList[i], binding.answerTwoText)
                    binding.answerTwoText.text = valueList[i].toString()
                }
                2 -> {
                    textSizeChanged(valueList[i], binding.answerThreeText)
                    binding.answerThreeText.text = valueList[i].toString()
                }
                3 -> {
                    textSizeChanged(valueList[i], binding.answerFourText)
                    binding.answerFourText.text = valueList[i].toString()
                }
            }
        }
    }

    fun textSizeChanged(value:Int ,textView:TextView){
        if (value.toString().length == 4){
            textView.textSize = 40f
        }else if (value.toString().length >= 5){
            textView.textSize = 32f
        }else{
            textView.textSize = 56f
        }
    }

}