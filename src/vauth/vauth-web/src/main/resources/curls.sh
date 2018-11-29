# 1 auth
time curl -X POST -H "Content-Type: application/x-www-form-urlencoded" --data 'username=admin&password=admin' -i "http://localhost:9960/v1/api/auth/authorize?redirect_uri=http://localhost/&client_id=9afba38b4ee1d59e72db1c8fa4d60462&response_type=code"

time curl -X POST -H "Content-Type: application/x-www-form-urlencoded" -i "http://localhost:9960/v1/api/auth/token?redirect_uri=http://localhost/&grant_type=authorization_code&client_secret=29ca5ab7e8ee102acb6278b842eef9cc&client_id=9afba38b4ee1d59e72db1c8fa4d60462&code=2ff66a72dd44227e4bf0ae66025ac85f"
time curl -X POST -H "Accept: application/json" -H "Content-Type: application/x-www-form-urlencoded" -i "http://localhost:9960/v1/api/auth/token?redirect_uri=http://localhost/&grant_type=authorization_code&client_secret=29ca5ab7e8ee102acb6278b842eef9cc&client_id=9afba38b4ee1d59e72db1c8fa4d60462&code=4d0997dcac1de26b6b3af05930840e8b"

time curl -X PUT -H "Accept: application/json" -i "http://localhost:9960/v1/api/auth/refresh/316b426ca3c100658588db83508a6968?client_secret=29ca5ab7e8ee102acb6278b842eef9cc"
time curl -H "Accept: application/json" --cookie 'token=60cc5dfb951afff9afa92f880482e57b' -i "http://localhost:9960/v1/api/auth/check/60cc5dfb951afff9afa92f880482e57b"

# 2 server
time curl -H "Accept: application/json" --cookie 'token=60cc5dfb951afff9afa92f880482e57b' -i http://localhost:9960/v1/api/server/version
time curl -H "Accept: application/json" --cookie 'token=60cc5dfb951afff9afa92f880482e57b' -i http://localhost:9960/v1/api/server/state
time curl -H "Accept: application/json" --cookie 'token=60cc5dfb951afff9afa92f880482e57b' -i http://localhost:9960/v1/api/server/reload

# 3 application
time curl -X POST -H "Content-Type: application/json" -H "Accept:application/json" -d '{"externalId":"exampleApplication"}' --cookie 'token=60cc5dfb951afff9afa92f880482e57b' -i http://localhost:9960/v1/api/vauth/application
time curl -X PUT -H "Content-Type: application/json" -H "Accept:application/json" -d '{"enabled" : false,"externalId" : "exampleApplicationUpdated","id" : 3}' --cookie 'token=60cc5dfb951afff9afa92f880482e57b' -i http://localhost:9960/v1/api/vauth/application/3
time curl -X DELETE -H "Accept: application/json" --cookie 'token=60cc5dfb951afff9afa92f880482e57b' -i http://localhost:9960/v1/api/vauth/application/3

time curl -H "Accept: application/json" --cookie 'token=60cc5dfb951afff9afa92f880482e57b' -i http://localhost:9960/v1/api/vauth/application
time curl -H "Accept: application/json" --cookie 'token=60cc5dfb951afff9afa92f880482e57b' -i http://localhost:9960/v1/api/vauth/application/0/100
time curl -H "Accept: application/json" --cookie 'token=60cc5dfb951afff9afa92f880482e57b' -i http://localhost:9960/v1/api/vauth/application/1
time curl -H "Accept: application/json" --cookie 'token=60cc5dfb951afff9afa92f880482e57b' -i http://localhost:9960/v1/api/vauth/application/count
time curl -H "Accept: application/json" --cookie 'token=60cc5dfb951afff9afa92f880482e57b' -i http://localhost:9960/v1/api/vauth/application/external_id/vauth_rest_api
time curl -H "Accept: application/json" --cookie 'token=60cc5dfb951afff9afa92f880482e57b' -i http://localhost:9960/v1/api/vauth/application/client_id/9afba38b4ee1d59e72db1c8fa4d60462
time curl -H "Accept: application/json" --cookie 'token=60cc5dfb951afff9afa92f880482e57b' -i http://localhost:9960/v1/api/vauth/application/client_secret/29ca5ab7e8ee102acb6278b842eef9cc

