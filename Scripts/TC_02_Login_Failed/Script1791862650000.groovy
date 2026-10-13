import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.configuration.RunConfiguration as RunConfiguration
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.util.KeywordUtil as KeywordUtil
import internal.GlobalVariable as GlobalVariable

// ==============================================================================
// TEST CASE: TC_02_Login_Failed (Negative Scenario)
// Skenario: Login menggunakan locked_out_user, verifikasi muncul pesan error
// ==============================================================================

// 1. Dynamic Path (Project-Relative Path untuk APK, sesuai rubrik)
String appPath = RunConfiguration.getProjectDir() + '/androidapp/SauceLabs.apk'

KeywordUtil.logInfo("=== START TC_02_LOGIN_FAILED ===")
KeywordUtil.logInfo("Membuka aplikasi dari path dinamis: " + appPath)

// 2. Start Application di HP Samsung
Mobile.startApplication(appPath, false)

// 3. Masukkan Username Terkunci & Password
Mobile.waitForElementPresent(findTestObject('page_Login/input_Username'), 20)
Mobile.setText(findTestObject('page_Login/input_Username'), GlobalVariable.invalid_username, 10)
Mobile.setText(findTestObject('page_Login/input_Password'), GlobalVariable.valid_password, 10)

// 4. Tap Tombol LOGIN
Mobile.tap(findTestObject('page_Login/btn_Login'), 10)

// 5. Assertion: Verifikasi pesan error login muncul
Mobile.waitForElementPresent(findTestObject('page_Login/text_Error_Message'), 15)
Mobile.verifyElementExist(findTestObject('page_Login/text_Error_Message'), 10)

KeywordUtil.logInfo(">> Assertion Berhasil: Pesan error login berhasil divalidasi.")

// 6. Tutup Aplikasi
Mobile.closeApplication()
KeywordUtil.logInfo("=== TC_02_LOGIN_FAILED PASSED ===")
