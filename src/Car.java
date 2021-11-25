import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;

public class Car implements Runnable {
    private static int CARS_COUNT;

    static {
        CARS_COUNT = 0;
    }

    private final CountDownLatch finishCountDown;

    private final Race race;
    private int speed;
    private String name;
    private final CountDownLatch startCountDown;
    public String getName() {
        return name;
    }

    public int getSpeed() {
        return speed;
    }

    public Car(Race race, int speed, CountDownLatch startCountDown, CountDownLatch finishCountDown) {
        this.race = race;
        this.speed = speed;
        this.startCountDown = startCountDown;
        CARS_COUNT++;
        this.name = "Участник #" + CARS_COUNT;
        this.finishCountDown = finishCountDown;
    }

    @Override
    public void run() {
        try {
            System.out.println(this.name + " готовится");
            Thread.sleep(500 + (int) (Math.random() * 800));
            System.out.println(this.name + " готов");
            startCountDown.countDown();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            synchronized (race){
                 race.wait();
            }
        }
        catch (InterruptedException exception){
            exception.printStackTrace();
        }
        for (int i = 0; i < race.getStages().size(); i++) {
            race.getStages().get(i).go(this);
        }
        finishCountDown.countDown();
    }
}
