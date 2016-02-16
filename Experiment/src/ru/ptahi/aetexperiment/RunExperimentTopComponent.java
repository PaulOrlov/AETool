package ru.ptahi.aetexperiment;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javax.imageio.ImageIO;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import org.netbeans.api.project.Project;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.util.Exceptions;
import org.openide.windows.TopComponent;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.WindowManager;
import processing.core.PApplet;
import static processing.core.PConstants.CENTER;
import ru.ptahi.iconmanager.IconManager;
import ru.ptahi.cfe.stumile.StimuleList;

/**
 * Top component which displays something.
 */
//@ConvertAsProperties(
//        dtd = "-//ru.ptahi.aetexperiment//RunExperiment//EN",
//        autostore = false
//)
@TopComponent.Description(
        preferredID = "RunExperimentTopComponent",
        //iconBase="SET/PATH/TO/ICON/HERE", 
        //        persistenceType = TopComponent.PERSISTENCE_ALWAYS
        persistenceType = TopComponent.PERSISTENCE_ONLY_OPENED
)
@TopComponent.Registration(mode = "editor", openAtStartup = false)
@ActionID(category = "Window", id = "ru.ptahi.aetexperiment.RunExperimentTopComponent")
@ActionReference(path = "Menu/Window" /*, position = 333 */)
//@TopComponent.OpenActionRegistration(
//        displayName = "#CTL_RunExperimentAction",
//        preferredID = "RunExperimentTopComponent"
//)
@Messages({
    "CTL_RunExperimentAction=RunExperiment",
    "CTL_RunExperimentTopComponent=RunExperiment Window",
    "HINT_RunExperimentTopComponent=This is a RunExperiment window"
})

class MyPA extends PApplet{
    
    public float mColor;
    int mx;
    int my;
    
    
    
    @Override
    public void setup(){
        size(900,200);
        background(50);
        fill(200,50);
        noStroke();
        //noCursor();
        //ellipseMode(CENTER);
        mColor = 200;
    }
    
    @Override
    public void draw(){
        //background(50);
        fill(0,5);
        rect(0,0,width,height);
        fill(250,20);
        ellipse(mx, my, 10, 10);
        fill(200,mColor,mColor, 50);
        rect(0, 0, width,height);
    }
    
    public void setWhiteColor() {
        mColor = 200;
    }

    public void setRedColor() {
        mColor = 50;
    }
    
}

public final class RunExperimentTopComponent extends TopComponent {
    
