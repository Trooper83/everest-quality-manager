import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions

cacheDriverPerThread = true

waiting {
    timeout = 20
}

environments {

    // run via “./gradlew -Dgeb.env=chrome iT”
    test {
        driver = { new ChromeDriver() }
    }

    // run via “./gradlew -Dgeb.env=chromeHeadless iT”
    chromeHeadless {
        driver = {
            ChromeOptions o = new ChromeOptions()
            o.addArguments('headless')
            new ChromeDriver(o)
        }
    }

    // run via “./gradlew -Dgeb.env=integrated iT”
    integrated {
        baseUrl = "http://localhost:8080"
        driver = { new ChromeDriver() }
    }
}
