
public class Items {

	String itemname;
	//how many are in the vending machine
	int amount;
	//cost of the item
	String price;
	//the button combo you'd press to get the item ex: A7
	String location;

	Items(String itemname, int amount, String price, String location){

		this.itemname=itemname;
		this.amount=amount;
		this.price=price;
		this.location=location;
	}


	//gets item name
	public String getitemname(){

		return itemname;

	}

	//gets item amount
	public int getamount(){

		return amount;

	}

	//gets item price
	public String getprice(){

		return price;

	}


	//get button combo needed to get item
	public String getlocation(){


		return location;
	}


	public void setitemname(String item){

		itemname=item;
		return;

	}

	public void setamount(int qty){

		amount=qty;
		return;
	}

	public void setprice(String cost){


		price=cost;
		return;
	}

	public void setlocation(String loc){

		location=loc;
		return;
	}
	
}
