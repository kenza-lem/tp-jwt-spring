#!/usr/bin/env sh
set -eu
cd "$(dirname "$0")/.."
if [ -f .env ]; then
  echo '.env existe deja ; configuration conservee.'
  exit 0
fi
umask 077
printf 'JWT_SECRET=%s\nJWT_EXPIRATION=3600000\nSERVER_PORT=8080\nH2_CONSOLE_ENABLED=false\nSHOW_SQL=false\n' "$(openssl rand -base64 48 | tr -d '\n')" > .env
echo 'Configuration locale creee dans .env (exclu de Git).'
