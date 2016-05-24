import javax.swing.*;
import BreezySwing.*;
import static java.lang.Math.*;

/*
 * Bugs:
 * Top menu only appears when setSize is called after creating window components in the constructor
 */

/*
 * ToDo:
 * Make all buttons function
 * Error handling for when a or b = 0 in multiplication or division
 * Error handling for dividing by zero (b is zero)
 * Error handling for powers when b is zero
 * Add clear console button and functionality
 */

@SuppressWarnings("serial")
public final class GUI extends GBFrame
{
	//private class constants
	private static final String VERSION_ID = "0.3";
	private static final int SIZE_X = 500;
	private static final int SIZE_Y = 500;
	
	//private variables
	private boolean usingDeg;
	private double lastResultNum;
	private double lastResultError;
	
	//Window Components
	//Window Menu
	private JMenuItem angRadItem;
	private JMenuItem angDegItem;
	
	//Row 1
	private JLabel aNumLabel;
	private JLabel aErrorLabel;
	//Row 2
	private DoubleField aNumField;
	private DoubleField aErrorField;
	private JButton aAnsButton;
	//Row 3
	private JLabel bNumLabel;
	private JLabel bErrorLabel;
	//Row 4
	private DoubleField bNumField;
	private DoubleField bErrorField;
	private JButton bAnsButton;
	//Row 5
	private JButton addButton;
	private JButton subtractButton;
	private JButton multButton;
	private JButton divButton;
	private JButton powButton;
	//Row 6
	private JButton sinButton;
	private JButton cosButton;
	private JButton tanButton;
	private JButton squareButton;
	private JButton squareRootButton;
	//Row 7
	private JTextArea console;
	//DEBUG
	private JLabel debugLabel;
	
	
	/**
	 * Standard constructor
	 */
	public GUI()
	{
		usingDeg = false;
		lastResultNum = 0;
		lastResultError = 0;
		
		angRadItem = addMenuItem("Angle Mode", "Radians");
		angDegItem = addMenuItem("Angle Mode", "Degrees");
		
		//row 1
		aNumLabel = addLabel("a", 1, 1, 2, 1);
		aErrorLabel = addLabel("a error", 1, 3, 2, 1);
		//row 2
		aNumField = addDoubleField(0, 2, 1, 2, 1);
		aErrorField = addDoubleField(0, 2, 3, 2, 1);
		aAnsButton = addButton("Ans", 2, 5, 1, 1);
		//row 3
		bNumLabel = addLabel("b", 3, 1, 2, 1);
		bErrorLabel = addLabel("b error", 3, 3, 2, 1);
		//row 4
		bNumField = addDoubleField(0, 4, 1, 2, 1);
		bErrorField = addDoubleField(0, 4, 3, 2, 1);
		bAnsButton = addButton("Ans", 4, 5, 1, 1);
		//Row 5
		addButton = addButton("+", 5, 1, 1, 1);
		subtractButton = addButton("-", 5, 2, 1, 1);
		multButton = addButton("*", 5, 3, 1, 1);
		divButton = addButton("/", 5, 4, 1, 1);
		powButton = addButton("a^b", 5, 5, 1, 1);
		//Row 6
		sinButton = addButton("sin(a)", 6, 1, 1, 1);
		cosButton = addButton("cos(a)", 6, 2, 1, 1);
		tanButton = addButton("tan(a)", 6, 3, 1, 1);
		squareButton = addButton("a^2", 6, 4, 1, 1);
		squareRootButton = addButton("sqrt(a)", 6, 5, 1, 1);
		//Row 7
		console = addTextArea("Propagated Error Calculator v" + VERSION_ID + "\n", 7, 1, 5, 10);
		console.setEditable(false);
		//DEBUG
		debugLabel = addLabel("INDEV v" + VERSION_ID , 18, 1, 1, 1);
		
		this.setSize(SIZE_X, SIZE_Y);
		this.setTitle("Propagated Error Calculator v" + VERSION_ID);
	}
	
	//event handler methods ---------------------------------------------------------------------
	 
	@Override
	public void buttonClicked(JButton buttonObj)
	{
		if (buttonObj == aAnsButton)
		{	
			aNumField.setNumber(lastResultNum);
			aErrorField.setNumber(lastResultError);
		}
		else if (buttonObj == bAnsButton)
		{
			bNumField.setNumber(lastResultNum);
			bErrorField.setNumber(lastResultError);
		}
		else if (buttonObj == addButton)
		{
			if (checkAllInputs())
			{
				lastResultNum = add();
				lastResultError = addError();
				consolePrintResults();
			}
		}
		else if (buttonObj == subtractButton)
		{
			if (checkAllInputs())
			{
				lastResultNum = subtract();
				lastResultError = subtractError();
				consolePrintResults();
			}
		}
		else if (buttonObj == subtractButton)
		{
			if (checkAllInputs())
			{
				lastResultNum = subtract();
				lastResultError = subtractError();
				consolePrintResults();
			}
		}
		else if (buttonObj == multButton)
		{
			if (checkAllInputs())
			{
				lastResultNum = multiply();
				lastResultError = multiplyError();
				consolePrintResults();
			}
		}
		else if (buttonObj == divButton)
		{
			if (checkAllInputs())
			{
				lastResultNum = divide();
				lastResultError = divideError();
				consolePrintResults();
			}
		}
		else if (buttonObj == powButton)
		{
			if (checkAllInputs())
			{
				lastResultNum = power();
				lastResultError = powerError();
				consolePrintResults();
			}
		}
		else
			messageBox("Error: Button " + buttonObj.toString() + " not implemented!");
	}

