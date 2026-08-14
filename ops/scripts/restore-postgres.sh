#!/bin/sh
set -eu
: "${POSTGRES_HOST:?}" "${POSTGRES_DB:?}" "${POSTGRES_USER:?}" "${PGPASSWORD:?}"
if [ "$#" -ne 1 ] || [ ! -f "$1" ]; then echo "usage: restore-postgres.sh backup.dump" >&2; exit 2; fi
pg_restore --host "$POSTGRES_HOST" --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" --clean --if-exists --exit-on-error "$1"
