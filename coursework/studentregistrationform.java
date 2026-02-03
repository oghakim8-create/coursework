/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package studentregistrationform;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.Period;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/**
 *
 * @author Heifer
 */

    /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */



public class studentregistrationform extends JFrame implements ActionListener {

   // Form fields
    private JTextField firstNameField, lastNameField, emailField, confirmEmailField;
    private JPasswordField passwordField, confirmPasswordField;
    private JComboBox<String> yearCombo, monthCombo, dayCombo;
    private JRadioButton maleRadio, femaleRadio;
    private ButtonGroup genderGroup;
    private JRadioButton civilRadio, cseRadio, electricalRadio, encRadio, mechanicalRadio;
    private ButtonGroup deptGroup;
    private JTextArea displayArea;
    private JButton submitBtn, cancelBtn;

    // Error labels (shown in red below each relevant field)
    private JLabel errFirst, errLast, errEmail, errConfirmEmail, errPass, errConfirmPass,
                   errDob, errGender, errDept, errAge;

    // CSV file
    private static final String CSV_FILE = "students.csv";
    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    private static final Pattern PASS_LETTER = Pattern.compile(".*[a-zA-Z].*");
    private static final Pattern PASS_DIGIT  = Pattern.compile(".*\\d.*");

    public studentregistrationform() {
        super("New Student Registration Form");
        setSize(780, 680);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(null);
        mainPanel.setBackground(new Color(245, 245, 255));

        // Title
        JLabel title = new JLabel("New Student Registration Form");
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setBounds(180, 15, 400, 35);
        mainPanel.add(title);

        int y = 70;
        int labelX = 40, fieldX = 190, errX = 480, width = 270;

        // First Name
        addLabel(mainPanel, "Student First Name", labelX, y);
        firstNameField = new JTextField(); firstNameField.setBounds(fieldX, y, width, 26);
        errFirst = createErrorLabel(); errFirst.setBounds(errX, y, 260, 26);
        mainPanel.add(firstNameField); mainPanel.add(errFirst); y += 38;

        // Last Name
        addLabel(mainPanel, "Student Last Name", labelX, y);
        lastNameField = new JTextField(); lastNameField.setBounds(fieldX, y, width, 26);
        errLast = createErrorLabel(); errLast.setBounds(errX, y, 260, 26);
        mainPanel.add(lastNameField); mainPanel.add(errLast); y += 38;

        // Email
        addLabel(mainPanel, "Email Address", labelX, y);
        emailField = new JTextField(); emailField.setBounds(fieldX, y, width, 26);
        errEmail = createErrorLabel(); errEmail.setBounds(errX, y, 260, 26);
        mainPanel.add(emailField); mainPanel.add(errEmail); y += 38;

        // Confirm Email
        addLabel(mainPanel, "Confirm Email Address", labelX, y);
        confirmEmailField = new JTextField(); confirmEmailField.setBounds(fieldX, y, width, 26);
        errConfirmEmail = createErrorLabel(); errConfirmEmail.setBounds(errX, y, 260, 26);
        mainPanel.add(confirmEmailField); mainPanel.add(errConfirmEmail); y += 38;

        // Password
        addLabel(mainPanel, "Password", labelX, y);
        passwordField = new JPasswordField(); passwordField.setBounds(fieldX, y, width, 26);
        errPass = createErrorLabel(); errPass.setBounds(errX, y, 260, 26);
        mainPanel.add(passwordField); mainPanel.add(errPass); y += 38;

        // Confirm Password
        addLabel(mainPanel, "Confirm Password", labelX, y);
        confirmPasswordField = new JPasswordField(); confirmPasswordField.setBounds(fieldX, y, width, 26);
        errConfirmPass = createErrorLabel(); errConfirmPass.setBounds(errX, y, 260, 26);
        mainPanel.add(confirmPasswordField); mainPanel.add(errConfirmPass); y += 38;

        // Date of Birth
        addLabel(mainPanel, "Date of Birth", labelX, y);
        JPanel dobPanel = new JPanel(null);
        dobPanel.setBounds(fieldX, y, 400, 35); dobPanel.setOpaque(false);

        yearCombo  = new JComboBox<>(); yearCombo.setBounds(0, 5, 100, 26);
        monthCombo = new JComboBox<>(new String[]{"January","February","March","April","May","June",
                "July","August","September","October","November","December"});
        monthCombo.setBounds(110, 5, 120, 26);
        dayCombo   = new JComboBox<>(); dayCombo.setBounds(240, 5, 70, 26);

        for (int i = 1950; i <= 2010; i++) yearCombo.addItem(String.valueOf(i)); // reasonable range

        dobPanel.add(yearCombo); dobPanel.add(monthCombo); dobPanel.add(dayCombo);
        mainPanel.add(dobPanel);

        errDob = createErrorLabel(); errDob.setBounds(errX, y, 260, 26);
        mainPanel.add(errDob); y += 45;

        // Auto-update days when year or month changes
        ActionListener updateDays = e -> updateDayCombo();
        yearCombo.addActionListener(updateDays);
        monthCombo.addActionListener(updateDays);

        // Gender
        addLabel(mainPanel, "Gender", labelX, y);
        maleRadio   = new JRadioButton("Male");   maleRadio.setBounds(fieldX, y, 80, 26);
        femaleRadio = new JRadioButton("Female"); femaleRadio.setBounds(fieldX+90, y, 100, 26);
        genderGroup = new ButtonGroup(); genderGroup.add(maleRadio); genderGroup.add(femaleRadio);
        mainPanel.add(maleRadio); mainPanel.add(femaleRadio);

        errGender = createErrorLabel(); errGender.setBounds(errX, y, 260, 26);
        mainPanel.add(errGender); y += 40;

        // Department
        addLabel(mainPanel, "Department", labelX, y);
        civilRadio     = new JRadioButton("Civil");                          civilRadio.setBounds(fieldX, y, 100, 26); y+=28;
        cseRadio       = new JRadioButton("Computer Science and Engineering"); cseRadio.setBounds(fieldX, y, 260, 26); y+=28;
        electricalRadio= new JRadioButton("Electrical");                     electricalRadio.setBounds(fieldX, y, 100, 26); y+=28;
        encRadio       = new JRadioButton("Electronics and Communication");   encRadio.setBounds(fieldX, y, 220, 26); y+=28;
        mechanicalRadio= new JRadioButton("Mechanical");                     mechanicalRadio.setBounds(fieldX, y, 120, 26); y+=10;
        
// Buttons (Submit & Cancel)
submitBtn = new JButton("Submit");
submitBtn.setBounds(220, y + 20, 120, 35);
submitBtn.addActionListener(this);
mainPanel.add(submitBtn);

cancelBtn = new JButton("Cancel");
cancelBtn.setBounds(360, y + 20, 120, 35);
cancelBtn.addActionListener(this);
mainPanel.add(cancelBtn);

y += 80; // move cursor down after buttons

        deptGroup = new ButtonGroup();
        deptGroup.add(civilRadio); deptGroup.add(cseRadio); deptGroup.add(electricalRadio);
        deptGroup.add(encRadio); deptGroup.add(mechanicalRadio);

        mainPanel.add(civilRadio); mainPanel.add(cseRadio); mainPanel.add(electricalRadio);
        mainPanel.add(encRadio); mainPanel.add(mechanicalRadio);

        errDept = createErrorLabel(); errDept.setBounds(errX, y-110, 260, 26);
        mainPanel.add(errDept); y += 40;

        // Display area
        // ===== RIGHT SIDE DISPLAY AREA =====
int rightX = 500;
int rightY = 170;

JLabel resultLabel = new JLabel("Your Data is Below:");
resultLabel.setFont(new Font("Arial", Font.BOLD, 14));
resultLabel.setBounds(rightX, rightY, 200, 25);
mainPanel.add(resultLabel);

displayArea = new JTextArea();
displayArea.setEditable(false);
displayArea.setLineWrap(true);
displayArea.setWrapStyleWord(true);
displayArea.setFont(new Font("Monospaced", Font.PLAIN, 13));

JScrollPane scroll = new JScrollPane(displayArea);
scroll.setBounds(rightX, rightY + 30, 260, 220);
mainPanel.add(scroll);


        

        add(mainPanel);
        

        // Initial day combo fill (default month January)
       updateDayCombo();
        
        setVisible(true);
    }

