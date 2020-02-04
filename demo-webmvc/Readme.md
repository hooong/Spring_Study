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



## @ModelAttribute

> - @RequestMapping에서 Map을 사용해서 name과 limit을 한번에 받아와서 하나의 객체로 만들었었다. 반면 @ModelAttribute는 만들어져있는 Event라는 객체로 받아올 수 있게해주는 애노테이션이다.
> - 생략이 가능하지만 가독성을 위해 써주는게 좋을 것 같다.

```java
// Handler
@PostMapping("/events")
@ResponseBody
public Event getEvent(@ModelAttribute Event event) {
  return event;
}

// test code
@Test
public void postEvent() throws Exception {
  mockMvc.perform(post("/events")
                  .param("name","hooong")
                  .param("limit","10"))
    .andDo(print())
    .andExpect(status().isOk())
    .andExpect(jsonPath("name").value("hooong"));
}					// => test 성공.

@Test
public void postEvent() throws Exception {
  mockMvc.perform(post("/events")
                  .param("name","hooong")
                  .param("limit","hong"))			// Integer인 'limit'에 문자열을 넣음
    .andDo(print())
    .andExpect(status().isOk())
    .andExpect(jsonPath("name").value("hooong"));
}					// => BindException 400 error 발생
```

- 위에서의 bindException을 다루고 싶은 경우 BindingResult라는 아규먼트를 추가해준다.

```java
// Handler
@PostMapping("/events")
@ResponseBody
public Event getEvent(@Valid @ModelAttribute Event event, BindingResult bindingResult) {
  if(bindingResult.hasErrors()) {
    System.out.println("=====================");
    bindingResult.getAllErrors().forEach(c -> {
      System.out.println(c.toString());
    });
  }
  return event;
}

// test code
@Test
public void postEvent() throws Exception {
  mockMvc.perform(post("/events")
                  .param("name","hooong")
                  .param("limit","hong"))			// Integer인 'limit'에 문자열을 넣음
    .andDo(print())
    .andExpect(status().isOk())
    .andExpect(jsonPath("name").value("hooong"));
}					// => 위에서와 같은 테스트이지만 BindingResult가 에러를 처리해줘서 test는 성공.
/*
- bindingResult에서 에러에 대한 정보를 넘겨받아 출력. => name는 넘겨받지만 limit은 convert를 하지 못하여 null값이 설정된다.

"Field error in object 'event' on field 'limit': rejected value [hong]; codes [typeMismatch.event.limit,typeMismatch.limit,typeMismatch.java.lang.Integer,typeMismatch]; arguments [org.springframework.context.support.DefaultMessageSourceResolvable: codes [event.limit,limit]; arguments []; default message [limit]]; default message [Failed to convert property value of type 'java.lang.String' to required type 'java.lang.Integer' for property 'limit'; nested exception is java.lang.NumberFormatException: For input string: "hong"]"
*/
```



### @Valid

- 바인딩을 한 뒤 값에 대한 검증을 하기위해서는 @Valid 또는 @Validated를 사용하면 된다.

```java
// Handler
@PostMapping("/events")
@ResponseBody
public Event getEvent(@Valid @ModelAttribute Event event, BindingResult bindingResult) {
  if(bindingResult.hasErrors()) {
    System.out.println("=====================");
    bindingResult.getAllErrors().forEach(c -> {
      System.out.println(c.toString());
    });
  }
  return event;
}

// Event Class
public class Event {

    private Integer id;

    private String name;

    @Min(0)		// 0보다 작은 값은 허용하지 않는다.
    private Integer limit;
		
  	... (이하 생략)
}

// Test Code
@Test
public void postEvent() throws Exception {
  mockMvc.perform(post("/events?name=hooong")
                  .param("limit","-10"))
    .andDo(print())
    .andExpect(status().isOk())
    .andExpect(jsonPath("name").value("hooong"));
} // => test는 성공. bindingResult를 통해 validation에러가 출력되는 것을 확인할 수 있다.
```

<br>

### @Validated

- @Valid를 사용하면 그룹을 지정할 수 없지만 @Validated를 사용하면 그룹을 지정할 수 있다.

