package pt.ipt.dama.dama_project1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    companion object {
        const val SEARCH_NAME = "SEARCH_NAME"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val searchButton: Button = findViewById(R.id.search_button)
        searchButton.setOnClickListener {
            val searchFor: EditText = findViewById(R.id.search_name)
            val searchName: String = searchFor.text.toString()
            if (searchName.isEmpty()) {
                Toast.makeText(baseContext, getString(R.string.toast1), Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(this@MainActivity, BookActivity::class.java)
                intent.putExtra(SEARCH_NAME, searchName)
                startActivity(intent)
            }
        }
    }

}