    private void addLabel(JPanel p, String txt, int x, int y) {
        JLabel lbl = new JLabel(txt);
        lbl.setFont(new Font("Arial", Font.PLAIN, 14));
        lbl.setBounds(x, y, 140, 26);
        p.add(lbl);
    }

    private JLabel createErrorLabel() {
        JLabel lbl = new JLabel("");
        lbl.setForeground(Color.RED);
        lbl.setFont(new Font("Arial", Font.PLAIN, 12));
        return lbl;
    }

    private void updateDayCombo() {
        dayCombo.removeAllItems();
        if (yearCombo.getSelectedItem() == null || monthCombo.getSelectedIndex() < 0) {
            dayCombo.addItem("Select Day");
            return;
        }

        int year  = Integer.parseInt(yearCombo.getSelectedItem().toString());
        int month = monthCombo.getSelectedIndex() + 1;

        YearMonth ym = YearMonth.of(year, month);
        int days = ym.lengthOfMonth();

        for (int d = 1; d <= days; d++) {
            dayCombo.addItem(String.format("%02d", d));
        }
    }

    private int calculateAge(LocalDate dob) {
        return Period.between(dob, LocalDate.now()).getYears();
    }

    private String getNextId(int year) {
        int counter = 1;
        try (BufferedReader br = new BufferedReader(new FileReader(CSV_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith(year + "-")) {
                    String[] parts = line.split("\\|", 2);
                    String id = parts[0].trim();
                    int num = Integer.parseInt(id.substring(5));
                    if (num >= counter) counter = num + 1;
                }
            }
        } catch (Exception ignored) {}
        return String.format("%d-%05d", year, counter);
    }