# 4 property
time curl -X POST -H "Content-Type: application/json" -H "Accept:application/json" -d '{"externalId":"property","tag":"commonFirst"}' --cookie 'token=60cc5dfb951afff9afa92f880482e57b' -i http://localhost:9960/v1/api/vauth/property
time curl -X PUT -H "Content-Type: application/json" -H "Accept:application/json" -d '{"enabled" : false, "id" : 48, "externalId" : "propertyUpdated", "tag" : "common"}' --cookie 'token=60cc5dfb951afff9afa92f880482e57b' -i http://localhost:9960/v1/api/vauth/property/48
time curl -X DELETE -H "Accept: application/json" --cookie 'token=60cc5dfb951afff9afa92f880482e57b' -i http://localhost:9960/v1/api/vauth/property/48

time curl -H "Accept: application/json" --cookie 'token=60cc5dfb951afff9afa92f880482e57b' -i http://localhost:9960/v1/api/vauth/property
time curl -H "Accept: application/json" --cookie 'token=60cc5dfb951afff9afa92f880482e57b' -i http://localhost:9960/v1/api/vauth/property/0/100
time curl -H "Accept: application/json" --cookie 'token=60cc5dfb951afff9afa92f880482e57b' -i http://localhost:9960/v1/api/vauth/property/1
time curl -H "Accept: application/json" --cookie 'token=60cc5dfb951afff9afa92f880482e57b' -i http://localhost:9960/v1/api/vauth/property/count
time curl -H "Accept: application/json" --cookie 'token=60cc5dfb951afff9afa92f880482e57b' -i http://localhost:9960/v1/api/vauth/property/external_id/email
time curl -H "Accept: application/json" --cookie 'token=60cc5dfb951afff9afa92f880482e57b' -i http://localhost:9960/v1/api/vauth/property/tag/common

# 5 role
time curl -X POST -H "Content-Type: application/json" -H "Accept:application/json" -d '{"externalId":"roleExample"}' --cookie 'token=60cc5dfb951afff9afa92f880482e57b' -i http://localhost:9960/v1/api/vauth/role
time curl -X PUT -H "Content-Type: application/json" -H "Accept:application/json" -d '{"externalId" : "roleExampleUpdated"}' --cookie 'token=60cc5dfb951afff9afa92f880482e57b' -i http://localhost:9960/v1/api/vauth/role/20
time curl -X DELETE -H "Accept: application/json" --cookie 'token=60cc5dfb951afff9afa92f880482e57b' -i http://localhost:9960/v1/api/vauth/role/20

time curl -H "Accept: application/json" --cookie 'token=60cc5dfb951afff9afa92f880482e57b' -i http://localhost:9960/v1/api/vauth/role
time curl -H "Accept: application/json" --cookie 'token=60cc5dfb951afff9afa92f880482e57b' -i http://localhost:9960/v1/api/vauth/role/0/100
time curl -H "Accept: application/json" --cookie 'token=60cc5dfb951afff9afa92f880482e57b' -i http://localhost:9960/v1/api/vauth/role/1
time curl -H "Accept: application/json" --cookie 'token=60cc5dfb951afff9afa92f880482e57b' -i http://localhost:9960/v1/api/vauth/role/count
time curl -H "Accept: application/json" --cookie 'token=60cc5dfb951afff9afa92f880482e57b' -i http://localhost:9960/v1/api/vauth/role/external_id/vauth_admin

# 6 group
time curl -X POST -H "Content-Type: application/json" -H "Accept:application/json" -d '{"externalId":"grupka"}' --cookie 'token=60cc5dfb951afff9afa92f880482e57b' -i http://localhost:9960/v1/api/vauth/group
time curl -X PUT -H "Content-Type: application/json" -H "Accept:application/json" -d '{"externalId" : "grupka-upp", "enabled" : true}' --cookie 'token=60cc5dfb951afff9afa92f880482e57b' -i http://localhost:9960/v1/api/vauth/group/1
time curl -X DELETE -H "Accept: application/json" --cookie 'token=60cc5dfb951afff9afa92f880482e57b' -i http://localhost:9960/v1/api/vauth/group/251

