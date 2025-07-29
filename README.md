# VkEdu Practice - User Segmentation Service

Сервис для управления пользователями и сегментами, позволяющий проводить A/B тестирование новых функций.

## Описание

Сервис предоставляет возможность:
- Создавать, редактировать и удалять сегменты
- Распределять сегменты случайным образом на определенный процент пользователей
- Получать информацию о пользователях и их сегментах
- Получать список пользователей по сегменту

## Запуск приложения

1. Убедитесь, что у вас установлена Java 21
2. Запустите приложение:
```bash
./mvnw spring-boot:run
```

Приложение будет доступно по адресу: http://localhost:8080

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

## Примеры использования

### Создание сегментов для экспериментов

1. **Создание сегмента для голосовых сообщений (20% пользователей):**
```bash
curl -X POST http://localhost:8080/api/segments \
  -H "Content-Type: application/json" \
  -d '{
    "name": "MAIL_VOICE_MESSAGES",
    "description": "Тестирование голосовых сообщений в почте",
    "percentage": 20
  }'
```

2. **Создание сегмента для скидки в облаке (30% пользователей):**
```bash
curl -X POST http://localhost:8080/api/segments \
  -H "Content-Type: application/json" \
  -d '{
    "name": "CLOUD_DISCOUNT_30",
    "description": "Скидка 30% на подписку в облаке",
    "percentage": 30
  }'
```

3. **Создание сегмента для GPT в письмах (25% пользователей):**
```bash
curl -X POST http://localhost:8080/api/segments \
  -H "Content-Type: application/json" \
  -d '{
    "name": "MAIL_GPT",
    "description": "Использование GPT в письмах",
    "percentage": 25
  }'
```

### Получение информации о пользователе

```bash
curl http://localhost:8080/api/users/1/segments
```

### Получение пользователей в сегменте

```bash
curl http://localhost:8080/api/users/segment/MAIL_GPT
```

## База данных

Приложение использует H2 in-memory базу данных. Консоль H2 доступна по адресу:
http://localhost:8080/h2-console

Параметры подключения:
- JDBC URL: `jdbc:h2:mem:testdb`
- Username: `sa`
- Password: `password`

## Технологии

- Spring Boot 3.5.3
- Spring Data JPA
- H2 Database
- Lombok
- Java 21 