    private JFXPanel fxContainer;
    private ExperimentNode context;
    private VBox vb;
    private Experiment eObj;
    Parent root;
    BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
    java.awt.Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImg, new Point(0, 0), "blank cursor");
    RunExperimentTopComponent mThis;
    private int trialsCounter = 1;
    private String screenName = "";
    BufferedImage capture;
    public static MyPA mpa;
    
    public static void makeItRed(){
        if(mpa != null){
            mpa.setRedColor();
        }
    }
    
    public static void makeItWhite(){
        if(mpa != null){
            mpa.setWhiteColor();
        }
    }
    

    public void storeBufferedImg() {
        Project project = (Project) context.getLookup().lookup(Project.class);
        String foderPathForScreens = "C:\\img\\";
        if (project != null) {
            foderPathForScreens = project.getProjectDirectory().getPath();
        }

        eObj = (Experiment) context.getLookup().lookup(Experiment.class);

        String userPrefix = "";
        if (eObj != null) {
            if (eObj.getParticipant() != null) {
                userPrefix = eObj.getParticipant().getFirstName() + eObj.getParticipant().getLastName();
            }
        }

        String fileName = foderPathForScreens + "/Screens/stImgForTrial" + userPrefix + "_" + trialsCounter + "_"
                + current.sObj.getComplexity() + "_";
        if (current.participantAnswer.equals(current.sObj.getCorrectAnswer())) {
            fileName += "AnsTRUE_";
        } else {
            fileName += "AnsFALSE_";
        }

        if (current.participantAnswer.equals("NAN")) {
            fileName += "SkipTRUE";
        } else {
            fileName += "SkipFALSE";
        }

        screenName = fileName + ".png";

        System.out.println(screenName);

        new SwingWorker<Void, Void>() {
            private boolean success;

            @Override
            protected Void doInBackground() throws Exception {

                if (ImageIO.write(capture, "png", new File(screenName))) {
                    success = true;
                }
                return null;
            }

            @Override
            protected void done() {
                if (success) {
                    // notify user it succeed
                    System.out.println("SuccessImg");
                } else {
                    // notify user it failed
                    System.out.println("FailedImg");
                }
            }
        }.execute();
    }

    public void screenCapture() {

        new SwingWorker<Void, Void>() {
            private boolean success;

            @Override
            protected Void doInBackground() throws Exception {
                Thread.sleep(400);

                try {
                    Robot robot = new Robot();
                    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                    java.awt.Rectangle screenRect = new java.awt.Rectangle(screenSize);
                    capture = robot.createScreenCapture(screenRect);
                } catch (AWTException ex) {
                    Exceptions.printStackTrace(ex);
                }
                return null;
            }

            @Override
            protected void done() {
                if (success) {
                    // notify user it succeed
                    System.out.println("SuccessTrial");
                } else {
                    // notify user it failed
                    System.out.println("FailedTrial");
                }
            }
        }.execute();

    }

    public RunExperimentTopComponent(ExperimentNode context) {
        initComponents();
        startIViewXListener();
        setName(Bundle.CTL_RunExperimentTopComponent());
        setToolTipText(Bundle.HINT_RunExperimentTopComponent());
        mThis = this;
        this.context = context;

        setLayout(new BorderLayout());
        fxContainer = new JFXPanel();
        mpa = new MyPA();
        mpa.init();
        add(mpa, BorderLayout.NORTH);
        add(fxContainer, BorderLayout.CENTER);

        Platform.setImplicitExit(false);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    createScene();
                } catch (Exception e) {
                    System.out.println("Found an exception ************************************");
                    Exceptions.printStackTrace(e);
                }
            }
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    @Override
    public void componentOpened() {
        // TODO add custom code on component opening
    }

    @Override
    public void componentClosed() {
        isTreadRuning = false;
    }

    void writeProperties(java.util.Properties p) {
        // better to version settings since initial version as advocated at
        // http://wiki.apidesign.org/wiki/PropertyFiles
        p.setProperty("version", "1.0");
        // TODO store your settings
    }

    void readProperties(java.util.Properties p) {
        String version = p.getProperty("version");
        // TODO read your settings according to their version
    }

    private void sendUPD() {
       try {
            DatagramSocket serverSocket = new DatagramSocket();
            String send = "ET_INC\n";
            byte[] m = send.getBytes();
            InetAddress aHost = InetAddress.getByName("192.168.0.2");
            int serverPort = 4444;
            DatagramPacket request = new DatagramPacket(m, m.length, aHost, serverPort);
            serverSocket.send(request);
            serverSocket.close();
        } catch (SocketException ex) {
            Exceptions.printStackTrace(ex);
        } catch (UnknownHostException ex) {
            Exceptions.printStackTrace(ex);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        trialsCounter++;
    }

    private void createScene() {

        try {
            root = FXMLLoader.load(getClass().getResource("Run.fxml"));
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }

        if (root == null) {
            return;
        }

        vb = (VBox) root.lookup("#vb");
        vb.setSpacing(10);
        vb.setAlignment(Pos.CENTER);

        Button runExpBtn = (Button) root.lookup("#runbutton");
        runExpBtn.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                Project project = (Project) context.getLookup().lookup(Project.class);
                if (project != null) {
                    StimuleList sList = (StimuleList) project.getLookup().lookup(StimuleList.class);
                    eObj.setStimuli(sList.getAllStimuli());
                    
                }
                runExperiment();
            }
        });
        runExpBtn.setGraphic(new ImageView(IconManager.getFXIcon("flag.png", 24, 24)));

        Label participantName = (Label) root.lookup("#participantname");
        if (context != null) {
            eObj = (Experiment) context.getLookup().lookup(Experiment.class);
            if (eObj != null) {
                if (eObj.getParticipant() != null) {
                    if (eObj.isIsDone()) {
                        participantName.setText("So, your experiment already done. \n\n");
                        vb.getChildren().remove(runExpBtn);
                        Scene scene = new Scene(root);
                        fxContainer.setScene(scene);
                        return;
                    }
                    participantName.setText("So, " + eObj.getParticipant().getFirstName() + " " + eObj.getParticipant().getLastName() + "...\n\n");
                } else {
                    participantName.setText("You have no participation in this experiment. \n Please, edit experiment and add participant.\n\n");
                    vb.getChildren().remove(runExpBtn);
                    Button editExp = new Button("Edit experiment");
                    editExp.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent t) {
                            SwingUtilities.invokeLater(new Runnable() {
                                @Override
                                public void run() {
                                    close();
                                    TopComponent tce = WindowManager.getDefault().findTopComponent("ExperimentEditorTopComponent");
                                    if (tce == null) {
                                        return;
                                    }
                                    if (!tce.isOpened()) {
                                        tce.open();
                                    }
                                    tce.requestActive();
                                }
                            });
                        }
                    });
                    vb.getChildren().add(editExp);
                }
            }
            Scene scene = new Scene(root);
            fxContainer.setScene(scene);

        } else {

            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    close();
                }
            });
        }
    }

    private void runExperiment() {
        if (vb == null) {
            return;
        }
        eObj.setBeginTime(String.valueOf(System.currentTimeMillis()));
        //TODO convert time to human readable        
        stieList = eObj.getStimuleINE();
        iterator = stieList.iterator();
        nextStimule();
        sendUPD();
        screenCapture();
    }


    Process process;
    ArrayList<Experiment.StimuleInExperiment> stieList;
    Iterator<Experiment.StimuleInExperiment> iterator;
    Experiment.StimuleInExperiment current;
    public static Ellipse smallCircle;
    boolean isTreadRuning = true;

    private void nextStimule() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
               // root.setCursor(Cursor.NONE);
                // vb.setCursor(Cursor.NONE);
                vb.getChildren().clear();
            }
        });

        if (iterator.hasNext()) {
            //setCursor(blankCursor);

//            final Experiment.StimuleInExperiment current = iterator.next();
            current = iterator.next();
            current.beginTime = String.valueOf(System.currentTimeMillis());
            current.indexInTheSet = stieList.indexOf(current);
            
            System.out.println("   ");
            System.out.println("maskType: " + current.maskType + ", stencilSize: " + current.stencilSize);
            System.out.println("   ");

            Platform.runLater(new Runnable() {
                @Override
                public void run() {
//                    Rectangle rect = new Rectangle(0, 0, 900, 900);
//                    rect.setFill(Color.TRANSPARENT);
//                    rect.setCursor(Cursor.NONE);

                    TextArea ta = new TextArea(current.sObj.getContent());
                    ta.setPrefWidth(1100);
                    ta.setMaxWidth(1100);
                    ta.setMaxHeight(getHeight() - 300);
                    ta.setPrefHeight(getHeight() - 300);
                    ta.setMinHeight(getHeight() - 300);

                    ta.setEditable(false);
                    ta.setPrefHeight(600);
                    ta.setCursor(Cursor.NONE);

                    ta.setStyle("-fx-text-fill: black;"
                            + "-fx-background-color: white;"
                            + "-fx-font: 14px \"Courier\" ;"
                            + "-fx-font-weight: normal;"
                            + "-fx-cursor: null;");

                    //                                "-fx-font: Courier New;"+ "-fx-font: 14px \"Serif\" ;"+ 
                    final Button aswerBtn = new Button("Ok");
                    Button nextBtn = new Button("And the Answer is...");
                    final Label yourAnswerLabel = new Label("Your answer: ");
                    final TextField answerField = new TextField();
                    answerField.setPrefWidth(400);
                    answerField.setMaxWidth(400);

                    nextBtn.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent t) {
                            SwingUtilities.invokeLater(new Runnable() {
                                @Override
                                public void run() {
                                    current.endTime = String.valueOf(System.currentTimeMillis());
                                    setCursor(java.awt.Cursor.getDefaultCursor());
                                }
                            });
                            vb.getChildren().clear();
                            vb.setAlignment(Pos.CENTER);
                            vb.getChildren().add(yourAnswerLabel);
                            vb.getChildren().add(answerField);
                            vb.getChildren().add(aswerBtn);
                            root.setCursor(Cursor.DEFAULT);
                            sendUPD();
                            //screenCapture();

                        }
                    });

                    nextBtn.addEventFilter(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent t) {
                            SwingUtilities.invokeLater(new Runnable() {
                                @Override
                                public void run() {
                                    setCursor(java.awt.Cursor.getDefaultCursor());
                                }
                            });
                        }
                    });

                    nextBtn.addEventFilter(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent t) {
                            SwingUtilities.invokeLater(new Runnable() {
                                @Override
                                public void run() {
                                    //setCursor(blankCursor);
                                }
                            });
                        }
                    });
                    //OK
                    aswerBtn.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent t) {
                            SwingUtilities.invokeLater(new Runnable() {
                                @Override
                                public void run() {
                                    current.participantAnswer = answerField.getText();
                                    sendUPD();
                                    storeBufferedImg();
                                    nextStimule();
                                    screenCapture();
                                }
                            });
                        }
                    });

                    Button noAnswerBtn = new Button("I have NO Answer");

                    noAnswerBtn.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent t) {
                            SwingUtilities.invokeLater(new Runnable() {
                                @Override
                                public void run() {
                                    current.endTime = String.valueOf(System.currentTimeMillis());
                                    current.participantAnswer = "NAN";
                                    sendUPD();
                                    storeBufferedImg();
                                    nextStimule();
                                    screenCapture();
                                }
                            });
                            vb.getChildren().clear();
                        }
                    });

                    noAnswerBtn.addEventFilter(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent t) {
                            SwingUtilities.invokeLater(new Runnable() {
                                @Override
                                public void run() {
                                    setCursor(java.awt.Cursor.getDefaultCursor());
                                }
                            });
                        }
                    });

                    noAnswerBtn.addEventFilter(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent t) {
                            SwingUtilities.invokeLater(new Runnable() {
                                @Override
                                public void run() {
                                    setCursor(blankCursor);
                                }
                            });
                        }
                    });

