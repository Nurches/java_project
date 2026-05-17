package app.service;

import java.util.List;
import java.util.stream.Collectors;

import app.audit.AuditLogger;
import communication.Message;
import core.LogService;
import core.UniversitySystem;
import users.Employee;
import users.User;

public class MessageService {
    private final AuditLogger auditLogger;

    public MessageService(AuditLogger auditLogger) {
        this.auditLogger = auditLogger;
    }

    public Message send(User sender, String receiverLogin, String text) {
        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException("Message text cannot be empty");
        }
        User receiver = UniversitySystem.getInstance().findUserByLogin(receiverLogin);
        if (receiver == null) {
            throw new IllegalArgumentException("Receiver not found: " + receiverLogin);
        }
        if (sender.getId().equals(receiver.getId())) {
            throw new IllegalArgumentException("Cannot send a message to yourself");
        }
        Message message;
        if (sender instanceof Employee employee && receiver instanceof Employee receiverEmployee) {
            message = employee.sendMessage(receiverEmployee, text);
        } else {
            message = new Message(sender, receiver, text);
            UniversitySystem.getInstance().addMessage(message);
            new LogService().info(sender.getName() + " sent message to " + receiver.getName());
        }
        auditLogger.log(sender.getLogin(), "SEND_MESSAGE", "messages", "to=" + receiverLogin);
        return message;
    }

    public List<Message> inbox(User user) {
        return UniversitySystem.getInstance().getMessages().stream()
                .filter(m -> m.getReceiver().getId().equals(user.getId()))
                .collect(Collectors.toList());
    }

    public List<Message> sent(User user) {
        return UniversitySystem.getInstance().getMessages().stream()
                .filter(m -> m.getSender().getId().equals(user.getId()))
                .collect(Collectors.toList());
    }

    public void markRead(User user, int index) {
        List<Message> messages = inbox(user);
        if (index < 0 || index >= messages.size()) {
            throw new IllegalArgumentException("Invalid message index");
        }
        messages.get(index).markAsRead();
    }
}
