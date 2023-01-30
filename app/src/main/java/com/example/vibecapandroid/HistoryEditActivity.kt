package com.example.vibecapandroid

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.vibecapandroid.databinding.ActivityHistoryEditBinding


class HistoryEditActivity : AppCompatActivity() {
    private val viewBinding: ActivityHistoryEditBinding by lazy {
        ActivityHistoryEditBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding.activityHistoryPostFinish.setOnClickListener() {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        viewBinding.activityHistoryPostCancel.setOnClickListener() {
            val dialogBinding = layoutInflater.inflate(R.layout.activity_history_postedit_dialog_cancel,null)

            val logoutDialog = Dialog(this)
            logoutDialog.setContentView(dialogBinding)
            logoutDialog.setCancelable(true)
            //logoutDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            logoutDialog.show()

            val cancelBtn =dialogBinding.findViewById<Button>(R.id.dialog_postdedit_cancel)
            cancelBtn.setOnClickListener{
                val intent = Intent(this, HistoryEditActivity::class.java)
                startActivity(intent)
            }

            val okayBtn =dialogBinding.findViewById<Button>(R.id.dialog_postedit_ok)
            okayBtn.setOnClickListener{
                val intent = Intent(this, VibePostActivity::class.java)
                startActivity(intent)
            }

        }
        }

    }
