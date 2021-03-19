
import java.io.*;
import java.util.*;
import org.json.simple.*;
import org.json.simple.parser.*;

public class Main{

	public static void main(String[] args){

		//read the initial input json file
		JSONParser parser=new JSONParser();
		Object obj;
		try{

			obj=parser.parse(new FileReader(args[0]));

			//get config info
			JSONObject jsonobject=(JSONObject) obj;
					
			JSONObject config=(JSONObject)jsonobject.get("config");
			long rows=(Long)config.get("rows");
			
			String columns=(String) config.get("columns");
			int col=Integer.parseInt(columns);
			


			//get item info
			JSONArray items=(JSONArray)jsonobject.get("items");
			
			
			//put item info into array list
			ArrayList<String>Inventory=new ArrayList<String>();
			
			if(items!=null){
				
				for(int i=0; i< items.size();i++){
					
					Inventory.add(items.get(i).toString());
					
				}
			}
			
			//create array list holding all item info with strings and long for price and the location(or button combo for the item in the machine)
			//assuming max # of different items in the machine is # of rows*col
			//create arraylist to hold log of actions
			ArrayList<String>actionlog=new ArrayList<String>();
			int maxnum=(int)(rows*col);
			
			//check if inventory size isn't greater than maxnum, if there is add it to the actionlog as an error, print the action log and return
			if(maxnum<Inventory.size()){
				actionlog.add("Error: Items that were to be loaded into vending machince inventory exceeded max inventory space(rows*columns). Vending Machine program terminated as a result.");
				printactionlog(actionlog);
				return;
			}
			
			ArrayList<Items>inmachine=new ArrayList<Items>();
			int row=1;
			int count=1;
			//add info to inmachine
			for(int i=0; i<Inventory.size(); i++){
					
					String info=Inventory.get(i);
					String temp;
					String substring;
					int amount;
					String price;
					String name;
					String location;
					//first get index of : which is right after amount in the string
					int firstin=info.indexOf(":");
					int incomma=info.indexOf(",");
					//substring info to get amount and convert to int
					temp=info.substring(firstin+1, incomma);
					
					//convert amount to int
					amount=Integer.parseInt(temp);
					
					//substring info some amount isn't in the string
					substring=info.substring(incomma+1);
					
					//get index of : and ,
					firstin=substring.indexOf(":");
					incomma=substring.indexOf(",");
					temp=substring.substring(firstin+3, incomma-1);
					price=temp;
					
					temp=substring.substring(incomma+2);
					
					//get index of :
					firstin=temp.indexOf(":");
					//substring so string starts at item name
					substring=temp.substring(firstin+2);
					
					//get index of "
					int quote=substring.indexOf("\"");
					//substring name
					temp=substring.substring(0, quote);
					name=temp;
					
					String loclet=getletter(row);
					String locnum=String.valueOf(count);
					String loc=loclet.concat(locnum);
					
					//add items to inmachine
					inmachine.add(new Items(name, amount, price, loc));
					if(count==col){
						
						//reset count
						count=0;
						row++;
					}
					count++;
			}
			
			
			
			//boolean determining if the user wants to quit the program
			boolean quit=false;
			
			//set up scanner to get user input
			Scanner sc=new Scanner(System.in);
			
			while(quit!=true){
				
				System.out.println();
				printinventory(inmachine);
				actionlog.add("Printed inventory for user to see so, they could decide what they would like to do.");
				
				//ask user if they want to make a purchase and log action
				System.out.println("Would you like to make a purchase? Y/N");
				actionlog.add("Asked user if they would like to make a purchase.");
				//get user input on purchase
				String makepurchase=sc.nextLine();
				
				if(makepurchase.equals("Y")){
					
					actionlog.add("User decided to make a purchase.");
					System.out.println("Please enter the location of the item you would like to purchase.");
					actionlog.add("Asked user to enter the location of the item they'd like to purchase.");
					String location=sc.nextLine();
					System.out.println("Please enter your payment amount in United States Dollars. (e.g. $1.00)");
					actionlog.add("Asked user for their payment amount in USD.");
					String payment=sc.nextLine();

					purchase(location, payment, inmachine, actionlog);
					
				}else if(makepurchase.equals("N")){
					
					actionlog.add("User decided not to make a purchase.");
					
					//ask if user would like to see the inventory again
					System.out.println("Would you like to view what is in the vending machine again? Y/N");
					actionlog.add("Asked user if they wanted to view the vending machine inventory again.");
					String viewin=sc.nextLine();
					
					if(viewin.equals("Y")){
						
						actionlog.add("User chose to view the machine inventory again.");
						printinventory(inmachine);
						actionlog.add("Printed machine inventory for user to see.");
						
					}else if(viewin.equals("N")){
						
						actionlog.add("User chose not to view inventory again.");
						//ask if user would like to update the machine inventory
						System.out.println("Would you like to update vending machine inventory? Y/N");
						actionlog.add("Asked user if they would like to update machine inventory.");
						String update=sc.nextLine();
						
						if(update.equals("Y")){
							
							actionlog.add("User decided to update machine inventory.");
							//ask user for update file
							System.out.println("Please enter the name of the JSON file that will be used to update the inventory, if it is in the same directory as this program. Otherwise please enter the file path of the JSON file that will be used to update the inventory.");
							String filepath=sc.nextLine();
							actionlog.add("Asked user for file/filepath of the json file they want to use to update the inventory.");
							//check that file is a json file
							actionlog.add("Checked if user input file/filepath was a json file.");
							if(filepath.contains(".json")!=true){
								System.out.println("Error: file entered is not a JSON file. Unable to update inventory.");
								actionlog.add("Error: file entered is not a JSON file. Unable to update inventory.");
							}else{
								
								actionlog.add("Begining inventory update.");
								updateinventory(filepath, inmachine, actionlog);
								actionlog.add("Machine inventory updated.");
								
							}
							
						}else if(update.equals("N")){
							
							actionlog.add("User decided not to update machine inventory.");
							//ask if user would like to view the action log
							System.out.println("Would you like to view the action log? Y/N");
							actionlog.add("Asked user if they would like to view the action log.");
							String viewlog=sc.nextLine();
							
							if(viewlog.equals("Y")){
								
								actionlog.add("User decided to view the action log.");
								printactionlog(actionlog);
								actionlog.add("Printed action log for user to view.");
								
							}else if(viewlog.equals("N")){
								
								actionlog.add("User decided not to view the action log.");
								//ask if the user would like to quit
								System.out.println("Would you like to quit using the vending machine? Y/N");
								actionlog.add("Asked user if they would like to quit using the vending machine.");
								String quitp=sc.nextLine();
								
								if(quitp.equals("Y")){
									
									actionlog.add("User chose to quit the program.");
									quit=true;
									
								}else if(quitp.equals("N")){
									
									actionlog.add("User chose not to quit the program.");
									
								}else{
									
									actionlog.add("Error: User entered improper response to if they'd like to quit (entered a response that wasn't Y or N).");
								}
								
							}else{
								actionlog.add("Error: User entered improper response to if they'd like to view the action log (entered a response that wasn't Y or N).");
							}
						}else{
							actionlog.add("Error: User entered improper response to if they'd like to update machine inventory (entered a response that wasn't Y or N).");
						}
					}else{
						actionlog.add("Error: User entered improper response to if they'd like to view machine inventory again (entered a response that wasn't Y or N).");
					}
					
					
					
					
					
					
				}else{
					actionlog.add("Error: User entered improper response to if they'd like to make a purchase (entered a response that wasn't Y or N).");
				}
				
			}

		}catch(Exception e){

			e.printStackTrace();
		}

		return;
	}

//will take a row number and convert it to a letter
public static String getletter(int row){
	
	if(row==1){
		 return "A";
	}else if(row==2){
		return"B";
	}else if(row==3){
		return"C";
	}else if(row==4){
		return"D";
	}else if(row==5){
		return"E";
	}else if(row==6){
		return"F";
	}else if(row==7){
		return "G";
	}else if(row==8){
		return"H";
	}else if(row==9){
		return"I";
	}else if(row==10){
		return"J";
	}else if(row==11){
		return"K";
	}else if(row==12){
		return"L";
	}else if(row==13){
		return"M";
	}else if(row==14){
		return"N";
	}else if(row==15){
		return"O";
	}else if(row==16){
		return"P";
	}else if(row==17){
		return"Q";
	}else if(row==18){
		return"R";
	}else if(row==19){
		return"S";
	}else if(row==20){
		return"T";
	}else if(row==21){
		return"U";
	}else if(row==22){
		return"V";
	}else if(row==23){
		return"W";
	}else if(row==24){
		return"X";
	}else if(row==25){
		return"Y";
	}else{
		return"Z";
	}
}

//used to print the info from the vending machine's inventory so user can see what is in the machine
public static void printinventory(ArrayList<Items> inventory){
	
	System.out.println("The following are stocked in the vending machine:");
	
	for(int i=0; i<inventory.size();i++){
		
		//get the item info from arraylist
		Items item=inventory.get(i);
		
		//print item info
		System.out.println("Name: "+ item.getitemname());
		System.out.println("Location: "+item.getlocation());
		System.out.println("Price: $"+item.getprice());
		System.out.println("Amount: "+item.getamount());
		System.out.println();
	}
	
	System.out.println("Please note that the location is what you need to enter to select that item and that if an item has an amount of 0, it is currently sold out.");
	return;
}

public static void printactionlog(ArrayList<String>actionlog){
	
	System.out.println("Action Log:");
	System.out.println();
	
	for(int i=0; i<actionlog.size();i++){
		
		System.out.println(actionlog.get(i));
	}
	
	return;
}

//make a purchase from the vending machine
public static void purchase(String location, String payment, ArrayList<Items>items, ArrayList<String> actionlog){
	
	System.out.println("Begining purchase");
	
	actionlog.add("Checked if payment contains $");
	if(payment.contains("$")!=true){
		System.out.println("Payment amount given in improper format. Unable to complete purchase.");
		actionlog.add("Payment amount given in improper format. Unable to complete purchase.");
		return;
	}
	
	for(Items i: items){
		
		if(i.getlocation().equals(location)){
			
			//check if there are 0 of what the user wanted in the machine
			System.out.println("Checking amount of item in the vending machine.");
			
			if(i.getamount()==0){
				System.out.println("There are currently 0 of "+i.getitemname()+" in the vending machine. This item is unfortantely sold out we are unable to complete your purchase. Your payment will be refunded.");
				actionlog.add("User tried to purchase "+i.getitemname()+" but it was sold out. Unable to complete their purchase. Their payment of "+payment+" was refunded to them.");
				return;
			}
			
			//remove convert to double for price
			double price=Double.parseDouble(i.getprice());
			
			//do same for payment
			double paying=Double.parseDouble(payment.substring(1));
			
			//check if payment is <,>,= to price
			System.out.println("Checking if payment amount is enough to make purchase");
			if(paying==price){
				System.out.println("Payment is exact amount; change will be $0.00.");
				System.out.println("Updating amount in inventory");
				//get original amount in inventory
				int qty=i.getamount();
				//subtract 1
				qty--;
				//update amount in inventory with new amount
				i.setamount(qty);
				//update action log with transaction info
				actionlog.add("User purchased a "+i.getitemname()+" for "+payment+". Their change was $0.00.");
				actionlog.add(i.getitemname()+" amount in inventory was updated to "+i.getamount());
				System.out.println("Purchase completed.");
				return;
				
			}else if(paying>price){
				
				System.out.println("Payment is greater than price of "+i.getitemname()+". Calculating change.");
				//calculate change
				double change=paying-price;
				System.out.println("Your change is $"+change+".");
				System.out.println("Updating amount in inventory.");
				//update amount in inventory
				int qty=i.getamount();
				qty--;
				i.setamount(qty);
				//update action log
				actionlog.add("User purchased a "+i.getitemname()+" for "+payment+". Their change was $"+change+".");
				actionlog.add(i.getitemname()+" amount in inventory was updated to "+i.getamount());
				System.out.println("Purchase completed.");
				
				return;
				
			}else if(paying<price){
				
				//not enough money
				System.out.println("Payment amount entered is less than the price of "+i.getitemname()+". Unable to complete purchase due to insufficent funds. Your payment of "+payment+" will be refunded.");
				actionlog.add("User tried to purchase "+i.getitemname()+" but their payment was insufficent. Unable to complete their purchase. Their payment of "+payment+" was refunded.");
				
				return;
			}
			
			
		}
	}
	//no item at that location in the machine
	System.out.println("There is no item in the vending machine at that location. Unable to complete purchase. Your payment of "+payment+" will be refunded.");
	actionlog.add("User entered a location with no item in it. Unable to complete purchase. Their payment of "+payment+" was refunded.");
	return;
	
}

//will be used to update machine inventory from a json file; assuming the json file is like the orginal input with config and all the items in the inventory, assuming it takes what was already in inventory into account or else user is choosing to restock
//will replace already existing inventory
public static void updateinventory(String input, ArrayList<Items>inventory, ArrayList<String>actionlog){
	
	JSONParser parser=new JSONParser();
	Object obj;
	
	try{
		
		obj=parser.parse(new FileReader(input));

		//get config info
		JSONObject jsonobject=(JSONObject) obj;
				
		JSONObject config=(JSONObject)jsonobject.get("config");
		//print for error checking
		//System.out.println("config: "+config);
		long rows=(Long)config.get("rows");
		//System.out.println("rows: "+rows);
		String columns=(String) config.get("columns");
		int col=Integer.parseInt(columns);
		//System.out.println("columns: "+col);


		//get item info
		JSONArray items=(JSONArray)jsonobject.get("items");
		//System.out.println("items: "+items);
		
		//put item info into array list
		ArrayList<String>Inventory=new ArrayList<String>();
		
		if(items!=null){
			
			for(int i=0; i< items.size();i++){
				
				Inventory.add(items.get(i).toString());
				//System.out.println(Inventory.get(i));
			}
		}
		
		//create array list holding all item info with strings and long for price and the location(or button combo for the item in the machine)
		//assuming max # of different items in the machine is # of rows*col

		int maxnum=(int)(rows*col);
		
		//check if inventory size isn't greater than maxnum, if there is add it to the actionlog as an error, print the action log and return
		if(maxnum<Inventory.size()){
			actionlog.add("Error: Items that were to be loaded into vending machince inventory exceeded max inventory space(rows*columns). Vending Machine program terminated as a result.");
			printactionlog(actionlog);
			return;
		}
		
		ArrayList<Items>inmachine=new ArrayList<Items>();
		int row=1;
		int count=1;
		//add info to inmachine
		for(int i=0; i<Inventory.size(); i++){
				
				String info=Inventory.get(i);
				String temp;
				String substring;
				int amount;
				String price;
				String name;
				String location;
				//first get index of : which is right after amount in the string
				int firstin=info.indexOf(":");
				int incomma=info.indexOf(",");
				//substring info to get amount and convert to int
				temp=info.substring(firstin+1, incomma);
				//print to check substring correctly
				//System.out.println(temp);
				//convert amount to int
				amount=Integer.parseInt(temp);
				//inmachine.get(i).setamount(amount);
				//substring info some amount isn't in the string
				substring=info.substring(incomma+1);
				//print to check if substring correctly
				//System.out.println(substring);
				//get index of : and ,
				firstin=substring.indexOf(":");
				incomma=substring.indexOf(",");
				temp=substring.substring(firstin+3, incomma-1);
				//price=Double.parseDouble(temp);
				price=temp;
				//check substring
				//System.out.println(price);
				temp=substring.substring(incomma+2);
				//System.out.println(temp);
				//get index of :
				firstin=temp.indexOf(":");
				//substring so string starts at item name
				substring=temp.substring(firstin+2);
				//System.out.println(substring);
				//get index of "
				int quote=substring.indexOf("\"");
				//substring name
				temp=substring.substring(0, quote);
				name=temp;
				//System.out.println(name);
				String loclet=getletter(row);
				String locnum=String.valueOf(count);
				String loc=loclet.concat(locnum);
				//System.out.println(loc);
				//add items to inmachine
				inmachine.add(new Items(name, amount, price, loc));
				if(count==col){
					
					//reset count
					count=0;
					row++;
				}
				count++;
		}
		
		//clear inventory list and replace with new inventory information
		inventory.clear();
		inventory.addAll(inmachine);
		
	}catch (Exception e){

		e.printStackTrace();
	}
	
	return;
}
}