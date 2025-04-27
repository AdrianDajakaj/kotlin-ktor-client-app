# kotlinktorclientapp

A simple Ktor application that sends and receives messages to/from a Discord server using a bot. The app returns data from an external API upon request from a Discord user.

**External API** : [scala-play-crud](https://github.com/AdrianDajakaj/scala-play-crud.git)

This project was created using the [Ktor Project Generator](https://start.ktor.io).

## Features

- Sending messages to the Discord platform
- Receiving user messages from the Discord platform directed to the application (bot)
- Returning a list of categories from external API upon specific user request: `!categories`

## Functionalities

**Sending messages to Discord**

Example using `curl`:
```bash
curl -X POST "http://localhost:8080/send" \
  -H "Content-Type: application/json" \
  -d '{"content":"Hello!"}'
```
This will send the content of the request (Hello!) to the configured Discord webhook.

**Receiving messages from Discord**
When a user sends a message, the bot replies with a thank-you message.
Example response in the server log:
```bash
Received message from user123: Hello Bot!
Reply sent to user123
```
The bot listens for messages in the configured Discord server and responds with a simple acknowledgment.

**Sending list of categories to Discord**
When a user sends a message: `!categories`, the bot replies with a list of categories from external API.

## Building & Running

Before building & running project:
- run API from: [scala-play-crud](https://github.com/AdrianDajakaj/scala-play-crud.git)

- set environment variables:

```bash
export API_BASE_URL=<your-api-url>
```

```bash
export DISCORD_WEBHOOK_URL=<your-discord-webhook-url>
```

```bash
export DISCORD_BOT_TOKEN=<your-discord-bot-token>
```

To build or run the project, use one of the following tasks:

| Task                          | Description                                                          |
| -------------------------------|---------------------------------------------------------------------- |
| `./gradlew test`              | Run the tests                                                        |
| `./gradlew build`             | Build everything                                                     |
| `buildFatJar`                 | Build an executable JAR of the server with all dependencies included |
| `buildImage`                  | Build the docker image to use with the fat JAR                       |
| `publishImageToLocalRegistry` | Publish the docker image locally                                     |
| `run`                         | Run the server                                                       |
| `runDocker`                   | Run using the local docker image                                     |

If the server starts successfully, you'll see the following output:

```
2024-12-04 14:32:45.584 [main] INFO  Application - Application started in 0.303 seconds.
2024-12-04 14:32:45.682 [main] INFO  Application - Responding at http://0.0.0.0:8080
```

