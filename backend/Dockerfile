# Docker의 이미지를 가져옴
FROM openjdk:11-jdk

# 빌드 변수 설정
ARG JAR_FILE=build/libs/almostThere-0.0.1-SNAPSHOT.jar

# 파일 복사
COPY ${JAR_FILE} app.jar

# 프로젝트 파일 실행
ENTRYPOINT ["java", "-Duser.timezone=Asia/Seoul", "-jar", "/app.jar"]
