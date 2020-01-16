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



### header, param



- header

  ```java
  // 특정한 헤더가 있는 요청 처리
  @RequestMapping(headers = "AUTHORIZATION")
  @RequestMapping(headers = HttpHeaders.AUTHORIZATION)
  
  // 특정한 헤더가 없는 요청 처리
  @RequestMapping(headers = "!" + HttpHeaders.AUTHORIZATION)
  
  // 특정 헤더에 value값도 있는 경우 처리
  @RequestMapping(headers = HttpHeaders.AUTHORIZATION + "=" + "hooong")
  ```

- param

  ```java
  // 특정 param이 있는 요청 처리
  @RequestMapping(params = "name")
  
  // 특정 param이 없는 요청 처리
  @RequestMapping(params = "!name")
  
  // 특정 키/값을 가지는 요청 처리
  @RequestMapping(params = "name=hooong")
  ```



### HEAD, OPTIONS

> ​	직접 구현하지 않아도 spring web mvc에서 자동으로 처리하는 http 메서드로 아래 OPTIONS테스트 결과를 보면 HEAD와 OPTIONS를 만들지 않았어도 Allows에 포함되어 있는 것을 확인할 수 있으.



- HEAD

  - GET과 동일하지만 body (즉, 응답본문)을 제외하고 응답 헤더만 받는다.

  ```java
  @Test
  public void holloTest() throws Exception {
    mockMvc.perform(head("/hello"))
      .andDo(print())
      .andExpect(status().isOk());
  }
  
  /** 원래는 return으로 "hello"를 반환하지만 Body가 비어있는 것을 확인할 수 있다.
  	 MockHttpServletResponse:
             Status = 200
      Error message = null
            Headers = [Content-Type:"text/plain;charset=UTF-8", Content-Length:"5"]
       Content type = text/plain;charset=UTF-8
               Body = 
      Forwarded URL = null
     Redirected URL = null
  */
  ```

- OPTIONS

  - 사용할 수 있는 메서드를 알려준다.

  ```java
  // Controller에는 @GetMapping과 @PostMapping을 만든 상태.
  @Test
  public void holloTest() throws Exception {
    mockMvc.perform(head("/hello"))
      .andDo(print())
      .andExpect(status().isOk());
  }
  
  /** Headers에 Allow로 가능한 요청 메서드들이 반환되었다.
  	MockHttpServletResponse:
             Status = 200
      Error message = null
            Headers = [Allow:"GET,HEAD,POST,OPTIONS"]
       Content type = null
               Body = 
      Forwarded URL = null
     Redirected URL = null
  */
  ```



### Custom Annotation

> - Meta Annotation 
>   - 애노테이션에 사용할 수 있는 애노테이션
> - Composed Annotation
>   - 하나 또는 여러개의 메타 애노테이션을 조합해서 만든 애노테이션
>   - 코드가 간결해지고 보다 구체적인 의미를 부여할 수 있음.



- @GetHelloMapping

```java
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@RequestMapping(method = RequestMethod.GET, value = "/hello")
public @interface GetHelloMapping {
}
```



- @Retention

  > 해당 애노테이션 정보를 언제까지 유지할 것인지를 설정하는 애노테이션

  ```java
  @Retention(RetentionPolicy.SOURCE)
   - 소스코드까지만 유지. 즉, 컴파일하면 정보가 사라짐. 따라서 주석으로 볼 수 있다.
  @Retention(RetentionPolicy.CLASS)
   - 컴파일 한 .class파일까지 유지. 즉, 런타임 시 클래스를 메모리로 읽어오면서 정보가 사라짐.
  @Retention(RetentionPolicy.RUNTIME)
   - 클래스가 메모리에 올라와있을때에도 정보를 유지.
  ```



- @Target

  > 해당 애노테이션을 어디에 사용할 수 있는지에 대한 정보

  `@Target(ElementType.METHOD)`



- @Documented

  > 해당 애노테이션을 사용한 코드의 문서에 애노테이션 정보를 표기할지를 결정.



### HandlerMethod - URI패턴



- ### @PathVariable

  > - request uri패턴에서 일부를 argument로 받는 방법.
  > - Uri 패턴에서 이름과 argument의 이름이 다를경우 ()안에 명시해준다.
  >   - `@PathVariable("id") Integer idvalue`
  > - type conversion을 알아서 해준다. (String으로 받은 id값이 integer로 변환될 수 있다.)
  > - 값이 반드시 있어야 한다. (required = false)옵션으로 바꿀 수 있긴하다.
  > - Optional을 지원한다. (`@PathVariable Optional<String> id`)

  - ##### 사용예제

  ```java
  @GetMapping("/events/{id}")
  @ResponseBody
  public Event getEvent(@PathVariable Integer id) {
    Event event = new Event();
    event.setId(idvalue);
    return event;
  }
  ```

