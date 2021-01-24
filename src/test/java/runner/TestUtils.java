package runner;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import runner.type.Profile;
import runner.type.ProfileType;
import runner.type.Run;
import runner.type.RunType;

import java.lang.reflect.Method;

public abstract class TestUtils {

    public static RunType getRunType(Object object) {
        Run run = object.getClass().getAnnotation(Run.class);
        if (run == null) {
            return RunType.Single;
        }

        return run.run();
    }

    public static ProfileType getProfileType(Object object, ProfileType defaultType) {
        Profile profile;
        if (object instanceof Method) {
            profile = ((Method)object).getAnnotation(Profile.class);
        } else {
            profile = object.getClass().getAnnotation(Profile.class);
        }

        if (profile == null) {
            return defaultType;
        }

        return profile.profile();
    }

    private static class MovingExpectedCondition implements ExpectedCondition<WebElement> {

        private By locator;
        private WebElement element = null;
        private Point location = null;

        public MovingExpectedCondition(WebElement element) {
            this.element = element;
        }

        public MovingExpectedCondition(By locator) {
            this.locator = locator;
        }

        @Override
        public WebElement apply(WebDriver driver) {
            if (element == null) {
                try {
                    element = driver.findElement(locator);
                } catch (NoSuchElementException e) {
                    return null;
                }
            }
            if (element.isDisplayed()) {
                Point location = element.getLocation();
                if (location.equals(this.location)) {
                    return element;
                }
                this.location = location;
            }
            return null;
        }
    }

    public static ExpectedCondition<WebElement> movingIsFinished(WebElement element) {
        return new MovingExpectedCondition(element);
    }

    public static ExpectedCondition<WebElement> movingIsFinished(By locator) {
        return new MovingExpectedCondition(locator);
    }

}
