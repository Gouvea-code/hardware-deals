#!/bin/sh
set -eu
: "${POSTGRES_HOST:?}" "${POSTGRES_DB:?}" "${POSTGRES_USER:?}" "${PGPASSWORD:?}" "${BACKUP_DIR:?}"
timestamp="$(date -u +%Y%m%dT%H%M%SZ)"
mkdir -p "$BACKUP_DIR"
pg_dump --host "$POSTGRES_HOST" --username "$POSTGRES_USER" --format=custom --file "$BACKUP_DIR/$POSTGRES_DB-$timestamp.dump" "$POSTGRES_DB"
find "$BACKUP_DIR" -type f -name '*.dump' -mtime "+${BACKUP_RETENTION_DAYS:-30}" -delete
