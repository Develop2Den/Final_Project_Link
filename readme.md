### what is running now
```shell
docker ps
```

### stop container
```shell
docker stop {container-name}
```

### start container (docker-compose.yaml) DON'T CLOSE
```shell
docker-compose up
```

### jump into container (pg_2)
```shell
docker exec -ti step_2 bash
```

### inside container:

#### become postgres user
```shell
su user_tinder
```

#### create database (fs8)
```shell
createdb DB_tinder
```

### exit from user postgres
```shell
exit
```

### exit from container
```shell
exit

### text
spring.security.oauth2.client.registration.google.client-id=C143568406207-v91aosc5q8i018vp87n1j34uhgofvvuo.apps.googleusercontent.com
spring.security.oauth2.client.registration.google.client-secret=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
```



