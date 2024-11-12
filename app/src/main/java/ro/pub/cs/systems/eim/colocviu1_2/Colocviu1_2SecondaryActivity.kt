package ro.pub.cs.systems.eim.colocviu1_2

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class Coloviu1_2SecondaryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val content = intent.getStringExtra("all_terms") ?: ""
        val numbers = content.split("+").filter { it.isNotEmpty() }  // Filtrăm elementele goale
        val sum = numbers.sumOf { it.toIntOrNull() ?: 0 }  // Safe handling pentru conversie

        val resultIntent = Intent().apply {
            putExtra("result", sum.toString())
        }
        setResult(RESULT_OK, resultIntent)
        finish()
    }
}
