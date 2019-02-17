Feature: Account functionalities

	Scenario: List cards
		Given user 1 have an account
		And user 2 have an account
		When user 1 list accounts
		Then user 1 have 1 account
		
	Scenario: List specific account
		Given user 1 have an account
		When user 1 lists the correct account
		Then only that account is returned
		
	Scenario: List other user's account
		Given user 1 have an account
		Then user 2 lists user 1 account and 404 is returned
	
	Scenario: List invalid account
		Given user 1 have an account
		Then user 1 lists the wrong account and 404 is returned
	
	Scenario: Add a new account
		Given user 1 have an account
		When user 1 adds an account with name Savings
		Then user 1 have 2 account
		And one account has name Savings
		
	Scenario: Validate when account dont have enough money
		Given user 1 have an account
		And account has balance 0
		Then some service asks if its possible to deduct 50 and it is not allowed
		
	Scenario: Validate when account have enough money
		Given user 1 have an account
		And account has balance 500
		Then some service asks if its possible to deduct 50 and it is allowed