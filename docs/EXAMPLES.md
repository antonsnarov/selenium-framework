# Примеры использования Docker и CI/CD

## 🐳 Docker примеры

### 1. Базовый запуск с custom конфигурацией

```bash
docker run --rm \
  -e HEADLESS=true \
  -e BROWSER=firefox \
  -e IMPLICIT_WAIT=15 \
  -v $(pwd)/allure-results:/app/allure-results \
  selenium-tests:latest
```

### 2. Интерактивный режим для отладки

```bash
docker run -it \
  -v $(pwd)/src:/app/src \
  selenium-tests:latest \
  /bin/bash
```

Теперь можете запускать команды вручную:
```bash
mvn clean test -Dtest=LoginTest
```

### 3. Запуск с привязкой портов (VNC)

```bash
docker run -it \
  -p 5900:5900 \
  selenium-tests:latest
```

Подключитесь через VNC на localhost:5900

### 4. Сборка для определённого браузера

```bash
docker build \
  --build-arg BROWSER=firefox \
  -t selenium-tests:firefox .
```

## 🔄 Docker Compose примеры

### 1. Запуск с несколькими Chrome нодами

```yaml
version: '3.8'
services:
  selenium-hub:
    image: selenium/hub:4.18.1
    ports:
      - "4444:4444"

  chrome-node-1:
    image: selenium/node-chrome:4.18.1
    depends_on:
      - selenium-hub
    environment:
      SE_EVENT_BUS_HOST: selenium-hub
      SE_EVENT_BUS_PUBLISH_PORT: 4442
      SE_EVENT_BUS_SUBSCRIBE_PORT: 4443
      SE_NODE_MAX_INSTANCES: 2

  chrome-node-2:
    image: selenium/node-chrome:4.18.1
    depends_on:
      - selenium-hub
    environment:
      SE_EVENT_BUS_HOST: selenium-hub
      SE_EVENT_BUS_PUBLISH_PORT: 4442
      SE_EVENT_BUS_SUBSCRIBE_PORT: 4443
      SE_NODE_MAX_INSTANCES: 2

  test-runner:
    build: .
    depends_on:
      - selenium-hub
    environment:
      REMOTE: "true"
      GRID_URL: "http://selenium-hub:4444"
```

### 2. С PostgreSQL для логирования результатов

```yaml
version: '3.8'
services:
  db:
    image: postgres:15
    environment:
      POSTGRES_DB: test_results
      POSTGRES_USER: user
      POSTGRES_PASSWORD: password
    ports:
      - "5432:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data

  test-runner:
    build: .
    depends_on:
      - db
    environment:
      DB_HOST: db
      DB_USER: user
      DB_PASSWORD: password

volumes:
  postgres_data:
```

### 3. С Apache Mesos для масштабирования

```yaml
version: '3.8'
services:
  selenium-hub:
    image: selenium/hub:4.18.1
    ports:
      - "4444:4444"
    environment:
      SE_NODE_MAX_INSTANCES: 100

  chrome-node:
    image: selenium/node-chrome:4.18.1
    depends_on:
      - selenium-hub
    environment:
      SE_EVENT_BUS_HOST: selenium-hub
      SE_NODE_MAX_INSTANCES: 10
    deploy:
      replicas: 5  # 5 экземпляров
```

## 🔄 CI/CD примеры

### 1. Расширенный GitHub Actions workflow с уведомлениями

```yaml
name: Tests with Slack Notifications

on:
  push:
    branches: [main]

jobs:
  test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      
      - name: Set up JDK
        uses: actions/setup-java@v4
        with:
          java-version: '17'
          cache: maven
      
      - name: Run Tests
        id: tests
        run: mvn clean test
        continue-on-error: true
      
      - name: Notify Slack on Success
        if: success()
        uses: slackapi/slack-github-action@v1
        with:
          webhook-url: ${{ secrets.SLACK_WEBHOOK }}
          payload: |
            {
              "text": "✅ Tests passed for ${{ github.repository }}",
              "blocks": [
                {"type": "section", "text": {"type": "mrkdwn", "text": "*Tests Passed* ✅"}}
              ]
            }
      
      - name: Notify Slack on Failure
        if: failure()
        uses: slackapi/slack-github-action@v1
        with:
          webhook-url: ${{ secrets.SLACK_WEBHOOK }}
          payload: |
            {
              "text": "❌ Tests failed for ${{ github.repository }}",
              "attachments": [
                {
                  "color": "danger",
                  "text": "Failed: ${{ steps.tests.outcome }}"
                }
              ]
            }
```

### 2. Матрица для тестирования на разных ОС

```yaml
jobs:
  test:
    strategy:
      matrix:
        os: [ubuntu-latest, windows-latest, macos-latest]
    runs-on: ${{ matrix.os }}
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-java@v4
        with:
          java-version: '17'
          cache: maven
      - run: mvn clean test
```

### 3. Scheduled регрессионное тестирование

