package abc;

public class HelloWorld {
    public static void main(String[] args) {
//        System.out.println("Hello, World!");
//        Integer a = 3;
//        int c = 3;
//        boolean b = a.equals(3);
//        System.out.println(b);
//        boolean d = a.equals(c);
//        System.out.println(d);
//        System.out.println(a==c);
//        System.out.println(a==3);

        class People{
            String name;
            int phoneNumber;

            public People(String name, int phoneNumber) {
                this.name = name;
                this.phoneNumber = phoneNumber;
            }
        }
        People p1 = new People("Manish", 123);
        People p2 = new People("Manish" , 123);
        boolean e1 = p1==p2;
        boolean e2 = p1.equals(p2);
//        System.out.println(e1);
//        System.out.println(e2);
        String s = "ABC";
        String s2 = "ABCD";
        String s3 = s2.substring(0,3);
        System.out.println(s3);
        System.out.println(s);
        System.out.println(s3==s);
        System.out.println(s3.equals(s));

    }
}
