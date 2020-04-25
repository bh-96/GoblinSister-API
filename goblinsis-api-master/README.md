## 도깨비언니 API

&nbsp;

&nbsp;

### - 서버, 디비 정보 변경 필요 

&nbsp;

&nbsp;

### - Host

- 운영서버 : 27.96.130.221:8101

### - Request / Response Type

- Content-Type=application/json

&nbsp;

&nbsp;

### 1. 일반 사용자

&nbsp;

#### 1-1. 회원가입

```
[Request]
- path : {host}/api/users/create
- method : POST
- example
{
	"account":"bh",	
	"password":"kim4289*",
	"name":"김보현", 
	"birth":"1996-04-12",
	"phone":"010-8833-6656",
	"address":"청학",
	"email":"96bohyun@naver.com",
	"gender":"F"
}
- explain
	account : string / 아이디
	password : string / 비밀번호
	name : string / 이름
	birth : string("yyyy-MM-dd") / 생일
	phone : string / 연락처
	address : string / 주소
	email : string / 이메일
	gender : string / 성별 (F : 여성, M : 남성)

[Response]
(성공)
{
    "id": 10,	
    "account": "bh",
    "password": "$2a$10$AO9hf5UJfcajSogI/Ad9E.xU3tBHmoiOX7P8Usb7Q9lNnBE6zH4h.",
    "name": "김보현",
    "birth": "1996-04-11T15:00:00.000+0000",
    "phone": "010-8833-6656",
    "address": "청학",
    "email": "96bohyun@naver.com",
    "gender": "F",
    "role": {
        "id": 11,
        "rolename": "ROLE_USER"
    }
}
- explain
	id : int / userId
	account : string / 아이디
	password : string / 비밀번호
	name : string / 이름
	birth : Date / 생일
	phone : string / 연락처
	address : string / 주소
	email : string / 이메일
	gender : string / 성별 (F : 여성, M : 남성)
	role {
		id : int / roleId
		rolename : string / 권한
	}

(실패)
- HttpStatus : 400
- Body
{
    "result": false,
    "e_message": "필수 값이 비어있습니다."
}
- explain
	result : boolean / 결과
	e_message : string / 에러메시지
```

&nbsp;

#### 1-2. 로그인

```
[Request]
- path : {host}/api/login
- method : POST
- example
{
	"account":"bh",
	"password":"kim4289*"
}
- explain
	account : string / 아이디
	password : string / 비밀번호

[Response]
(성공)
- HttpStatus : 200
- Headers
key : auth-role
value : ROLE_USER

key : auth-token
value : eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJiaCIsImV4cCI6MTU3ODM3MTAwM30.MJL5XRxaLiC-gJLSKw2LAF1AhUkF8Zl78ZMyD_S6X7itQl0t2j8tXA9IXtdXLNV6hu_ul4A6lesyT30v7WVrEg

(실패)
- HttpStatus : 401
- Body
{
    "timestamp": "2019-12-31T04:27:54.581+0000",
    "status": 401,
    "error": "Unauthorized",
    "message": "Unauthorized",
    "path": "/api/login"
}
```

&nbsp;

#### 1-3. 마이페이지

