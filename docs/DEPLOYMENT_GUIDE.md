# 🚀 Инструкция по первому развёртыванию Docker и CI/CD

Следуйте этой инструкции шаг за шагом, чтобы установить Docker и CI/CD для вашего проекта.

## Шаг 1: Установка Docker (если не установлен)

### macOS
```bash
# Через Homebrew
brew install docker docker-compose

# Или скачайте Docker Desktop
# https://www.docker.com/products/docker-desktop

# Проверка установки
docker --version
docker-compose --version
```

### Linux (Ubuntu/Debian)
```bash
# Установка Docker
sudo apt-get update
sudo apt-get install docker.io docker-compose -y

# Запуск Docker daemon
sudo systemctl start docker
sudo systemctl enable docker

# Добавить текущего пользователя в группу docker
sudo usermod -aG docker $USER
newgrp docker

# Проверка
docker --version
```

### Windows
```bash
# Установите Docker Desktop для Windows
# https://www.docker.com/products/docker-desktop

# В PowerShell проверьте установку
docker --version
```

## Шаг 2: Первый локальный запуск

### Без Docker (самый быстрый способ)

```bash
# Перейдите в директорию проекта
cd ~/IdeaProjects/selenium-framework

# Запустите тесты
mvn clean test

# Просмотрите отчёт
mvn allure:serve
```

### В Docker контейнере

```bash
# Собрать образ (первый раз, займёт время)
docker build -t selenium-tests:latest .

# Запустить тесты
docker run --rm \
  -v $(pwd)/allure-results:/app/allure-results \
  -e HEADLESS=true \
  selenium-tests:latest

# Проверить артефакты
ls -la allure-results/
```

### С Docker Compose (Selenium Grid)

```bash
# Убедитесь, что Docker запущен
docker ps

# Запустите весь стэк
docker-compose up

# В другом терминале проверьте статус
docker-compose ps

# Просмотрите логи
docker-compose logs -f test-runner

# Остановите (Ctrl+C) и очистите
docker-compose down
```

## Шаг 3: Подготовка GitHub репо

### 3.1 Создайте репо на GitHub

1. Откройте https://github.com/new
2. Назовите его `selenium-framework`
3. Выберите **Public** (чтобы GitHub Pages работал бесплатно)
4. Кликните **Create repository**

### 3.2 Инициализируйте Git локально

```bash
cd ~/IdeaProjects/selenium-framework

# Инициализируем Git (если ещё не инициализирован)
git init
git config user.name "Your Name"
git config user.email "your.email@example.com"

# Проверяем текущие файлы
git status
```

### 3.3 Добавьте remote и первый commit

```bash
# Замените YOUR-USERNAME на ваш GitHub username
git remote add origin https://github.com/YOUR-USERNAME/selenium-framework.git

# Проверяем remote
git remote -v

# Добавляем все файлы
git add .

# Создаём первый commit
git commit -m "Initial commit: Add Selenium framework with Docker and CI/CD"

# Меняем ветку на main (GitHub требует main, а не master)
git branch -M main

# Pushим код
git push -u origin main
```

## Шаг 4: Включаем GitHub Actions

GitHub Actions автоматически запустится, как только вы сделаете push.

### Проверьте работу

1. Откройте https://github.com/YOUR-USERNAME/selenium-framework
2. Кликните вкладку **Actions**
3. Должен быть workflow run с статусом
4. Дождитесь завершения (обычно 3-5 минут)

### Если тесты упали

1. Кликните на workflow run
2. Посмотрите логи каждого шага
3. Исправьте ошибку локально
4. Сделайте новый commit и push

## Шаг 5: Включаем GitHub Pages (для Allure отчётов)

### 5.1 Создайте пустую ветку gh-pages

```bash
git checkout --orphan gh-pages
git reset --hard
git commit --allow-empty -m "Initial gh-pages branch"
git push -u origin gh-pages

# Вернитесь на main
git checkout main
```

### 5.2 Включите GitHub Pages в Settings

1. Откройте https://github.com/YOUR-USERNAME/selenium-framework/settings/pages
2. **Source**: Deploy from a branch
3. **Branch**: gh-pages / root
4. Кликните **Save**

