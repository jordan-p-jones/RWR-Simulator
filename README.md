# RWR-Simulator
Simulates a Radar Warning Receiver using Spring Boot, JavaFX, RabbitMQ, and Hibernate.

The idea is that say you have some real-time system capable of receiving messages and displaying them on a GUI, but you also need a way to run your own message simulations in a reproduceable manner to test the application. When this application starts, it sets up some hard-coded test data representing different signals detected at sequential points in time. It uses RabbitMQ to send messages to an exchange one second apart, and then a message receiver class is able to detect them, decode the messages, do some operations on the data, and send it to the GUI for display. It will also log the message data received to a database, since in theory, in a real-time system you would probably want to record everything that happened and how it was interpreted for later analysis or for recreating a sitaution.

Below is a short loop of what the application looks like with 11 seconds of simulated signal data:
![](https://github.com/jordan-p-jones/RWR-Simulator/blob/main/RwrAppDemo.gif)

Note that running this application requires manually starting a RabbitMQ service on your local machine prior to running the application, as well as having a Postgres database already existing (although Hibernate will create the tables when the program runs for the first time).
