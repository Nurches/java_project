package app.cli.menu;

import java.util.List;

import app.cli.CliIO;
import app.cli.SessionContext;
import communication.Message;
import communication.News;
import users.Employee;
import users.Student;
import users.User;

public class CommonMenu {
    private final CliIO io;
    private final SessionContext session;

    public CommonMenu(CliIO io, SessionContext session) {
        this.io = io;
        this.session = session;
    }

    public void run() {
        boolean loop = true;
        while (loop) {
            int choice = io.chooseMenu("Common", List.of(
                    "Profile",
                    "News feed",
                    "Messages"));
            switch (choice) {
                case 1 -> showProfile();
                case 2 -> newsMenu();
                case 3 -> messagesMenu();
                case 0 -> loop = false;
                default -> io.println("Unknown option");
            }
        }
    }

    private void showProfile() {
        User u = session.currentUser();
        io.println("\n--- Profile ---");
        io.println("Login: " + u.getLogin());
        io.println("Name: " + u.getName());
        io.println("Email: " + u.getEmail());
        io.println("Role: " + u.getRole());
        if (u instanceof Student s) {
            io.println("Major: " + s.getMajor());
            io.println("Year: " + s.getYearOfStudy());
            io.println("GPA: " + String.format("%.2f", s.getGpa()));
            io.println("Credits: " + s.getCredits());
            io.println("Failed courses: " + s.getFailedCoursesCount());
            io.println("Researcher: " + s.isResearcherActive());
        }
        if (u instanceof Employee e) {
            io.println("Department: " + e.getDepartment());
            io.println("Salary: " + e.getSalary());
        }
        io.pause();
    }

    private void newsMenu() {
        List<News> news = session.services().newsService.getAll();
        if (news.isEmpty()) {
            io.println("No news.");
            io.pause();
            return;
        }
        for (int i = 0; i < news.size(); i++) {
            News n = news.get(i);
            io.println((i + 1) + ") [" + n.getTopic() + "] " + n.getTitle() + (n.isPinned() ? " (pinned)" : ""));
        }
        int idx = io.readInt("Open news # (0 back): ") - 1;
        if (idx < 0 || idx >= news.size()) {
            return;
        }
        News selected = news.get(idx);
        io.println("\n" + selected.getTitle());
        io.println(selected.getContent());
        io.println("Comments: " + selected.getComments().size());
        int action = io.chooseMenu("News", List.of("Add comment", "View comments"));
        if (action == 1) {
            String text = io.readLine("Comment: ");
            try {
                session.services().commentService.addComment(session.currentUser(), idx, text);
                io.println("Comment added.");
            } catch (Exception e) {
                io.println("Failed: " + e.getMessage());
            }
        } else if (action == 2) {
            selected.getComments().forEach(c -> io.println("- " + c.getAuthor().getName() + ": " + c.getText()));
        }
        io.pause();
    }

    private void messagesMenu() {
        int choice = io.chooseMenu("Messages", List.of("Inbox", "Sent", "Send message"));
        if (choice == 0) {
            return;
        }
        User user = session.currentUser();
        try {
            switch (choice) {
                case 1 -> showMessages(session.services().messageService.inbox(user), true);
                case 2 -> showMessages(session.services().messageService.sent(user), false);
                case 3 -> sendMessage();
                default -> {
                }
            }
        } catch (Exception e) {
            io.println("Failed: " + e.getMessage());
        }
    }

    private void showMessages(List<Message> messages, boolean inbox) {
        if (messages.isEmpty()) {
            io.println("No messages.");
            io.pause();
            return;
        }
        for (int i = 0; i < messages.size(); i++) {
            Message m = messages.get(i);
            String peer = inbox ? m.getSender().getName() : m.getReceiver().getName();
            io.println((i + 1) + ") " + peer + " | " + m.getStatus() + " | " + m.getText());
        }
        if (inbox) {
            int mark = io.readInt("Mark as read # (0 skip): ") - 1;
            if (mark >= 0 && mark < messages.size()) {
                messages.get(mark).markAsRead();
                io.println("Marked as read.");
            }
        }
        io.pause();
    }

    private void sendMessage() {
        if (!(session.currentUser() instanceof Employee)) {
            io.println("Only employees can send messages.");
            return;
        }
        String to = io.readLine("Receiver login: ");
        String text = io.readLine("Message: ");
        session.services().messageService.send(session.currentUser(), to, text);
        io.println("Message sent.");
        io.pause();
    }
}
