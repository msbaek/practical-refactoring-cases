# 들어가기

이 예제에서는 한가지 기능(Http GET 요청 처리)을 구현한 후 유사한 다른 기능(PUT 요청 처리)이 추가되었을 때 일반적으로 해결하는 방법(분기 처리)과 객체지향적으로 처리하는 방법(추상화 적용)을 설명한다.


# 1. 최초 요구사항

GET 메소드로 http request를 요청하고 결과를 반환하는 요구사항

![](http://i.imgur.com/dOe9DnP.png)

# 2. POST 메소드 처리 추가 요청 사항

언제나 그렇듯 요구사항은 항상 변한다.

기존 GET 메소드 처리 외에 POST 메소드 처리가 추가로 요구되었다.

### 2.1 failing test 추가
![image](http://i.imgur.com/iiM46dN.png)

### 2.2 make it pass

새로운 요구사항이 생기면 보통은 기존에 비슷한 요구사항을 처리하던 class에 새로운 메소드를 추가하거나 기존 메소드를 살짝 수정하여 이를 해결하는게 많이 볼 수 있는 모습니다. 새로운 기능을 구현하기 위해 필요한 기능 중 많은 부분을 이미 구현된 기능에서 차용해서 쓸 수 있는 것이 한가지 이유이다.

![image](http://i.imgur.com/enmj2DX.png)

### 3.3 refactoring
코드를 읽기 좋게 하기 위해 Extract Method를 수차례 수행
![image](https://api.monosnap.com/rpc/file/download?id=6sXGzD0rKRCivfB6WOIskMyu2k5fqS)

# 3. 문제점

여기서 더 이상 추가/변경 요구사항이 없다면 문제가 없다. 하지만 GET, POST 외에도 PUT, DELETE, HEAD 등 다양한 메소드에 대한 요구사항이 발생한다면 isGet 필드 값을 기반으로 분기처리하는 로직들은 계속해서 변경이 발생하게 된다.

이는 [OCP(Open Closed Principle)](https://www.youtube.com/watch?v=dqa-IdafeIE)위반이다.

객체지향적인 방법으로 POST 메소드 추가에 대응을 했다면 변경이 필요한 곳(분기처리가 필요한 곳)에 다형성을 갖도록 하여 분기를 제거함으로써 OCP를 준수할 수 있다.

다형성을 제공하는 방법은 2가지를 생각할 수 있다.

- 상속: 변경이 필요한 함수들을 abstract로 선언하고 서브 클래스에서 오버라이드하는 방법 - [Template Method Pattern](https://www.google.co.kr/url?sa=t&rct=j&q=&esrc=s&source=web&cd=1&cad=rja&uact=8&ved=0CCIQFjAAahUKEwjlr8-fy8bIAhUjKKYKHYbAClw&url=https%3A%2F%2Fen.wikipedia.org%2Fwiki%2FTemplate_method_pattern&usg=AFQjCNH1JeW2hWCJ-mkbqGlmR-OGR5ZNqQ&sig2=oqjLyvYqSuttzde82hFzAg)
- 위임: 변경이 필요한 함수들을 interface로 정의하고 구현체를 제공하는 방법 - [Strategy Pattern](https://www.google.co.kr/url?sa=t&rct=j&q=&esrc=s&source=web&cd=2&cad=rja&uact=8&ved=0CCgQFjABahUKEwjHreury8bIAhVBe6YKHRD4CeI&url=https%3A%2F%2Fen.wikipedia.org%2Fwiki%2FStrategy_pattern&usg=AFQjCNEs9k3dvzK3TPaeWwmhld6qHDK6-Q&sig2=ktUelyLgs3BZRPZgrqw5oQ&bvm=bv.105039540,d.dGY)

이 예제에서는 상속을 이용하는 방법이 좋아보인다. 만일 변경이 필요한 메소드가 1개였다면 위임이 보다 좋아 보였을 것 같다(이건 그냥 개인적인 취향인듯. 혹 맘에 안 드시는 분은 위임으로 해결해 보셔도...)

즉

- createUrl
- setPOSTConnectionSettings(이 메소드 이름은 setAdditionalConnectionSettings라고 하는게 좀 더 나을 듯)
- writePOSTParameters(additionalWorkWith(paramStrings)라고 하는게...)

등의 함수들을 abstract로 정의하고 서브 클래스에서 구현하도록 하겠다.

# 4. 다형성을 갖도록 상속을 적용

### 4.1 테스트가 다형성을 갖도록 변경

![image](https://api.monosnap.com/rpc/file/download?id=GwlgCur2umBJKkptVivPAt6jHXC8PN)

### 4.2 makt it pass

테스트가 동작하도록 수정

아래와 같이 2개의 클래스를 추가

```
package ocp;

public class GETRequestExecutor extends HttpRequestExecutor {
}
```

```
package ocp;

public class POSTExecutor extends HttpRequestExecutor {
}
```

### 4.3 Push Members Down

서브 클래스에서 오버라이드해야할 메소드들을 IntelliJ의 "Push Members Down"을 이용해서 서브 클래스로 내린다.

![image](http://i.imgur.com/iY2phYg.png)

위 그림은 서브 클래스로 내릴 `createUrl` 메소드에서 `push down members`를 선택하는 화면이다.

![image](https://api.monosnap.com/rpc/file/download?id=waiYlLBothPlFGwp8tACi7iN75m8E0)

`Keep Abstract`를 선택한 후 `Refactor`를 클릭한다.

이렇게 하면 `createUrl` 메소드는 GETExecutor, POSTExecutor로 복사되면서 HttpRequestExecutor에는 아래와 같이 protected abstract로 남게 된다.

![image](https://api.monosnap.com/rpc/file/download?id=iGkUzKrLArLflrFDtvg54OKstfUYeM)

### 4.4 Make it work

push down한 메소드들을 아래와 같이 수정하여 테스트가 성공하도록 한다.

![image](http://i.imgur.com/muVtfuA.png)

![image](http://i.imgur.com/zikE1Yp.png)

### 4.5 More Push Down Members

![image](https://api.monosnap.com/rpc/file/download?id=vV9w84U7PQtrobHh0v5BMZC1O6z4my)

![image](https://api.monosnap.com/rpc/file/download?id=pQi3Sp0vBKbKci4N3dPMOiaE1r652f)

![image](https://api.monosnap.com/rpc/file/download?id=fbQp1iE8MD2NRR0TwYVdq7j6C4qs26)

### Refactoring

아래와 같이 불필요한 코드를 제거한다.

![image](https://api.monosnap.com/rpc/file/download?id=r2sr49iHp529zgKED0a90wwcyIziSE)

# 결론

이상에서 조건문을 추가하여 새로운 요구사항을 반영하던 코드를 새로운 서브 클래스를 추가하여 동일한 결과를 얻도록 개선해 보았다.

이와 같은 개선을 통해 새로운 요구사항이 있을 때 기존 코드를 수정하는 것이 아니라 새로운 코드(서브클래스)를 추가하기만 하면 되므로 OCP를 준수하고, 이를 통해 시스템을 보다 안정적으로 운영할 수 있게 되었다.

처음 새로운 기능을 구현할 때 향후 발생 가능한 다양한 경우를 고려하여 미리 추상화를 적용하는 것은 바람직하지 못하다. 우리가 예상한 일은 실제로 일어나지 않을때가 더 많고, 우리가 예상하지 않은 일들이 더 자주 일어나기 때문에...또 새로운 기능을 넣는 것보다 이미 있는 기능(코드)를 제거하는 것이 훨씬 어렵고 위험하기 때문에...

초기에 시간을 많이 들여서 고민하여 향후 발생 가능한 모든 변경 사항을 찾아낼 수 있다면 너무 좋을 것이다. 하지만 이러기에는 너무 많은 시간이 들고, 또 ["Unknown Unknowns"](https://en.wikipedia.org/wiki/There_are_known_knowns) 때문에 미래를 내다 볼 수 있는 **마법의 구슬**이 없다면 불가한 것이다.

이러기 보다는 지금 필요한 만큼만 실행하고 향후 변화가 필요할 때 앞으로의 동일한 변화를 잘 수용할 수 있도록 개선하는 것이 훨씬 좋은 방법이다. 이것이 **OCP**가 중요한 개념인 이유이고, 이것이 **애자일을 실행하는 방법**이라고 생각한다.