#!/bin/bash

echo "=== Простой тест API ==="
echo

echo "1. Проверка пользователей:"
curl -s http://localhost:8080/api/users | head -100
echo
echo

echo "2. Создание тестового сегмента:"
curl -X POST http://localhost:8080/api/segments \
  -H "Content-Type: application/json" \
  -d '{"name": "TEST_SEGMENT", "description": "Тестовый сегмент", "percentage": 50}' \
  -w "\nHTTP Status: %{http_code}\n"
echo

echo "3. Проверка сегментов:"
curl -s http://localhost:8080/api/segments
echo
echo

echo "4. Проверка пользователя в сегменте:"
curl -s http://localhost:8080/api/users/1/segments
echo
echo 