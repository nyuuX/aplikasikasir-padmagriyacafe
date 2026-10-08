**Padmagriya Cafe User Manual**
Operational system for cafe orders, payments, stock, menus, and financial reports for version 1.0.

**1. Introduction**
Padmagriya Cafe is a web-based application that helps cafes manage daily activities: recording customer orders, processing payments, managing menus and ingredient stock, and monitoring financial reports. This application is used by three types of users: Cashier, Admin, and Owner. Each role can only view the menus appropriate for their tasks. This manual explains how to use the application step-by-step for each role.

**2. How to Open the Application**

* Ensure the application is running on your computer (see the CARA MENJALANKAN.txt file).


* Open a browser, such as Chrome or Edge.


* Type the address http://localhost:8080 and press Enter.


* The Padmagriya Cafe login page will appear.



**3. Accounts and Access Rights**
Default available accounts:

* **Admin:** Username: `admin`, Password: `admin123`.


* **Cashier:** Username: `kasir`, Password: `kasir123`.


* **Owner:** Username: `owner`, Password: `owner123`.



The menu displayed in the left panel differs for each role:

* The Dashboard feature can be accessed by the Cashier, Admin, and Owner.


* The Create orders and Process payments features can only be accessed by the Cashier.


* The View payment history, Manage menus, and Manage ingredient stock features can only be accessed by the Admin.


* The Financial reports and Export PDF features can only be accessed by the Owner.


* If a user attempts to open a page they do not have access to, the application will redirect them back to the Dashboard with a message stating that access is denied.



**4. Login and Logout**
**Login**

* On the System Login page, fill in the Username and Password.


* Click the Login button.


* If the data is correct, the application directly opens the Dashboard.


* If incorrect, the message "Username atau password salah" (Username or password incorrect) appears and you can try again.



**Logout**

* Click Logout at the bottom of the left panel.


* Always logout after finishing, especially if the computer is shared.



**5. Dashboard**
The Dashboard is the first page after login. It contains:

* Total Menu: the number of registered menus.


* Today's Revenue: total sales that have been paid for today.


* Low Stock (Admin only): the number of ingredients whose stock is at or below the minimum limit.


* Low stock notification (Admin only): a list of ingredients that need to be purchased immediately, displayed as a red label complete with the remaining stock.


* Favorite Menu per Category: the two most frequently ordered menus in each category, complete with photos, number sold, and revenue.


* To switch pages, use the menu in the left panel.



**6. Guide for Cashiers**
The Cashier is responsible for creating orders and processing payments. Available menus: Dashboard, Orders, and Payments.

**6.1 Creating an Order**

* Click Orders in the left panel.


* In the Create Order section, select a category using the All, Food, Beverage, or Snack buttons to shorten the list.


* Each menu card displays a photo, name, remaining stock, and price.


* To order a menu item, check the box on its card, then fill in the quantity ordered (greater than 0).


* Repeat for other menus ordered by the same customer.


* Click Save Order.


* Once successful, the message "Pesanan berhasil dibuat" (Order successfully created) appears and the new order will show up in the Order List table at the bottom of the page.



Things to note:

* Menus only appear if their status is available and they are in stock.


* The menu stock will immediately decrease according to the ordered quantity.


* If a menu's stock runs out, its status automatically becomes empty and it cannot be ordered again until the Admin restocks it.


* If the order quantity exceeds the stock, the application will reject the order and display a message indicating insufficient stock.


* New orders have an unpaid status until the cashier processes them on the Payment page.



**6.2 Processing Payments**

* Click Payments in the left panel.


* This page only displays unpaid orders.


* Each order is displayed in a card containing the order number, time, item details, and Total.


* Select a Payment Method on the card of the order you want to pay for.


* Click Process Payment.



**Cash Payment**

* Select the CASH method.


* Fill in the Amount Received field with the nominal amount of money from the customer.


* By default, this field is already filled according to the total.


* Click Process Payment.


* The change is calculated automatically by the system.


* If the amount received is less than the total, the message "Uang diterima belum mencukupi total pembayaran" (Amount received is insufficient for the total payment) appears and the payment will not be processed.



**QRIS Payment**

* Select the QRIS method.


* A QRIS Receipt will appear on the card containing a QR code, transaction code, order number, and the nominal amount to be paid.


