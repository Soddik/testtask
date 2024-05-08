![Java](https://img.shields.io/badge/java-%23ED8B00.svg)

## Older records remover

### Описание

Приложение удаляет записи из таблицы, согласно параметрам запроса(имя таблицы, дата).
В зависимости от отношения кол-ва удаляемых записей к размеру таблицы выбирается одна из стратегий:

1. BatchStrategy - стратегия удаления записей партиями, используется если кол-во удаляемых записей меньше 30% всей
   таблицы.
2. TempTableStrategy - стратегия удаления записей с использованием временной таблицы, используется если кол-во удаляемых
   записей больше или ровно 30% всей таблицы.
3. TruncateStrategy - стратегия удаления записей с использованием TRUNCATE, используется если все записи таблицы
   подлежат удалению.

## Технологии

Java 17, Spring Boot, PostgreSQL, Swagger

## Для обоих задач

## API

### Endpoints

| Endpoint              | Request Type | Task # | Request body example                                   | Response code |                    Description |
|-----------------------|:------------:|--------|--------------------------------------------------------|---------------|-------------------------------:|
| api/v1/db/delete      |     POST     | 2      | `{"tableName":"table_name", "dateTime": "2024-01-01"}` | 200           |            Delete data from db |
| api/v1/db/populate    |     POST     | 2      | `{"tableName":"table_name"}`                           | 201           |          Populate db with data |

Swagger - `/swagger-ui/index.html#/`

### SettingUp Dev

Загрузить ZIP архив [link](https://github.com/Soddik/testtask/archive/refs/heads/master.zip).

Загрузить проект из репозитория с помощью HTTPS:
`git clone https://github.com/Soddik/testtask.git`.

### Запуск в Docker

Выполнить `docker-compose up -d --build` в командной строке, находясь в папке с проектом.
