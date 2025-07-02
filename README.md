# AI Tool Java Copilot

This project is a Spring Boot application for managing scheduled tasks, integrating with NATS for event-driven task management. It is designed to consume JSON-based event messages, manage tasks with date/period-based functionality, and provide REST endpoints for task operations.

## Project Vision
- **Consumer Service**: Subscribes to NATS for event-based task messages (JSON format).
- **REST API**: Exposes endpoints to view and edit scheduled tasks.
- **Task Scheduling**: Supports date and period-based (recurring) tasks.

## Core Features
- **Task Management**: Create, update, delete tasks from consumed events, supporting date ranges.
- **Recurring Tasks**: Daily, weekly, and monthly recurrence patterns.
- **Calendar Integration**: View events by day, week, or month.

## Domain Model
- **Task**: `id`, `title`, `description`, `startDate`, `endDate`, `priority`, `status`, `recurrenceType`, `participants`
- **Participant**: `id`, `name`, `title`, `department`
- **RecurrencePattern**: Handles daily, weekly, monthly recurrence logic

## Architecture Overview
- **Domain**: Core business entities (Task, Participant, RecurrencePattern)
- **DTO**: Data transfer objects for event messages (similar to Task, includes participants)
- **Listener**: Subscribes to NATS and processes incoming task events
- **Service**: Business logic for task management
- **Controller**: REST endpoints for task operations

## Technologies Used
- Java 17+
- Spring Boot
- NATS Java Client
- Jackson (JSON serialization/deserialization)
- Lombok

## Sample Task Event JSON
```
{
    "id": "a7b2c5d8-4e9f-41a2-b3c6-7d8e9f0a1b2c",
    "title": "Clean Chemical Storage Area",
    "description": "Deep cleaning and organization of chemical storage room with proper labeling verification.",
    "startDate": "2025-07-03T14:30:00",
    "endDate": "2025-07-03T17:00:00",
    "priority": "HIGH",
    "status": "PENDING",
    "recurrenceType": "MONTHLY",
    "recurrencePattern": {
      "interval": 1,
      "type": "MONTHLY",
      "dayOfMonth": 3,
      "endDate": "2025-12-31T00:00:00"
    },
    "participants": [
      {
        "id": "3f8a9b2c-5d6e-47f8-a9b0-c1d2e3f4a5b6",
        "name": "Sarah Martinez",
        "role": "INSPECTOR"
      }
    ]
  }
```

## Getting Started

### Prerequisites
- Java 17 or higher
- Maven
- Running NATS server (default subject: `tasks`)

### Build and Run
```
./mvnw clean install
./mvnw spring-boot:run
```

### Configuration
Edit `src/main/resources/application.properties` to set NATS connection details and subject if needed:
```
nats.subject=tasks
nats.url=nats://localhost:4222
```

## Next Steps
1. **Domain Models**: Implement `Task`, `Participant`, and `RecurrencePattern` entities.
2. **DTO**: Create event message DTO (similar to Task, includes participants).
3. **Listener**: Implement NATS listener for consuming task events.
4. **Service & Controller**: Add business logic and REST endpoints for task management.

## License
MIT

