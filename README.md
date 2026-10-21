# College Event Management System

An object-oriented Java Swing and MySQL desktop application developed for managing college events, student registrations, and offline payment confirmations.

---

## 🛠️ System Architecture & Features

### 1. **Administrator Dashboard**
- **Authentication**: Secure admin login (`admin` / `admin123`).
- **Event Management (CRUD)**:
  - Add new events with categories, venues, dates, fees, prizes, payment location, and deadlines.
  - Edit existing event information.
  - View event details.
  - Delete events (with cascade support for registrations).
- **Registration Management**:
  - View all student registration requests in real time.
  - Confirm offline payments once received at the registration/payment desk.

### 2. **Student Dashboard**
- **Student Authentication**: Log in as registered students (`student1`, `student2`, `student3`, `student4`).
- **Profile Display**: Displays student name, ID, department, and semester.
- **Browse & Search Events**: Filter events dynamically by title or category.
- **Event Registration**:
  - View full details of any event (time, venue, fee, cash prizes, offline payment location and deadline).
  - Register for events (starts in `Pending` status).
  - Automatically updates dashboard registration status upon closing details or manual refresh.
  - Duplicate registration protection.

### 3. **Database & Persistence**
- Relational schema using MySQL with Referential Integrity (Foreign Keys with `ON DELETE CASCADE` and `UNIQUE` constraints).
- Auto-initialization: `DBConnection` automatically provisions missing tables (`students`, `events`, `registrations`) and sample seed data on the first connection!
- Support for `db.properties` with BOM protection and fallback configurations.

---

## 🔑 Default Login Credentials

| Role | Username | Password | Full Name / Notes |
| :--- | :--- | :--- | :--- |
| **Admin** | `admin` | `admin123` | System Administrator |
| **Student 1** | `student1` | `devan123` | DEVANARAYANAN (CSE, S3) |
| **Student 2** | `student2` | `ashikh123` | ASHIKH S (CSE, S3) |
| **Student 3** | `student3` | `pridhveeja123` | PRIDHVEEJA VINOD K (CSE, S3) |
| **Student 4** | `student4` | `alameen123` | AL AMEEN (CSE, S3) |

---

## ⚙️ Database Configuration

Configuration is located in `db.properties`:

```properties
db.url=jdbc:mysql://localhost:3306/college_event_db?createDatabaseIfNotExist=true&allowPublicKeyRetrieval=true&useSSL=false
db.user=root
db.password=
```

> **Note**: If `college_event_db` does not exist on your MySQL server, the connection URL automatically creates it, and the application will create all tables and populate initial sample data automatically.

You can also manually import the schema using MySQL CLI or MySQL Workbench:
```bash
mysql -u root -p < schema.sql
```

---

## 🚀 How to Run

### Option 1: Using the provided Batch Files (Windows)
- Double-click `compile.bat` to compile.
- Double-click `run.bat` to launch the application.

### Option 2: Using the Command Line
Compile:
```powershell
javac -cp "lib/*;src" -d bin src/*.java
```

Run Main GUI:
```powershell
java -cp "bin;lib/*" Main
```

Run Database Connection Test:
```powershell
java -cp "bin;lib/*" TestDB
```