	@Override
	public void menuItemSelected(JMenuItem menuItem)
	{
		if (menuItem == angRadItem)
		{
			usingDeg = false;
			consolePrintln("angle mode: radians");
		}
		else if (menuItem == angDegItem)
		{
			usingDeg = true;
			consolePrintln("angle mode: degrees");
		}
		else
			messageBox("Error: Menu item " + menuItem.toString() + " not implemented!");
	}
	
	//console methods ---------------------------------------------------------------------------
	
	private void consolePrint(String s)
	{
		console.append(s);
		//I got this of the Internet, should automatically scroll the console when necessary
		console.setCaretPosition(console.getDocument().getLength());
	}
	
	private void consolePrintln(String s)
	{
		consolePrint(s + "\n");
	}
	
	private void consolePrintResults()
	{
		consolePrintln(lastResultNum + " +- " + lastResultError);
	}
	
	//error checking methods ---------------------------------------------------------------------
	
	private boolean allFieldsValid()
	{
		return allAFieldsValid() && allBFieldsValid();
	}
	
	private boolean allAFieldsValid()
	{
		return aNumField.isValidNumber() && aErrorField.isValidNumber();
	}
	
	private boolean allBFieldsValid()
	{
		return bNumField.isValidNumber() && bErrorField.isValidNumber();
	}
	
	/**
	 * Checks all input fields and throws an error message via message box if appropriate
	 * @return true if all input fields are valid
	 */
	private boolean checkAllInputs()
	{
		if (allFieldsValid())
			return true;
		else
		{
			if(!aNumField.isValidNumber())
				messageBox("Error: a does not contain a valid number", 400, 125);
			else if (!aErrorField.isValidNumber())
				messageBox("Error: a error does not contain a valid number", 400, 125);
			else if (!bNumField.isValidNumber())
				messageBox("Error: b does not contain a valid number", 400, 125);	
			else if (!bErrorField.isValidNumber())
				messageBox("Error: b error does not contain a valid number", 400, 125);
			
			return false;
		}
	}
	
	/**
	 * Checks all a input fields and throws an error message via message box if appropriate
	 * @return true if all a input fields are valid
	 */
	private boolean checkAInputs()
	{
		if (allAFieldsValid())
			return true;
		else
		{
			if(!aNumField.isValidNumber())
				messageBox("Error: a does not contain a valid number");
			else if (!aErrorField.isValidNumber())
				messageBox("Error: a error does not contain a valid number");
		
			return false;
		}
	}
	
	//math methods -------------------------------------------------------------------------------
	//Inline basic operations?
	private double add()
	{
		return aNumField.getNumber() + bNumField.getNumber();
	}
	
	private double addError()
	{
		return sqrt(pow(aErrorField.getNumber(), 2) + pow(bErrorField.getNumber(), 2));
	}
	
	private double subtract()
	{
		return aNumField.getNumber() - bNumField.getNumber();
	}
	
	private double subtractError()
	{
		return addError();
	}
	
	private double multiply()
	{
		return aNumField.getNumber() * bNumField.getNumber();
	}
	
	private double multiplyError()
	{
		// multiply() * sqrt( (aError / a)^2 + (bError / b)^2 )
		return multiply() * sqrt( pow(aErrorField.getNumber() / aNumField.getNumber(), 2) + pow(bErrorField.getNumber() / bNumField.getNumber(), 2) );
	}
	
	private double divide()
	{
		// divide() * sqrt( (aError / a)^2 + (bError / b)^2 )
		return aNumField.getNumber() / bNumField.getNumber(); 
	}
	
	private double divideError()
	{
		return divide() * sqrt( pow(aErrorField.getNumber() / aNumField.getNumber(), 2) + pow(bErrorField.getNumber() / bNumField.getNumber(), 2) );
	}
	
	//not to be confused with Math.pow()
	private double power()
	{
		return pow(aNumField.getNumber(), bNumField.getNumber());
	}
	
	private double powerError()
	{
		return power() * aErrorField.getNumber() / aNumField.getNumber();
	}
}
