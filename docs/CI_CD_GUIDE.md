# CI/CD Guide для Selenium тестов

## 🔄 Что такое CI/CD?

**CI (Continuous Integration)** — автоматический запуск тестов при каждом коммите
**CD (Continuous Deployment)** — автоматическое развёртывание при успешных тестах

## 🚀 GitHub Actions Workflow

Наш workflow файл (`.github/workflows/tests.yml`) выполняет:

1. ✅ Запуск тестов при push/PR в main/develop
2. ✅ Генерация Allure отчётов
3. ✅ Загрузка артефактов (результаты тестов)
4. ✅ Публикация отчёта в GitHub Pages
5. ✅ Ежедневное планировщика на 2:00 UTC

## 📋 Структура Workflow

```yaml
name: Automated Tests CI/CD          # Имя workflow

on:                                   # Когда запускать
  push:
    branches: [main, develop]        # На push в ветки
  pull_request:
    branches: [main, develop]        # На PR в ветки
  schedule:
    - cron: '0 2 * * *'              # Каждый день в 2:00 UTC

jobs:
  test:                              # Job 1: Запуск тестов
    runs-on: ubuntu-latest           # На Ubuntu
    
    steps:
      - uses: actions/checkout@v4    # 1. Скачать код
      - uses: actions/setup-java@v4  # 2. Установить Java
      - run: mvn clean test          # 3. Запустить тесты
      - run: mvn allure:report       # 4. Сгенерировать отчёт
      - uses: actions/upload-artifact # 5. Загрузить артефакты
```

## 🔑 Ключевые сценарии

### 1. Запуск на Push в main/develop

```yaml
on:
  push:
    branches: [main, develop]
```

Когда разработчик делает `git push`, GitHub Actions:
1. Скачивает код
2. Запускает все тесты
3. Если тесты упали — помечает commit как failed
4. Если прошли — commit отмечается зелёным ✅

### 2. Запуск на Pull Request

```yaml
on:
  pull_request:
    branches: [main, develop]
```

При создании PR:
1. Тесты запускаются автоматически
2. Результат показывается в PR
3. Можно требовать, чтобы тесты прошли перед merge

**Как требовать успешные тесты:**
- GitHub → Repo Settings → Branches → Branch protection rules
- ✅ Require status checks to pass before merging

### 3. Ежедневное расписание

```yaml
schedule:
  - cron: '0 2 * * *'  # Каждый день в 2:00 UTC
```

Полезно для обнаружения хрупких тестов или проблем с зависимостями.

**Формат Cron:**
```
*    *    *    *    *
│    │    │    │    └─ День недели (0=Sunday, 6=Saturday)
│    │    │    └────── Месяц
│    │    └─────────── День месяца
│    └──────────────── Час
└───────────────────── Минута
```

**Примеры:**
- `0 2 * * *` — каждый день в 2:00
- `0 9 * * 1-5` — будни в 9:00
- `0 */6 * * *` — каждые 6 часов

## 📊 Artifacts и Reports

### Upload Artifacts

```yaml
- uses: actions/upload-artifact@v4
  with:
    name: test-results
    path: target/surefire-reports/
```

**Где найти:**
1. GitHub → Actions → Workflow run → Artifacts
2. Скачать ZIP с результатами

### Allure Report

```yaml
- uses: simple-elf/allure-report-action@master
  with:
    allure_results: allure-results
    keep_reports: 20
```

Автоматически:
1. Генерирует HTML отчёт
2. Сравнивает с предыдущими запусками
3. Показывает тренды (графики успеха/падения)

### GitHub Pages

```yaml
- uses: peaceiris/actions-gh-pages@v3
  with:
    github_token: ${{ secrets.GITHUB_TOKEN }}
    publish_dir: allure-report
```

Публикует отчёт на: `https://username.github.io/repo-name/`

**Как включить GitHub Pages:**
1. Repo Settings → Pages
2. Source: GitHub Actions
3. Отчёт будет доступен автоматически

