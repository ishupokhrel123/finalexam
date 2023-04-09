package test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.Color;
import org.openqa.selenium.support.ui.Select;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidateToggle {

    WebDriver webDriver;
    final String TESTURL = "http://techfios.com/test/101/";

    @Before
    public void setUp(){
        System.setProperty("webdriver.chrome.driver", "/Users/ishu/Downloads/newdriver/chromedriver");
        webDriver = new ChromeDriver();
    }

    @Test
    public void validateToggleChecked(){

        //Setting the url to visit
        webDriver.get(TESTURL);

        // finding and clicking the allbox
        webDriver.findElement(By.name("allbox")).click();

        Pattern pattern = Pattern.compile("\\btodo\\[\\S+\\]");

        String fullPageSource = webDriver.getPageSource();

        Matcher newMatcher = pattern.matcher(fullPageSource);
        System.out.println("full page source contains ? " + newMatcher.find());

        List<String> allMatchingElements = new ArrayList<>();

        while(newMatcher.find()){
            allMatchingElements.add(newMatcher.group());
        }

        if(allMatchingElements.size() > 0){
            for(String eachLocator: allMatchingElements){
                System.out.println("checking " + eachLocator);
                Assert.assertEquals("true",webDriver.findElement(By.name(eachLocator)).getAttribute("checked"));
            }

        }
        // close the driver
        webDriver.close();
    }

    @Test
    public void validateSingleListItemRemoved(){

        //Setting the url to visit
        webDriver.get(TESTURL);

        Pattern pattern = Pattern.compile("\\btodo\\[\\S+\\]");

        String fullPageSource = webDriver.getPageSource();

        Matcher newMatcher = pattern.matcher(fullPageSource);

        List<String> allMatchingElements = new ArrayList<>();

        while(newMatcher.find()){
            allMatchingElements.add(newMatcher.group());
        }



        if(allMatchingElements.size() > 0){
            String targetElement = allMatchingElements.get(0);
            webDriver.findElement(By.name(targetElement)).click();

            WebElement removeElement = webDriver.findElement(By.cssSelector("input[name='submit'][value='Remove']"));
            removeElement.click();

            String newPageSource = webDriver.getPageSource();
            newMatcher = pattern.matcher(newPageSource);
            List<String> newMatchingElements = new ArrayList<>();

            while(newMatcher.find()){
                newMatchingElements.add(newMatcher.group());
            }

            Assert.assertEquals(newMatchingElements.size(),allMatchingElements.size() - 1);
            System.out.println("Just checking if it reaches here");
        }

        webDriver.close();
    }

    @Test
    public void validateEverythingRemovedWhenToggleIsOn(){

        //Setting the url to visit
        webDriver.get(TESTURL);

        webDriver.findElement(By.name("allbox")).click();

        webDriver.findElement(By.cssSelector("input[name='submit'][value='Remove']")).click();

        Pattern pattern = Pattern.compile("\\btodo\\[\\S+\\]");

        String fullPageSource = webDriver.getPageSource();

        Matcher newMatcher = pattern.matcher(fullPageSource);

        List<String> allMatchingElements = new ArrayList<>();

        while(newMatcher.find()){
            allMatchingElements.add(newMatcher.group());
        }

        Assert.assertEquals(0,allMatchingElements.size());

        webDriver.close();

    }
    @Test
    public void userAddAbilityAndDisplayed(){
        //Setting the url to visit
        webDriver.get(TESTURL);

        webDriver.findElement(By.name("categorydata")).sendKeys("newaddedcategory");

        webDriver.findElement(By.cssSelector("input[name='submit'][value='Add category']")).click();

        List<WebElement> webElementList = webDriver.findElements(By.xpath("//*[text()='newaddedcategory']"));

        Assert.assertFalse(webElementList.isEmpty());
        webDriver.findElement(By.xpath("//a[contains(text(),'Yes')]")).click();
        webDriver.close();

    }

    @Test
    public void userCannotAddDuplicateCategory(){
        //Setting the url to visit
        webDriver.get(TESTURL);

        String expectedString = "The category you want to add already exists:";

        webDriver.findElement(By.name("categorydata")).sendKeys("twiceattemptcategory");

        webDriver.findElement(By.cssSelector("input[name='submit'][value='Add category']")).click();


        webDriver.findElement(By.name("categorydata")).sendKeys("twiceattemptcategory");

        webDriver.findElement(By.cssSelector("input[name='submit'][value='Add category']")).click();

        String pageSource = webDriver.getPageSource();
        System.out.println(pageSource);

        Pattern pattern = Pattern.compile(expectedString);

        Matcher matcher = pattern.matcher(pageSource);

        List<String> foundElements = new ArrayList<>();

        while(matcher.find()){
            foundElements.add(matcher.group());
        }

        Assert.assertTrue(foundElements.size() > 0);

        webDriver.findElement(By.xpath("//a[contains(text(),'Yes')]")).click();
        webDriver.close();

    }


    @Test
    public void dropDownContainsAllMonths(){
        //Setting the url to visit
        webDriver.get(TESTURL);

        List<String> expectedMonths = Arrays.asList(
                "Jan",
                "Feb",
                "Mar",
                "Apr",
                "May",
                "Jun",
                "Jul",
                "Aug",
                "Sep",
                "Oct",
                "Nov",
                "Dec");
        Select dropDown = new Select(webDriver.findElement(By.name("due_month")));
        List<String> dropDownOptions = new ArrayList<>();
        List<WebElement> dropdownElements = dropDown.getOptions();
        for(WebElement eachWebElement: dropdownElements){
            dropDownOptions.add(eachWebElement.getText());
        }
        Assert.assertTrue(dropDownOptions.containsAll(expectedMonths));

        webDriver.close();
    }

    @Test
    public void changeBackgroundSkyBlue(){
        //Setting the url to visit
        webDriver.get(TESTURL);
        webDriver.findElement(By.cssSelector("button[onclick='myFunctionSky()']")).click();
        WebElement webElement = webDriver.findElement(By.tagName("body"));
        String backgroundColor = webElement.getCssValue("background-color");
        Color actualColor = Color.fromString(backgroundColor);
        Color expectedColor = Color.fromString(Color.fromString("skyblue").asRgba());

        Assert.assertEquals(expectedColor,actualColor);

        webDriver.close();

    }

    @Test
    public void changeBackgroundSkyWhite(){
        //Setting the url to visit
        webDriver.get(TESTURL);
        webDriver.findElement(By.cssSelector("button[onclick='myFunctionWhite()']")).click();
        WebElement webElement = webDriver.findElement(By.tagName("body"));
        String backgroundColor = webElement.getCssValue("background-color");
        Color actualColor = Color.fromString(backgroundColor);
        Color expectedColor = Color.fromString(Color.fromString("white").asRgba());

        Assert.assertEquals(expectedColor,actualColor);

        webDriver.close();

    }
}
