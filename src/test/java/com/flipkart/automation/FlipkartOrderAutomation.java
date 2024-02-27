package com.flipkart.automation;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.*;
import org.openqa.selenium.support.ui.*;
import org.testng.annotations.*;
import io.github.bonigarcia.wdm.WebDriverManager;
import java.util.*;

/**
 * This class performs automation testing on Flipkart website.
 */
public class FlipkartOrderAutomation {

	private WebDriver driver;
	private WebDriverWait wait;

	/**
	 * Method to set up the WebDriver and initialize necessary objects before test
	 * execution.
	 */
	@BeforeClass
	public void setUpWebDriver() {
		WebDriverManager.chromedriver().setup(); // Set up ChromeDriver
		driver = new ChromeDriver(); // Initialize ChromeDriver
		driver.manage().window().maximize(); // Maximize the browser window
		wait = new WebDriverWait(driver, 20); // Initialize WebDriverWait
	}

	/**
	 * Test method to complete the order process on Flipkart.
	 *
	 * @throws InterruptedException if interrupted while waiting
	 */
	@Test
	public void TC_001_testCompleteOrderProcess() throws InterruptedException {
		openWebsite("https://www.flipkart.com"); // Open Flipkart website
		searchForItem("ipad", By.name("q")); // Search for the specified item
		clickOnSuggestedItem(By.xpath("//li[@class='_3D0G9a']"), By.xpath("(//div[@class='YGcVZO _2VHNef'])[2]")); // Click on the suggested item
		applyFilter("Online Only", By.xpath("//div[contains(text(),'Brand') and @class='_2gmUFU _3V8rao']")); // Apply online only filter
		selectProduct(By.className("_4rR01T")); // Select the first search result
		checkoutAndPlaceOrder(); // Proceed to checkout and place the order
	}

	/**
	 * Method to perform cleanup after test execution.
	 */
	@AfterClass
	public void tearDown() {
		if (driver != null) {
			driver.quit(); // Quit the WebDriver instance
		}
	}

	/**
	 * Method to open a website.
	 *
	 * @param url the URL of the website to open
	 */
	private void openWebsite(String url) {
		driver.get(url); // Navigate to the specified URL
	}

	/**
	 * Method to search for an item on Flipkart.
	 *
	 * @param item    the item to search for
	 * @param locator the locator of the search box element
	 * @throws InterruptedException if interrupted while waiting
	 */
	private void searchForItem(String item, By locator) throws InterruptedException {
		WebElement searchBox = driver.findElement(locator); // Locate the search box element
		searchBox.sendKeys(item); // Enter the item to search for
		Thread.sleep(3000); // Wait for suggestions to load
	}

	/**
	 * Method to click on a suggested item.
	 *
	 * @param suggestionLocator the locator of the suggestion element
	 * @param elementLocator    the locator of the element to click within the suggestion
	 */
	private void clickOnSuggestedItem(By suggestionLocator, By elementLocator) {
		List<WebElement> suggestionList = driver.findElements(suggestionLocator); // Find suggestion elements
		for (WebElement suggestion : suggestionList) {
			WebElement element = wait.until(ExpectedConditions.elementToBeClickable(elementLocator)); // Wait for the element to be clickable
			element.click(); // Click on the element
			break; // Break loop after clicking the first suggestion
		}
	}

	/**
	 * Method to apply a filter.
	 *
	 * @param filterName the name of the filter to apply
	 * @param filterLocator the locator of the filter element
	 */
	private void applyFilter(String filterName, By filterLocator) {
		clickOnElement(filterLocator); // Click on the filter
	}

	/**
	 * Method to select a product.
	 *
	 * @param productLocator the locator of the product element
	 */
	private void selectProduct(By productLocator) {
		clickOnElement(productLocator); // Click on the product
		switchToNewWindow(); // Switch to the new window
	}

	/**
	 * Method to click on an element.
	 *
	 * @param locator the locator of the element to click
	 */
	private void clickOnElement(By locator) {
		WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator)); // Wait for the element to be clickable
		element.click(); // Click on the element
	}

	/**
	 * Method to switch to a new window.
	 */
	private void switchToNewWindow() {
		for (String handle : driver.getWindowHandles()) {
			driver.switchTo().window(handle); // Switch to the new window
		}
	}

	/**
	 * Method to proceed to checkout and place the order.
	 *
	 * @throws InterruptedException if interrupted while waiting
	 */
	private void checkoutAndPlaceOrder() throws InterruptedException {
		try {
			WebElement checkoutButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[@class='_2KpZ6l _2U9uOA _3v1-ww']")));
			checkoutButton.click();

			WebElement placeOrderButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[contains(text(),'Place Order')]")));
			placeOrderButton.click();
		} catch (TimeoutException e) {
			System.out.println("Timeout waiting for the checkout or place order button to appear.");
			e.printStackTrace();
		}

		// Enter Random Email and Phone number
		Random rand = new Random();
		String email = "test" + rand.nextInt(1000) + "@example.com"; // Generate random email
		String phoneNumber = Long.toString((long) (Math.random() * 10000000000L)); // Generate random phone number

		WebElement emailOrPhoneField = driver.findElement(By.xpath("//input[@class='_2IX_2- _17N0em']")); // Locate email or phone field
		emailOrPhoneField.click(); // Click on the field
		emailOrPhoneField.sendKeys(email); // Enter the generated email
		Thread.sleep(3000); // Wait for a moment
		emailOrPhoneField.clear(); // Clear the field
		Thread.sleep(1000); // Wait for a moment
		emailOrPhoneField.sendKeys(phoneNumber); // Enter the generated phone number
	}
}