- ### @MatrixVariable

  > - @PathVariable과 다르게 키/값 쌍의 데이터를 argument로 받는 방법.
  >
  > - Type conversion을 지원
  >
  > - 값이 반드시 있어야 한다. 
  >
  > - Optional을 지원
  >
  > - 이 Annotation을 사용하려면 아래와 같은 설정으로 이 기능을 활성화 해야함.
  >
  >   ```java
  >   @Configuration
  >   public class WebConfig implements WebMvcConfigurer {
  >   
  >       @Override
  >       public void configurePathMatch(PathMatchConfigurer configurer) {
  >           UrlPathHelper urlPathHelper = new UrlPathHelper();
  >           // 세미콜론을 없애지 않도록 설정.
  >           urlPathHelper.setRemoveSemicolonContent(false);
  >           configurer.setUrlPathHelper(urlPathHelper);
  >       }
  >     
  >   }
  >   ```

  - ##### 사용예제

  ```java
  // request : '../events/1;name=hooong'
  
  @GetMapping("/events/{id}")
  @ResponseBody
  public Event getEvent(@PathVariable Integer id, 
                        @MatrixVariable String name) {
    Event event = new Event();
    event.setId(idvalue);
    event.setName(name);
    return event;
  }
  ```

  

  ### @RequestMapping

  > - 요청 매개변수 (하부항목에 설명)에 있는 단순 타입의 데이터를 argument로 받는 방법.
  >   - 요청 매개변수에는 '쿼리 매개변수', '폼 데이터'가 있다. 쿼리 매개변수는 `/events?name=hooong` 와 같이 uri에 ?뒤에 더해지는 매개변수이고, 폼 데이터는 말그대로 <form>태그 안에서 넘어오는 매개변수들을 말한다.
  >
  > - Type Conversion 지워
  >
  > - 값이 반드시 있어야 한다. ( required옵션과 Optional을 사용하여 설정을 바꿀 수도 있다.)
  >
  > - Map을 사용해서 받아올 수도 있다. 그러나 그냥 하나하나 받아오는게 편할 수 있다.
  >
  >   ```java
  >   @PostMapping("/events")
  >   @ResponseBody
  >   public Event getEvent(@RequestParam Map<String, String> params) {
  >       Event event = new Event();
  >       event.setName(params.get("name"));
  >       return event;
  >   }
  >   ```
  >
  > - 생략이 가능하다. => 생략하지 않고 명시하는게 좋을 수 있다.

  - 사용예제

  ```java
  // request : '/events?name=hooong&limit=5'
  //		또는 '/events'에 form 데이터에 name와 limit정보를 담고있음.
  
  @PostMapping("/events")
  @ResponseBody
  public Event getEvent(@RequestParam String name,
                        @RequestParam Integer limit) {
    Event event = new Event();
    event.setName(name);
    event.setLimit(limit);
    return event;
  }
  ```



### Thymeleaf 간단 사용법

- html파일안의 <html>태그 안에 속성을 추가해준다.
  - `xmlns:th="http://www.thymeleaf.org"`

```html
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>Thymeleaf</title>
</head>
<body>

</body>
</html>
```

- thymeleaf의 표현식

  ```
  @{} : URL을 표현하는 식
  ${} : variable을 표현하는 식
  *{} : selection 표현식
  ```

  - 참고 : [https://www.thymeleaf.org/doc/articles/standarddialect5minutes.html](https://www.thymeleaf.org/doc/articles/standarddialect5minutes.html)

- thymeleaf를 활용하여 form만들어보기

  ```java
  @Controller
  public class SampleController {
  
      @GetMapping("/events/form")
      public String eventsForm(Model model) {
        	// "event"라는 이름으로 새로운 event객체를 넘겨준다.
          model.addAttribute("event",new Event());
          return "/events/form";
      }
  
      @PostMapping("/events")
      @ResponseBody
      public Event getEvent(@RequestParam String name,
                            @RequestParam Integer limit) {
          Event event = new Event();
          event.setName(name);
          event.setLimit(limit);
          return event;
      }
  }
  ```

  ```html
  <!DOCTYPE html>
  <html lang="en" xmlns:th="http://www.thymeleaf.org">
  <head>
      <meta charset="UTF-8">
      <title>Create New Event</title>
  </head>
  <body>
  <!-- Controller에서 받은 "event"를 th:object를 통해 받아온다. -->
  <form action="#" th:action="@{/events}" method="post" th:object="${event}">
    	<!-- *{}를 써서 event객체안의 name과 limit 속성을 selection 할 수 있다. -->
      <input type="text" title="name" th:field="*{name}"/>
      <input type="text" title="limit" th:field="*{limit}"/>
      <input type="submit">
  </form>
  </body>
  </html>
  ```




