package com.example.kintest

import com.example.tools.LogUtils
import org.kin.sdk.base.KinAccountContext
import org.kin.sdk.base.KinEnvironment
import org.kin.sdk.base.models.*
import org.kin.sdk.base.network.services.AppInfoProvider
import org.kin.sdk.base.repository.InvoiceRepository
import org.kin.sdk.base.stellar.models.NetworkEnvironment
import org.kin.sdk.base.storage.KinFileStorage
import org.kin.sdk.base.tools.Promise
import org.kin.sdk.base.tools.toByteArray
import java.util.*

object KinBaseManager {


    private val testKinEnvironment: KinEnvironment.Agora by lazy {
        KinEnvironment.Agora.Builder(NetworkEnvironment.KinStellarTestNet)
            .setAppInfoProvider(object : AppInfoProvider {
                override val appInfo: AppInfo =
                    AppInfo(
                        AppIdx(1),
                        KinAccount.Id("GAIVIT7SMFS2F6Z37OZWXJSGI3XKSUH2TODFB3LX32U4NYCLRBG353GP"),
                        "Tapatalk",
                        R.drawable.ic_launcher_background
                    )

                override fun getPassthroughAppUserCredentials(): AppUserCreds {
                    return AppUserCreds("demo_app_uid", "demo_app_user_passkey")
                }
            })
            .setStorage(KinFileStorage.Builder("${KinTestApplication.getInstance().applicationContext.filesDir}/kin"))
            .build()
            .apply {
                addDefaultInvoices(invoiceRepository)
            }
    }

    private val kinAccountContext by lazy {
        KinAccountContext.Builder(testKinEnvironment)
            .useExistingAccount(KinAccount.Id("GAIVIT7SMFS2F6Z37OZWXJSGI3XKSUH2TODFB3LX32U4NYCLRBG353GP"))
            .build()
    }

    fun importPrivateKey() {
        runOnProcess {
            testKinEnvironment.importPrivateKey(Key.PrivateKey("SDYCLWHNWIWP2SQU4Q4WOW6OI675ZTWDRDHEI63SIZVJBF2W6HFYQIBN"))
                .then {
                    LogUtils.log(description = "Add Private Key State:$it")
                }
        }
    }

    fun createNewWallet() {
        runOnProcess {
            kinAccountContext.apply {
                val newWallet =
                    KinAccountContext.Builder(testKinEnvironment).createNewAccount().build()
                LogUtils.e(msg = "NewAccountId:${newWallet.accountId.encodeAsString()}")
                newWallet.getAccount().then {
                    LogUtils.e(msg = it.balance.amount.value.toEngineeringString())
                }
//                newWallet.getAccount().doOnResolved {
//                    LogUtils.e(msg = "Get KinAccount Success.")
//                }
            }
//            testClient.apply {
//                val newWallet = this.addAccount()
//                LogUtils.e(
//                    "新的钱包已经添加，参数如下：", "PublicAddress:${newWallet.publicAddress}\n" +
//                            "PrivateAddress:${newWallet.export("Tpt#1234")}"
//                )
//            }
        }
    }

    fun sendKinPayment() {
        runOnProcess {
            testKinEnvironment.importPrivateKey(Key.PrivateKey("SDYCLWHNWIWP2SQU4Q4WOW6OI675ZTWDRDHEI63SIZVJBF2W6HFYQIBN"))
                .then {
                    LogUtils.e(msg = "添加私钥的状态:${it}")
                    if (it) {
                        kinAccountContext.sendKinPayment(
                            KinAmount(500L),
                            KinAccount.Id("GDMJAFM62WFJ6AZSP3F55J5ECWXDMUDKSAAIFR4KSJMJCQVY2Y6KPNKB"),
                            KinBinaryMemo.Builder(AppIdx(1).value)
                                .setTranferType(KinBinaryMemo.TransferType.P2P)
                                .build()
                                .toKinMemo()
//                    Optional.empty()
                        ).then({ kinPayment ->
                            LogUtils.e(
                                msg = "交易详情：${kinPayment.amount.value}\n" +
                                        "${kinPayment.sourceAccountId.value}\n" +
                                        "${kinPayment.fee}\n" +
                                        "${kinPayment.id}\n" +
                                        "${kinPayment.invoice}\n" +
                                        "${kinPayment.memo}\n" +
                                        "${kinPayment.sourceAccountId}\n" +
                                        "${kinPayment.status}\n" +
                                        "${kinPayment.timestamp}\n"
                            )
                        }, { throwable ->
                            throwable.printStackTrace()
                        })
                    }
                }
        }
    }

