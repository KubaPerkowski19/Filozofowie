import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.Semaphore;
public class Filozof extends Thread {
    static int variant;
    static int Philosoph_limit;
    static Semaphore [] Fork;
    Random gen_number;
    int Num;
    public Filozof ( int Num ) {
        this.Num = Num;
        this.gen_number = new Random(Num);
    }
    public static void setPhilosoph_limit(int ile){
        Philosoph_limit = ile;
    }
    public void run ( ) {
        if(variant == 1){
            System.out.println("Philosoph nr:" + Num +" is thinking");
            try {
                Thread.sleep((long) (7000 * Math.random()));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            //podniesienie lewego widelca przez filozofa
            Fork[Num].acquireUninterruptibly();
            //podniesienie prawego widelca przez filozofa
            Fork[(Num + 1) % Philosoph_limit].acquireUninterruptibly();

            System.out.println("Philosoph nr:" + Num +" is eating");
            try {
                Thread.sleep((long) (5000 * Math.random()));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Philosoph nr:" + Num + " stops eating");
            //puszczenie lewego widelca przez filozofa
            Fork[Num].release();
            //puszczenie prawego widelca przez filozofa
            Fork[(Num + 1) % Philosoph_limit].release();
        }else if (variant == 2){
            while (true) {
                System.out.println("Philosoph nr:" + Num +" is thinking");
                try {
                    Thread.sleep((long) (5000 * Math.random()));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                if (Num == 0) {
                    //podniesienie prawego widelca przez filozofa
                    Fork[(Num + 1) % Philosoph_limit].acquireUninterruptibly();
                    //podniesienie lewego widelca przez filozofa
                    Fork[Num].acquireUninterruptibly();
                } else {
                    //podniesienie lewego widelca przez filozofa
                    Fork[Num].acquireUninterruptibly();
                    //podniesienie prawego widelca przez filozofa
                    Fork[(Num + 1) % Philosoph_limit].acquireUninterruptibly();
                }

                System.out.println("Philosoph nr:" + Num +" is eating");
                try {
                    Thread.sleep((long) (3000 * Math.random()));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("Philosoph nr:" + Num + " stops eating");
                //puszczenie lewego widelca przez filozofa
                Fork[Num].release();
                //puszczenie prawego widelca przez filozofa
                Fork[(Num + 1) % Philosoph_limit].release();
            }
        }else if (variant == 3){
                System.out.println("Philosoph nr:" + Num +" is thinking");
                try {
                    Thread.sleep((long) (5000 * Math.random()));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                int strona = gen_number.nextInt(2);
                boolean podnioslDwaWidelce = false;
                do {
                    if (strona == 0) {
                        //podniesienie lewego widelca przez filozofa
                        Fork[Num].acquireUninterruptibly();
                        if (!(Fork[(Num + 1) % Philosoph_limit].tryAcquire())) {
                            //puszczenie lewego widelca przez filozofa
                            Fork[Num].release();
                        } else {
                            podnioslDwaWidelce = true;
                        }
                    } else {
                        //podniesienie prawego widelca przez filozofa
                        Fork[(Num + 1) % Philosoph_limit].acquireUninterruptibly();
                        if (!(Fork[Num].tryAcquire())) {
                            //puszczenie prawego widelca przez filozofa
                            Fork[(Num + 1) % Philosoph_limit].release();
                        } else {
                            podnioslDwaWidelce = true;
                        }
                    }
                } while (podnioslDwaWidelce == false);
                System.out.println("Philosoph nr:" + Num +" is eating");
                try {
                    Thread.sleep((long) (3000 * Math.random()));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("Philosoph nr:" + Num + " stops eating");
                //puszczenie lewego widelca przez filozofa
                Fork[Num].release();
                //puszczenie prawego widelca przez filozofa
                Fork[(Num + 1) % Philosoph_limit].release();
            }
        }
    public static void main ( String [] args ) {
        Scanner sc = new Scanner(System.in);
        do {
            System.out.println("1-two Philosophs is not holding any forks");
            System.out.println("2-For out of five Philosophs take left than right fork");
            System.out.println("3-Coin flip");
            variant =sc.nextInt();
        } while (variant <1 || variant >3);
        int Philosoph_amount = 0;
        do {
            System.out.println("Philosoph count? :");
            Philosoph_amount=sc.nextInt();
        } while (Philosoph_amount < 2 || Philosoph_amount > 100);
        setPhilosoph_limit(Philosoph_amount);
        Fork = new Semaphore[Philosoph_limit];
        for (int i = 0; i< Philosoph_limit; i++) {
            Fork[ i ]=new Semaphore ( 1 ) ;
        }
        for (int i = 0; i< Philosoph_limit; i++) {
            new Filozof(i).start();
        }
    }
}