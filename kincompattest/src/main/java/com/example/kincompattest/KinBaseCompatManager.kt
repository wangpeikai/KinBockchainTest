package com.example.kincompattest

import android.annotation.SuppressLint
import android.widget.TextView
import com.example.tools.LogUtils
import kin.sdk.Environment
import kin.sdk.KinAccount
import kin.sdk.KinClient
import kotlinx.coroutines.*

object KinBaseCompatManager {

    private const val NO_ACCOUNT = "No Account."

    //Init KinClient
    private val testClient: KinClient by lazy {
        KinClient(
            KinCompatTestApplication.getInstance().applicationContext,
            Environment.TEST,
            "appi",
            "testnet"
        )
    }

    //Init MainClient
    private val mainNet: KinClient by lazy {
        KinClient(
            KinCompatTestApplication.getInstance().applicationContext,
            Environment.PRODUCTION,
            "appi",
            "mainnet"
        )
    }

    @SuppressLint("SetTextI18n")
    fun printClientData(view: TextView) {
        runOnCoroutine {
            testClient.apply {
                view.text = "Has account or not:${hasAccount()}\n" +
                        "AppID:${appId}\n" +
                        "storeKey:${storeKey}\n" +
                        "AccountCount:$accountCount"
                LogUtils.e("Has account or not:", hasAccount().toString())
                LogUtils.e("AppID:", appId)
                LogUtils.e("storeKey:", storeKey)
                LogUtils.e("AccountCount:", accountCount.toString())
            }
        }
    }

    fun createNewWallet(method: (kinAccountContent: String) -> Unit) {
        runOnCoroutine {
            testClient.addAccount().apply {
                method(
                    "添加账号成功!\n" +
                            "公钥：${this.publicAddress}\n" +
                            "余额：${this.balanceSync.value()}\n" +
                            "状态：${this.statusSync}"
                )
            }
        }

    }

    //TODO
//    fun doPayment() {
//        if (testClient.accountCount < 2) {
//            Toast.makeText(
//                KinTestApplication.getInstance().applicationContext,
//                "测试环境下请确保Wallet数量大于2",
//                Toast.LENGTH_SHORT
//            ).show()
//            return
//        }
//        val wallet1 = testClient.getAccount(0).balance.run(object : ResultCallback<Balance?> {
//            override fun onResult(result: Balance?) {}
//            override fun onError(e: Exception) {}
//        })
//        val wallet2 = testClient.getAccount(1)
//        //TODO payment
//    }

    fun printFirstWalletData(method: (content: String) -> Unit) {

        if (!testClient.hasAccount()) {
            LogUtils.e(msg = NO_ACCOUNT)
            method(NO_ACCOUNT)
            return
        }

        runOnCoroutine {
            try {
                method(
                    "余额为：${testClient.getAccount(0).balanceSync.value()}\n" +
                            "状态:${testClient.getAccount(0).statusSync}\n" +
                            "公钥：${testClient.getAccount(0).publicAddress}"
                )
                LogUtils.e(
                    msg = "余额为：${testClient.getAccount(0).balanceSync.value()}\n" +
                            "状态:${testClient.getAccount(0).statusSync}\n" +
                            "公钥：${testClient.getAccount(0).publicAddress}"
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    }

    fun printAllWalletData(method: (content: String) -> Unit) {
        if (!testClient.hasAccount()) {
            LogUtils.e(msg = NO_ACCOUNT)
            method(NO_ACCOUNT)
            return
        }

        runOnCoroutine {
            if (testClient.accountCount > 0) {
                var str = ""
                for (i in 0 until testClient.accountCount) {
                    LogUtils.e(
                        "所有钱包的信息如下：\n" +
                                "钱包${i}:",
                        "PublicAddress:${testClient.getAccount(i).publicAddress}\nPrivateAddress:${testClient.getAccount(
                            i
                        ).export("Tpt#1234")}"
                    )
                    str += "所有钱包的信息如下：\n" +
                            "钱包${i}:\nPublicAddress:${testClient.getAccount(i).publicAddress}\nPrivateAddress:${testClient.getAccount(
                                i
                            ).export("Tpt#1234")}\n\n\n"
                }
                method(str)
            }
        }
    }

    fun clearAllWallets(method: () -> Unit) {
        runOnCoroutine {
            testClient.clearAllAccounts().apply {
                method()
            }
        }
    }


    fun getAllWallets(): List<KinAccount> {

        val accountList = mutableListOf<KinAccount>()

        if (testClient.accountCount <= 0) {
            return accountList
        }

        for (i in 0 until testClient.accountCount) {
            accountList.add(testClient.getAccount(i))
        }

        return accountList

    }

    private fun runOnCoroutine(method: () -> Unit) {
        GlobalScope.launch {
            method()
        }
    }

    private fun test(view: TextView) = runBlocking {
        repeat(8) {
            view.text = "协程执行${it}。线程ID为：${Thread.currentThread().id}。"
            LogUtils.e(msg = "协程执行${it}。线程ID为：${Thread.currentThread().id}。")
            delay(1000)
        }
    }

    private fun test1(view: TextView) = GlobalScope.launch {
        repeat(8) {
            view.text = "协程执行${it}。线程ID为：${Thread.currentThread().id}。"
            LogUtils.e(msg = "协程执行${it}。线程ID为：${Thread.currentThread().id}。")
            delay(1000)
        }
    }

    private fun test2(view: TextView) = GlobalScope.async {

        repeat(8) {
            view.text = "协程执行${it}。线程ID为：${Thread.currentThread().id}。"
            LogUtils.e(msg = "协程执行${it}。线程ID为：${Thread.currentThread().id}。")
            delay(1000)
        }
    }


}