time curl -H "Accept: application/json" --cookie 'token=60cc5dfb951afff9afa92f880482e57b' -i http://localhost:9960/v1/api/vauth/group
time curl -H "Accept: application/json" --cookie 'token=60cc5dfb951afff9afa92f880482e57b' -i http://localhost:9960/v1/api/vauth/group/0/100
time curl -H "Accept: application/json" --cookie 'token=60cc5dfb951afff9afa92f880482e57b' -i http://localhost:9960/v1/api/vauth/group/1
time curl -H "Accept: application/json" --cookie 'token=60cc5dfb951afff9afa92f880482e57b' -i http://localhost:9960/v1/api/vauth/group/count
time curl -H "Accept: application/json" --cookie 'token=60cc5dfb951afff9afa92f880482e57b' -i http://localhost:9960/v1/api/vauth/group/external_id/grupka-upp

# 7 group role

time curl -H "Accept: application/json" --cookie 'token=60cc5dfb951afff9afa92f880482e57b' -i http://localhost:9960/v1/api/vauth/group/1/role
time curl -X POST -H "Accept: application/json" --cookie 'token=60cc5dfb951afff9afa92f880482e57b' -i http://localhost:9960/v1/api/vauth/group/1/role/1
time curl -X DELETE -H "Accept: application/json" --cookie 'token=60cc5dfb951afff9afa92f880482e57b' -i http://localhost:9960/v1/api/vauth/group/1/role/1

# 8 user
time curl -X POST -H "Content-Type: application/json" -H "Accept:application/json" -d '{"externalId":"viltenfirst","password":"quentin1979"}' --cookie 'token=60cc5dfb951afff9afa92f880482e57b' -i http://localhost:9960/v1/api/vauth/user
time curl -X PUT -H "Content-Type: application/json" -H "Accept:application/json" -d '{"enabled" : true, "id" : 2, "externalId" : "vilten"}' --cookie 'token=60cc5dfb951afff9afa92f880482e57b' -i http://localhost:9960/v1/api/vauth/user/2
time curl -X DELETE -H "Accept: application/json" --cookie 'token=60cc5dfb951afff9afa92f880482e57b' -i http://localhost:9960/v1/api/vauth/user/2
time curl -X POST -H "Content-Type: application/x-www-form-urlencoded" --data 'old_password=pass&new_password=pass' --cookie 'token=60cc5dfb951afff9afa92f880482e57b' -i http://localhost:9960/v1/api/vauth/user/2/pass

time curl -H "Accept: application/json" --cookie 'token=60cc5dfb951afff9afa92f880482e57b' -i http://localhost:9960/v1/api/vauth/user
time curl -H "Accept: application/json" --cookie 'token=60cc5dfb951afff9afa92f880482e57b' -i http://localhost:9960/v1/api/vauth/user/0/100
time curl -H "Accept: application/json" --cookie 'token=60cc5dfb951afff9afa92f880482e57b' -i http://localhost:9960/v1/api/vauth/user/1
time curl -H "Accept: application/json" --cookie 'token=60cc5dfb951afff9afa92f880482e57b' -i http://localhost:9960/v1/api/vauth/user/count
time curl -H "Accept: application/json" --cookie 'token=60cc5dfb951afff9afa92f880482e57b' -i http://localhost:9960/v1/api/vauth/user/external_id/admin

# 9 uservalue
time curl -X POST -H "Content-Type: application/json" -H "Accept:application/json" -d '{}' --cookie 'token=60cc5dfb951afff9afa92f880482e57b' -i http://localhost:9960/v1/api/vauth/uservalue/user/1/property/1
time curl -X PUT -H "Content-Type: application/json" -H "Accept:application/json" -d '{"uservalue":"false"}' --cookie 'token=60cc5dfb951afff9afa92f880482e57b' -i http://localhost:9960/v1/api/vauth/uservalue/1/user/1/property/1
time curl -X DELETE -H "Accept: application/json" --cookie 'token=60cc5dfb951afff9afa92f880482e57b' -i http://localhost:9960/v1/api/vauth/uservalue/1

