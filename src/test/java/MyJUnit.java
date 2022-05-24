import io.opentelemetry.api.internal.Utils;
import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
//import org.jcp.xml.dsig.internal.dom.Utils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.*;

import static java.lang.Thread.sleep;

public class MyJUnit {
    WebDriver driver;
    @Before
    public void setup(){
        System.setProperty("webdriver.chrome.driver", "./src/test/resources/chromedriver.exe");
        ChromeOptions ops=new ChromeOptions();
        ops.addArguments("--headed");
        driver=new ChromeDriver(ops);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));

    }
    @Test
    public void getTitle(){
        driver.get("https://demoqa.com/");
        String title= driver.getTitle();
        System.out.println(title);
        Assert.assertEquals("ToolsQA",title);
    }
    @Test
    public void checkIfImageExists(){
        driver.get("https://demoqa.com/");
//        WebElement image1= driver.findElement(By.cssSelector("img"));
//        Assert.assertTrue(String.valueOf(image1.isDisplayed()),true);
        WebElement image2= driver.findElement(By.xpath("//body/div[@id='app']/div[1]/div[1]/div[1]/a[1]/img[1]"));
        Assert.assertTrue(String.valueOf(image2.isDisplayed()),true);

    }
          //Write on textbox
    @Test
    public void writeSomething() {
        driver.get("https://demoqa.com/text-box");
        WebElement txtUsername = driver.findElement(By.id("userName"));
//        WebElement txtUsername=driver.findElement(By.cssSelector("[type=text]"));
//        WebElement txtUsername=driver.findElement(By.cssSelector("[placeholder='Full Name']"));
//        WebElement txtUsername=driver.findElement(By.tagName("input"));
//        WebElement txtUsername = driver.findElement(By.cssSelector("input"));

        txtUsername.sendKeys("Rahim");
//        List<WebElement> elements= driver.findElements(By.tagName("input"));
//        elements.get(0).sendKeys("Rahim");
//        elements.get(1).sendKeys("rahim@test.com");


        driver.findElement(By.id("userEmail")).sendKeys("rahim@test.com");
        Actions actions=new Actions(driver);
        WebElement btnSubmit= driver.findElement(By.id("submit"));
        actions.moveToElement(btnSubmit).click().perform();
    }
    @Test
    public void clickButton(){
        driver.get("https://demoqa.com/buttons");
        List<WebElement> buttons= driver.findElements(By.tagName("button"));
        buttons.get(3).click();
        Actions actions=new Actions(driver);
        actions.doubleClick(buttons.get(1)).perform();
        actions.contextClick(buttons.get(2)).perform();

        String doubleClickMessage= driver.findElement(By.id("doubleClickMessage")).getText();
        String rightClickMessage= driver.findElement(By.id("rightClickMessage")).getText();
        String dynamicClickMessage= driver.findElement(By.id("dynamicClickMessage")).getText();

        Assert.assertTrue(doubleClickMessage.contains("You have done a double click"));
        Assert.assertTrue(rightClickMessage.contains("You have done a right click"));
        Assert.assertTrue(dynamicClickMessage.contains("You have done a dynamic click"));


    }

    @Test
    public void alertHandle() throws InterruptedException {   
        driver.get("https://demoqa.com/alerts");
        driver.findElement(By.id("alertButton")).click();
        driver.switchTo().alert().accept();
    }
    @Test
    public void alertHandlewithDelay() throws InterruptedException {
        driver.get("https://demoqa.com/alerts");
        driver.findElement(By.id("timerAlertButton")).click();
         Thread.sleep(7000);
        driver.switchTo().alert().accept();
    }

    @Test
    public void dialogBoxHandle(){
        driver.get("https://demoqa.com/alerts");
        driver.findElement(By.id("confirmButton")).click();
        driver.switchTo().alert().dismiss();
    }
    @Test
    public void promptHandle(){
        driver.get("https://demoqa.com/alerts");
        driver.findElement(By.id("promtButton")).click();
        driver.switchTo().alert().sendKeys("Hello Sazzad Hossain");
        driver.switchTo().alert().accept();
    }
    @Test
    public void selectDate(){
        driver.get("https://demoqa.com/date-picker");
        driver.findElement(By.id("datePickerMonthYearInput")).click();
        driver.findElement(By.id("datePickerMonthYearInput")).clear();
        driver.findElement(By.id("datePickerMonthYearInput")).sendKeys("04/14/2022");
        driver.findElement(By.id("datePickerMonthYearInput")).sendKeys(Keys.ENTER);
    }
    @Test
    public void selectDropdown(){
        driver.get("https://demoqa.com/select-menu");
        Select options=new Select(driver.findElement(By.id("oldSelectMenu")));
        options.selectByValue("3");
//        options.selectByIndex(2);
//        options.selectByVisibleText("Green");

    }
    @Test
    public void selectMultipleDropdown(){
        driver.get("https://demoqa.com/select-menu");
        Select options=new Select(driver.findElement(By.id("cars")));
        if(options.isMultiple()){
            options.selectByValue("volvo");
            options.selectByValue("audi");
        }
    }
    @Test
    public void handleMultipleTab() throws InterruptedException {
        driver.get("https://demoqa.com/browser-windows");
        driver.findElement(By.id("tabButton")).click();
        Thread.sleep(3000);
        ArrayList<String> w = new ArrayList(driver.getWindowHandles());
        //switch to open tab
        driver.switchTo().window(w.get(1));
        System.out.println("New tab title: " + driver.getTitle());
        String text = driver.findElement(By.id("sampleHeading")).getText();
        Assert.assertEquals(text,"This is a sample page");
        driver.close();
        driver.switchTo().window(w.get(0));
    }
    @Test
    public void handleWindow(){
        driver.get("https://demoqa.com/browser-windows");
        driver.findElement(By.id(("windowButton"))).click();
        String mainWindowHandle = driver.getWindowHandle();
        Set<String> allWindowHandles = driver.getWindowHandles();
        Iterator<String> iterator = allWindowHandles.iterator();

        while (iterator.hasNext()) {
            String ChildWindow = iterator.next();
            if (!mainWindowHandle.equalsIgnoreCase(ChildWindow)) {
                driver.switchTo().window(ChildWindow);
                String text= driver.findElement(By.id("sampleHeading")).getText();
                Assert.assertTrue(text.contains("This is a sample page"));
            }

        }

    }
    @Test
    public void modalDialog(){
        driver.get("https://demoqa.com/modal-dialogs");
        driver.findElement(By.id("showSmallModal")).click();
        String text= driver.findElement(By.className("modal-body")).getText();
        System.out.println(text);
        driver.findElement(By.id("closeSmallModal")).click();
    }
    @Test
    public void scrapData(){
        driver.get("https://demoqa.com/webtables");
        WebElement table = driver.findElement(By.className("rt-table"));
        List<WebElement> allRows = table.findElements(By.className("rt-tr"));
        int i=0;
        for (WebElement row : allRows) {
            List<WebElement> cells = row.findElements(By.className("rt-td"));
            for (WebElement cell : cells) {
                i++;
                System.out.println("num["+i+"] "+ cell.getText());

            }
        }
    }
    @Test
    public void uploadImage(){
        driver.get("https://demoqa.com/upload-download");
        driver.findElement(By.id("uploadFile")).sendKeys("C:\\Users\\SAZZAD\\Desktop\\test");
    }
    @Test
    public void handleIFrame(){
        driver.get("https://demoqa.com/frames");
        driver.switchTo().frame("frame1");
        String text= driver.findElement(By.id("sampleHeading")).getText();
        System.out.println(text);
        driver.switchTo().defaultContent();

    }
    @Test
    public void mouseHover() {
        driver.get("https://green.edu.bd");
        List<WebElement> about_us = driver.findElements(By.xpath("//a[contains(text(),'About Us')]"));
        //WebElement about_us=driver.findElement(By.linkText("ABOUT US"));
        Actions actions = new Actions(driver);
        actions.moveToElement(about_us.get(2)).perform();
    }
    @Test
    public void keyboardEvents() throws InterruptedException {
        driver.get("https://www.google.com/");
        WebElement searchElement = driver.findElement(By.name("q"));
        Actions actions = new Actions(driver);
        actions.moveToElement(searchElement).
                keyDown(Keys.SHIFT)
                .sendKeys("Selenium webdriver")
                .keyUp(Keys.SHIFT)
                .doubleClick(searchElement)
                .contextClick(searchElement).perform();
    }
    @Test
    public void takeScreenShot() throws IOException {
        driver.get("https://demoqa.com/");
        File screenshotFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        String time = new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss-aa").format(new Date());
        String fileWithPath = "./src/test/resources/screenshots/" + time + ".png";
        File DestFile = new File(fileWithPath);
        FileUtils.copyFile(screenshotFile, DestFile);
    }
    public static void readFromExcel(String filePath,String fileName,String sheetName) throws IOException {
        File file = new File(filePath+"\\"+fileName);
        FileInputStream inputStream = new FileInputStream(file);
        Workbook workbook = null;
        String fileExtensionName = fileName.substring(fileName.indexOf("."));
        if(fileExtensionName.equals(".xlsx")){
            workbook = new HSSFWorkbook(inputStream);
        }
        Sheet sheet = workbook.getSheet(sheetName);
        int rowCount = sheet.getLastRowNum()-sheet.getFirstRowNum();
        for (int i = 0; i < rowCount+1; i++) {
            Row row = sheet.getRow(i);
            for (int j = 0; j < row.getLastCellNum(); j++) {
                DataFormatter formatter = new DataFormatter();
                System.out.print(formatter.formatCellValue ((row.getCell(j)))+"|| ");
            }
            System.out.println();

        }
    }

        @Test
    public void readExcelFile() throws IOException {
        String filePath = "./src/test/resources/";
            readFromExcel(filePath,"Book1.xlsx","Sheet1");
    }
    @After
    public void closeDriver(){
//        driver.close();
        //driver.quit();
    }

}
