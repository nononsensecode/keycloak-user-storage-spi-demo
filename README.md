# Keycloak User Storage SPI Demo

This project is written in `Kotlin`. It extends the `keycloak` server to use a `REST` api as user storage. It will get users, realm role mappings and client role mappings from this external source.

You can get the `REST` api for users from [here]("https://github.com/nononsensecode/simple-user-app")