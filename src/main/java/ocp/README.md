# 1. 최초 요구사항

GET 메소드로 http request를 요청하고 결과를 반환하는 요구사항

![](http://i.imgur.com/dOe9DnP.png)

# 2. POST 메소드 처리 추가 요청 사항

언제나 그렇듯 요구사항은 항상 변한다.

기존 GET 메소드 처리 외에 POST 메소드 처리가 추가로 요구되었다.

### 2.1 failing test 추가
![image](http://i.imgur.com/iiM46dN.png)

### 2.2 make it pass
![image](http://i.imgur.com/enmj2DX.png)

### 3.3 refactoring
코드를 읽기 좋게 하기 위해 Extract Method를 수차례 수행
![image](https://api.monosnap.com/rpc/file/download?id=6sXGzD0rKRCivfB6WOIskMyu2k5fqS)

# 3. 문제점

여기서 더 이상 추가/변경 요구사항이 없다면 문제가 없다. 하지만 GET, POST 외에도 PUT, DELETE, HEAD 등 다양한 메소드에 대한 요구사항이 발생한다면 isGet 필드 값을 기반으로 분기처리하는 로직들은 계속해서 변경이 발생하게 된다.

이는 [OCP(Open Closed Principle)](https://www.youtube.com/watch?v=dqa-IdafeIE)위반이다.

객체지향적인 방법으로 POST 메소드 추가에 대응을 했다면 변경이 필요한 곳(분기처리가 필요한 곳)에 다형성을 갖도록 하여 분기를 제거함으로써 OCP를 준수할 수 있다.

다형성을 제공하는 방법은 2가지를 생각할 수 있다.

- 상속: 변경이 필요한 함수들을 abstract로 선언하고 서브 클래스에서 오버라이드하는 방법
- 위임: 변경이 필요한 함수들을 interface로 정의하고 구현체를 제공하는 방법

이 예제에서는 상속을 이용하는 방법이 좋아보인다. 만일 변경이 필요한 메소드가 1개였다면 위임이 보다 좋아 보였을 것 같다(이건 그냥 개인적인 취향인듯. 혹 맘에 안 드시는 분은 위임으로 해결해 보셔도...)

즉

- createUrl
- setPOSTConnectionSettings(이 메소드 이름은 setAdditionalConnectionSettings라고 하는게 좀 더 나을 듯)
- writePOSTParameters(additionalWorkWith(paramStrings)라고 하는게...)

등의 함수들을 abstract로 정의하고 서브 클래스에서 구현하도록 하겠다.

# 4. 다형성을 갖도록 상속을 적용

### 4.1 테스트가 다형성을 갖도록 변경

![image](https://api.monosnap.com/rpc/file/download?id=GwlgCur2umBJKkptVivPAt6jHXC8PN)