```java
// Event Class
public class Event {
	
  	// 그룹을 사용하기 위한 interface를 생성.
    interface ValidateLimit {}
    interface ValidateName {}

    private Integer id;
		
  	// @NotBlank에는 ValidateName이라는 그룹을 지정
    @NotBlank(groups = ValidateName.class)
    private String name;
		
  	// @Min에는 ValidateLimit이라는 그룹을 지정
    @Min(value = 0, groups = ValidateLimit.class)
    private Integer limit;
	
  	... (이하 생략)
}

// Handler
@PostMapping("/events")
@ResponseBody
//	ValidateName라는 그룹을 지정해서 @NotBlank만 검증을 한다.
public Event getEvent(@Validated(Event.ValidateName.class) @ModelAttribute Event event, 													BindingResult bindingResult) {
  if(bindingResult.hasErrors()) {
    System.out.println("=====================");
    bindingResult.getAllErrors().forEach(c -> {
      System.out.println(c.toString());
    });
  }
  return event;
}
// 또는 ValidateLimit을 지정하여 @Min(0)만을 검증.
public Event getEvent(@Validated(Event.ValidateLimit.class) @ModelAttribute Event event, 													BindingResult bindingResult) {
  if(bindingResult.hasErrors()) {
    System.out.println("=====================");
    bindingResult.getAllErrors().forEach(c -> {
      System.out.println(c.toString());
    });
  }
  return event;
}
```

<br>

### form(submit) 에러처리

> form을 사용해 데이터를 submit을 하려할때 @valid를 통해 검증이 되지 않으면 BindingResult에 에러 정보가 담긴다고 했는데, 이 에러를 view를 통해 띄워주는 방법에 대해 알아보겠습니다.

<center><img width="507" alt="Screen Shot 2020-01-16 at 8 05 25 PM" src="https://user-images.githubusercontent.com/37801041/72519901-a2c30e80-389b-11ea-9798-c0ccd59a3236.png"></center>
```java
// Handler
@PostMapping("/events")
public String createEvent(@Validated @ModelAttribute Event event,
                          BindingResult bindingResult,
                          Model model) {
  // 바인딩 에러가 발생하면 /events/form으로 요청이 보내진다.
  if(bindingResult.hasErrors()) {
    return "/events/form";
  }

  // Post -> Redirect -> Get
  // redirection을 해주어야 Post에서 새로고침을 했을 경우 Form이 중복 submit되는 것을 막을 수 있다.
  return "redirect:/events/list";
}

@GetMapping("/events/list")
public String getEvents(Model model) {
  Event event1 = new Event();
  event1.setName("hooong");
  event1.setLimit(10);

  Event event2 = new Event();
  event2.setName("hooong2");
  event2.setLimit(20);

  List<Event> eventList = new ArrayList<>();
  eventList.add(event1);
  eventList.add(event2);

  model.addAttribute(eventList);

  return "/events/list";
}
```

- ### `events/form.html`

```html
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>Create New Event</title>
</head>
<body>
<form action="#" th:action="@{/events}" method="post" th:object="${event}">
  	<!-- 바로 아래 p태그부분의 타임리프를 사용해 에러에 대한 정보를 띄워준다. -->	
    <p th:if="${#fields.hasErrors('limit')}" th:errors="*{limit}">Incorrect date</p>
    <p th:if="${#fields.hasErrors('name')}" th:errors="*{name}">Incorrect date</p>
  
    <input type="text" title="name" th:field="*{name}"/>
    <input type="text" title="limit" th:field="*{limit}"/>
    <input type="submit">
</form>
</body>
</html>
```

- ### `events/list.html`

```html
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>Create New Event</title>
</head>
<body>
<a th:href="@{/events/form}">Create New Event</a>
  
<!-- 타임리프를 사용해 받아온 eventList를 보여주는 코드 -->
<div th:unless="${#lists.isEmpty(eventList)}">
    <ul th:each="event: ${eventList}">
        <p th:text="${event.Name}">Event Name</p>
    </ul>
</div>

</body>
</html>
```

<br>

### @SessionAttributes

>장바구니 또는 입력을 여러 페이지를 통하여 받는 경우에는 특정 개체들에대한 정보를 계속 유지해야한다. 이 정보를 유지해주는 것이 Session이다. 그럼 Session을 사용하는 방법들에 대하여 알아보자.

- HttpSessions

  ```java
  @GetMapping("/events/form")
  public String eventsForm(Model model, HttpSession httpSession) {
    Event newEvent = new Event();
    newEvent.setLimit(50);
    model.addAttribute("event",newEvent);
    // newEvent를 "event"라는 이름으로 session에 담는다.
    httpSession.setAttribute("event",newEvent);
    return "/events/form";
  }
  ```

- @SessionAttributes

  ```java
  @Controller
  @SessionAttributes("event")
  public class SampleController {
  
      @GetMapping("/events/form")
      public String eventsForm(Model model) {
          Event newEvent = new Event();
          newEvent.setLimit(50);
          model.addAttribute("event",newEvent);
          return "/events/form";
      }
  }
  ```

  > @Controller 애노테이션 밑에 @SessionAttributes("event")라고 써주게되면 자동으로 "event"라는 이름으로 model이 만들어질때 session에도 바인딩이 된다.

  

