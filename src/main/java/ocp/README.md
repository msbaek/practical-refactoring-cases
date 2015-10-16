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