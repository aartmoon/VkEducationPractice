# VkEdu Practice - User Segmentation Service

Сервис для управления пользователями и сегментами.

## Описание

Сервис предоставляет возможность:
- Создавать, редактировать и удалять сегменты
- Распределять сегменты случайным образом на определенный процент пользователей
- Получать информацию о пользователях и их сегментах
- Получать список пользователей по сегменту

## Запуск приложения (Prod: PostgreSQL + Docker)

1. Запустите приложение (первый запуск):
```bash
docker-compose up --build -d
```
2. Последующие запуски:
```bash
docker-compose up -d
```
## Запуск приложения (Dev: h2 in memory)
```bash
mvn spring-boot:run
```

Приложение будет доступно по адресу: http://localhost:8081

## API Endpoints

### Пользователи

#### Получить всех пользователей
```
GET /api/users
```

#### Получить пользователя по ID
```
GET /api/users/{id}
```

#### Получить сегменты пользователя
```
GET /api/users/{id}/segments
```

#### Получить пользователей по сегменту
```
GET /api/users/segment/{segmentName}
```

#### Получить общее количество пользователей
```
GET /api/users/count
```

### Сегменты

#### Получить все сегменты
```
GET /api/segments
```

#### Получить сегмент по ID
```
GET /api/segments/{id}
```

#### Получить сегмент по имени
```
GET /api/segments/name/{name}
```

#### Создать новый сегмент
```
POST /api/segments
Content-Type: application/json

{
    "name": "MAIL_GPT",
    "description": "Тестирование GPT в письмах",
    "percentage": 30
}
```

#### Обновить сегмент
```
PUT /api/segments/{id}?name=NEW_NAME&description=NEW_DESCRIPTION
```

#### Удалить сегмент
```
DELETE /api/segments/{id}
```

#### Получить количество пользователей в сегменте
```
GET /api/segments/{name}/users/count
```

## База данных 

Параметры подключения h2:

- JDBC URL: `jdbc:h2:mem:testdb`
- Username: `sa`
- Password: `password`

Параметры для PostgreSQL:

- DB= `vkedu`
- USER= `myuser`
- PASSWORD= `mypassword`

## Технологии

- Spring Boot
- Spring Data JPA
- H2 Database
- PostgreSQL
- Lombok
- Java 21
- Docker & Docker Compose