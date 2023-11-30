package omok.model;// $Id: ServerUI.java,v 1.3 2018/04/06 21:32:56 cheon Exp $

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * A simple chat dialog.
 * @author Fernando Muñoz
 */
public class ServerUI extends JDialog {

    /** Default dimension of chat dialogs. */
    private final static Dimension DIMENSION = new Dimension(400, 400);
    private JButton connectButton;
    private PrintWriter out;
    private BufferedReader in;
    private JTextField hostNameField;
    private JTextField hostPortField;
    private JTextField opponentHostNameField;
    private JTextField opponentPortField;
    private JTextArea chatArea;
    private JButton disconnectButton;
    private JButton closeButton;
    private JTextField hostIPField;

    /**
     * Create a main dialog.
     */
    public ServerUI() {
        this(DIMENSION);
    }

    /**
     * Create a main dialog of the given dimension.  @param dim the dim
     */
    public ServerUI(Dimension dim) {
        super((JFrame) null, "JavaChat");
        initializeUI();
        setSize(dim);
        setLocationRelativeTo(null);
    }

    /** Configure UI of this dialog. */
    private void initializeUI() {
        setTitle("Omok");

        // Main panel with BoxLayout for vertical stacking
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        // Player section
        JLabel playerSectionLabel = new JLabel("Player");
        playerSectionLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Center align the label
        mainPanel.add(playerSectionLabel);

        JPanel playerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        playerPanel.add(new JLabel("Host name:"));
        hostNameField = new JTextField(15);
        playerPanel.add(hostNameField);
        playerPanel.add(new JLabel("IP address:")); // Label for IP address
        hostIPField = new JTextField(15); // Field for IP address
        hostIPField.setEditable(false); // Prevent editing the IP address
        playerPanel.add(hostIPField);
        playerPanel.add(new JLabel("Port number:"));
        hostPortField = new JTextField(5);
        playerPanel.add(hostPortField);
        mainPanel.add(playerPanel);

        JSeparator separator = new JSeparator();
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1)); // Make the separator span the entire width
        mainPanel.add(separator);


        // Opponent section
        JLabel opponentSectionLabel = new JLabel("Opponent");
        opponentSectionLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Center align the label
        mainPanel.add(opponentSectionLabel);

        JPanel opponentPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        opponentPanel.add(new JLabel("Hostname/IP:"));
        opponentHostNameField = new JTextField(15);
        opponentPanel.add(opponentHostNameField);
        opponentPanel.add(new JLabel("Port number:"));
        opponentPortField = new JTextField(5);
        opponentPanel.add(opponentPortField);
        mainPanel.add(opponentPanel);

        // Connect/Disconnect buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        connectButton = new JButton("Connect");
        connectButton.addActionListener(e -> connectClicked(e));
        buttonPanel.add(connectButton);
        disconnectButton = new JButton("Disconnect");
        buttonPanel.add(disconnectButton);
        disconnectButton.addActionListener(e -> disconnect(e));
        mainPanel.add(buttonPanel);


        // Chat area
        chatArea = new JTextArea(10, 30);
        chatArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(chatArea);
        mainPanel.add(scrollPane);
        chatArea.append("Server started on port 8000\n");
        // Close button
        closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dispose());
        mainPanel.add(closeButton);

        add(mainPanel);
        pack(); // Adjusts the window to fit the preferred size and layouts of its subcomponents

        populatePlayerDetails();

    }

    private void populatePlayerDetails() {
        try {
            InetAddress localHost = InetAddress.getLocalHost();
            hostNameField.setText(localHost.getHostName());
            hostNameField.setEditable(false);

            hostIPField.setText(localHost.getHostAddress()); // Display the IP address
            hostPortField.setText("8000"); // Example port number
            hostPortField.setEditable(false);
        } catch (UnknownHostException e) {
            e.printStackTrace();
            hostNameField.setText("Unknown Host");
            hostIPField.setText("Unknown IP");
            hostPortField.setText("");
        }
    }

    /** Callback to be called when the connect button is clicked. */
    private void connectClicked(ActionEvent event) {
        String address = opponentHostNameField.getText();
        int port = Integer.parseInt(opponentPortField.getText());
        new Thread(() -> {
            try {
                Socket socket = new Socket(address, port);
                out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                // Read messages from the server and append to the chat area
                String s;
                while ((s = in.readLine()) != null) {
                    final String receivedMsg = s;
                    SwingUtilities.invokeLater(() -> chatArea.append(receivedMsg + "\n"));
                }
            } catch (IOException e) {
                final String errorMessage = e.getMessage();
                SwingUtilities.invokeLater(() -> warn(errorMessage));
            }
        }).start();
    }

    private void disconnect(ActionEvent event) {
        String address = opponentHostNameField.getText();
        int port = Integer.parseInt(opponentPortField.getText());
        Socket socket;
        try {
            socket = new Socket(address, port);
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
            if (out != null) {
                out.close();
            }
            if (in != null) {
                in.close();
            }
        } catch (IOException e) {
            e.printStackTrace(); // Log the exception
        } finally {
            socket = null;
            out = null;
            in = null;
        }
        chatArea.append("Disconnected from the server.\n");
    }

    /** Show the given message in a dialog. */
    private void warn(String msg) {
        JOptionPane.showMessageDialog(this, msg, "JavaChat",
                JOptionPane.PLAIN_MESSAGE);
    }

}
