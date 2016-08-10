#!/usr/bin/env bash

# Exit immediately if any commands return non-zero
set -e

NAME=$APP_NAME

cf api $CF_API_DEV &> /dev/null
cf auth $CF_USER_DEV $CF_PASSWORD_DEV &> /dev/null
cf target -o $CF_ORG_DEV &> /dev/null
cf target -s $CF_SPACE_DEV &> /dev/null

cf push "$NAME"-blue &> /dev/null
cf map-route "$NAME"-blue $CF_APP_URL_DEV -n $NAME &> /dev/null

cf push "$NAME"-green &> /dev/null
cf map-route "$NAME"-green $CF_APP_URL_DEV -n $NAME &> /dev/null