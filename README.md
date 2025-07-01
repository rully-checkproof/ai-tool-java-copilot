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
  "id": "123e4567-e89b-12d3-a456-426614174000",
  "title": "Inspect Safety Equipment",
  "description": "Monthly inspection of all safety equipment in the facility.",
  "startDate": "2025-07-01T09:00:00",
  "endDate": "2025-07-01T11:00:00",
  "priority": "HIGH",
  "status": "IN_PROGRESS",
  "recurrenceType": "MONTHLY",
  "recurrencePattern": {
    "interval": 1,
    "daysOfWeek": ["MONDAY"],
    "endDate": "2025-12-31T00:00:00"
  },
  "participants": [
    {
      "id": "987e6543-e21b-12d3-a456-426614174999",
      "name": "John Doe",
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

