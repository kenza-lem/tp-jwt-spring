# Vérifications exécutées le 15 octobre 2026

## Environnement et périmètre

Windows, OpenJDK 25, code Java niveau 17, Spring Boot 4.1.1,
Maven Wrapper 3.10.0 et Newman 6.2.3.

## Résultats

| Vérification | Résultat constaté |
|---|---|
| Maven verify | BUILD SUCCESS, JAR exécutable produit |
| Tests JUnit | 8 exécutés, 0 échec, 0 erreur |
| Démarrage du JAR avec `.env` local | Serveur démarré sur un port de test |
| Installation depuis une copie propre | Configuration locale générée, 8 tests réussis, JAR produit |
| Collection Newman v2.1 | 12 requêtes exécutées, 17 assertions, 0 échec |

Les données de compte de la collection sont générées pour chaque exécution.
Les tests Java utilisent une clé HMAC temporaire en mémoire. Les rapports
publics ne contiennent ni cette clé, ni la clé locale, ni les JWT reçus,
ni les mots de passe temporaires.

La première tentative Newman sur un serveur lancé séparément a échoué par
timeouts de connexion (29 erreurs de requête/assertion). Aucun résultat de cette
tentative n’est présenté comme un succès. Le serveur et Newman ont ensuite été
lancés ensemble dans le même environnement local : les 12 requêtes et les
17 assertions ont réussi. Aucun changement du code API n’a été nécessaire
pour corriger cet échec de communication.

## Sources des preuves

- `reports/java-summary.json` : extrait nettoyé des rapports Surefire JUnit.
- `reports/newman-summary.json` : réponses et assertions réelles, JWT masqué.
- `reports/test-report.html` : présentation de ces données.
- `screenshots/*.png` : captures de ce rapport rendu dans Chromium.

Le backend ne possède pas d’interface graphique. Les captures présentent les
résultats API ; aucune capture de Postman n’est fabriquée. La collection a été
exécutée avec Newman, pas dans l’application Postman.
