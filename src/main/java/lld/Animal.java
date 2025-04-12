package lld;

class Animal {
    public void printAnimal() {
        System.out.print("I am from the Animal class\n");
    }
    void printAnimalTwo() {
        System.out.print("I am from the Animal class\n");
    }
}

class Lion extends Animal {
    // method overriding
    public void printAnimal() {
        System.out.print("I am from the Lion class\n");
    }
}

class Test {
    public static void modify(int a) {
        a = a + 10;
    }

    public static void main(String[] args) {
        Animal animal = new Lion();

        animal.printAnimal();
        animal.printAnimalTwo();
        int x = 5;
        Test.modify(x);
        System.out.println("Printing X");
        System.out.println(x);
    }


}


