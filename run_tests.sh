#!/bin/bash

echo "=== Запуск тестов для VkEdu Practice ==="
echo

echo "1. Запуск unit тестов..."
./mvnw test -Dtest="*Test" -DfailIfNoTests=false

echo
echo "2. Запуск тестов с подробным выводом..."
./mvnw test -Dtest="*Test" -DfailIfNoTests=false -Dsurefire.useFile=false

echo
echo "3. Запуск тестов с покрытием кода..."
./mvnw test jacoco:report -Dtest="*Test" -DfailIfNoTests=false

echo
echo "=== Тестирование завершено ==="
echo
echo "Результаты тестов можно найти в:"
echo "- target/surefire-reports/ (отчеты о тестах)"
echo "- target/site/jacoco/ (покрытие кода)" 