//                    smallCircle = new Ellipse(); 
//                    smallCircle.setCenterX(50.0f);
//                    smallCircle.setCenterY(50.0f);
//                    smallCircle.setRadiusX(50.0f);
//                    smallCircle.setRadiusY(50.0f);
//                    smallCircle.setFill(new Color(0.2, 0.2, 0.2, 0.3));


                    vb.setAlignment(Pos.TOP_CENTER);
                    Group g = new Group();
//                    HBox mhb = new HBox();
//                    mhb.getChildren().add(smallCircle);
                    g.getChildren().add(ta);
                    //g.getChildren().add(g);
                    
                    //g.getChildren().add(rect);
                    //g.setCursor(Cursor.NONE);
                    vb.getChildren().add(g);

                    HBox hb = new HBox();
                    hb.getChildren().add(noAnswerBtn);
                    hb.getChildren().add(nextBtn);
                    hb.setSpacing(90);
                    hb.setAlignment(Pos.CENTER);
                    vb.getChildren().add(hb);

                }
            });

        } else {
            eObj.setIsDone(true);
            serialize();
            exportToCSFile();
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    vb.getChildren().clear();
                    Label endField = new Label("Thank you!\n\n\n The end.");
                    vb.setAlignment(Pos.CENTER);
                    vb.getChildren().add(endField);
                }
            });

            //expId, partId, questId, date, beginTime, endTime, duration, complexity, isAnswerCorrect
        }
    }

    private void serialize() {
        SerializeCookie sc = context.getLookup().lookup(SerializeCookie.class);
        if (sc != null) {
            sc.serialize();
        }
    }

    private void exportToCSFile() {
        ExportToCSFileCookie sc = context.getLookup().lookup(ExportToCSFileCookie.class);
        if (sc != null) {
            sc.serialize();
        }
    }

    private void startIViewXListener() {
        try {
            DatagramSocket serverSocket = new DatagramSocket();
            InetAddress IPAddress = InetAddress.getByName("192.168.0.2");
            int serverPort = 4444;
            String send = "ET_STR\n";
            byte[] m = send.getBytes();
            DatagramPacket request = new DatagramPacket(m, m.length, IPAddress, serverPort);
            serverSocket.send(request);
            
            send = "ET_REC\n";
            m = send.getBytes();
            request = new DatagramPacket(m, m.length, IPAddress, serverPort);
            serverSocket.send(request);

            send = "ET_FIX\n";
            m = send.getBytes();
            request = new DatagramPacket(m, m.length, IPAddress, serverPort);
            serverSocket.send(request);

            send = "ET_FRM \"%ET %SX %SY\" \n";
            m = send.getBytes();
            request = new DatagramPacket(m, m.length, IPAddress, serverPort);
            serverSocket.send(request);
            serverSocket.close();

            Thread thr = new Thread() {
                public void run() {
                    try {
                        long fixBegin = 0;
                        DatagramSocket clientSocket = new DatagramSocket(5555);
                        while (isTreadRuning) {
                            byte[] receiveData = new byte[1024];
                            DatagramPacket receive = new DatagramPacket(receiveData, receiveData.length);
                            clientSocket.receive(receive);
                            String mRes = new String(receive.getData());
                            String[] parts = mRes.split(" ");
                            if (parts[0].equals("ET_SPL")) {
                                int xl = Integer.parseInt(parts[2].trim());
                                int xr = Integer.parseInt(parts[3].trim());
                                int yl = Integer.parseInt(parts[4].trim());
                                int yr = Integer.parseInt(parts[5].trim());

                                double gazeX = ((double) xl + (double) xr) / 2.0;
                                double gazeY = ((double) yl + (double) yr) / 2.0;

                                //System.out.println("GX: " + gazeX + ", GY: " + gazeY);
                                if(mpa != null){
                                    mpa.mx = (int) gazeX;
                                    mpa.my = (int) gazeY;
                                }
                                
                            } else if (parts[0].equals("ET_FIX")) {
                                fixBegin = System.currentTimeMillis();
                            }
                            
                            if(System.currentTimeMillis() - fixBegin > 500){
                                makeItRed();
                            } else {
                                makeItWhite();
                            }
                        }
                        clientSocket.close();
                    } catch (SocketException ex) {
                        
                    } catch (IOException ex) {
                        
                    }
                }
            };

            thr.start();

        } catch (SocketException ex) {
            
        } catch (UnknownHostException ex) {
            Exceptions.printStackTrace(ex);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
    }
}
