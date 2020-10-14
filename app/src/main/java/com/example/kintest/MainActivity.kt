package com.example.kintest

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initListener()
    }

    private fun initListener() {
        btn_create_new_wallet.setOnClickListener {
            KinBaseManager.createNewWallet()
        }

        btn_send_payment.setOnClickListener {
            KinBaseManager.sendKinPayment()
        }

        btn_check_balance.setOnClickListener {
            KinBaseManager.checkBalance()
        }
    }

    override fun onClick(view: View?) {

    }
}