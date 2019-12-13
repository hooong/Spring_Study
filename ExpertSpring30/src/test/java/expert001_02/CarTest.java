package expert001_02;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CarTest {
    @Test
    public void 자동차_코리아타이어_장착_타이어브랜드_테스트() {
        Tire tire1 = new KoreanTire();
        Car car1 = new Car(tire1);

        assertEquals("장착된 타이어: 코리아 타이어", car1.getTireBrand());
    }

    @Test
    public void 자동차_미국타이어_장착_타이어브랜드_테스트() {
        Tire tire2 = new AmericaTire();
        Car car2 = new Car(tire2);

        assertEquals("장착된 타이어: 미국 타이어", car2.getTireBrand());
    }
}