    private void clearAllErrors() {
        JLabel[] errs = {errFirst, errLast, errEmail, errConfirmEmail, errPass, errConfirmPass,
                         errDob, errGender, errDept, errAge};
        for (JLabel e : errs) if (e != null) e.setText("");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == cancelBtn) {
            int choice = JOptionPane.showConfirmDialog(this, "Exit application?", "Confirm",
                    JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) System.exit(0);
            return;
        }

        // Submit
        clearAllErrors();

        StringBuilder errors = new StringBuilder();
        boolean valid = true;

        // 1. Required & trim
        String fname  = firstNameField.getText().trim();
        String lname  = lastNameField.getText().trim();
        String email  = emailField.getText().trim();
        String cemail = confirmEmailField.getText().trim();
        String pass   = new String(passwordField.getPassword()).trim();
        String cpass  = new String(confirmPasswordField.getPassword()).trim();

        if (fname.isEmpty())  { errFirst.setText("Required");  valid = false; }
        if (lname.isEmpty())  { errLast.setText("Required");   valid = false; }
        if (email.isEmpty())  { errEmail.setText("Required");  valid = false; }
        if (cemail.isEmpty()) { errConfirmEmail.setText("Required"); valid = false; }
        if (pass.isEmpty())   { errPass.setText("Required");   valid = false; }
        if (cpass.isEmpty())  { errConfirmPass.setText("Required"); valid = false; }

        // Email format & match
        if (!email.isEmpty() && !EMAIL_PATTERN.matcher(email).matches()) {
            errEmail.setText("Invalid email format"); valid = false;
        }
        if (!email.equals(cemail)) {
            errConfirmEmail.setText("Emails do not match"); valid = false;
        }

        // Password rules
        if (!pass.isEmpty()) {
            if (pass.length() < 8 || pass.length() > 20) {
                errPass.setText("8-20 characters required"); valid = false;
            } else if (!PASS_LETTER.matcher(pass).matches()) {
                errPass.setText("At least one letter"); valid = false;
            } else if (!PASS_DIGIT.matcher(pass).matches()) {
                errPass.setText("At least one digit"); valid = false;
            } else if (!pass.equals(cpass)) {
                errConfirmPass.setText("Passwords do not match"); valid = false;
            }
        }

        // DOB
        LocalDate dob = null;
        if (yearCombo.getSelectedItem() == null || monthCombo.getSelectedIndex() < 0 || dayCombo.getSelectedIndex() < 0) {
            errDob.setText("Select full date"); valid = false;
        } else {
            try {
                int y = Integer.parseInt(yearCombo.getSelectedItem().toString());
                int m = monthCombo.getSelectedIndex() + 1;
                int d = Integer.parseInt(dayCombo.getSelectedItem().toString());
                dob = LocalDate.of(y, m, d);
                int age = calculateAge(dob);
                if (age < 16 || age > 60) {
                    errDob.setText("Age must be 16 - 60 (" + age + ")"); valid = false;
                }
            } catch (Exception ex) {
                errDob.setText("Invalid date"); valid = false;
            }
        }

        // Gender
        String gender = null;
        if (maleRadio.isSelected())   gender = "M";
        else if (femaleRadio.isSelected()) gender = "F";
        if (gender == null) {
            errGender.setText("Select gender"); valid = false;
        }

        // Department
        String dept = null;
        if (civilRadio.isSelected())      dept = "Civil";
        else if (cseRadio.isSelected())   dept = "CSE";
        else if (electricalRadio.isSelected()) dept = "Electrical";
        else if (encRadio.isSelected())   dept = "E&C";
        else if (mechanicalRadio.isSelected()) dept = "Mechanical";
        if (dept == null) {
            errDept.setText("Select department"); valid = false;
        }

        if (!valid) {
            errors.append("Please correct the highlighted errors.");
            JOptionPane.showMessageDialog(this, errors.toString(), "Validation Failed",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // All valid â†’ generate ID & save
        int birthYear = dob.getYear();
        String id = getNextId(birthYear);

        String fullName = fname + " " + lname;
        String record = String.format("%s | %s | %s | %s | %s | %s",
                id, fullName, gender, dept,
                dob.format(DATE_FMT), email);

        // Append to CSV
        try (PrintWriter pw = new PrintWriter(new FileWriter(CSV_FILE, true))) {
            pw.println(record);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Failed to save: " + ex.getMessage(),
                    "File Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Show in display area
        displayArea.append(record + "\n");
        displayArea.setCaretPosition(displayArea.getDocument().getLength());

        JOptionPane.showMessageDialog(this, "Registration successful!\nID: " + id,
                "Success", JOptionPane.INFORMATION_MESSAGE);

        // Optional: clear form after success
        // firstNameField.setText(""); ... etc.
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(studentregistrationform::new);
    }
}
    
    


    

