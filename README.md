# Test Script Generator

This project is a Selenium Java automation framework using Maven, TestNG, and WebDriverManager. Test cases are driven by Excel files placed in the `testcases/` folder.

## Features
- Selenium WebDriver with Java
- TestNG for test management
- WebDriverManager for automatic driver management
- Excel-driven test case generation (Apache POI)
- Ready for CI/CD with Jenkins

## Getting Started

### Prerequisites
- Java 11+
- Maven 3.6+

### Folder Structure
```
Test_Script_Generator/
├── pom.xml
├── src/
│   ├── main/java/
│   └── test/java/
├── testcases/
│   └── (your Excel files)
├── testng.xml
└── README.md
```

### How to Run
1. Place your test case Excel files in the `testcases/` folder.
2. Build and run tests:
   ```
   mvn clean test
   ```

### Jenkins Integration
- Use the provided `Jenkinsfile` for CI/CD.
- Configure your Jenkins job to use Maven and Java.

---

## Excel Format
(You will need to specify the columns and format for your test case Excel files.)

---

## License
MIT 