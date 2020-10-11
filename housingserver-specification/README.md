# Generating Rest API implementation and DTOs from OpenApi 3 specification
Here we use openapi-generator to generate SpringBoot Rest API implementation and model classes.

Some further tweaking to generated Java code is needed, since openapi-generator just
forgets about server url (or baseUrl in Swagger 2 parlance). This is done with maven-replacer-plugin - a simple search and replace operation to Api implementation classes.

The process produces a jar, which is then utilized by housgingserver-server.