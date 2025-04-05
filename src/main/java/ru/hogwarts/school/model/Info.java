package ru.hogwarts.school.model;

public class Info {

    private int port;

    public Info() {
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public String toString() {
        return "Info{" +
               "port=" + port +
               '}';
    }
}
