package expert001_03;

public class Driver {
    public static void main(String[] args) {
        Tire tire = new AmericaTire();
        Car car = new Car();
        car.setTire(tire);

        System.out.println(car.getTireBrand());
    }
}
