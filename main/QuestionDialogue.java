package main;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class QuestionDialogue {
    
    GamePanel gp;
    public String currentQuestion = "";
    public String correctAnswer = "";
    public boolean isQuestionActive = false;
    private JTextField answerField;
    private JDialog questionDialog;
    
    public QuestionDialogue(GamePanel gp) {
        this.gp = gp;
    }
    
    // Call this method when you want to ask a question
    public void askQuestion(String question, String answer) {
        this.currentQuestion = question;
        this.correctAnswer = answer.toLowerCase().trim(); // Make it case-insensitive
        this.isQuestionActive = true;
        
        showQuestionDialog();
    }
    
    // This creates a popup window for the player to type their answer
    private void showQuestionDialog() {
        // Stop the game while question is showing
        gp.gameState = gp.pauseState;
        
        // Create a dialog box (like a popup window)
        questionDialog = new JDialog();
        questionDialog.setTitle("Question!");
        questionDialog.setSize(400, 200);
        questionDialog.setLocationRelativeTo(gp);
        questionDialog.setModal(true); // Player MUST answer before continuing
        questionDialog.setLayout(new BorderLayout(10, 10));
        
        // Question text at the top
        JLabel questionLabel = new JLabel("<html><div style='text-align: center; padding: 10px;'>" 
                                          + currentQuestion + "</div></html>");
        questionLabel.setFont(new Font("Arial", Font.BOLD, 16));
        questionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Input box where player types answer
        answerField = new JTextField(20);
        answerField.setFont(new Font("Arial", Font.PLAIN, 14));
        
        // Submit button
        JButton submitButton = new JButton("Submit Answer");
        submitButton.setFont(new Font("Arial", Font.BOLD, 14));
        
        // What happens when player clicks submit
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkAnswer();
            }
        });
        
        // Also let player press ENTER to submit
        answerField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkAnswer();
            }
        });
        
        // Put everything in the dialog
        JPanel centerPanel = new JPanel();
        centerPanel.add(new JLabel("Your Answer: "));
        centerPanel.add(answerField);
        
        questionDialog.add(questionLabel, BorderLayout.NORTH);
        questionDialog.add(centerPanel, BorderLayout.CENTER);
        questionDialog.add(submitButton, BorderLayout.SOUTH);
        
        // Show the dialog
        questionDialog.setVisible(true);
    }
    
    // Check if the player's answer is correct
    private void checkAnswer() {
        String playerAnswer = answerField.getText().toLowerCase().trim();
        
        if (playerAnswer.equals(correctAnswer)) {
            // CORRECT! Show success message
            JOptionPane.showMessageDialog(questionDialog, 
                "Correct! You may proceed.", 
                "Success!", 
                JOptionPane.INFORMATION_MESSAGE);
            
            isQuestionActive = false;
            questionDialog.dispose(); // Close the question window
            gp.gameState = gp.playState; // Resume game
            
        } else {
            // WRONG! Lose 1 life
            gp.player.life -= 1;
            
            JOptionPane.showMessageDialog(questionDialog, 
                "Wrong answer! You lost 1 life.\nLives remaining: " + gp.player.life, 
                "Incorrect!", 
                JOptionPane.ERROR_MESSAGE);
            
            // Check if player is dead
            if (gp.player.life <= 0) {
                JOptionPane.showMessageDialog(questionDialog, 
                    "Game Over! You ran out of lives.", 
                    "Game Over", 
                    JOptionPane.ERROR_MESSAGE);
                
                isQuestionActive = false;
                questionDialog.dispose();
                // You can add game over logic here
                
            } else {
                // Let them try again
                answerField.setText("");
                answerField.requestFocus();
            }
        }
    }
}