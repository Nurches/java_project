package users;

import communication.Message;
import enums.UserRole;

public abstract class Employee extends User {
    private static final long serialVersionUID = 1L;

    protected double salary;
    protected String department;

    protected Employee(String id, String login, String password, String name, String email, UserRole role, double salary,
            String department) {
        super(id, login, password, name, email, role);
        this.salary = salary;
        this.department = department;
    }

    public double getSalary() {
        return salary;
    }

    public String getDepartment() {
        return department;
    }

    public void sendMessage(Employee receiver, String text) {
        // TODO Integrate with central message service.
        Message message = new Message(this, receiver, text);
        System.out.println("Message sent: " + message.getText());
    }
}
