package main;
import java.io.IOException;

import com.ser.blueline.BlueLineException;

import blueline_own.*;

public class Start {
	public static void main(String[]args){
		try {
			Controller controller = new Controller();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BlueLineException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
