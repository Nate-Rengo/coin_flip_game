import javax.swing.*;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.EventListener;

public class View{
    private JFrame jFrame = new JFrame();
    private JButton loginButton;
    private JButton logoutButton;
    private JButton createAccountButton;
    private JButton addCurrencyButton;
    private JTextField currencyFieldAccount;
    private JTextField usernameField;
    private JTextField passwordField;
    private JButton flipCoinButton;
    private JCheckBox heads;
    private JCheckBox tails;
    private JTabbedPane jTabs = new JTabbedPane();
    private JPanel jPanelPlayGame;
    private JPanel betAmount;
    private JLabel betlable;
    private JSlider betSlider;
    private  JButton rollDieButton;
    private JComboBox<String> comboBox;
    private JTextField currencyField;

    public View(){
        this.jTabs.add("login",this.createLogin());
        this.jTabs.add("Scoreboard",this.createScoreboard());
        this.jTabs.add("PLAY",this.playGame());
        this.jTabs.add("Account",this.account());
        this.jFrame.add(this.jTabs);
        this.jFrame.setSize(500, 500);
        this.jFrame.setLocationRelativeTo(null);
        this.jFrame.setVisible(true);
        jTabs.setEnabled(false);
        this.jFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    }
    public JPanel createLogin(){
        jFrame= new JFrame();
        JPanel loginPanel= new JPanel();
        loginPanel.setLayout(new GridLayout(4,2));
        loginButton= new JButton("Login");
        createAccountButton  = new JButton("Create Account");
        logoutButton= new JButton("EXIT");
        JLabel userNameLable=  new JLabel("Enter Username:");
        JLabel passwordFieldLable=  new JLabel("Enter Password:");
        usernameField = new JTextField(20);
        passwordField= new JTextField(20);
        loginPanel.add(userNameLable);
        loginPanel.add(usernameField);
        loginPanel.add(passwordFieldLable);
        loginPanel.add(passwordField);;
        loginPanel.add(loginButton);
        loginPanel.add(createAccountButton);
        loginPanel.add(logoutButton);
        logoutButton.setEnabled(true);
        return loginPanel;
    }
    public JPanel createScoreboard(){
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new GridLayout(3,1));
        jPanel.setVisible(true);
        jPanel.repaint();
        return jPanel;
    }
    public void updateScoreboard(String scoreboardString){
        JPanel scoreboardPanel = (JPanel) jTabs.getComponentAt(1);
        scoreboardPanel.removeAll();
        String[] update = scoreboardString.split(",");
        for (String part : update) {
            JLabel label = new JLabel(part);
            scoreboardPanel.add(label);
        }
        scoreboardPanel.revalidate();
        scoreboardPanel.repaint();
    }
    public JPanel playGame(){
        jPanelPlayGame = new JPanel();
        jPanelPlayGame.setLayout(new GridLayout(3,3) );
        String[] options = {"1", "2", "3", "4", "5", "6"};
        currencyField= new JTextField();
        comboBox = new JComboBox<>(options);
        comboBox.setSelectedIndex(-1);
        rollDieButton = new JButton("Roll 6:1");
        flipCoinButton= new JButton("Flip Coin 1:1");
        heads= new JCheckBox("Heads");
        tails= new JCheckBox("Tails");
        betAmount= new JPanel();
        betlable = new JLabel("$50");
        betSlider = new JSlider(JSlider.HORIZONTAL,50,1000,50);
        betSlider.setMajorTickSpacing(50);
        betSlider.setPaintTicks(true);
        betSlider.setSnapToTicks(true);
        jPanelPlayGame.add(flipCoinButton);
        jPanelPlayGame.add(heads);
        jPanelPlayGame.add(tails);
        jPanelPlayGame.add(betAmount);
        jPanelPlayGame.add(betSlider);
        jPanelPlayGame.add(betlable);
        jPanelPlayGame.add(comboBox);
        jPanelPlayGame.add(rollDieButton);
        jPanelPlayGame.add(currencyField);
        return jPanelPlayGame;
    }
    public JPanel account() {
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new GridLayout(2,2));
        JLabel currencyLable= new JLabel("Enter Amount:");
        currencyFieldAccount = new JTextField(20);
        addCurrencyButton= new JButton("Add Currency");
        jPanel.add(currencyLable);
        jPanel.add(currencyFieldAccount);
        jPanel.add(addCurrencyButton);
        return jPanel;
    }

    public JTextField getusernameField() {
        return usernameField;
    }
    public JFrame getjFrame() {
        return jFrame;
    }

    public void setloginButtonActionListener(ActionListener listen){
        loginButton.addActionListener(listen);
    }
    public void setFlipCoinButtonActionListener(ActionListener listen){
        flipCoinButton.addActionListener(listen);
    }
    public void setAddCurrencyButtonActionListener(ActionListener listen){
        addCurrencyButton.addActionListener(listen);
    }
    public void setlogoutButtonActionListener(ActionListener listen){
        logoutButton.addActionListener(listen);
    }
    public void setbetSliderChangeListener(ChangeListener c){
        betSlider.addChangeListener(c);
    }

    public void setcreateAccountButtonActionListener(ActionListener listen){
        createAccountButton.addActionListener(listen);
    }
    public void setrollDieButtonActionListener(ActionListener a){
        rollDieButton.addActionListener(a);
    }
    public void setheadsCheckBoxItemListener(ItemListener e){
        getHeads().addItemListener(e);
    }
    public void setTailsCheckBoxItemListener(ItemListener e){
        getTails().addItemListener(e);
    }
    public void addScoreboardTabChangeListener(ChangeListener listener) {
        jTabs.addChangeListener(listener);
    }
    public String getusernameFieldText(){
        return usernameField.getText();
    }
    public String getPasswordFieldText(){
        return passwordField.getText();
    }
    public JTextField getPassword() {
        return passwordField;
    }
    public JTabbedPane getjTabs() {
        return jTabs;
    }
    public JButton getLogoutButton() {
        return logoutButton;
    }
    public JTextField getPasswordField() {
        return passwordField;
    }
    public JTextField getCurrencyFieldAccount() {
        return currencyFieldAccount;
    }
    public JButton getCreateAccountButton() {
        return createAccountButton;
    }
    public JCheckBox getHeads() {
        return heads;
    }
    public JCheckBox getTails() {
        return tails;
    }
    public JSlider getBetSlider() {
        return betSlider;
    }
    public JLabel getBetlable() {
        return betlable;
    }
    public JComboBox<String> getComboBox() {
        return comboBox;
    }
    public JTextField getCurrencyField() {
        return currencyField;
    }

}
