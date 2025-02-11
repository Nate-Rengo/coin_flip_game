import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.rmi.ServerException;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Random;
public class Controller{
    private Boolean headsBool = false;
    private Boolean tailsBool = false;
    public static View view = new View();
    private PrintWriter writer;
    private BufferedReader reader;
    private String readFromServer;
    private String username;
    private String password;
    protected int userBetAmount;
    protected Socket socket;

    public Controller()  {
        view.setloginButtonActionListener(new loginButtonActionListener());
        view.setcreateAccountButtonActionListener(new createAccountButtonActionListener());
        view.setlogoutButtonActionListener(new logoutButtonActionListener());
        view.setAddCurrencyButtonActionListener(new addCurrencyButtonActionListener());
        view.setheadsCheckBoxItemListener(new headsTailsItemListener());
        view.setTailsCheckBoxItemListener(new TailsItemListener());
        view.setFlipCoinButtonActionListener(new flipCoinButtonActionListener());
        view.setbetSliderChangeListener(new betSliderChangeListener());
        view.setrollDieButtonActionListener(new rollDieButtonActionListener());
        view.addScoreboardTabChangeListener(new addScoreboardTabChangeListener());
        view.getjFrame().setDefaultCloseOperation(0);
        view.getjFrame().repaint();
        try {
            socket = new Socket("127.0.0.1", 5500);
            writer = new PrintWriter(socket.getOutputStream());

            InputStreamReader stream = new InputStreamReader(socket.getInputStream());
            reader = new BufferedReader(stream);
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
    public class loginButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            username= view.getusernameFieldText();
            password= view.getPasswordFieldText();

            if(!username.isEmpty()&&!password.isEmpty()){
                writer.println("LOGIN");
                writer.println(username);
                writer.println(password);
                writer.flush();
                try {
                    InputStreamReader stream = new InputStreamReader(socket.getInputStream());
                    reader = new BufferedReader(stream);
                    readFromServer = reader.readLine();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                while(true)
                {
                    if(Objects.equals(readFromServer, "TRUE"))
                    {
                        view.getjTabs().setEnabled(true);
                        view.getLogoutButton().setEnabled(true);
                        break;
                    }
                    else if (Objects.equals(readFromServer, "FAIL"))
                    {
                        JOptionPane.showMessageDialog(null,"Invalid Username and password.");
                        break;
                    }
                }

            }
            else{
                JFrame error= new JFrame();
                JOptionPane.showMessageDialog(error,"Please Enter Username and Password");
            }

            view.getusernameField().setText("");
            view.getPassword().setText("");
            view.getCreateAccountButton().setEnabled(true);
            String currencySet= Integer.toString(checkBalance());
            view.getCurrencyField().setText(currencySet);

        }
    }
    public class logoutButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            view.getjTabs().setEnabled(false);
            view.getLogoutButton().setEnabled(false);
            view.getCreateAccountButton().setEnabled(true);
            writer.println("LOGOUT");
            writer.println(username);
            writer.println(password);
            writer.flush();
            System.exit(0);
        }
    }
    public class createAccountButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            username = view.getusernameFieldText();
            password = view.getPasswordFieldText();

            if(!username.isEmpty()&&!password.isEmpty()){
                var yesOrNo = JOptionPane.showConfirmDialog(null, "Are you sure you want to create a new Account?");
                if (yesOrNo == 0) {
                    var usernameConfirm = JOptionPane.showInputDialog("Confirm username.");
                    if(usernameConfirm.equals(username)){
                        var passwordConfirm = JOptionPane.showInputDialog("Confirm Password.");
                        if(passwordConfirm.equals(password)){
                            JOptionPane.showMessageDialog(null,"Creating account");
                            writer.println("CREATE ACCOUNT");
                            writer.println(username);
                            writer.println(password);
                            writer.flush();
                            try {
                                InputStreamReader stream = new InputStreamReader(socket.getInputStream());
                                reader = new BufferedReader(stream);
                                readFromServer = reader.readLine();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            if(Objects.equals(readFromServer,"NAME EXISTS")){
                                JOptionPane.showMessageDialog(null,"USERNAME already exists, please choose a unique username!");
                            }
                            else {
                                JOptionPane.showMessageDialog(null, "Please login!");
                                JOptionPane.showMessageDialog(null, "After logging in, go to the Account tab to add currency!");
                            }

                        }
                        else{
                            JOptionPane.showMessageDialog(null,"Passwords do not match.");
                        }
                    }
                    else{
                        JOptionPane.showMessageDialog(null,"Usernames do not match.");
                    }

                }
                if (yesOrNo == 1) {
                    JOptionPane.showMessageDialog(null, "Canceling account creation.");
                }

            }
            else{
                JFrame error= new JFrame();
                JOptionPane.showMessageDialog(error,"Please Enter Username and Password");
            }
            view.getusernameField().setText("");
            view.getPasswordField().setText("");

        }
    }
    public class addCurrencyButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            boolean inputIsInteger = false;
            try {
                Integer.parseInt(view.getCurrencyFieldAccount().getText());
                inputIsInteger=true;
            }
            catch (NumberFormatException x) {
                inputIsInteger = false;
                JOptionPane.showMessageDialog(null,"Enter A valid currency deposit");

            }
            if(inputIsInteger){
                int currency = Integer.parseInt(view.getCurrencyFieldAccount().getText());
                writer.println("ADD CURRENCY");
                writer.println(username);
                writer.println(password);
                writer.println(currency);
                writer.flush();
                String currencySet= Integer.toString(checkBalance());
                view.getCurrencyField().setText(currencySet);

            }

            view.getCurrencyFieldAccount().setText("");
        }
    }
    public class headsTailsItemListener implements ItemListener {
        @Override
        public void itemStateChanged(ItemEvent e) {
            if(view.getTails().isSelected()){
                tailsBool=true;
            }
            else if(view.getHeads().isSelected()){
                headsBool=true;
            }

        }
    }
    public class TailsItemListener implements ItemListener {
        @Override
        public void itemStateChanged(ItemEvent event) {
            if(view.getTails().isSelected()){
                tailsBool=true;
            }
            else if(view.getHeads().isSelected()){
                headsBool=true;
            }

        }
    }
    public class addScoreboardTabChangeListener implements ChangeListener {
        @Override
        public void stateChanged(ChangeEvent w) {
            JTabbedPane sourceTabbedPane = (JTabbedPane) w.getSource();
            int index = sourceTabbedPane.getSelectedIndex();
            if (index == 1) {
                writer.println("SCOREBOARD");
                writer.flush();
                try {
                    InputStreamReader stream = new InputStreamReader(socket.getInputStream());
                    reader = new BufferedReader(stream);
                    readFromServer = reader.readLine();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                String scoreboardUpdate=readFromServer;
                if(!scoreboardUpdate.isEmpty()) {
                    view.updateScoreboard(scoreboardUpdate);
                }
                else{
                    JOptionPane.showMessageDialog(null,"Error please try again");
                }


            }
        }
    }
    public class flipCoinButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            if(userBetAmount>=50) {
                if (tailsBool && !headsBool) {
                    if(userBetAmount<=checkBalance()) {
                        writer.println("FLIP COIN");
                        writer.println(username);
                        writer.println(password);
                        writer.println(2);
                        writer.println(userBetAmount);
                        writer.flush();
                        try {
                            InputStreamReader stream = new InputStreamReader(socket.getInputStream());
                            reader = new BufferedReader(stream);
                            readFromServer = reader.readLine();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        if (Objects.equals(readFromServer, "WON")) {
                            JOptionPane.showMessageDialog(null, "You WON!!! :)");

                        } else if (Objects.equals(readFromServer, "DIDNOTWIN")) {
                            JOptionPane.showMessageDialog(null, "You Lost :(");
                        }
                    }
                    else{
                        JOptionPane.showMessageDialog(null, "Enter valid bet you have "+readFromServer+"  left");
                    }

                } else if (!tailsBool && headsBool) {
                    if(userBetAmount<=checkBalance()) {
                        writer.println("FLIP COIN");
                        writer.println(username);
                        writer.println(password);
                        writer.println(1);
                        writer.println(userBetAmount);
                        writer.flush();
                        try {
                            InputStreamReader stream = new InputStreamReader(socket.getInputStream());
                            reader = new BufferedReader(stream);
                            readFromServer = reader.readLine();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        if (Objects.equals(readFromServer, "WON")) {
                            JOptionPane.showMessageDialog(null, "You WON!!! :)");

                        } else if (Objects.equals(readFromServer, "DIDNOTWIN")) {
                            JOptionPane.showMessageDialog(null, "You Lost :(");
                            view.getTails().setSelected(false);
                            view.getHeads().setSelected(false);
                            headsBool = false;
                            tailsBool = false;
                        }
                    }
                    else{
                        JOptionPane.showMessageDialog(null, "Enter valid bet you have "+readFromServer+"  left");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Please Select heads or tails");

                }
            }
            else{
                JOptionPane.showMessageDialog(null, "Please Select A BET AMOUNT");
            }
            view.getTails().setSelected(false);
            view.getHeads().setSelected(false);
            String currencySet= Integer.toString(checkBalance());
            view.getCurrencyField().setText(currencySet);
            headsBool=false;
            tailsBool=false;
        }
    }
    public class betSliderChangeListener implements ChangeListener{
        @Override
        public void stateChanged(ChangeEvent e){
            int betAmount= view.getBetSlider().getValue();
            userBetAmount=betAmount;
            view.getBetlable().setText("Bet Amount: $"+betAmount);
        }
    }
    public class rollDieButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {

            if(userBetAmount>=50) {
                if (view.getComboBox().getSelectedIndex() != -1) {
                    int guess=view.getComboBox().getSelectedIndex()+1;
                    if(userBetAmount<=checkBalance()) {
                        writer.println("ROLL DIE");
                        writer.println(username);
                        writer.println(password);
                        writer.println(guess);
                        writer.println(userBetAmount);
                        writer.flush();
                        try {
                            InputStreamReader stream = new InputStreamReader(socket.getInputStream());
                            reader = new BufferedReader(stream);
                            readFromServer = reader.readLine();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        if (Objects.equals(readFromServer, "WON")) {
                            JOptionPane.showMessageDialog(null, "You WON!!! :)");

                        } else if (Objects.equals(readFromServer, "DIDNOTWIN")) {
                            JOptionPane.showMessageDialog(null, "You Lost :(");
                        }
                    }
                    else{
                        JOptionPane.showMessageDialog(null, "Enter valid bet you have "+readFromServer+"  left");
                    }

                } else {
                    JOptionPane.showMessageDialog(null, "Please select an option!");
                }
            }
            else{
                JOptionPane.showMessageDialog(null, "Please place bet!");
            }
            view.getComboBox().setSelectedIndex(-1);
            String currencySet= Integer.toString(checkBalance());
            view.getCurrencyField().setText(currencySet);

        }
    }
    public int checkBalance(){
        writer.println("CHECK");
        writer.println(username);
        writer.println(password);
        writer.flush();
        try {
            InputStreamReader stream = new InputStreamReader(socket.getInputStream());
            reader = new BufferedReader(stream);
            readFromServer = reader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        int balance= Integer.valueOf(readFromServer);
        return balance;
    }

}
