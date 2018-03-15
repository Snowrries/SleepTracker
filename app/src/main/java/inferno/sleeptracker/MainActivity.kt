package inferno.sleeptracker

import android.app.Activity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import java.time.*
import java.time.temporal.ChronoUnit

class MainActivity : Activity() {
    lateinit var sleepTime:LocalDateTime
    lateinit var wakeTime: LocalDateTime
    var sleeping:Boolean = false
    val PREFS_FILENAME = "inferno.sleeptracker.prefs"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //check user prefs
        val prefs = this.getSharedPreferences(PREFS_FILENAME, 0)
        sleeping = prefs.getBoolean("sleep",false)
        sleepTime = LocalDateTime.parse(prefs.getString("sleepTime","2007-12-03T10:15:30"))
        wakeTime = LocalDateTime.parse(prefs.getString("wakeTime","2007-12-03T10:15:30"))

        if(sleeping){
            hours.text = "Sleeping..."
            sleepButton.text = getString(R.string.wake)
            timestamp.text = getString(R.string.from,sleepTime.toString(),"...")
        }
        else{
            hours.text = getString(R.string.hours,sleepTime.until(wakeTime, ChronoUnit.HOURS))
            sleepButton.text = getString(R.string.sleep)
            timestamp.text = getString(R.string.from,sleepTime.toString(),wakeTime.toString())
        }
    }
    fun sleep(view: View){
        //record timestamp into prefs and set sleeping flag
        if(!sleeping){
            sleepTime = LocalDateTime.now()
            hours.text = getString(R.string.Sleeping)
            sleeping = true
            sleepButton.text = getString(R.string.wake)
            timestamp.text = getString(R.string.from,sleepTime.toString(),"...")
        }
        else{
            //record timestamp and update hours
            wakeTime = LocalDateTime.now()
            hours.text = getString(R.string.hours,sleepTime.until(wakeTime, ChronoUnit.HOURS))
            sleeping = false
            sleepButton.text = getString(R.string.sleep)
            timestamp.text = getString(R.string.from,sleepTime.toString(),wakeTime.toString())
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        val prefsEditor = this.getSharedPreferences(PREFS_FILENAME,0).edit()
        prefsEditor.putBoolean("sleep",sleeping)
        prefsEditor.putString("sleepTime",sleepTime.toString())
        prefsEditor.putString("wakeTime",wakeTime.toString())
        prefsEditor.commit()
    }
}
