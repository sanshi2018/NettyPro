public class staticInmutiThread implements Runnable{

    private  int num   = 0;
    @Override
    public void run() {
        num =3;
        System.out.println("当前线程："+Thread.currentThread().getName()+" num:"+num);
        num = 5;
        System.out.println("当前线程："+Thread.currentThread().getName()+" num:"+num*2);
    }

    public static void main(String[] args) {
        staticInmutiThread staticInmutiThread = new staticInmutiThread();
        //开启50个线程跑run方法
        for (int i = 0; i < 100; i++) {
            new Thread(staticInmutiThread,"Thread-"+i).start();
        }
    }
}
