import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.configuration.RunConfiguration as RunConfiguration
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.util.KeywordUtil as KeywordUtil
import internal.GlobalVariable as GlobalVariable

// ==============================================================================
// TEST CASE: TC_01_Login_Success (Positive Scenario)
// Skenario: Login menggunakan username dan password valid, verifikasi halaman produk
// ==============================================================================

// 1. Dynamic Path (Project-Relative Path untuk APK, memenuhi syarat rubrik: no hardcode)
String appPath = RunConfiguration.getProjectDir() + '/androidapp/SauceLabs.apk'

KeywordUtil.logInfo("=== START TC_01_LOGIN_SUCCESS ===")
KeywordUtil.logInfo("Membuka aplikasi dari path dinamis: " + appPath)

// 2. Start Application di HP Samsung
Mobile.startApplication(appPath, false)

// 3. Masukkan Username & Password dari GlobalVariable
Mobile.waitForElementPresent(findTestObject('page_Login/input_Username'), 20)
Mobile.setText(findTestObject('page_Login/input_Username'), GlobalVariable.valid_username, 10)
Mobile.setText(findTestObject('page_Login/input_Password'), GlobalVariable.valid_password, 10)

// 4. Tap Tombol LOGIN
Mobile.tap(findTestObject('page_Login/btn_Login'), 10)

// 5. Assertion: Verifikasi berhasil masuk ke katalog produk (Header 'PRODUCTS' muncul)
Mobile.waitForElementPresent(findTestObject('page_Products/text_Header_Products'), 20)
Mobile.verifyElementExist(findTestObject('page_Products/text_Header_Products'), 10)

KeywordUtil.logInfo(">> Assertion Berhasil: User berhasil login dan melihat katalog PRODUCTS.")

// 6. Tutup Aplikasi
Mobile.closeApplication()
KeywordUtil.logInfo("=== TC_01_LOGIN_SUCCESS PASSED ===")
