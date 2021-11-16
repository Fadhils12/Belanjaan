package com.stups.belanjaan

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.BaseAdapter
import android.widget.TextView
import com.stups.belanjaan.adapter.DbAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {

    private var listBarang = ArrayList<Barang>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            var intent = Intent(this, BelanjaanActivity::class.java)
            startActivity(intent)
        }

        loadData()
    }

    override fun onResume() {
        super.onResume()
        loadData()
    }

    private fun loadData() {
        var dbAdapter = DbAdapter(this)
        var cursor = dbAdapter.allQuery()

        listBarang.clear()
        if (cursor.moveToFirst()){
            do {
                val id = cursor.getInt(cursor.getColumnIndex("Id"))
                val nama = cursor.getString(cursor.getColumnIndex("Nama"))
                val jenis = cursor.getString(cursor.getColumnIndex("Jenis"))
                val harga = cursor.getString(cursor.getColumnIndex("Harga"))

                listBarang.add(Barang(id, nama, jenis, harga))
            }while (cursor.moveToNext())
        }

        var barangAdapter = BarangAdapter(this, listBarang)
        list_belanjaan.adapter = barangAdapter
    }

    inner class BarangAdapter: BaseAdapter{

        private var barangList = ArrayList<Barang>()
        private var context: Context? = null

        constructor(context: Context, barangList: ArrayList<Barang>) : super(){
            this.barangList = barangList
            this.context = context
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
            val view: View?
            val vh: ViewHolder

            if (convertView == null){
                view = layoutInflater.inflate(R.layout.barang, parent, false)
                vh = ViewHolder(view)
                view.tag = vh
                Log.i("db", "set tag for ViewHolder, position: " + position)
            }else{
                view = convertView
                vh = view.tag as ViewHolder
            }

            var mBarang = barangList[position]

            vh.tvNama.text = mBarang.name
            vh.tvJenis.text = mBarang.jenis
            vh.tvHarga.text = "Rp." + mBarang.harga

            list_belanjaan.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, position, id ->
                updateBarang(mBarang)
            }

            return view
        }

        override fun getItem(position: Int): Any {
            return barangList[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return barangList.size
        }

    }

    private fun updateBarang(barang: Barang) {
        var  intent = Intent(this, BelanjaanActivity::class.java)
        intent.putExtra("MainActId", barang.id)
        intent.putExtra("MainActNama", barang.name)
        intent.putExtra("MainActJenis", barang.jenis)
        intent.putExtra("MainActHarga", barang.harga)
        startActivity(intent)
    }

    private class ViewHolder(view: View?){
        val tvNama: TextView
        val tvJenis: TextView
        val tvHarga: TextView

        init {
            this.tvNama = view?.findViewById(R.id.tv_name) as TextView
            this.tvJenis = view?.findViewById(R.id.tv_jenis) as TextView
            this.tvHarga = view?.findViewById(R.id.tv_harga) as TextView
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}