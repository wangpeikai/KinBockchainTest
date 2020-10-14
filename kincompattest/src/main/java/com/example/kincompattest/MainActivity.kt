package com.example.kincompattest

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private val alertDialog by lazy {
        AlertDialog.Builder(this)
            .setMessage("On Adding New Account......")
            .setCancelable(false)
            .create()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initListener()
    }

    private fun initListener() {
        btn_print_client_data.setOnClickListener(this)

        btn_clear_all_wallet.setOnClickListener(this)

        btn_print_all_wallet_data.setOnClickListener(this)

        btn_print_first_wallet_data.setOnClickListener(this)

        btn_create_new_wallet_compat.setOnClickListener(this)
    }


    override fun onClick(view: View?) {
        when (view) {
            btn_print_client_data -> {
                KinBaseCompatManager.printClientData(tv_log)
            }
            btn_create_new_wallet_compat -> {
                alertDialog.setCanceledOnTouchOutside(false)
                alertDialog.show()
                KinBaseCompatManager.createNewWallet { content ->
                    tv_log.text = content
                    runOnUiThread { alertDialog.hide() }
                }
            }
            btn_clear_all_wallet -> {
                KinBaseCompatManager.clearAllWallets {
                    tv_log.text = "Has been clearing all accounts."
                }
            }
            btn_print_first_wallet_data -> {
                KinBaseCompatManager.printFirstWalletData {
                    tv_log.text = it
                }
            }
            btn_print_all_wallet_data -> {
                alertDialog.setCanceledOnTouchOutside(false)
                alertDialog.show()
                KinBaseCompatManager.printAllWalletData {
                    tv_log.text = it
                    runOnUiThread { alertDialog.hide() }
                }
            }
        }
    }

}