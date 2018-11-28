#### The assignment
Create a „ping“ functionality with enhanced functions. The program’s aim is to test the IP network and to determine RTT (Round Trip Time)
##### Programming language: Java 
##### Program description:
The program should run on two independent computers connected through a network
The program should have two main functioning modes:

1. The Pitcher (message thrower) should run on the first, „A“, computer, and have the
functionality of generating and sending messages to another computer
2. The Catcher (message catcher) should run on the second, „B“ ,computer and have the
functionality of receiving the message and sending replies to the message received

The program should be able to define the length of a certain message and the speed of message sending defined as „msgs/s“

The program in “Pitcher mode” should be able to calculate and display statistical information for every single second:
1. Time - HH:MM:SS
2. Total number of sent messages
3. The number of sent messages in the previous second (speed)
4. Average time in the last second needed for the message to complete a cycle (A->B->A)
5. Total maximum time needed for the message to complete a cycle (A->B->A)
6. Average time for the previous second needed for the message to come from A to B (A-> B)
7. Average time for the previous second needed for the message to come from B to A (B-> A)

Format the print in one or two rows in the console window, to ensure clear and organized data tracking
The time should be expressed in „ms“.

Every message sent from the Pitcher should be numerated, in order for the Pitcher to notify us about the messages that were lost (messages for which the Catcher sent no reply)

In Pitcher mode, it should be possible to determine the length of the message (usually about 50 – 3000 bytes). 
The Pitcher should send a message as long as defined, and the Catcher should reply with a message of the exact same size. 
The content of the message is arbitrary (e.g. the number of the package, useful data for statistical measures, alphabet and similar)

The parameters will be inserted through a command line and the program should print the statistics in the console window. 

##### The command arguments should be defined as it follows:

Argument   | Description  
---|---
 -p | Pitcher mode 
 -c | Catcher mode 
 -port <port> |  [Pitcher] socket port used for connecting 
 -port <port> |  [Catcher] socket port used for listening
 -bind <ip_address> | [Catcher] socket bind address that will be used to run listen
 -mps <rate> |  [Pitcher] the speed of message sending expressed as „messages per second“; Default: 1
 -size <size> | [Pitcher] message length; Minimum: 50, Maximum: 3000, Default: 300
 <hostname> |  [Pitcher] the name of the computer which runs Catcher
 
#### Run example
On computer „compB“: java -jar ping-0.1.jar –c –bind 192.168.0.1 –port 9900
On computer „compA“: java -jar ping-0.1.jar –p –port 9900 –mps 30 –size 1000 compB
