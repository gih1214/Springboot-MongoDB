package site.metacoding.mongocrud.web;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import site.metacoding.mongocrud.domain.Naver;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT) // 통합테스트(포트 남는거 랜덤으로 지정하기)
public class NaverApiControllerTest {

    @Autowired // DI 어노테이션 - test할 때 사용하기
    private TestRestTemplate rt; // http 통신하는 거

    private static HttpHeaders headers;

    // assertNotNull(rt); // null 이 아니면 true -> beforeall에는 검증 코드를 넣으면 안 된다.
    @BeforeAll // 모든 애들이 실행되는 최초에 실행됨
    public static void init() { // beforall은 static 붙여줘야 함
        headers = new HttpHeaders(); // DI가 안 됐으니 new 해줘야 하는데 init에서 한번만 할거임
        headers.setContentType(MediaType.APPLICATION_JSON);
    }

    @Test
    public void save_테스트() throws JsonProcessingException {
        // given - 내가 데이터를 만든거
        Naver naver = new Naver();
        naver.setTitle("스프링 1강");
        naver.setCompany("잠와요");

        ObjectMapper om = new ObjectMapper();
        String content = om.writeValueAsString(naver); // 자바오브젝트 -> 제이슨으로 변경
        // 제이슨 -> 자바오브젝트로 변경하는 것은?

        HttpEntity<String> httpEntity = new HttpEntity<>(content, headers);

        // when(실행) - 이걸 할 때
        ResponseEntity<String> response = rt.exchange("/navers", HttpMethod.POST, httpEntity, String.class);

        // then(검증) - 어떤 결과를 원하느냐
        // System.out.println("==========================================");
        // System.out.println(response.getBody());
        // System.out.println(response.getHeaders());
        // System.out.println(response.getStatusCode());
        // System.out.println(response.getStatusCode().is2xxSuccessful());
        // System.out.println("==========================================");
        // assertTrue(response.getStatusCode().is2xxSuccessful());
        // assertFalse(response.getStatusCode().is2xxSuccessful());

        // json 파서로 restapicontroller 테스트
        DocumentContext dc = JsonPath.parse(response.getBody());
        // System.out.println(dc.jsonString());
        String title = dc.read("$.title");
        // System.out.println(title);
        assertEquals("스프링 1강", title);
    }

    @Test
    public void findAll_테스트() {
        // given - get은 줄게 없다.

        // when(실행) - 이걸 할 때
        ResponseEntity<String> response = rt.exchange("/navers", HttpMethod.GET, null, String.class);

        // then
        // System.out.println(response.getBody());
        // DocumentContext dc = JsonPath.parse(response.getBody());
        // String title = dc.read("$.[0].title");
        // assertEquals("지방선거 6.1 곧 다가온다.", title);
        assertTrue(response.getStatusCode().is2xxSuccessful());
    }
}
