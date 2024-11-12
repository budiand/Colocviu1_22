package ro.pub.cs.systems.eim.colocviu1_2

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class Coloviu1_2MainActivity : AppCompatActivity() {

    private lateinit var Term: EditText
    private lateinit var AllTerms: EditText

    private var sum = 0

    private val intentFilter = IntentFilter().apply {
        addAction(Constants.actionTypes[0])  // adăugăm acțiunea definită în Constants
    }

    private val messageBroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            context?.let {
                Toast.makeText(context, intent?.getStringExtra(Constants.BROADCAST_RECEIVER_EXTRA).toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_practical_test01_2_main)

        Term = findViewById(R.id.term)
        AllTerms = findViewById(R.id.all_terms)

        val addButton = findViewById<Button>(R.id.add)
        addButton.setOnClickListener {
            if (Term.text.isNotEmpty()) {
                AllTerms.append("+${Term.text}")
                Term.text.clear()
            }
        }

        val computeButton = findViewById<Button>(R.id.compute)
        computeButton.setOnClickListener {
            val intent = Intent(this, Coloviu1_2SecondaryActivity::class.java)
            intent.putExtra("all_terms", AllTerms.text.toString())  // Ensure key matches
            startActivityForResult(intent, 1)
            startServiceIfConditionIsMet(sum)
        }
    }

    // D
    private fun startServiceIfConditionIsMet(sum: Int) {
        if (sum > Constants.SUM_THRESHOLD) {
            val intent = Intent(applicationContext, Colociu1_2Service::class.java).apply {
                putExtra(Constants.KEY_SUM, sum)
            }
            applicationContext.startService(intent)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                val result = data?.getStringExtra("result")
                if (result != null) {
                    sum = result.toInt()
                }
                Toast.makeText(this, "Rezultatul primit: $result", Toast.LENGTH_LONG).show()
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Calculul nu a fost efectuat", Toast.LENGTH_LONG).show()
            }
        }
    }

    // Salvăm valorile în Bundle înainte ca activitatea să fie distrusă
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putInt(Constants.KEY_SUM, sum)
    }

    // Restaurăm valorile în onRestoreInstanceState
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        sum = savedInstanceState.getInt(Constants.KEY_SUM, 0)
        AllTerms.setText(sum.toString())
    }

    override fun onResume() {
        super.onResume()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(messageBroadcastReceiver, intentFilter, Context.RECEIVER_EXPORTED)
        } else {
            registerReceiver(messageBroadcastReceiver, intentFilter)
        }
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(messageBroadcastReceiver)
    }

    override fun onDestroy() {
        val intent = Intent(applicationContext, Colociu1_2Service::class.java)
        applicationContext.stopService(intent)
        super.onDestroy()
    }
}