```
1. 아이디 중복 체크
[Request]
- path : {host}/api/users/check?account={account}
- method : GET
- example
localhost:8101/api/users/check?account=bh

[Response]
(성공 - 중복)
- HttpStatus : 200
- body
true		// boolean

(실패 또는 중복 아님)
- HttpStatus : 200
- body
false		// boolean


2. 비밀번호 확인
[Request]
- path : {host}/api/users/check/pw?password={password}
- method : GET
- Headers
		auth-token={auth-token}		// 로그인 후 얻은 token 
- example
localhost:8101/api/users/check/pw?password=kim4289*

[Response]
(성공 - 일치)
- HttpStatus : 200
- body
true		// boolean

(실패 또는 불일치)
- HttpStatus : 200
- body
false		// boolean


3. 비밀번호 변경
[Request]
- path : {host}/api/users/pw
- method : PUT
- Headers
		auth-token={auth-token}		// 로그인 후 얻은 token 
- example
{
	"password":"kim4289*",
	"newPassword":"bhbhbh1234*"
}
- explain
	password : string / 기존 비밀번호
	newPassword : string / 변경할 비밀번호 

[Response]
(성공 - 변경)
- HttpStatus : 200
- body
true		// boolean

(실패 또는 기존 비밀번호 불일치 또는 변경할 비밀번호 형식 오류)
- HttpStatus : 200
- body
false		// boolean


4. 회원정보 수정 (아이디, 비밀번호 제외)
[Request]
- path : {host}/api/users
- method : PUT
- Headers
		auth-token={auth-token}		// 로그인 후 얻은 token 
- example
{
	"name":"보현이"
}
- explain
	name, birth, phone, address, email, gender -> string

[Response]
(성공)
- HttpStatus : 200
- body
{
    "id": 10,
    "account": "bh",
    "password": "$2a$10$.CnrigM4J4tsq4inOLHH3Oqxs2KakJQdIKEBXauQZ0DgRDuiyFF3C",
    "name": "보현이",
    "birth": "1996-04-11T15:00:00.000+0000",
    "phone": "01088336656",
    "address": "청학",
    "email": "96bohyun@naver.com",
    "gender": "F",
    "role": {
        "id": 11,
        "rolename": "ROLE_USER"
    }
}
- explain
	id : int / userId
	account : string / 아이디
	password : string / 비밀번호
	name : string / 이름
	birth : Date / 생일
	phone : string / 연락처
	address : string / 주소
	email : string / 이메일
	gender : string / 성별 (F : 여성, M : 남성)
	role {
		id : int / roleId
		rolename : string / 권한
	}


(실패)
- HttpStatus : 400
- body
{
    "result": false,	// boolean
    "e_message": "회원정보 수정 실패"
}


5. 사용자 아이디 가져오기
[Request]
- path : {host}/api/users/account?id={id}
- method : GET
- example
localhost:8101/api/users/account?id=7

[Response]
(성공)
- HttpStatus : 200
- body
bh

(실패)
- HttpStatus : 400
- body
{
    "result": false,	// boolean
    "e_message": "해당 인덱스의 사용자가 존재하지 않습니다."
}


6. 회원탈퇴
[Request]
- path : {host}/api/users?password={password}
- method : DELETE
- Headers
		auth-token={auth-token}		// 로그인 후 얻은 token 
- example
localhost:8101/api/users?password=testtest1!

[Response]
(성공)
- HttpStatus : 200
- body
true		// boolean

(실패 또는 비밀번호 불일치)
- HttpStatus : 200
- body
false		// boolean
```

&nbsp;

#### 1-4. 게시글

