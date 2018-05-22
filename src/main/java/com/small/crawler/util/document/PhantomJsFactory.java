package com.small.crawler.util.document;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.small.crawler.util.CrawlerUtil;
import com.small.crawler.util.captcha.YunImageIdentify;

/**
 * @author xinheng-cqb
 * @date 2018年4月20日
 * @introduce: 通过PhantomJs来获取页面，能同事加载该页面的js内容
 */
public class PhantomJsFactory {

	public static void main(String[] args) {

		PhantomJSDriver driver = getHeadlessDriver();
		driver.manage().window().maximize();
		// WebDriver driver = debugWebPage();
		driver.navigate().to("https://login.youzan.com/sso/index?service=kdt");
		try {
			TimeUnit.SECONDS.sleep(3);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		WebElement userNameElement = driver.findElement(By.name("mobile"));
		WebElement pwdElement = driver.findElement(By.name("password"));
		userNameElement.sendKeys("name");
		pwdElement.sendKeys("password");

		WebElement imageElement = driver.findElement(By.className("captcha-img"));
		String imagePath = partOfScreenshot(driver, imageElement);
		// String imagePath = debugPartOfScreenshot(driver, imageElement);
		System.out.println(imagePath);
		String result = YunImageIdentify.invoke(imagePath);
		System.out.println(result);
		driver.findElement(By.name("captcha_code")).sendKeys(result);

		WebElement loginButton = driver.findElement(By.xpath("/html/body/div[2]/div/div/div[1]/div/div[1]/div[2]/form/fieldset/div[5]/button"));
		loginButton.click();
		try {
			TimeUnit.SECONDS.sleep(3);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		String windowHandle = driver.getWindowHandle();
		driver.switchTo().window(windowHandle);
		File srcFile = driver.getScreenshotAs(OutputType.FILE);
		System.out.println(srcFile.getAbsolutePath());
		driver.findElement(By.xpath("//*[@id=\"js-react-container\"]/div/div[2]/div[2]/div/ul/li[1]/div[1]/span")).click();
		try {
			TimeUnit.SECONDS.sleep(2);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		windowHandle = driver.getWindowHandle();
		driver.switchTo().window(windowHandle);
		StringBuilder sb = new StringBuilder();
		for (Cookie cookie : driver.manage().getCookies()) {
			sb.append(MessageFormat.format("{0}={1}; ", cookie.getName(), cookie.getValue()));
		}

		driver.quit();
		CrawlParam crawlParam = new CrawlParam();
		crawlParam.setCookie(sb.toString());
		crawlParam
				.setUrlStr("https://www.youzan.com/v2/ump/promocode/list.json?csrf_token=91977527457320982901617978885907513905834341339066427150751935061982940744644&keyword=&p=1&type=all&orderby=created_time&order=desc&page_size=100&disable_express_type=");
		System.out.println(HttpURLConnectionFactory.getDocumentStr(crawlParam).substring(0, 57));

	}

	/**
	 * @introduce: 获取无界面的驱动器，返回的驱动通过driver.get(url)形式来调用，driver使用结束后需要调用.quit()来关闭
	 * @return WebDriver
	 */
	public static PhantomJSDriver getHeadlessDriver() {
		String programPath = CrawlerUtil.getResourcePath("phantomjs/phantomjs.exe");
		DesiredCapabilities caps = new DesiredCapabilities();
		caps.setJavascriptEnabled(true);
		caps.setCapability("takesScreenshot", true);
		caps.setCapability(PhantomJSDriverService.PHANTOMJS_PAGE_CUSTOMHEADERS_PREFIX + "Accept-Language", "en-US");
		caps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, programPath);
		caps.setCapability("phantomjs.page.settings.userAgent",
				"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36");
		caps.setCapability("phantomjs.page.customHeaders.User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36");
		ArrayList<String> cliArgsCap = new ArrayList<String>();
		cliArgsCap.add("--web-security=false");
		cliArgsCap.add("--ssl-protocol=any");
		cliArgsCap.add("--ignore-ssl-errors=true");
		((DesiredCapabilities) caps).setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS, cliArgsCap);
		return new PhantomJSDriver(caps);

	}

	/**
	 * @introduce: 用无界面的驱动器，根据指定的webElement进行截图
	 * @param driver
	 * @param imageElement
	 * @return String
	 */
	public static String partOfScreenshot(PhantomJSDriver driver, WebElement imageElement) {
		// 截图整个页面
		File screen = driver.getScreenshotAs(OutputType.FILE);
		try {
			BufferedImage img = ImageIO.read(screen);
			// 获得元素的高度和宽度
			int width = imageElement.getSize().getWidth();
			int height = imageElement.getSize().getHeight();
			// 创建一个矩形使用上面的高度，和宽度
			Rectangle rect = new Rectangle(width, height);
			Point p = imageElement.getLocation();
			BufferedImage dest = img.getSubimage(p.getX(), p.getY(), rect.width, rect.height);
			// 存为png格式
			ImageIO.write(dest, "png", screen);
			return screen.getAbsolutePath();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @introduce: 用实际的谷歌浏览器来调试使用，返回的驱动通过driver.navigate().to(url)形式来调用，driver使用结束后需要调用.close()来关闭
	 * @return WebDriver
	 */
	public static WebDriver debugWebPage() {

		String programPath = CrawlerUtil.getResourcePath("phantomjs/chromedriver.exe");
		DesiredCapabilities caps = new DesiredCapabilities();
		System.setProperty(ChromeDriverService.CHROME_DRIVER_EXE_PROPERTY, programPath);
		caps.setJavascriptEnabled(true);
		caps.setCapability("takesScreenshot", true);
		return new ChromeDriver(caps);
	}

	/**
	 * @introduce: 用实际的谷歌浏览器来调试，根据指定的webElement进行截图
	 * @param driver
	 * @param imageElement
	 * @return String
	 */
	public static String debugPartOfScreenshot(WebDriver driver, WebElement imageElement) {
		// 截图整个页面
		File screen = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		try {
			BufferedImage img = ImageIO.read(screen);
			// 获得元素的高度和宽度
			int width = imageElement.getSize().getWidth();
			int height = imageElement.getSize().getHeight();
			// 创建一个矩形使用上面的高度，和宽度
			Rectangle rect = new Rectangle(width, height);
			Point p = imageElement.getLocation();
			BufferedImage dest = img.getSubimage(p.getX(), p.getY(), rect.width, rect.height);
			// 存为png格式
			ImageIO.write(dest, "png", screen);
			return screen.getAbsolutePath();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
