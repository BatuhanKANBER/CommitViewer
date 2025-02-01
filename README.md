# CommitViewer

CommitViewer is a tool designed to visualize the commit history of Git repositories. This project provides developers with better commit history tracking and analysis capabilities.

## Features

- Listing commits from public repositories on GitHub and GitLab providers
- Viewing detailed information of commits
- Storing data in a database

## Installation

### Requirements

- Java 11 or higher
- Git
- Maven

### Steps

1. Clone this repository to your machine:

    ```bash
    git clone https://github.com/BatuhanKANBER/CommitViewer.git
    ```

2. Navigate to the project directory:

    ```bash
    cd CommitViewer\viewer
    ```

3. Download the necessary dependencies and build the project:

    ```bash
    ./mvnw clean install
    ```

4. Start the application:

    ```bash
    ./mvnw spring-boot:run
    ```

## Usage

- After starting the application, you will be asked to enter the owner and repository name of your Git repo.
- After providing the required input, you can start viewing the commit history.

