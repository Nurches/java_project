package app.service;

import java.util.List;
import java.util.stream.Collectors;

import app.audit.AuditLogger;
import communication.Message;
import core.UniversitySystem;
import users.Employee;
import users.User;

public class MessageService {
    private final AuditLogger auditLogger;

    public MessageService(AuditLogger auditLogger) {
        this.auditLogger = auditLogger;
    }

    public Message send(User sender, String receiverLogin, String text) {
        if (!(sender instanceof Employee employee)) {
            throw new SecurityException("Only employees can send messages");
        }
        User receiver = UniversitySystem.getInstance().findUserByLogin(receiverLogin);
        if (receiver == null) {
            throw new IllegalArgumentException("Receiver not found");
        }
        if (!(receiver instanceof Employee receiverEmployee)) {
            throw new IllegalArgumentException("Receiver must be an employee");
        }
        Message message = employee.sendMessage(receiverEmployee, text);
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
