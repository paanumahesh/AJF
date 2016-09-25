Feature: To check the api functionality works correctly.
          This test checks the CommonApiSteps are working
@apisanity
Scenario: the api given, when & then work
Given I get an authentication code at URL "http://dev.api.events/oauth/v2/token?client_id=1_4xe54qko6n40o8g4ggoo8w88o0co00ow8wg0gs8w0ggkowscgk&client_secret=4mdo999k5am8kc0gkwoo8gowo8gsc0k048k44socg4404gg0gs&grant_type=client_credentials"
When I connect to the api at "http://dev.api.events/app_dev.php/api/v2/" with parameters "events.json?" and the authentication code
Then The GET response header status will be 200