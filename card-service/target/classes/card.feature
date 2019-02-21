Feature: Card functionalities

	Scenario: List cards
		Given user 1 have a card
		When user 1 list cards
		Then user 1 have 1 card
		
	Scenario: Adding a card with correct info
		When the user 1 adds a card with ok account
		Then user 1 have 1 card
		
	Scenario: Adding a card with wrong info
		When the user 1 adds a card with bad account
		Then user 1 have 0 card
		
	Scenario: Changing pin
		Given user 1 have a card
		When user 1 changes pin to 4321
		Then user 1 pin is changed to 4321
		
	Scenario: Blocking the card
		Given user 1 have a card
		When user 1 blocks the card
		Then user 1 card is blocked
		
	Scenario: Paying with the card
		Given user 1 have a card
		And user 1 have 1 card
		And the card have balance 200
		When user 1 pays 50 with correct pin 1234
		Then the payment is approved
		
	Scenario: Paying with a blocked card
		Given user 1 have a blocked card
		And user 1 have 1 card
		And user 1 card is blocked
		Then user 1 pays 50 with pin 1234 and its not approved
		
	Scenario: Paying with wrong pin
		Given user 1 have a card
		And the card have balance 200
		Then user 1 pays 50 with pin 4321 and its not approved
		
	Scenario: Paying with a card with not enough funds
		Given user 1 have a card
		And the card have balance 0
		Then user 1 pays 50 with pin 1234 and its not approved