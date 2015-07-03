package com.fortytwo.opent4c.screens;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.fortytwo.opent4c.netcode150.Pak150;
import com.fortytwo.opent4c.proxy.ClientManager;
import com.fortytwo.opent4c.proxy.ProxyManager;
import com.fortytwo.opent4c.tools.CalendarUtils;

public class InfoScreen implements Screen{
	
	private Stage stage;
	private SpriteBatch batch;
	private OrthographicCamera camera;
	private BitmapFont font;
	private LabelStyle labelStyle;
	private Label configTitleLabel;
	private Label serverLabel;
	private Label serverVersionLabel;
	private Label serverPortLabel;
	private Label clientVersionLabel;
	private Label maxClientsLabel;
	private Label proxyPortLabel;
	private Label botLabel;
	private Label snifferLabel;
	private ShapeRenderer shapes;
	private Label commandTitleLabel;
	private TextButtonStyle buttonStyle;
	private TextButton viewInfosButton;
	private TextButton viewPakStatsButton;
	private TextButton quitButton;
	private TextButtonStyle redButtonStyle;
	private TextButtonStyle bluButtonStyle;
	private TextButton viewMonitorButton;
	private TextButton viewServerButton;
	private TextButton viewClientsButton;
	private TextButton viewBotsButton;
	private Label screenTitleLabel;
	private Label numberOfConnectedClientsLabel;
	private int numberOfConnectedClients = 0;
	private static Map<Integer,Label> consoleLines;
	private static final int consoleLineMaxLength = 116;
	private static boolean active = false;
	
	public InfoScreen(SpriteBatch batch) {
		stage = new Stage();
		this.batch = batch;
		shapes = new ShapeRenderer();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		stage.getViewport().setCamera(camera);
		addText();
	}
	
	private void addText() {
		font = new BitmapFont();
		font.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		font.setScale(1f);
		//textFieldStyle = new TextFieldStyle();
		//textFieldStyle.font = font;
		//textFieldStyle.fontColor = Color.WHITE;
		//TextureRegion cursor = Atlas.getUtils().findRegion("Cursor");
		//textFieldStyle.cursor = new SpriteDrawable(new Sprite(cursor));
		buttonStyle = new TextButtonStyle();
		buttonStyle.font = font;
		redButtonStyle = new TextButtonStyle();
		redButtonStyle.font = font;
		redButtonStyle.fontColor = Color.RED;
		bluButtonStyle = new TextButtonStyle();
		bluButtonStyle.font = font;
		bluButtonStyle.fontColor = Color.CYAN;
		labelStyle = new LabelStyle();
		labelStyle.font = font;
	    configTitleLabel = new Label("CONFIG", labelStyle);
	    serverLabel = new Label("Server Address : "+ProxyManager.serverAddress.getHostAddress(), labelStyle);
	    serverVersionLabel = new Label("Server Version : "+ProxyManager.serverVersion, labelStyle);
	    serverPortLabel = new Label("Server Port : "+ProxyManager.serverPort, labelStyle);
	    clientVersionLabel = new Label("Client Version :"+ProxyManager.clientVersion, labelStyle);
	    maxClientsLabel = new Label("Max Clients : "+ProxyManager.max_clients, labelStyle);
	    proxyPortLabel = new Label("Proxy Port : "+ProxyManager.proxyPort, labelStyle);
	    botLabel = new Label("Bot : "+ProxyManager.bot, labelStyle);
	    snifferLabel = new Label("Sniffer : "+ProxyManager.sniffer, labelStyle);
	    commandTitleLabel = new Label("COMMANDS", labelStyle);
	    screenTitleLabel = new Label("INFOS", labelStyle);

	    /*serverInput = new TextField("127.0.0.1", textFieldStyle);
	    serverInput.setWidth(500);
	    serverInput.setFocusTraversal(true);
	    serverInput.setMaxLength(50);
	    serverInput.setPosition(700, 475);
	    loginInput = new TextField("test", textFieldStyle);
	    loginInput.setWidth(1000);
	    loginInput.setFocusTraversal(true);
	    loginInput.setMaxLength(50);
	    loginInput.setPosition(590, 450);
	    passInput = new TextField("azerty", textFieldStyle);
	    passInput.setWidth(500);
	    passInput.setFocusTraversal(true);
	    passInput.setMaxLength(50);
	    passInput.setPasswordMode(true);
	    passInput.setPasswordCharacter('²');
	    passInput.setPosition(635, 423);*/
	    viewInfosButton = new TextButton("View Informations", bluButtonStyle);
	    viewPakStatsButton = new TextButton("View Pak Statistics", buttonStyle);
	    viewMonitorButton = new TextButton("View Pak Monitor", buttonStyle);
	    quitButton = new TextButton("QUIT", redButtonStyle);
	    viewServerButton = new TextButton("View Server Informations", buttonStyle);
	    viewClientsButton = new TextButton("View Clients Informations", buttonStyle);
	    viewBotsButton = new TextButton("View Bots Informations", buttonStyle);
	    addListeners();
	    stage.addActor(configTitleLabel);
	    stage.addActor(serverVersionLabel);
	    stage.addActor(serverPortLabel);
	    stage.addActor(serverLabel);
	    stage.addActor(clientVersionLabel);
	    stage.addActor(maxClientsLabel);
	    stage.addActor(proxyPortLabel);
	    stage.addActor(botLabel);
	    stage.addActor(snifferLabel);
	    stage.addActor(commandTitleLabel);
	    stage.addActor(viewInfosButton);
	    stage.addActor(viewPakStatsButton);
	    stage.addActor(viewMonitorButton);
	    stage.addActor(quitButton);
	    stage.addActor(viewServerButton);
	    stage.addActor(viewClientsButton);
	    stage.addActor(viewBotsButton);
	    stage.addActor(screenTitleLabel);
	    addScreenText();
	    addConsole(21);
	    setConsolePosition();
	    //stage.addActor(loginInput);
	    //stage.addActor(passInput);
	    //stage.addActor(serverInput);
	}
	
