#!/usr/bin/env bash

# Exit immediately if any commands return non-zero
set -e

NAME=$APP_NAME
echo "App name is $NAME"
cf api $CF_API_DEV &> /dev/null
echo "Tyring to authenticate "
cf auth $CF_USER_DEV $CF_PASSWORD_DEV &> /dev/null
echo "Logged in succssfuly, trying to setting org target"
cf target -o $CF_ORG_DEV &> /dev/null
cf target -s $CF_SPACE_DEV &> /dev/null
echo "Target set, trying to push to cf"
cf push "$NAME"-blue &> /dev/null
cf map-route "$NAME"-blue $CF_APP_URL_DEV -n $NAME &> /dev/null
echo "cf deployed to blue,trying green.."

cf push "$NAME"-green &> /dev/null
cf map-route "$NAME"-green $CF_APP_URL_DEV -n $NAME &> /dev/null
echo "cf deployed to green"
