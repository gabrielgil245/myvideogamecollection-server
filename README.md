# My Video Game Collection Server
## Project Description
This application is designed to allow users to create and organize their video game collection.

My Video Game Collection Web Platform/Frontend: https://github.com/gabrielgil245/myvideogamecollection-client

## Technologies Used
- Spring Boot
- Spring WebMVC
- Spring Data
- Postgres
- Lombok
- JUnit
- Mockito
- H2
- Postman
- JavaDocs

## Features
### Users can:
- Register.
- Login/Logout.
- Reset their password (using an email feature).
- Modify their information.
- Upload a profile picture (using AWS: S3).
- Search other people.
- Add Game Platforms.
  - Add/assign video games to a platform.
  - Assign a status to a game: Not Started, In Progress, Beat, Complete, Dropped
- View their own profile.
  - Including their consoles and games.
- View others’ profile.
  - Including their consoles and games.

Todo-List:
- Add uploading a profile picture
- Add Email Encryption
- Add JSON Web Tokens

## Getting Started
- git clone https://github.com/gabrielgil245/myvideogamecollection
- Create a relational database called "myvideogamecollection".
- List of environment variables:
  - AWS_DATABASE_URI - Relational Database Host Name.
  - AWS_DATABASE_USERNAME - Database Username.
  - AWS_DATABASE_PASSWORD - Database Password.
  - AWS_S3_ID - S3 ID.
  - AWS_S3_KEY - S3 Key.
  - AWS_S3_BUCKET - S3 Bucket Name.
- The CROSS_ORIGIN_VALUE is located in the CrossOriginUtil class; it's set to "http://localhost:4200" but may be changed.

## Usage
- Run the application from the MyvideogamecollectionApplication class.
- HTTP requests may be made to "http://localhost:9000" using the web platform (the link is located at the top of this file).
