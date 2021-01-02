package application;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Vector;


import javafx.scene.control.Labeled;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;

public class CalculationCore {
	short limit = 10;
	// the limit of numbers that will be shown after the calculation.
	double firstNum = -1;
	short continiousLimit = 37;
	short originalContiniousLimit = continiousLimit;
	Vector<Double> dbs = new Vector<Double>(7);
	// how many times is the continued fraction will be calculated!
	BigDecimal finalResult = new BigDecimal(0);
	FlowPane child = new FlowPane();
	Label resultLabel;
	CalculationCore(BorderPane parent, Label lb) {
		resultLabel = lb;
		parent.setCenter(child);
		child.setVgap(5);
		child.setHgap(5);
		RadioButton RB1 = new RadioButton("NA");
		RadioButton RB2 = new RadioButton("NA");
		ToggleGroup TG1 = new ToggleGroup();
		RB1.setToggleGroup(TG1);
		RB2.setToggleGroup(TG1);
		RB1.setDisable(true);
		RB2.setDisable(true);
		RB1.setCursor(javafx.scene.Cursor.HAND);
		RB2.setCursor(javafx.scene.Cursor.HAND);
		ChoiceBox limitBox = new ChoiceBox();
		for (int i = 10 ; i!=21 ; i++) {
			limitBox.getItems().add(i);
		}
		limitBox.setOnAction((event)->{
			System.out.println("limitBox action event is fired!");
			if (limitBox.getSelectionModel().getSelectedIndex()!=limit-10) {
				System.out.println("The value of limitBox has changed!");
				limit = (short)(limitBox.getSelectionModel().getSelectedIndex()+10);
				// checking & reshowing : 
				if (resultLabel.getText().isEmpty()) {
					System.out.println("The text is empty! nothing to update!");
				}
				else {
					System.out.println("Updating the text limit after decimals!");
					resultLabel.setText(truncateDecimals(finalResult));
					;
				}
			}
			;
		});
		limitBox.setValue(limitBox.getItems().get(limit-10));
		
		child.getChildren().addAll(RB1, RB2, limitBox);
	}
	String truncateDecimals(BigDecimal number) {
		// Truncate , return ..
		String str = number.toString();
		if (str.length()-str.indexOf('.')-1<=limit) {
			System.out.println("No need to truncate the string!");
			return str;
		}
		else {
			return str.substring(0, str.indexOf('.')+limit+1);
		}
	}
	double[] convertToDoubles(String[] text) {
		double[] doubles = new double[text.length];
		int i = 0;
		for (String str : text) {
			doubles[i] = Double.parseDouble(str);
			i++;
		}
		return doubles;
	} 
	boolean check(boolean[] checks) {
		boolean condition = false;
		short count = 0; 
		for (boolean ind : checks) {
			if (ind) {
				count++;
			}
		}
		if (count==checks.length) {
			condition = true;
		}
		
		return condition;
	}
	
