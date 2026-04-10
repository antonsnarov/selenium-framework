# 📖 Справочник команд Docker и CI/CD

## ⚡ Самые частые команды

```bash
# Тесты локально
mvn clean test

# Тесты в Docker
./run-tests-docker.sh

# Тесты с Grid
./run-tests-grid.sh

# Просмотр отчёта
mvn allure:serve
```

## 🐳 Docker команды

| Команда | Описание |
|---------|---------|
| `docker build -t name .` | Собрать образ |
| `docker run image` | Запустить контейнер |
| `docker run -it image bash` | Запустить интерактивно |
| `docker ps` | Список запущенных контейнеров |
| `docker ps -a` | Все контейнеры (включая остановленные) |
| `docker logs container` | Логи контейнера |
| `docker logs -f container` | Логи в реальном времени |
| `docker stop container` | Остановить контейнер |
| `docker rm container` | Удалить контейнер |
| `docker rmi image` | Удалить образ |
| `docker exec -it container bash` | Выполнить команду в контейнере |
| `docker inspect image` | Информация об образе |
| `docker images` | Список образов |
| `docker prune` | Очистить неиспользуемые ресурсы |

## 🔧 Docker Compose команды

| Команда | Описание |
|---------|---------|
| `docker-compose up` | Запустить сервисы |
| `docker-compose up -d` | Запустить в фоне |
| `docker-compose down` | Остановить и удалить контейнеры |
| `docker-compose ps` | Статус сервисов |
| `docker-compose logs` | Логи всех сервисов |
| `docker-compose logs -f service` | Логи конкретного сервиса |
| `docker-compose exec service bash` | Команда в контейнере |
| `docker-compose restart` | Перезапустить все |
| `docker-compose build` | Пересобрать образы |
| `docker-compose pull` | Скачать новые версии образов |

## 🧪 Maven команды

| Команда | Описание |
|---------|---------|
| `mvn clean` | Удалить target директорию |
| `mvn compile` | Компилировать код |
| `mvn test` | Запустить тесты |
| `mvn clean test` | Чистая сборка + тесты |
| `mvn test -Dtest=Class` | Конкретный класс тестов |
| `mvn test -Dtest=*Login*` | По паттерну имени |
| `mvn test -Dheadless=true` | В headless-режиме |
| `mvn allure:report` | Генерировать отчёт |
| `mvn allure:serve` | Просмотреть отчёт |
| `mvn dependency:tree` | Дерево зависимостей |
| `mvn install` | Установить в local repo |
| `mvn package` | Собрать JAR/WAR |

## 📊 GitHub Actions команды

| Элемент | Описание |
|---------|---------|
| `on.push` | Триггер на push |
| `on.pull_request` | Триггер на PR |
| `on.schedule` | Триггер по времени |
| `jobs.*.runs-on` | ОС для запуска (ubuntu-latest) |
| `steps.*.run` | Команда для выполнения |
| `steps.*.uses` | Action из marketplace |
| `if: success()` | Условие если прошло |
| `if: failure()` | Условие если упало |
| `if: always()` | Всегда выполнить |

## 🔐 Переменные окружения (Docker)

| Переменная | Значение | Влияние |
|------------|----------|--------|
| `HEADLESS` | true/false | Запуск браузера без UI |
| `BROWSER` | chrome/firefox | Выбор браузера |
| `REMOTE` | true/false | Использовать Grid |
| `GRID_URL` | http://... | URL Grid Hub |
| `IMPLICIT_WAIT` | число | Implicit timeout (сек) |
| `EXPLICIT_WAIT` | число | Explicit timeout (сек) |
| `PAGE_LOAD_TIMEOUT` | число | Page load timeout (сек) |
| `DISPLAY` | :99 | X11 display для headless |

## 📝 Структура Dockerfile

```dockerfile
FROM <base-image>           # Базовый образ
WORKDIR <path>              # Рабочая директория
COPY <src> <dest>           # Копировать файлы
RUN <command>               # Выполнить команду
EXPOSE <port>               # Открыть порт
ENV <key>=<value>           # Переменная окружения
CMD ["command"]             # Команда по умолчанию
ENTRYPOINT ["command"]      # Точка входа
```

## 📋 Структура docker-compose.yml

