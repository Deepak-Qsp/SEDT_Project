package com.autdeskcrm.conatcttest;

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

public class CreateAndDeleteContactWithOrgNameTest {

	@Test
	public void createAndDeletContactWithOrgTest() throws Throwable {
		
		WebDriverUtils wLib = new WebDriverUtils();
		FileLib fLib = new FileLib();
		ExcelLib excelLib = new ExcelLib();

		/*Read data from properties file*/
		String BROWSER = fLib.getPropertyKeyValue("browser");
		String URL = fLib.getPropertyKeyValue("url");
		String USERNAME = fLib.getPropertyKeyValue("username");
		String PASSWORD = fLib.getPropertyKeyValue("password");

		/* Read test script specific data*/
		String orgName = excelLib.getExcelData("contact", 4, 2)+ "_"+ wLib.getRamDomNum();
		String org_Type = excelLib.getExcelData("contact", 4, 3);
		String org_industry = excelLib.getExcelData("contact", 4, 4);
		String contactName = excelLib.getExcelData("contact", 4, 5);
		String selectLabel = excelLib.getExcelData("contact", 4, 6);
		String alertMsg = excelLib.getExcelData("contact", 4, 7);
		String expContactSearchRes = excelLib.getExcelData("contact", 4, 8);
		String expOrgSearchRes = excelLib.getExcelData("contact", 4, 9);
		
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
		driver.findElement(By.name("accountname")).sendKeys(orgName);
		
		WebElement  swb1 = driver.findElement(By.name("accounttype"));
        wLib.select(swb1, org_Type);
				
		WebElement  swb2 = driver.findElement(By.name("industry"));
       wLib.select(swb2, org_industry);
		driver.findElement(By.cssSelector(".crmbutton.small.save")).click();
		
		
		/*step 6 : verify the Org*/
		WebElement seccesMsgLoc = driver.findElement(By.xpath("//span[@class='dvHeaderText']"));
		wLib.waitForAnyElement(driver, seccesMsgLoc);
		String actOrgName = seccesMsgLoc.getText();

		Assert.assertTrue(actOrgName.contains(orgName),"Org creation Fail");
		
		/*step 7 : navigate to Contact page*/
		driver.findElement(By.linkText("Contacts")).click();
		
		/*step 8 : navigate to create new Contact page*/
		driver.findElement(By.cssSelector("img[title='Create Contact...']")).click();
				
		/*step 9 : creat new Contact page*/
		driver.findElement(By.name("lastname")).sendKeys(contactName);
		driver.findElement(By.xpath("//input[@name='account_name']/following-sibling::img")).click();
		
		   //open new tab
	     wLib.switchToPoupWindow(driver);
		
		driver.findElement(By.name("search_text")).sendKeys(orgName);
		driver.findElement(By.name("search")).click();
		driver.findElement(By.linkText(orgName)).click();
		
		//come back to parent Window
		wLib.switchToMainWindow(driver);
		driver.findElement(By.cssSelector(".crmbutton.small.save")).click();
		
		/*step  10 : verify the Contact*/
		String actconatct = driver.findElement(By.xpath("//span[@class='dvHeaderText']")).getText();
		Assert.assertTrue(actconatct.contains(contactName));
		
		/*step 11 : navigate to Contact page */
		driver.findElement(By.linkText("Contacts")).click();
		
		/*step 12 : select the newly created contact*/
		driver.findElement(By.name("search_text")).sendKeys(orgName);
		WebElement sWb = driver.findElement(By.xpath("//div[@id='basicsearchcolumns_real']/select"));
		wLib.select(sWb, selectLabel);
		driver.findElement(By.name("submit")).click();
		WebElement searchRec = driver.findElement(By.xpath("//td[contains(text(),'Showing Records 1 - 1 of 1')]"));
		wLib.waitForElemnetVsibility(driver, searchRec);
				
		/*step 13 : Delete Contact*/
		driver.findElement(By.name("selected_id")).click();
		driver.findElement(By.cssSelector(".crmbutton.small.delete")).click();
		    /*Validate alert*/
		Assert.assertTrue(wLib.getAlertText(driver).contains(alertMsg) ,"Alert Validation Fails");
		wLib.alertOk(driver);
		
		/*step 14 : cross check the deletion of contact */
		WebElement searchField = driver.findElement(By.name("search_text"));
		searchField.clear();
		searchField.sendKeys(orgName);
		WebElement sWb1 = driver.findElement(By.xpath("//div[@id='basicsearchcolumns_real']/select"));
		wLib.select(sWb1, selectLabel);
		driver.findElement(By.name("submit")).click();
			/*Validation of no Contact found msg*/
		String actSecContSearchRes = driver.findElement(By.xpath("//span[@class='genHeaderSmall']")).getText().trim();
		Assert.assertTrue(actSecContSearchRes.contains(expContactSearchRes),"Validation of \"No Contact Found\" Fails");
		
		/*step 15 : delete the Org*/
			/*navigate to Org page */
		driver.findElement(By.linkText("Organizations")).click();
		
			/*select the org*/
		driver.findElement(By.name("search_text")).sendKeys(orgName);
		WebElement sWb2 = driver.findElement(By.xpath("//div[@id='basicsearchcolumns_real']/select"));
		wLib.select(sWb2, selectLabel);
		driver.findElement(By.name("submit")).click();
		WebElement searchRec1 = driver.findElement(By.xpath("//td[contains(text(),'Showing Records 1 - 1 of 1')]"));
		wLib.waitForElemnetVsibility(driver, searchRec1);
				
			/*Delete Org*/
		driver.findElement(By.name("selected_id")).click();
		driver.findElement(By.cssSelector(".crmbutton.small.delete")).click();
		    /*Validate alert*/
		Assert.assertTrue(wLib.getAlertText(driver).contains(alertMsg) ,"Alert Validation Fails");
		wLib.alertOk(driver);
			/*cross chk the deletion of org */
		WebElement searchField1 = driver.findElement(By.name("search_text"));
		searchField.clear();
		searchField.sendKeys(orgName);
		WebElement sWb3 = driver.findElement(By.xpath("//div[@id='basicsearchcolumns_real']/select"));
		wLib.select(sWb3, selectLabel);
		driver.findElement(By.name("submit")).click();
			/*Validation of no org found msg*/
		String actSecOrgSearchRes = driver.findElement(By.xpath("//span[@class='genHeaderSmall']")).getText().trim();
		Assert.assertTrue(actSecOrgSearchRes.contains(expOrgSearchRes),"Validation of \"No Org Found\" Fails");
		
		/*step 16 : logout*/
		/*WebElement admin = driver.findElement(By.xpath("//span[text()=' Administrator']/../following-sibling::td[1]/img"));*/
		WebElement admin = driver.findElement(By.xpath("//img[@src='themes/softed/images/user.PNG']"));
		wLib.moveMouseToElemnet(driver, admin);
		driver.findElement(By.linkText("Sign Out")).click();

		/*step 17 : close browse*/
		driver.close();

		


	}
}
