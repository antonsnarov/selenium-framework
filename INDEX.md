# 🎓 Обучающий проект: Docker + CI/CD для Selenium Framework

> Полный пример производственной конфигурации для автоматизированного тестирования с Docker контейнеризацией и GitHub Actions CI/CD

## 📚 Документация (с чего начать)

### 🚀 Для нетерпеливых (5 минут)
Начните отсюда если вы в спешке:
- **[QUICKSTART.md](./QUICKSTART.md)** — 4 команды для запуска тестов

### 📖 Пошаговые гайды (1-2 часа)
1. **[DEPLOYMENT_GUIDE.md](./docs/DEPLOYMENT_GUIDE.md)** — установка и первый запуск
2. **[DOCKER_GUIDE.md](./docs/DOCKER_GUIDE.md)** — изучение Docker (30 мин)
3. **[CI_CD_GUIDE.md](./docs/CI_CD_GUIDE.md)** — изучение GitHub Actions (30 мин)

### 📚 Полная документация (время на изучение)
- **[README.md](./README.md)** — обзор проекта и структура
- **[EXAMPLES.md](./docs/EXAMPLES.md)** — примеры конфигураций и сценарии
- **[CHEATSHEET.md](./docs/CHEATSHEET.md)** — справочник всех команд
- **[SUMMARY.md](./docs/SUMMARY.md)** — что было реализовано

## 🎯 Быстрый старт (выберите один вариант)

### Вариант 1️⃣: Локально (нужен Maven)
```bash
mvn clean test                    # Запустить тесты
mvn allure:serve                  # Посмотреть отчёт
```

### Вариант 2️⃣: Docker одной командой (нужен Docker)
```bash
./run-tests-docker.sh             # Всё автоматически
# + собирает образ
# + запускает тесты
# + генерирует отчёт
```

### Вариант 3️⃣: Selenium Grid (нужен Docker Compose)
```bash
./run-tests-grid.sh               # Запуск с Grid
# + стартует Hub + Node
# + запускает тесты
# + открывает браузер через VNC
```

### Вариант 4️⃣: GitHub Actions (нужен GitHub аккаунт)
```bash
git push origin main              # Тесты запустятся автоматически!
# Смотрите Actions → Latest workflow run
```

## 📁 Что включено в проект

### Файлы Docker & Compose
```
Dockerfile              → Multi-stage образ с браузерами
docker-compose.yml     → Selenium Grid + Test Runner
.dockerignore         → Оптимизация контекста сборки
run-tests-docker.sh   → Скрипт запуска в Docker
run-tests-grid.sh     → Скрипт запуска с Grid
```

### GitHub Actions CI/CD
```
.github/workflows/tests.yml  → Автоматический запуск тестов
```

### Документация (6 полных гайдов + README)
```
QUICKSTART.md                 → Быстрый старт
README.md                     → Основная документация
docs/
├── DEPLOYMENT_GUIDE.md       → Пошаговое развёртывание
├── DOCKER_GUIDE.md           → Полный Docker гайд
├── CI_CD_GUIDE.md            → Полный CI/CD гайд
├── EXAMPLES.md               → Примеры конфигураций
├── CHEATSHEET.md             → Справочник команд
└── SUMMARY.md                → Что было реализовано
```

## 🎓 Рекомендуемый путь обучения

### День 1️⃣ — Docker (2-3 часа)
- [ ] Прочитайте [DOCKER_GUIDE.md](./docs/DOCKER_GUIDE.md)
- [ ] Запустите `docker build -t selenium-tests .`
- [ ] Запустите `docker run selenium-tests:latest`
- [ ] Запустите `./run-tests-docker.sh`
- [ ] Используйте `docker ps`, `docker logs`

### День 2️⃣ — GitHub Actions (1-2 часа)
- [ ] Прочитайте [CI_CD_GUIDE.md](./docs/CI_CD_GUIDE.md)
- [ ] Создайте GitHub репо
- [ ] Сделайте `git push`
- [ ] Смотрите в Actions как запускаются тесты
- [ ] Включите GitHub Pages для отчётов

### День 3️⃣ — Selenium Grid (1 час)
- [ ] Прочитайте [EXAMPLES.md](./docs/EXAMPLES.md)
- [ ] Запустите `./run-tests-grid.sh`
- [ ] Подключитесь к VNC (localhost:7900)
- [ ] Видите браузер в реальном времени

### День 4️⃣ — Практика (2+ часа)
- [ ] Добавьте новый тест
- [ ] Запустите локально, в Docker, в CI/CD
- [ ] Модифицируйте docker-compose.yml
- [ ] Добавьте Firefox ноду
- [ ] Интегрируйте уведомления (Slack/Teams)

## 💡 Ключевые команды

```bash
# Maven
mvn clean test                      # Локальный запуск
mvn allure:serve                    # Просмотр отчёта
mvn test -Dtest=LoginTest          # Конкретный класс
mvn test -Dheadless=true           # Headless режим

# Docker
docker build -t selenium-tests .   # Собрать образ
docker run selenium-tests:latest   # Запустить
docker ps                          # Список контейнеров
docker logs <id>                   # Логи

# Docker Compose
docker-compose up                  # Запустить всё
docker-compose down                # Остановить
docker-compose logs -f             # Логи в реальном времени

# Scripts
./run-tests-docker.sh             # Полный цикл в Docker
./run-tests-grid.sh               # С Selenium Grid
```