```
1. 게시글 목록
[Request]
- path : {host}/api/post?page={page}&size={size} 
- method : GET
- example
localhost:8101/api/post?page=0&size=10	// page, size 생략 시 default page=0, size=20

[Response]
(성공)
- HttpStatus : 200
- body
{
    "content": [
        {
            "id": 2,
            "subject": "싸다 싸!",
            "productName": "롱패딩~",
            "price": 40000,
            "content": "<p>ㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋ</p><p><br></p><p><img src=\"/api/file/4\" style=\"width: 225px;\"></p><p><br></p><p>ㅎㅎㅎㅎㅎㅎㅎ</p><p><img src=\"/api/file/5\" style=\"width: 192px;\"><br></p>",
            "regDate": "2019-12-21T15:00:00.000+0000",
            "modDate": "2019-12-30T15:00:00.000+0000",
            "category": 0,
            "stock": 5
        }
    ],
    "pageable": {
        "sort": {
            "sorted": false,
            "unsorted": true,
            "empty": true
        },
        "offset": 0,
        "pageNumber": 0,	
        "pageSize": 10,		
        "unpaged": false,
        "paged": true
    },
    "totalElements": 1,
    "last": true,
    "totalPages": 1,
    "number": 0,
    "size": 10,
    "sort": {
        "sorted": false,
        "unsorted": true,
        "empty": true
    },
    "numberOfElements": 1,
    "first": true,
    "empty": false
}
- explain
	content : string / JSONArray
		id : int / postId
		subject : string / 게시글 제목
		productName : string / 제품명
		price : int / 가격
		content : string / 게시글 내용
		regDate : Date / 게시글 등록일자
		modDate : Date / 게시글 수정일자
		category : int / 게시글 분류
		stock : int / 재고량
	pageable {
        pageNumber : int / 현재 페이지 넘버 (0부터)
        pageSize : int / 한 페이지당 목록 개수 
    }
	
(실패)
- HttpStatus : 400
- body
{
    "result": false,	// boolean
    "e_message": "게시글을 읽어오지 못했습니다."
}


2. 게시글 확인
[Request]
- path : {host}/api/post/{id}
- method : GET
- example
localhost:8101/api/post/2

[Response]
(성공)
- HttpStatus : 200
- body
{
    "id": 2,	// int
    "subject": "싸다 싸!",	// string 게시글 제목
    "productName": "롱패딩~",	// string 제품명
    "price": 40000,	// int 가격
    "content": "<p>ㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋ</p><p><br></p><p><img src=\"/api/file/4\" style=\"width: 225px;\"></p><p><br></p><p>ㅎㅎㅎㅎㅎㅎㅎ</p><p><img src=\"/api/file/5\" style=\"width: 192px;\"><br></p>",	// string 게시글 내용
    "regDate": "2019-12-21T15:00:00.000+0000",	// Date 등록일자
    "modDate": "2019-12-30T15:00:00.000+0000",	// Date 수정일자
    "category": 0,	// int 게시글 분류
    "stock": 5	// int 재고량
}
- explain
	id : int / postId
	subject : string / 게시글 제목
	productName : string / 제품명
	price : int / 가격
	content : string / 게시글 내용
	regDate : Date / 게시글 등록일자
	modDate : Date / 게시글 수정일자
	category : int / 게시글 분류
	stock : int / 재고량

(실패)
- HttpStatus : 400
- body
{
    "result": false,	// boolean
    "e_message": "postId가 유효하지 않습니다."
}
```

&nbsp;

#### 1-5. 구매

