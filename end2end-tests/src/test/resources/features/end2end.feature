Feature: End 2 End testing for the whole Micro Bank

	Scenario: It should be possible to sign up with the application
		When user signs up with username test and password secret
		Then status 201 is returned
	
	Scenario: It should be possible to login to the application
		Given user signs up with username test and password secret
		When user logs in with username test and password secret
		Then token is returned
		And status 200 is returned
	
	Scenario: It should be possible to create an account
		Given user signs up with username test and password secret
		And user logs in with username test and password secret
		When user creates an account
		Then status 201 is returned
		And 1 accouns exists
		
	Scenario: If signed in it should be possible to check the balance of an account.
		Given user signs up with username test and password secret
		And user logs in with username test and password secret
		And user creates an account
		Then user gets his account and can see the balance
		And status 200 is returned
		
	Scenario: If signed in it should be possible to see the transactions made to and from that account.
		Given user signs up with username test and password secret
		And user logs in with username test and password secret
		And user creates an account
		Then user gets his transactions and can see them
		And status 200 is returned
		
	Scenario: If signed in it should be possible to transfer money from one account to another account.
		Given user signs up with username test and password secret
		And user logs in with username test and password secret
		And user creates an account
		And user creates an account
		And user have 500 on all his accounts
		Then user can transfer money bewteen his accounts
		
	Scenario: If signed in it should be possible to add a card to an account.
		Given user signs up with username test and password secret
		And user logs in with username test and password secret
		And user creates an account
		Then user creates a card to that account and its ok
		And status 201 is returned
		
	Scenario: It should be possible to pay with a card if the correct pincode is entered
		Given user signs up with username test and password secret
		And user logs in with username test and password secret
		And user creates an account
		And user creates a card to that account and its ok
		Then user pays 50 with the correct pincode
		And status 201 is returned
		And the transaction is PENDING
		
	Scenario: It should not be possible to pay with a card if the wrong pincode is entered
		Given user signs up with username test and password secret
		And user logs in with username test and password secret
		And user creates an account
		And user creates a card to that account and its ok
		Then user pays 50 with the wrong pincode
		And status 403 is returned
		
	Scenario: It should be possible to change the pincode of a card.
		Given user signs up with username test and password secret
		And user logs in with username test and password secret
		And user creates an account
		And user creates a card to that account and its ok
		Then user change the pin from 1234 to 4321 and its ok
		And status 204 is returned
		
	Scenario: It should be possible to block a card
		Given user signs up with username test and password secret
		And user logs in with username test and password secret
		And user creates an account
		And user creates a card to that account and its ok
		Then user blocks the card and its ok
		And status 200 is returned
		
	Scenario: It should not be possible to pay with a blocked card
		Given user signs up with username test and password secret
		And user logs in with username test and password secret
		And user creates an account
		And user creates a card to that account and its ok
		And user blocks the card and its ok
		Then user pays 50 with the correct pincode
		And status 403 is returned
		