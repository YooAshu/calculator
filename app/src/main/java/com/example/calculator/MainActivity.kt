package com.example.calculator

import android.media.AudioAttributes
import android.media.SoundPool
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.notkamui.keval.Keval


class MainActivity : AppCompatActivity(), View.OnClickListener {


    private lateinit var soundPool: SoundPool
    private var soundId1: Int = 0
    private var soundId2: Int = 0


    private var canAddOperator = false
    private var canAddDecimal = true
    private var showResult = false
    private var resetInput = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val audioAttributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build()

            soundPool = SoundPool.Builder()
                .setMaxStreams(1)
                .setAudioAttributes(audioAttributes)
                .build()
        } else {
            soundPool = SoundPool(1, android.media.AudioManager.STREAM_MUSIC, 0)
        }

        // Load the sound file
        soundId1 = soundPool.load(this, R.raw.buttonclick, 1)
        soundId2 = soundPool.load(this, R.raw.equalbutton, 1)






    }

    lateinit var inputText:TextView
    lateinit var resultText:TextView

    fun allClearButton(v: View?){
        soundPool.play(soundId1, 0.7f, 0.7f, 1, 0, 1.0f)

        inputText = findViewById<TextView>(R.id.inputText)
        resultText = findViewById<TextView>(R.id.resultText)
        inputText.text = ""
        resultText.text = ""
        canAddOperator = false
        canAddDecimal = true
        showResult = false
        resetInput = false

    }

    fun numButtonClick(view: View){

        inputText = findViewById<TextView>(R.id.inputText)
        if (view is Button){

            soundPool.play(soundId1, 0.7f, 0.7f, 1, 0, 1.0f)

            if(view.text=="." && canAddDecimal){
                inputText.append(view.text)
                canAddDecimal = false
//                evaluateAuto()
            }
            else if(view.text!="."){
                inputText.append(view.text)
                canAddOperator = true
                evaluateAuto()
            }

            if(resetInput){
                inputText.text = view.text
                resetInput = false
            }


        }


    }

    fun opButtonClick(view:View){

        if (view is Button && canAddOperator){
            soundPool.play(soundId1, 0.7f, 0.7f, 1, 0, 1.0f)
            inputText.append(view.text)
            canAddOperator = false
            canAddDecimal = true
            showResult = true
            resetInput = false
        }

    }

    fun equalButton(v: View?){
        soundPool.play(soundId2, 0.7f, 0.7f, 1, 0, 1.0f)
        inputText = findViewById<TextView>(R.id.inputText)
        resultText = findViewById<TextView>(R.id.resultText)

        var res = Keval.eval(inputText.text.toString()).toString()
        resultText.text = checkDoubleOrInt(res.toDouble()).toString()

        showResult = false
        resetInput = true
        inputText.text = resultText.text
        resultText.text = ""

    }

    fun clearToLeft(v: View?){
        soundPool.play(soundId1, 0.7f, 0.7f, 1, 0, 1.0f)
        var expressionLength = inputText.text.length
        if(expressionLength>0){
            inputText.text = inputText.text.subSequence(0,expressionLength-1)
        }

    }

    fun evaluateAuto(){
        if (showResult){
            inputText = findViewById<TextView>(R.id.inputText)
            resultText = findViewById<TextView>(R.id.resultText)

            var res = Keval.eval(inputText.text.toString()).toString()
            resultText.text = checkDoubleOrInt(res.toDouble()).toString()
//            resultText.text = Keval.eval(inputText.text.toString()).toString()
        }
    }

    fun checkDoubleOrInt(result:Double): Any {
        var f = result.toInt()
        if(result-f.toDouble() == 0.0){
            return result.toInt()
        }
        else{
            return result
        }
    }





    override fun onClick(v: View?) {
        TODO("Not yet implemented")
    }





}


