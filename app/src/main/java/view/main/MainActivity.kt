package view.main

import android.Manifest
import android.database.Cursor
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import model.googlemaps.GoogleMapsFragment
import com.example.myweatherapp.R
import com.example.myweatherapp.databinding.MainActivityBinding
import view.details.HistoryFragment


class MainActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.M)
    private val permissionResult =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { result ->
            when {
                result -> getContact()
                !shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS) ->
                    Toast.makeText(
                        this,
                        "Увы вы не дали разрешения к контактам :-(",
                        Toast.LENGTH_LONG
                    ).show()

                else -> Toast.makeText(
                    this,
                    "Увы вы не дали разрешения к контактам :-(",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

    private lateinit var binding: MainActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitAllowingStateLoss()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_history, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {

            R.id.menu_google_maps -> {
                supportFragmentManager.
                    beginTransaction()
                        .add(R.id.container, GoogleMapsFragment())
                        .addToBackStack("")
                        .commitAllowingStateLoss()

                true
            }

            R.id.menu_history -> {
                supportFragmentManager.apply {
                    beginTransaction()
                        .add(R.id.container, HistoryFragment.newInstance())
                        .addToBackStack("")
                        .commitAllowingStateLoss()
                }
                true
            }
            R.id.menu_content_provider -> {
                permissionResult.launch(Manifest.permission.READ_CONTACTS)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun getContact() {
        contentResolver

        val cursor: Cursor? = contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            null,
            null,
            null,
            ContactsContract.Contacts.DISPLAY_NAME + " ASC"
        )

        val contracts = mutableListOf<String>()
        cursor?.let {
            for (i in 0..cursor.count) {
                if (cursor.moveToPosition(i)) {
                    val name =
                        cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))

                    contracts.add(name)
                }
            }
            it.close()
        }

        AlertDialog.Builder(this)
            .setItems(contracts.toTypedArray()) { _, _ -> }
            .setCancelable(true)
            .show()
    }
}