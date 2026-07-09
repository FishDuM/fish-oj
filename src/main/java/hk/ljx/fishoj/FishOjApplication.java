package hk.ljx.fishoj;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("hk.ljx.fishoj.**.mapper")
public class FishOjApplication {

    public static void main(String[] args) {
        SpringApplication.run(FishOjApplication.class, args);
    }

}