```
1. 구매
[Request]
- path : {host}/api/purchase
- method : POST
- Headers
		auth-token={auth-token}		// 로그인 후 얻은 token 
- example
{
	"productName":"롱패딩",
	"price":40000,
	"amount":2,	
	"payer":"김보현",
	"address":"경기도 남양주시 별내면 화접리",
	"phone":"010-8833-6656",
	"postId":2
}
- explain
	productName : string / 제품명
	price : int / 가격
	amount : int / 구매 수량
	payer : string / 입금자명
	address : string / 배송지
	phone : string / 구매자 연락처
	postId : int / postId

[Response]
(성공)
- HttpStatus : 200
- body
{
    "id": 2,
    "productName": "롱패딩",
    "price": 40000,
    "amount": 2,
    "total": 80000,	
    "payer": "김보현",
    "address": "경기도 남양주시 별내면 화접리",
    "phone": "01088336656",
    "status": 1,
    "regDate": "2020-01-01T11:25:49.592+0000",	
    "modDate": null,
    "userId": 10,
    "postId": 2
}
- explain
	id : int / purchaseId
	productName : string / 제품명
	price : int / 가격
	amount : int / 구매 수량
	total : int / 입금해야 할 가격
	payer : string / 입금자명
	address : string / 배송지
	phone : string / 구매자 연락처
	status : int / 배송상태 
	(1 -> 주문완료, 2 -> 주문취소, 3 -> 입금확인, 4 -> 배송중, 5 -> 배송완료, 6 -> 환불신청)
	regDate : Date / 구매일자
	modDate : Date / 상태변경 일자
	userId : int / userId
	postId : int / postId

(실패)
- HttpStatus : 400
- body
{
    "result": false,	// boolean
    "e_message": "구매 실패"
}


2. 구매내역 조회
[Request]
- path : {host}/api/purchase?page={page}&size={size}
- method : GET
- Headers
		auth-token={auth-token}		// 로그인 후 얻은 token 
- example
localhost:8101/api/purchase?page=0&size=20	// page, size 생략 시 default page=0, size=20

[Response]
(성공)
- HttpStatus : 200
- body
{
    "content": [
        {
            "id": 1,
            "productName": "롱패딩",
            "price": 40000,
            "amount": 2,
            "total": 80000,
            "payer": "김보현",
            "address": "경기도 남양주시 별내면 화접리",
            "phone": "01088336656",
            "status": 1,
            "regDate": "2019-12-31T15:00:00.000+0000",
            "modDate": null,
            "userId": 10,
            "postId": 2
        },
        {
            "id": 2,
            "productName": "롱패딩",
            "price": 40000,
            "amount": 1,
            "total": 40000,
            "payer": "김보현",
            "address": "경기도 남양주시 별내면 화접리",
            "phone": "01088336656",
            "status": 1,
            "regDate": "2019-12-31T15:00:00.000+0000",
            "modDate": null,
            "userId": 10,
            "postId": 2
        }
    ],
    "pageable": {
        "sort": {
            "sorted": false,
            "unsorted": true,
            "empty": true
        },
        "offset": 0,
        "pageNumber": 0,
        "pageSize": 20,
        "paged": true,
        "unpaged": false
    },
    "totalElements": 2,
    "totalPages": 1,
    "last": true,
    "number": 0,
    "size": 20,
    "first": true,
    "sort": {
        "sorted": false,
        "unsorted": true,
        "empty": true
    },
    "numberOfElements": 2,
    "empty": false
}
- explain
	content : string / JSONArray
		id : int / purchaseId
		productName : string / 제품명
		price : int / 가격
		amount : int / 구매 수량
		total : int / 입금해야 할 가격
		payer : string / 입금자명
		address : string / 배송지
		phone : string / 구매자 연락처
		status : int / 배송상태 
		regDate : Date / 구매일자
		modDate : Date / 상태변경 일자
		userId : int / userId
		postId : int / postId
	pageable {
        pageNumber : int / 현재 페이지 넘버 (0부터)
        pageSize : int / 한 페이지당 목록 개수 
    }

(실패)
- HttpStatus : 400
- body
{
    "result": false,	// boolean
    "e_message": "구매 내역 조회 실패"
}


3. 구매취소
[Request]
- path : {host}/api/purchase?id={id}
- method : DELETE
- Headers
		auth-token={auth-token}		// 로그인 후 얻은 token 
- example
localhost:8101/api/purchase?id=1	// id : purchaseId

[Response]
(성공)
- HttpStatus : 200
- body
true	// boolean

(실패)
- HttpStatus : 200
- body
false	// boolean
```

&nbsp;

#### 1-6. 댓글

