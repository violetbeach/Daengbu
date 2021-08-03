# 댕부 _(Daengbu)_

<br/>

<img src ="https://user-images.githubusercontent.com/63458653/127138647-f3a56cfe-618f-462a-97a1-822a07ecc15e.png" width="400px"/>

<br/>

**댕부**는 댕댕이를 부탁해 라는 뜻으로 **강아지 입 · 분양 커뮤니티** 입니다.
무료로 사용자들이 강아지를 입양, 분양 할 수 있습니다.

<br/>



> Main : <https://daengbu.tech>
>
> API : <https://daengbu.tech/swagger-ui.html>

<br/>

## 🚩 목차

- [Why 댕부 ?](#-why-댕부-)
- [제작 기간 & 참여 인원](#-제작-기간--참여-인원)
- [브라우저 지원](#-브라우저-지원)
- [사용 기술](#-사용-기술)
- [ERD 설계](#-ERD-설계)
- [기능](#-기능)
- [License](#-license)

<br/>

## 🔧 Why 댕부 ?

<br/>

<img src ="https://user-images.githubusercontent.com/63458653/127138690-8e7f68ff-956b-4ba3-9159-aa235fc7c12d.png">

<br/>

댕부는 **사용자가 편하게 강아지를 입양, 분양할 수 있는 것**을 목적으로 합니다. 광고나 팝업, 복잡한 기능이 없어서 **쉽고**, 검색기능이 있어 **편리**합니다. 댕부는 강아지 공장에서 키워진 강아지를 파는 사이트가 아닌, 사정이 있어 키우지 못하는 강아지를 **채팅**을 통해  **분양하고 입양할 수 있는 공간**이 되어줄 것입니다.

<br/>

## 👷 제작 기간 & 참여 인원

- 2021년 2월 17일 ~ 2021년 7월 27일
- 개인 프로젝트

<br/>

## 🌏 브라우저 지원

| <img src="https://user-images.githubusercontent.com/1215767/34348387-a2e64588-ea4d-11e7-8267-a43365103afe.png" alt="Chrome" width="16px" height="16px" /> Chrome | <img src="https://user-images.githubusercontent.com/1215767/34348590-250b3ca2-ea4f-11e7-9efb-da953359321f.png" alt="IE" width="16px" height="16px" /> Internet Explorer | <img src="https://user-images.githubusercontent.com/1215767/34348380-93e77ae8-ea4d-11e7-8696-9a989ddbbbf5.png" alt="Edge" width="16px" height="16px" /> Edge | <img src="https://user-images.githubusercontent.com/1215767/34348394-a981f892-ea4d-11e7-9156-d128d58386b9.png" alt="Safari" width="16px" height="16px" /> Safari | <img src="https://user-images.githubusercontent.com/1215767/34348383-9e7ed492-ea4d-11e7-910c-03b39d52f496.png" alt="Firefox" width="16px" height="16px" /> Firefox |
| :----------------------------------------------------------: | :----------------------------------------------------------: | :----------------------------------------------------------: | :----------------------------------------------------------: | :----------------------------------------------------------: |
|                             Yes                              |                             11+                              |                             Yes                              |                             Yes                              |                             Yes                              |

<br/>

## 🎨 사용 기술

`Front-end`

- Thymeleaf + Thymeleaf-layout-dialect
- jQuery 3.5.1
- BootStrap 4.0.0
- Popper 1.14.3

`Back-end`

- Java 8
- SpringBoot 2.4.2
  - Spring Security + JWT
  - Spring Mail
  - Hibernate Validator
  - Dev-tools
- MySQL 8.0.24
- Mybatis
- HikariCP
- Swagger 2
- Lucy-XSS-Servlet-Filter
- Lombok

`Deploy`

- AWS EC2 (Ubuntu 18.04 LTS)
- AWS RDS
- AWS Route53
- AWS Certification Manager
- AWS ELB (ALB)

`etc.`

- Teachable Machine 2.0
- Tensorflow.js 1.3.1

`Environment`

- Eclipse 4.19.0
- Apache Maven
- SourceTree 3.4.4

<br/>

## 🧾 ERD 설계

![erd2](https://user-images.githubusercontent.com/63458653/127174920-315f87c6-b9cd-43aa-b2bc-92508e4c2689.png)

<br/>

## 📡 기능

`User`

- 로그인 (Stateless. JWT in httponly cookie)
- 로그아웃
- 정보수정
- 회원가입
- 아이디 / 비밀번호 찾기
- 이메일 인증 (Java mail sender)
- 카카오 로그인 (OAuth 2.0)

`Article`

- 게시글쓰기 (+ 이미지 업로드)
- 게시글  리스트 조회
- 게시글 검색
- 게시글 상세 조회
- 게시글 수정
- 게시글 삭제
- 찜 하기, 찜 해제
- 찜 리스트 조회
- 내 글 리스트 조회

`Chat`

- 채팅방 생성
- 채팅방 리스트 조회
- 채팅방 나가기
- 채팅방 조회
- 메시지 전송

`Security`

- Stateless. JWT in httponly cookie
- 비밀번호 해시, 솔트 (BCrypt Password Encoder)
- XSS 방어 로직 (Lucy-XSS-Servlet-Filter)
- CSRF 공격 방어 로직 (Custom Referer Filter)
- SQL Injection 방지 (Prepared Statement)
- TLS 1.2 적용 (AWS Certificate Manager with ELB)
- 데이터 유효성 검사 (Hibernate Validator)
- 클라이언트 IP 기록

`etc.`

- AI 강아지 이름 추천

<br/>

## 📜 License

This software is licensed under the [MIT](https://github.com/VioletBeach/Daengbu/blob/master/LICENSE) © [VioletBeach](https://github.com/VioletBeach).