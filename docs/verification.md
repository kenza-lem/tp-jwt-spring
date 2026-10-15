# Vérifications de la version alignée sur le PDF

Exécution réelle du 15 octobre 2026, Windows et OpenJDK 25.

| Contrôle | Résultat |
|---|---|
| Maven verify | BUILD SUCCESS, JAR créé |
| Tests Java | 9 tests exécutés, 0 échec, 0 erreur |
| Collection Postman exécutée avec Newman 6.2.3 | 14 requêtes, 20 assertions, 0 échec |

Les tests couvrent les réponses du support : inscription 200 « Utilisateur cree »,
doublon 400 « Email deja utilise », connexion avec JWT, hello et profil texte,
accès sans token, mauvaise signature, expiration et utilisateur inconnu.
Les routes /api/** demandent une authentification et non un rôle spécifique.

Sources nettoyées : reports/java-summary.json et reports/newman-summary.json.
Le serveur du JAR a été lancé avec Newman dans le même environnement local,
sur le port 18081. Il a été arrêté après les tests. Aucun secret ou JWT réel
n’est présent dans les rapports exportés.

Les captures personnelles de l’utilisateur dans Postman ne sont pas encore réalisées.
Le guide docs/guide-postman.md décrit les manipulations et les captures à produire.
Les anciennes captures et preuves (version précédente) sont archivées sous
docs/archives/avant-partie2/ ; elles ne sont pas présentées comme preuve de cette version.
