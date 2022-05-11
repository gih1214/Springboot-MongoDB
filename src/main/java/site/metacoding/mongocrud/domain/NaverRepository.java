package site.metacoding.mongocrud.domain;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface NaverRepository extends MongoRepository<Naver, String> {

    // 스프링부트에서 몽고디비에 있는 파이썬 데이터 select 하기
    @Query("{ title : ?0, company : ?1") // 숫자는 파라미터 순서
    List<Naver> mFindByTitleAndCompany(String title, String company);
}