time curl -H "Accept: application/json" --cookie 'token=60cc5dfb951afff9afa92f880482e57b' -i http://localhost:9960/v1/api/vauth/uservalue
time curl -H "Accept: application/json" --cookie 'token=60cc5dfb951afff9afa92f880482e57b' -i http://localhost:9960/v1/api/vauth/uservalue/0/100
time curl -H "Accept: application/json" --cookie 'token=60cc5dfb951afff9afa92f880482e57b' -i http://localhost:9960/v1/api/vauth/uservalue/1
time curl -H "Accept: application/json" --cookie 'token=60cc5dfb951afff9afa92f880482e57b' -i http://localhost:9960/v1/api/vauth/uservalue/count
time curl -H "Accept: application/json" --cookie 'token=60cc5dfb951afff9afa92f880482e57b' -i http://localhost:9960/v1/api/vauth/uservalue/user_id/1
time curl -H "Accept: application/json" --cookie 'token=60cc5dfb951afff9afa92f880482e57b' -i http://localhost:9960/v1/api/vauth/uservalue/property_id/1

# 10 user role
time curl -H "Accept: application/json" --cookie 'token=60cc5dfb951afff9afa92f880482e57b' -i http://localhost:9960/v1/api/vauth/user/1/role
time curl -X POST -H "Accept: application/json" --cookie 'token=60cc5dfb951afff9afa92f880482e57b' -i http://localhost:9960/v1/api/vauth/user/1/role/2
time curl -X DELETE -H "Accept: application/json" --cookie 'token=60cc5dfb951afff9afa92f880482e57b' -i http://localhost:9960/v1/api/vauth/user/1/role/2

# 11 user group
time curl -H "Accept: application/json" --cookie 'token=60cc5dfb951afff9afa92f880482e57b' -i http://localhost:9960/v1/api/vauth/user/1/group
time curl -X POST -H "Accept: application/json" --cookie 'token=60cc5dfb951afff9afa92f880482e57b' -i http://localhost:9960/v1/api/vauth/user/1/group/1
time curl -X DELETE -H "Accept: application/json" --cookie 'token=60cc5dfb951afff9afa92f880482e57b' -i http://localhost:9960/v1/api/vauth/user/1/group/1

# 12 actCode
time curl -X POST -H "Content-Type: application/json" -H "Accept:application/json" -d '{}' --cookie 'token=60cc5dfb951afff9afa92f880482e57b' -i http://localhost:9960/v1/api/vauth/actCode/user/1
time curl -X PUT -H "Content-Type: application/json" -H "Accept:application/json" -d '{}' --cookie 'token=60cc5dfb951afff9afa92f880482e57b' -i http://localhost:9960/v1/api/vauth/actCode/1/user/1
time curl -X DELETE -H "Accept: application/json" --cookie 'token=60cc5dfb951afff9afa92f880482e57b' -i http://localhost:9960/v1/api/vauth/actCode/1

time curl -H "Accept: application/json" --cookie 'token=60cc5dfb951afff9afa92f880482e57b' -i http://localhost:9960/v1/api/vauth/actCode
time curl -H "Accept: application/json" --cookie 'token=60cc5dfb951afff9afa92f880482e57b' -i http://localhost:9960/v1/api/vauth/actCode/0/100
time curl -H "Accept: application/json" --cookie 'token=60cc5dfb951afff9afa92f880482e57b' -i http://localhost:9960/v1/api/vauth/actCode/1
time curl -H "Accept: application/json" --cookie 'token=60cc5dfb951afff9afa92f880482e57b' -i http://localhost:9960/v1/api/vauth/actCode/count
time curl -H "Accept: application/json" --cookie 'token=60cc5dfb951afff9afa92f880482e57b' -i http://localhost:9960/v1/api/vauth/actCode/act_code/362fc94f5066ab2463a1358b3b2a0063
time curl -H "Accept: application/json" --cookie 'token=60cc5dfb951afff9afa92f880482e57b' -i http://localhost:9960/v1/api/vauth/actCode/user_id/1

