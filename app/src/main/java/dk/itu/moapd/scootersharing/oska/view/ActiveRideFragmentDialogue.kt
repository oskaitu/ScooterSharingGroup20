package dk.itu.moapd.scootersharing.oska.view

import android.app.Dialog
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import dk.itu.moapd.scootersharing.oska.R

class ActiveRideFragmentDialogue : DialogFragment() {

    private var timer: CountDownTimer? = null
    private var timeLeftInMillis = 600000L // 10 minutes in milliseconds
    private var isTimerPaused = false

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val scooterToBeChanged = MainFragment.selectedScooter

        val builder = AlertDialog.Builder(requireActivity())
            .setTitle("Ride time")
            .setMessage("You are driving ${scooterToBeChanged._name} Vroom Vroom")
            .setPositiveButton("Stop driving") { _, _ ->
                stopTimer()
                dismiss()
            }
            .setNegativeButton("Pause") { _, _ ->
                if (isTimerPaused) {
                    resumeTimer()
                } else {
                    pauseTimer()
                }
            }

        val view = View.inflate(context, R.layout.fragment_active, null)
        builder.setView(view)

        timer = object : CountDownTimer(timeLeftInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftInMillis = millisUntilFinished
                val minutes = (timeLeftInMillis / 1000) / 60
                val seconds = (timeLeftInMillis / 1000) % 60
                view.findViewById<TextView>(R.id.timer)
                    .text = String.format("%02d:%02d", minutes, seconds)
            }

            override fun onFinish() {
                stopTimer()
                dismiss()
            }
        }

        startTimer()

        return builder.create()
    }

    private fun startTimer() {
        timer?.start()
    }

    private fun pauseTimer() {
        timer?.cancel()
        isTimerPaused = true
    }

    private fun resumeTimer() {
        startTimer()
        isTimerPaused = false
    }

    private fun stopTimer() {
        timer?.cancel()
        timeLeftInMillis = 0
    }
}
