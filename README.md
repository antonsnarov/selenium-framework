# Selenium Test Framework с Docker и CI/CD

Полнофункциональный Selenium-фреймворк для тестирования saucedemo.com с интеграцией Docker, GitHub Actions CI/CD и Allure отчётами.

## 📋 Требования

- Java 17+
- Maven 3.9+
- Docker & Docker Compose (для контейнеризации)
- Chrome или Firefox браузер

## 🚀 Быстрый старт

### Локальный запуск

```bash
# Установка зависимостей
mvn clean install

# Запуск всех тестов
mvn clean test

# Запуск конкретного класса тестов
mvn clean test -Dtest=LoginTest

# Запуск в headless-режиме
mvn clean test -Dheadless=true

# Просмотр Allure-отчёта
mvn allure:report
open allure-report/index.html
```

## 🐳 Docker

### Запуск в Docker контейнере

```bash
# Собрать образ
docker build -t selenium-tests:latest .

# Запустить тесты в контейнере
docker run --rm \
  -v $(pwd)/allure-results:/app/allure-results \
  -e HEADLESS=true \
  selenium-tests:latest
```

### Запуск с Docker Compose

```bash
# Запустить тесты с Selenium Grid
docker-compose up

# Остановить всё
docker-compose down

# Смотреть логи
docker-compose logs -f test-runner
```

**Доступ к Selenium Grid:**
- Hub: http://localhost:4444
- VNC Chrome: localhost:7900 (password: secret)

## 🔄 CI/CD (GitHub Actions)

Тесты автоматически запускаются при:
- `push` в branches `main` или `develop`
- `pull_request` в branches `main` или `develop`
- По расписанию каждый день в 2:00 UTC

**Результаты:**
- ✅ Запуск тестов в headless-режиме
- 📊 Генерация Allure-отчёта
- 📈 Публикация отчёта в GitHub Pages
- 📦 Загрузка артефактов (test-results, allure-results)

## 📁 Структура проекта

```
selenium-framework/
├── src/
│   ├── main/
│   │   ├── java/com/saucedemo/
│   │   │   ├── config/          # ConfigReader для настроек
│   │   │   └── driver/          # WebDriver Factory
│   │   └── resources/
│   │       └── config.properties
│   └── test/
│       ├── java/com/saucedemo/
│       │   ├── pages/           # Page Objects
│       │   └── tests/           # Test Classes
│       └── resources/
│           ├── testng.xml       # TestNG конфигурация
│           └── allure.properties
├── Dockerfile                   # Docker образ
├── docker-compose.yml          # Compose конфигурация
├── .github/workflows/tests.yml # GitHub Actions
└── pom.xml                     # Maven конфигурация
```

## ⚙️ Конфигурация (config.properties)

```properties
# Браузер
browser=chrome              # chrome, firefox

# Режим запуска
headless=true              # true для CI/CD
remote=false               # true для Selenium Grid

# Timeouts
implicit.wait=10
explicit.wait=15
page.load.timeout=30

# Базовый URL
base.url=https://www.saucedemo.com

# Тестовые пользователи (saucedemo)
user.standard=standard_user
user.locked=locked_out_user
password=secret_sauce
```

## 🧪 Примеры запуска

### Запуск конкретных тестов

```bash
# Только LoginTest
mvn clean test -Dtest=LoginTest

# Только методы с CRITICAL severity
mvn clean test -Dgroups=CRITICAL

# На Firefox в headless-режиме
mvn clean test -Dbrowser=firefox -Dheadless=true
```

### Запуск с Selenium Grid

```bash
# Запустить Grid через compose
docker-compose up -d

# Запустить тесты против Grid
mvn clean test -Dremote=true -Dgrid.url=http://localhost:4444
```

## 📊 Allure Отчёты

```bash
# Сгенерировать отчёт
mvn allure:report

# Открыть отчёт
mvn allure:serve
```

Отчёт будет доступен в `allure-report/index.html`

## 🛠️ Технологический стек

| Компонент | Версия |
|-----------|--------|
| Selenium  | 4.18.1 |
| TestNG    | 7.9.0  |
| Allure    | 2.24.0 | Отчёты о тестировании |
| Java      | 17     |
| Maven     | 3.9.6  |
| Docker    | latest |

## 📚 Паттерны

- **Page Object Model** — Инкапсуляция UI элементов в Page Classes
- **Factory Pattern** — Создание WebDriver через DriverFactory
- **Singleton Pattern** — ConfigReader для единственного экземпляра конфигурации
- **ThreadLocal** — Поддержка параллельного выполнения тестов

## 🔐 Переменные окружения

При запуске в Docker/CI можно переопределить:

```bash
HEADLESS=true           # Запуск браузера без UI
BROWSER=chrome          # Браузер
REMOTE=true             # Использовать Selenium Grid
GRID_URL=...            # URL Grid Hub
IMPLICIT_WAIT=10        # Implicit timeout
EXPLICIT_WAIT=15        # Explicit timeout
```

## 🐛 Отладка

### Снятие скриншотов при ошибках
Автоматически к отчёту Allure прикрепляются скриншоты при падении тестов.

### VNC доступ к браузеру в Docker Compose
```bash
# Через VNC viewer к localhost:7900, пароль: secret
```

### Просмотр логов контейнера
```bash
docker-compose logs -f test-runner
```

## 📝 Лучшие практики

1. ✅ Всегда используйте Page Object Model
2. ✅ Добавляйте @Step аннотации для Allure
3. ✅ Используйте fluent-методы для читаемости
4. ✅ Добавляйте @Epic, @Feature, @Story аннотации
5. ✅ Запускайте тесты локально перед push
6. ✅ Регулярно проверяйте Allure отчёты в GitHub Pages

## 📞 Контакты / Поддержка

При вопросах проверьте:
- config.properties на наличие необходимых свойств
- Docker logs на ошибки
- GitHub Actions workflow для CI/CD статуса

