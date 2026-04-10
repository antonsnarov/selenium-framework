# 🚀 Быстрый старт с Docker и CI/CD

## 1️⃣ Локальный запуск (без Docker)

```bash
# Установка Maven зависимостей
mvn clean install

# Запуск всех тестов
mvn clean test

# Просмотр Allure отчёта
mvn allure:serve
```

## 2️⃣ Запуск в Docker (одна команда)

```bash
./run-tests-docker.sh
```

Что происходит:
1. ✅ Собирается Docker образ
2. ✅ Запускаются тесты в контейнере
3. ✅ Генерируется Allure отчёт
4. ✅ Отчёт открывается в браузере

## 3️⃣ Запуск с Selenium Grid

```bash
./run-tests-grid.sh
```

Что происходит:
1. ✅ Запускается Selenium Hub + Chrome Node
2. ✅ Тесты подключаются к Grid
3. ✅ Можно видеть браузер через VNC (localhost:7900)

## 4️⃣ CI/CD через GitHub Actions

1. **Скопируйте репо на GitHub**
   ```bash
   git remote add origin https://github.com/your-username/selenium-framework.git
   git push -u origin main
   ```

2. **GitHub Actions автоматически:**
   - ✅ Запустит тесты на каждый push
   - ✅ Генерирует отчёт в GitHub Pages
   - ✅ Показывает статус в PR

3. **Включите GitHub Pages:**
   - Settings → Pages → Deploy from branch → gh-pages

## 📊 Основные команды

### Maven
```bash
mvn clean test                    # Все тесты
mvn test -Dtest=LoginTest        # Конкретный класс
mvn test -Dheadless=true         # В headless-режиме
mvn allure:report                # Генерировать отчёт
mvn allure:serve                 # Просмотреть отчёт
```

### Docker
```bash
docker build -t selenium-tests .  # Собрать образ
docker run --rm selenium-tests    # Запустить контейнер
docker ps                         # Список контейнеров
docker logs <container-id>        # Логи контейнера
```

### Docker Compose
```bash
docker-compose up                 # Запустить всё
docker-compose up -d              # В фоне
docker-compose down               # Остановить
docker-compose logs -f            # Логи в реальном времени
```

## 📁 Структура файлов

```
selenium-framework/
├── Dockerfile                 # Docker образ для тестов
├── docker-compose.yml         # Selenium Grid + Test Runner
├── .github/workflows/tests.yml # GitHub Actions CI/CD
├── docs/
│   ├── DOCKER_GUIDE.md       # Полный гайд по Docker
│   └── CI_CD_GUIDE.md        # Полный гайд по CI/CD
├── run-tests-docker.sh       # Скрипт запуска в Docker
├── run-tests-grid.sh         # Скрипт запуска с Grid
├── src/
│   ├── main/java/com/saucedemo/
│   │   ├── config/ConfigReader.java
│   │   └── driver/DriverFactory.java
│   └── test/java/com/saucedemo/
│       ├── pages/             # Page Objects
│       └── tests/             # Test Classes
└── pom.xml
```

## 🎯 Ваша задача

Для обучения выполните по порядку:

### День 1: Docker
- [ ] Прочитайте `docs/DOCKER_GUIDE.md`
- [ ] Запустите `./run-tests-docker.sh`
- [ ] Измените Dockerfile (добавьте Firefox ноду)
- [ ] Запустите `./run-tests-grid.sh`

### День 2: CI/CD
- [ ] Прочитайте `docs/CI_CD_GUIDE.md`
- [ ] Создайте GitHub репо и push кода
- [ ] Проверьте, что GitHub Actions запускает тесты
- [ ] Включите GitHub Pages для отчётов

### День 3: Практика
- [ ] Добавьте новый тест в CartTest
- [ ] Сделайте commit и push
- [ ] Проверьте, что тест запустился в CI/CD
- [ ] Посмотрите отчёт в GitHub Pages

## 🔍 Проверка работы

### Локально
```bash
# Должны пройти все тесты
mvn clean test

# Отчёт должен открыться
mvn allure:serve
```

### В Docker
```bash
# Тесты запускаются в контейнере
docker run --rm selenium-tests:latest

# Должны создаться артефакты
ls -la target/surefire-reports/
```

### На GitHub
1. Откройте `https://github.com/your-username/selenium-framework`
2. Перейдите на вкладку **Actions**
3. Должны быть успешные workflow запуски ✅

## 💡 Советы для обучения

1. **Читайте логи внимательно** — они содержат всю информацию об ошибках
2. **Используйте `docker logs`** — чтобы отладить контейнер
3. **Смотрите исходный код** — это лучший учебник
4. **Экспериментируйте** — меняйте конфигурацию и смотрите результат
5. **GitHub Actions UI** — очень информативна для отладки

## 🆘 Если что-то не работает

### Docker не запускается
```bash
# Проверьте, установлен ли Docker
docker --version

# Проверьте логи сборки
docker build -t test . --progress=plain
```

### Тесты падают в Docker
```bash
# Запустите контейнер интерактивно
docker run -it selenium-tests:latest /bin/bash

# Посмотрите логи
docker logs <container-id>
```

### GitHub Actions не запускается
1. Проверьте `.github/workflows/tests.yml` синтаксис
2. Откройте Actions → workflow run → посмотрите логи
3. Убедитесь, что main ветка создана

## 📚 Полезные ссылки

- [Docker Documentation](https://docs.docker.com/)
- [Selenium Grid 4](https://www.selenium.dev/documentation/grid/)
- [GitHub Actions](https://docs.github.com/en/actions)
- [Allure Report](https://docs.qameta.io/allure/)

## 🎓 Что вы изучите

После выполнения всех шагов вы поймёте:

✅ Как контейнеризировать Java приложение
✅ Как использовать Docker Compose для оркестрации сервисов
✅ Как настроить CI/CD pipeline в GitHub Actions
✅ Как автоматизировать запуск тестов
✅ Как интегрировать отчёты (Allure) в workflow
✅ Как использовать Selenium Grid для параллельного тестирования

Удачи в обучении! 🚀