	private void addConsole(int maxLines) {
		consoleLines = new HashMap<Integer,Label>(maxLines);
		for (int i = 0 ; i < maxLines ; i++){
			consoleLines.put(i,new Label("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX", labelStyle));
			stage.addActor(consoleLines.get(i));
		}
	}

	private void addScreenText() {
	    numberOfConnectedClientsLabel = new Label("Clients connected : "+numberOfConnectedClients, labelStyle);
	    stage.addActor(numberOfConnectedClientsLabel);

		
	}

	private void getNumberOfCLientsConnected() {
		numberOfConnectedClients = ClientManager.getClientsSize();
		numberOfConnectedClientsLabel.setText("Clients connected : "+numberOfConnectedClients);
	}

	@Override
	public void render(float delta) {
		Gdx.graphics.setTitle(ProxyManager.getTitle()+" [Memory Usage : "+Gdx.app.getJavaHeap()/1024/1024+" Mio @ "+Gdx.graphics.getFramesPerSecond()+"FPS]");
	    configTitleLabel.setPosition(getXPos(7), getYPos(97));
	    serverLabel.setPosition(getXPos(1), getYPos(93));
	    serverVersionLabel.setPosition(getXPos(1), getYPos(90));
	    serverPortLabel.setPosition(getXPos(1), getYPos(87));
	    clientVersionLabel.setPosition(getXPos(1), getYPos(84));
	    maxClientsLabel.setPosition(getXPos(1), getYPos(81));
	    proxyPortLabel.setPosition(getXPos(1), getYPos(78));
	    botLabel.setPosition(getXPos(1), getYPos(75));
	    snifferLabel.setPosition(getXPos(1), getYPos(72));
	    commandTitleLabel.setPosition(getXPos(5), getYPos(68));
	    viewInfosButton.setPosition(getXPos(1), getYPos(64));
	    viewPakStatsButton.setPosition(getXPos(1), getYPos(61));
	    viewMonitorButton.setPosition(getXPos(1), getYPos(58));
	    quitButton.setPosition(getXPos(7), getYPos(1));
	    viewServerButton.setPosition(getXPos(1), getYPos(55));
	    viewClientsButton.setPosition(getXPos(1), getYPos(52));
	    viewBotsButton.setPosition(getXPos(1), getYPos(49));
	    screenTitleLabel.setPosition(getXPos(50), getYPos(97));
	    numberOfConnectedClientsLabel.setPosition(getXPos(19), getYPos(93));
	    getNumberOfCLientsConnected();
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		camera.update();
		stage.act(delta);
		stage.draw();
		shapes.begin(ShapeType.Line);
		shapes.line(getXPos(18), 0, getXPos(18), getYPos(100));
		shapes.line(0, getYPos(96), getXPos(100), getYPos(96));
		shapes.line(0, getYPos(71), getXPos(18), getYPos(71));
		shapes.line(0, getYPos(67), getXPos(100), getYPos(67));
		shapes.line(0, getYPos(4), getXPos(18), getYPos(4));
		shapes.end();
	}

