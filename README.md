# Instruction for Running Age of Pokemon Game and Source Code

---

## **1. System Requirements**
- **Operating System:** Windows (other operating systems supported with correct JDK installation)
- **RAM:** At least 4 GB
- **Processor:** Intel i3 or equivalent
- **Disk Space:** 4 GB free space

---

## **2. Prerequisite Software**
Ensure you have the following installed:
- **Java Runtime Environment (JRE)** (included in the provided folder)
- **Java Development Kit (JDK)** version 23 (if running the source code)
- **IDE (Optional for source code execution):** IntelliJ IDEA, Eclipse, or NetBeans (latest versions recommended)
- If using another operating system, download JDK 23 from the official [Oracle website](https://www.oracle.com/java/technologies/downloads/).

---

## **3. Folder Structure**
```
AOP
├── AgeOfPokemon.exe
├── JRE/
│   ├── bin/
│   └── lib/
│── src/main/java
│    ├── com/example/
│    │	├── App.java
│    │  └── Main.java
│    └── module-info.java
├── instruction.md
├── pom.xml
├── config.xml
└── README.md

```

---

## **4. Running the Executable (.exe) File**

1. **Locate the Game Folder:**
   - Navigate to the folder containing your game executable file (`AgeOfPokemon.exe`) and the `JRE` folder.

2. **Ensure JRE Path Integrity:**
   - Confirm that the `JRE` folder contains both `bin` and `lib` subfolders.

3. **Run the Game:**
   - Double-click on `AgeOfPokemon.exe` to launch the game.

4. **Verify Java Path (Optional):**
   - If the game doesn't detect the included JRE, set the Java path manually:
     - Open **"Edit the system environment variables"** and click **Environment Variables**.
     - Under **System Variables**, click **New**, and add:
       - Variable Name: `JAVA_HOME`
       - Variable Value: Path to `JRE` folder.

---

## **5. Running the Source Code**

1. **Ensure Correct JDK Installation:**
   - Download and install **JDK 23** if not already available.

2. **Enable Maven (Recommended):**
   - Ensure your project supports Maven for better dependency management.
   - If you use VSCode, go to the Extension tab and download the package "Extension for Java" which include Maven extension for Java.

3. **Enable JavaFX:**
   - If the project uses Maven and includes a JavaFX dependency in the `pom.xml`, you don't need to download JavaFX SDK separately.
   - Otherwise, download **JavaFX SDK 23** and manually add the JavaFX library to your project dependencies.

4. **Run the Application:**
   - Locate the main file in the `src/main/java/com/example/Main.java` directory (e.g., `Main.java`).

---

## **6. External Libraries and Dependencies**
- **JavaFX** (declared in `pom.xml`)
- **Jackson** (declared in `pom.xml`)
- No other external libraries or cloud services are required.

---

## **7. Troubleshooting**

- **Game Does Not Launch:**
  - Verify that the included `JRE` folder contains `bin` and `lib` directories.
  - Check environment variables and set `JAVA_HOME` correctly.

- **Source Code Compilation Errors:**
  - Ensure the correct JDK version is set in your IDE.
  - Confirm that JavaFX is correctly linked to the project.
  - You may use Maven for dependency management.

- **Performance Issues:**
  - Close unnecessary applications to free up system resources.

---

By following this roadmap, you should be able to run and enjoy the Age of Pokemon game or develop it further without any technical hurdles. Happy Gaming!

