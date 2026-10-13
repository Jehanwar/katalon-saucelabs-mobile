package internal

import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.main.TestCaseMain


/**
 * This class is generated automatically by Katalon Studio and should not be modified or deleted.
 */
public class GlobalVariable {
     
    /**
     * <p>Profile default : Valid username for SauceLabs app</p>
     */
    public static Object valid_username
     
    /**
     * <p>Profile default : Valid password for SauceLabs app</p>
     */
    public static Object valid_password
     
    /**
     * <p>Profile default : Locked out user for negative scenario</p>
     */
    public static Object invalid_username
     
    /**
     * <p>Profile default : Wrong password for negative scenario</p>
     */
    public static Object invalid_password
     

    static {
        try {
            def selectedVariables = TestCaseMain.getGlobalVariables("default")
			selectedVariables += TestCaseMain.getGlobalVariables(RunConfiguration.getExecutionProfile())
    
            valid_username = selectedVariables['valid_username']
            valid_password = selectedVariables['valid_password']
            invalid_username = selectedVariables['invalid_username']
            invalid_password = selectedVariables['invalid_password']
            
        } catch (Exception e) {
            TestCaseMain.logGlobalVariableError(e)
        }
    }
}
