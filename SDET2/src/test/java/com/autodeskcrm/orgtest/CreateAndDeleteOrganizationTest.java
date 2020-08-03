package com.autodeskcrm.orgtest;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.autodeskcrm.gerericutils.ExcelLib;
import com.autodeskcrm.gerericutils.FileLib;
import com.autodeskcrm.gerericutils.WebDriverUtils;

/**This Class will Create and delete the Organization
 * @author Deepak
 *
 */
public class CreateAndDeleteOrganizationTest {

	@Test
	public void createAndDeletOrgtest() throws Throwable {
		WebDriverUtils wLib = new WebDriverUtils();
		FileLib fLib = new FileLib();
		ExcelLib excelLib = new ExcelLib();

		/*Read data from properties file*/
		String BROWSER = fLib.getPropertyKeyValue("browser");
		String URL = fLib.getPropertyKeyValue("url");
		String USERNAME = fLib.getPropertyKeyValue("username");
		String PASSWORD = fLib.getPropertyKeyValue("password");


		/*Read test script specific data*/
		String OrgName = excelLib.getExcelData("org", 4, 2)+"_"+wLib.getRamDomNum();
		String selectLabel = excelLib.getExcelData("org", 4, 3);
		String alertMsg = excelLib.getExcelData("org", 4, 4);
		String expSearchRes = excelLib.getExcelData("org", 4, 5);
		
		/*step 1 : launch the browser*/
		WebDriver driver = null;

		if(BROWSER.equals("chrome")) {
			driver = new ChromeDriver();
		}else if(BROWSER.equals("firefox")) {
			driver = new FirefoxDriver();
		}else if(BROWSER.equals("ie")) {
			driver = new InternetExplorerDriver();
		}else if(BROWSER.equals("edge")) {
			driver = new EdgeDriver();
		}else {
			driver = new ChromeDriver();
		}

		wLib.waitForPagetoLoad(driver);
		driver.get(URL);


		/*step 2 : login*/
		driver.findElement(By.name("user_name")).sendKeys(USERNAME);
		driver.findElement(By.name("user_password")).sendKeys(PASSWORD);
		driver.findElement(By.id("submitButton")).click();

		/*step 3 : navigate to Org page*/
		driver.findElement(By.linkText("Organizations")).click();


		/*step 4 : navigate to create new Org page*/
		driver.findElement(By.cssSelector("img[title='Create Organization...']")).click();

		/*step 5 : create Org*/
		driver.findElement(By.name("accountname")).sendKeys(OrgName);
		driver.findElement(By.cssSelector(".crmbutton.small.save")).click();


		/*step 6 : verify the Org*/
		WebElement seccesMsgLoc = driver.findElement(By.xpath("//span[@class='dvHeaderText']"));
		wLib.waitForAnyElement(driver, seccesMsgLoc);
		String actOrgName = seccesMsgLoc.getText();

		Assert.assertTrue(actOrgName.contains(OrgName),"Org creation Fail");
		
		/*step 7 : navigate to Org page */
		driver.findElement(By.linkText("Organizations")).click();
		
		/*step 8 : select the newly created org*/
		driver.findElement(By.name("search_text")).sendKeys(OrgName);
		WebElement sWb = driver.findElement(By.xpath("//div[@id='basicsearchcolumns_real']/select"));
		wLib.select(sWb, selectLabel);
		driver.findElement(By.name("submit")).click();
		WebElement searchRec = driver.findElement(By.xpath("//td[contains(text(),'Showing Records 1 - 1 of 1')]"));
		wLib.waitForElemnetVsibility(driver, searchRec);
				
		/*step 9 : Delete Org*/
		driver.findElement(By.name("selected_id")).click();
		driver.findElement(By.cssSelector(".crmbutton.small.delete")).click();
		    /*Validate alert*/
		Assert.assertEquals(wLib.getAlertText(driver), alertMsg,"Alert Validation Fails");
		wLib.alertOk(driver);
		/*step 10 : cross chk the deletion of org */
		WebElement searchField = driver.findElement(By.name("search_text"));
		searchField.clear();
		searchField.sendKeys(OrgName);
		WebElement sWb1 = driver.findElement(By.xpath("//div[@id='basicsearchcolumns_real']/select"));
		wLib.select(sWb1, selectLabel);
		driver.findElement(By.name("submit")).click();
			/*Validation of no org found msg*/
		String actSecSearchRes = driver.findElement(By.xpath("//span[@class='genHeaderSmall']")).getText().trim();
		Assert.assertTrue(actSecSearchRes.contains(expSearchRes),"Validation of \"No Org Found\" Fails");
			
		/*step 11 : logout*/
		WebElement wb = driver.findElement(By.xpath("//span[text()=' Administrator']/../following-sibling::td[1]/img"));
		wLib.moveMouseToElemnet(driver, wb);
		driver.findElement(By.linkText("Sign Out")).click();

		/*step 12 : close browse*/
		driver.close();



	}
}
