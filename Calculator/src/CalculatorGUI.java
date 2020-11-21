import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class CalculatorGUI {
    JFrame frame;
    JTextArea numberDialog;

    /**
     * construct a calculator
     */
    public CalculatorGUI() {
        setUI();
        frame = new JFrame("Calculator");
        frame.addKeyListener(new keyBoardHandler());
        frame.setFocusable(true);
        JMenuBar menuBar = new JMenuBar();
        frame.setJMenuBar(menuBar);
        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic('F');
        menuBar.add(fileMenu);
        JMenuItem copyItem = new JMenuItem("Copy to clipboard");
        JMenuItem exitItem = new JMenuItem("Exit ");
        // if exit item is clicked than program will finish
        exitItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });
        // when user selects copy item the content of number dialog will be copied into system clipboard
        copyItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String dialogData = numberDialog.getText();
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(new StringSelection(dialogData), null);
                JOptionPane.showMessageDialog(frame, "Successfully copied to clipboard");
            }
        });
        exitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, ActionEvent.ALT_MASK));
        copyItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
        fileMenu.add(copyItem);
        fileMenu.add(exitItem);
        JPanel calculator = new JPanel(new BorderLayout());
        numberDialog = new JTextArea(6, 10);
//        numberDialog.addKeyListener(new keyBoardHandler());
        numberDialog.setEditable(false);
        numberDialog.setFocusable(false);
        numberDialog.setToolTipText("the operators and operands will be displayed here");
        JScrollPane display = new JScrollPane(numberDialog);
        calculator.add(display, BorderLayout.PAGE_START);
        JTabbedPane tabs = new JTabbedPane();
        tabs.add("Standard", standardCalculator());
        tabs.add("Scientific", scientificCalculator());
        calculator.add(tabs, BorderLayout.CENTER);
        frame.add(calculator);
        frame.setSize(350, 450);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    /**
     * creates a scientific calculator
     *
     * @returns the panel of calculator
     */
    public JPanel scientificCalculator() {
        JPanel scientificPanel = new JPanel(new BorderLayout());
        scientificPanel.setFocusable(false);
        JPanel keyboard = new JPanel();
        keyboard.setFocusable(false);
        keyboard.setLayout(new BoxLayout(keyboard, BoxLayout.LINE_AXIS));
        scientificPanel.add(keyboard, BorderLayout.CENTER);
        JPanel leftSideBtns = new JPanel(new GridLayout(5, 1));
        leftSideBtns.setFocusable(false);
        keyboard.add(leftSideBtns);
        JButton nBtn = new JButton("n");
        nBtn.setFocusable(false);
        JButton npBtn = new JButton("Np");
        npBtn.setFocusable(false);
        JButton nFactBtn = new JButton("n!");
        nFactBtn.setFocusable(false);
        JButton powBtn = new JButton("^");
        powBtn.setFocusable(false);
        JButton sqrtBtn = new JButton("sqrt");
        sqrtBtn.setFocusable(false);
        leftSideBtns.add(nBtn);
        leftSideBtns.add(npBtn);
        leftSideBtns.add(nFactBtn);
        leftSideBtns.add(powBtn);
        leftSideBtns.add(sqrtBtn);
        JPanel mainKeys = generateMainKeys();
        keyboard.add(mainKeys);
        JPanel basicOperators = generateBasicOperators();
        keyboard.add(basicOperators);
        JPanel functionsPanel = new JPanel(new GridLayout(5, 1));
        functionsPanel.setFocusable(false);
        keyboard.add(functionsPanel);
        JButton sinBtn = new JButton("sin");
        sinBtn.setFocusable(false);
        sinBtn.addActionListener(new FunctionBtnHandler());
        JButton tanBtn = new JButton("tan");
        tanBtn.setFocusable(false);
        tanBtn.addActionListener(new FunctionBtnHandler());
        JButton expBtn = new JButton("exp");
        expBtn.setFocusable(false);
        expBtn.addActionListener(new FunctionBtnHandler());
        JButton logBtn = new JButton("log");
        logBtn.setFocusable(false);
        logBtn.addActionListener(new FunctionBtnHandler());
        JButton shiftBtn = new JButton("shift");
        shiftBtn.setFocusable(false);
        /*
        replace sin with cos and cos with sin and tan with cot and cot with tan
        and reprints the replaced form in number dialog
         */
        shiftBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] txt = numberDialog.getText().split("\n");
                String lastLine = txt[txt.length - 1];
                if (lastLine.contains("sin"))
                    lastLine = lastLine.replaceAll("sin", "cos");
                else if (lastLine.contains("cos"))
                    lastLine = lastLine.replaceAll("cos", "sin");
                if (lastLine.contains("tan"))
                    lastLine = lastLine.replaceAll("tan", "cot");
                else if (lastLine.contains("cot"))
                    lastLine = lastLine.replaceAll("cot", "tan");
                numberDialog.setText("");
                txt[txt.length - 1] = lastLine;
                for (int i = 0; i < txt.length; i++) {
                    numberDialog.append(txt[i]);
                    if (!(i == txt.length - 1))
                        numberDialog.append("\n");
                }
            }
        });
        functionsPanel.add(sinBtn);
        functionsPanel.add(tanBtn);
        functionsPanel.add(expBtn);
        functionsPanel.add(logBtn);
        functionsPanel.add(shiftBtn);
        return scientificPanel;
    }

    /**
     * handles what should be printed when function buttons are clicked
     */
    private class FunctionBtnHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton funBtn = (JButton) e.getSource();
            String funName = funBtn.getText();
            numberDialog.append(funName + "(");
        }
    }

    /**
     * creates an standard calculator
     *
     * @return the panel of standatd calculator
     */
    public JPanel standardCalculator() {
        JPanel standardPanel = new JPanel(new BorderLayout());
        standardPanel.setFocusable(false);
        JPanel keyBoard = new JPanel();
        keyBoard.setFocusable(false);
        keyBoard.setLayout(new BoxLayout(keyBoard, BoxLayout.LINE_AXIS));
        standardPanel.add(keyBoard, BorderLayout.CENTER);
        JPanel mainKeys = generateMainKeys();
        keyBoard.add(mainKeys);
        JPanel basicOperators = generateBasicOperators();
        keyBoard.add(basicOperators);
        return standardPanel;
    }

    /**
     * generates the buttons of basic operators
     *
     * @return the panel of buttons
     */
    private JPanel generateBasicOperators() {
        JPanel basicOperators = new JPanel(new GridLayout(5, 1));
        basicOperators.setFocusable(false);
        JButton plusBtn = new JButton("+");
        plusBtn.setFocusable(false);
        plusBtn.addActionListener(new BtnHandler());
        JButton minusBtn = new JButton("-");
        minusBtn.setFocusable(false);
        minusBtn.addActionListener(new BtnHandler());
        JButton multiplyBtn = new JButton("*");
        multiplyBtn.setFocusable(false);
        multiplyBtn.addActionListener(new BtnHandler());
        JButton divideBtn = new JButton("/");
        divideBtn.setFocusable(false);
        divideBtn.addActionListener(new BtnHandler());
        JButton equalBtn = new JButton("=");
        equalBtn.setFocusable(false);
        equalBtn.addActionListener(new EqualActionHandler());
        basicOperators.add(plusBtn);
        basicOperators.add(minusBtn);
        basicOperators.add(multiplyBtn);
        basicOperators.add(divideBtn);
        basicOperators.add(equalBtn);
        return basicOperators;
    }

    /**
     * handles the operation needs to be done when user click on '=' button
     */
    private class EqualActionHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String[] lines = numberDialog.getText().split("\n");
            String lastLine = lines[lines.length - 1];
            String result = calculate(lastLine);
            numberDialog.append(String.format("\n%s", result));
        }
    }

    /**
     * creates the main keys of calculator
     *
     * @ returns the panel of keys
     */
    public JPanel generateMainKeys() {
        JPanel mainKeys = new JPanel(new GridLayout(5, 3));
        mainKeys.setFocusable(false);
        JButton cBtn = new JButton("C");
        cBtn.setFocusable(false);
        // clears the number dialog
        cBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                numberDialog.setText("");
            }
        });
        JButton CEBtn = new JButton("CE");
        CEBtn.setFocusable(false);
        // clears any new character added to the last line
        CEBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] text = numberDialog.getText().split("\n");
                String lastLineResult;
                if (text.length >= 2)
                    lastLineResult = calculate(text[text.length - 2]);
                else
                    lastLineResult = "";
                text[text.length - 1] = lastLineResult + "";
                numberDialog.setText("");
                for (int i = 0; i < text.length; i++) {
                    numberDialog.append(text[i]);
                    if (!(i == text.length - 1)) {
                        numberDialog.append("\n");
                    }
                }
            }
        });
        JButton percBtn = new JButton("%");
        percBtn.setFocusable(false);
        percBtn.addActionListener(new BtnHandler());
        mainKeys.add(cBtn);
        mainKeys.add(CEBtn);
        mainKeys.add(percBtn);
        for (int i = 9; i > 0; i--) {
            JButton numBtn = new JButton("" + i);
            numBtn.setFocusable(false);
            numBtn.setBackground(Color.CYAN);
            numBtn.addActionListener(new BtnHandler());
            mainKeys.add(numBtn);
        }
        JPanel parenthesisPanel = new JPanel(new GridLayout(1, 2));
        parenthesisPanel.setFocusable(false);
        JButton parenthesisBtn1 = new JButton("(");
        parenthesisBtn1.setFocusable(false);
        parenthesisBtn1.addActionListener(new BtnHandler());
        JButton parenthesisBtn2 = new JButton(")");
        parenthesisBtn2.setFocusable(false);
        parenthesisBtn2.addActionListener(new BtnHandler());
        parenthesisPanel.add(parenthesisBtn1);
        parenthesisPanel.add(parenthesisBtn2);
        JButton zeroBtn = new JButton("0");
        zeroBtn.setFocusable(false);
        zeroBtn.addActionListener(new BtnHandler());
        zeroBtn.setBackground(Color.cyan);
        JButton dotBtn = new JButton(".");
        dotBtn.setFocusable(false);
        dotBtn.addActionListener(new BtnHandler());
        mainKeys.add(parenthesisPanel);
        mainKeys.add(zeroBtn);
        mainKeys.add(dotBtn);
        return mainKeys;
    }

    /**
     * handles the button user click on GUI and adds proper character to the number dialog
     */
    private class BtnHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton btn = (JButton) e.getSource();
            String number = btn.getText();
            numberDialog.append(number);
        }
    }

    /**
     * handles the keys user enters and filter them
     */
    private class keyBoardHandler extends KeyAdapter {
        private char[] allowedChar = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '%', '*', '(', ')', '-', '+', '/', '.'};

        @Override
        public void keyPressed(KeyEvent e) {
            for (char character : allowedChar) {
                if (e.getKeyChar() == character)
                    numberDialog.append(Character.toString(e.getKeyChar()));
                else if (e.getKeyChar() == '=') {
                    new JButton("=") {
                        {
                            addActionListener(new EqualActionHandler());
                            doClick();
                        }
                    };
                    break;
                }
            }
            System.out.println(e.getKeyCode());
        }
    }

    /**
     * calculates the equation
     *
     * @param lastLine the line containing operators and operands
     * @return calculated answer as string
     */
    private String calculate(String lastLine) {
        /*
        converts all functions to Math.functions for the script generator
         */
        if (lastLine.contains("sin"))
            lastLine = lastLine.replaceAll("sin", "Math.sin");
        if (lastLine.contains("cos"))
            lastLine = lastLine.replaceAll("cos", "Math.cos");
        if (lastLine.contains("tan"))
            lastLine = lastLine.replaceAll("tan", "Math.tan");
        if (lastLine.contains("cot"))
            lastLine = lastLine.replaceAll("cot", "Math.cot");
        if (lastLine.contains("exp"))
            lastLine = lastLine.replaceAll("exp", "Math.exp");
        if (lastLine.contains("log"))
            lastLine = lastLine.replaceAll("log", "Math.log");
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("JavaScript");
        String result = null;
        try {
            result = engine.eval(lastLine).toString();
        } catch (Exception e) {
            return "error";
        }
        return result;
    }

    /**
     * sets the look and feel to nimbus
     */
    private void setUI() {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // If Nimbus is not available, you can set the GUI to another look and feel.
        }
    }
}
