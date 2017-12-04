# Apple Maps - Parallel Appium Execution
This project is thrown together to show multiple devices executing, multiple scripts via TestNG configuration files

By default, if you add additional classes to the test execution, it will execute them on separate devices.  If you want to execute a series of tests on the same device, then I found you need to add them to the same class as separate @Test parameters.

## Examples
* Running 1 test script in a class see [Maps_Memphis](/src/Maps_Memphis.java)
 * This runs a test against apple Maps looking for Memphis, and then verifing a small town around it
* Running 2 tests in one class see [Maps_Wilm_Split](/src/Maps_Wilm_Split.java)
 * This runs 2 tests doing the same function as above but against, Wilmington, DE and then Philadelphia, PA


## Updates to make
Modify the TestNG.xml file with Perfecto Username and Password along with host for the Perfecto cloud

Additionally, modify the Class Parameters as needed for different devices.  In order to add additional parameters, you will need to update the java class files
```XML
<!DOCTYPE suite SYSTEM "http://testng.org/testng-1.0.dtd" >
<suite name="Appium Suite" parallel="tests">
  	<parameter name="user" value="PERFECTO_USER"></parameter>
  	<parameter name="password" value="PERFECTO_PASSWORD"></parameter>
  	<parameter name="host" value="<PERFECTO_CLOUD>.perfectomobile.com"></parameter>
  <test name="Open  Maps on iPhone - Wilmington">
    <classes>
      <class name="Maps_Wilm_Split"/>
        <parameter name="deviceModel" value="iPhone-6"></parameter>
        <parameter name="devicePlatform" value="iOS"></parameter>
        <parameter name="deviceManufacturer" value="Apple"></parameter>
    </classes>
  </test> <!-- Test -->
</suite> <!-- Suite -->

```
