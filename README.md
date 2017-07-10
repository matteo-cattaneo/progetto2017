# **Lorenzo il Magnifico**

Group #22

_   Nicola Cucchi       825642          nicola.cucchi@mail.polimi.it

_   Matteo Cattaneo       826804          matteo12.cattaneo@mail.polimi.it

***

The Main Class of the **Server** is the **StartServer** in the package **server**

The Main Class of the **Client** is the **StartClient** in the package **client**


**IMPLEMENTATIONS**

We have implemented the following features:

-> **Complete rules pack**, so a game is played with Excommunications, LeaderCards and Permanent Effects.

-> **RMI** and **Socket connection** ( a Game can be done with both connections )

        -> RMI listens on port 1099 and Socket listens on port 1337
				
        -> connection type can be chosen by the client before starting a game
				
        -> Polimi probably blocks port 1337, so it's necessary to use a different network to connect to the server.
        
-> **CLI** interface (GUI predisposed --> GUIinterface, but not implemented so CLI is chosen as default UI)

-> **Nickname + Password login**: 

        -> hashed password 
        
        -> if a nickname is already used you must insert the rigth password  ( Log In )
        
              -> if a new session with nickname & password already logged is istantiated the old one is blocked
              
        -> if a nikcname is new it is memorized on the server with the password ( Sign Up )


**DATA LOADING**

All data for cards (Leader, Development and Excommunications) & board bonuses (FaithGrid and ActionSpaces) 
& Timeouts are located in **JSON** folder.

We load this data using Google's **gson** external library -> Maven Dependency


**GAME DEPLOYMENT**

-> Eclipse: you must run the StartServer and the StartClient (in this order)

-> Jar files: are generated into the target folder (by maven) and you must run

    java -jar Client.jar and java -jar Server.jar from the target folder
	
-> Default values are localhost as Server IP and "Player" as username

-> Server must have the JSON folder in the same folder where Server is executed.

-> Client must have CLI_Media folder in the same folder where Client is executed.

