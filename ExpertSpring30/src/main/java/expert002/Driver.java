package expert002;

import org.springframework.context.Applicationcontext;


public class Driver {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("expert002/expert002.xml");

        Car car = context.getBean("car", Car.class);

        Tire tire = context.getBean("tire", Tire.class);

        car.setTire(tire);

        System.out.println(car.getTireBrand());
    }
}