# 13 authCode
time curl -X POST -H "Content-Type: application/json" -H "Accept:application/json" -d '{"redirectUrl":"http://localhost"}' --cookie 'token=e6408841881e42e8428db4dcf59d62b2' -i http://localhost:9960/v1/api/vauth/authCode/user/1/application/1
time curl -X PUT -H "Content-Type: application/json" -H "Accept:application/json" -d '{"redirectUrl":"http://localhost2"}' --cookie 'token=e6408841881e42e8428db4dcf59d62b2' -i http://localhost:9960/v1/api/vauth/authCode/2/user/1/application/1
time curl -X DELETE -H "Accept: application/json" --cookie 'token=e6408841881e42e8428db4dcf59d62b2' -i http://localhost:9960/v1/api/vauth/authCode/2

time curl -H "Accept: application/json" --cookie 'token=e6408841881e42e8428db4dcf59d62b2' -i http://localhost:9960/v1/api/vauth/authCode
time curl -H "Accept: application/json" --cookie 'token=e6408841881e42e8428db4dcf59d62b2' -i http://localhost:9960/v1/api/vauth/authCode/0/100
time curl -H "Accept: application/json" --cookie 'token=e6408841881e42e8428db4dcf59d62b2' -i http://localhost:9960/v1/api/vauth/authCode/1
time curl -H "Accept: application/json" --cookie 'token=e6408841881e42e8428db4dcf59d62b2' -i http://localhost:9960/v1/api/vauth/authCode/count
time curl -H "Accept: application/json" --cookie 'token=e6408841881e42e8428db4dcf59d62b2' -i http://localhost:9960/v1/api/vauth/authCode/auth_code/ee3322d59fb6748fe351f373aefda321
time curl -H "Accept: application/json" --cookie 'token=e6408841881e42e8428db4dcf59d62b2' -i http://localhost:9960/v1/api/vauth/authCode/user_id/1
time curl -H "Accept: application/json" --cookie 'token=e6408841881e42e8428db4dcf59d62b2' -i http://localhost:9960/v1/api/vauth/authCode/application/1

# 14 token
time curl -X POST -H "Content-Type: application/json" -H "Accept:application/json" -d '{}' --cookie 'token=e6408841881e42e8428db4dcf59d62b2' -i http://localhost:9960/v1/api/vauth/token/user/1/application/1
time curl -X PUT -H "Content-Type: application/json" -H "Accept:application/json" -d '{}' --cookie 'token=e6408841881e42e8428db4dcf59d62b2' -i http://localhost:9960/v1/api/vauth/token/1/user/1/application/1
time curl -X DELETE -H "Accept: application/json" --cookie 'token=e6408841881e42e8428db4dcf59d62b2' -i http://localhost:9960/v1/api/vauth/token/1

time curl -H "Accept: application/json" --cookie 'token=e6408841881e42e8428db4dcf59d62b2' -i http://localhost:9960/v1/api/vauth/token
time curl -H "Accept: application/json" --cookie 'token=e6408841881e42e8428db4dcf59d62b2' -i http://localhost:9960/v1/api/vauth/token/0/100
time curl -H "Accept: application/json" --cookie 'token=e6408841881e42e8428db4dcf59d62b2' -i http://localhost:9960/v1/api/vauth/token/2
time curl -H "Accept: application/json" --cookie 'token=e6408841881e42e8428db4dcf59d62b2' -i http://localhost:9960/v1/api/vauth/token/count
time curl -H "Accept: application/json" --cookie 'token=e6408841881e42e8428db4dcf59d62b2' -i http://localhost:9960/v1/api/vauth/token/token/e6408841881e42e8428db4dcf59d62b2
time curl -H "Accept: application/json" --cookie 'token=e6408841881e42e8428db4dcf59d62b2' -i http://localhost:9960/v1/api/vauth/token/expiration_token/34c7d94611b7597981bc12122b066dea
time curl -H "Accept: application/json" --cookie 'token=e6408841881e42e8428db4dcf59d62b2' -i http://localhost:9960/v1/api/vauth/token/user_id/1
time curl -H "Accept: application/json" --cookie 'token=e6408841881e42e8428db4dcf59d62b2' -i http://localhost:9960/v1/api/vauth/token/application/1

