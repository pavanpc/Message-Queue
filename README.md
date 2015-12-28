/*
   Author- Pavan PC
   Date  - 27-Sep-2015
*/

A Library to implement different  queue implementaions like Inmemory Queue, File Queue and SQS queue
It also supports different implementations based on environment like Local, prod , statging etc


#Design Details
--------------------
** Please take a look into class_diagram file for the complete  design ! **


1. The library implents a common interface QueueService with method signatures
2. AbstarctQueue Service classs implements this and adds basic behaviors
3. Diffrent Queue service like File and In memory inherits from AbstarctQueue service and adds its own implementations
4. Having AbstarctQueue Service helps to add re usability and reduce coulpling between components. Its easy to add any new services
5. There is Queue interface for queue operations
5. Abstract queue implements this
6. All other queues Inmemory Queue and File queue extend this for custom implementation.
7. We have Message and Inflight Message class to store message data and Inflight messages details
8. Producer and Consumer class takes Queue Service as argument and talk to respective services(File,SQS,Inmemory) using  AbstarctQueueService class


Implementation Details
-----------------------
1. Inmemory Queue
   a. I have used ConcurrentLinkedQueue to represent a queue as it is concurrent and non blocking. Synchronized blocks are used to make it Thread safe.
   b. Bllocking queues can also be used.But it will block the entire queue.

2. File Queeu
   a. I have used File to represent a queue
   b. Each file will have messages(string ) stored in each line. Each line consists of a Message ID and Message Body seperated by $(delimiter) 
   c. Every push operation opens and closes a given file. This is a costly operation
   d. All files will be stored in a directory sqs/
   e. Lock on a file is handled by synchronized blocks   
   f. Each file in sqs/ directory represents a queue
   g. Pull operation is handled by removing first line , writing to a temp file and renaming it.
   TODO:****
      Instead of opening and closing files for every operation, store messages in memory and flush it to desk using flush cycles.

Behavior
--------
1. Visibility Timeout
   a. In order to account for visibility timeout, when ever a message is pulled from a queue, it will be stored in a
      concurrentHashMap of Inflight messages.
         Key- messageId
         Value- InflightMessage object (Message object, timestamp)
   b. When pull is called, message is removed from the queue and pushed to Inflightmessage queue
   c. Timestamp of InflightMessage is set to currentTime+ VisibilityTimeoue
   d. Every 5 seconds(configurable) a thread runs for each queue to check for inflight messages status
   e. The thread removes message and put it back to queue if delete is not called (currentTime > (InflightMessage timestamp))
   f. The thread removes message if delete is called before  VisibilityTimeoue
   g. If visibility timeout is changed in between , this will put in effect only after next thread cycle(Visibility thread)


Scope
------
1. an in-memory queue service, suitable for same-JVM producers and consumers;

2. a file-based queue, service suitable for same-host producers and consumers.

3. Thread safe.

4. Configurable for different environments (local.properties, prod.properties)

3. an interface for SQS(implementaion explained in source file).


Test Case
---------
1. Includes Junit test cases for Inmemory and File queue services
