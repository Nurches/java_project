# University System (Full CLI)

Interactive role-based university management system with persistence to `university_system.ser`.

## Features

- Role-based CLI: `ADMIN`, `MANAGER`, `TEACHER`, `STUDENT` (incl. bachelor/master/PhD)
- Course catalog, registration workflow (`PENDING` / `APPROVED` / `REJECTED`)
- Grading, GPA, transcripts, teacher ratings
- News, comments, employee messages, teacher complaints
- Research papers, h-index, journal, rankings
- PBKDF2 password hashing
- Audit log (in-memory + console)
- Auto save/load on CLI exit

## Run CLI

```bash
javac $(find src -name "*.java")
java -cp src Main
```

At login type `exit` to quit. Data is saved to `university_system.ser` (override with env `UNIVERSITY_DATA_FILE`).

## Demo scenario (non-interactive)

```bash
java -cp src Main --demo
```

## Users and roles

There is **no limit** on how many users each role can have. The system stores all users in a list; you can add as many as needed.

- **Admin** → menu *Create user* (any role, unique `id` and `login`)
- **Admin** → *List users* shows everyone currently in the system
- Data persists in `university_system.ser` after exit

### Example seed accounts (first run only, when DB is empty)

These are just **demo logins**, not a hard cap of one per role:

| Login    | Password   | Role    |
|----------|------------|---------|
| admin    | admin123   | ADMIN   |
| manager  | man123     | MANAGER |
| manager2 | man123     | MANAGER |
| teacher  | teach123   | TEACHER |
| teacher2 | teach123   | TEACHER |
| student  | stud123    | STUDENT |
| student2 | stud123    | STUDENT |
| master   | master123  | MASTER  |

Seed courses: `C1` (OOP), `C2` (Databases).

## Tests

```bash
java -cp src tests.DomainRulesTest
java -cp src tests.PersistenceSmokeTest
```

## PostgreSQL (optional, not wired in Java yet)

Apply in order:

1. `src/db/migration/V1__init.sql`
2. `src/db/migration/V2__seed_roles.sql`

## CLI navigation

After login: **Main menu** → Common (profile, news, messages) or role-specific actions → `0` Back / Logout.
