# Footprinter

![Test](https://github.com/swsnu/swppfall2022-team5/actions/workflows/test.yaml/badge.svg)
![Frontend Build](https://github.com/swsnu/swppfall2022-team5/actions/workflows/frontend-build.yaml/badge.svg)
![Backend Lint](https://github.com/swsnu/swppfall2022-team5/actions/workflows/backend-lint.yaml/badge.svg)

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=swsnu_swppfall2022-team5&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=swsnu_swppfall2022-team5)

[![Coverage Status](https://coveralls.io/repos/github/swsnu/swppfall2022-team5/badge.svg?branch=main)](https://coveralls.io/github/swsnu/swppfall2022-team5?branch=main)

## Frontend
### Run
```bash
cd frontend
yarn install
yarn start
```

### Test
```bash
cd frontend
yarn test:unit --coverage
```

## Backend
### Run
```bash
cd backend
./gradlew bootRun
```

### Test
```bash
cd backend
./gradlew test jacocoTestReport
```
