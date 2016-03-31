package com.example.tictactoe;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class TTTActivity extends ActionBarActivity {

    // TAG for logging
    private static final String TAG = "TTTActivity";

    // server to connect to
    protected static final int GROUPCAST_PORT = 20000;
    protected static final String GROUPCAST_SERVER = "csx283.hopto.org";

    // networking
    Socket socket = null;
    BufferedReader in = null;
    PrintWriter out = null;
    boolean connected = false;

    // UI elements
    Button board[][] = new Button[3][3];
    Button bConnect = null;
    Button bList = null;
    Button bJoin = null;
    Button bCreate = null;
    Button bListUsr = null;
    Button bListGpUsr = null;
    Button bListMyGp = null;
    Button bPlayTTT = null;
    Button bSymbolO = null;
    Button bSymbolX = null;
    Button bSendMsg = null;
    Button bBack = null;
    EditText etName = null;
    EditText etGroupName = null;
    EditText etPlayTTT = null;
    EditText etUserName = null;
    EditText etChatMsg = null;
    TextView tvGroups = null;
    TextView tvUsers = null;
    TextView tvChooseYourSymbol = null;
    TextView tvChatRoom = null;
    TextView tvPlayStatus = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ttt);

        // find UI elements defined in xml
        bConnect = (Button) this.findViewById(R.id.bConnect);
        bList = (Button) this.findViewById(R.id.listGP);
        bJoin = (Button) this.findViewById(R.id.joinGP);
        bCreate = (Button) this.findViewById(R.id.createGP);
        bListUsr = (Button) this.findViewById(R.id.listUsr);
        bListGpUsr = (Button) this.findViewById(R.id.listGpUsr);
        bListMyGp = (Button) this.findViewById(R.id.listMyGp);
        bPlayTTT = (Button) this.findViewById(R.id.playTTT);
        bSymbolO = (Button) this.findViewById(R.id.symbolO);
        bSymbolX = (Button) this.findViewById(R.id.symbolX);
        tvChooseYourSymbol = (TextView) this.findViewById(R.id.chooseYourSymbol);
        bSendMsg = (Button) this.findViewById(R.id.sendMsg);
        tvUsers = (TextView) this.findViewById(R.id.tvUsers);
        etName = (EditText) this.findViewById(R.id.etName);
        etUserName = (EditText) this.findViewById(R.id.etUserName);
        tvGroups = (TextView) this.findViewById(R.id.tvGroups);
        etGroupName = (EditText) this.findViewById(R.id.etGroupName);
        etPlayTTT = (EditText) this.findViewById(R.id.etPlayTTT);
        etChatMsg = (EditText) this.findViewById(R.id.chatMsg);
        tvChatRoom = (TextView) this.findViewById(R.id.playChatRoom);
        tvPlayStatus = (TextView) this.findViewById(R.id.playStatus);
        bBack = (Button) this.findViewById(R.id.backGUP);
        board[0][0] = (Button) this.findViewById(R.id.b00);
        board[0][1] = (Button) this.findViewById(R.id.b01);
        board[0][2] = (Button) this.findViewById(R.id.b02);
        board[1][0] = (Button) this.findViewById(R.id.b10);
        board[1][1] = (Button) this.findViewById(R.id.b11);
        board[1][2] = (Button) this.findViewById(R.id.b12);
        board[2][0] = (Button) this.findViewById(R.id.b20);
        board[2][1] = (Button) this.findViewById(R.id.b21);
        board[2][2] = (Button) this.findViewById(R.id.b22);

        // hide login controls
        hideLoginControls();

        // make the board non-clickable
        disableBoardClick();

        // hide the board
        hideBoard();

        // hide the groups
        hideGroups();

        // hide the users
        hideUsers();

        // hide the playTTT
        hidePlayTTT();

        // hide the playStatus
        hidePlayStatus();

        // assign OnClickListener to connect button
        bConnect.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String name = etName.getText().toString();
                // sanitity check: make sure that the name does not start with an @ character
                if (name.equals("") || name.startsWith("@")) {
                    Toast.makeText(getApplicationContext(), "Invalid name",
                            Toast.LENGTH_SHORT).show();
                } else {
                    send("NAME,"+etName.getText());
                }
            }
        });

        bList.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                send("LIST,GROUPS");
            }
        });

        bJoin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String groupName = etGroupName.getText().toString();
                if (groupName.equals("")) {
                    Toast.makeText(getApplicationContext(), "Invalid group name",
                            Toast.LENGTH_SHORT).show();
                } else {
                    send("JOIN,@"+etGroupName.getText());
                }
            }
        });

        bCreate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String groupName = etGroupName.getText().toString();
                if (groupName.equals("")) {
                    Toast.makeText(getApplicationContext(), "Invalid group name",
                            Toast.LENGTH_SHORT).show();
                } else {
                    send("JOIN,@"+etGroupName.getText());
                }
            }
        });

        bListUsr.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                send("LIST,USERS");
            }
        });

        bListGpUsr.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View V) {
                String groupName = etUserName.getText().toString();
                if (groupName.equals("")) {
                    Toast.makeText(getApplicationContext(), "Invalid group name",
                            Toast.LENGTH_SHORT).show();
                } else {
                    send("LIST,USERS,@" + etUserName.getText());
                }
            }
        });

        bListMyGp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View V) {
                send("LIST,MYGROUPS");
            }
        });

        bPlayTTT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String playTTT = etPlayTTT.getText().toString();
                if (playTTT.equals("")) {
                    Toast.makeText(getApplicationContext(), "Invalid group name",
                            Toast.LENGTH_SHORT).show();
                } else {
                    hideUsers();
                    hideGroups();
                    hidePlayTTT();
                    enableBoardClick();
                    showBoard();
                    showPlayStatus();
                }
            }
        });

        bSymbolO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvChooseYourSymbol.setText("O");
            }
        });

        bSymbolX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvChooseYourSymbol.setText("X");
            }
        });

        bSendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String playTTT = etPlayTTT.getText().toString();
                String chatMsg = etChatMsg.getText().toString();
                send("MSG,@" + playTTT + "," + chatMsg);
            }
        });

        bBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableBoardClick();
                hidePlayStatus();
                hideBoard();
                showUsers();
                showGroups();
                showPlayTTT();
            }
        });


        // assign a common OnClickListener to all board buttons
        View.OnClickListener boardClickListener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int x, y;
                String playTTT = etPlayTTT.getText().toString();
                String bBoardText = tvChooseYourSymbol.getText().toString();

                switch (v.getId()) {
                    case R.id.b00:
                        x = 0;
                        y = 0;
                        send("MSG,@" + playTTT + "," + bBoardText + "00");
                        board[0][0].setEnabled(false);
                        board[0][0].setTextSize(50);
                        board[0][0].setText(bBoardText);
                        break;
                    case R.id.b01:
                        x = 0;
                        y = 1;
                        send("MSG,@" + playTTT + "," + bBoardText + "01");
                        board[0][1].setEnabled(false);
                        board[0][1].setTextSize(50);
                        board[0][1].setText(bBoardText);
                        break;
                    case R.id.b02:
                        x = 0;
                        y = 2;
                        send("MSG,@" + playTTT + "," + bBoardText + "02");
                        board[0][2].setEnabled(false);
                        board[0][2].setTextSize(50);
                        board[0][2].setText(bBoardText);
                        break;
                    case R.id.b10:
                        x = 1;
                        y = 0;
                        send("MSG,@" + playTTT + "," + bBoardText + "10");
                        board[1][0].setEnabled(false);
                        board[1][0].setTextSize(50);
                        board[1][0].setText(bBoardText);
                        break;
                    case R.id.b11:
                        x = 1;
                        y = 1;
                        send("MSG,@" + playTTT + "," + bBoardText + "11");
                        board[1][1].setEnabled(false);
                        board[1][1].setTextSize(50);
                        board[1][1].setText(bBoardText);
                        break;
                    case R.id.b12:
                        x = 1;
                        y = 2;
                        send("MSG,@" + playTTT + "," + bBoardText + "12");
                        board[1][2].setEnabled(false);
                        board[1][2].setTextSize(50);
                        board[1][2].setText(bBoardText);
                        break;
                    case R.id.b20:
                        x = 2;
                        y = 0;
                        send("MSG,@" + playTTT + "," + bBoardText + "20");
                        board[2][0].setEnabled(false);
                        board[2][0].setTextSize(50);
                        board[2][0].setText(bBoardText);
                        break;
                    case R.id.b21:
                        x = 2;
                        y = 1;
                        send("MSG,@" + playTTT + "," + bBoardText + "21");
                        board[2][1].setEnabled(false);
                        board[2][1].setTextSize(50);
                        board[2][1].setText(bBoardText);
                        break;
                    case R.id.b22:
                        x = 2;
                        y = 2;
                        send("MSG,@" + playTTT + "," + bBoardText + "22");
                        board[2][2].setEnabled(false);
                        board[2][2].setTextSize(50);
                        board[2][2].setText(bBoardText);
                        break;

                    default:
                        break;
                }
            }
        };

        // assign OnClickListeners to board buttons
        for (int x = 0; x < 3; x++)
            for (int y = 0; y < 3; y++)
                board[x][y].setOnClickListener(boardClickListener);


        // start the AsyncTask that connects to the server
        // and listens to whatever the server is sending to us
        connect();

    }


    @Override
    protected void onDestroy() {
        Log.i(TAG, "onDestroy called");
        disconnect();
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle menu click events
        if (item.getItemId() == R.id.exit) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ttt, menu);
        return true;
    }




    /***************************************************************************/
    /********* Networking ******************************************************/
    /***************************************************************************/

    /**
     * Connect to the server. This method is safe to call from the UI thread.
     */
    void connect() {

        new AsyncTask<Void, Void, String>() {

            String errorMsg = null;

            @Override
            protected String doInBackground(Void... args) {
                Log.i(TAG, "Connect task started");
                try {
                    connected = false;
                    socket = new Socket(GROUPCAST_SERVER, GROUPCAST_PORT);
                    Log.i(TAG, "Socket created");
                    in = new BufferedReader(new InputStreamReader(
                            socket.getInputStream()));
                    out = new PrintWriter(socket.getOutputStream());

                    connected = true;
                    Log.i(TAG, "Input and output streams ready");

                } catch (UnknownHostException e1) {
                    errorMsg = e1.getMessage();
                } catch (IOException e1) {
                    errorMsg = e1.getMessage();
                    try {
                        if (out != null) {
                            out.close();
                        }
                        if (socket != null) {
                            socket.close();
                        }
                    } catch (IOException ignored) {
                    }
                }
                Log.i(TAG, "Connect task finished");
                return errorMsg;
            }

            @Override
            protected void onPostExecute(String errorMsg) {
                if (errorMsg == null) {
                    Toast.makeText(getApplicationContext(),
                            "Connected to server", Toast.LENGTH_SHORT).show();

                    hideConnectingText();
                    showLoginControls();

                    // start receiving
                    receive();

                } else {
                    Toast.makeText(getApplicationContext(),
                            "Error: " + errorMsg, Toast.LENGTH_SHORT).show();
                    // can't connect: close the activity
                    finish();
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    /**
     * Start receiving one-line messages over the TCP connection. Received lines are
     * handled in the onProgressUpdate method which runs on the UI thread.
     * This method is automatically called after a connection has been established.
     */

    void receive() {
        new AsyncTask<Void, String, Void>() {

            @Override
            protected Void doInBackground(Void... args) {
                Log.i(TAG, "Receive task started");
                try {
                    while (connected) {

                        String msg = in.readLine();

                        if (msg == null) { // other side closed the
                            // connection
                            break;
                        }
                        publishProgress(msg);
                    }

                } catch (UnknownHostException e1) {
                    Log.i(TAG, "UnknownHostException in receive task");
                } catch (IOException e1) {
                    Log.i(TAG, "IOException in receive task");
                } finally {
                    connected = false;
                    try {
                        if (out != null)
                            out.close();
                        if (socket != null)
                            socket.close();
                    } catch (IOException e) {
                    }
                }
                Log.i(TAG, "Receive task finished");
                return null;
            }

            @Override
            protected void onProgressUpdate(String... lines) {
                // the message received from the server is
                // guaranteed to be not null
                String msg = lines[0];
                String bBoardText = tvChooseYourSymbol.getText().toString();

                // TODO: act on messages received from the server
                if(msg.startsWith("+OK,NAME")) {
                    hideLoginControls();
                    showUsers();
                    showGroups();
                    showPlayTTT();
                    return;
                }

                if(msg.startsWith("+ERROR,NAME")) {
                    Toast.makeText(getApplicationContext(), msg.substring("+ERROR,NAME,".length()), Toast.LENGTH_SHORT).show();
                    return;
                }

                if(msg.startsWith("+OK,LIST,GROUPS")) {
                    String groupsList = msg.replaceAll("@","\n@");
                    tvGroups.setText(groupsList);
                    return;
                }

                if(msg.startsWith("+OK,JOIN")) {
                    tvGroups.setText(msg);
                    return;
                }

                if(msg.startsWith("+ERROR,JOIN")){
                    Toast.makeText(getApplicationContext(), msg.substring("+ERROR,JOIN,".length()), Toast.LENGTH_SHORT).show();
                    return;
                }

                if(msg.startsWith("+OK,LIST,USERS,@")){
                    String usersList = msg.replaceAll("@","\n@");
                    tvUsers.setText(usersList);
                    return;
                }
                else if(msg.startsWith("+OK,LIST,USERS")){
                    String usersList = msg.replaceAll(":",":\n");
                    tvUsers.setText(usersList);
                    return;
                }

                if(msg.startsWith("+ERROR,LIST,USERS,@")){
                    Toast.makeText(getApplicationContext(), msg.substring("+ERROR,LIST,USERS,".length()), Toast.LENGTH_SHORT).show();
                    return;
                }

                if(msg.startsWith("+OK,LIST,MYGROUPS")){
                    String usersList = msg.replaceAll("@","\n@");
                    tvUsers.setText(usersList);
                    return;
                }

                if(msg.startsWith("+OK,MSG") || msg.startsWith("+MSG")) {
                    int startChatMsg = 0;
                    int endChatMsg = 0;
                    int endGameMsg = 0;
                    if (msg.startsWith("+OK,MSG")) {
                        startChatMsg = "+OK,MSG,".length();
                        endChatMsg = msg.length() - ": x client(s) notified".length();
                        endGameMsg = msg.length() - ": x client(s) notified".length() - 2;
                    } else if (msg.startsWith("+MSG")) {
                        startChatMsg = "+MSG,".length();
                        endChatMsg = msg.length();
                        endGameMsg = msg.length() - 2;
                    }
                    String chatMsg = msg.substring(startChatMsg, endChatMsg);
                    String gameMsg = msg.substring(startChatMsg, endGameMsg);
                    Log.i("chatMsg:",chatMsg);
                    Log.i("gameMsg:",gameMsg);


                    //receive O from O symbol users
                    if (chatMsg.endsWith("O00") || msg.endsWith("O00")) {
                        board[0][0].setEnabled(false);
                        board[0][0].setTextSize(50);
                        board[0][0].setText("O");
                        if (gameMsg.endsWith(bBoardText)) {
                            tvPlayStatus.setText("Opponent's turn");
                        } else {
                            tvPlayStatus.setText("Your turn");
                        }
                        determineWinner(bBoardText);
                        return;
                    }

                    if (chatMsg.endsWith("O01") || msg.endsWith("O01")) {
                        board[0][1].setEnabled(false);
                        board[0][1].setTextSize(50);
                        board[0][1].setText("O");
                        if (gameMsg.endsWith(bBoardText)) {
                            tvPlayStatus.setText("Opponent's turn");
                        } else {
                            tvPlayStatus.setText("Your turn");
                        }
                        determineWinner(bBoardText);
                        return;
                    }

                    if (chatMsg.endsWith("O02") || msg.endsWith("O02")) {
                        board[0][2].setEnabled(false);
                        board[0][2].setTextSize(50);
                        board[0][2].setText("O");
                        if (gameMsg.endsWith(bBoardText)) {
                            tvPlayStatus.setText("Opponent's turn");
                        } else {
                            tvPlayStatus.setText("Your turn");
                        }
                        determineWinner(bBoardText);
                        return;
                    }

                    if (chatMsg.endsWith("O10") || msg.endsWith("O10")) {
                        board[1][0].setEnabled(false);
                        board[1][0].setTextSize(50);
                        board[1][0].setText("O");
                        if (gameMsg.endsWith(bBoardText)) {
                            tvPlayStatus.setText("Opponent's turn");
                        } else {
                            tvPlayStatus.setText("Your turn");
                        }
                        determineWinner(bBoardText);
                        return;
                    }

                    if (chatMsg.endsWith("O11") || msg.endsWith("O11")) {
                        board[1][1].setEnabled(false);
                        board[1][1].setTextSize(50);
                        board[1][1].setText("O");
                        if (gameMsg.endsWith(bBoardText)) {
                            tvPlayStatus.setText("Opponent's turn");
                        } else {
                            tvPlayStatus.setText("Your turn");
                        }
                        determineWinner(bBoardText);
                        return;
                    }

                    if (chatMsg.endsWith("O12") || msg.endsWith("O12")) {
                        board[1][2].setEnabled(false);
                        board[1][2].setTextSize(50);
                        board[1][2].setText("O");
                        if (gameMsg.endsWith(bBoardText)) {
                            tvPlayStatus.setText("Opponent's turn");
                        } else {
                            tvPlayStatus.setText("Your turn");
                        }
                        determineWinner(bBoardText);
                        return;
                    }

                    if (chatMsg.endsWith("O20") || msg.endsWith("O20")) {
                        board[2][0].setEnabled(false);
                        board[2][0].setTextSize(50);
                        board[2][0].setText("O");
                        if (gameMsg.endsWith(bBoardText)) {
                            tvPlayStatus.setText("Opponent's turn");
                        } else {
                            tvPlayStatus.setText("Your turn");
                        }
                        determineWinner(bBoardText);
                        return;
                    }

                    if (chatMsg.endsWith("O21") || msg.endsWith("O21")) {
                        board[2][1].setEnabled(false);
                        board[2][1].setTextSize(50);
                        board[2][1].setText("O");
                        if (gameMsg.endsWith(bBoardText)) {
                            tvPlayStatus.setText("Opponent's turn");
                        } else {
                            tvPlayStatus.setText("Your turn");
                        }
                        determineWinner(bBoardText);
                        return;
                    }

                    if (chatMsg.endsWith("O22") || msg.endsWith("O22")) {
                        board[2][2].setEnabled(false);
                        board[2][2].setTextSize(50);
                        board[2][2].setText("O");
                        if (gameMsg.endsWith(bBoardText)) {
                            tvPlayStatus.setText("Opponent's turn");
                        } else {
                            tvPlayStatus.setText("Your turn");
                        }
                        determineWinner(bBoardText);
                        return;
                    }

                    //receive X from X symbol users
                    if (chatMsg.endsWith("X00") || msg.endsWith("X00")) {
                        board[0][0].setEnabled(false);
                        board[0][0].setTextSize(50);
                        board[0][0].setText("X");
                        if (gameMsg.endsWith(bBoardText)) {
                            tvPlayStatus.setText("Opponent's turn");
                        } else {
                            tvPlayStatus.setText("Your turn");
                        }
                        determineWinner(bBoardText);
                        return;
                    }

                    if (chatMsg.endsWith("X01") || msg.endsWith("X01")) {
                        board[0][1].setEnabled(false);
                        board[0][1].setTextSize(50);
                        board[0][1].setText("X");
                        if (gameMsg.endsWith(bBoardText)) {
                            tvPlayStatus.setText("Opponent's turn");
                        } else {
                            tvPlayStatus.setText("Your turn");
                        }
                        determineWinner(bBoardText);
                        return;
                    }

                    if (chatMsg.endsWith("X02") || msg.endsWith("X02")) {
                        board[0][2].setEnabled(false);
                        board[0][2].setTextSize(50);
                        board[0][2].setText("X");
                        if (gameMsg.endsWith(bBoardText)) {
                            tvPlayStatus.setText("Opponent's turn");
                        } else {
                            tvPlayStatus.setText("Your turn");
                        }
                        determineWinner(bBoardText);
                        return;
                    }

                    if (chatMsg.endsWith("X10") || msg.endsWith("X10")) {
                        board[1][0].setEnabled(false);
                        board[1][0].setTextSize(50);
                        board[1][0].setText("X");
                        if (gameMsg.endsWith(bBoardText)) {
                            tvPlayStatus.setText("Opponent's turn");
                        } else {
                            tvPlayStatus.setText("Your turn");
                        }
                        determineWinner(bBoardText);
                        return;
                    }

                    if (chatMsg.endsWith("X11") || msg.endsWith("X11")) {
                        board[1][1].setEnabled(false);
                        board[1][1].setTextSize(50);
                        board[1][1].setText("X");
                        if (gameMsg.endsWith(bBoardText)) {
                            tvPlayStatus.setText("Opponent's turn");
                        } else {
                            tvPlayStatus.setText("Your turn");
                        }
                        determineWinner(bBoardText);
                        return;
                    }

                    if (chatMsg.endsWith("X12") || msg.endsWith("X12")) {
                        board[1][2].setEnabled(false);
                        board[1][2].setTextSize(50);
                        board[1][2].setText("X");
                        if (gameMsg.endsWith(bBoardText)) {
                            tvPlayStatus.setText("Opponent's turn");
                        } else {
                            tvPlayStatus.setText("Your turn");
                        }
                        determineWinner(bBoardText);
                        return;
                    }

                    if (chatMsg.endsWith("X20") || msg.endsWith("X20")) {
                        board[2][0].setEnabled(false);
                        board[2][0].setTextSize(50);
                        board[2][0].setText("X");
                        if (gameMsg.endsWith(bBoardText)) {
                            tvPlayStatus.setText("Opponent's turn");
                        } else {
                            tvPlayStatus.setText("Your turn");
                        }
                        determineWinner(bBoardText);
                        return;
                    }

                    if (chatMsg.endsWith("X21") || msg.endsWith("X21")) {
                        board[2][1].setEnabled(false);
                        board[2][1].setTextSize(50);
                        board[2][1].setText("X");
                        if (gameMsg.endsWith(bBoardText)) {
                            tvPlayStatus.setText("Opponent's turn");
                        } else {
                            tvPlayStatus.setText("Your turn");
                        }
                        determineWinner(bBoardText);
                        return;
                    }

                    if (chatMsg.endsWith("X22") || msg.endsWith("X22")) {
                        board[2][2].setEnabled(false);
                        board[2][2].setTextSize(50);
                        board[2][2].setText("X");
                        if (gameMsg.endsWith(bBoardText)) {
                            tvPlayStatus.setText("Opponent's turn");
                        } else {
                            tvPlayStatus.setText("Your turn");
                        }
                        determineWinner(bBoardText);
                        return;
                    }

                    tvChatRoom.setMovementMethod(new ScrollingMovementMethod());
                    tvChatRoom.append('\n' + chatMsg);

                    return;
                }


                // [ ... and so on for other kinds of messages]


                // if we haven't returned yet, tell the user that we have an unhandled message
                Toast.makeText(getApplicationContext(), "Unhandled message: "+msg, Toast.LENGTH_SHORT).show();
            }

        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }


    /**
     * Disconnect from the server
     */
    void disconnect() {
        new Thread() {
            @Override
            public void run() {
                if (connected) {
                    connected = false;
                }
                // make sure that we close the output, not the input
                if (out != null) {
                    out.print("BYE");
                    out.flush();
                    out.close();
                }
                // in some rare cases, out can be null, so we need to close the socket itself
                if (socket != null)
                    try { socket.close();} catch(IOException ignored) {}

                Log.i(TAG, "Disconnect task finished");
            }
        }.start();
    }

    /**
     * Send a one-line message to the server over the TCP connection. This
     * method is safe to call from the UI thread.
     *
     * @param msg
     *            The message to be sent.
     * @return true if sending was successful, false otherwise
     */
    boolean send(String msg) {
        if (!connected) {
            Log.i(TAG, "can't send: not connected");
            return false;
        }

        new AsyncTask<String, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(String... msg) {
                Log.i(TAG, "sending: " + msg[0]);
                out.println(msg[0]);
                return out.checkError();
            }

            @Override
            protected void onPostExecute(Boolean error) {
                if (!error) {
                    Toast.makeText(getApplicationContext(),
                            "Message sent to server", Toast.LENGTH_SHORT)
                            .show();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Error sending message to server",
                            Toast.LENGTH_SHORT).show();
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, msg);

        return true;
    }

    /***************************************************************************/
    /***** UI related methods **************************************************/
    /***************************************************************************/

    /**
     * Hide the "connecting to server" text
     */
    void hideConnectingText() {
        findViewById(R.id.tvConnecting).setVisibility(View.GONE);
    }

    /**
     * Show the "connecting to server" text
     */
    void showConnectingText() {
        findViewById(R.id.tvConnecting).setVisibility(View.VISIBLE);
    }

    /**
     * Hide the login controls
     */

    void hideGroups() {
        findViewById(R.id.llGroups).setVisibility(View.GONE);
    }

    /**
     * Hide the groups
     */
    void showGroups() {
        findViewById(R.id.llGroups).setVisibility(View.VISIBLE);
    }

    /**
     * Show the groups
     */

    void hideUsers() {
        findViewById(R.id.llUsers).setVisibility(View.GONE);
    }

    /**
     * Hide the users
     */
    void showUsers() {
        findViewById(R.id.llUsers).setVisibility(View.VISIBLE);
    }

    /**
     * Show the users
     */

    void hidePlayTTT() {
        findViewById(R.id.llPlay).setVisibility(View.GONE);
    }

    /**
     * Hide the play
     */
    void showPlayTTT() {
        findViewById(R.id.llPlay).setVisibility(View.VISIBLE);
    }

    /**
     * Show the play
     */

    void hidePlayStatus() {
        findViewById(R.id.llPlayStatus).setVisibility(View.GONE);
        tvChatRoom.setText("Chat Room\n");
    }

    /**
     * Hide the play status
     */
    void showPlayStatus() {
        findViewById(R.id.llPlayStatus).setVisibility(View.VISIBLE);
    }

    /**
     * Show the play status
     */
    void hideLoginControls() {
        findViewById(R.id.llLoginControls).setVisibility(View.GONE);
    }

    /**
     * Show the login controls
     */
    void showLoginControls() {
        findViewById(R.id.llLoginControls).setVisibility(View.VISIBLE);
    }

    /**
     * Hide the tictactoe board
     */
    void hideBoard() {
        findViewById(R.id.llBoard).setVisibility(View.GONE);
    }

    /**
     * Show the tictactoe board
     */
    void showBoard() {
        findViewById(R.id.llBoard).setVisibility(View.VISIBLE);
    }


    /**
     * Make the buttons of the tictactoe board clickable if they are not marked yet
     */
    void enableBoardClick() {
        for (int x = 0; x < 3; x++)
            for (int y = 0; y < 3; y++)
                if ("".equals(board[x][y].getText().toString()))
                    board[x][y].setEnabled(true);
    }

    /**
     * Make the tictactoe board non-clickable
     */
    void disableBoardClick() {
        for (int x = 0; x < 3; x++)
            for (int y = 0; y < 3; y++) {
                board[x][y].setText("");
                board[x][y].setEnabled(false);
            }
    }

    void determineWinner(String bBoardText) {
        if( board[0][0].getText().toString().equals(bBoardText) && board[0][1].getText().toString().equals(bBoardText) && board[0][2].getText().toString().equals(bBoardText) || 
            board[1][0].getText().toString().equals(bBoardText) && board[1][1].getText().toString().equals(bBoardText) && board[1][2].getText().toString().equals(bBoardText) ||
            board[2][0].getText().toString().equals(bBoardText) && board[2][1].getText().toString().equals(bBoardText) && board[2][2].getText().toString().equals(bBoardText) ||
            board[0][0].getText().toString().equals(bBoardText) && board[1][0].getText().toString().equals(bBoardText) && board[2][0].getText().toString().equals(bBoardText) ||
            board[0][1].getText().toString().equals(bBoardText) && board[1][1].getText().toString().equals(bBoardText) && board[2][1].getText().toString().equals(bBoardText) ||
            board[0][2].getText().toString().equals(bBoardText) && board[1][2].getText().toString().equals(bBoardText) && board[2][2].getText().toString().equals(bBoardText) ||
            board[0][0].getText().toString().equals(bBoardText) && board[1][1].getText().toString().equals(bBoardText) && board[2][2].getText().toString().equals(bBoardText) ||
            board[0][2].getText().toString().equals(bBoardText) && board[1][1].getText().toString().equals(bBoardText) && board[2][0].getText().toString().equals(bBoardText)
            ) {
            disableBoardClick();
            new AlertDialog.Builder(TTTActivity.this)
                    .setTitle("Congratulation!")
                    .setMessage("You win!\nDo you want to back to main menu?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            disableBoardClick();
                            hidePlayStatus();
                            hideBoard();
                            showUsers();
                            showGroups();
                            showPlayTTT();
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                        }
                    })
                    .show();
        }
        else {
            String oppBoardText = null;
            if(bBoardText.equals("O")) {
                oppBoardText = "X";
            }
            else if(bBoardText.equals("X")) {
                oppBoardText = "O";
            }
            if( board[0][0].getText().toString().equals(oppBoardText) && board[0][1].getText().toString().equals(oppBoardText) && board[0][2].getText().toString().equals(oppBoardText) ||
                    board[1][0].getText().toString().equals(oppBoardText) && board[1][1].getText().toString().equals(oppBoardText) && board[1][2].getText().toString().equals(oppBoardText) ||
                    board[2][0].getText().toString().equals(oppBoardText) && board[2][1].getText().toString().equals(oppBoardText) && board[2][2].getText().toString().equals(oppBoardText) ||
                    board[0][0].getText().toString().equals(oppBoardText) && board[1][0].getText().toString().equals(oppBoardText) && board[2][0].getText().toString().equals(oppBoardText) ||
                    board[0][1].getText().toString().equals(oppBoardText) && board[1][1].getText().toString().equals(oppBoardText) && board[2][1].getText().toString().equals(oppBoardText) ||
                    board[0][2].getText().toString().equals(oppBoardText) && board[1][2].getText().toString().equals(oppBoardText) && board[2][2].getText().toString().equals(oppBoardText) ||
                    board[0][0].getText().toString().equals(oppBoardText) && board[1][1].getText().toString().equals(oppBoardText) && board[2][2].getText().toString().equals(oppBoardText) ||
                    board[0][2].getText().toString().equals(oppBoardText) && board[1][1].getText().toString().equals(oppBoardText) && board[2][0].getText().toString().equals(oppBoardText)
                    ) {
                disableBoardClick();
                new AlertDialog.Builder(TTTActivity.this)
                        .setTitle("Try again!")
                        .setMessage("You lose!\nDo you want to back to main menu?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                disableBoardClick();
                                hidePlayStatus();
                                hideBoard();
                                showUsers();
                                showGroups();
                                showPlayTTT();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .show();
            }
            
        }
    }
   

}
