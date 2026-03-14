start:
	./gradlew bootRun

build:
	./gradlew build

test:
	./gradlew test

clean:
	./gradlew clean

migrate:
	./gradlew flywayMigrate

docker-up:
	docker compose up --build -d

docker-down:
	docker compose down

docker-logs:
	docker compose logs -f app