### 5.3 Проверьте развёртывание

1. Откройте вкладку **Actions** и дождитесь workflow
2. Откройте https://YOUR-USERNAME.github.io/selenium-framework/
3. Должен открыться Allure отчёт

## Шаг 6: Добавляем защиту ветки (опционально, но рекомендуется)

1. Settings → Branches → Branch protection rules
2. Кликните **Add rule**
3. Pattern: `main`
4. ✅ Require status checks to pass before merging
5. Выберите `test` job
6. Кликните **Create**

Теперь коммиты в main обязательно пройдут тесты перед merge.

## Шаг 7: Локальные скрипты для удобства

```bash
# Убедитесь, что скрипты исполняемы
chmod +x run-tests-docker.sh
chmod +x run-tests-grid.sh

# Теперь можете запускать одной командой
./run-tests-docker.sh
./run-tests-grid.sh
```

## Шаг 8: Проверяем всё работает

### Локальный запуск
```bash
mvn clean test
# ✅ Должны пройти все тесты
```

### Docker запуск
```bash
./run-tests-docker.sh
# ✅ Должны пройти все тесты в контейнере
```

### GitHub Actions
1. Откройте Actions → Latest workflow run
2. ✅ Все шаги должны быть зелёными

### GitHub Pages отчёт
1. Откройте https://YOUR-USERNAME.github.io/selenium-framework/
2. ✅ Должен отобразиться Allure отчёт

## Шаг 9: Сделайте тестовый commit

```bash
# Добавьте новый тест или измените существующий
echo "// Test change" >> src/test/java/com/saucedemo/tests/LoginTest.java

# Commit и push
git add .
git commit -m "Test: Add test change to trigger CI/CD"
git push

# Откройте Actions и смотрите как запускается workflow
```

## 🎓 Теперь вы готовы!

Вы успешно:
- ✅ Установили Docker
- ✅ Запустили тесты локально и в контейнере
- ✅ Создали GitHub репо
- ✅ Настроили GitHub Actions CI/CD
- ✅ Включили GitHub Pages для отчётов
- ✅ Защитили main ветку

## 📚 Дальнейшее обучение

1. **Отредактируйте конфигурацию:**
   - `config.properties` — для разных браузеров и режимов
   - `docker-compose.yml` — добавьте Firefox ноду
   - `.github/workflows/tests.yml` — добавьте email уведомления

2. **Напишите свои тесты:**
   - Добавьте новый Page Object
   - Добавьте новый Test класс
   - Убедитесь, что они проходят локально и в CI/CD

3. **Интеграции:**
   - Slack уведомления при падении тестов
   - Email отчёты
   - Интеграция с Jira
   - Отправка отчётов в S3

## 🆘 Решение проблем

### "Docker not found" на Mac
```bash
# Запустите Docker Desktop сначала
open /Applications/Docker.app
sleep 30
docker ps  # Проверьте подключение
```

### Тесты падают в CI/CD, но локально проходят
- Проверьте `config.properties` на localhost зависимостями
- Убедитесь, что используется headless браузер в CI
- Посмотрите логи GitHub Actions

### GitHub Pages не обновляется
- Подождите 2-3 минуты после workflow запуска
- Очистите кэш браузера (Ctrl+Shift+R)
- Проверьте ветку gh-pages содержит файлы
  ```bash
  git checkout gh-pages
  ls -la
  git checkout main
  ```

### "Permission denied" при запуске скриптов
```bash
chmod +x *.sh
./run-tests-docker.sh
```

## 📞 Получить помощь

1. Проверьте [DOCKER_GUIDE.md](./docs/DOCKER_GUIDE.md) для Docker вопросов
2. Проверьте [CI_CD_GUIDE.md](./docs/CI_CD_GUIDE.md) для GitHub Actions вопросов
3. Посмотрите [EXAMPLES.md](./docs/EXAMPLES.md) для примеров конфигураций
4. Читайте [CHEATSHEET.md](./docs/CHEATSHEET.md) для быстрого справочника

Успехов! 🚀

