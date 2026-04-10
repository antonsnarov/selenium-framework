# Docker Guide для Selenium тестов

## 📦 Что такое Docker?

Docker — это система контейнеризации, которая позволяет упаковать приложение со всеми его зависимостями в отдельный контейнер. Это гарантирует, что тесты будут работать одинаково:
- На локальной машине разработчика
- На CI/CD сервере
- На боевом сервере

## 🏗️ Структура Dockerfile

Наш Dockerfile использует **multi-stage build** для оптимизации размера образа:

```dockerfile
# Stage 1: Builder - собирает проект
FROM maven:3.9.6-eclipse-temurin-17 AS builder
  └─ Устанавливает зависимости
  └─ Компилирует исходный код

# Stage 2: Runtime - финальный образ для запуска
FROM maven:3.9.6-eclipse-temurin-17
  └─ Устанавливает Chrome/Firefox браузеры
  └─ Копирует собранный проект из Builder
  └─ Запускает тесты
```

**Преимущества:**
- ✅ Меньше размер финального образа (зависимости не копируются)
- ✅ Быстрее загружается при повторной сборке
- ✅ Безопаснее (не используются артефакты сборки)

## 🚀 Запуск тестов в Docker

### 1. Простой запуск в контейнере

```bash
# Собрать образ
docker build -t selenium-tests:latest .

# Запустить тесты
docker run --rm \
  -v $(pwd)/allure-results:/app/allure-results \
  -e HEADLESS=true \
  selenium-tests:latest
```

**Флаги:**
- `--rm` — удалить контейнер после завершения
- `-v` — примонтировать директорию для артефактов
- `-e` — переменные окружения
- `-t` — имя образа

### 2. Docker Compose с Selenium Grid

```bash
# Запустить полный стэк (Hub + Chrome Node + Test Runner)
docker-compose up

# Запустить в фоне
docker-compose up -d

# Посмотреть логи
docker-compose logs -f

# Остановить
docker-compose down
```

**Сервисы в docker-compose.yml:**

```yaml
selenium-hub:      # Coordinator для нескольких браузеров
chrome-node:       # Chrome браузер для параллельного запуска
test-runner:       # Контейнер для запуска тестов
```

### 3. Использование скриптов

```bash
# Запуск тестов в Docker
./run-tests-docker.sh

# Запуск тестов с Grid
./run-tests-grid.sh
```

## 📊 Важные переменные окружения

| Переменная | Значение | Описание |
|------------|----------|---------|
| `HEADLESS` | true/false | Запуск браузера без UI |
| `BROWSER` | chrome/firefox | Выбор браузера |
| `DISPLAY` | :99 | X11 display для headless режима |
| `REMOTE` | true/false | Использовать Selenium Grid |
| `GRID_URL` | URL | Адрес Grid Hub |

## 🔍 Отладка Docker контейнера

### Запустить контейнер в интерактивном режиме

```bash
docker run -it selenium-tests:latest /bin/bash
```

### Посмотреть логи контейнера

```bash
docker logs -f <container-id>
```

### Проверить образ

```bash
docker inspect selenium-tests:latest
docker history selenium-tests:latest  # Слои образа
```

## 📈 Оптимизация Docker образа

### Кэширование слоёв Maven

```dockerfile
COPY pom.xml .
RUN mvn dependency:go-offline -B  # ← Кэшируется при изменении зависимостей
COPY src ./src
RUN mvn clean compile              # ← Пересобирается только при изменении кода
```

### Размер образа

```bash
# Посмотреть размер
docker images selenium-tests

# Очистить неиспользуемые образы
docker image prune

# Удалить образ
docker rmi selenium-tests:latest
```

## 🌐 Selenium Grid в Docker Compose

Основные возможности:

1. **Hub** — управляет сессиями браузеров
2. **Nodes** — контейнеры с браузерами (можно добавить несколько)
3. **VNC доступ** — видеть браузер в реальном времени

### Доступ к Hub

```
http://localhost:4444
```

### VNC к Chrome

```
VNC: localhost:7900
Пароль: secret
```

Используйте VNC Viewer (встроено в macOS) или другой VNC клиент.

## 🔧 Кастомизация Dockerfile

### Добавить Firefox ноду в docker-compose

```yaml
firefox-node:
  image: selenium/node-firefox:4.18.1
  depends_on:
    - selenium-hub
  environment:
    SE_EVENT_BUS_HOST: selenium-hub
    # остальные параметры как у chrome-node
```

### Изменить браузер в Dockerfile

```dockerfile
ENV BROWSER=firefox
```

### Добавить свои зависимости

```dockerfile
RUN apt-get update && apt-get install -y \
    chrome-extension \
    другой-пакет \
    && rm -rf /var/lib/apt/lists/*
```

## ❌ Решение проблем

### Проблема: "Could not initialize class org.codehaus.groovy.runtime.InvokerHelper"

**Решение:** Убедитесь, что используется Java 17+

### Проблема: "Xvfb: command not found"

**Решение:** Xvfb устанавливается автоматически, но может потребоваться перестроить образ

### Проблема: "Connection refused" при подключении к Grid

**Решение:** Убедитесь, что контейнеры запущены:
```bash
docker-compose ps
```

### Проблема: "Недостаточно памяти"

**Решение:** Выделите больше ресурсов Docker в настройках

## 📚 Дополнительные ресурсы

- [Docker Documentation](https://docs.docker.com/)
- [Selenium Grid 4 docs](https://www.selenium.dev/documentation/grid/)
- [Docker Compose docs](https://docs.docker.com/compose/)
- [Best practices для Docker образов](https://docs.docker.com/develop/dev-best-practices/)