	private static void writeConsoleLine(final String line) {
		Gdx.app.postRunnable(new Runnable(){	
			public void run(){
				for(int i = 0 ; i < consoleLines.size()-1 ; i++){
					consoleLines.get(i).setText(consoleLines.get(i+1).getText());
				}
				consoleLines.get(consoleLines.size()-1).setText(CalendarUtils.getTimeStringFromNow()+" "+line);
			}
		});
	}

	public static void writeConsoleLines(String lines){
		String[] split = lines.split(""+System.lineSeparator());
		for (String s : split){
			int length = s.length();
			if (length > consoleLineMaxLength){
				while(length > consoleLineMaxLength)
				writeConsoleLine(s.substring(0,consoleLineMaxLength));
				length -= consoleLineMaxLength;
				s = s.substring(consoleLineMaxLength+1);
			}else{
				writeConsoleLine(s);
			}
		}
	}
	
	private void setConsolePosition() {
		int offset = 0;
		for (int i = 0 ; i < consoleLines.size() ; i++){
		    consoleLines.get(i).setPosition(getXPos(19), getYPos(62-offset));
		    offset += 3;
		}
	}

	private void addListeners() {
	    viewInfosButton.addListener(new ClickListener(){
	    	public void clicked (InputEvent event, float x, float y) {

		 	}
	    });
	    viewPakStatsButton.addListener(new ClickListener(){
	    	public void clicked (InputEvent event, float x, float y) {
	    		ProxyManager.setPakStatsScreen();
		 	}
	    });
	    quitButton.addListener(new ClickListener(){
	    	public void clicked (InputEvent event, float x, float y) {
	    		ProxyManager.quit();
		 	}
	    });
	    viewMonitorButton.addListener(new ClickListener(){
	    	public void clicked (InputEvent event, float x, float y) {
	    		ProxyManager.setMonitorScreen();
		 	}
	    });
	    viewServerButton.addListener(new ClickListener(){
	    	public void clicked (InputEvent event, float x, float y) {
	    		ProxyManager.setServerScreen();
		 	}
	    });
	    viewClientsButton.addListener(new ClickListener(){
	    	public void clicked (InputEvent event, float x, float y) {
	    		ProxyManager.setClientsScreen();
		 	}
	    });
	    viewBotsButton.addListener(new ClickListener(){
	    	public void clicked (InputEvent event, float x, float y) {
	    		ProxyManager.setBotsScreen();
		 	}
	    });
	    /*viewPakStatsButton.addListener(new ClickListener(){
	    	public void clicked (InputEvent event, float x, float y) {
	    		
		 	}
	    });
	    quitButton.addListener(new ClickListener(){
	    	public void clicked (InputEvent event, float x, float y) {
	    		ProxyManager.quit();
		 	}
	    });*/
	    /*loginInput.addListener(new ClickListener(){
	    	public void clicked(InputEvent event, float x, float y){
	    		loginInput.setText("");
	    	}
	    });
	    passInput.addListener(new ClickListener(){
	    	public void clicked(InputEvent event, float x, float y){
	    		passInput.setText("");
	    	}
	    });
	    passInput.addListener(new FocusListener(){
	    	@Override
	        public boolean handle(Event event){
	            if (event.toString().equals("mouseMoved") 
                    || event.toString().equals("exit") 
                    || event.toString().equals("enter") 
                    || event.toString().equals("keyDown") 
                    || event.toString().equals("keyUp") 
                    || event.toString().equals("keyTyped") 
                    || event.toString().equals("touchDown") 
                    || event.toString().equals("touchUp")){
	                return false;
	            }
	    		if(passInput.getText().equals("password"))passInput.setText("");
                return true;
	    	}
	    });
	    
	    serverInput.addListener(new ClickListener(){
	    	public void clicked(InputEvent event, float x, float y){
	    		serverInput.setText("");

	    	}
	    });*/
	}
	
	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height);		
	}

	@Override
	public void show() {
		active = true;
		Gdx.input.setInputProcessor(stage);
		stage.addAction(Actions.alpha(1, 0.3f));
	}

	@Override
	public void hide() {
		stage.addAction(Actions.alpha(0, 0.3f));
		active = false;
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		stage.dispose();		
	}

	private int getXPos(int pos){
		int result = 0;
		result = (int) (pos*stage.getWidth()/100);
		return result;
	}
	
	private int getYPos(int pos){
		int result = 0;
		result = (int) (pos*stage.getHeight()/100);
		return result;
	}
}
