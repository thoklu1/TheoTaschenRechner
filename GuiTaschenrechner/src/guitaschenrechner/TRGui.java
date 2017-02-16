package guitaschenrechner;


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class TRGui
{
    private JFrame window;
    private Container contentPane;
    private JTextField input;
    private JTextField output;
    private String oldCalc;
    private boolean clearOnAction;
    private TR comp;

    public TRGui()
    {
        comp = new TR();
        window = new JFrame("TI-nspire CAS");
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setResizable(false);
        contentPane = window.getContentPane();
        createGUI();
        window.pack();
        window.setVisible(true);
    }
    
    private void createGUI()
    {        
        input = new JTextField();
        input.setEditable(false);
        contentPane.add(input);
        
        output = new JTextField();
        output.setEditable(false);
        contentPane.add(output);
        
        contentPane.add(new JButton("ANS"));
        contentPane.add(new JButton("("));
        contentPane.add(new JButton(")"));
        contentPane.add(new JButton("CE"));
        
        ((JButton) contentPane.getComponents()[5]).createToolTip().setTipText
            ("Press Ctrl to clear all");
        
        contentPane.add(new JButton("7"));
        contentPane.add(new JButton("8"));
        contentPane.add(new JButton("9"));
        contentPane.add(new JButton("+"));
        
        contentPane.add(new JButton("4"));
        contentPane.add(new JButton("5"));
        contentPane.add(new JButton("6"));
        contentPane.add(new JButton("-"));
        
        contentPane.add(new JButton("3"));
        contentPane.add(new JButton("2"));
        contentPane.add(new JButton("1"));
        contentPane.add(new JButton("*"));
        
        contentPane.add(new JButton("0"));
        contentPane.add(new JButton(","));
        contentPane.add(new JButton("="));
        contentPane.add(new JButton("/"));
        
        window.setLayout(new TRLayout());
        
        addActionListeners();
    }
    
    private void addActionListeners()
    {
        ActionListener generalListener = getGeneralListener();
        ActionListener clearListener = getClearListener();
        ActionListener commitListener = getCommitListener();
        
        Component[] components = contentPane.getComponents();
        for(int i = 2; i < components.length; i++) {
            JButton button = (JButton) components[i];
            
            if(button.getText().equals("=")) {
                button.addActionListener(commitListener);
            }
            button.addActionListener(!(button.getText().equals("CE")) ? 
                generalListener : clearListener);
        }
    }   
    
    private ActionListener getGeneralListener()
    {
        return new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if(clearOnAction) {
                        input.setText("");
                        clearOnAction = false;
                    }
                    input.setText(input.getText() + 
                        ((JButton) e.getSource()).getText());
                }};
    }

    private ActionListener getClearListener()
    {
        return new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if(!((e.getModifiers() & ActionEvent.CTRL_MASK) 
                            == ActionEvent.CTRL_MASK)) {
                        input.setText(input.getText().length() > 0 ? 
                            input.getText().substring(0, input.getText().length() - 1)
                             : "");
                    }
                    else {
                        input.setText("");
                    }
                }};
    }
    
    private ActionListener getCommitListener()
    {
        return new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    String calc = input.getText();
                    calc = calc.replace(".", ",");
                    calc = calc.replace("ANS", "(" + oldCalc + ")");
                    oldCalc = calc.replace("=", "");
                    input.setText(oldCalc);
                    String result = comp.rechne(calc);
                    output.setText(result == TR.FEHLER ? 
                       comp.gibFehlerBeschreibung() : result.replace(".", ","));
                    clearOnAction = true;
                }};
   }
}