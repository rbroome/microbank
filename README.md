# MicroBank
The goal here was to make a small banking app to learn more about microservices architecture.
I started with the usecases below.

#### Use Cases
 - It should be possible to sign up with the application
 - It should be possible to login to the application
 - When signed in it should be possible to check the balance of an account.
 - When signed in it should be possible to see the transactions made to and from that account.
 - When signed in it should be possible to transfer money from one account to another account.
 - When signed in it should be possible to add a card to an account.
 - It should be possible to pay with a card if the correct pincode is entered
 - It should be possible to change the pincode of a card.
 - It should be possible to block a card


#### Architecture
For this I figured that i needed 4 service:
#### Services
 - Account Service
 - Transaction Service
 - Card Service
 - User Service

![Picture of architecture](https://raw.githubusercontent.com/rbroome/microbank/master/services.png)


## Account Service
The account service would take care of everything regarding accounts. For the users perspective that would be:
#### User actions
 - Show all accounts belonging to a user
 - Show a specific account beloning to a user
 - Add a new account to a user

 It will also need to be able to authorize if a transfer or payment is possible
#### Other services
 - Authorize if a specific amount can be deducted from an account

## Transaction Service
The transaction service will only be accessed through other services and will take care of everything regarding transaction.
#### Other services
 - Provide transactions connected to a specific account
 - Create transactions
 - Commit transactions with status pending, when it is finalized at the "bank"

## Card Service
The card service will take care of everything regarding cards, and payments with cards.
Most of these actions would be done through other services, but for example card payments will be handled here.
#### User actions
 - Show all cards belonging to a user
 - Pay with a card if the pincod is entered.

#### Other services
 - Add a card connected to an account
 - Change the pin of a card
 - Block a card

## User Service
The user service will handle signups and logins. It will also act as the gatewat routing everything to the correct service.