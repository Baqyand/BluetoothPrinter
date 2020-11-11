package com.baqynra.withbaqyand.bluetoothprint

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.mazenrashed.printooth.Printooth
import com.mazenrashed.printooth.data.printable.Printable
import com.mazenrashed.printooth.data.printable.RawPrintable
import com.mazenrashed.printooth.data.printable.TextPrintable
import com.mazenrashed.printooth.data.printer.DefaultPrinter
import com.mazenrashed.printooth.ui.ScanningActivity
import com.mazenrashed.printooth.utilities.Printing
import com.mazenrashed.printooth.utilities.PrintingCallback
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), PrintingCallback {

    internal var printing: Printing? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initview()
    }

    private fun initview() {
        if ( printing != null){
            printing!!.printingCallback = this
        }
        btn3.setOnClickListener {
            if(Printooth.hasPairedPrinter()){
                Printooth.removeCurrentPrinter()
            }
            else{
                startActivityForResult(Intent( this@MainActivity, ScanningActivity::class.java),
                    ScanningActivity.SCANNING_FOR_PRINTER)
                changePairAndUnpair()
            }
        }
        btn1.setOnClickListener {
            if (!Printooth.hasPairedPrinter()){
                startActivityForResult(Intent(this@MainActivity, ScanningActivity::class.java), ScanningActivity.SCANNING_FOR_PRINTER)
            }else{
                printText()
            }
        }
        btn2.setOnClickListener {
            if (!Printooth.hasPairedPrinter()){
                startActivityForResult(Intent(this@MainActivity, ScanningActivity::class.java), ScanningActivity.SCANNING_FOR_PRINTER)
            }else{
                printText()
            }
        }
    }

    private fun printText() {
        val printables = ArrayList<Printable>()
        printables.add(RawPrintable.Builder(byteArrayOf(27,100,4)).build())

        //add text
        printables.add(TextPrintable.Builder()
            .setText("Hello Gaiss : Ini Struknya")
            .setCharacterCode(DefaultPrinter.CHARCODE_PC1252)
            .setNewLinesAfter(1)
            .build())

        //custom
        printables.add(
            TextPrintable.Builder()
                .setText("Selamat Datang")
                .setAlignment(DefaultPrinter.ALIGNMENT_CENTER)
                .setEmphasizedMode(DefaultPrinter.EMPHASIZED_MODE_BOLD)
                .setNewLinesAfter(1)
                .build())

        printables.add(
            TextPrintable.Builder()
                .setText("di")
                .setAlignment(DefaultPrinter.ALIGNMENT_CENTER)
                .setEmphasizedMode(DefaultPrinter.EMPHASIZED_MODE_BOLD)
                .setNewLinesAfter(1)
                .build())
        printables.add(
            TextPrintable.Builder()
                .setText("Barber Cut ")
                .setAlignment(DefaultPrinter.ALIGNMENT_CENTER)
                .setEmphasizedMode(DefaultPrinter.EMPHASIZED_MODE_BOLD)
                .setNewLinesAfter(1)
                .build())
        printables.add(
            TextPrintable.Builder()
                .setText("==============================")
                .setAlignment(DefaultPrinter.ALIGNMENT_CENTER)
                .setNewLinesAfter(1)
                .build())
        printables.add(TextPrintable.Builder()
            .setText("")
            .setAlignment(DefaultPrinter.ALIGNMENT_CENTER)
            .setNewLinesAfter(1)
            .build())

        printing!!.print(printables)
    }


    private fun changePairAndUnpair() {
        if (Printooth.hasPairedPrinter()){
            btn3.text = "unpair ${Printooth.getPairedPrinter()?.name}"
        }
        else{
            btn3.text = "Paired"
        }
    }

    override fun connectingWithPrinter() {
        Toast.makeText(this, "Connecting ", Toast.LENGTH_SHORT).show()
    }

    override fun connectionFailed(error: String) {
        Toast.makeText(this, "Failed : $error ", Toast.LENGTH_SHORT).show()
    }

    override fun onError(error: String) {
        Toast.makeText(this, "Error : $error", Toast.LENGTH_SHORT).show()
    }

    override fun onMessage(message: String) {
        Toast.makeText(this, "Message : $message ", Toast.LENGTH_SHORT).show()
    }

    override fun printingOrderSentSuccessfully() {
        Toast.makeText(this, "Sent to Printer ", Toast.LENGTH_SHORT).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ScanningActivity.SCANNING_FOR_PRINTER && resultCode == Activity.RESULT_OK){
            initPrinting()
            changePairAndUnpair()
        }
    }

    private fun initPrinting() {
        if (Printooth.hasPairedPrinter())
            printing = Printooth.printer()
        if (printing!= null)
            printing!!.printingCallback = this

    }
}