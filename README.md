# Getting Started

### Reference Documentation

Dev Task:

------
Create a system to control projects and tasks.
The project should have a title, description, status (new, pending, failed, done), and duration.
The project should have additional fields for clients and companies.
Create validation that one of the fields is populated - client or company.  Project cannot be created without association to one of the entities (client/company).

Every project can have multiple tasks and the status and time frame for the projects is related to the tasks included.
Every task should have a status as well (new, pending, done) and duration. Adding a new task to project will reflect on project duration automatically.

Create Rest API with CRUD operation for projects and tasks.
Delete of the project and tasks should be "soft delete" - mark as deleted, but not delete from the database.
Project cannot be deleted if there is a tasks which are in the new or pending status. All tasks should be finished, project must be marked as done and then there should be a possibility to delete a project.


API should always return HTTP code 200, but with different "code" in the response - code -> 0 for success and code -> -1 for error.
Example:
{
"code": 0,
"data": ... some data
"validation_errors: []
}

Create pagination on list operation (default is 20 results per page).

Language: Kotlin
Framework: Spring Boot
Database: PostgreSQL.
Use ORM for DB operations.