<br>

### @SessionAttributes를 사용하여 멀티 폼 서브밋 구현

> 예제로 Event에 이름을 넣는 페이지, 인원제한을 넣는 페이지를 나누어 폼으로 입력을 받는 것을 구현해 볼 것이다.

- Controller

  ```java
  @Controller
  @SessionAttributes("event")			// 세션을 자동으로 바인딩해주기 위한 애노테이션
  public class SampleController {
  		
    	// name을 받는 form을 띄워주는 GET
      @GetMapping("/events/form/name")
      public String eventsFormName(Model model) {
          model.addAttribute("event",new Event());
          return "/events/form-name";
      }
  		
    	// name을 입력받은 form을 처리하는 POST
      @PostMapping("/events/form/name")
      public String eventsFormNameSubmit(@Validated @ModelAttribute Event event,
                                BindingResult bindingResult) {
          if(bindingResult.hasErrors()) {
              return "/events/form-name";
          }
          return "redirect:/events/form/limit";
      }
  		
    	// limit을 받는 form을 띄워주는 GET
    	// @ModelAttribute Event를 통하여 Session의 event를 그대로 사용한다.
      @GetMapping("/events/form/limit")
      public String eventsFormLimit(@ModelAttribute Event event, Model model) {
          model.addAttribute("event",event);
          return "/events/form-limit";
      }
  
    	// limit을 받은 form을 처리하는 POST
      @PostMapping("/events/form/limit")
      public String eventsFormLimitSubmit(@Validated @ModelAttribute Event event,
                                         BindingResult bindingResult,
                                         SessionStatus sessionStatus) {
          if(bindingResult.hasErrors()) {
              return "/events/form-limit";
          }
        	// setComplete()를 사용하면 Session을 비울 수 있다.
          sessionStatus.setComplete();
          return "redirect:/events/list";
      }
  		
    	
      @GetMapping("/events/list")
      public String getEvents(Model model) {
          // ... (events를 보여주는 로직 작성)
      }
  }
  ```

- View(html)

  ```html
  <!-- 'form-name.html' -->
  <!DOCTYPE html>
  <html lang="en" xmlns:th="http://www.thymeleaf.org">
  <head>
      <meta charset="UTF-8">
  </head>
  <body>
  <form action="#" th:action="@{/events/form/name}" method="post" th:object="${event}">
      <p th:if="${#fields.hasErrors('name')}" th:errors="*{name}">Incorrect date</p>
      Name : <input type="text" title="name" th:field="*{name}"/>
      <input type="submit">
  </form>
  </body>
  </html>
  
  <!-- 'form-limit.html' -->
  <!DOCTYPE html>
  <html lang="en" xmlns:th="http://www.thymeleaf.org">
  <head>
      <meta charset="UTF-8">
  </head>
  <body>
  <form action="#" th:action="@{/events/form/limit}" method="post" th:object="${event}">
      <p th:if="${#fields.hasErrors('limit')}" th:errors="*{limit}">Incorrect date</p>
      Limit : <input type="text" title="limit" th:field="*{limit}"/>
      <input type="submit">
  </form>
  </body>
  </html>
  ```

  <br>

### @SessionAttribute

> @SessionAttributes와는 's'가 붙고 안붙고인데 하는 일이 다르다. 이 점을 유의 해야한다.

- @SessionAttributes는 애노테이션이 붙어있는 해당 컨트롤러 내에서만 동작을 한다. 따라서 같은 컨트롤러 안에서 다루는 특정 모델 객체를 세션에 넣고 공유할 때 사용하는 것이다.

- @SessionAttribute는 컨트롤러 밖(인터셉터, 필터 등)에서 만들어진 세션을 가져올 수 있다.

  - 하지만  session에 데이터를 넣고 빼고 싶은 경우에는 HttpSession을 사용해야 한다.

  <br>

#### 사용자가 접속한 시간 세션 저장 구현

- `VisitTimeInterceptor`라는 이름의 인터셉터를 만든다.

```java
public class VisitTimeInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
      	// session에 "visitTime"값이 없으면 지금 시간으로 set한다.
        if (session.getAttribute("visitTime") == null) {
            session.setAttribute("visitTime", LocalDateTime.now());
        }
        return true;
    }
}
```

- `WebMvcConfiguration`에 등록을 해준다.

```java
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new VisitTimeInterceptor());
    }
}
```

- `@SessionAttribute`를 사용하여 데이터 띄워보기

