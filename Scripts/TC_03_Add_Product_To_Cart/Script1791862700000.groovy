import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.configuration.RunConfiguration as RunConfiguration
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.util.KeywordUtil as KeywordUtil
import internal.GlobalVariable as GlobalVariable

// ==============================================================================
// TEST CASE: TC_03_Add_Product_To_Cart (Positive Scenario)
// Skenario: Login, tambah item ke keranjang, verifikasi halaman keranjang
// ==============================================================================

// 1. Dynamic Path (Project-Relative Path untuk APK)
String appPath = RunConfiguration.getProjectDir() + '/androidapp/SauceLabs.apk'

KeywordUtil.logInfo("=== START TC_03_ADD_PRODUCT_TO_CART ===")
KeywordUtil.logInfo("Membuka aplikasi dari path dinamis: " + appPath)

// 2. Start Application di HP Samsung
Mobile.startApplication(appPath, false)

// 3. Login dengan kredensial valid
Mobile.waitForElementPresent(findTestObject('page_Login/input_Username'), 20)
Mobile.setText(findTestObject('page_Login/input_Username'), GlobalVariable.valid_username, 10)
Mobile.setText(findTestObject('page_Login/input_Password'), GlobalVariable.valid_password, 10)
Mobile.tap(findTestObject('page_Login/btn_Login'), 10)

// 4. Verifikasi katalog produk terbuka
Mobile.waitForElementPresent(findTestObject('page_Products/text_Header_Products'), 20)
KeywordUtil.logInfo(">> Katalog produk berhasil dimuat.")

// 5. Tap 'ADD TO CART' pada produk pertama
Mobile.waitForElementPresent(findTestObject('page_Products/btn_Add_To_Cart'), 15)
Mobile.tap(findTestObject('page_Products/btn_Add_To_Cart'), 10)
KeywordUtil.logInfo(">> Tombol ADD TO CART berhasil di-tap.")

// 6. Tap Icon Keranjang Belanja (Cart)
Mobile.waitForElementPresent(findTestObject('page_Products/btn_Cart'), 10)
Mobile.tap(findTestObject('page_Products/btn_Cart'), 10)

// 7. Assertion: Verifikasi halaman keranjang terbuka
Mobile.waitForElementPresent(findTestObject('page_Products/text_Header_Cart'), 15)
Mobile.verifyElementExist(findTestObject('page_Products/text_Header_Cart'), 10)
KeywordUtil.logInfo(">> Assertion Berhasil: Halaman keranjang belanja berhasil terbuka dengan produk.")

// 8. Tutup Aplikasi
Mobile.closeApplication()
KeywordUtil.logInfo("=== TC_03_ADD_PRODUCT_TO_CART PASSED ===")
