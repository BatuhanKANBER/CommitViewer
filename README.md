# CommitViewer

CommitViewer is a tool designed to visualize the commit history of Git repositories. This project provides developers with better commit history tracking and analysis capabilities.

## Features

- Listing the last 1 month's commits from public repositories on GitHub and GitLab providers.
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

# Pages

## Home Page
![Home](https://github.com/user-attachments/assets/f9032aff-5f70-4150-bebb-63383e01c683)
## Developers Page
![Developers](https://github.com/user-attachments/assets/55433dc7-a7d3-41b7-9a3b-020de66d4ab1)
## Commits List Page
![Commits](https://github.com/user-attachments/assets/4bab360c-5d87-4dc8-84db-4f8a4740a01e)
## Commit Details Page
![CommitDetails](https://github.com/user-attachments/assets/50f500ba-5d5c-409e-8630-62611fed658e)

## This project is deployed on AWS, with the public IP address: 13.48.44.185
