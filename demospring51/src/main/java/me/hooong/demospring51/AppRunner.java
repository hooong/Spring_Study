package me.hooong.demospring51;

import com.sun.source.tree.WhileLoopTree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class AppRunner implements ApplicationRunner {
    @Autowired
    ApplicationContext messageSource;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        while(true) {
            System.out.println(messageSource.getMessage("greeting", new String[]{"hooong"}, Locale.KOREA));
            System.out.println(messageSource.getMessage("greeting", new String[]{"hooong"}, Locale.getDefault()));
            Thread.sleep(1000l);
        }

    }
}