	int[] analyzeDecimal() { // will return int[2]
		int[] analysis = new int[2];
		int dot = finalResult.toString().indexOf(".");
		System.out.println("dot : "+dot);
		String num = finalResult.toString().substring(0, dot+1+14);
		int state = -1;
		int firstPart = 0; // first part will be compared with each secondPart!
		int secondPart = 0; 
		String part = ""; // without the dot
		int index = 0; // index for every loop!
		int loop = 6;
		int originalLoop = loop;
		int size = 0; // until six!
		boolean[] checks = new boolean[10]; // 10 compares for each part!
		// length of checks is 10!
		int continues = -1; // -1 means there is no continues part!
		boolean stop = false;
		// TODO : check for the end of the part to save from any errors!
		while (true) { // while for size!
			if (size!=6) {
			while (true) { // we need to compare a part with a part!
				// while for loop
				// 6 loops, every loop will increase the compared part by 1!
				if (index!=checks.length) { // TODO : check every part with the increasing pos
					firstPart = dot+1-(originalLoop-loop);
					part = num.substring(firstPart, firstPart+(size+1));
					int partSize = part.length();
					System.out.println("firstPart : "+firstPart+"\npartSize : "+partSize);
					while (index!=checks.length) { // while for index!
						secondPart = firstPart+partSize+index;
						String comparedPart = num.substring(secondPart, secondPart+partSize);
						if (part==comparedPart) {
							System.out.println(part+" == "+comparedPart);
							checks[index] = true;
						}
						else {
							System.out.println(part+" != "
							+comparedPart);
							checks[index] = false;
						}
						index++;
					}
					if (check(checks)) {
						stop = true;
						continues = firstPart;
						System.out.println("Found a match : ");
						System.out.println("Pos : "+);
					}
				}
				else if (loop!=0) { // reset everything : 
					for (int i = 0 ; i!=10 ; i++) {
						checks[i] = false;
					}
					index = 0;
				}
				else { // finished!
					System.out.println("Finished with : ");
					System.out.println("last part : "+part);
					System.out.println("firstpart p : "+firstPart+"\nsecondPart p : "+secondPart);
					break;
				}
				loop--;
			}
			loop = originalLoop;
			size++;
			}
			else {
				break;
			}
		}
		if (continues==-1) {
			state = 2;
		}
		else {
			if (dot+1==continues) {
				state = 1;
			}
			else {
				state = 0;
			}
		}
		System.out.println("state : "+state);
		analysis[0] = state;
		analysis[1] = continues;
		
		return analysis;
	}
	// TODO complete it : 
	String convertToStandard() {
		// determine the state first : 
		System.out.println("FinalResult is : "+finalResult);
		int[] analysis = analyzeDecimal();
		if (analysis[0]==0) { // constant + continues
			;
		}
		else if (analysis[0]==1) { // continues 
			;
		}
		else if (analysis[0]==2) { // constant
			;
		}
		else {
			System.out.println("An error has occured at convertToStandard function...\n"
					+ "The decimal state determination is wrong...");
		}
		;
		
		return "ToStandard";
	}
	void turnOnRadioButtons(int min, int max) {
		((Labeled)child.getChildren().get(0)).setText(min+"");
		((Labeled)child.getChildren().get(1)).setText(max+"");
		child.getChildren().get(0).setDisable(false);
		child.getChildren().get(1).setDisable(false);
	}
	void turnOffRadioButtons() {
		((Labeled)child.getChildren().get(0)).setText("NA");
		((Labeled)child.getChildren().get(1)).setText("NA");
		child.getChildren().get(0).setDisable(true);
		child.getChildren().get(1).setDisable(true);
	}
	String writeEquation(double[] doubles, int size) {
		double[] numbersInEquation = new double[size];
		StringBuilder equation = new StringBuilder();
		int originalSize = size;
		{
		boolean secondRound = false;
		while (true) {
			size--;
			if (size==0&&originalSize!=1) {
				System.out.println("Last number!");
				numbersInEquation[originalSize-1] = 2*doubles[0];
				break;
			}
			else if (size==0&&originalSize==1) {
				System.out.println("Last number!");
				numbersInEquation[originalSize-1] = doubles[0];
				break;
			}
			if (originalSize%2==0) { // even
				if (!secondRound) {
					numbersInEquation[originalSize-size-1] = doubles[originalSize-size-1];
				}
				else {
					numbersInEquation[originalSize-size-1] = doubles[size];
				}
				if (size==originalSize/2) { // middle reached
					secondRound = true;
				}
			} 
			else { // odd
				if ((originalSize-1)/2==size) { // the middle
					secondRound = true;
					numbersInEquation[(originalSize-1)/2] = doubles[(originalSize-1)/2];
				}
				else if (!secondRound) { // first middle
					numbersInEquation[originalSize-size-1] = doubles[originalSize-size-1];
				}
				else { // second middle
					numbersInEquation[originalSize-size-1] = doubles[size];
				}
			}
		}
		}
		System.out.println("numbersInEquation : "+Arrays.toString(numbersInEquation));
		System.out.println("Writing the equation : ");
		int count = numbersInEquation.length-2+1;
		// TODO : use the continiousLimit here :
		int index = 0;
		boolean once = false;
		for (double number : numbersInEquation) {
			if (index==0&&!once) { // first element
				once = true;
				if (originalSize==1) {
					System.out.println("the original size is 1!");
					equation.append("("+number);
					break;
				}
				else {
					equation.append("(");
				}
			}
			else if (index>0&&index<numbersInEquation.length-1) { // in the middle
				equation.append("1/("+number+"+");
			}
			else { // last element
				System.out.println("Last element!");
				equation.append("1/("+number);
				while (count!=0) {
					count--;
					equation.append(")");
				}
			}
			index++;
			System.out.println("The equation is : "+equation);
	}
	if (originalSize!=1) {
		firstNum = doubles[0];
		repeatEquation(equation);
	}
	else {
		equation.append(")^2");
	}
	
	System.out.println("The equation is : "+equation);
	return equation.toString();
	}
	