* The Amount Received field is filled automatically according to the total and cannot be changed.


* After the customer finishes paying, click Process Payment.


* Once the payment is successful, the message "Pembayaran berhasil diproses" (Payment successfully processed) appears, the order disappears from the unpaid list, and its status changes to paid and completed.



**7. Guide for Admins**
The Admin manages the cafe's master data and monitors payment history. Available menus: Dashboard, Menu, Stock, and Payment History.

**7.1 Managing Menus**
Open the Menu page to add, change, or delete menus.

**Adding a new menu**

* In the Menu Form section, fill in the Menu Name.


* Select Category: Food, Beverage, or Snack.


* Fill in the Price (in multiples of 500) and Initial Stock.


* Check Available if the menu can be ordered immediately.


* Click Save Menu.


* The new menu automatically uses the default photo.


* In this version, there is no photo upload feature via the screen yet.



**Changing a menu**

* In the Menu List table, directly change the name, category, price, stock, or check the availability status on the intended menu row.


* Click Update on the same row to save the changes.



**Deleting a menu**

* Click Delete on the row of a menu that is no longer used.


* Only delete if absolutely necessary, as this action cannot be undone.


* Tip: to temporarily stop selling a menu without deleting it, simply uncheck Available and click Update.



**7.2 Managing Ingredient Stock**
Open the Stock page to monitor raw materials such as coffee, milk, or rice.

**Adding ingredients**

* In the Stock Form, fill in the Ingredient Name, Stock Amount, Unit (e.g., kg, liter, pcs), and Minimum Limit.


* Click Save Stock.



**Changing or deleting ingredients**

* In the Stock List table, change the required values and then click Update.


* Click Delete to remove the ingredient from the list.


* The Status column displays a Safe (green) or Low (red) label.


* The Low label appears when the stock amount is at or below the minimum limit.


* Ingredients with a Low status also appear as notifications on the Dashboard, so restock immediately.



**7.3 Viewing Payment History**

* Click Payment History in the left panel to see all paid orders, complete with item details, totals, and order times.


* This page is for viewing only; Admins cannot process payments.



**8. Guide for Owners**
The Owner monitors cafe performance through the Reports page.

**8.1 Reading Financial Reports**
Click Reports in the left panel. This page contains:

* Total Sales: all revenue from paid orders.


* Estimated Profit: expected earnings.


* Total Transactions: the number of paid transactions.


* Average: the average value per transaction.


* Payment Methods: the distribution of revenue between QRIS and cash.


* Profit Summary: estimated capital and margin.


* Transaction History: a table of every order along with its total, method, payment amount, and payment time.


* Important note: estimated profit and capital are calculated assuming a 35% margin (65% capital) because the cost of goods sold data per menu is not yet available.


* These figures are only estimates, not definite accounting profits.



**8.2 Downloading PDF Reports**

* Click the Export PDF button at the top right of the Reports page.


* The browser will download the laporan-keuangan-padmagriya.pdf file.


* The file contains a financial summary and the last 12 transactions at the time the report was printed.



**9. Frequently Asked Questions**

* The page cannot be opened in the browser: Ensure the application is running and Docker Desktop's status is Running. Wait one to two minutes after running the application, then refresh the page.


* The message "Access only for role..." or "Access not allowed" appears: This feature is not for your role. Login with the appropriate account, for example, a Cashier account to create orders.


* A menu does not appear on the Orders page: It is likely the menu's status is unavailable or it is out of stock. Ask the Admin to open the Menu page, restock it, and check Available.


* An order has been created but is not on the Payment page: The Cashier's Payment page only displays unpaid orders. Paid orders can be viewed by the Admin in Payment History or by the Owner in Reports.


* I selected the wrong menu after the order was saved: In this version, saved orders cannot be changed or canceled from the screen. Contact the Admin or developer to handle it.


* Table feature: Table number management has been disabled. If accessed, the application will return to the Dashboard along with a notification.



**10. Usage Tips**

* Always logout after finishing work.


* Change the default passwords before the application is used for actual operations.


* Check low stock notifications on the Dashboard every day before the cafe opens.


* Verify the total and payment method before pressing Process Payment, because processed payments cannot be canceled from the screen.
