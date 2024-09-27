package com.thx.resourcelib.ext

import android.app.Activity
import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NdefRecord
import android.nfc.NfcAdapter
import android.nfc.tech.IsoDep
import android.nfc.tech.MifareClassic
import android.nfc.tech.Ndef
import android.nfc.tech.NdefFormatable
import android.nfc.tech.NfcA
import android.nfc.tech.NfcB
import android.nfc.tech.NfcF
import android.nfc.tech.NfcV
import android.util.Log
import com.thx.resourcelib.entity.NFCCardInfo
import java.lang.ref.WeakReference

/**
 * @Description:
 * @Author: tanghx
 * @Version: V1.00
 * @Create Date: 2024/7/9 6:15 PM
 */
class ReadNFCHelper {

    /* ======================================================= */
    /* Fields                                                  */
    /* ======================================================= */

    /** activity */
    private var mActivity: WeakReference<Activity>? = null

    /** NFC适配器 */
    private var nfcAdapter: NfcAdapter? = null

    /** NFC通知Intent */
    private var pendingIntent: PendingIntent? = null

    /** NFC卡片过滤器列表 */
    private var intentFiltersArray: Array<IntentFilter> = arrayOf()

    /** NFC卡种列表 */
    private var techListsArray: Array<Array<String>> = arrayOf()

    /** NFC相关错误回调 */
    private var errorCallBack: ((String) -> Unit)? = null

    /** NFC读取内容回调 */
    private var nfcContentCallBack: ((NFCCardInfo) -> Unit)? = null


