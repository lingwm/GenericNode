#!/bin/bash
# Dummy jar file
#java -jar GenericNode.jar


#Membership server - centralized node directory - to be launched first
java -jar MembershipServer.jar ts 4410

#TCP Server
# The last argument below refers to <ip-address> at step 2 in the #Instructions.pdf file

#java -jar GenericNode.jar ts 1234 172.17.0.2

        

#TCP Server – centralized node directory if used
# java -jar GenericNode.jar ts 4410
#TCP Server – KV store
#java -jar GenericNode.jar ts 1234 <IP of node directory if used>

