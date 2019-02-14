$(document).ready(function() {var formatter = new CucumberHTML.DOMFormatter($('.cucumber-report'));formatter.uri("src/test/resources/features/card.feature");
formatter.feature({
  "name": "Card functionalities",
  "description": "",
  "keyword": "Feature"
});
formatter.scenario({
  "name": "List cards",
  "description": "",
  "keyword": "Scenario"
});
formatter.before({
  "status": "passed"
});
formatter.step({
  "name": "user 1 have a card",
  "keyword": "Given "
});
formatter.match({
  "location": "CardStepDefinitions.haveACard(String)"
});
formatter.result({
  "status": "passed"
});
formatter.step({
  "name": "user 1 list cards",
  "keyword": "When "
});
formatter.match({
  "location": "CardStepDefinitions.listCards(String)"
});
formatter.result({
  "status": "passed"
});
formatter.step({
  "name": "user 1 have 1 card",
  "keyword": "Then "
});
formatter.match({
  "location": "CardStepDefinitions.userHaveCards(String,int)"
});
formatter.result({
  "status": "passed"
});
formatter.scenario({
  "name": "Adding a card with correct info",
  "description": "",
  "keyword": "Scenario"
});
formatter.before({
  "status": "passed"
});
formatter.step({
  "name": "the user 1 adds a card with ok account",
  "keyword": "When "
});
formatter.match({
  "location": "CardStepDefinitions.userAddsACard(String)"
});
formatter.result({
  "status": "passed"
});
formatter.step({
  "name": "user 1 have 1 card",
  "keyword": "Then "
});
formatter.match({
  "location": "CardStepDefinitions.userHaveCards(String,int)"
});
formatter.result({
  "status": "passed"
});
formatter.scenario({
  "name": "Adding a card with wrong info",
  "description": "",
  "keyword": "Scenario"
});
formatter.before({
  "status": "passed"
});
formatter.step({
  "name": "the user 1 adds a card with bad account",
  "keyword": "When "
});
formatter.match({
  "location": "CardStepDefinitions.userAddsACardWithBadAccount(String)"
});
formatter.result({
  "status": "passed"
});
formatter.step({
  "name": "user 1 have 0 card",
  "keyword": "Then "
});
formatter.match({
  "location": "CardStepDefinitions.userHaveCards(String,int)"
});
formatter.result({
  "status": "passed"
});
formatter.scenario({
  "name": "Changing pin",
  "description": "",
  "keyword": "Scenario"
});
formatter.before({
  "status": "passed"
});
formatter.step({
  "name": "user 1 have a card",
  "keyword": "Given "
});
formatter.match({
  "location": "CardStepDefinitions.haveACard(String)"
});
formatter.result({
  "status": "passed"
});
formatter.step({
  "name": "user 1 changes pin to 4321",
  "keyword": "When "
});
formatter.match({
  "location": "CardStepDefinitions.changePin(String,String)"
});
formatter.result({
  "status": "passed"
});
formatter.step({
  "name": "user 1 pin is changed to 4321",
  "keyword": "Then "
});
formatter.match({
  "location": "CardStepDefinitions.userPinIs(String,String)"
});
formatter.result({
  "status": "passed"
});
formatter.scenario({
  "name": "Blocking the card",
  "description": "",
  "keyword": "Scenario"
});
formatter.before({
  "status": "passed"
});
formatter.step({
  "name": "user 1 have a card",
  "keyword": "Given "
});
formatter.match({
  "location": "CardStepDefinitions.haveACard(String)"
});
formatter.result({
  "status": "passed"
});
formatter.step({
  "name": "user 1 blocks the card",
  "keyword": "When "
});
formatter.match({
  "location": "CardStepDefinitions.blockCard(String)"
});
formatter.result({
  "status": "passed"
});
formatter.step({
  "name": "user 1 card is blocked",
  "keyword": "Then "
});
formatter.match({
  "location": "CardStepDefinitions.theCardIsBlocked(String)"
});
formatter.result({
  "status": "passed"
});
formatter.scenario({
  "name": "Paying with the card",
  "description": "",
  "keyword": "Scenario"
});
formatter.before({
  "status": "passed"
});
formatter.step({
  "name": "user 1 have a card",
  "keyword": "Given "
});
formatter.match({
  "location": "CardStepDefinitions.haveACard(String)"
});
formatter.result({
  "status": "passed"
});
formatter.step({
  "name": "user 1 have 1 card",
  "keyword": "And "
});
formatter.match({
  "location": "CardStepDefinitions.userHaveCards(String,int)"
});
formatter.result({
  "status": "passed"
});
formatter.step({
  "name": "the card have balance 200",
  "keyword": "And "
});
formatter.match({
  "location": "CardStepDefinitions.theCardHaveBalance(int)"
});
formatter.result({
  "status": "passed"
});
formatter.step({
  "name": "user 1 pays 50 with correct pin 1234",
  "keyword": "When "
});
formatter.match({
  "location": "CardStepDefinitions.pay(String,int,String)"
});
formatter.result({
  "status": "passed"
});
formatter.step({
  "name": "the payment is approved",
  "keyword": "Then "
});
formatter.match({
  "location": "CardStepDefinitions.approvedPayment()"
});
formatter.result({
  "status": "passed"
});
formatter.scenario({
  "name": "Paying with a blocked card",
  "description": "",
  "keyword": "Scenario"
});
formatter.before({
  "status": "passed"
});
formatter.step({
  "name": "user 1 have a blocked card",
  "keyword": "Given "
});
formatter.match({
  "location": "CardStepDefinitions.haveABlockedCard(String)"
});
formatter.result({
  "status": "passed"
});
formatter.step({
  "name": "user 1 have 1 card",
  "keyword": "And "
});
formatter.match({
  "location": "CardStepDefinitions.userHaveCards(String,int)"
});
formatter.result({
  "status": "passed"
});
formatter.step({
  "name": "user 1 card is blocked",
  "keyword": "And "
});
formatter.match({
  "location": "CardStepDefinitions.theCardIsBlocked(String)"
});
formatter.result({
  "status": "passed"
});
formatter.step({
  "name": "user 1 pays 50 with pin 1234 and its not approved",
  "keyword": "Then "
});
formatter.match({
  "location": "CardStepDefinitions.payNotApproved(String,int,String)"
});
formatter.result({
  "status": "passed"
});
formatter.scenario({
  "name": "Paying with wrong pin",
  "description": "",
  "keyword": "Scenario"
});
formatter.before({
  "status": "passed"
});
formatter.step({
  "name": "user 1 have a card",
  "keyword": "Given "
});
formatter.match({
  "location": "CardStepDefinitions.haveACard(String)"
});
formatter.result({
  "status": "passed"
});
formatter.step({
  "name": "the card have balance 200",
  "keyword": "And "
});
formatter.match({
  "location": "CardStepDefinitions.theCardHaveBalance(int)"
});
formatter.result({
  "status": "passed"
});
formatter.step({
  "name": "user 1 pays 50 with pin 4321 and its not approved",
  "keyword": "Then "
});
formatter.match({
  "location": "CardStepDefinitions.payNotApproved(String,int,String)"
});
formatter.result({
  "status": "passed"
});
formatter.scenario({
  "name": "Paying with a card with not enough funds",
  "description": "",
  "keyword": "Scenario"
});
formatter.before({
  "status": "passed"
});
formatter.step({
  "name": "user 1 have a card",
  "keyword": "Given "
});
formatter.match({
  "location": "CardStepDefinitions.haveACard(String)"
});
formatter.result({
  "status": "passed"
});
formatter.step({
  "name": "the card have balance 0",
  "keyword": "And "
});
formatter.match({
  "location": "CardStepDefinitions.theCardHaveBalance(int)"
});
formatter.result({
  "status": "passed"
});
formatter.step({
  "name": "user 1 pays 50 with pin 1234 and its not approved",
  "keyword": "Then "
});
formatter.match({
  "location": "CardStepDefinitions.payNotApproved(String,int,String)"
});
formatter.result({
  "status": "passed"
});
});