package me.hooong.demospring51;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.awt.print.Book;
import java.util.List;

@Service
public class BookService {

    @Autowired
    BookRepository myBookRepository;

    public void printBookRepository() {
        System.out.println(myBookRepository.getClass());
    }

    // 밑을 실행할때는 이미 빈이 initializing 된 상태임.
    @PostConstruct
    public void SetUp() {
        // 이거 실행하려면 runner가 필요없음. lifecycle을 확인해보셈.
        System.out.println(myBookRepository.getClass());
    }

//    @Autowired
//    List<BookRepository> bookRepositories;
//
//    public void printBookRepository() {
//        this.bookRepositories.forEach(System.out::println);
//    }

//    @Autowired
//    public void setBookRepository(BookRepository bookRepository) {
//        this.bookRepository = bookRepository;
//    }
//    @Autowired
//    public BookService(BookRepository bookRepository) {
//        this.bookRepository = bookRepository;
//    }
}
