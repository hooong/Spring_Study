#  HTTP 요청 메서드

- GET

  - 클라이언트가 서버의 리소스를 요청할 때 사용.
  - 캐싱 가능.
  - Idempotent (같은 요청에는 같은 응답)

- POST

  - 클라이언트가 서버의 리소스를 수정 또는 생성할때 사용.
  - 캐싱 불가능
  - GET과 다르게 idempotent하지 않을 수 있다.

- PUT

  - URI에 해당하는 데이터를 새로 만들거나 수정할 때 사용.
  - Idempotent하다.

  ```
  POST와 PUT의 차이점
  - 두 요청 모두 서버의 리소스를 새로 만들거나 수정할 때 사용할 수 있다. 그러나 다른점이 있다면 idempotent하지 않다는 것이다. idempotent는 같은 요청을 몇번 보내도 항상 같은 결과가 나온다는 것인데 POST의 경우 '/dogs'라는 요청으로 '/dogs/1', '/dogs/2'...과 같이 다른 곳에 리소스가 생길 수 있다. 그러나 PUT의 경우에는 리소스의 위치를 명시해줘야 함으로써 idempotent하다는 것이다. 가령 '/dogs/1'이라고 PUT요청을 보내면 '/dogs/1'의 위치에 리소스를 새로 만들거나 그 위치에 있는 리소스를 수정하겠다는 것이다.
  ```

- PATCH

  - PUT과 비슷, 기존 엔티티와 새로운 데이터를 보내야한다.
  - idempotent하다.

- DELETE

  - URI에 해당하는 리소스를 삭제할 때 사용.
  - idempotent하다.



### 요청메서드로 Mapping



- @RequestMapping ( 모든 요청메서드를 허용한다. 단, method속성으로 지정가능.)

  ```java
  // value속성만 가질 때
  @Controller
  public class SampleController {
  
      @RequestMapping("/hello")
      @ResponseBody
      public String hello() {
          return "hello";
      }
  }
  
  // method속성을 가질 때
  @Controller
  public class SampleController {
  
      @RequestMapping(value = "/hello", 
                      method = {RequestMethod.GET, RequestMethod.POST}
              			  )
      @ResponseBody
      public String hello() {
          return "hello";
      }
  }
  ```



- @---Mapping  (---에는 각각의 요청 메서드가 들어간다.)

  ```java
  @Controller
  public class SampleController {
  
      @GetMapping("/hello") // @PostMapping("/hello")
      @ResponseBody
      public String hello() {
          return "hello";
      }
  }
  ```



### URI 패턴로 Mapping



- 식별자

  ```java
  ? : 한 글자 
    ex) "/hello/?" => "hello/a", "hello/1", "hello/2" ...
  
  * : 여러 글자
    ex) "/hello/*" => "hello/hooong", "hello/hi" ...
    
  ** : 여러 패스
    ex) "hello/**" => "hello/hooong/1", "hello/1/2/3" ...
  ```



- @RequestMapping을 통한 조합

  ```java
  // "hello/hooong"
  @Controller
  @RequestMapping("/hello")
  public class SampleController {
  
      @GetMapping("/hooong")
      @ResponseBody
      public String hellohooong() {
          return "hello";
      }
    
    	// 매핑이 중복이되면 가장 구체적으로 맞는 handler를 선택 => 여기서는 hellohooong을 선택.
      @GetMapping("/**")
      @ResponseBody
      public String hello() {
          return "hello";
      }
  
  }
  ```

  

- 정규식

  ```java
  // "/hooong" => return "hello hooong"
  @Controller
  public class SampleController {
  
      @GetMapping("/{name:[a-z]+}")
      @ResponseBody
      public String hello(@PathVariable name) {
          return "hello " + name;
      }
    
  }
  ```

  

### 미디어타입 Mapping



- 특정 타입의 데이터를 담고있는 요청만 처리하는 Handler (content-type 헤더로 필터링)

  ```java
  // Json타입을 가지고 있는 요청만 처리
  @Controller
  public class SampleController {
  
      @GetMapping(
              value = "/hello",
              consumes = MediaType.APPLICATION_JSON,
      )
      @ResponseBody
      public String hello() {
          return "hello";
      }
  }
  ```

  - 매치되지 않는 경우에 415 (Unsupported Media Type) 뱉음.



- 특정한 타입의 응답을 만드는 핸들러 (accept 헤더로 필터링)

  ```java
  // Plain Text를 만들어내는 핸들러 
  @Controller
  public class SampleController {
  
      @GetMapping(
              value = "/hello",
              produces = MediaType.TEXT_PLAIN_VALUE
      )
      @ResponseBody
      public String hello() {
          return "hello";
      }
  }
  ```

  - 매치되지 않는 경우에 406 (Not Acceptable) 뱉음.