```yaml
version: '3.8'              # Версия синтаксиса
services:                   # Сервисы/контейнеры
  service-name:
    image: image:tag        # Образ
    build: .                # Путь к Dockerfile
    ports:                  # Маппинг портов
      - "8080:8080"
    environment:            # Переменные окружения
      KEY: value
    volumes:                # Монтирование директорий
      - ./local:/container
    depends_on:             # Зависимости
      - other-service
    command: sh script.sh   # Команда запуска
    restart: always         # Policy перезапуска

volumes:                    # Именованные volumes
  volume-name:
```

## 🔄 Cron синтаксис

```
*    *    *    *    *
│    │    │    │    └─ День недели (0-6)
│    │    │    └────── Месяц (1-12)
│    │    └─────────── День (1-31)
│    └──────────────── Час (0-23)
└───────────────────── Минута (0-59)
```

**Примеры:**
- `0 9 * * *` — 9:00 каждый день
- `0 9 * * 1-5` — 9:00 будни (пн-пт)
- `*/30 * * * *` — каждые 30 минут
- `0 */6 * * *` — каждые 6 часов
- `0 0 1 * *` — первое число каждого месяца

## 🎯 Типичные сценарии

### Запуск теста с логированием
```bash
docker run --rm \
  -e HEADLESS=true \
  -v $(pwd)/logs:/app/logs \
  selenium-tests:latest \
  mvn clean test -X
```

### Доступ к контейнеру по SSH (через волшебство)
```bash
docker run --rm -d \
  -p 22:22 \
  -e PASSWORD=password \
  selenium-tests:latest

ssh root@localhost
```

### Запуск несколько тестов параллельно
```bash
for i in {1..3}; do
  docker run --rm selenium-tests:latest &
done
wait
```

### Мониторинг ресурсов контейнера
```bash
docker stats container-name
```

### Копирование файлов из контейнера
```bash
docker cp container:/app/allure-results ./results
```

## 🚨 Решение проблем

### "Image not found"
```bash
docker build -t selenium-tests:latest .
```

### "Port already in use"
```bash
# Найти процесс на порту
lsof -i :8080

# Или использовать другой порт
docker run -p 9090:8080 image
```

### "Cannot connect to Docker daemon"
```bash
# Запустить Docker Desktop или daemon
docker-machine start
```

### "Out of memory"
```bash
# Увеличить память Docker в настройках
# Docker Desktop → Preferences → Resources
```

## 📊 Мониторинг

### Размер образов
```bash
docker images --format "table {{.Repository}}\t{{.Size}}"
```

### Использование дискового пространства
```bash
docker system df
docker system prune -a  # Очистить всё неиспользуемое
```

### Статистика контейнеров
```bash
docker stats --no-stream
```

## 💾 Резервное копирование

### Сохранить образ
```bash
docker save selenium-tests:latest | gzip > backup.tar.gz
```

### Загрузить образ
```bash
gunzip -c backup.tar.gz | docker load
```

### Сохранить volume
```bash
docker run --rm -v volume-name:/data -v $(pwd):/backup \
  alpine tar czf /backup/volume.tar.gz -C /data .
```

## 🌐 Networking

### Проверить DNS в контейнере
```bash
docker run --rm alpine nslookup google.com
```

### Подключение контейнера к сети
```bash
docker network create my-network
docker run --network my-network image
```

### Просмотр сетей
```bash
docker network ls
docker network inspect my-network
```

## 📦 Registry (Docker Hub)

```bash
# Логин
docker login

# Тег образа для публикации
docker tag selenium-tests:latest username/selenium-tests:latest

# Публикация
docker push username/selenium-tests:latest

# Скачивание
docker pull username/selenium-tests:latest
```

## ✅ Чеклист для начинающих

- [ ] Установил Docker и Docker Compose
- [ ] Собрал образ: `docker build -t selenium-tests .`
- [ ] Запустил контейнер: `docker run selenium-tests:latest`
- [ ] Запустил Docker Compose: `docker-compose up`
- [ ] Посмотрел логи: `docker logs container-id`
- [ ] Создал GitHub Actions workflow
- [ ] Запустил тесты через GitHub Actions
- [ ] Просмотрел отчёт в GitHub Pages
- [ ] Добавил новый тест и проверил CI/CD
- [ ] Настроил напоминание о падении тестов

Поздравляем! 🎉 Вы готовы использовать Docker и CI/CD в своих проектах!