    fun checkBalance() {
        runOnProcess {
            val ob = kinAccountContext.observeBalance()
            ob.add {
                LogUtils.e(msg = "${it.amount}")
                LogUtils.e(msg = "${it.pendingAmount}")
                ob.dispose()
            }
        }
    }

//    fun printClientData() {
//        runOnProcess {
//            testClient.apply {
//                LogUtils.e("Has account or not:", this.hasAccount().toString())
//                LogUtils.e("AppID:", this.appId)
//                LogUtils.e("storeKey:", this.storeKey)
//                LogUtils.e("AccountCount:", this.accountCount.toString())
//            }
//        }
//    }

//    fun printAllWalletData() {
//        runOnProcess {
//            if (testClient.accountCount > 0) {
//                for (i in 0 until testClient.accountCount) {
//                    LogUtils.e(
//                        "所有钱包的信息如下：\n钱包${i}:",
//                        "PublicAddress:${testClient.getAccount(i).publicAddress}\n" +
//                                "PrivateAddress:${testClient.getAccount(i).export("Tpt#1234")}"
//                    )
//                }
//            }
//        }
//    }

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

//    fun printFirstWalletData() {
//
//        if (!testClient.hasAccount()) {
//            LogUtils.e(msg = "没有账号啊宝贝")
//            return
//        }
//        runOnProcess {
//            try {
//                LogUtils.e("余额为：", "${testClient.getAccount(0).balanceSync}")
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//        }
//
//    }

//    fun fundForFirstWallet() {
//        val accountContext =
//            InMemoryKinAccountContextRepositoryImpl(testKinEnvironment).getKinAccountContext(
//                org.kin.sdk.base.models.KinAccount.Id(testClient.getAccount(0).publicAddress!!)
//            )
//        (accountContext as KinAccountContextImpl).service
//            .testService
//            .fundAccount(accountContext.accountId)
//            .then {
//                it.balance.amount
//                it.balance.pendingAmount
//            }
//    }

    private fun addDefaultInvoices(invoiceRepository: InvoiceRepository) {
        val invoice1 = Invoice.Builder().apply {
            addLineItem(
                LineItem.Builder("Boombox Badger Sticker", KinAmount(25))
                    .setDescription("Let's Jam!")
                    .setSKU(
                        SKU(
                            UUID.fromString("8b154ad6-dab8-11ea-87d0-0242ac130003").toByteArray()
                        )
                    )
                    .build()
            )
            addLineItem(
                LineItem.Builder("Relaxer Badger Sticker", KinAmount(25))
                    .setDescription("#HammockLife")
                    .setSKU(
                        SKU(
                            UUID.fromString("964d1730-dab8-11ea-87d0-0242ac130003").toByteArray()
                        )
                    )
                    .build()
            )
            addLineItem(
                LineItem.Builder("Classic Badger Sticker", KinAmount(25))
                    .setDescription("Nothing beats the original")
                    .setSKU(
                        SKU(
                            UUID.fromString("cc081bd6-dab8-11ea-87d0-0242ac130003").toByteArray()
                        )
                    )
                    .build()
            )
        }.build()

        val invoice2 = Invoice.Builder().apply {
            addLineItem(
                LineItem.Builder("Fancy Tunic of Defence", KinAmount(42))
                    .setDescription("+40 Defence, -9000 Style")
                    .setSKU(
                        SKU(
                            UUID.fromString("a1b4a796-dab8-11ea-87d0-0242ac130003").toByteArray()
                        )
                    )
                    .build()
            )
            addLineItem(
                LineItem.Builder("Wizard Hat", KinAmount(99)).setDescription("+999 Mana")
                    .setSKU(
                        SKU(
                            UUID.fromString("a911cae6-dab8-11ea-87d0-0242ac130003").toByteArray()
                        )
                    )
                    .build()
            )
        }.build()

        val invoice3 = Invoice.Builder().apply {
            addLineItem(
                LineItem.Builder("Start a Chat", KinAmount(50))
                    .setSKU(
                        SKU(
                            UUID.fromString("cfe1f0b0-dab8-11ea-87d0-0242ac130003").toByteArray()
                        )
                    )
                    .build()
            )
        }.build()

        Promise.all(
            invoiceRepository.addInvoice(invoice1),
            invoiceRepository.addInvoice(invoice2),
            invoiceRepository.addInvoice(invoice3)
        ).resolve()
    }

    private fun runOnProcess(method: () -> Unit) {
        Thread(Runnable { method() }).start()
    }

}