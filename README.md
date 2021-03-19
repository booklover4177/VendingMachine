Vending Machince Program:

How to run the code:
To run the code you must compile the code and then run it with a JSON file in the format of the input.json file that was 
originally given to me along with the coding challenge description.

Approach taken/how the program works:
The code takes the input JSON file and pulls the config information and from that it takes the number of rows and columns.
It then creates a JSON array to put the information from the JSON file that are part of "items" in and it gets all the "items".
The reading, getting, creating of JSONObjects and JSONArrays, and otherwise manipulating the information in the JSON file 
is done through the use of the external jar library json-simple-1.1.1.jar.
A String ArrayList is then created to store all the Strings containg the item information. A loop then goes through the the JSONArray
and adds the Strings to the ArrayList one at a time. A String ArrayList called actionlog is then created, the action log logs all the actions taken for audit purposes.
Then the max number of items that the vending machine can hold is calculated by multiplying the number of rows by the number of columns. The max number of items is
then compared to the size of the arraylist holding all the item information, if it is greater than the max number of items an error message is added to the action log
stating that the items loaded into inventory exceeded the max inventory space and then prints the action log and returns, ending the program because the vending machine
couldn't be setup properly. If the number of items doesn't exceed the max number of items, an ArrayList of type Items named inmachine is created. The Items class is defined in 
Items.java. It holds the item name, price, amount (how many are in the vending machine), and the location (in the machine, e.g. A1, A2, A3...etc.). A loop is then used to
get each string holding all the item information at index i from the String ArrayList. That information is then substringed to get the item name, price, and amount. The location
is determined by the number row and column count the item is in, they were defined and set equal to 1 before entering the loop and the col is increased at the end of the loop; 
while the row is only increased if the column count is equal to the column number pulled from the config information in which case the column count is reset to 0 and the row
is increased. A String named loclet is created and initialized to getLetter(row), getLetter(int row) is a function that takes a number and converts it to the corresponding letter
of the alphabet. A String named locnum is created and initialized to String.valueOf(count) to convert the column count value to a String. loclet and locnum and then concatentated
to get the location of the item in the vending machine. All this information is then added to inmachine, like so: inmachine.add(new Items(name, amount, price, loc)).
Once the loop finishes the vending machine's inventory has been properly set up. A boolean variable called quit is then created and initialized to false. This variable will
allow the program to know to quit or not based upon the user's decisions. It then enters into a while loop that will continue to run while quit does not equal true.
The program then prints inmachine so the user knows what is in the vending machine, what the items are, how much they cost, their location in the machine, and how many of each are
in the vending machine. This is added to the action log. Then the user is asked if they would like to make a purchase and they are asked to respond Y/N. This is added to the
action log. If the user doesn't answer Y or N, an error message is added to the action log stating they didn't respond in the correct format. It then prints the inventory and asks
the question again. If the user responds Y it adds their response to the action log and then asks for the location of the item they would like to purchase. This is added to the action
log. It gets the user response. Then it asks the user to enter their payment in USD, with an example of correct formatting. This is added to the action log. Then it gets the user 
response. Then the program calls the purchase function, which reports the state of the transaction as it moves through the purchase, it checks the formatting of the payment, if the 
location selected is in the vending machine, if the payment is exact, greater than or less than the price and responds accordingly. All of this is also added to the action log. After
The purchase is complete. It returns to the start of the while loop and prints the inventory and asks you if you would like to make a purchase again. If however, the user respond N
to the question of if they'd like to make a purchase it then proceeds to ask if you would like to view the inventory again, if you respond Y it prints the inventory again and returns
to the start of the while loop again. If you responded N, then you are asked if you would like to update the machine inventory. If you respond Y, then you are asked for the JSON file
you want to use to update the inventory or its filepath. All these actions are added to the action log as they occur. It then checks that the user input a JSON file. If it is not
a JSON file it prints an error message, adds that error message to the action log and returns to the start of the while loop. If it was a JSON file it calls the updateinventory 
function which pulls the information from the JSON file and puts it in inmachine in the same way as I described in the initial inventory setup, excet that it first clears inmachine
of the old information and then adds all the new information at once. Please note: this was done under the assumption that the JSON file used to update the inventory would be in the
exact same format as the inventory file I was given initially. This is added to the action log and once the inventory is updated it returns to the begining of the while loop.
If the user chose not to update the inventory they are then asked if they would like to view the action log. If the user responds Y then it is added to the action log and then
the function printactionlog is called and it prints the action log for the user to see. It then returns to the begining of the while loop. If the user responded N it then asks
if you would like to quit using the vending machine. If the user responds Y then it sets quit to true, which causes it to exit the while loop and end the program. If they respond
with N it returns to the begining of the while loop.
