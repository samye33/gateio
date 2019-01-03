package com.utils;


import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.*;

public class SeleniumTradePost {

    public static void bidSelenium() throws Exception {
//        System.setProperty("webdriver.chrome.driver",
//                "D:\\chromedriver.exe");

        WebDriver dr = HttpBase.getFirefoxDriver();

        dr.get("https://www.taobao.com/");
        dr.manage().window().maximize();
        Thread.sleep(1000);
        dr.findElement(By.cssSelector("body")).sendKeys(Keys.CONTROL + "t");
        dr.get("https://www.baidu.com/");
        dr.findElement(By.cssSelector("body")).sendKeys(Keys.CONTROL + "w");
        ArrayList<String> tabs = new ArrayList<String>(dr.getWindowHandles());
        dr.switchTo().window(tabs.get(0)); //switches to new tab
//        dr.get("https://gateio.io");
        System.out.println("已经切换到了" + dr.getWindowHandle());
    }

    public void askSelenium() {

    }

    public void putTradePwdPost() {

    }

    public static void main(String[] args) throws Exception {
        bidSelenium();
    }
}
