import java.awt.*;
import java.util.*;
import javax.swing.*;

class Flashcard {
    private String question;
    private String answer;

    public Flashcard(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

    @Override 
    public String toString() {
        return "Q: " + question + "\nA: " + answer;
    }
}

public class FlashcardApp extends JFrame {
    private java.util.List<Flashcard> flashcards = new ArrayList<>();
    private JTextArea viewArea;
    private JTextArea quizArea;
    private Iterator<Flashcard> quizIterator;
    private Flashcard currentCard;
    private boolean showingAnswer = false;

    public FlashcardApp() {
        setTitle("Flashcard App");
        setSize(500, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Add", createAddPanel());
        tabs.addTab("View", createViewPanel());
        tabs.addTab("Quiz", createQuizPanel());

        add(tabs);
    }

    private JPanel createAddPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel inputPanel = new JPanel(new GridLayout(4, 1));

        JTextField questionField = new JTextField();
        JTextField answerField = new JTextField();
        JButton addButton = new JButton("Add Flashcard");

        inputPanel.add(new JLabel("Question:"));
        inputPanel.add(questionField);
        inputPanel.add(new JLabel("Answer:"));
        inputPanel.add(answerField);

        panel.add(inputPanel, BorderLayout.CENTER);
        panel.add(addButton, BorderLayout.SOUTH);

        addButton.addActionListener(e -> {
            String question = questionField.getText().trim();
            String answer = answerField.getText().trim();
            if (!question.isEmpty() && !answer.isEmpty()) {
                flashcards.add(new Flashcard(question, answer));
                questionField.setText("");
                answerField.setText("");
                JOptionPane.showMessageDialog(this, "Flashcard added!");
            }
        });

        return panel;
    }

    private JPanel createViewPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        viewArea = new JTextArea();
        viewArea.setEditable(false);
        panel.add(new JScrollPane(viewArea), BorderLayout.CENTER);

        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> {
            StringBuilder sb = new StringBuilder();
            for (Flashcard card : flashcards) {
                sb.append(card).append("\n\n");
            }
            viewArea.setText(sb.toString());
        });
        panel.add(refreshButton, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel createQuizPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        quizArea = new JTextArea();
        quizArea.setFont(new Font("Arial", Font.PLAIN, 16));
        quizArea.setEditable(false);
        panel.add(new JScrollPane(quizArea), BorderLayout.CENTER);

        JButton nextButton = new JButton("Next / Show Answer");
        panel.add(nextButton, BorderLayout.SOUTH);

        nextButton.addActionListener(e -> handleQuiz());
        return panel;
    }

    private void handleQuiz() {
        if (quizIterator == null || !quizIterator.hasNext()) {
            Collections.shuffle(flashcards);
            quizIterator = flashcards.iterator();
        }

        if (!showingAnswer) {
            if (quizIterator.hasNext()) {
                currentCard = quizIterator.next();
                quizArea.setText("Q: " + currentCard.getQuestion());
                showingAnswer = true;
            }
        } else {
            quizArea.setText("A: " + currentCard.getAnswer());
            showingAnswer = false;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FlashcardApp().setVisible(true));
    }
}