```java
@GetMapping("/events/list")
public String getEvents(Model model, @SessionAttribute LocalDateTime visitTime) {
    System.out.println(visitTime);
  	// 새로고침을 해도 처음에 들어온 시간이 출력되는 것을 확인할 수 있다.

    // ... (이하 생략)
}
```

- `HttpSession`을 사용하여 데이터 띄워보기

```java
@GetMapping("/events/list")
public String getEvents(Model model, HttpSession httpSession) {
    LocalDateTime visitTime = (LocalDateTime) httpSession.getAttribute("visitTime");

    // ... (이하 생략)
}
```

> @SessionAttribute는 타입 컨버전을 자동으로 지원을 해주지만 HttpSession을 통하여 가져오게 되면 Object로 가져오기 때문에 위의 코드처럼 컨버전을 따로 해주어야하는 번거로움이 있을 수 있다.

<br>

### RedirectAttributes

> Spring Boot에서는 기본적으로 Model에 들어있는  Primitive type( 기본 자료형 ex_int,long... )의 데이터가 URI 쿼리 매개변수로 추가가 되지 않게끔 설정이 되어있다.

- Spring Boot의 설정을 바꾸어 URI 쿼리 매개변수에 추가시키기

  - `application.properties`에 설정 추가하기

    ```
    spring.mvc.ignore-default-model-on-redirect=false
    // 기본적으로 true값으로 추가되지 않게끔 설정되었음.
    ```

  - `Model`에 데이터 담기

    ```java
    @PostMapping("/events/form/limit")
    public String eventsFormLimitSubmit(@Validated @ModelAttribute Event event,
                                        BindingResult bindingResult,
                                        SessionStatus sessionStatus,
                                        Model model) {
      if(bindingResult.hasErrors()) {
        return "/events/form-limit";
      }
      sessionStatus.setComplete();
      
      // model에 name와 limit을 담아준다.
      model.addAttribute("name",event.getName());
      model.addAttribute("limit",event.getLimit());
      return "redirect:/events/list";
    }
    ```

  - URI 확인해보기

    <center><img width="372" alt="Screen Shot 2020-01-20 at 11 14 11 PM" src="https://user-images.githubusercontent.com/37801041/72733195-b4c5e980-3bda-11ea-888b-f316b714a8d8.png"></center>
<br>
  
- RedirectAttributes를 사용하여 추가하기

  - Spring Boot의 기본설정을 건드릴 필요가 없다.

  - redirect시 원하는 값만 전달할 수 있다.

  - RedirectAttributes를 사용해 데이터 담기 

    > RedirectAttributes로 데이터를 넘기려면 String으로 바뀔 수 있는 값만 담을 수 있다.

    ```java
    @PostMapping("/events/form/limit")
    public String eventsFormLimitSubmit(@Validated @ModelAttribute Event event,
                                        BindingResult bindingResult,
                                        SessionStatus sessionStatus,
                                        RedirectAttributes attributes) {
        if(bindingResult.hasErrors()) {
          return "/events/form-limit";
        }
        sessionStatus.setComplete();
      
      	// attributes에 name와 limit을 모두 넣어보았다.
      	// name이나 limit 중 원하는 값만 넣을수도 있다.
        attributes.addAttribute("name",event.getName());
        attributes.addAttribute("limit",event.getLimit());
        return "redirect:/events/list";
    }
    ```

    <br>

- redirect 요청 처리 시 쿼리 매개변수 다루기

  - @RequestParam

    ```java
    @GetMapping("/events/list")
    public String getEvents(@RequestParam String name,
                            @RequestParam Integer limit,
                            Model model,
                            @SessionAttribute LocalDateTime visitTime) {
        System.out.println(visitTime);
    
      	Event newEvent = new Event();
    	  newEvent.setName(name);
        newEvent.setLimit(limit);
    
        List<Event> eventList = new ArrayList<>();
        eventList.add(event);
    
        model.addAttribute(eventList);
    
        return "/events/list";
    }
    ```

  - @ModelAttirbute

    ```java
    @GetMapping("/events/list")
    public String getEvents(@ModelAttribute("newEvent") Event event,
                            Model model,
                            @SessionAttribute LocalDateTime visitTime) {
        System.out.println(visitTime);
    
        List<Event> eventList = new ArrayList<>();
        eventList.add(event);
    
        model.addAttribute(eventList);
    
        return "/events/list";
    }
    ```

    > @ModelAttribute로 받을 때 주의할 점
    >
    > - Controller에서 SessionAttributes로 지정해놓은 이름과 동일하게 지정을 하면 안된다.
    > - 동일할 경우 session에서 찾아오기 때문에 session이 비어있으면 에러가 발생한다. 비어있지 않다해도 URI를 통해 받는 값과 동일하지 않은 값이 올 수 있다.
    > - 따라서 위의 ("newEvent")처럼 다른 이름으로 지정을 해주거나 event의 이름을 바꾸면 된다.



