
# 구현 사항

## OAuth2.0 Settings

- 네이버 개발자센터 -> 애플리케이션 등록 -> 사용 API(네이버 로그인)
- 카카오 개발자센터 -> 애플리케이션 등록 -> 제품 설정 -> 카카오 로그인

## thymeleaf & pages

- (js) 로그인 이후 요청 시 HTTP 헤더 포함하도록 처리
- todo page

## Refactor Todo

- Member ID 혹은 member email 요청에서 받지 말고 HTTP Request Header로 token 받아서 처리하기
- 각 멤버마다 Todo URI 다르게 수정
- 