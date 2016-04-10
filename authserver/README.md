This project is a simple, minimal implementation of an OAuth2
Authorization Server for use with Spring Cloud sample apps. It has a
context root of `/uaa` (so that it won't share cookies with other apps
running on other ports on the root resource). OAuth2 endpoints are:

* `/uaa/oauth/token` the Token endpoint, for clients to acquire access
  tokens. There is one client ("acme" with secret "acmesecret"). With
  Spring Cloud Security this is the `oauth2.client.tokenUri`.
* `/uaa/oauth/authorize` the Authorization endpoint to obtain user
  approval for a token grant.  Spring Cloud Security configures this
  in a client app as `oauth2.client.authorizationUri`.
* `/uaa/oauth/check_token` the Check Token endpoint (not part of the
  OAuth2 spec). Can be used to decode a token remotely. Spring Cloud
  Security configures this in a client app as
  `oauth2.resource.tokenInfoUri`.
