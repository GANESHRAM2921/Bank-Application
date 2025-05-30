package GUIs;

import db_objs.User;

import javax.swing.*;

abstract public class BaseFrame extends JFrame {
    protected User user;
    public BaseFrame(String title,User user){
        this.user = user;
        initialize(title);
    }
    public BaseFrame(String title) {
        initialize(title);
    }

    private void initialize(String title) {
        setTitle(title);
        setSize(420, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);
        setResizable(false);
        setLocationRelativeTo(null);
        addGuiComponents();
    }

    protected abstract void addGuiComponents();
}