## 🏗️ Build Matrix (Несколько версий)

```yaml
strategy:
  matrix:
    java-version: ['17', '21']
    browser: ['chrome', 'firefox']
```

Это запустит тесты на каждой комбинации (Java 17 + Chrome, Java 17 + Firefox и т.д.)

## 🔐 Secrets и Variables

### GitHub Secrets

Для чувствительных данных (пароли, API ключи):

```yaml
env:
  SAUCE_LABS_KEY: ${{ secrets.SAUCE_LABS_KEY }}
```

**Как добавить Secret:**
1. Repo Settings → Secrets and variables → Actions
2. New repository secret
3. Использовать в workflow: `${{ secrets.SECRET_NAME }}`

### Environment Variables

```yaml
env:
  HEADLESS: true
  BROWSER: chrome
```

## 📈 Status Badges

Добавьте badge статуса в README:

```markdown
[![Tests](https://github.com/username/repo/actions/workflows/tests.yml/badge.svg)](https://github.com/username/repo/actions)
```

## 🚀 Продвинутые техники

### Условное выполнение шагов

```yaml
- name: Deploy
  if: github.ref == 'refs/heads/main' && success()
  run: ./deploy.sh
```

### Кэширование зависимостей

```yaml
- uses: actions/setup-java@v4
  with:
    java-version: '17'
    distribution: 'temurin'
    cache: maven  # ← Автоматически кэширует Maven зависимости
```

### Параллельное выполнение Job'ов

```yaml
jobs:
  test:
    runs-on: ubuntu-latest
    # ...
  
  docker-build:
    runs-on: ubuntu-latest
    needs: test  # ← Зависит от job 'test'
    if: success()
    # ...
```

### Уведомления о падении

```yaml
- name: Notify on Failure
  if: failure()
  uses: your-notification-action
  with:
    message: "Tests failed!"
```

## 🔍 Отладка Workflow

### Просмотр логов

1. GitHub → Actions → Workflow run
2. Кликнуть на job → посмотреть логи каждого шага

### Debug Mode

Включить debug логи:

```yaml
env:
  ACTIONS_STEP_DEBUG: true
```

### Local Testing (Act)

Можно запустить workflow локально:

```bash
# Установить act: https://github.com/nektos/act
act -j test
```

## 🛠️ Типичные конфигурации

### Запуск только определённых тестов

```yaml
- run: mvn test -Dtest=LoginTest
```

### Параллельное выполнение тестов

```yaml
- run: mvn test -DsuiteXmlFile=src/test/resources/testng.xml
```

### Отправка отчёта по Email

```yaml
- name: Send Email Report
  if: always()
  uses: dawidd6/action-send-mail@v3
  with:
    server_address: ${{ secrets.EMAIL_SERVER }}
    server_port: 465
    username: ${{ secrets.EMAIL_USERNAME }}
    password: ${{ secrets.EMAIL_PASSWORD }}
    subject: "Test Report"
    body: "Tests ${{ job.status }}"
```

## 🔄 GitLab CI (альтернатива GitHub Actions)

Если используете GitLab, конфигурация `.gitlab-ci.yml`:

```yaml
stages:
  - test
  - report

test:
  stage: test
  image: maven:3.9.6-eclipse-temurin-17
  script:
    - mvn clean test
  artifacts:
    reports:
      junit: target/surefire-reports/*.xml

allure:
  stage: report
  image: maven:3.9.6-eclipse-temurin-17
  script:
    - mvn allure:report
  artifacts:
    paths:
      - allure-report/
```

## 📚 Дополнительные ресурсы

- [GitHub Actions Documentation](https://docs.github.com/en/actions)
- [Workflow Syntax Reference](https://docs.github.com/en/actions/using-workflows/workflow-syntax-for-github-actions)
- [GitHub Actions Marketplace](https://github.com/marketplace?type=actions)
- [Act - Local GitHub Actions](https://github.com/nektos/act)

