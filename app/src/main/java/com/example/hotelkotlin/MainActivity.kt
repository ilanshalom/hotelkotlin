package com.example.hotelkotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.hotelkotlin.R
import com.example.hotelkotlin.HotelHelper
import android.database.sqlite.SQLiteDatabase
import android.widget.EditText
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import android.content.ContentValues
import android.database.Cursor
import android.view.View
import android.widget.TextView
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //setTitle("O texto do título da tela");
    }

    fun apaga(v: View?) {
        var ch: HotelHelper? = null
        var bdw: SQLiteDatabase? = null
        try {
            ch = HotelHelper(applicationContext)
            bdw = ch.writableDatabase
            val codhab = findViewById<View>(R.id.edcodhab) as EditText
            val cod = codhab.text.toString()
            if (cod.isEmpty()) {
                Toast.makeText(
                    applicationContext, "Por favor, digite o código da habitação.", Toast.LENGTH_LONG).show()
            } else {
                val id = bdw.delete("tbl_habitacoes", "codhabitacao =$cod", null).toLong()
                if (id == 0L) {
                    Toast.makeText(
                        applicationContext,
                        "\nNão foi possível eliminar. \nVerifique o código.\n",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    Toast.makeText(applicationContext, "Habitação eliminada com sucesso.", Toast.LENGTH_LONG).show()
                }
            }
        } catch (ex: Exception) {
            Toast.makeText(
                applicationContext, "\nErro processando o BD. \n",
                Toast.LENGTH_LONG
            ).show()
        } finally {
            bdw?.close()
            ch?.close()
        }
    }

    fun inserir(v: View?) {
        var codigo = 0
        var qtde = 0
        var preco = 0.0f
        val campoCod = findViewById<EditText>(R.id.edcodhab)
        val campoQtde = findViewById<EditText>(R.id.edqtdepessoas)
        val campoPreco = findViewById<EditText>(R.id.edpreco)
        try {
            codigo = campoCod.text.toString().toInt()
            qtde = campoQtde.text.toString().toInt()
            preco = campoPreco.text.toString().toFloat()
            if (codigo == 0) throw Exception("erro")
        } catch (erro: Exception) {
            Snackbar.make(v!!, "Por favor, digite dados corretos!", Snackbar.LENGTH_LONG).show()
            return  //abandonamos a inserção
        }
        val valores = ContentValues()
        valores.put("codhabitacao", codigo) //codhabitacao é o nome do campo da tabela
        valores.put("qtdepessoas", qtde) //qtdepessoas é o nome do campo da tabela
        valores.put("precodiaria", preco) //precodiaria é o nome do campo da tabela
        var helper: HotelHelper? = null //classe derivada de SQLiteOpenHelper
        var bdw: SQLiteDatabase? = null
        try {
            helper = HotelHelper(applicationContext)
            bdw = helper.writableDatabase
            val id = bdw.insert("tbl_habitacoes", "", valores)
            if (id == -1L) {
                Snackbar.make(v!!, "Não foi possível inserir!", Snackbar.LENGTH_LONG).show()
            } else {
                Snackbar.make(v!!, "Habitação inserida com sucesso.", Snackbar.LENGTH_LONG).show()
            }
        } catch (ex: Exception) {
            Snackbar.make(v!!, "Erro processando o BD!", Snackbar.LENGTH_LONG).show()
        } finally {
            bdw?.close()
            helper?.close()
        }
    }

    fun altera(v: View?) {
        var codigo = 0
        var qtde = 0
        var preco = 0.0f
        val campoCod = findViewById<EditText>(R.id.edcodhab)
        val campoQtde = findViewById<EditText>(R.id.edqtdepessoas)
        val campoPreco = findViewById<EditText>(R.id.edpreco)
        try {
            codigo = campoCod.text.toString().toInt()
            qtde = campoQtde.text.toString().toInt()
            preco = campoPreco.text.toString().toFloat()
            if (codigo == 0 || qtde == 0 || preco == 0f) throw Exception("erro")
        } catch (erro: Exception) {
            Snackbar.make(v!!, "Por favor, digite dados corretos!", Snackbar.LENGTH_LONG).show()
            return  //abandonamos a alteração dos dados da habitação
        }
        val valores = ContentValues()
        valores.put("qtdepessoas", qtde)
        valores.put("precodiaria", preco)
        var helper: HotelHelper? = null
        var bdw: SQLiteDatabase? = null
        try {
            helper = HotelHelper(applicationContext)
            bdw = helper.writableDatabase
            val id = bdw.update("tbl_habitacoes", valores, "codhabitacao=$codigo", null
            ).toLong()
            if (id == 0L) {
                Snackbar.make(v!!, "Não foi possível alterar!", Snackbar.LENGTH_LONG).show()
            } else {
                Snackbar.make(v!!, "Habitação alterada com sucesso.", Snackbar.LENGTH_LONG).show()
            }
        } catch (ex: Exception) {
            Snackbar.make(v!!, "Erro processando o BD!", Snackbar.LENGTH_LONG).show()
        } finally {
            bdw?.close()
            helper?.close()
        }
    }

    fun listar(v: View) {
        var helper: HotelHelper? = null //classe derivada de SQLiteOpenHelper
        var bdr1: SQLiteDatabase? = null
        var cursor: Cursor? = null
        var str = "\nHabitações cadastradas:\n\n"
        try {
            val ctx = v.context
            helper = HotelHelper(ctx)
            bdr1 = helper.readableDatabase
            cursor = bdr1.query(
                "tbl_habitacoes", null, null, null, null, null, "codhabitacao")
            while (cursor.moveToNext()) {
                val cod = cursor.getString(0)
                val qt = cursor.getString(1)
                val pr = cursor.getFloat(2)
                str += "código: $cod, pessoas: $qt, diária R$ ${String.format("%.2f", pr)} \n"
            }
            (findViewById<View>(R.id.res) as TextView).text = str
        } catch (ex: Exception) {
            Snackbar.make(v, "Erro processando o BD!", Snackbar.LENGTH_LONG).show()
        } finally {
            cursor?.close()
            bdr1?.close()
            helper?.close()
        }
    }
}