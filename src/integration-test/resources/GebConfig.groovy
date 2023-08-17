import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions

cacheDriverPerThread = true

waiting {
    timeout = 20
}

environments {

    // run via “./gradlew -Dgeb.env=chrome iT”
    chrome {
        driver = { new ChromeDriver() }
    }

    // run via “./gradlew -Dgeb.env=chromeHeadless iT”
    headless {
        driver = {
            ChromeOptions o = new ChromeOptions()
            o.addArguments('headless')
            new ChromeDriver(o)
        }
    }

    // run via “./gradlew -Dgeb.env=integrated iT”
    integrated {
        baseUrl = "https://www.everestquality.com"
        driver = {
            ChromeOptions o = new ChromeOptions()
            //o.addArguments('headless')
            new ChromeDriver(o)
        }
    }
    // run via “./gradlew -Dgeb.env=local iT”
    local {
        baseUrl = "http://localhost:8080"
        driver = {
            ChromeOptions o = new ChromeOptions()
            //o.addArguments('headless')
            new ChromeDriver(o)
        }
    }
}
