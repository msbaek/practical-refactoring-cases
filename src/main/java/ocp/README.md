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

객체지향적인 방법으로 POST 메소드 추가에 대응을 했다면 변경이 필요한 곳(분기처리가 필요한 곳)에 추상화를 적용했어야 한다.

이 말은 변경이 필요한 곳을 조건문으로 처리하지 말고 인터페이스에 대한 호출로 변경해야 한다는 것을 의미한다.

즉

- createUrl
- setPOSTConnectionSettings(이 메소드 이름은 setAdditionalConnectionSettings라고 하는게 좀 더 나을 듯)
- writePOSTParameters

등의 함수들을 갖는 인터페이스를 정의하고 이에 대한 구현체를 제공하라는 의미이다.