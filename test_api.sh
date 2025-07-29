#!/bin/bash

echo "=== Тестирование API сервиса управления пользователями и сегментами ==="
echo

echo "1. Получение всех пользователей:"
curl -s http://localhost:8080/api/users | head -200
echo
echo

echo "2. Создание сегмента MAIL_VOICE_MESSAGES (20% пользователей):"
curl -X POST http://localhost:8080/api/segments \
  -H "Content-Type: application/json" \
  -d '{"name": "MAIL_VOICE_MESSAGES", "description": "Тестирование голосовых сообщений в почте", "percentage": 20}'
echo
echo

echo "3. Создание сегмента CLOUD_DISCOUNT_30 (30% пользователей):"
curl -X POST http://localhost:8080/api/segments \
  -H "Content-Type: application/json" \
  -d '{"name": "CLOUD_DISCOUNT_30", "description": "Скидка 30% на подписку в облаке", "percentage": 30}'
echo
echo

echo "4. Создание сегмента MAIL_GPT (25% пользователей):"
curl -X POST http://localhost:8080/api/segments \
  -H "Content-Type: application/json" \
  -d '{"name": "MAIL_GPT", "description": "Использование GPT в письмах", "percentage": 25}'
echo
echo

echo "5. Получение всех сегментов:"
curl -s http://localhost:8080/api/segments
echo
echo

echo "6. Проверка количества пользователей в каждом сегменте:"
echo "MAIL_VOICE_MESSAGES: $(curl -s http://localhost:8080/api/segments/MAIL_VOICE_MESSAGES/users/count)"
echo "CLOUD_DISCOUNT_30: $(curl -s http://localhost:8080/api/segments/CLOUD_DISCOUNT_30/users/count)"
echo "MAIL_GPT: $(curl -s http://localhost:8080/api/segments/MAIL_GPT/users/count)"
echo

echo "7. Проверка сегментов пользователя 1:"
curl -s http://localhost:8080/api/users/1/segments
echo
echo

echo "8. Проверка сегментов пользователя 2:"
curl -s http://localhost:8080/api/users/2/segments
echo
echo

echo "9. Проверка сегментов пользователя 3:"
curl -s http://localhost:8080/api/users/3/segments
echo
echo

echo "=== Тестирование завершено ===" 