package org.example.examples;

public class person {
    private int ID;

    public person(int ID) {
        this.ID = ID;
    }

    public int GETID(){
        return ID;
    }

    public void doSomethingElse(String messagse) {
        System.out.println("Doing something else..."+ messagse);
    }
}
