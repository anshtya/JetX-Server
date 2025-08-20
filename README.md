## Build the Application & Docker Image
```
./gradlew buildImage
docker load -i build/jib-image.tar                
docker compose up -d
```