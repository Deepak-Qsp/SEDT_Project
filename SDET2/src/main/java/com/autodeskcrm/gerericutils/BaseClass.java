package com.autodeskcrm.gerericutils;

import java.sql.SQLException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

/**
 * @author Deepak
 *
 */
public class BaseClass {

	public WebDriver driver = null;
	public DataBaseLib dBLib = new DataBaseLib();
	public WebDriverUtils wLib = new WebDriverUtils();
	public FileLib fLib = new FileLib();
	public ExcelLib excelLib = new ExcelLib();

	@BeforeSuite
	public void configBS() throws SQLException {
		/*connect to DB*/
		dBLib.connectToDB();
	}
	/*@Parameter("browser")
	@BeforeTest
	public void configBT(String Browser) {


		if(Browser.equals("chrome")) {
			driver = new ChromeDriver();
		}else if(Browser.equals("firefox")) {
			driver = new FirefoxDriver();
		}else if(Browser.equals("ie")) {
			driver = new InternetExplorerDriver();
		}else if(Browser.equals("edge")) {
			driver = new EdgeDriver();
		}else {
			driver = new ChromeDriver();
		}

	}
	 */	
	@BeforeClass
	public void configBC() throws Throwable {
		/*launch the browser*/
		String BROWSER = fLib.getPropertyKeyValue("browser");

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
	}
	@BeforeMethod
	public void configBM() throws Throwable {

		
		/*Read data from properties file*/

		String URL = fLib.getPropertyKeyValue("url");
		String USERNAME = fLib.getPropertyKeyValue("username");
		String PASSWORD = fLib.getPropertyKeyValue("password");

		wLib.waitForPagetoLoad(driver);
		driver.get(URL);

		/*login*/
		driver.findElement(By.name("user_name")).sendKeys(USERNAME);
		driver.findElement(By.name("user_password")).sendKeys(PASSWORD);
		driver.findElement(By.id("submitButton")).click();

	}
	@AfterMethod
	public void configAM() {
		/*logout*/
		WebElement user = driver.findElement(By.xpath("//span[text()=' Administrator']/../following-sibling::td[1]/img"));
		wLib.moveMouseToElemnet(driver, user);
		driver.findElement(By.linkText("Sign Out")).click();

	}
	@AfterClass
	public void configAC() {
		/*close browse*/
		driver.close();

	}
	@AfterSuite
	public void configAS() throws Throwable {
		/*disconnect Database*/
		dBLib.disconnectDB();
	}










}