```yaml
on:
  schedule:
    - cron: '0 0 * * *'      # Каждый день в midnight UTC
    - cron: '0 */6 * * *'    # Каждые 6 часов
    - cron: '0 9 * * 1'      # Понедельник в 9:00

jobs:
  regression-tests:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-java@v4
        with:
          java-version: '17'
      
      - name: Run Full Regression Suite
        run: mvn clean test -Dtest=**/*Test
      
      - name: Create GitHub Issue if Failed
        if: failure()
        uses: actions/github-script@v6
        with:
          script: |
            github.rest.issues.create({
              owner: context.repo.owner,
              repo: context.repo.repo,
              title: '🚨 Regression Test Failed',
              body: 'Regression tests failed on ' + new Date().toISOString(),
              labels: ['regression', 'critical']
            })
```

### 4. Deploy отчёта в S3

```yaml
- name: Upload Allure Report to S3
  if: always()
  uses: jakejarvis/s3-cp-action@master
  with:
    args: --recursive --delete
  env:
    AWS_S3_BUCKET: ${{ secrets.AWS_S3_BUCKET }}
    AWS_ACCESS_KEY_ID: ${{ secrets.AWS_ACCESS_KEY_ID }}
    AWS_SECRET_ACCESS_KEY: ${{ secrets.AWS_SECRET_ACCESS_KEY }}
    SOURCE_DIR: 'allure-report'
    DEST_DIR: 'test-reports/build-${{ github.run_number }}'
```

### 5. Уведомление в Teams

```yaml
- name: Notify Teams
  if: always()
  uses: jdcargile/ms-teams-notification@v1.3
  with:
    github-token: ${{ github.token }}
    ms-teams-webhook-uri: ${{ secrets.MS_TEAMS_WEBHOOK_URI }}
    notification-color: ${{ job.status == 'success' && '28a745' || 'dc3545' }}
```

## 📝 Makefile для упрощения команд

Создайте `Makefile`:

```makefile
.PHONY: test docker-build docker-run docker-compose-up help

help:
	@echo "Available commands:"
	@echo "  make test              - Run tests locally"
	@echo "  make test-headless     - Run tests in headless mode"
	@echo "  make docker-build      - Build Docker image"
	@echo "  make docker-run        - Run tests in Docker"
	@echo "  make docker-compose-up - Start Docker Compose stack"
	@echo "  make docker-compose-down - Stop Docker Compose"
	@echo "  make allure-report     - Generate Allure report"
	@echo "  make allure-serve      - View Allure report"

test:
	mvn clean test

test-headless:
	mvn clean test -Dheadless=true

docker-build:
	docker build -t selenium-tests:latest .

docker-run: docker-build
	docker run --rm \
		-v $$(pwd)/allure-results:/app/allure-results \
		-e HEADLESS=true \
		selenium-tests:latest

docker-compose-up:
	docker-compose up

docker-compose-down:
	docker-compose down

allure-report:
	mvn allure:report

allure-serve:
	mvn allure:serve

clean:
	mvn clean
	rm -rf allure-results allure-report
	docker rmi selenium-tests:latest

.DEFAULT_GOAL := help
```

Теперь можете использовать:
```bash
make test
make docker-run
make allure-serve
make help
```

## 🛠️ Custom скрипты

### deploy-report.sh

```bash
#!/bin/bash
set -e

REPORT_DIR="allure-report"
S3_BUCKET="s3://my-test-reports"

echo "📊 Generating report..."
mvn allure:report

echo "📤 Uploading to S3..."
aws s3 sync "$REPORT_DIR" "$S3_BUCKET/build-$BUILD_NUMBER/"

echo "✅ Report deployed to $S3_BUCKET/build-$BUILD_NUMBER/"
```

### parallel-tests.sh

```bash
#!/bin/bash

echo "🚀 Running tests in parallel..."

mvn clean test \
  -DsuiteXmlFile=src/test/resources/testng.xml \
  -Dthread-count=4 \
  -Dparallel=methods

echo "✅ Parallel tests completed"
```

## 🎯 Реальные сценарии

### Сценарий 1: Smoke тесты на каждый push

```yaml
on:
  push:
    branches: [develop]

jobs:
  smoke:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-java@v4
        with:
          java-version: '17'
          cache: maven
      - run: mvn clean test -Dtest=LoginTest  # Только smoke тесты
```

### Сценарий 2: Полная регрессия по расписанию

```yaml
on:
  schedule:
    - cron: '0 2 * * *'  # Ночью

jobs:
  regression:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-java@v4
        with:
          java-version: '17'
          cache: maven
      - run: mvn clean test  # Все тесты
      - run: mvn allure:report
```

### Сценарий 3: Load тестирование в Docker Compose

```bash
#!/bin/bash
# load-test.sh

for i in {1..10}; do
  docker run --rm \
    --network host \
    -e REMOTE=true \
    -e GRID_URL=http://localhost:4444 \
    selenium-tests:latest &
done

wait
echo "✅ Load test completed"
```

Используйте эти примеры как основу для своей конфигурации! 🚀

