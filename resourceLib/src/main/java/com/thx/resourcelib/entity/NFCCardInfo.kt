package com.thx.resourcelib.entity

/**
 * @Description:NFC卡读取信息
 * @Author: tanghx
 * @Version: V1.00
 * @Create Date: 2024/7/9 5:07 PM
 */

/**
 * 技术              描述	                                                            卡种
 * NfcA             提供 NFC-A（ISO 14443-3A）的性能和I / O操作的访问。	                    M1卡
 * NfcB	            提供 NFC-B (ISO 14443-3B)的性能和I / O操作的访问。
 * NfcF	            提供 NFC-F (JIS 6319-4)的性能和I / O操作的访问。
 * NfcV	            提供 NFC-V (ISO 15693)的性能和I / O操作的访问。	                        15693卡
 * IsoDep	        提供 ISO-DEP (ISO 14443-4)的性能和I / O操作的访问。	                    CPU卡
 * Ndef	            提供NFC标签已被格式化为NDEF的数据和操作的访问。
 * NdefFormatable	提供可能被格式化为NDEF的 formattable的标签。
 * MifareClassic	如果此Android设备支持MIFARE,提供访问的MIFARE Classic性能和I / O操作。	    m1卡
 * MifareUltralight	如果此Android设备支持MIFARE,提供访问的MIFARE 超轻性能和I / O操作。
 */
data class NFCCardInfo(
    /** 卡号 */
    var tagID: String = "",
    /** NFC标签类型(例如NFC-A（ISO 14443-3A）) */
    var tagType: String = "",
    /** NFC用到的技术列表 */
    var techList: ArrayList<String> = arrayListOf(),
    /** URI内容 */
    var uriContent: String = "",
    /** 应用包名内容 */
    var pkgContent: String = "",
    /** 扇区内容是否跟密码匹配 */
    var pwdMatchResult: Boolean = false,
    /** 匹配成功的扇区内容 (扇区列表<扇区内的块内容列表>) */
    var matchedSectionContentList: ArrayList<ArrayList<String>> = arrayListOf()
)

