Freight Forwarding App is an android app. The development language used in this project is Kotlin. Front end design of this app used to xml. Freight forwarding app provides the all over facilities related to the freight forwarding system. It provides the facility from rate related queries to delivery order handling status including real time status updates. It is used to defend the final year project.

Open the app after splash screen app display a login screen.

Now the app is two sided. First is Client Side and the second is Admin Side.

Firstly we discussed the client side.

After splash screen client views a client login screen.

<img width="720" height="1600" alt="Client login" src="https://github.com/user-attachments/assets/1ecd0e73-9bbb-4dc4-a056-9f16bc8e87e5" />

Client attempt the login process using the email and password.

If account is not created then client created an account using the sign up activity 

<img width="720" height="1600" alt="Sign up" src="https://github.com/user-attachments/assets/14f7e6f4-eaf8-4eab-9ee7-25eb5034e72b" />

Above pic show the sign up process user fill the form and create an account then user attempt the login process and successfully login the app.

After the login user views a dashboard.

<img width="720" height="1600" alt="Client dashboard" src="https://github.com/user-attachments/assets/5234b435-dce4-4a7b-925a-641537f75c93" />

Above picture shows the client dashboard.

If user click others icon, app shows the all over features.

<img width="720" height="1600" alt="other icons client" src="https://github.com/user-attachments/assets/68885ffb-9df5-443b-8ec4-8e09bf9aca7f" />

Now we discuss the rate module of client side

 <img width="720" height="1600" alt="Client ratess" src="https://github.com/user-attachments/assets/680cdfb2-95f6-4f1e-9fac-c8f5b28b53ac" />

Above pic show the rate form, user fill the form for the cargo rates. User select the movement mode from own choice i.e. By-Air or By-Sea.

User fill the form and press the calculate button.

<img width="720" height="1600" alt="Client rates air" src="https://github.com/user-attachments/assets/23f9d328-6b5d-4a75-add8-f3fe76900071" />

Next show rates to the user, related from the user details as shown given below

<img width="720" height="1600" alt="Client select rates air" src="https://github.com/user-attachments/assets/f51236d4-ca3a-4905-8c82-13de2c43d794" />

If user rates suitable then user select rates and press create job button. After pressing the button, job is created and assign a unique reference number as shown is given below.

 <img width="720" height="1600" alt="Job created" src="https://github.com/user-attachments/assets/1231c512-ad21-4435-9204-f4c8a6ca3fa2" />

Picture  shows the reference number and the same reference number chat is created in chat module.

If user rates non suitable then stop working no further processing for creating a job.

Second we discuss the chat module of client side

<img width="720" height="1600" alt="Client chats" src="https://github.com/user-attachments/assets/728aea59-4774-4bc3-adb1-3b0ab513c8d9" />

Client side chat module shows the all chat of the client shipments and client is identify every chat of shipment easily just using a reference number.

This module provides a real time communication option between client and the admin means freight forwarder.

The communication is similar to whatsapp communication. User use this module to send messages, images and files to admin in real time.  

<img width="720" height="1600" alt="Client chat" src="https://github.com/user-attachments/assets/4f728b3e-9264-453a-ae04-89e8349b7a67" />

Above pic shows the functionality of chat. 

Next discussed the status update module of client side

 # status list

Above pic shows the all  shipment status on one place. user select a specific shipment to view the all over detail and update status of shipment 

  # status detail

Picture shows the detail of specific shipment and real time updated status.

App provides a document scanning picture to extract the shipment related documents. It scans the physical documents through the app and shares digitally.

App provides AI Assistance to help out new customers understanding the process of freight forwarding system.

  # pic ai assistance

Customer send the message to AI Assistant and the app automatically response to the customer.

App provides a tracking option. Customer directly track the cargo flight position on real time.

  # tracking pic

Customer track the shipment and check the flight arrival and departure status on real time along the airline or sea line tracking on this option.

Customer check the delivery order issue status through the app and view the delivery order.

  # do pic

If delivery order show this activity then issue the delivery order otherwise still not issue the delivery order.

Customer view the file and open file to check the detail of delivery order.

Customer views the payment invoice in real time against the cargo. When invoice is created, it shows the customer immediately.

  # invoice pic

Customer open the invoice and check the amount and verify the bill with rates if invoice is verified from rates then customer pay to the freight forwarder.

Customer views a profile option from dashboard

  # profile pic

Customer update the profile details if changing the data means address or phone number.

Customer change the own login password using the current password and set the new password with the passage of time.

 # chage password

Customer provides a review to the app and services.

  # feedback

Customer rate a rating and feedback to the app and app services.

About us activity shows the history of build a company

Log out button click user log out the app.

Now, We discuss the admin side 

After splash screen admin views a client login screen and then move to admin login from client login.

   # Adding pic admin login

Admin attempt the login process using the email and password.

After the login admin views a dashboard.

 # Admin dashboard

Above picture shows the admin dashboard.

If admin click others icon, app shows the all over features.

   # adding other icon pic

Now we discuss the rates management module of admin side

  # add rate management pic

Above picture shows the all functionalities of the rate management module.

Firstly we discuss the add new country and city function.

  # add pic new country

Picture shows a form if country or city is not added in the app then admin use this activity and add country and city.

And add first airline name and rates according to weight scale.

  # add sea pic new country

Above picture shows the sea process of adding a new country and city and their respective rates.

Next we discuss the add rates function of same module

  # add rates 

Add air rates process first select a country and the city and fill the above form then rates are added in database.

 # add sea rates

Add sea rates process is similar to add air rates.

And other functions update and remove rates is similar to add rates function.

Second we discuss the chat module of admin side

 # chat pic 

Admin side chat module shows the all chats of client shipments and admin is identify every chat of shipment easily just using a reference number.

This module provides a real time communication option between client and the admin means freight forwarder.

The communication is similar to whatsapp communication. Admin use this module to send messages, images and files to client in real time.  

 # chat pic 

Above pic shows the functionality of chat. 

Admin side chat module is same as client side chat module but the main difference is admin views the all clients chat and the client view only own shipment chats

Next discussed the status update module of admin side

 # status update

Above pic shows to select the reference number and select action. 

  # status drop down

Picture shows to select the status view or update. If admin select view then app show a status similar to client side means show a detail of shipment.

If admin select status update then admin update the information or add a new information

   # status update

App provides a document scanning picture to extract the shipment related documents. It scans the physical documents through the app and shares digitally.

Tracking module of admin side is same as client side. Admin track the shipment through tracking activity.

Admin upload the delivery order on app and client view the delivery order.

  # do pic

If the delivery order is uploaded it means client ensure the delivery order is issued. If the delivery order still not uploaded then delivery order not issued.

Customer view the delivery order and verify the issued status.

Admin upload the payment invoice in real time against the cargo. When invoice is uploaded, it shows the customer immediately.

  # invoice pic

Admin views a profile option from dashboard

  # profile pic

Owner add a new admin using a secret key

 # secret key 

 If the secret key is wrong then shake a dialog and show the alert message.

   # secret alert

If admin enter a secret key correct then unlock the add admin feature and open the add admin form. Fill the opened form and set a password then press add admin button and the new admin successfully added.

Admin change the own login password using the current password and set the new password with the passage of time.

 # chage password

About us activity shows the history of build a company

Log out button click admin log out the app.
