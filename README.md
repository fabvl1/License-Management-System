# Drivers License Management System

Desktop Java application to manage driver's licenses, exams, centers, users, and traffic violations.

## Overview
This project is focused on licensing center operations:
- Driver management
- License issuance and expiration tracking
- Exam registration and results
- Traffic violations and penalties
- Administrative reports (PDF)

It uses Java (Swing) for the UI/business logic and PostgreSQL as the data store.

## Tech Stack
- Java 17
- Swing (desktop UI)
- PostgreSQL JDBC driver
- iText PDF
- FlatLaf
- jDatePicker

## Project Layout
- `src/`: Java source code
- `src/libs/`: third-party JAR dependencies
- `icons/`: additional assets
- `bin/`: build output (ignored by git)

## Requirements
- JDK 17+
- PostgreSQL server

## Run In VS Code
1. Open this folder in VS Code.
2. Ensure Java extension pack is installed.
3. Use JDK 17 in the workspace.
4. Run main class: `model.Main`.

## Run From Terminal (Windows PowerShell)
Compile:
```powershell
javac -cp "src/libs/*" -d bin @(Get-ChildItem -Recurse -Path src -Filter *.java | ForEach-Object { $_.FullName })
```

Run:
```powershell
java -cp "bin;src/libs/*" model.Main
```

## Notes
- Generated reports are written to `reports/`.
- Build artifacts under `bin/` are not intended to be committed.

## ER Diagram
https://i.imgur.com/uWKGbZK.jpeg