Полный справочник смотрите в [CHEATSHEET.md](./docs/CHEATSHEET.md)

## 🏗️ Архитектура

```
┌─────────────────────────────────────────────────┐
│           GitHub Actions (CI/CD)                │
│  Запускает тесты на каждый push/PR/по времени  │
└────────────────────┬────────────────────────────┘
                     │
        ┌────────────┴────────────┐
        │                         │
┌───────▼──────────┐    ┌────────▼────────────┐
│  Maven Local     │    │  Docker Container   │
│  (локальный)     │    │  (изолированно)     │
└──────────────────┘    └──────────┬──────────┘
                                   │
                        ┌──────────▼──────────┐
                        │  Selenium Grid      │
                        │  Hub + Chrome Nodes │
                        │  (параллелизм)      │
                        └─────────────────────┘
```

## 🎯 Что вы изучите

✅ **Docker контейнеризация** — упаковать Java приложение
✅ **Multi-stage builds** — оптимизировать размер образов
✅ **Docker Compose** — оркестрировать несколько сервисов
✅ **Selenium Grid** — параллельное тестирование
✅ **GitHub Actions** — CI/CD pipeline
✅ **Allure Reports** — красивые отчёты
✅ **Best practices** — как делают профессионалы
✅ **DevOps основы** — логирование, мониторинг, масштабирование

## 📊 Технологический стек

| Компонент | Версия | Назначение |
|-----------|--------|-----------|
| Java | 17 | Язык программирования |
| Selenium | 4.18.1 | Автоматизация браузера |
| TestNG | 7.9.0 | Фреймворк для тестов |
| Maven | 3.9.6 | Сборка проекта |
| Allure | 2.24.0 | Отчёты о тестировании |
| Docker | latest | Контейнеризация |
| Docker Compose | 3.8 | Оркестрация сервисов |
| GitHub Actions | — | CI/CD pipeline |

## 🔐 Security & Best Practices

✅ Использование GitHub Secrets для credentials
✅ Headless браузер в CI для безопасности
✅ Multi-stage Docker builds для минимизации риска
✅ Защита main ветки от неправильных commit'ов
✅ ThreadLocal в DriverFactory для параллельного выполнения
✅ Page Object Model для поддерживаемости кода

## 📈 Масштабирование

```bash
# Добавить 3 Chrome ноды
docker-compose up -d --scale chrome-node=3

# Запустить тесты параллельно
mvn test -DthreadCount=10

# GitHub Actions матрица для разных конфигураций
strategy:
  matrix:
    java-version: [17, 21]
    browser: [chrome, firefox]
```

## 🆘 Если что-то не работает

1. **Проверьте логи:**
   ```bash
   docker logs <container-id>
   docker-compose logs -f
   ```

2. **Прочитайте гайд по решению проблем:**
   - [DOCKER_GUIDE.md](./docs/DOCKER_GUIDE.md#-решение-проблем)
   - [CI_CD_GUIDE.md](./docs/CI_CD_GUIDE.md#-отладка-workflow)
   - [DEPLOYMENT_GUIDE.md](./docs/DEPLOYMENT_GUIDE.md#-решение-проблем)

3. **Посмотрите CHEATSHEET:**
   - [CHEATSHEET.md](./docs/CHEATSHEET.md#-решение-проблем)

## 📞 Что дальше?

После изучения базов:

### 🚀 Продвинутые темы
- Интеграция с Slack/Teams уведомлениями
- Отправка отчётов по Email
- Сохранение отчётов в S3
- Интеграция с Jira/TestRail
- Load тестирование с несколькими параллельными запусками

### 🏢 Production использование
- Kubernetes вместо Docker Compose
- Jenkins вместо GitHub Actions
- Sauce Labs вместо локального Selenium Grid
- Docker Registry для сохранения образов
- Мониторинг и логирование (ELK, Datadog)

### 📚 Дальнейшее обучение
- Docker Advanced (volumes, networks, secrets)
- Kubernetes basics (pods, services, deployments)
- CI/CD практики (GitOps, GitLab CI)
- Infrastructure as Code (Terraform, Ansible)

## 📄 Лицензия

MIT License — используйте как угодно!

## 🎉 Поздравляем!

Вы теперь имеете production-ready конфигурацию для автоматизированного тестирования. Это не просто примеры — это реальная конфигурация, используемая в боевых проектах.

---

**Начните с:** [QUICKSTART.md](./QUICKSTART.md) (5 минут)

**Затем читайте:** [DEPLOYMENT_GUIDE.md](./docs/DEPLOYMENT_GUIDE.md) (30 минут)

**Потом изучайте:** [DOCKER_GUIDE.md](./docs/DOCKER_GUIDE.md) и [CI_CD_GUIDE.md](./docs/CI_CD_GUIDE.md) (по часу каждый)

Удачи! 🚀
