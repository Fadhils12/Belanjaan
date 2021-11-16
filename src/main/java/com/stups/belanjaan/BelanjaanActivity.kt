package com.stups.belanjaan

import android.content.ContentValues
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ListAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.stups.belanjaan.adapter.DbAdapter
import kotlinx.android.synthetic.main.activity_belanjaan.*

open class BelanjaanActivity : AppCompatActivity() {

    var id=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_belanjaan)

        try {
            var bundle: Bundle? = intent.extras
            id = bundle!!.getInt("MainActId", 0)
            if (id !=0){
                txNama.setText(bundle.getString("MainActNama"))
                txJenis.setText(bundle.getString("MainActJenis"))
                txHarga.setText(bundle.getString("MainActHarga"))
            }
        }catch (ex: Exception){
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.add_menu, menu)

        val itemDelete: MenuItem = menu.findItem(R.id.delete)

        itemDelete.isVisible = id != 0
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.save -> {
                var dbAdapter = DbAdapter(this)

                var values = ContentValues()
                values.put("Nama", txNama.text.toString())
                values.put("Jenis", txJenis.text.toString())
                values.put("Harga", txHarga.text.toString())

                if (id == 0){
                    val mID = dbAdapter.insert(values)

                    if (mID > 0){
                        Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show()
                        finish()
                    }else{
                        Toast.makeText(this, "Failed to save data", Toast.LENGTH_SHORT).show()
                    }
                }else{
                    var selectionArgs = arrayOf(id.toString())
                    val mID = dbAdapter.update(values, "Id=?", selectionArgs)
                    if (mID > 0){
                        Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show()
                        finish()
                    }else{
                        Toast.makeText(this, "Failed to update data", Toast.LENGTH_SHORT).show()
                    }
                }
                true
            }
            R.id.delete ->{
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Delete Data")
                builder.setMessage("This Data Will Be Deleted")

                builder.setPositiveButton("Iya Setuju") { dialog: DialogInterface?, which: Int ->
                    var dbAdapter = DbAdapter(this)
                    val selectionArgs = arrayOf(id.toString())
                    dbAdapter.delete("Id=?", selectionArgs)
                    Toast.makeText(this, "Terhapus", Toast.LENGTH_SHORT).show()
                    finish()
                }
                builder.setNegativeButton("Batal"){ dialog: DialogInterface?, which: Int ->  }

                val alertDialog: AlertDialog = builder.create()
                alertDialog.setCancelable(false)
                alertDialog.show()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}