	void repeatEquation(StringBuilder equation) {
		System.out.println("Repeating the equation "+continiousLimit+" times.");
		short originalContiniousLimit = continiousLimit;
		BigDecimal lastResult = new BigDecimal(0); // as x!
		while (continiousLimit!=0) {
			continiousLimit--;
			System.out.println("---\nlast result before : "+lastResult.doubleValue());
			if (continiousLimit==0) { // last
				System.out.println("Finishing the repeatEquation method!");
				System.out.println("EQUATION : "+equation);
				equation.insert(1, firstNum+"+");
				equation.append(")^2");
			}
			if (continiousLimit+1==originalContiniousLimit) { // first
				lastResult = new BigDecimal(evaluateEquation(equation.toString()));
				dbs.add(lastResult.doubleValue());
			}
			else if (continiousLimit+2==originalContiniousLimit) { // second 
				equation.insert(lastBrackets(equation.toString())[1], "+"+lastResult.doubleValue());
				System.out.println(equation);
				lastResult = new BigDecimal(evaluateEquation(equation.toString()));
				dbs.add(lastResult.doubleValue());
			}
			// not first, second nor last
			else if (continiousLimit<originalContiniousLimit) { 
				System.out.println("Not first nor last or second!");
				int[] x = lastBrackets(equation.toString());
				equation.replace(lastPlus(equation.toString())+1, x[1], lastResult.doubleValue()+"");
				System.out.println("3 : "+equation);
				lastResult = new BigDecimal(evaluateEquation(equation.toString()));
				dbs.add(lastResult.doubleValue());
				System.out.println(lastResult.doubleValue());
			}
			System.out.println("---\nlast result after : "+lastResult.doubleValue());
		}
		System.out.println(equation);
		
		return ;
	}
	int lastPlus(String equation) {
		int lastFoundA = 0;
		while (true) { 
			if (equation.indexOf('+', lastFoundA+1)!=-1) {
				lastFoundA = equation.indexOf('+', lastFoundA+1);
			}
			else {
				break;
			}
		}
		return lastFoundA;
	}
	int[] lastBrackets(String equation) { // will return an integer array contains {start, end}
		int lastFoundA = 0;
		while (true) { 
			if (equation.indexOf('(', lastFoundA+1)!=-1) {
				lastFoundA = equation.indexOf('(', lastFoundA+1);
			}
			else {
				break;
			}
		}
		int firstFoundB = 0;
		firstFoundB = equation.indexOf(')');
		System.out.println(lastFoundA+" : "+firstFoundB);
		int[] array = {lastFoundA, firstFoundB};
		
		return array;
	}
	int countBrackets(StringBuilder e) {
		int count = 0;
		String equation = e.toString();
		int lastFound = 0;
		while (true) { 
			if (equation.indexOf('(', lastFound+1)!=-1) {
				count++;
				lastFound = equation.indexOf('(', lastFound+1);
			}
			else {
				break;
			}
		}
		
		return count;
	}
	String evaluateEquation(String e) {
		// we will truncate the brackets into pieces and eval every piece, 
		// and then return it without brackets by replacing it inside the equation!
		StringBuilder equation = new StringBuilder(e);
		int count = countBrackets(equation);
		int[] pos;
		String part;
		String result;
		// TODO : fix here : 
		BigDecimal tempResult = new BigDecimal(0);
		while (count!=0) { // every one loop, it will truncate, evaluate, replace the new value!
			pos = lastBrackets(equation.toString());
			part = equation.substring(pos[0], pos[1]+1);
			if (lastBrackets(part)[1]==-1) {
				part += ")";
			}
			tempResult = new BigDecimal(new Main().eval(part));
			result = truncateDecimals(tempResult);
			equation.replace(pos[0], pos[1]+1, result);
			count--;
		}
		part = equation.substring(0, equation.length());
		if (lastBrackets(part)[1]==-1) {
			part += ")";
		}
		finalResult = new BigDecimal(new Main().eval(part));
		return truncateDecimals(finalResult);
	}
	String calculate(String numbersText) {
		if (numbersText.matches("([1-9]{1}[0-9]*([,]?[1-9]{1}[0-9]*)*)+")) {
			finalResult = new BigDecimal(0);
			String[] text = numbersText.split(",");
			double[] numbers = convertToDoubles(text);
			System.out.println("The doubles are : "+Arrays.toString(numbers));
			int min = numbers.length*2-1, max = numbers.length*2;
			System.out.println("min, max : "+min+" "+max);
			String equation;
			if (((RadioButton)child.getChildren().get(0)).isSelected()) {
				equation = writeEquation(numbers, Integer.parseInt(((Labeled)child.getChildren().get(0)).getText()));
			}
			else if (((RadioButton)child.getChildren().get(1)).isSelected()) {
				equation = writeEquation(numbers, Integer.parseInt(((Labeled)child.getChildren().get(1)).getText()));
			}
			else {
				equation = writeEquation(numbers, min);
			}
			System.out.println("The equation is : "+equation);
			String result = evaluateEquation(equation);
			System.out.println("The result is : "+result);
			continiousLimit = originalContiniousLimit;
			System.out.println(dbs);
			
			return result;
		}
		else {
			return "Error: The input syntax is fault!";
		}
	}
}