### FlashAttribute

> RedirectAttributes와 비슷하지만 FlashAttribute는 요청을 보낼 때 session에 넣고 넘겨준다. 그리고 바로 다음 요청이 처리되면 session에서 지워진다. 따라서 일회성이다.

- 사용법

```java
@PostMapping("/events/form/limit")
public String eventsFormLimitSubmit(@Validated @ModelAttribute Event event,
                                    BindingResult bindingResult,
                                    SessionStatus sessionStatus,
                                    RedirectAttributes attributes) {
    if(bindingResult.hasErrors()) {
      return "/events/form-limit";
    }
    sessionStatus.setComplete();
  	// RedirectAttributes안에 addFlashAttribute를 사용한다.
    attributes.addFlashAttribute("newEvent",event);
    return "redirect:/events/list";
}
```

- `@ModelAttribute("newEvent") Event event`를 통하여 받아도 되지만 FlashAttr의 경우 자동으로  Model에 넣어준다. 

  ```java
  Event newEvent = (Event) model.asMap().get("newEvent");
  ```

  > Object로 넘어오기때문에 (Event)로 타입을 컨버전해주어야한다.

- `RedirectAttributes`는 String으로 바뀔 수 있는 값만 넣어줄 수 있었지만 `FlashAttribute`는 임의의 객체를 저장할 수도 있다.

- `RedirectAttributes`는 URI 쿼리 파라미터로 데이터를 담아 넘기지만 `FlashAttribute`는 session에 담아 넘기므로 URI에 데이터가 노출되지않는다.



## MultipartFile

> 파일 업로드를 할때 사용하는 핸들러 메소드 argument

<br>

- Spring MVC의 경우에는 MultipartResolver 빈이 설정되어있어야한다. 

  ```java
  // example MultipartResolver bean
  @Bean(name = "multipartResolver")
  public CommonsMultipartResolver multipartResolver() {
      CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
      multipartResolver.setMaxUploadSize(100000);
      return multipartResolver;
  }
  ```

- Spring Boot에서는 자동으로 설정이 된다. => 따라서  properties에서 최대 요청 가능 용량같은 옵션을 설정할 수 있다.

- POST요청을 보낼 때 `enctype="multipart/form-data"`가 있어야 파일을 참조할 수 있다.

- List<MultipartFile>을 사용해 여러 파일을 참조할 수 있다.

  <br>

  ### 파일 업로드 구현

- 파일 업로드 form 만들기

  - `files/index.html`

  ```html
  <form method="POST" enctype="multipart/form-data" action="#" th:action="@{/file}">
      File: <input type="file" name="file"/>
      <input type="submit" value="Upload"/>
  </form>
  ```

  - `FileController.java`

  ```java
  @Controller
  public class FileController {
  
      @GetMapping("/file")
      public String fileUploadForm() {
          return "files/index";
      }
    
  }
  ```

<br>

- POST로 오는 파일 업로드 요청 처리하기

  - `FileController.java`

  ```java
  @Controller
  public class FileController {
  
      @GetMapping("/file")
      public String fileUploadForm() {
          return "files/index";
      }
  
      @PostMapping("/file")
      public String fileUpload(@RequestParam MultipartFile file,
                               RedirectAttributes attributes) {
          // save...
        
          System.out.println(file.getName());		// getName : html에서 넘어오는 name ('file')
          System.out.println(file.getOriginalFilename());
        																				// getOriginalFilename : 원래 파일명
        
          String message = file.getOriginalFilename() + " is uploaded";
        	
        	// 파일 업로드가 되었다는 message를 'files/index.html'에서 띄워준다.
          attributes.addFlashAttribute("message", message);
          return "redirect:/file";
      }
  }
  ```

  - `message`를 뛰우기 위한 html 코드

  ```html
  <div th:if="${message}">
      <h2 th:text="${message}"/>
  </div>
  ```
  <br>

  ### Test 코드 만들기

  ```java
  @RunWith(SpringRunner.class)
  @SpringBootTest
  @AutoConfigureMockMvc
  public class FileControllerTest {
  
      @Autowired
      private MockMvc mockMvc;
  
      @Test
      public void fileUploadTest () throws Exception {
          MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "hello file".getBytes());
        // 인자값 : 넘겨줄때의 파일 이름, 원래 파일 이름, 파일 타입, 파일 데이터
  
          this.mockMvc.perform(multipart("/file").file(file))
                  .andDo(print())
                  .andExpect(status().is3xxRedirection());
      }
  }
  ```

  

