
public class Test
{
    static C c=new C();
    //flag用来标志子线程执行结束
    static boolean flag=false;

    public static void main(String []arg)
    {

        c.setvalue(12);
        System.out.println("子线程执行之前value的值是："+c.getvalue());
        System.out.println("执行子线程");


        Thread mythread = new MyThread(c);
        mythread.start();

        //等待子线程执行结束
        while(!flag);
        System.out.println("子线程执行之后value的值是："+c.getvalue());
    }

    public static void callback()
    {
        System.out.println("子线程执行结束");
        flag=true;
    }
}


class C
{
    private int value=0;
    public int getvalue()
    {
        return value;
    }
    public void setvalue(int v)
    {
        this.value=v;
    }
}



class MyThread extends Thread
{
    public MyThread(C cc)
    {
        this.cc=cc;
    }
    private C cc;
    @Override
    public void run()
    {
        cc.setvalue(20);
        Test.flag = true;
//        Test.callback();
    }
}