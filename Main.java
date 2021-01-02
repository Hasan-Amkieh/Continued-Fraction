package application;

import java.util.Arrays;
import java.util.regex.Pattern;

import javafx.application.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class Main extends Application {
	// C : 
	static {
		System.load(System.getProperty("user.dir")+"\\eval.dll");
	}
	public native double eval(String equation);
	// C ;
	Stage prmStage;
	Scene scene;
	boolean isClicked = false;
	// is the mouse clicked in the area of closeMenu node!
	int[] lastPoint = new int[2];
	GridPane root1 = new GridPane();
	CalculationCore CC;
	@Override
	public void start(Stage primaryStage) {
		// init vars : 
		prmStage = primaryStage;
		BorderPane root2 = new BorderPane();
		Label results = new Label();
		CC = new CalculationCore(root2, results);
		root1.setBorder(new Border(new BorderStroke(Color.FORESTGREEN,
				BorderStrokeStyle.SOLID, new CornerRadii(0), new BorderWidths(3, 3, 3, 3))));
		scene = new Scene(root1, 600, 450);
		
		// closeMenu : 
		GridPane closeMenu = new GridPane();
		setCloseMenu(closeMenu);
		// closeMenu ;
		
		// mainMenu : 
		MenuBar mainMenu = new MenuBar();
		mainMenu.setPrefSize(scene.getWidth(), 16);
		Menu helpMenu = new Menu("Help");
		MenuItem aboutCreatorItem = new MenuItem("About the Creator");
		aboutCreatorItem.setOnAction((event)->{
			Stage aboutStage = new Stage();
			GridPane aboutRoot = new GridPane();
			Label lb = new Label(" My name is Hasan Amkieh.\n This program is written in java/javafx,\n"
					+ " you can contact me :\n hassan1551@outlook.com");
			aboutRoot.add(lb, 0, 0);
			Button okButton = new Button("Ok");
			okButton.setOnAction((eve)->{
				aboutStage.close();
			});
			aboutRoot.add(okButton, 0, 1);
			Scene aboutScene = new Scene(aboutRoot, 200, 100);
			aboutStage.setScene(aboutScene);
			aboutStage.show();
		});
		
		MenuItem explanationItem = new MenuItem("How this program works?");
		explanationItem.setOnAction((event)->{
			root1.setDisable(true);
			String msg = new String("This program is designed to calculate the Continued\n"
					+ "Fractions. Nevertheless, it saves your time from\n"
					+ "repeating the formula again and again.\n"
					+ "The main formula that this program is using : \n"
					+ "\"(a+1/(b+1/(c+1/(...))))^2\".\n"
					+ "In the textinput, you will fill in\n"
					+ "the numbers that will be stuffed into the formula.\n"
					+ "e.g. You entered 1, 2 into the textfield, You will\n"
					+ "have to choose b/w 3 and 4, assuming that\n"
					+ "1 is a and 2 is b.\n"
					+ "The 3 or 4 determines how many numbers is the\n"
					+ "sequence going to be like.\n"
					+ "For example : if you chose 3, then the\n"
					+ "numbers are : a b 2a\n"
					+ "if you chose 4, numbers are : a b b 2a.\n"
					+ "You also have the combobox, you can\n"
					+ "choose from 10 to 20. This number represents how\n"
					+ "many digits are going to be shown after\n"
					+ "the decimal point in the result's label.");
			Label rootNode = new Label(msg);
			rootNode.setDisable(true);
			rootNode.setTextFill(Color.BLACK);
			rootNode.setFont(new Font(14));
			rootNode.setPadding(new Insets(6, 6, 6, 6));
			Scene exScene = new Scene(rootNode, 340, 600);
			Stage exStage = new Stage();
			exStage.setScene(exScene);
			exStage.setOnCloseRequest((event1)->{
				root1.setDisable(false);
			});
			exStage.setAlwaysOnTop(true);
			exStage.show();
		});
		
		helpMenu.getItems().addAll(aboutCreatorItem, explanationItem);
		mainMenu.getMenus().addAll(helpMenu);
		// mainMenu ; 
		// closeMenu moving ability : 
		closeMenu.addEventHandler(MouseEvent.MOUSE_PRESSED, (event)->{
			isClicked = true;
			System.out.println("Mouse Dragging is enabled!");
			lastPoint[0] = (int)event.getScreenX();
			lastPoint[1] = (int)event.getScreenY();
			System.out.println("Starting from : ("+
			lastPoint[0]+","+lastPoint[1]+")");
		});
		closeMenu.addEventHandler(MouseEvent.MOUSE_DRAGGED, (event)->{
			if (isClicked) {
				int x, y;
				x = (int)(prmStage.getX()+(event.getScreenX()-lastPoint[0]));
				y = (int)(prmStage.getY()+(event.getScreenY()-lastPoint[1]));
				// Convert this to Math.ceiling
				lastPoint[0] = (int)event.getScreenX();
				lastPoint[1] = (int)event.getScreenY();
				prmStage.setX(x);
				prmStage.setY(y);
			};
		});
		
		closeMenu.addEventHandler(MouseEvent.MOUSE_RELEASED, (event)->{
			isClicked = false;
			System.out.println("Finsihing the dragging!");
		});
		// closeMenu moving ability ;
		
		// results : 
		results.setAlignment(Pos.BOTTOM_LEFT);
		Font resultsFont = Font.font("Times New Roman", FontWeight.MEDIUM, 18);
		results.setFont(resultsFont);
		double fivePercent = scene.getWidth()*0.05;
		root2.setMargin(results, new Insets(fivePercent, fivePercent*5,
		fivePercent, fivePercent*5));
		// results ;
		
		// numberInput : 
		TextField numberInput = new TextField();
		numberInput.setPromptText("Enter the value seperated by ','");
		numberInput.setOnKeyTyped((event)->{
			System.out.println("The numberInput text hsa changed!");
			String txt = numberInput.getText();
			if (txt.matches("([1-9]{1}[0-9]*([,]?[1-9]+)*)+")) {
				if (numberInput.getText().isEmpty()) {
					CC.turnOffRadioButtons();
				}
				else {
					int min, max;
					Pattern pattern = Pattern.compile("[,]");
					String[] stringResults = pattern.split(txt);
					int count = stringResults.length;
					min = count*2-1; max = count*2;
					System.out.println(min+" "+max+"\n"+count);
					System.out.println("results are : "+Arrays.toString(stringResults));
					CC.turnOnRadioButtons(min, max);
				}
			}
		});
		EventHandler<ActionEvent> onCalculate = new EventHandler<ActionEvent>() {
		public void handle(ActionEvent event) {
			System.out.println("Starting Calculation : ");
			String result = CC.calculate(numberInput.getText());
			System.out.println("the result is : "+result);
			if (result.startsWith("Error:")) {
				String errorMsg = result.replace("Error: ", "");
				System.out.println("An error has occured : "+errorMsg);
				showError(errorMsg);
			}
			else {
				results.setText(result);
			}
		}};
		numberInput.setOnAction(onCalculate);
		root2.setMargin(numberInput, new Insets(fivePercent, fivePercent*1.5,
		fivePercent/2, fivePercent*1.5));
		// numberInput ;
		
		// SDButton : 
		Button SDButton = new Button("S<->D");
		SDButton.setId("SDButton");
		SDButton.setOnAction((event)->CC.convertToStandard());
		root2.setMargin(SDButton, new Insets(fivePercent*1.5, fivePercent*2,
		fivePercent*1.5, fivePercent*4));
		// SDButton ;
		
		// calcButton : 
		Button calcButton = new Button("Calclate");
		calcButton.setOnAction(onCalculate);
		calcButton.setId("calcButton");
		root2.setMargin(calcButton, new Insets(fivePercent*1.5, fivePercent*4,
		fivePercent*1.5, fivePercent*2));
		// calcButton ;
		
		// scene onResize event necessary calculations, like : 
		scene.widthProperty().addListener((event)->{
			System.out.println("Width of the window has changed");
			double FPercent = scene.getWidth()*0.05;
			
			root2.setMargin(numberInput, new Insets(FPercent, FPercent*1.5,
			FPercent/2, FPercent*1.5));
			
			root2.setMargin(SDButton, new Insets(FPercent*1.5, FPercent*2,
			FPercent*1.5, FPercent*4));
			
			root2.setMargin(calcButton, new Insets(FPercent*1.5, FPercent*4,
			FPercent*1.5, FPercent*2));
			
			root2.setMargin(results, new Insets(fivePercent, fivePercent*6,
			fivePercent, fivePercent*6));
		});
		// ;
		
		primaryStage.initStyle(StageStyle.TRANSPARENT);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Continued Fractions");
		primaryStage.show();
		
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		root2.setTop(numberInput);
		root2.setLeft(SDButton);
		root2.setRight(calcButton);
		root2.setBottom(results);
		root1.add(closeMenu, 0, 0);
		root1.add(mainMenu, 0, 1);
		root1.add(root2, 0, 2);
		
		return ;
	}
	void showError(String errorMsg) {
		Stage errorStage = new Stage();
		BorderPane root = new BorderPane();
		Label msg = new Label(errorMsg);
		root.setTop(msg);
		Button okButton = new Button("Ok");
		root.setBottom(okButton);
		Scene errorScene = new Scene(root, 300, 150);
		errorStage.setScene(errorScene);
		errorStage.setAlwaysOnTop(true);
		errorStage.show();
		root1.setDisable(true);
		okButton.setOnAction((ActionEvent event)->{
			errorStage.close();
			root1.setDisable(false);
		});
		errorStage.setOnCloseRequest((event)->{
			root1.setDisable(false);
		});
	}
	public static void main(String[] args) {
		launch(args);
	}
	private void setCloseMenu(GridPane closeMenu) {
		Button minButton = new Button("-");
		Button closeButton = new Button("X");
		Font font = Font.font("Times New Roman", FontWeight.MEDIUM, 12);
		// closeMenu settings : 
		closeMenu.setBackground(new Background(new BackgroundFill(Color.FORESTGREEN, 
				new CornerRadii(2d, 2d, 2d, 2d, false), new Insets(0))));
		closeMenu.prefWidthProperty().bind(scene.widthProperty());
		// closeMenu Listeners : 
		closeMenu.prefWidthProperty().addListener((oevent, ov, nv)->{
			System.out.println("closeMenu.prefWidth has changed!");
			closeButton.setPrefWidth(0.05*nv.doubleValue());
			minButton.setPrefWidth(0.05*nv.doubleValue());
		});
		closeMenu.prefHeightProperty().addListener((oevent, ov, nv)->{
			System.out.println("closeMenu.prefHeight has changed!");
			//closeButton.setPrefHeight(nv.doubleValue());
			//minButton.setPrefHeight(nv.doubleValue());
		});
		// closeMenu Listeners ; 
		// closeMenu settings ; 
		// closeButton settings : 
		closeButton.setBackground(new Background(new BackgroundFill(
		Color.LIMEGREEN, new CornerRadii(2, 2, 2, 2, false), new Insets(0))));
		closeButton.setFont(font);
		//closeButton.widthProperty().addListener(widthChangeListener);
		closeButton.setTextFill(Color.WHITE);
		closeButton.setPrefSize(closeMenu.getPrefWidth()*0.05, closeMenu.getPrefHeight());
		closeButton.prefHeightProperty().bind(closeMenu.heightProperty());
		closeButton.setOnMouseReleased((event)->{
			System.out.println("The stage is going to close!");
			prmStage.close();
		});
		// closeButton settings ;
		// minButton settings : 
		minButton.setPrefSize(closeMenu.getPrefWidth()*0.05, closeMenu.getPrefHeight());
		minButton.prefHeightProperty().bind(closeMenu.heightProperty());
		minButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
		minButton.setTextFill(Color.WHITE);
		minButton.setBackground(new Background(new BackgroundFill(
		Color.LIMEGREEN, new CornerRadii(2, 2, 2, 2, false), new Insets(0))));
		minButton.setOnMouseReleased((event)->{
			System.out.println("The stage is going to be minimized!");
			prmStage.setIconified(true);
		});
		// minButton settings ;
		ColumnConstraints CC1 = new ColumnConstraints();
		CC1.setHgrow(Priority.ALWAYS);
		CC1.setPrefWidth(scene.getWidth()-minButton.getWidth()-closeButton.getWidth());
		CC1.setHalignment(HPos.RIGHT);
		closeMenu.getColumnConstraints().addAll(CC1);
		closeMenu.add(minButton, 0, 0);
		closeMenu.add(closeButton, 1, 0);
	}
}
