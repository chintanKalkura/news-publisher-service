1. News publisher service will be used by authors/reporters with a contibutor role to write up an article, 
post it for validation and publish it on the website.
2. This service will have 2 microservices - one for publishing: news-publisher-api, another for validation:
news-validation-api. 
3. The news-publisher-api will receive the article store it and then send it to the other microservice for 
legal validation process.
4. While the news-publisher-api service receives the response from news-validation-api. The article will be 
sent for approval to the editor/approver page.
5. news-validation-api will respond with legal recommendation to the news-publisher-api, which will be displayed 
on the editor/approver page.
6. The editor can review the legal recommendation and the article and suggest changes to the article. The 
suggested changes should be sent as notification via email to the author/contributor.
7. Or the editor can choose to approve the article, which will then be published on to the main website.
8. The author/reporter can edit the article if changes are suggested by editor and submit for review again.
Repeating steps 3 to 7.
9. The user/reader can use his credentials to log in to the website and read the articles that are published.

The purpose is to practice implementing security features. OAUTH2.0 - bearer and JWT. Web page login and 
remember me feature(cookies).
Auth implmentation for each step as below:
1. Web page login. author role
2.
3. OAUTH2.0 Bearer JWT
4. 
5. OAUTH2.0 Bearer JWT
6. Web page login. editor role
7. 
8. Web page login. author role
9. Web page login. reader role