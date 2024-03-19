package ru.myitschool.lab23

import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var pressure: TextView
    private lateinit var unit: TextView
    private lateinit var sensorManager: SensorManager
    private lateinit var pressureSensor: Sensor
    private lateinit var pas_unit: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        pressure = findViewById(R.id.pressure)
        unit = findViewById(R.id.pressure_unit)
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        if(sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE) == null) {
            pressure.text = "Нет датчика сенсора!"
        } else {
            pressureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE)
            sensorManager.registerListener(this,pressureSensor,SensorManager.SENSOR_DELAY_GAME)
        }

    }

    override fun onResume() {
        super.onResume()
        val sharedPreferences = getSharedPreferences("pressure_key", Context.MODE_PRIVATE)
        pas_unit = sharedPreferences.getString("pressure","0").toString()
        when(pas_unit) {
            "0" -> unit.text = getString(R.string.gpa)
            "1" -> unit.text = getString(R.string.mmrtst)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.action_settings) {
            val intent = Intent(this,SettingsActivity::class.java)
            startActivity(intent)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSensorChanged(event: SensorEvent) {
        if(event.sensor.type == Sensor.TYPE_PRESSURE) {
            if(pas_unit == "0") {
                pressure.text = "${event.values[0]}"
            } else {
                pressure.text = "${event.values[0]*0.750063755419211}"
            }
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

    }
}
