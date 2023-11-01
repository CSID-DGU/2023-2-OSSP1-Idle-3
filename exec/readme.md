# Almost There 포팅 매뉴얼

## 목차

- [Almost There 포팅 매뉴얼](#Almost There-포팅-매뉴얼)
  - [목차](#목차)
  - [프로젝트 기술 스택](#프로젝트-기술-스택)
  - [EC2 세팅](#ec2-세팅)
    - [Nginx \& Letsencrypt 설치](#nginx--letsencrypt-설치)
    - [MySQL](#mysql)
    - [Docker](#docker)
    - [Jenkins](#jenkins)
    - [Redis](#redis)
  - [CI/CD Pipeline](#cicd-pipeline)
    - [Backend](#backend)
    - [Frontend](#frontend)
  - [외부 서비스](#외부-서비스)
    - [Kakao 소셜 로그인](#kakao-소셜-로그인)
    - [Kakao API 설정](#kakao-api-설정)
    - [ODSay API 설정](#odsay-api-설정)
    - [Spring Redis 설정](#spring-redis-설정)

## 프로젝트 기술 스택

- [BE] Spring Boot 2.7.10
- [BE] JDK zulu 11.0.17
- [FE] Vue 2.6.14
- [FE] Vuetify 2.6.14
- [DB] MySQL 8.0.32
- [DB] Redis 7.0.11
- [Infra] Ubuntu 20.04
- [Infra] Docker 23.0.1
- [Infra] Jenkins 2.396
- [Infra] NginX 1.18.0

## EC2 세팅

### Nginx & Letsencrypt 설치

```bash
Nginx는 웹 서버 프로그램으로 HTTP로서의 역할 및 리버스 프록시/로드 밸런서 역할을 수행한다.
Letsencrypt는 Http → Https가 되기 위해서는 SSL 인증서가 필요한데 기본적으로 SSL은 매우 비싸다.
따라서 Letsencrypt라는 무료 SSL 인증서를 통하여 보안을 강화한다.

# Nginx 설치
sudo apt-get update
sudo apt-get install nginx
sudo nginx -v
sudo systemctl stop nginx

# SSL 인증
sudo apt-get install letsencrypt
sudo letsencrypt certonly --standalone -d [도메인] # 도메인에 알맞게 인증서를 발급
ls /etc/letsencrypt/live/[도메인]
sudo vim /etc/nginx/sites-available/[적절한파일명].conf # 파일 생성 후 nginx 설정을 채워준다.

sudo vim nginx.conf

# nginx.conf 작성
server {
location /{
    proxy_pass http://localhost:3000;
  }

  location /assets/ {
    proxy_pass http://localhost:3000/assets/;
  }

  location /ws {
            proxy_pass http://localhost:3000;
            proxy_http_version 1.1;
            proxy_set_header Upgrade $http_upgrade;
            proxy_set_header Connection "upgrade";
            proxy_set_header Host $host;
            proxy_set_header Origin "";
  }

  location /api/websocket {
            proxy_pass http://localhost:9999;
            proxy_http_version 1.1;
            proxy_set_header Upgrade $http_upgrade;
            proxy_set_header Connection "upgrade";
            proxy_set_header Host $host;
            proxy_set_header Origin "";
  }

  location /api {
    location /api/member {
      if ($request_method = 'OPTIONS') {
        add_header 'Access-Control-Allow-Origin' '$http_origin';
        add_header 'Access-Control-Allow-Credentials' 'true';
        add_header 'Access-Control-Allow-Methods' 'GET, POST, PUT, DELETE, PATCH, OPTIONS';
        add_header 'Access-Control-Allow-Headers' 'Content-Type, Access-Token';
        return 204;
      }
      proxy_hide_header 'Access-Control-Allow-Origin';
      add_header 'Access-Control-Allow-Origin' '$http_origin' always;
      add_header 'Access-Control-Allow-Credentials' 'true' always;
      add_header 'Access-Control-Expose-Headers' 'Set-Cookie';

      proxy_pass http://localhost:9999/api/member;
    }

    if ($request_method = 'OPTIONS') {
    add_header 'Access-Control-Allow-Origin' '*';
    add_header 'Access-Control-Allow-Methods' 'GET, POST, PUT, DELETE, PATCH, OPTIONS';
    add_header 'Access-Control-Allow-Headers' 'Content-Type, Access-Token';
    add_header 'Access-Control-Max-Age' 86400;
    return 204;
    }

    # 1. hide the Access-Control-Allow-Origin from the server response
    proxy_hide_header 'Access-Control-Allow-Origin';
    # 2. add a new custom header that allows all * origin instead
    add_header 'Access-Control-Allow-Origin' '*' always;

    proxy_pass http://localhost:9999/api;
  }

  location /jenkins {
    proxy_pass http://localhost:8000;
  }

  location /swagger-ui/ {
    proxy_pass http://localhost:9999/api/swagger-ui/index.html;
  }

  listen 443 ssl; # managed by Certbot
  ssl_certificate /etc/letsencrypt/live/k8a401.p.ssafy.io/fullchain.pem; # managed by Certbot
  ssl_certificate_key /etc/letsencrypt/live/k8a401.p.ssafy.io/privkey.pem; # managed by Certbot
  client_max_body_size 10M;
  # include /etc/letsencrypt/options-ssl-nginx.conf; # managed by Certbot
  # ssl_dhparam /etc/letsencrypt/ssl-dhparams.pem; # managed by Certbot
}

server {

  if ($host = k8a401.p.ssafy.io) {
    return 301 https://$host$request_uri;
  } # managed by Certbot

  listen 80;
  server_name k8a401.p.ssafy.io;
  return 404; # managed by Certbot
}

# nginx 설정을 링크하고 테스트한 다음 다시 실행하여 SSL 적용
sudo ln -s /etc/nginx/sites-available/[파일명].conf /etc/nginx/sites-enabled/[파일명].conf
sudo nginx -t
sudo nginx -s reload
```

### MySQL

- MySQL 서버 설치

```bash
sudo apt install mysql-server
```

- root 계정으로 접속

```bash
sudo mysql -u root
```

- Backend에서 사용할 계정 생성

```bash
use mysql;
CREATE USER '<ID>'@'%' identified by mysql_native_password '<PW>';
FLUSH PRIVILEGES;
```

- 데이터베이스 생성 후 권한 허용

```bash
create database <db명>;
GRANT ALL PRIVILEGES ON <db명>.* to '<ID>'@'%';
FLUSH PRIVILEGES;
```

- MySQL 설정 파일을 연다

```bash
sudo vim /etc/mysql/mysql.conf.d/mysqld.cnf
```

- 외부에서의 접속도 허용하기 위해 bind-address 값을 0.0.0.0으로 변경

```bash
bind-address = 0.0.0.0
```

- MySQL 재실행

```bash
sudo service mysql restart
```

### Docker

```bash
# Docker 클라이언트와 데이몬 설치
sudo apt-get remove docker docker-ce docker.io containerd runc

sudo apt-get update
sudo apt-get install \
  ca-certificates \
  curl \
  gnupg \
  lsb-release

sudo mkdir -m 0755 -p /etc/apt/keyrings
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo gpg --dearmor -o /etc/apt/keyrings/docker.gpg

echo \
  "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.gpg] https://download.docker.com/linux/ubuntu \
  $(lsb_release -cs) stable" | sudo tee /etc/apt/sources.list.d/docker.list > /dev/null

sudo apt-get update
sudo apt-get install docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin
```

### Jenkins

```bash
# Docker 이미지로 Jenkins 실행
docker run \
  --name jenkins-docker \
  -p 8000:8080 -p 50000:50000 \
  -v /home/jenkins:/var/jenkins_home \
  -v /var/run/docker.sock:/var/run/docker.sock \
  -v /usr/bin/docker:/usr/bin/docker \
  -u root \
  -d \
  jenkins/jenkins:lts

# jenkins 컨테이너 접속
docker exec -it [jenkins 컨테이너ID] bin/bash

# jenkins 컨테이너 log 확인
docker logs [jenkins 컨테이너ID]
```

### Redis

- Redis 이미지 받기

```bash
sudo docker pull redis:alpine
```

- Redis 네트워크 생성

```bash
sudo docker network create redis-network

#정보 확인
sudo docker inspect redis-network
```

- Redis 컨테이너 실행

```bash
# local-redis라는 이름으로 로컬-docker 간 6379 포트 개방
# redis-network 이름의 네트워크를 사용,
# 로컬의 redis_temp와 도커의 /data를 서로 연결
# redis:alpine 이미지를 사용하여 백그라운드에서 실행
sudo docker run --name local-redis --network redis-network -p 6379:6379 -v redis_temp:/data -d redis:alpine redis-server --appendonly yes --requirepass "<PW>"
```

## CI/CD Pipeline

### Backend

```bash
pipeline{
    agent any
    environment{
        PROFILE=""
        DATABASE_URL=""
        DATABASE_ID=""
        DATABASE_PASSWORD=""
        LOGIN_SUCCESS_URL=""
        JWT_TOKEN_SECRET_KEY=""
        KAKAO_CLIENT_ID=""
        KAKAO_REDIRECT_URL=""
        KAKAO_CLIENT_SECRET=""
        REDIS_URL=""
        REDIS_PASSWORD=""
        CLOVA_APIURL=""
        CLOVA_SECRETKEY=""
    }
    stages{
        stage('Ready'){
            steps{
                sh "echo 'Ready'"
                git branch: 'develop',
                credentialsId: 'asng',
                url: 'https://lab.ssafy.com/s08-final/S08P31A401.git'

            }
        }
        stage('Backend Build'){
            steps{
                withCredentials([string(credentialsId: 'PROFILE', variable: 'PROFILE'),
                                 string(credentialsId: 'DATABASE_URL', variable: 'DATABASE_URL'),
                                 string(credentialsId: 'DATABASE_ID', variable: 'DATABASE_ID'),
                                 string(credentialsId: 'DATABASE_PASSWORD', variable: 'DATABASE_PASSWORD'),
                                 string(credentialsId: 'LOGIN_SUCCESS_URL', variable: 'LOGIN_SUCCESS_URL'),
                                 string(credentialsId: 'JWT_TOKEN_SECRET_KEY', variable: 'JWT_TOKEN_SECRET_KEY'),
                                 string(credentialsId: 'KAKAO_CLIENT_ID', variable: 'KAKAO_CLIENT_ID'),
                                 string(credentialsId: 'KAKAO_REDIRECT_URL', variable: 'KAKAO_REDIRECT_URL'),
                                 string(credentialsId: 'KAKAO_CLIENT_SECRET', variable: 'KAKAO_CLIENT_SECRET'),
                                 string(credentialsId: 'REDIS_URL', variable: 'REDIS_URL'),
                                 string(credentialsId: 'REDIS_PASSWORD', variable: 'REDIS_PASSWORD'),
                                 string(credentialsId: 'CLOVA_APIURL', variable: 'CLOVA_APIURL'),
                                 string(credentialsId: 'CLOVA_SECRETKEY', variable: 'CLOVA_SECRETKEY')]) {
                    withEnv(["PROFILE=${PROFILE}", "DATABASE_URL=${DATABASE_URL}","DATABASE_ID=${DATABASE_ID}"
                    ,"DATABASE_PASSWORD=${DATABASE_PASSWORD}","LOGIN_SUCCESS_URL=${LOGIN_SUCCESS_URL}"
                    ,"JWT_TOKEN_SECRET_KEY=${JWT_TOKEN_SECRET_KEY}","KAKAO_CLIENT_ID=${KAKAO_CLIENT_ID}","KAKAO_REDIRECT_URL=${KAKAO_REDIRECT_URL}"
                    ,"KAKAO_CLIENT_SECRET=${KAKAO_CLIENT_SECRET}", "REDIS_URL=${REDIS_URL}", "REDIS_PASSWORD=${REDIS_PASSWORD}"
                    , "CLOVA_APIURL=${CLOVA_APIURL}", "CLOVA_SECRETKEY=${CLOVA_SECRETKEY}"]) {
                        sh "echo 'Test'"
                        dir('backend') {
                            sh 'chmod +x gradlew'
                            sh './gradlew clean build'
                        }
                    }
                }
            }
        }

        stage('Containers Remove'){
            steps{
                sh "sudo docker rm -f almostthere-backend"
            }
        }

        stage('Images Remove'){
            steps{
                sh "sudo docker rmi almostthere-backend"
            }
        }

        stage('Deploy'){
            steps{
                withCredentials([string(credentialsId: 'PROFILE', variable: 'PROFILE'),
                                 string(credentialsId: 'DATABASE_URL', variable: 'DATABASE_URL'),
                                 string(credentialsId: 'DATABASE_ID', variable: 'DATABASE_ID'),
                                 string(credentialsId: 'DATABASE_PASSWORD', variable: 'DATABASE_PASSWORD'),
                                 string(credentialsId: 'LOGIN_SUCCESS_URL', variable: 'LOGIN_SUCCESS_URL'),
                                 string(credentialsId: 'JWT_TOKEN_SECRET_KEY', variable: 'JWT_TOKEN_SECRET_KEY'),
                                 string(credentialsId: 'KAKAO_CLIENT_ID', variable: 'KAKAO_CLIENT_ID'),
                                 string(credentialsId: 'KAKAO_REDIRECT_URL', variable: 'KAKAO_REDIRECT_URL'),
                                 string(credentialsId: 'KAKAO_CLIENT_SECRET', variable: 'KAKAO_CLIENT_SECRET'),
                                 string(credentialsId: 'REDIS_URL', variable: 'REDIS_URL'),
                                 string(credentialsId: 'REDIS_PASSWORD', variable: 'REDIS_PASSWORD'),
                                 string(credentialsId: 'CLOVA_APIURL', variable: 'CLOVA_APIURL'),
                                 string(credentialsId: 'CLOVA_SECRETKEY', variable: 'CLOVA_SECRETKEY')]) {
                    dir('backend'){
                        sh "sudo docker build -t almostthere-backend ./"
                        sh "sudo docker run -d --name almostthere-backend -e PROFILE=${PROFILE} 
                        -e DATABASE_URL=${DATABASE_URL} -e DATABASE_ID=${DATABASE_ID} -e DATABASE_PASSWORD=${DATABASE_PASSWORD} 
                        -e LOGIN_SUCCESS_URL=${LOGIN_SUCCESS_URL} -e JWT_TOKEN_SECRET_KEY=${JWT_TOKEN_SECRET_KEY} 
                        -e KAKAO_CLIENT_ID=${KAKAO_CLIENT_ID} -e KAKAO_REDIRECT_URL=${KAKAO_REDIRECT_URL} -e KAKAO_CLIENT_SECRET=${KAKAO_CLIENT_SECRET} 
                        -e REDIS_URL=${REDIS_URL} -e REDIS_PASSWORD=${REDIS_PASSWORD} 
                        -e CLOVA_APIURL=${CLOVA_APIURL} -e CLOVA_SECRETKEY=${CLOVA_SECRETKEY} -v /home/ubuntu/almostthere_store:/var/lib/almostthere -p 9999:9999 almostthere-backend"
                    }
                }
            }
        }

        stage('Connect to Redis Network') {
            steps {
                script {
                def containerId = sh(returnStdout: true, script: "sudo docker ps --filter 'name=almostthere-backend' --format '{{.ID}}'").trim()
                sh "sudo docker network connect redis-network ${containerId}"
                }
            }
        }


    }
}
```

### Frontend

```bash
pipeline{
    agent any
    environment{
        VUE_APP_API_BASE_URL=""
        VUE_APP_KAKAO_API_KEY=""
        VUE_APP_KAKAO_LOGIN_URL=""
        VUE_APP_ODSAY_KEY=""
        VUE_APP_KAKAO_CAR_WAY_KEY=""
    }

    stages{
        stage('Ready'){
            steps{
                sh "echo 'Ready'"
                git branch: 'develop',
                credentialsId: 'asng',
                url: 'https://lab.ssafy.com/s08-final/S08P31A401.git'

            }
        }

        stage('Containers Remove'){
            steps{
                sh "sudo docker rm -f almostthere-frontend"
            }
            }
        stage('Images Remove'){
            steps{
                sh "sudo docker rmi almostthere-frontend"
            }
        }

        stage('Deploy'){
            steps{
                withCredentials([string(credentialsId: 'VUE_APP_API_BASE_URL', variable: 'VUE_APP_API_BASE_URL'),
                                 string(credentialsId: 'VUE_APP_KAKAO_API_KEY', variable: 'VUE_APP_KAKAO_API_KEY'),
                                 string(credentialsId: 'VUE_APP_ODSAY_KEY', variable: 'VUE_APP_ODSAY_KEY'),
                                 string(credentialsId: 'VUE_APP_KAKAO_CAR_WAY_KEY', variable: 'VUE_APP_KAKAO_CAR_WAY_KEY'),
                                 string(credentialsId: 'VUE_APP_KAKAO_LOGIN_URL', variable: 'VUE_APP_KAKAO_LOGIN_URL')]) {
                    dir('frontend'){
                        sh "sudo docker build -t almostthere-frontend ./"
                        sh "sudo docker run -d --name almostthere-frontend -e VUE_APP_ODSAY_KEY=${VUE_APP_ODSAY_KEY} -e VUE_APP_KAKAO_CAR_WAY_KEY=${VUE_APP_KAKAO_CAR_WAY_KEY} -e VUE_APP_API_BASE_URL=${VUE_APP_API_BASE_URL} -e VUE_APP_KAKAO_API_KEY=${VUE_APP_KAKAO_API_KEY} -e VUE_APP_KAKAO_LOGIN_URL=${VUE_APP_KAKAO_LOGIN_URL} -v /home/ubuntu/almostthere_store:/app/public/almostthere -p 3000:3000 almostthere-frontend"
                    }
                }
            }
        }
    }

}
```

## 외부 서비스

### Kakao 소셜 로그인

1. [https://developers.kakao.com/](https://developers.kakao.com/) 접속
2. 애플리케이션 추가 후
3. CLIENT_ID, CLIENT_SECRET, REDIRECT_URL, 설정 및 동의항목 추가(profile_nickname, account_email ,profile_image)
4. application.yml 설정

```bash
spring:
	security:
    oauth2:
      client:
        registration:
          kakao:
            client-id: ${KAKAO_CLIENT_ID} //REST API 키
            redirect-uri: ${KAKAO_REDIRECT_URL} # 서버 주소/api/login/oauth2/code/kakao
            client-authentication-method: POST
            client-secret: ${KAKAO_CLIENT_SECRET}
            authorization-grant-type: authorization_code
            scope:
              - profile_nickname
              - account_email
              - profile_image
            client_name: kakao
        provider:
          kakao:
            authorization-uri: https://kauth.kakao.com/oauth/authorize
            token-uri: https://kauth.kakao.com/oauth/token
            user-info-uri: https://kapi.kakao.com/v2/user/me
            user-name-attribute: id


LOGIN_SUCCESS_URL: ${LOGIN_SUCCESS_URL} # (http://프론트주소/?login= #로그인 성공시 URL 추가)

# jwt token secret-key 설정
jwt:
  token:
    secret-key: ${JWT_TOKEN_SECRET_KEY}
```

5. Front env 설정

```bash
VUE_APP_KAKAO_LOGIN_URL=$VUE_APP_KAKAO_LOGIN_URL //(http://백엔드주소/api/oauth2/authorization/kakao)
```

### Kakao API 설정

- 위에서 설정한 API 키와 허용 IP 주소 설정

#### Backend

- 영수증 계산 시 /api/meeting-calculate/receipt Key 추가

```bash
String secretKey = "네이버 API KEY";
```

#### Frontend

```bash
VUE_APP_API_BASE_URL="http://백엔드주소/api"
VUE_APP_KAKAO_API_KEY=$CLIENT_ID # JavaScript 키
VUE_APP_KAKAO_CAR_WAY_KEY = $CLIENT_ID # REST API 키
```

### ODSay API 설정

#### Front

[https://lab.odsay.com/](https://lab.odsay.com/) 접속 후 어플리케이션 등록 및 플랫폼 환경 URI 설정

```bash
VUE_APP_ODSAY_KEY = $VUE_APP_ODSAY_KEY # API 키
```

### Spring Redis 설정

- redis 설치 후

```bash
redis-cli 실행 후
-a ${REDIS_PASSWORD} # 원하는 패스워드 설정
```

- Spring redis 설정

```bash
spring:
	redis:
    host: ${REDIS_URL} # 서버 host
    port: 6379 # redis 서버 포트
    password: ${REDIS_PASSWORD}
```