## ResponseEntity

> ResponseEntity<T>는 View를 제공하지 않는 형태로 요청을 처리하고, 직접 결과 데이터 및 Http 상태 코드를 설정하여 응답을 할 수 있다. 이번 글에서는 이를 활용해서 파일 다운로드를 구현해보겠다.

<br>

- ResourceLoader 사용

  > 우선 다운로드 할 수 있게끔하려는 파일을 코드상에서 읽어와야한다. (이때 파일은 프로젝트의 `resources` 폴더에 넣어 주었다.) 여기서는 ResourceLoader를 사용하여 리소스를 읽어와서 다운로드가 가능하게끔 만들겠다.

  `Resource resource = resourceLoader.getResource("classpath:" + filename);`

<br>

- resource를 File로 저장하기

  > 뒤에서 나올 내용이지만 해당 파일의 길이를 헤더에 지정해주기 위해서 resource를 File로 읽어 저장해준다. 

  `File file = resource.getFile()`

<br>

- ResponseEntity 사용하기

  > ResponseEntity를 사용하기 위해서는 응답 상태 코드, 응답 헤더, 응답 본문을 설정해주어야한다.

  ```java
  @GetMapping("/file/{filename}")
      public ResponseEntity<Resource> fileDownload(@PathVariable String filename) throws IOException {
          Resource resource = resourceLoader.getResource("classpath:" + filename);
          File file = resource.getFile();
  				
        	// 파일의 mediaType를 알아내기 위한 api
          Tika tika = new Tika();
          String mediaType = tika.detect(file);
  
          return ResponseEntity.ok()	// ok 200 상태코드 설정
                  .header(HttpHeaders.CONTENT_DISPOSITION, "attachement;filename=\"" + resource.getFilename() + "\"")		// 파일이 다운로드 되어 저장될 이름
                  .header(HttpHeaders.CONTENT_TYPE, mediaType)  // 파일의 타입
                  .header(HttpHeaders.CONTENT_LENGTH, file.length() + "")		// 파일의 크기
                  .body(resource);		// 응답 본문
      }
  ```

  - ResponseEntity에서 `.`으로 이어가며 응답 상태코드, 헤더, 본문을 설정해준다.
  - 위에서 [Tika](http://tika.apache.org/)를 사용하였는데 mediaType를 알아내기 위한  api이다.
    - 이를 사용하려면 [Apache Tika Core](https://mvnrepository.com/artifact/org.apache.tika/tika-core)를 dependency로 추가를 해주어야한다.

<br>

- 서버 실행 후 파일 다운로드 요청 보내보기

  <center><img width="150" alt="Screen Shot 2020-01-23 at 5 38 20 PM" src="https://user-images.githubusercontent.com/37801041/72968670-5762b000-3e07-11ea-8d3a-e8196ed3198e.png"></center>
- 위와 같이 요청을 보내면 파일이 다운로드 된다. 단, test.jpeg라는 파일이 resources폴더에 존재해야한다.
  
<center><img width="150" alt="Screen Shot 2020-01-23 at 5 38 26 PM" src="https://user-images.githubusercontent.com/37801041/72968674-59c50a00-3e07-11ea-9414-d861e2eba297.png"></center>



## @RequestBody와 HttpEntity

<br>

- ### @RequestBody

  >@RequestBody는 요청 본문(body)에 json 또는 그 외의 형태로 데이터가 넘어올 때 그 데이터를 HttpMessageConverter를 통해 지정된 객체로 받아온다.

```java
@RestController
@RequestMapping("/api/events")
public class EventApi {

    @PostMapping
    public Event createEvent(@RequestBody @Valid Event event, BindingResult bindingResult) {
        // save event to DB
      	
      	// @Validated 또는 @Valid로 값을 검증할 수 있으며
      	// BindingResult를 사용해서 에러를 검출해낼 수 있다.
        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach(error -> {
                System.out.println(error);
            });
        }

        return event;
    }
}
```

<br>

- #### HttpMessageConverter

  - 만약 기본으로 등록되는 컨버터로 지원되지 않는 형식의 데이터라면 Spring MVC 설정인  WebMvcConfigurer를 통하여 설정을 할 수 있다.

  ```java
  @Configuration
  public class WebConfig implements WebMvcConfigurer {
  
      @Override
      public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
          // 기본으로 등록되는 컨버터들을 무시하고 여기서 등록하는 컨버터들이 대체가 됨.
      }
  
      @Override
      public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
          // 기본으로 등록되는 컨버터들에 추가로 등록
      }
  }
  ```

  - 기본 컨버터들이 명시된 곳.

    `WebMvcConfigurationSupport.addDefaultHttpMessageConverters`

<br>

- ### HttpEntity

  > - @RequestBody와 기능적으로 비슷하나 추가적으로 요청의 헤더 정보를 가져올 수 있다.
  >
  > - 앞에 원래  @RequestBody가 붙은 상태로 써야하나 생략이 가능하다.
  >
  > - @RequestBody와는 다르게 @Valid를 사용할 수 없다.

```java
@PostMapping
public Event createEvent(HttpEntity<Event> event) {
  // save event to DB

  System.out.println(event.getHeaders());		// Headers 정보를 가져온다.
  return event.getBody();		// body 내용을 가져온다.
}
```

<br>

- ### Test Code

```java
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class EventApiTest {

    @Autowired
    ObjectMapper objectMapper;	// Event객체를 json으로 변환하기 위한 객체

    @Autowired
    MockMvc mockMvc;

    @Test
    public void createEvent() throws Exception {
        Event event = new Event();
        event.setName("hooong");
        event.setLimit(20);

        String json = objectMapper.writeValueAsString(event);

        mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(json))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").value("hooong"))
                .andExpect(jsonPath("limit").value(20));
    }

}
```



## @ResponseBody와  ResponseEntity

<br>

- ### @ResponseBody

> @ResponseBody는 핸들러 메서드에 붙일 수 있는 애노테이션으로 HttpMessageConverter를 사용해 응답 본문(body) 메시지로 보낼 때 사용할 수 있다. 그러나 @RestController를 사용하면 그 Class안의 모든 메서드에  @ResponseBody가 자동으로 붙게된다.

<br>

```java
@Controller
@RequestMapping("/api/events")
public class EventApi {

    @PostMapping
  	@ResponseBody
    public Event createEvent(@RequestBody @Valid Event event, 
                                             BindingResult bindingResult) {
        // save event to DB
        if (bindingResult.hasErrors()){
						bindingResult.getAllErrors().forEach(error -> {
                System.out.println(error);
            });
        }
				
        return event;
    }
}
```

```java
// 위의 코드와 똑같은 동작을 하는 코드이다.

@RestController		// 자동으로 @ResponseBody가 붙는다
@RequestMapping("/api/events")
public class EventApi {

    @PostMapping
    public Event createEvent(@RequestBody @Valid Event event, 
                                             BindingResult bindingResult) {
        // save event to DB
        if (bindingResult.hasErrors()){
						bindingResult.getAllErrors().forEach(error -> {
                System.out.println(error);
            });
        }
				
        return event;
    }
}
```

​		<br>

- ### ResponseEntity

> 응답을 보낼 때 헤더 및 상태 코드를 직접적으로 다룰 때 사용할 수 있다.

```java
@RestController
@RequestMapping("/api/events")
public class EventApi {

    @PostMapping
    public ResponseEntity<Event> createEvent(@RequestBody @Valid Event event, BindingResult bindingResult) {
        // save event to DB
        if (bindingResult.hasErrors()){
          	// error가 있다면 BadRequest를 보낸다.
            return ResponseEntity.badRequest().build();
        }

				return ResponseEntity.ok(event);  // 상태코드 200으로 event를 응답본문에 담아 보낸다.
      
				// 아래 코드는 상태코드를 CREATED(201)로 설정하여 보낸다.      
        // return new ResponseEntity<Event>(event,HttpStatus.CREATED);
    }
}
```

<br>

- ### 참고

  > https://docs.spring.io/spring/docs/current/spring-framework-reference/web.html#mvc-ann-responsebody

<br>

## @ModelAttribute의 또 다른 사용법

```java
@ModelAttribute
public void categories(Model model) {
    model.addAttribute("categories",List.of("study", "seminar", "hobby", "social"));
}

// 또는
@ModelAttribute("categories")
public List<String> categories(Model model) {
  return List.of("study", "seminar", "hobby", "social");
}
```

- 위와 같이  `@ModelAttribute`를 사용하게되면 이 메서드를 가지는 컨트롤러 안의 모든 메서드에 model정보를 넘겨주게 된다.

- Petclinic의 예로보면 아래와 같다.

  ```java
  @Controller
  @RequestMapping("/owners/{ownerId}")
  class PetController {
    	
    	...
      
      @ModelAttribute("owner")
      public Owner findOwner(@PathVariable("ownerId") int ownerId) {
          return this.owners.findById(ownerId);
      }
    
    	...
        
  }
  ```

  - 위와 같이 PetController안에 "owner"라는 @ModelAttribute가 있어서 컨트롤러 안의 모든 핸들러에서 해당 owner의 모델 정보를 사용할 수 있게된다.

<br>

## @InitBinder

> 특정 컨트롤러에서 바인딩 또는 검증 설정을 변경하고 싶을 때 @InitBinder를 이용해 설정값을 지정할 수 있다.

<br>

- ### 사용법

  ```java
  @InitBinder
  public void initEventBinder(WebDataBinder webDataBinder) {
    ... (설정)
  }
  
  // 특정 모델 객체에만 적용을 하고 싶을 경우 아래와 같이 이름을 지정.
  @InitBinder("event")
  public void initEventBinder(WebDataBinder webDataBinder) {
    ... (설정)
  }
  ```

  <br>

- ### 바인딩 설정

  ```java
  @InitBinder
  public void initEventBinder(WebDataBinder webDataBinder) {
    	webDataBinder.setDisallowedFields("id");
    	// 또는
    	// webDataBinder.setAllowedFields();
  }
  ```

  - `setDisallowedFields()` : 지정한 값을 제외하고 바인딩을 한다.
  - `setAlloewdFields()` : 지정한 값들만 바인딩 한다.

  <br>

  ### 예)  `id`값을 바인딩 하고 안하기

  - 아무 설정을 하지 않고 id값을 받을 경우

    <img width="217" alt="Screen Shot 2020-02-04 at 10 03 04 PM" src="https://user-images.githubusercontent.com/37801041/73751775-1a9d9e00-47a3-11ea-9d40-033c505ba3f6.png">

    id값이 정상적으로 넘어오는 것을 확인할 수 있다.

  - `webDataBinder.setDisallowedFields("id");` 설정을 해줄 경우

    <img width="173" alt="Screen Shot 2020-02-04 at 10 03 41 PM" src="https://user-images.githubusercontent.com/37801041/73751784-1e312500-47a3-11ea-95ad-4c35b6f4ca36.png">

    Id값에  `null`값으로 바인딩이 되지 않는 것을 확인할 수 있다.

<br>

- ### Formatter 설정

  ```java
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private LocalDate startDate;
  ```

  - 위의 코드와 같이 포매터를 `Event`라는 클래스에서 startDate라는 포맷을 지정해줄 수도 있고
  - `webDataBinder.addCustomFormatter();`를 사용하여 설정해줄 수도 있다.

<br>

- ### Validator 설정

  - 첫번째 방법

    - `EventValidator`라는 클래스를 다음과 같이 만들어준다.

    ```java
    public class EventValidator implements Validator {
        @Override
        public boolean supports(Class<?> aClass) {
            return Event.class.isAssignableFrom(aClass);
        }
    
        @Override
        public void validate(Object o, Errors errors) {
            Event event = (Event)o;
          	// name값이 "aaa"이면 에러를 발생
            if (event.getName().equalsIgnoreCase("aaa")) {
                errors.rejectValue("name", "wrongValue","the value is not allowed");
            }
        }
    }
    ```

    - 컨트롤러의 @InitBinder에 `addValidators()`를 사용하여 validator를 추가해준다.

    ```java
    @InitBinder("event")
    public void initEventBinder(WebDataBinder webDataBinder) {
      webDataBinder.addValidators(new EventValidator());
    }
    ```

    <br>

  - 두번째 방법

    - `EventValidator`라는 클래스 @Component를 사용하여 빈으로 등록해준다.

    ```java
    @Component
    public class EventValidator {
    
        public void validate(Event event, Errors errors) {
            if (event.getName().equalsIgnoreCase("aaa")) {
                errors.rejectValue("name", "wrongValue","the value is not allowed");
            }
        }
    }
    ```

    - 컨트롤러에서 의존성 주입을 받고  특정시점에서 validator를 사용한다.

    ```java
    @Autowired
    EventValidator eventValidator;
    
     ...
       
    @PostMapping("/events/form/name")
    public String eventsFormNameSubmit(@Validated @ModelAttribute Event event,
                                          BindingResult bindingResult) {
       if(bindingResult.hasErrors()) {
         return "/events/form-name";
       }
       // validator 실행
       eventValidator.validate(event, bindingResult);
    
       return "redirect:/events/form/limit";
    }
    ```

  <br>

  - ### 실행의 예

    <center><img width="427" alt="Screen Shot 2020-02-04 at 10 12 22 PM" src="https://user-images.githubusercontent.com/37801041/73752725-cd223080-47a4-11ea-9c43-f7c50dfa0dac.png"></center