```
1. 댓글
[Request]
- path : {host}/api/comment
- method : POST
- Headers
		auth-token={auth-token}		// 로그인 후 얻은 token 
- example
{
	"comment":"싸다~~~",	
	"postId":2
}
- explain
	comment : string / 댓글 내용
	postId : int / postId

[Response]
(성공)
- HttpStatus : 200
- body
{
    "id": 6,
    "comment": "싸다~~~",
    "regDate": "2020-01-01T11:54:54.483+0000",
    "modDate": null,
    "reComment": null,
    "post": {
        "id": 2,
        "subject": "싸다 싸!",
        "productName": "롱패딩",
        "price": 40000,
        "content": "<p>ㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋ</p><p><br></p><p><img src=\"/api/file/4\" style=\"width: 225px;\"></p><p><br></p><p>ㅎㅎㅎㅎㅎㅎㅎ</p><p><img src=\"/api/file/5\" style=\"width: 192px;\"><br></p>",
        "regDate": "2019-12-21T15:00:00.000+0000",
        "modDate": "2019-12-30T15:00:00.000+0000",
        "category": 0,
        "stock": 2
    },
    "user": {
        "id": 10,
        "account": "bh",
        "password": "$2a$10$aeh55EpgpIPlD8kD4VcngezwLjF.C3f0QcfbSkLMe6su4zjmdKmje",
        "name": "김보현",
        "birth": "1996-04-11T15:00:00.000+0000",
        "phone": "01088336656",
        "address": "청학",
        "email": "96bohyun@naver.com",
        "gender": "F",
        "role": {
            "id": 11,
            "rolename": "ROLE_USER"
        }
    }
}
- explain
	id : int / commentId
	comment : string / 댓글 내용
	regDate : Date / 등록일자
	modDate : Date / 수정일자
	reComment : string / 대댓글 (관리자)
	post {
		id : int / postId
		subject : string / 게시글 제목
		productName : string / 제품명
		price : int / 가격
		content : string / 게시글 내용
		regDate : Date / 게시글 등록일자
		modDate : Date / 게시글 수정일자
		category : int / 게시글 분류
		stock : int / 재고량
    }
	user {
		id : int / userId
		account : string / 아이디
		password : string / 비밀번호
		name : string / 이름
		birth : Date / 생일
		phone : string / 연락처
		address : string / 주소
		email : string / 이메일
		gender : string / 성별 (F : 여성, M : 남성)
		role {
			id : int / roleId
			rolename : string / 권한
		}  
    }

(실패)
- HttpStatus : 400
- body
{
    "result": false,	// boolean
    "e_message": "댓글 달기 실패"
}


2. 댓글 수정
[Request]
- path : {host}/api/comment
- method : PUT
- Headers
		auth-token={auth-token}		// 로그인 후 얻은 token 
- example
{
	"id":5,	
	"comment":"가성비 굳"
}
- explain
	id : int / commentId
	comment : string / 수정할 댓글 내용

[Response]
(성공)
- HttpStatus : 200
- body
{
    "id": 5,
    "comment": "가성비 굳",
    "regDate": "2019-12-31T15:00:00.000+0000",
    "modDate": "2020-01-01T11:59:09.929+0000",
    "reComment": null,
    "post": {
        "id": 2,
        "subject": "싸다 싸!",
        "productName": "롱패딩",
        "price": 40000,
        "content": "<p>ㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋ</p><p><br></p><p><img src=\"/api/file/4\" style=\"width: 225px;\"></p><p><br></p><p>ㅎㅎㅎㅎㅎㅎㅎ</p><p><img src=\"/api/file/5\" style=\"width: 192px;\"><br></p>",
        "regDate": "2019-12-21T15:00:00.000+0000",
        "modDate": "2019-12-30T15:00:00.000+0000",
        "category": 0,
        "stock": 2
    },
    "user": {
        "id": 10,
        "account": "bh",
        "password": "$2a$10$aeh55EpgpIPlD8kD4VcngezwLjF.C3f0QcfbSkLMe6su4zjmdKmje",
        "name": "김보현",
        "birth": "1996-04-11T15:00:00.000+0000",
        "phone": "01088336656",
        "address": "청학",
        "email": "96bohyun@naver.com",
        "gender": "F",
        "role": {
            "id": 11,
            "rolename": "ROLE_USER"
        }
    }
}

(실패)
- HttpStatus : 400
- body
{
    "result": false,	// boolean
    "e_message": "잘못된 형식의 댓글 입니다."
}


3. 댓글 삭제
[Request]
- path : {host}/api/comment?id={id}
- method : DELETE
- Headers
		auth-token={auth-token}		// 로그인 후 얻은 token 
- example
localhost:8101/api/comment?id=6		// id : commentId

[Response]
(성공)
- HttpStatus : 200
- body
true	// boolean

(실패)
- HttpStatus : 200
- body
false	// boolean
```

&nbsp;

#### 1-7. 리뷰

```

```

&nbsp;

#### 1-8. 환불

```

```

&nbsp;

&nbsp;

### 2. 관리자

&nbsp;

#### 2-1. 회원관리