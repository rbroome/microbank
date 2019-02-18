Feature: Transaction functionalites
		
	Scenario: System user asks for transactions
		Given that 5 transactions exists for account 1234 with status COMMITTED
		When system user asks for transaction for account 1234
		Then 5 transactions exists for account 1234
		
	Scenario: Normal user asks for transactions
		Given that 5 transactions exists for account 1234 with status COMMITTED
		When user 2 asks for transaction for account 1234
		Then status 403 is returned
		
	Scenario: System user tries to create a transaction
		When system user creates a transaction between account 1234 and account 4321
		Then 1 transaction exists for account 1234 with status PENDING and type CardTransaction
		And 1 transaction exists for account 4321 with status PENDING and type CardTransaction
		
	Scenario: Normal user tries to create a transaction
		When user 2 creates a transaction between account 1234 and account 4321
		Then status 403 is returned
			
	Scenario: System user tries commit transactions
		Given that 5 transactions exists for account 1234 with status PENDING
		When system user commits transactions for account 1234
		Then 5 transaction exists for account 1234 with status COMMITTED and type CardTransaction
		
	Scenario: Normal user tries to commit transactions
		Given that 5 transactions exists for account 1234 with status PENDING
		When user 2 commits transactions for account 1234
		Then status 403 is returned
		And 0 transaction exists for account 1234 with status COMMITTED and type CardTransaction
		
	Scenario: System user tries to create a transaction when its not allowed
		When system user creates a transaction between invalid account 0000 and account 4321
		Then 0 transaction exists for account 0000 with status PENDING and type CardTransaction
		And 0 transaction exists for account 0000 with status COMMITTED and type CardTransaction
		
	Scenario: System user tries to create a transaction with type CardTransaction
		When system user creates a CardTransaction type transaction between account 1234 and account 4321
		Then 1 transaction exists for account 1234 with status PENDING and type CardTransaction
		And 1 transaction exists for account 4321 with status PENDING and type CardTransaction
		
	Scenario: System user tries to create a transaction with type TransferTransaction
		When system user creates a TransferTransaction type transaction between account 1234 and account 4321
		Then 1 transaction exists for account 1234 with status PENDING and type TransferTransaction
		And 1 transaction exists for account 4321 with status PENDING and type TransferTransaction