    companion object {
        val mInstance: ReadNFCHelper by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            ReadNFCHelper()
        }
    }


    /* ======================================================= */
    /* Private Methods                                         */
    /* ======================================================= */

    /**
     * 检查NFC当前状态
     */
    private fun checkNFCIsEnable(): Boolean {
        val context = mActivity?.get() ?: return false
        nfcAdapter = NfcAdapter.getDefaultAdapter(context)
        return if (nfcAdapter == null) {
            // 不支持NFC
            errorCallBack?.invoke(
                getResourceString(
                    context.applicationContext,
                    com.thx.resourcelib.R.string.cant_support_nfc
                )
            )
            false
        } else if (true != nfcAdapter?.isEnabled) {
            // NFC未打开
            errorCallBack?.invoke(
                getResourceString(
                    context.applicationContext,
                    com.thx.resourcelib.R.string.nfc_disenable_msg
                )
            )
            false
        } else {
            true
        }
    }

    /**
     * NFC 卡片内容解析
     */
    private fun processTECHIntent(intent: Intent?) {
        val context = mActivity?.get() ?: return
        val tag = intent?.getParcelableExtra<android.nfc.Tag>(NfcAdapter.EXTRA_TAG)
        if (tag == null) {
            errorCallBack?.invoke(
                getResourceString(
                    context.applicationContext,
                    com.thx.resourcelib.R.string.nfc_not_found_anyone
                )
            )
            return
        }
        val nfcContentInfo = NFCCardInfo()
        val tagInfo = StringBuilder()
        tagInfo.append("Tag ID: ").append(bytesToHex(tag.id)).append("\n")
        nfcContentInfo.tagID = "${bytesToHex(tag.id)}"

        val tagTechList = arrayListOf<String>()
        for (tech in tag.techList) {
            tagInfo.append("Tech: ").append(tech).append("\n")
            tagTechList.add(tech)
        }
        nfcContentInfo.techList = tagTechList

        for (tech in tag.techList) {
            when (tech) {
                "android.nfc.tech.NfcA" -> {
                    val nfcA = NfcA.get(tag)
                    tagInfo.append("NfcA: ").append(nfcA.toString()).append("\n")
                    nfcContentInfo.tagType = "NFC-A（ISO 14443-3A）"
                }

                "android.nfc.tech.NfcB" -> {
                    val nfcB = NfcB.get(tag)
                    tagInfo.append("NfcB: ").append(nfcB.toString()).append("\n")
                    nfcContentInfo.tagType = "NFC-B (ISO 14443-3B)"
                }

                "android.nfc.tech.NfcF" -> {
                    val nfcF = NfcF.get(tag)
                    tagInfo.append("NfcF: ").append(nfcF.toString()).append("\n")
                    nfcContentInfo.tagType = "NFC-F (JIS 6319-4)"
                }

                "android.nfc.tech.NfcV" -> {
                    val nfcV = NfcV.get(tag)
                    tagInfo.append("NfcV: ").append(nfcV.toString()).append("\n")
                    nfcContentInfo.tagType = "NFC-V (ISO 15693)"
                }

                "android.nfc.tech.IsoDep" -> {
                    val isoDep = IsoDep.get(tag)
                    tagInfo.append("IsoDep: ").append(isoDep.toString()).append("\n")
                    nfcContentInfo.tagType = "ISO-DEP (ISO 14443-4)"
                }

                "android.nfc.tech.Ndef" -> {
                    val ndef = Ndef.get(tag)
                    tagInfo.append("Ndef: ").append(ndef.toString()).append("\n")
                    ndef.cachedNdefMessage.records.forEach {
                        if (String(
                                it.type,
                                java.nio.charset.StandardCharsets.UTF_8
                            ) == "android.com:pkg"
                        ) {
                            val value = String(it.payload, java.nio.charset.StandardCharsets.UTF_8)
                            tagInfo.append("Ndef: ").append("解析包名  ").append(value).append("\n")
                            nfcContentInfo.pkgContent = value
                        } else if (it.type.contentEquals(NdefRecord.RTD_URI)) {
                            val value = String(
                                it.payload.sliceArray(1 until it.payload.size),
                                java.nio.charset.StandardCharsets.UTF_8
                            )
                            tagInfo.append("Ndef: ").append("解析URI  ").append(value).append("\n")
                            nfcContentInfo.uriContent = value
                        } else {
                            val value = String(it.payload, java.nio.charset.StandardCharsets.UTF_8)
                            tagInfo.append("Ndef: ").append("解析其他Ndef数据  ").append(value)
                                .append("\n")
                        }
                    }
                }

                "android.nfc.tech.NdefFormatable" -> {
                    val ndefFormatable = NdefFormatable.get(tag)
                    tagInfo.append("NdefFormatable: ").append(ndefFormatable.toString())
                        .append("\n")
                }
            }
        }
//        Toast.makeText(this, "NFC Content: $tagInfo", Toast.LENGTH_LONG).show()
        Log.e("xiang_nfc", "===NFC Content: $tagInfo")
        // 加密扇区内容
        val mifareClassic = MifareClassic.get(tag)
        if(mifareClassic==null){
            nfcContentCallBack?.invoke(nfcContentInfo)
            return
        }
//        Log.e("xiang_nfc","====数据格式：${mifareClassic.type}")
        mifareClassic.let { tagTech ->
            try {
                tagTech.connect()
//                val sectorIndex = 1 // Replace with the sector you want to read
//                val keyA = MifareClassic.KEY_DEFAULT // Replace with your key

//                val authenticated = tagTech.authenticateSectorWithKeyA(sectorIndex, keyA)
//                if (authenticated) {
//                    val blockIndex = tagTech.sectorToBlock(sectorIndex)
//                    val data = tagTech.readBlock(blockIndex)
//                    val dataString = data.joinToString("") { String.format("%02X", it) }
//                    Toast.makeText(this, "Data: $dataString", Toast.LENGTH_LONG).show()
//                    Log.e("xiang_nfc","扇区Data: $dataString")
//                } else {
//                    Toast.makeText(this, "Authentication failed", Toast.LENGTH_LONG).show()
//                }
                // 加密的key
                val key = byteArrayOf(
                    0xC0.toByte(), 0xDC.toByte(), 0x0D, 0x37, 0xF4.toByte(), 0x84.toByte()
                )
                // 取扇区个数,循环读取
                val secCount = tagTech.sectorCount
                var auth = false
                // 识别的扇区列表
                val matchedSectionList = arrayListOf<ArrayList<String>>()
                (0 until secCount).forEach { index ->
                    auth = tagTech.authenticateSectorWithKeyB(index, key)
                    Log.e("xiang_nfc", "扇区${index}密钥匹配结果：$auth")
                    if (auth) {
                        nfcContentInfo.pwdMatchResult = true
                        // 指定扇区中第一个块的索引
                        val convertIndex = tagTech.sectorToBlock(index)
                        // 扇区中的块数
                        val blocksCount = mifareClassic.getBlockCountInSector(index)
                        val blockContentList = arrayListOf<String>()
                        for (itemBlockDataIndex in convertIndex until convertIndex + blocksCount) {
                            // 扇区中第${sectorToBlock}块的data
                            val itemBlockData = tagTech.readBlock(itemBlockDataIndex)

                            /**
                             *  // 扇区中第${sectorToBlock}块的data string
                             *  val itemBlockDataString = itemBlockData.joinToString("") { String.format("%02X", it) }
                             *  Log.e("xiang_nfc","第${index}扇区 第${itemBlockDataIndex-convertIndex}块内容：$itemBlockDataString")
                             *  if(itemBlockDataString == "F7EC8ECBFD27B816433EB979EDC73B61"){
                             *      Log.e("xiang_nfc","第${index}扇区 第${itemBlockDataIndex-convertIndex}块内容认证成功！")
                             *  }
                             */
                            val itemBlockDataString =
                                itemBlockData.joinToString("") { String.format("%02X", it) }
                            blockContentList.add(itemBlockDataString)
                        }
                        matchedSectionList.add(blockContentList)
                    }
                }
                nfcContentInfo.matchedSectionContentList = matchedSectionList
                nfcContentCallBack?.invoke(nfcContentInfo)
            } catch (e: Exception) {
                val defaultErrMsg = getResourceString(
                    context.applicationContext,
                    com.thx.resourcelib.R.string.nfc_read_error
                )
                errorCallBack?.invoke(defaultErrMsg)
//                Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_LONG).show()
            } finally {
                tagTech.close()
            }
        }
    }

    private fun bytesToHex(bytes: ByteArray): String {
        val sb = java.lang.StringBuilder()
        for (b in bytes) {
            sb.append(String.format("%02X", b))
        }
        return sb.toString()
    }


    /* ======================================================= */
    /* Public Methods                                          */
    /* ======================================================= */

    /**
     * 初始化NFC
     */
    fun initNFC(act: Activity): ReadNFCHelper {
        this.mActivity = WeakReference(act)
        if (!checkNFCIsEnable()) return this
        val context = mActivity?.get() ?: return this
        pendingIntent = PendingIntent.getActivity(
            context,
            0,
            Intent(context, context.javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
            PendingIntent.FLAG_MUTABLE
        )
        val ndef = IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED)
        try {
            ndef.addDataType("*/*")
        } catch (e: IntentFilter.MalformedMimeTypeException) {
//            throw RuntimeException("fail", e)
            errorCallBack?.invoke("NFC功能初始化失败!")
            return this
        }
        val techDiscovered = IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED)
        intentFiltersArray = arrayOf(ndef, techDiscovered)
        techListsArray = arrayOf(
            arrayOf(
                Ndef::class.java.name,
            ),
            arrayOf(
                MifareClassic::class.java.name
            )
        )
        return this
    }

    /**
     * 开启NFC读卡功能
     */
    fun enableForegroundDispatch(): ReadNFCHelper {
        val activity = mActivity?.get() ?: return this
        nfcAdapter?.enableForegroundDispatch(
            activity,
            pendingIntent,
            intentFiltersArray,
            techListsArray
        )
        return this
    }

    /**
     * 关闭NFC读卡功能
     */
    fun disableForegroundDispatch(): ReadNFCHelper {
        val activity = mActivity?.get() ?: return this
        nfcAdapter?.disableForegroundDispatch(activity)
        return this
    }

    /**
     * 处理NFC读卡结果
     */
    fun processNFCIntent(intent: Intent) {
        if (NfcAdapter.ACTION_TECH_DISCOVERED == intent.action) {
            processTECHIntent(intent)
        }
    }

    /**
     * 注册 NFC相关错误回调
     */
    fun registerOnReadErrorListener(errorCallBack: (String) -> Unit): ReadNFCHelper {
        this.errorCallBack = errorCallBack
        return this
    }

    /**
     * 注册 NFC读取内容回调
     */
    fun registerOnReadSuccessListener(nfcContentCallBack: (NFCCardInfo) -> Unit): ReadNFCHelper {
        this.nfcContentCallBack = nfcContentCallBack
        return this
    }

}