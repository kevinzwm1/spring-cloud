#!/bin/bash
if test -z "$MONGODB_PASSWORD"; then
    echo "MONGODB_PASSWORD not defined"
    exit 1
fi

auth="-u kevin1 -p $MONGODB_PASSWORD"

# MONGODB USER CREATION
(
echo "setup mongodb auth"
create_user="if (!db.getUser('kevin1')) { db.createUser({ user: 'kevin1', pwd: '$MONGODB_PASSWORD', roles: [ {role:'dbOwner', db:'test'} ]}) }"
until mongo piggymetrics --eval "$create_user" || mongo piggymetrics $auth --eval "$create_user"; do sleep 5; done
killall mongod
sleep 1
killall -9 mongod
) &

# INIT DUMP EXECUTION
(
if test -n "$INIT_DUMP"; then
    echo "execute dump file"
	until mongo piggymetrics $auth $INIT_DUMP; do sleep 5; done
fi
) &

echo "start mongodb without auth"
chown -R mongodb /data/db
gosu mongodb mongod "$@"

echo "restarting with auth on"
sleep 5
exec gosu mongodb mongod --auth "$@"