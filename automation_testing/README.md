# SauceDemo Test Cases Automation

Independent Java, Selenium, and TestNG automation for all 20 cases in `sauce_demo_TestCases.pdf`.

## Requirements

- JDK 21 or newer
- Apache Maven 3.9 or newer
- Google Chrome (Selenium Manager obtains the compatible driver when required)

## Run the tests

Run all tests headlessly (the default):

```powershell
mvn test
```

To see the browser while tests run:

```powershell
mvn test -Dheadless=false
```

## Notes

- Browser setup and cleanup are centralized in `base.BaseTest` and use Chrome.
- The tests use explicit waits; implicit waits are disabled to keep timing predictable.
- External social-media links may redirect to a provider's current domain. The tests verify the Sauce Labs destination rather than a brittle legacy hostname.
